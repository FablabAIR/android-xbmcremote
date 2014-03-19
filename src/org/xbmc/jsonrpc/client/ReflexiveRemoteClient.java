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
import org.xbmc.api.type.ListItemType;

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
		mConnection.getString(manager, "Input.Select", null).equals("OK");

		 ArrayList<Integer> activities = new ArrayList<Integer>();
		 activities.add(1);
		 activities.add(3);
		 activities.add(4);
		 activities.add(5);
		 activities.add(9);
		 activities.add(10);
		return activities;
	}
	
	@Override
	public ArrayList<Addon> getPlugins(INotifiableManager manager) {
		ArrayList<Addon> addons = new ArrayList<Addon>();
		final JsonNode result = mConnection.getJson(manager, "Addons.GetAddons", obj());
		System.err.println(result.size());
		final JsonNode jsonAddons = result.get("addons");
		if(jsonAddons != null){
			for (Iterator<JsonNode> i = jsonAddons.getElements(); i.hasNext();) {
				JsonNode jsonAddon = (JsonNode)i.next();
				addons.add(new Addon(getString(jsonAddon, "addonid"), getString(jsonAddon, "type")));
			}
		}
		//addons.add(new Addon("pvr.argustv", "xbmc.pvrclient"));
		return addons;
	}

	@Override
	public Boolean executeAddon(INotifiableManager manager, String addonId) {
		return mConnection.getString(manager, "Addons.ExecuteAddon", obj().p("addonid",addonId)).equals("OK");
	}

	@Override
	public ArrayList<ListItemType> getCurrentListDisplayed(INotifiableManager manager) {
		long start=System.nanoTime();
		while((System.nanoTime()-start)<600000000);
		ArrayList<ListItemType> listItemsDisplayed = new ArrayList<ListItemType>();
		final JsonNode result = mConnection.getJson(manager, "GUI.GetCurrentListDisplayed", obj());
		System.err.println(result.size());
		int id = 0 ; 
		if(result!= null){
			for (Iterator<JsonNode> i = result.getElements(); i.hasNext();) {
				JsonNode jsonItem = (JsonNode)i.next();
				listItemsDisplayed.add(new ListItemType(getString(jsonItem,"itemId"),id));
				id++;
			}
		}
//		ArrayList<ListItemType> listItemsDisplayed = new ArrayList<ListItemType>();
//		listItemsDisplayed.add(new ListItemType("Test1",12001));
//		listItemsDisplayed.add(new ListItemType("Test2",12002));
//		listItemsDisplayed.add(new ListItemType("Test3",12003));
		return listItemsDisplayed;
	}
	
	@Override
	public ArrayList<ListItemType> setSelectedItem(INotifiableManager manager){
		mConnection.getString(manager, "Input.Select", null).equals("OK");
		long start=System.nanoTime();
		while((System.nanoTime()-start)<600000000);
		ArrayList<ListItemType> listItemsDisplayed = new ArrayList<ListItemType>();
		final JsonNode result = mConnection.getJson(manager, "GUI.GetCurrentListDisplayed", obj());
		System.err.println(result.size());
		int id = 0 ; 
		if(result!= null){
			for (Iterator<JsonNode> i = result.getElements(); i.hasNext();) {
				JsonNode jsonItem = (JsonNode)i.next();
				listItemsDisplayed.add(new ListItemType(getString(jsonItem,"itemId"),id));
				id++;
			}
		}
//		ArrayList<ListItemType> listItemsDisplayed = new ArrayList<ListItemType>();
//		listItemsDisplayed.add(new ListItemType("Test1",12001));
//		listItemsDisplayed.add(new ListItemType("Test2",12002));
//		listItemsDisplayed.add(new ListItemType("Test3",12003));
		return listItemsDisplayed;

	}
}