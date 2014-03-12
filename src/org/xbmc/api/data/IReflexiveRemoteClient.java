package org.xbmc.api.data;

import java.util.ArrayList;

import org.xbmc.api.business.INotifiableManager;
import org.xbmc.api.object.Addon;
import org.xbmc.api.object.Album;

public interface IReflexiveRemoteClient extends IClient {

	/**
	 * Gets all activity active 
	 * @param sortBy Sort field, see SortType.* 
	 * @param sortOrder Sort order, must be either SortType.ASC or SortType.DESC.
	 * @return All activity
	 */
	public ArrayList<Integer> getActivities(INotifiableManager manager);
	
	public  Boolean executeAddon (INotifiableManager manager,String addonId);
	public  ArrayList<Addon> getPlugins (INotifiableManager manager);

}
