package org.xbmc.android.remote.presentation.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.xbmc.android.remote.R;
import org.xbmc.android.remote.business.ManagerFactory;
import org.xbmc.android.remote.presentation.controller.AlbumListController;
import org.xbmc.android.remote.presentation.controller.IController;
import org.xbmc.android.remote.presentation.controller.NFCWriterController;
import org.xbmc.api.business.IMusicManager;
import org.xbmc.api.object.Album;
import org.xbmc.api.object.Genre;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class NFCWriterActivity extends Activity {
	TextView textView;
	private PendingIntent pendingIntent;
	private IntentFilter[] intentFiltersArray;
	private String[][] techListsArray;
	private NfcAdapter mAdapter;
	private NFCWriterController nfcController;
	private Handler mHandler;
	
	private Album album;
	private Genre genre;
	private int type;

	public static final int ACTION_WRITE_ALBUM = 0;
	public static final int ACTION_WRITE_GENRE = 1;
	public static final int ACTION_READ_ALBUM = 2;
	public static final int ACTION_READ_GENRE = 3;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_writer_layout);
		textView = (TextView) findViewById(R.id.nfc_writer_message);
		mHandler = new Handler();
		nfcController = new NFCWriterController();
		nfcController.onCreate(this, mHandler);
		
		type = this.getIntent().getIntExtra("type", -1);
		
		album = (Album)this.getIntent().getSerializableExtra("Album");
		
		genre = (Genre)this.getIntent().getSerializableExtra("Genre");
		
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		intentFiltersArray = new IntentFilter[] { ndef };
		techListsArray = new String[][] { new String[] { Ndef.class.getName() } };
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter == null || !mAdapter.isEnabled()) {
			finish();
		}
	}
	
	public void onPause() {
		super.onPause();
		mAdapter.disableForegroundDispatch(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Ndef ndef = Ndef.get(tagFromIntent);
		String name = null;
		String artist = null;
		int year;
		int id;
		if(type != -1){
			if(type==ACTION_WRITE_ALBUM){
				Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				NdefRecord recordType = NdefRecord.createUri(Integer.toString(ACTION_READ_ALBUM));
		        NdefRecord recordId = NdefRecord.createUri(Integer.toString(album.id));
		        NdefRecord recordAlbum = NdefRecord.createUri(album.name);
		        NdefRecord recordArtist = NdefRecord.createUri(album.artist);
		        NdefRecord recordYear = NdefRecord.createUri(Integer.toString(album.year));
		        NdefMessage message = new NdefMessage(new NdefRecord[] {recordType,recordId,recordAlbum,recordArtist,recordYear});
		        if (writeTag(message, detectedTag)) {
		            Toast.makeText(this, "Success: Wrote placeid to nfc tag", Toast.LENGTH_LONG)
		                .show();
					this.finish();
		        }
			}
			else if(type==ACTION_WRITE_GENRE){
				Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				NdefRecord recordType = NdefRecord.createUri(Integer.toString(ACTION_READ_GENRE));
		        NdefRecord recordId = NdefRecord.createUri(Integer.toString(genre.id));
		        NdefRecord recordName = NdefRecord.createUri(genre.name);
		        NdefMessage message = new NdefMessage(new NdefRecord[] {recordType,recordId,recordName});
		        if (writeTag(message, detectedTag)) {
		            Toast.makeText(this, "Success: Wrote placeid to nfc tag", Toast.LENGTH_LONG)
		                .show();
					this.finish();
		        }
			}
		}
		else{
			try {
				ndef.connect();
				NdefMessage mess = ndef.getNdefMessage();
				NdefRecord[] rec = mess.getRecords();
				
				type = Integer.parseInt(parseNFC(rec[0].getPayload()));
				
				if(type==ACTION_READ_ALBUM){
					id = Integer.parseInt(parseNFC(rec[1].getPayload()));
					name = parseNFC(rec[2].getPayload());
					artist = parseNFC(rec[3].getPayload());
					year = Integer.parseInt(parseNFC(rec[4].getPayload()));
					
					Album album = new Album(id, name, artist, year);
					nfcController.playMusic(album);
				}
				else if(type==ACTION_READ_GENRE){
					id = Integer.parseInt(parseNFC(rec[1].getPayload()));
					name = parseNFC(rec[2].getPayload());
					
					Genre genre = new Genre(id, name);
					nfcController.playMusic(genre);
				}
				this.finish();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private boolean writeTag(NdefMessage message, Tag tag) {
	    int size = message.toByteArray().length;
	    try {
	        Ndef ndef = Ndef.get(tag);
	        if (ndef != null) {
	            ndef.connect();
	            if (!ndef.isWritable()) {
					Toast.makeText(getApplicationContext(),
					"Error: tag not writable",
					Toast.LENGTH_SHORT).show();
	                return false;
	            }
	            if (ndef.getMaxSize() < size) {
					Toast.makeText(getApplicationContext(),
					"Error: tag too small",
					Toast.LENGTH_SHORT).show();
	                return false;
	            }
	            ndef.writeNdefMessage(message);
	            return true;
	        } else {
	            NdefFormatable format = NdefFormatable.get(tag);
	            if (format != null) {
	                try {
	                    format.connect();
	                    format.format(message);
	                    return true;
	                } catch (IOException e) {
	                    return false;
	                }
	            } else {
	                return false;
	            }
	        }
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	private String parseNFC(byte[] payload){
        String text = null;
		try {
        	String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        	int languageCodeLength = payload[0] & 0077;
			String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
			text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
        return text;
	}
}
