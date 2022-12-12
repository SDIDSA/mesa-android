package org.luke.mesa.data.beans;

import org.json.JSONObject;
import org.luke.mesa.data.property.BooleanProperty;
import org.luke.mesa.data.property.IntegerProperty;
import org.luke.mesa.data.property.StringProperty;

public class Channel extends Bean {
	public static final String CHANNEL = "channel";
	
	private static final String TEXT_TYPE = "text";
	
	private IntegerProperty id;
	private StringProperty name;
	private StringProperty type;
	private BooleanProperty unread;
	
	private ChannelGroup group;

	public Channel(JSONObject obj) {
		id = new IntegerProperty();
		name = new StringProperty();
		type = new StringProperty();
		unread = new BooleanProperty();
		init(obj);
	}
	
	public void setGroup(ChannelGroup group) {
		this.group = group;
	}
	
	public ChannelGroup getGroup() {
		return group;
	}
	
	public IntegerProperty idProperty() {
		return id;
	}

	public Integer getId() {
		return id.get();
	}

	public void setId(Integer val) {
		id.set(val);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String val) {
		name.set(val);
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
	
	public BooleanProperty unreadProperty() {
		return unread;
	}
	
	public boolean isUnread() {
		return unread.get();
	}
	
	public void setUnread(Boolean val) {
		unread.set(val);
	}

	public String getTypeChar() {
		return isTextChannel() ? "# " : "";
	}
	
	@Override
	public String toString() {
		return "{"
				+ "\n\t\t\tid : " + id.get() 
				+ "\n\t\t\tname : " + name.get() 
				+ "\n\t\t\ttype : " + type.get() 
			+ "\n\t\t}";
	}

	public boolean isTextChannel() {
		return type.get().equals(TEXT_TYPE);
	}
}