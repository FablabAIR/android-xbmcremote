package org.xbmc.android.remote.business;

import java.util.ArrayList;

import org.xbmc.api.business.DataResponse;
import org.xbmc.api.business.INotifiableManager;
import org.xbmc.api.business.IReflexiveRemoteManager;
import org.xbmc.api.business.ISortableManager;
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

	@Override
	public void executePlugins(DataResponse<ArrayList<String>> response,
			Context context) {
		// TODO Auto-generated method stub
		
	}
}
