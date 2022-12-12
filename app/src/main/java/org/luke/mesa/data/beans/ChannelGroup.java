package org.luke.mesa.data.beans;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.data.binding.Bindings;
import org.luke.mesa.data.binding.boolean_type.BooleanBinding;
import org.luke.mesa.data.property.BooleanProperty;
import org.luke.mesa.data.property.IntegerProperty;
import org.luke.mesa.data.property.StringProperty;

public class ChannelGroup extends Bean {
	private IntegerProperty id;
	private StringProperty name;

	private BooleanBinding unreadBinding;
	private BooleanProperty unread;
	
	private Server server;

	private ObservableList<Channel> channels;
	
	public ChannelGroup(JSONObject obj) {
		id = new IntegerProperty();
		name = new StringProperty();

		unread = new BooleanProperty();
		
		channels = new ObservableArrayList<>();

		init(obj);
	}

	public void setServer(Server server) {
		this.server = server;
	}
	
	public Server getServer() {
		return server;
	}

	public ObservableList<Channel> getChannels() {
		return channels;
	}

	public void addChannel(Channel channel) {
		channel.setGroup(this);
		channels.add(channel);
		
		if(unreadBinding == null) {
			unreadBinding = Bindings.whenBoolean(channel.unreadProperty()).then(true).otherwise(false);
		}else {
			unreadBinding = unreadBinding.or(channel.unreadProperty());
		}
		
		unread.unbind();
		unread.bind(unreadBinding);
	}

	public boolean isUnread() {
		return unread.get();
	}
	
	public BooleanProperty unreadProperty() {
		return unread;
	}
	
	public void setChannels(JSONArray arr) {
		for(int i = 0; i< arr.length(); i++) {
			try {
				addChannel(new Channel(arr.getJSONObject(i)));
			} catch (JSONException e) {
				ErrorHandler.handle(e, "set channels");
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

	public Channel getChannel(Integer chid) {
		for(Channel channel : channels) {
			if(channel.getId().equals(chid)) {
				return channel;
			}
		}
		
		return null;
	}
	
	public boolean hasChannel(Integer chid) {
		for(Channel channel : channels) {
			if(channel.getId().equals(chid)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "{"
				+ "\n\t\tid : " + id.get() 
				+ "\n\t\tname : " + name.get() 
				+ "\n\t\tchannels : " + stringifyChannels()
			+ "\n\t}";
	}
	
	private String stringifyChannels() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0;i<channels.size();i++) {
			if(i != 0) {
				sb.append(", ");
			}
			sb.append(channels.get(i).toString());
		}
		
		return sb.toString();
	}

	public boolean removeChannel(int channel) {

		boolean removed = channels.removeIf(ch -> ch.getId().equals(channel));
		if(removed) {
			if(!channels.isEmpty()) {
				unreadBinding = Bindings.whenBoolean(channels.get(0).unreadProperty()).then(true).otherwise(false);
				
				for(int i = 1; i < channels.size(); i++) {
					unreadBinding = unreadBinding.or(channels.get(i).unreadProperty());
				}
				
				unread.unbind();
				unread.bind(unreadBinding);
			}else {
				unreadBinding = null;
				
				unread.unbind();
				unread.set(false);
			}
			return true;
		}
		return false;
	}
}