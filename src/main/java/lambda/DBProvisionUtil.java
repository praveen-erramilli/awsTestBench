package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DBProvisionUtil implements RequestStreamHandler {

    static final String STACK_NAME = "MySQLCreatorStack";

    @Override
    public void handleRequest(InputStream requestInputStream, OutputStream responseStream, Context context) throws IOException{

        JSONObject jsonObject = getInputJSONData(requestInputStream);
        System.out.println("::RequestInput:: "+jsonObject.toString());

        CloudFormationClient client = CloudFormationClient.builder()
                .region(Region.US_EAST_2)
                .build();

        String template = convertStreamToString(DBProvisionUtil.class.getClassLoader().getResourceAsStream("template.json"));

        JSONObject templateJSON = new JSONObject(template);

        String modifiedTemplate = templateJSON.toString();

        JSONObject input = jsonObject.getJSONObject("input");
        List<Parameter> paramsList = getTemplateParameters(input);

        JSONObject resourceProperties = new JSONObject();
        resourceProperties.put("MasterUsername", input.optString("DBUser", "admin"));
        resourceProperties.put("MasterUserPassword", input.optString("DBPassword", "praveene"));
        resourceProperties.put("DBInstanceIdentifier", "praveendbinstance");

        paramsList.add(Parameter.builder().parameterKey("Token").parameterValue(jsonObject.getString("token")).build());
        paramsList.add(Parameter.builder().parameterKey("Output").parameterValue(resourceProperties.toString()).build());

        CreateStackRequest createRequest = CreateStackRequest.builder()
                .stackName(STACK_NAME)
                .templateBody(modifiedTemplate)
                .parameters(paramsList)
                .build();
        client.createStack(createRequest);

        client.close();

        try (Writer w = new OutputStreamWriter(responseStream, "UTF-8")) {
            w.write(jsonObject.toString());
        }
    }

    static JSONObject getInputJSONData(InputStream requestInputStream) throws IOException {
        StringWriter eventWriter = new StringWriter();
        IOUtils.copy(requestInputStream, eventWriter, "UTF-8");
        return new JSONObject(eventWriter.toString());
    }

    private List<Parameter> getTemplateParameters(JSONObject jsonObject) {
        List<Parameter> paramsList = new ArrayList<>();
        String allocatedStorage = jsonObject.optString("DBAllocatedStorage",null);
        if (allocatedStorage != null) {
            paramsList.add(Parameter.builder().parameterKey("DBAllocatedStorage").parameterValue(allocatedStorage).build());
        }
        String dbInstanceClass = jsonObject.optString("DBClass",null);
        if (dbInstanceClass != null) {
            paramsList.add(Parameter.builder().parameterKey("DBClass").parameterValue(dbInstanceClass).build());
        }
        String dbName = jsonObject.optString("DBName",null);
        if (dbName != null) {
            paramsList.add(Parameter.builder().parameterKey("DBName").parameterValue(dbName).build());
        }
        String masterUsername = jsonObject.optString("DBUser",null);
        if (masterUsername != null) {
            paramsList.add(Parameter.builder().parameterKey("DBUser").parameterValue(masterUsername).build());
        }
        String masterUserPassword = jsonObject.optString("DBPassword",null);
        if (masterUserPassword != null) {
            paramsList.add(Parameter.builder().parameterKey("DBPassword").parameterValue(masterUserPassword).build());
        }
        return paramsList;
    }

    // Convert a stream into a single, newline separated string
    public static String convertStreamToString(InputStream in) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringbuilder = new StringBuilder();
        String line = null;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            stringbuilder.append(line + "\n");
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringbuilder.toString();
    }
}
