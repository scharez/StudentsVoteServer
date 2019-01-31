package utils;

import org.json.JSONObject;

public class CustomException {

    public CustomException() {

    }

    public String buildException(int code, String message) {

        JSONObject user = new JSONObject();

            user.put("code", code)
                    .put("message", message);

        return user.toString();
    }
}
