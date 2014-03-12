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

//	@Override
//	public ArrayList<String> getPlugins(INotifiableManager manager) {
//
//	
//	//	JsonNode addons = mConnection.getJson(manager, "Addons.GetAddons", obj().p("params", ""));
//		
//		ObjNode obj = new ObjNode(FACTORY) ;
//		//obj.p(PARAM_PROPERTIES, arr().add("director").add("file").add("genre").add("imdbnumber").add("playcount").add("rating").add("runtime").add("thumbnail").add("year"));
//		
//		ArrayList<String> addons = new ArrayList<String>();
//		System.out.println(mConnection.toString());
//		System.out.println("1");
//		JsonNode result = mConnection.getJson(manager, "Addons.GetAddons");
//		return addons ;
//	}
	
	@Override
	public ArrayList<Addon> getPlugins(INotifiableManager manager) {
	//	JsonNode addons = mConnection.getJson(manager, "Addons.GetAddons", obj().p("params", ""));
		
		//obj = sort(obj.p(PARAM_PROPERTIES, arr().add("director").add("file").add("genre").add("imdbnumber").add("playcount").add("rating").add("runtime").add("thumbnail").add("year")), sortBy, sortOrder);
		
		ArrayList<Addon> addons = new ArrayList<Addon>();
//		final JsonNode result = mConnection.getJson(manager, "Addons.GetAddons", obj());
//		final JsonNode jsonAddons = result.get("addons");
//		if(jsonAddons != null){
//			for (Iterator<JsonNode> i = jsonAddons.getElements(); i.hasNext();) {
//				JsonNode jsonAddon = (JsonNode)i.next();
//				System.out.println(getString(jsonAddon, "addonid"));
//				addons.add(getString(jsonAddon, "addonid"));
//			}
//		}
		
		addons.add(new Addon("pvr.vdr.vnsi", "xbmc.pvrclient"));
		addons.add(new Addon("pvr.argustv", "xbmc.pvrclient"));
		return addons;
	}
	

}
