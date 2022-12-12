package org.luke.mesa.abs.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.api.basic.BasicApiGet;
import org.luke.mesa.abs.api.json.JsonApiCall;
import org.luke.mesa.abs.api.json.Param;
import org.luke.mesa.abs.api.multipart.MultiPartApiCall;
import org.luke.mesa.abs.api.multipart.Part;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.functional.JsonConsumer;
import org.luke.mesa.abs.utils.functional.StringConsumer;

public class API {
    public static final String VERSION = "1.0.0";
    public static final String DEV_BASE = "http://10.0.2.2:4000/";
    public static final String REL_BASE = "https://mesa69.herokuapp.com/";
    public static final String BASE = DEV_BASE;
    public static final String INVITE_BASE = "https://mesa-invite.tk/";
    public static final String INVITE_CODE = "hTKzmak";
    public static JSONObject netErr;

    static {
        try {
            netErr = new JSONObject("{\"err\":[{\"key\":\"global\",\"value\":\"net_err\"}]}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private API() {

    }

    public static void asyncBasicGet(String path, String action, StringConsumer onResult,
                                     Param... params) {
        asyncExec(new BasicApiGet(path, params), action, res -> {
			try {
				onResult.accept(res.getString("body"));
			} catch (JSONException x) {
				x.printStackTrace();
				ErrorHandler.handle(x, action);
			}
		}, null);
    }

    public static void asyncJsonPost(String path, String action, JsonConsumer onResult, String session,
                                     Param... params) {
        asyncExec(new JsonApiCall(path, params), action, onResult, session);
    }

    public static void asyncMultiPost(String path, String action, JsonConsumer onResult, String session,
                                      Part... parts) {
        asyncExec(new MultiPartApiCall(path, parts), action, onResult, session);
    }

    private static void asyncExec(ApiCall call, String action, JsonConsumer onResult, String session) {
        new Thread() {
            @Override
            public void run() {
                try {
                    call.execute(result -> {
                        try {
                            Log.i("api response", result.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (onResult != null)
                            onResult.accept(result);
                    }, session);
                } catch (Exception x) {
                    x.printStackTrace();
                    ErrorHandler.handle(x, action);
                    Platform.runLater(() -> onResult.accept(netErr));
                }
            }
        }.start();
    }

    public static void asyncJsonPost(String path, String action, JsonConsumer onResult, Param... params) {
        asyncJsonPost(path, action, onResult, null, params);
    }

    public static class Auth {
        private static final String PREFIX = BASE + "auth/";

        public static final String LOGIN = PREFIX + "login";

        public static final String PHONE_OWN = PREFIX + "phone_own";

        public static final String EMAIL_OWN = PREFIX + "email_own";

        public static final String USERNAME_OWN = PREFIX + "username_own";

        public static final String VERIFY_PHONE_OWN = PREFIX + "verify_phone_own";

        public static final String REGISTER = PREFIX + "register";

        public static final String VERIFY_EMAIL = PREFIX + "verify";

        public static final String EDIT_USERNAME = PREFIX + "editUsername";

        public static final String EDIT_EMAIL = PREFIX + "editEmail";

        public static final String SEND_PHONE_CODE = PREFIX + "sendPhoneCode";

        public static final String VERIFY_PHONE = PREFIX + "verifyPhone";

        public static final String FINALIZE_PHONE = PREFIX + "finalizePhone";

        public static final String REMOVE_PHONE = PREFIX + "removePhone";

        public static final String CHANGE_PASSWORD = PREFIX + "changePassword";

        public static final String DELETE_ACCOUNT = PREFIX + "deleteAccount";

        private Auth() {

        }
    }

    public static class Session {
        private static final String PREFIX = BASE + "session/";

        public static final String LOGOUT = PREFIX + "logout";

        public static final String GET_USER = PREFIX + "getUser";

        public static final String GET_USER_FOR_ID = PREFIX + "getUserForId";

        public static final String CREATE_SERVER = PREFIX + "createServer";

        public static final String GET_SERVERS = PREFIX + "getServers";

        public static final String GET_SERVER = PREFIX + "getServer";

        public static final String CREATE_INVITE = PREFIX + "createInvite";

        public static final String JOIN_WITH_INVITE = PREFIX + "joinWithInvite";

        public static final String SEND_MESSAGE = PREFIX + "sendMessage";

        public static final String GET_MESSAGES = PREFIX + "getMessages";

        public static final String SEEN = PREFIX + "seen";

        public static final String DELETE_CHANNEL = PREFIX + "deleteChannel";

        public static final String CREATE_CHANNEL = PREFIX + "createChannel";

        private Session() {

        }
    }
}
