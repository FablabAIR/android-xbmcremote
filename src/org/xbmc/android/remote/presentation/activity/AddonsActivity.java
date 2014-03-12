package org.xbmc.android.remote.presentation.activity;

import org.xbmc.android.remote.R;
import android.os.Bundle;

public class AddonsActivity extends AbsListActivity {
	
	public AddonsActivity() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupLists(R.layout.blanklist);
	}
}
