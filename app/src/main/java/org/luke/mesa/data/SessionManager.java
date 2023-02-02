package org.luke.mesa.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.api.API;
import org.luke.mesa.abs.utils.Threaded;
import org.luke.mesa.data.beans.User;

import java.net.URISyntaxException;
import java.util.prefs.Preferences;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SessionManager {
    private static final String ACCESS_TOKEN = "access_token";

    private static SharedPreferences prefs;

    public static void init(App owner) {
        prefs = PreferenceManager.getDefaultSharedPreferences(owner);
    }

    //	private static HashMap<String, String> data = new HashMap<>();
    private SessionManager() {

    }

    public static void put(String key, String value) {
        if(prefs == null) throw new IllegalStateException("the preferences object hasn't been initialized");
        prefs.edit().putString(key,value).apply();
//		data.put(key, value);
    }

    public static String get(String key) {
        if(prefs == null) throw new IllegalStateException("the preferences object hasn't been initialized");
        return prefs.getString(key, null);
//		return data.get(key);
    }

    public static void registerSocket(Socket socket, String token, String uid) {
        Runnable register = () -> socket.emit("register",
                JsonUtils.make("socket", socket.id(), "token", token, User.USER_ID, uid));

        System.out.println("listening for reconnect...");
        socket.io().on("reconnect", data -> new Thread(() -> {
            Threaded.waitWhile(() -> socket.id() == null);
            System.out.println("reconnecting");
            register.run();
        }).start());
        register.run();
    }

    public static void storeSession(String token, App owner, String uid) throws URISyntaxException {
        put(ACCESS_TOKEN, token);
        Socket socket = owner.getMainSocket();
        registerSocket(socket, token, uid);
    }

    public static String getSession() {
        return get(ACCESS_TOKEN);
    }

    public static void clearSession() {
        if(prefs == null) throw new IllegalStateException("the preferences object hasn't been initialized");
        prefs.edit().remove(ACCESS_TOKEN).apply();
//		data.remove(ACCESS_TOKEN);
    }
}
