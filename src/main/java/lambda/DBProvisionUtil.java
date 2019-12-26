package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.json.JSONObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class DBProvisionUtil implements RequestHandler<ProvisionRequest, APIGatewayProxyResponseEvent> {

    static final String STACK_NAME = "MySQLCreatorStack2";

    @Override
    public APIGatewayProxyResponseEvent handleRequest(ProvisionRequest requestClass, Context context) {
        System.out.println("::RequestInput:: "+requestClass);

        CloudFormationClient client = CloudFormationClient.builder()
                .region(Region.US_EAST_2)
                .build();

        String template = convertStreamToString(DBProvisionUtil.class.getClassLoader().getResourceAsStream("template.json"));

        JSONObject templateJSON = new JSONObject(template);
        JSONObject properties = templateJSON.getJSONObject("Resources").getJSONObject("DB").getJSONObject("Properties");

        setInputValuesInToTemplateData(requestClass, properties);

        String modifiedTemplate = templateJSON.toString();

        System.out.println("::ModifiedTemplate:: "+modifiedTemplate);

        CreateStackRequest createRequest = CreateStackRequest.builder()
                .stackName(STACK_NAME)
                .templateBody(modifiedTemplate)
                .build();
        client.createStack(createRequest);

        client.close();

        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody("test---body");
        responseEvent.setStatusCode(200);

        ProvisionRespone responseClass = new ProvisionRespone();
        responseClass.statusCode = 200;

        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        responseClass.setHeaders(headers);

        return responseEvent;
    }

    private void setInputValuesInToTemplateData(ProvisionRequest requestClass, JSONObject properties) {
        String allocatedStorage = requestClass.getAllocatedStorage();
        if (allocatedStorage != null) {
            properties.put("AllocatedStorage", allocatedStorage);
        }
        String dbInstanceClass = requestClass.getdBInstanceClass();
        if (dbInstanceClass != null) {
            properties.put("DBInstanceClass", dbInstanceClass);
        }
        String dbName = requestClass.getdBName();
        if (dbInstanceClass != null) {
            properties.put("DBName", dbName);
        }
        String engine = requestClass.getEngine();
        if (engine != null) {
            properties.put("Engine", engine);
        }
        String masterUsername = requestClass.getMasterUsername();
        if (masterUsername != null) {
            properties.put("MasterUsername", masterUsername);
        }
        String masterUserPassword = requestClass.getMasterUserPassword();
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
