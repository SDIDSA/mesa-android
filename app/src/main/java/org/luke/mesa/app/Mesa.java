package org.luke.mesa.app;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.api.API;
import org.luke.mesa.abs.api.Session;
import org.luke.mesa.abs.components.Page;
import org.luke.mesa.abs.utils.DataUtils;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.app.pages.session.SessionPage;
import org.luke.mesa.app.pages.welcome.Welcome;
import org.luke.mesa.data.SessionManager;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Mesa extends App {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postCreate();
    }

    @Override
    protected void postInit() {
        super.postInit();
    }

    protected void postCreate() {
        new Thread(() -> {
            SessionManager.init(this);
            DataUtils.readCountryCodes(this);
            Page.clearCache();

            try {
                Socket socket = IO.socket(API.BASE);
                socket.connect();

                putMainSocket(socket);
            } catch (URISyntaxException x) {
                ErrorHandler.handle(x, "init socket");
            }

            Runnable loadLogin = () -> loadPage(Welcome.class);

            String token = SessionManager.getSession();
            if (token == null) {
                Platform.runAfter(loadLogin, 2000);
            } else {
                Session.getUser(result -> {
                    if (result.has("user")) {
                        Session.getServers(servers -> {
                            JSONObject userJson = result.getJSONObject("user");
                            JSONArray servarr = servers.getJSONArray("servers");
                            putServers(servarr);
                            putData("user", userJson);

                            SessionManager.registerSocket(getMainSocket(), token, userJson.getString("id"));

                            Platform.runAfter(() -> loadPage(SessionPage.class), 2000);
                        });
                    } else {
                        SessionManager.clearSession();
                        loadLogin.run();
                    }
                });
            }
        }).start();
    }
}