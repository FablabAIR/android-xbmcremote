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
import org.xbmc.api.type.ListItemType;
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
	private HashMap<String, ListItemType> mFileItems;
	protected ListAdapter mAdapter;

	public void onCreate(Activity activity, Handler handler, AbsListView list) {
		mReflexiveManager = ManagerFactory.getReflexiveRemoteManager(this);
		if (!isCreated()) {
			super.onCreate(activity, handler, list);
			
			if((mActivity.getIntent().getIntExtra("first", -1)==0)){
				fillUpAddon();
			}
			else{
				fillUpItem();
			}

			activity.registerForContextMenu(mList);
			mList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (mFileItems == null)
						return;
					
					DataResponse<Boolean> response = new DataResponse<Boolean>() {
                        public void run() {
                            if (value) {
                                System.err.println("Execution Plugin OK");
                            } else {
                                System.out.println("Execution Plugin Failed");
                            }
                        }
                    };
                    
					Intent intent = new Intent(mActivity, ListActivity.class);
					intent.putExtra(ListController.EXTRA_LIST_CONTROLLER, new AddonController());
					intent.putExtra("first", 1);
					mActivity.startActivity(intent);
					
				}
			});
		}
	}

	private class FileItemAdapter extends ArrayAdapter<ListItemType> {

		FileItemAdapter(Activity activity, ArrayList<ListItemType> items) {
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
			view.title = this.getItem(position).getName();
			final Resources res = mActivity.getResources();
			view.setCover(BitmapFactory.decodeResource(res,
					R.drawable.icon_play));
			return view;
		}
	}
	
	private void fillUpAddon() {
		
		mFileItems = null;
		mList.setTextFilterEnabled(false);
		setTitle("Addons");
		showOnLoading();
		DataResponse<ArrayList<ListItemType>> mediaListHandler = new DataResponse<ArrayList<ListItemType>>() {
			public void run() {
				if (value.size() > 0) {
					mFileItems = new HashMap<String, ListItemType>();
					for (ListItemType item : value) {
						mFileItems.put(item.getName(), item);
					}
					setListAdapter(new FileItemAdapter(mActivity, value));
				} else {
					setNoDataMessage("No files found.", R.drawable.icon_folder_dark);
				}
			}
		};
		//methode pour recuperer liste
		mReflexiveManager.GetCurrentListDisplayed(mediaListHandler,mActivity.getApplicationContext());
	}

	private void fillUpItem() {
		mFileItems = null;
		mList.setTextFilterEnabled(false);
		setTitle("Addons");
		showOnLoading();
		DataResponse<ArrayList<ListItemType>> mediaListHandler = new DataResponse<ArrayList<ListItemType>>() {
			public void run() {
				if (value.size() > 0) {
					mFileItems = new HashMap<String, ListItemType>();
					for (ListItemType item : value) {
						mFileItems.put(item.getName(), item);
					}
					setListAdapter(new FileItemAdapter(mActivity, value));
				} else {
					//setNoDataMessage("No files found.", R.drawable.icon_folder_dark);
					mActivity.startActivity(new Intent(mActivity, RemoteActivity.class));
					mActivity.finish();
				}
			}
		};
		mReflexiveManager.setSelectedItem(mediaListHandler,mActivity.getApplicationContext());
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
