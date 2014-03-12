package org.xbmc.api.business;

import java.util.ArrayList;

import org.xbmc.api.object.Addon;
import org.xbmc.api.object.Album;

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
	public void executePlugins(final DataResponse<ArrayList<String>> response, final Context context);
}
