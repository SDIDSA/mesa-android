package org.luke.mesa.data.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.api.Session;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.abs.utils.functional.UserConsumer;
import org.luke.mesa.data.property.BooleanProperty;
import org.luke.mesa.data.property.StringProperty;

public class User extends Bean {
	public static final String USER_ID = "user_id";
	
	private static HashMap<String, User> cache = new HashMap<>();
	private static HashMap<String, ArrayList<UserConsumer>> waiting = new HashMap<>();

	public static synchronized void getForId(String id, UserConsumer onUser) {
		User res = cache.get(id);
		if (res == null) {
			ArrayList<UserConsumer> pending = waiting.get(id);
			if (pending == null) {
				pending = new ArrayList<>();
				waiting.put(id, pending);
				Session.getUserForId(id, result -> {
					try {
						User u = new User(result.getJSONObject("user"), id);
						for(UserConsumer consumer : waiting.get(id)) {
							consumer.accept(u);
						}
						waiting.remove(id);
					}catch(JSONException x) {
						ErrorHandler.handle(x, "get user");
					}

				});
			}
			pending.add(onUser);
		} else {
			onUser.accept(res);
		}
	}
	
	public static void clear() {
		cache.clear();
	}

	private StringProperty id;
	private StringProperty email;
	private StringProperty username;
	private StringProperty password;
	private StringProperty phone;
	private StringProperty avatar;
	private StringProperty birthDate;
	private BooleanProperty emailConfirmed;
	
	private BooleanProperty online;

	public User(JSONObject obj) {
		id = new StringProperty();
		email = new StringProperty();
		username = new StringProperty();
		password = new StringProperty();
		phone = new StringProperty();
		avatar = new StringProperty();
		birthDate = new StringProperty();
		emailConfirmed = new BooleanProperty();
		
		online = new BooleanProperty();
		
		init(obj);

		if(id.get() != null && !id.get().isEmpty()) {
			cache.put(id.get(), this);
		}
	}
	
	public User(JSONObject obj, String id) {
		this(obj);
		setId(id);
		cache.put(id, this);
	}

	public StringProperty idProperty() {
		return id;
	}

	public String getId() {
		return id.get();
	}

	public void setId(String val) {
		id.set(val);
	}

	public StringProperty emailProperty() {
		return email;
	}

	public String getEmail() {
		return email.get();
	}

	public void setEmail(String val) {
		email.set(val);
	}

	public StringProperty usernameProperty() {
		return username;
	}

	public String getUsername() {
		return username.get();
	}

	public void setUsername(String val) {
		username.set(val);
	}

	public StringProperty passwordProperty() {
		return password;
	}

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String val) {
		password.set(val);
	}

	public StringProperty phoneProperty() {
		return phone;
	}

	public String getPhone() {
		return phone.get();
	}

	public void setPhone(String val) {
		phone.set(val);
	}

	public StringProperty avatarProperty() {
		return avatar;
	}

	public String getAvatar() {
		return avatar.get();
	}

	public void setAvatar(String val) {
		avatar.set(val);
	}

	public StringProperty birthDateProperty() {
		return birthDate;
	}

	public String getBirthDate() {
		return birthDate.get();
	}

	public void setBirthDate(String val) {
		birthDate.set(val);
	}

	public BooleanProperty emailConfirmedProperty() {
		return emailConfirmed;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed.get();
	}

	public void setEmailConfirmed(Boolean val) {
		emailConfirmed.set(val);
	}
	
	public BooleanProperty onlineProperty() {
		return online;
	}
	
	public void setOnline(Boolean val) {
		online.set(val);
	}
	
	public boolean isOnline() {
		return online.get();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " {" + "\tid : " + id.get() + "\temail : " + email.get() + "\tusername : "
				+ username.get() + "\tpassword : " + password.get() + "\tphone : " + phone.get() + "\tavatar : "
				+ avatar.get() + "\tbirthDate : " + birthDate.get() + "\temailConfirmed : " + emailConfirmed.get()
				+ "}";
	}
}