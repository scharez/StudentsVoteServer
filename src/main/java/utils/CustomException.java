package utils;

import org.json.JSONObject;

public class CustomException {

    public CustomException() {

    }

    public String buildException(int code, String error, String message) {

        JSONObject ce = new JSONObject();

            ce.put("statusCode", code)
                    .put("error", error)
                    .put("message", message);

        return ce.toString();
    }
}
