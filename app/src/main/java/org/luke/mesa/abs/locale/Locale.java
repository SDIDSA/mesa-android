package org.luke.mesa.abs.locale;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.Assets;

public class Locale {
	private final HashMap<String, String> values;

	public Locale(App owner, String name) {
		values = new HashMap<>();
		String file = Assets.readAsset(owner, "locales/".concat(name).concat(".json"));
		try {
			JSONObject obj = new JSONObject(file);

			Iterator<String> keys = obj.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				values.put(key, obj.getString(key));
			}
		}catch(JSONException x) {
			Log.e("read locale", "failed to read locale ".concat(name), x);
		}
	}

	public String get(String key) {
		String found = values.get(key);

		if (found == null) {
			found = key;
		}

		return found;
	}
}
