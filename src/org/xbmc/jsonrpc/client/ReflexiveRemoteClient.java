package org.xbmc.jsonrpc.client;

import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.xbmc.api.business.INotifiableManager;
import org.xbmc.api.data.IReflexiveRemoteClient;
import org.xbmc.api.object.Album;
import org.xbmc.api.object.Host;
import org.xbmc.jsonrpc.Connection;
import org.xbmc.api.object.Addon;

public class ReflexiveRemoteClient extends Client implements IReflexiveRemoteClient {

	public ReflexiveRemoteClient(Connection connection) {
		super(connection);
	}

	@Override
	public void setHost(Host host) {
		mConnection.setHost(host);
	}

	@Override
	public ArrayList<Integer> getActivities(INotifiableManager manager) {
		 ArrayList<Integer> activities = new ArrayList<Integer>();
		 activities.add(1);
		 activities.add(2);
		 activities.add(3);
		return activities;
	}
	
	@Override
	public ArrayList<Addon> getPlugins(INotifiableManager manager) {
		ArrayList<Addon> addons = new ArrayList<Addon>();
		System.out.println("1111");
		final JsonNode result = mConnection.getJson(manager, "Addons.GetAddons", obj());
		System.err.println(result.size());
		final JsonNode jsonAddons = result.get("addons");
		if(jsonAddons != null){
			for (Iterator<JsonNode> i = jsonAddons.getElements(); i.hasNext();) {
				JsonNode jsonAddon = (JsonNode)i.next();
				addons.add(new Addon(getString(jsonAddon, "addonid"), getString(jsonAddon, "type")));
			}
		}
		addons.add(new Addon("pvr.argustv", "xbmc.pvrclient"));
		return addons;
	}

	@Override
	public Boolean executeAddon(INotifiableManager manager, String addonId) {
		return mConnection.getString(manager, "Addons.ExecuteAddon", obj().p("addonid",addonId)).equals("OK");
	}
}