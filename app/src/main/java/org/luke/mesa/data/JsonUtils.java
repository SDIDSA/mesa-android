package org.luke.mesa.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.utils.ErrorHandler;

public class JsonUtils {
    private JsonUtils() {

    }

    public static JSONObject make(String... strings) {
        if (strings.length % 2 == 1) {
            throw new IllegalArgumentException("must be called with an even number of args");
        }

        JSONObject obj = new JSONObject();

        for (int i = 0; i < strings.length; i += 2) {
            try {
                obj.put(strings[i], strings[i + 1]);
            } catch (JSONException e) {
                ErrorHandler.handle(e, "making json");
            }
        }

        return obj;
    }
}