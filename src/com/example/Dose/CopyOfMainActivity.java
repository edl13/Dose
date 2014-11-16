package com.example.Dose;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.sql.Time;
import java.util.*;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.net.MalformedURLException;
import android.os.AsyncTask;

import com.example.Dose.R;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

public class CopyOfMainActivity extends Activity {



	protected static final int RESULTCODE = 11111;

	private ImageButton btnSpeak;
	private TextView txtText;
	private TextView IntxtText;
	private List<PillReminder> temp;
	private TextToSpeech ttobj;
	private PillReminder P;
	private List<PillReminder> PillList;
	private ListView listView;
	private ArrayList<String> nameArr;
	private ArrayList<Integer> numArr;
	private ArrayList<Integer> timeArr;

	private MobileServiceClient mClient;
	private MobileServiceTable<PillReminder> PillTable;


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		


		txtText = (TextView) findViewById(R.id.txtText);
		IntxtText = (TextView)findViewById(R.id.IntxtText);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

		// Create the Mobile Service Client instance, using the provided
		// Mobile Service URL and key
		try {
			mClient = new MobileServiceClient(
					"https://doseapp.azure-mobile.net/",
					"CdCRbcIjrIYTLxBxdlyTGKjNhVafNb81", 
					this);
			PillTable = mClient.getTable(PillReminder.class);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// Get the Mobile Service Table instance to use




		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en_US");

				try {
					startActivityForResult(intent, RESULTCODE);
					ttobj=new TextToSpeech(getApplicationContext(), 
							new TextToSpeech.OnInitListener() {
						@Override
						public void onInit(int status) {
							if(status != TextToSpeech.ERROR){
								ttobj.setLanguage(Locale.US);
							}				
						}
					});
					txtText.setText("");
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Opps! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//		writePill(P);

		switch (requestCode) {
		case RESULTCODE: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				txtText.setText(text.get(0));
				String SpokenText = txtText.getText().toString();
				speakText(SpokenText);

				P = new PillReminder(SpokenText, 1000, 1112111);
				writePill(P);
				List<PillReminder> lst;
				try {
					lst = readPill();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					lst = new ArrayList<PillReminder>();
					e.printStackTrace();
					
				}
				//IntxtText.setText(lst.get(0).getPillName());

			}
			break;
		}

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void speakText(String toSpeak){
		Toast.makeText(getApplicationContext(), toSpeak, 
				Toast.LENGTH_SHORT).show();
		ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

	}

	@SuppressWarnings({ "deprecation", "deprecation" })
	public void writePill(PillReminder P){
		PillTable.insert(P, new TableOperationCallback<PillReminder>() {
			public void onCompleted(PillReminder entity, Exception exception, ServiceFilterResponse response) {
				if (exception == null) {
					// Insert succeeded
				} else {
					// Insert failed
				}
			}
		});
	}
	@SuppressWarnings("deprecation")
	public List<PillReminder> readPill() throws Throwable{
		temp = new ArrayList<PillReminder>();
		nameArr = new ArrayList<String>();
		timeArr = new ArrayList<Integer>();
		numArr = new ArrayList<Integer>();
		PillTable.execute(new TableQueryCallback<PillReminder>(){
			@Override
			public void onCompleted(List<PillReminder> P, int count, Exception exception, ServiceFilterResponse response){
				if(exception == null){
					for (int i = 0; i < P.size(); i++) {
						nameArr.add(P.get(i).getPillName());
						timeArr.add(P.get(i).getPillTimes());
						numArr.add(P.get(i).getPillNum());
					}

				}
				else{
					IntxtText.setText("lol");
				}

			}		
		});
		for(int i = 0; i < numArr.size(); i++){
			PillReminder PTemp = new PillReminder(nameArr.get(i), numArr.get(i), timeArr.get(i));
			temp.add(PTemp);
		}				
		IntxtText.setText(String.valueOf(temp.isEmpty()));

		return temp;
	}

//	public void PTransfer(List<PillReminder> P){
//		PillList = P;
//	}

}

