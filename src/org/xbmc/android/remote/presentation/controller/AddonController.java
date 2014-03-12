package org.xbmc.android.remote.presentation.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.xbmc.android.remote.R;
import org.xbmc.android.remote.business.ManagerFactory;
import org.xbmc.android.remote.presentation.activity.AddonsActivity;
import org.xbmc.android.remote.presentation.activity.ListActivity;
import org.xbmc.android.remote.presentation.activity.NowPlayingActivity;
import org.xbmc.android.remote.presentation.activity.RemoteActivity;
import org.xbmc.android.remote.presentation.widget.OneLabelItemView;
import org.xbmc.api.business.DataResponse;
import org.xbmc.api.business.IControlManager;
import org.xbmc.api.business.IInfoManager;
import org.xbmc.api.business.IReflexiveRemoteManager;
import org.xbmc.api.data.IReflexiveRemoteClient;
import org.xbmc.api.info.FileTypes;
import org.xbmc.api.object.Addon;
import org.xbmc.api.object.Album;
import org.xbmc.api.object.FileLocation;
import org.xbmc.api.type.MediaType;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("serial")
public class AddonController extends ListController implements IController {

	private IReflexiveRemoteManager mReflexiveManager;
	private HashMap<String, Addon> mFileItems;
	protected ListAdapter mAdapter;
	

	public void onCreate(Activity activity, Handler handler, AbsListView list) {
		mReflexiveManager = ManagerFactory.getReflexiveRemoteManager(this);
		if (!isCreated()) {
			super.onCreate(activity, handler, list);
			fillUp();

			activity.registerForContextMenu(mList);
			mList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (mFileItems == null)
						return;
					// setListAdapter(new FileItemAdapter(mActivity,
					// listAddon));
					Intent intent = new Intent(mActivity, AddonsActivity.class);
					intent.putExtra(ListController.EXTRA_LIST_CONTROLLER, new AddonController());
					
					mActivity.startActivity(intent);
				}
			});
		}
	}

	private class FileItemAdapter extends ArrayAdapter<Addon> {

		FileItemAdapter(Activity activity, ArrayList<Addon> items) {
			super(activity, 0, items);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			final OneLabelItemView view;
			if (convertView == null) {
				view = new OneLabelItemView(mActivity, parent.getWidth(),
						mFallbackBitmap, mList.getSelector(), true);
			} else {
				view = (OneLabelItemView) convertView;
			}
			view.reset();
			view.position = position;
			view.title = this.getItem(position).name;
			final Resources res = mActivity.getResources();
			view.setCover(BitmapFactory.decodeResource(res,
					R.drawable.icon_play));
			return view;
		}
	}

	private void fillUp() {

		mFileItems = null;
		mList.setTextFilterEnabled(false);
		setTitle("Addons");
		showOnLoading();
		DataResponse<ArrayList<Addon>> mediaListHandler = new DataResponse<ArrayList<Addon>>() {
			public void run() {
				if (value.size() > 0) {
					mFileItems = new HashMap<String, Addon>();
					for (Addon item : value) {
						mFileItems.put(item.name, item);
					}
					setListAdapter(new FileItemAdapter(mActivity, value));
				} else {
					setNoDataMessage("No files found.", R.drawable.icon_folder_dark);
				}
			}
		};
		//methode des listes
		mReflexiveManager.getPluginsTest(mediaListHandler,mActivity.getApplicationContext());
	}

	public void setListAdapter(ListAdapter adapter) {
		synchronized (this) {
			mAdapter = adapter;
			((AdapterView<ListAdapter>) mList).setAdapter(adapter);
		}
	}

	@Override
	public void onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub

	}

}
