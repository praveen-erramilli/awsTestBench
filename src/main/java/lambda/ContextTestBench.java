package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

    public class ContextTestBench implements RequestHandler<ContextTestBench.Request, ContextTestBench.Response> {
    @Override
    public Response handleRequest(Request request, Context context) {
        System.out.println("Testing Context Object");
        System.out.println("RemainingTime : " + context.getRemainingTimeInMillis());
        System.out.println("FunctionName : " + context.getFunctionName());
        System.out.println("Function Version: "+ context.getFunctionVersion());
        System.out.println("Arn :" + context.getInvokedFunctionArn());
        System.exit(0);
        System.out.println("MemoryLimit :"+context.getMemoryLimitInMB());
        context.getLogger().log("This is the test for context.getLogger() method");

        return new Response();
    }

    static class Request
    {

    }

    static class Response
    {

    }
}