package org.luke.mesa.abs.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.App;
import org.luke.mesa.data.CountryCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataUtils {
    private static List<CountryCode> countryCodes;

    public static List<CountryCode> readCountryCodes(App owner) {
        if (countryCodes == null) {
            countryCodes = new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(Objects.requireNonNull(Assets.readAsset(owner, "countries.json")));
                JSONArray arr = obj.getJSONArray("countryCodes");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject countryObj = (JSONObject) arr.get(i);
                    countryCodes.add(
                            new CountryCode(countryObj.getString("country_name"), countryObj.getString("country_code"), countryObj.getString("dialling_code")));
                }
            } catch (JSONException e) {
                Log.e("reading country codes", "failed to read country codes", e);
            }
        }
        return countryCodes;
    }
}
