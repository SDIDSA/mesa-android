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

public class Server extends Bean {
	public static final String SERVER = "server";
	public static final String SERVER_ID = "server_id";
	
	private IntegerProperty id;
	private StringProperty owner;
	private StringProperty name;
	private StringProperty icon;

	private BooleanBinding unreadBinding;
	private BooleanProperty unread;

	private ObservableList<ChannelGroup> groups;
	private ObservableList<String> members;

	private int order;

	public Server(JSONObject obj, int order) {
		id = new IntegerProperty();
		owner = new StringProperty();
		name = new StringProperty();
		icon = new StringProperty();

		unread = new BooleanProperty();

		groups = new ObservableArrayList<>();
		members = new ObservableArrayList<>();

		init(obj);

		this.order = order;
	}

	public int getOrder() {
		return order;
	}

	public ObservableList<ChannelGroup> getGroups() {
		return groups;
	}

	public void addGroup(ChannelGroup group) {
		group.setServer(this);
		groups.add(group);

		if (unreadBinding == null) {
			unreadBinding = Bindings.whenBoolean(group.unreadProperty()).then(true).otherwise(false);
		} else {
			unreadBinding = unreadBinding.or(group.unreadProperty());
		}

		unread.unbind();
		unread.bind(unreadBinding);
	}

	public void removeChannel(int channel) {
		for (ChannelGroup group : groups) {
			if (group.removeChannel(channel)) {
				break;
			}
		}
	}

	public void addChannel(int groupId, Channel channel) {
		for (ChannelGroup group : groups) {
			if (group.getId().equals(groupId)) {
				group.addChannel(channel);
				break;
			}
		}
	}

	public boolean isUnread() {
		return unread.get();
	}

	public BooleanProperty unreadProperty() {
		return unread;
	}

	public void addMember(String member) {
		members.add(member);
	}

	public IntegerProperty idProperty() {
		return id;
	}

	public void setMembers(JSONArray arr) {
		for(int i = 0; i< arr.length(); i++) {
			try {
				addMember(arr.getString(i));
			} catch (JSONException e) {
				ErrorHandler.handle(e, "set Members");
			}
		}
	}
	
	public ObservableList<String> getMembers() {
		return members;
	}

	public void setGroups(JSONArray arr) {
		for(int i = 0; i< arr.length(); i++) {
			try {
				addGroup(new ChannelGroup(arr.getJSONObject(i)));
			} catch (JSONException e) {
				ErrorHandler.handle(e, "set groups");
			}
		}
	}

	public Integer getId() {
		return id.get();
	}

	public void setId(Integer val) {
		id.set(val);
	}

	public StringProperty ownerProperty() {
		return owner;
	}

	public String getOwner() {
		return owner.get();
	}

	public void setOwner(String val) {
		owner.set(val);
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

	public StringProperty iconProperty() {
		return icon;
	}

	public String getIcon() {
		return icon.get();
	}

	public void setIcon(String val) {
		icon.set(val);
	}

	public Channel getChannel(Integer channel) {
		for (ChannelGroup group : groups) {
			Channel ch = group.getChannel(channel);
			if (ch != null) {
				return ch;
			}
		}

		return null;
	}

	public boolean hasChannel(Integer channel) {
		for (ChannelGroup group : groups) {
			if (group.hasChannel(channel)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " {" + "\n\tid : " + id.get() + "\n\towner : " + owner.get() + "\n\tname : "
				+ name.get() + "\n\ticon : " + icon.get() + "\n\tgroups : " + stringifyGroups() + "\n}";
	}

	private String stringifyGroups() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < groups.size(); i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(groups.get(i).toString());
		}

		return sb.toString();
	}
}