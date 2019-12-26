package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DBProvisionUtil implements RequestStreamHandler {

    static final String STACK_NAME = "MySQLCreatorStack2";

    @Override
    public void handleRequest(InputStream requestInputStream, OutputStream responseStream, Context context) throws IOException{

        StringWriter eventWriter = new StringWriter();
        IOUtils.copy(requestInputStream, eventWriter, "UTF-8");
        String inpString = eventWriter.toString();
        String body = new JSONObject(inpString).getString("body");
        JSONObject jsonObject = new JSONObject(body);
        System.out.println("::RequestInput:: "+jsonObject.toString());

        CloudFormationClient client = CloudFormationClient.builder()
                .region(Region.US_EAST_2)
                .build();

        String template = convertStreamToString(DBProvisionUtil.class.getClassLoader().getResourceAsStream("template.json"));

        JSONObject templateJSON = new JSONObject(template);
        JSONObject properties = templateJSON.getJSONObject("Resources").getJSONObject("DB").getJSONObject("Properties");

        setInputValuesInToTemplateData(jsonObject, properties);

        String modifiedTemplate = templateJSON.toString();

        System.out.println("::ModifiedTemplate:: "+modifiedTemplate);

        CreateStackRequest createRequest = CreateStackRequest.builder()
                .stackName(STACK_NAME)
                .templateBody(modifiedTemplate)
                .build();
        client.createStack(createRequest);

        client.close();

        JSONObject output = new JSONObject();
        output.put("statusCode", 200);
        output.put("body", jsonObject.toString());
        output.put("isBase64Encoded", false);
        output.put("headers", new HashMap<>());
        try (Writer w = new OutputStreamWriter(responseStream, "UTF-8")) {
            w.write(output.toString());
        }
    }

    private void setInputValuesInToTemplateData(JSONObject requestClass, JSONObject properties) {
        String allocatedStorage = requestClass.optString("allocatedStorage",null);
        if (allocatedStorage != null) {
            properties.put("AllocatedStorage", allocatedStorage);
        }
        String dbInstanceClass = requestClass.optString("dBInstanceClass",null);
        if (dbInstanceClass != null) {
            properties.put("DBInstanceClass", dbInstanceClass);
        }
        String dbName = requestClass.optString("dBName",null);
        if (dbInstanceClass != null) {
            properties.put("DBName", dbName);
        }
        String engine = requestClass.optString("engine",null);
        if (engine != null) {
            properties.put("Engine", engine);
        }
        String masterUsername = requestClass.optString("masterUsername",null);
        if (masterUsername != null) {
            properties.put("MasterUsername", masterUsername);
        }
        String masterUserPassword = requestClass.optString("masterUserPassword",null);
        if (masterUserPassword != null) {
            properties.put("MasterUserPassword", masterUserPassword);
        }
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
