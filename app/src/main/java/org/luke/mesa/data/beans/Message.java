package org.luke.mesa.data.beans;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.data.property.IntegerProperty;
import org.luke.mesa.data.property.StringProperty;

public class Message extends Bean implements Comparable<Message> {
	private static HashMap<Integer, Message> cache = new HashMap<>();

	public static synchronized Message get(JSONObject object) {
		try {
			Message found = cache.get(object.getInt("id"));
			if (found == null) {
				found = new Message(object);
				cache.put(found.getId(), found);
			}

			return found;
		}catch(JSONException x) {
			ErrorHandler.handle(x, "get message");
			return null;
		}
	}

	private IntegerProperty id;
	private IntegerProperty channel;
	private StringProperty sender;
	private StringProperty type;
	private StringProperty content;
	private StringProperty time;

	public Message() {
		id = new IntegerProperty();
		channel = new IntegerProperty();
		sender = new StringProperty();
		type = new StringProperty();
		content = new StringProperty();
		time = new StringProperty();
	}

	public Message(int id, int channel, String sender, String content, String time) {
		this();
		setId(id);
		setChannel(channel);
		setSender(sender);
		setContent(content);
		setTime(time);
	}

	public Message(JSONObject obj) {
		this();
		init(obj);
		if(!getContent().isEmpty()) {
			try {
				setContent(URLDecoder.decode(getContent(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				ErrorHandler.handle(e, "url-decode message content");
			}
		}
	}

	public IntegerProperty idProperty() {
		return id;
	}

	public Integer getId() {
		return id.get();
	}

	public void setId(Integer val) {
		id.set(val);
		cache.put(val, this);
	}

	public IntegerProperty channelProperty() {
		return channel;
	}

	public Integer getChannel() {
		return channel.get();
	}

	public void setChannel(Integer val) {
		channel.set(val);
	}

	public StringProperty senderProperty() {
		return sender;
	}

	public String getSender() {
		return sender.get();
	}

	public void setSender(String val) {
		sender.set(val);
	}

	public StringProperty contentProperty() {
		return content;
	}

	public String getContent() {
		return content.get();
	}

	public void setContent(String val) {
		content.set(val);
	}

	public StringProperty typeProperty() {
		return type;
	}

	public String getType() {
		return type.get();
	}

	public void setType(String val) {
		type.set(val);
	}

	public StringProperty timeProperty() {
		return time;
	}

	public String getTime() {
		return time.get();
	}

	public void setTime(String val) {
		time.set(val);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " {" + "\tsender : " + sender.get() + "\ttype : " + type.get()
				+ "\tcontent : " + content.get() + "\ttime : " + time.get() + "}";
	}

	@Override
	public int compareTo(Message o) {
		return Integer.compare(id.get(), o.id.get());
	}
}