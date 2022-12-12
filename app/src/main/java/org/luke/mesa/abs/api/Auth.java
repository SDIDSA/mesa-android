package org.luke.mesa.abs.api;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.api.json.Param;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.abs.utils.functional.JsonConsumer;
import org.luke.mesa.data.beans.User;


public class Auth {
	public static JSONObject netErr;

	static {
		try {
			netErr = new JSONObject("{\"err\":[{\"key\":\"global\",\"value\":\"net_err\"}]}");
		} catch (JSONException e) {
			ErrorHandler.handle(e, "create netErr");
		}
	}

	private static final String PASSWORD = "password";
	
	private Auth() {
		
	}
	
	public static void auth(String email, String password, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.LOGIN, "login with credentials", onResult,
				new Param("email_phone", email),
				new Param(PASSWORD, hashPassword(password)));
	}

	public static void phoneOwn(String phone, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.PHONE_OWN, "verify phone before register", onResult,
				new Param("phone", phone));
	}

	public static void verifyPhoneOwn(String phone, String code, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.VERIFY_PHONE_OWN, "verify phone ownership", onResult,
				new Param("phone", phone),
				new Param("code", code));
	}

	public static void emailOwn(String email, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.EMAIL_OWN, "verify email before register", onResult,
				new Param("email", email));
	}

	public static void usernameOwn(String username, String password, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.USERNAME_OWN, "verify username before register", onResult,
				new Param("username", username),
				new Param(PASSWORD, password));
	}

	public static void registerEmail(String email, String username, String password, String birthDate,
			JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.REGISTER, "create account", onResult,
				new Param("email", email),
				new Param("username", username),
				new Param(PASSWORD, hashPassword(password)),
				new Param("birth_date", birthDate));	
	}

	public static void registerPhone(String phone, String username, String password, String birthDate,String phoneCode,
								JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.REGISTER, "create account", onResult,
				new Param("phone", phone),
				new Param("username", username),
				new Param(PASSWORD, hashPassword(password)),
				new Param("birth_date", birthDate),
				new Param("phone_code", phoneCode));
	}
	
	public static void verifyEmail(String userId, String code, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.VERIFY_EMAIL,  "verify email", onResult,
				new Param(User.USER_ID, userId),
				new Param("verification_code", code));
	}
	
	public static void editUsername(String userId, String username, String password, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.EDIT_USERNAME, "change username", onResult, 
				new Param(User.USER_ID, userId),
				new Param("username", username),
				new Param(PASSWORD, hashPassword(password)));
	}
	
	public static void editEmail(String userId, String email, String password, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.EDIT_EMAIL, "change email", onResult, 
				new Param(User.USER_ID, userId),
				new Param("email", email),
				new Param(PASSWORD, hashPassword(password)));
	}
	
	public static void changePassword(String userId, String currentPass, String newPass, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.CHANGE_PASSWORD, "change password", onResult,
				new Param(User.USER_ID, userId),
				new Param("curr_pass", hashPassword(currentPass)),
				new Param("new_pass", hashPassword(newPass)));
	}
	
	public static void deleteAccount(String userId, String password, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.DELETE_ACCOUNT, "delete account", onResult,
				new Param(User.USER_ID, userId),
				new Param(PASSWORD, hashPassword(password)));
	}
	
	public static void sendPhoneCode(String userId, String phone, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.SEND_PHONE_CODE, "send phone code", onResult,
				new Param(User.USER_ID, userId),
				new Param("phone", phone));
	}
	
	public static void verifyPhone(String userId, String phone, String code, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.VERIFY_PHONE, "verify phone code", onResult,
				new Param(User.USER_ID, userId),
				new Param("phone", phone),
				new Param("code", code));
	}
	
	public static void finalizePhone(String userId, String password, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.FINALIZE_PHONE, "confirm phone change", onResult,
				new Param(User.USER_ID, userId),
				new Param(PASSWORD, hashPassword(password)));
	}
	
	public static void removePhone(String userId, String password, JsonConsumer onResult) {
		API.asyncJsonPost(API.Auth.REMOVE_PHONE, "remove phone", onResult,
				new Param(User.USER_ID, userId),
				new Param(PASSWORD, hashPassword(password)));
	}

	public static String hashPassword(String password) {
		return DigestUtils.sha256Hex(password);
	}
}
