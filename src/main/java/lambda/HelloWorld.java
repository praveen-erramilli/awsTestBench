package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class HelloWorld implements RequestHandler<HelloWorld.RequestClass, HelloWorld.ResponseClass> {

    @Override
    public ResponseClass handleRequest(RequestClass req, Context context) {
        ResponseClass responseClass = new ResponseClass();
        responseClass.greeting = "HelloWorld "+ req.firstName + req.lastName;
        return responseClass;
    }


    public static class RequestClass
    {
        private String firstName;
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

    public static class ResponseClass
    {
        private String greeting;

        public String getGreeting() {
            return greeting;
        }

        public void setGreeting(String greeting) {
            this.greeting = greeting;
        }
    }
}