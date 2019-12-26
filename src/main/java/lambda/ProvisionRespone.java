package lambda;

import java.util.Map;

public class ProvisionRespone {
    int statusCode;
    Map<String,String> headers;
    Body body;
    boolean isBase64Encoded = false;

    public ProvisionRespone(int statusCode, Map<String, String> headers, Body body, boolean isBase64Encoded) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.isBase64Encoded = isBase64Encoded;
    }

    public ProvisionRespone() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String,String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String,String> headers) {
        this.headers = headers;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public boolean isBase64Encoded() {
        return isBase64Encoded;
    }

    public void setBase64Encoded(boolean base64Encoded) {
        isBase64Encoded = base64Encoded;
    }

    @Override
    public String toString() {
        return "ProvisionRespone{" +
                "statusCode=" + statusCode +
                ", headers=" + headers +
                ", body=" + body +
                ", isBase64Encoded=" + isBase64Encoded +
                '}';
    }

    static class Body{
        String message;

        public Body(String message) {
            this.message = message;
        }

        public Body() {
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }
}
