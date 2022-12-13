package org.luke.mesa.data;

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

    //	private static HashMap<String, String> data = new HashMap<>();
    private SessionManager() {

    }

    public static void put(String key, String value) {
        Preferences.userRoot().put(key, value);
//		data.put(key, value);
    }

    public static String get(String key) {
        return Preferences.userRoot().get(key, null);
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
        if(socket == null) {
            socket = IO.socket(API.BASE);
            socket.connect();

            owner.putMainSocket(socket);
        }
        registerSocket(socket, token, uid);
    }

    public static String getSession() {
        return get(ACCESS_TOKEN);
    }

    public static void clearSession() {
        Preferences.userRoot().remove(ACCESS_TOKEN);
//		data.remove(ACCESS_TOKEN);
    }
}
