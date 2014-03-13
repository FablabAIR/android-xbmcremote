package org.xbmc.api.business;

import java.util.ArrayList;

import org.xbmc.api.object.Addon;
import org.xbmc.api.object.Album;
import org.xbmc.api.type.ListItemType;

import android.content.Context;

public interface IReflexiveRemoteManager extends IManager {

	/**
	 * Gets all movies from database
	 * @param response Response object
	 */
	public void getActivities(final DataResponse<ArrayList<Integer>> response, final Context context);
	
	/**
	 * Gets all movies from database
	 * @param response Response object
	 */
	public void getPlugins(final DataResponse<ArrayList<Addon>> response, final Context context);
	
	/**
	 * Gets all movies from database
	 * @param response Response object
	 */
	public void executePlugins(final DataResponse<Boolean> response, final Context context,final String addonid);
	
	public void GetCurrentListDisplayed(final DataResponse<ArrayList<ListItemType>> response, final Context context);
	
	public void setSelectedItem(final DataResponse<ArrayList<ListItemType>> response, final Context context);

}
