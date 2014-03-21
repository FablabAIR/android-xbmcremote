package org.xbmc.jsonrpc.client;

import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.w3c.dom.ls.LSInput;
import org.xbmc.api.business.INotifiableManager;
import org.xbmc.api.data.IReflexiveRemoteClient;
import org.xbmc.api.object.Album;
import org.xbmc.api.object.Host;
import org.xbmc.jsonrpc.Connection;
import org.xbmc.api.object.Addon;
import org.xbmc.api.type.ListItemType;

public class ReflexiveRemoteClient extends Client implements IReflexiveRemoteClient {
	
	private static final int HOME_ACTION_REMOTE = 0;
	private static final int HOME_ACTION_MUSIC = 1;
	private static final int HOME_ACTION_VIDEOS = 2;
	private static final int HOME_ACTION_PICTURES = 3;
	private static final int HOME_ACTION_NOWPLAYING = 4;
	private static final int HOME_ACTION_RECONNECT = 5;
	private static final int HOME_ACTION_WOL = 6;
	private static final int HOME_ACTION_TVSHOWS = 7;
	private static final int HOME_ACTION_POWERDOWN = 8;
	private static final int HOME_ACTION_NFC = 9;
	private static final int HOME_ACTION_ADDON = 10;
	private static final int HOME_ACTION_WEATHER = 11;
	private static final int HOME_ACTION_PVR = 12;
	
	public ReflexiveRemoteClient(Connection connection) {
		super(connection);
	}

	@Override
	public void setHost(Host host) {
		mConnection.setHost(host);
	}

	@Override
	public ArrayList<Integer> getActivities(INotifiableManager manager) {
		ArrayList<String> listMainItems = new ArrayList<String>();
//		final JsonNode result = mConnection.getJson(manager, "GUI.GetCurrentMainMenu", obj());
//		System.err.println(result.size());
//		if(result!= null){
//			for (Iterator<JsonNode> i = result.getElements(); i.hasNext();) {
//				JsonNode jsonItem = (JsonNode)i.next();
//				listMainItems.add(getString(jsonItem,"itemId"));
//			}
//		}
		
		listMainItems.add("Movies");
		listMainItems.add("TVShow");
		listMainItems.add("Music");
		listMainItems.add("Programs");

		
		 ArrayList<Integer> mainMenu = new ArrayList<Integer>();
		 int tmp = -1 ; 
		 for (String menuItem : listMainItems) {
			 tmp = getMenuInt(menuItem);
			 if(tmp != -1)
				 {
				 	mainMenu.add(tmp);
				 }
		}
		 return mainMenu;
	}
	
	private Integer getMenuInt(String menuItem) {
		if(menuItem.equals("Movies")){
			return HOME_ACTION_VIDEOS;
		}else if(menuItem.equals("TVShow")){
			return HOME_ACTION_TVSHOWS;
		}else if(menuItem.equals("Music")){
			return HOME_ACTION_MUSIC;
		}else if(menuItem.equals("Picture")){
			return HOME_ACTION_PICTURES;
		}else if(menuItem.equals("Programs")){
			return HOME_ACTION_ADDON;
		}else if(menuItem.equals("Weather")){
			return HOME_ACTION_WEATHER;
		}else if(menuItem.equals("PVR")){
			return HOME_ACTION_PVR;
		}else{
			return -1;
		}

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
		return addons;
	}

	@Override
	public Boolean executeAddon(INotifiableManager manager, String addonId) {
		return mConnection.getString(manager, "Addons.ExecuteAddon", obj().p("addonid",addonId)).equals("OK");
	}

	@Override
	public ArrayList<ListItemType> getCurrentListDisplayed(INotifiableManager manager) {
		//Wait Start plugin
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		return listItemsDisplayed;

	}
}