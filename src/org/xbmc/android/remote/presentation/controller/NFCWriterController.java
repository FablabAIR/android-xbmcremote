package org.xbmc.android.remote.presentation.controller;

import java.io.Serializable;

import org.xbmc.android.remote.business.ManagerFactory;
import org.xbmc.android.remote.presentation.activity.NowPlayingActivity;
import org.xbmc.android.remote.presentation.controller.ListController.QueryResponse;
import org.xbmc.api.business.DataResponse;
import org.xbmc.api.business.IMusicManager;
import org.xbmc.api.object.Album;
import org.xbmc.api.object.Genre;
import org.xbmc.api.presentation.INotifiableController;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Handler;
import android.widget.Toast;

@SuppressWarnings("serial")
public class NFCWriterController extends AbstractController implements Serializable, INotifiableController {

	private IMusicManager mMusicManager;
	private Album album;
	
	public void onCreate(Activity activity, Handler handler) {
		super.onCreate(activity, handler);
		mMusicManager = ManagerFactory.getMusicManager(this);
	}
	
	public void playMusic(Album album){
			mMusicManager.play(new QueryResponse(
				mActivity, 
				"Playing album \"" + album.name + "\" by " + album.artist + "...", 
				"Error playing album!",
				true
			), album, mActivity.getApplicationContext());
	}
	public void playMusic(Genre genre){
		mMusicManager.play(new QueryResponse(
				mActivity, 
				"Playing all songs of genre " + genre.name + "...", 
				"Error playing songs!",
				true
			), genre, mActivity.getApplicationContext());
	}
	protected class QueryResponse extends DataResponse<Boolean> {
		private final String mSuccessMessage;
		private final String mErrorMessage;
		private final boolean mGotoNowPlaying;
		public QueryResponse(Activity activity, String successMessage, String errorMessage) {
			super();
			mSuccessMessage = successMessage;
			mErrorMessage = errorMessage;
			mGotoNowPlaying = false;
		}
		public QueryResponse(Activity activity, String successMessage, String errorMessage, boolean gotoNowPlaying) {
			super();
			mSuccessMessage = successMessage;
			mErrorMessage = errorMessage;
			mGotoNowPlaying = gotoNowPlaying;
		}
		public void run() {
			Toast.makeText(mActivity, value ? mSuccessMessage :  mErrorMessage, Toast.LENGTH_LONG).show();
			if (value && mGotoNowPlaying) {
				mActivity.startActivity(new Intent(mActivity, NowPlayingActivity.class));
			}
		}
	}

}
