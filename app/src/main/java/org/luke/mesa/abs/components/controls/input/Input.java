package org.luke.mesa.abs.components.controls.input;

import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.data.observable.StringObservable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Input {
    HashMap<ViewGroup, List<Input>> inputMapCache = new HashMap<>();

    static List<Input> applyError(JSONObject res, ViewGroup parent) {
        ArrayList<Input> result = new ArrayList<>();
        try {
            JSONArray arr = res.getJSONArray("err");
            List<Input> inputs = mapInputs(parent);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String key = obj.getString("key");
                String val = obj.getString("value");
                for (Input input : inputs) {
                    if (input.getKey().equals(key)) {
                        result.add(input);
                        if (obj.has("plus")) {
                            String plus = obj.getString("plus");
                            input.setError(val, plus);
                        } else {
                            input.setError(val);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    static void clearError(ViewGroup parent) {
        for (Input input : mapInputs(parent)) {
            input.clearError();
        }
    }

    static List<Input> mapInputs(ViewGroup parent) {
        List<Input> inputs = inputMapCache.get(parent);
        if (inputs == null) {
            inputs = new ArrayList<>();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child instanceof Input) {
                    Input input = (Input) child;
                    inputs.add(input);
                } else if (child instanceof ViewGroup) {
                    inputs.addAll(mapInputs((ViewGroup) child));
                }
            }
            inputMapCache.put(parent, inputs);
        }
        return inputs;
    }

    void setError(String error, String plus);

    void setError(String error);

    void clearError();

    String getValue();

    void setValue(String value);

    StringObservable valueProperty();

    String getKey();
}
