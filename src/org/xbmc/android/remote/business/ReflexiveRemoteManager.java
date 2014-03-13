package org.xbmc.android.remote.business;

import java.util.ArrayList;
import java.util.Arrays;

import org.xbmc.api.business.DataResponse;
import org.xbmc.api.business.INotifiableManager;
import org.xbmc.api.business.IReflexiveRemoteManager;
import org.xbmc.api.business.ISortableManager;
import org.xbmc.api.data.IControlClient;
import org.xbmc.api.data.IMusicClient;
import org.xbmc.api.type.ListItemType;
import org.xbmc.api.type.SortType;
import org.xbmc.api.object.Addon;
import org.xbmc.api.object.Album;

import android.content.Context;

public class ReflexiveRemoteManager extends AbstractManager implements IReflexiveRemoteManager,INotifiableManager {

	public void getActivities(final DataResponse<ArrayList<Integer>> response, final Context context) {
		mHandler.post(new Command<ArrayList<Integer>>(response, this) {
			@Override
			public void doRun() throws Exception { 
				response.value = refelexiveRemote(context).getActivities(ReflexiveRemoteManager.this);
			}
		});
	}

	
	public void getPlugins(final DataResponse<ArrayList<Addon>> response,final Context context) {
		mHandler.post(new Command<ArrayList<Addon>>(response, this) {
			@Override
			public void doRun() throws Exception { 
				response.value = refelexiveRemote(context).getPlugins(ReflexiveRemoteManager.this);
			}
		});
	}
	

	public void getPluginsTest(final DataResponse<ArrayList<Addon>> response,final Context context) {
		mHandler.post(new Command<ArrayList<Addon>>(response, this) {
			@Override
			public void doRun() throws Exception { 
				final ArrayList<Addon> listAddon = new ArrayList<Addon>(Arrays.asList(new Addon("name", "int")));
				response.value = listAddon;
			}
		});
	}
	
	@Override
	public void executePlugins(final DataResponse<Boolean> response,
			final Context context,final String addonid) {
		mHandler.post(new Command<Boolean>(response, this) {
			@Override
			public void doRun() throws Exception { 
				response.value = refelexiveRemote(context).executeAddon(ReflexiveRemoteManager.this,addonid);
			}
		});
		
	}


	@Override
	public void GetCurrentListDisplayed(final DataResponse<ArrayList<ListItemType>> response,final Context context) {
		mHandler.post(new Command<ArrayList<ListItemType>>(response, this) {
			@Override
			public void doRun() throws Exception { 
				response.value = refelexiveRemote(context).getCurrentListDisplayed(ReflexiveRemoteManager.this);
			}
		});
		
	}


	@Override
	public void setSelectedItem(final DataResponse<ArrayList<ListItemType>> response,final Context context) {
		mHandler.post(new Command<ArrayList<ListItemType>>(response, this) {
			@Override
			public void doRun() throws Exception { 
				response.value = refelexiveRemote(context).setSelectedItem(ReflexiveRemoteManager.this);
			}
		});
		
	}
}
