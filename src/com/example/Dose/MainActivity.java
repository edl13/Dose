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
import com.firebase.client.*;
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

public class MainActivity extends Activity {



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
	
	private Firebase myFirebaseRef;
	private Firebase pillList;
	
	public ArrayList<PillReminder> realPillList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    Firebase.setAndroidContext(this);
	    myFirebaseRef = new Firebase("https://vivid-torch-7263.firebaseio.com/");

		pillList = myFirebaseRef.child("pillList");
		
		realPillList = new ArrayList<PillReminder>();

		txtText = (TextView) findViewById(R.id.txtText);
		IntxtText = (TextView)findViewById(R.id.IntxtText);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

		pillList.addValueEventListener(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot snapshot) {
		        System.out.println(snapshot.getValue());
//		        if (snapshot.getValue()!=null)
//		        	IntxtText.setText("lol");
		    
//		    	snapshot.getValue(pillList);
		    }
		    @Override
		    public void onCancelled(FirebaseError firebaseError) {
		        System.out.println("The read failed: " + firebaseError.getMessage());
		    }
		});


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
//		pillList.setValue(P);
		//		writePill(P);

		switch (requestCode) {
		case RESULTCODE: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				txtText.setText(text.get(0));
				String SpokenText = txtText.getText().toString();
				//speakText(SpokenText);
				parse(SpokenText);
				//P = new PillReminder(SpokenText, 1000, 1112111);
				//realPillList.add(P);
//				try {
//				pillList.push().setValue(P);}
//				catch (Throwable e) {
//					e.printStackTrace();
//				}
			}
			break;
		}

		}

	}

	

    public void parse(String str){
        str = str.toLowerCase();
        if (str.contains("week")) this.gotoScheduleView();
        else if(str.contains("next") || str.contains("today")){
        	boolean returned = false;
        	for (PillReminder pm: realPillList) {
        		if (parseHash(pm.getPillTimes(), "sunday")) {
        			returned=true;
                	String plural;
                	if(pm.getPillNum()>=1){
                		plural = "s";
                	}
                	else{
                		plural = "";
                	}
                	speakText("Your next medication is " + pm.getPillNum() + pm.getPillName() + plural);
                }
        		}
        	if (!returned) speakText("There are none today.");
        	}
        else if(str.startsWith("remind me to take ")){
        	String rest = str.substring(18);
        	 
        	String[] breakup = rest.split("on");
        	
            //String name = rest[0].split(" ")[1];
//            
        	  int amount = countPills(breakup[0].split(" ")[0]);
        	  String name = breakup[0].split(" ")[1];
        	  int days = parseDate(breakup[1]);
        	  realPillList.add(new PillReminder(name, amount, days));
        	  speakText("Added " + amount + " " + name + "to your schedule");
//            int days = parseDate(rest[1]);
//            realPillList.add(new PillReminder(name, amount, days));
        }
        else{
        	speakText("Command not recognized. Try again.");
        }
//            else{
//                String[] numberDrug = restArr[0].split();
//                writePill(new PillReminder(numberDrug[numberDrug.length-1], parseDate(restArr[1])*countPills(numberDrug[0])));
//            }
//        }
//        else{
//            idkLayout();
//        }
    }
    public static boolean parseHash(int integ, String day) {
    	String intstr = ""+integ;
    	return intstr.charAt(intstr.length()-1)=='1';
//    	for (int i = 0; i < 7-intstr.length(); i++)
//    		intstr = "0" + intstr;
//    	if (day.equals("monday")) return intstr.charAt(0)=='1';
//    	if (day.equals("tuesday"))return intstr.charAt(1)=='1';
//    	if (day.equals("wednesday"))return intstr.charAt(2)=='1';
//    	if (day.equals("thursday"))return intstr.charAt(3)=='1';
//    	if (day.equals("friday"))return intstr.charAt(4)=='1';
//    	if (day.equals("saturday"))return intstr.charAt(5)=='1';
//    	if (day.equals("sunday"))return intstr.charAt(6)=='1';
//    	else return false;
    }
    public static int parseDate(String str){
        int result = 0;
        if(str.contains("monday")){
            result += 1000000;
        }
        if(str.contains("tuesday")){
            result += 100000;
        }
        if(str.contains("wednesday")){
            result += 10000;
        }
        if(str.contains("thursday")){
            result += 1000;
        }
        if(str.contains("friday")){
            result += 100;
        }
        if(str.contains("saturday")){
            result += 10;
        }
        if(str.contains("sunday")){
            result += 1;
        }
        return result;
    }

    public static int countPills(String str){
        if(str.equals("one")){
            return 1;
        }
        if(str.equals("two") || str.equals("to") || str.equals("too")){
            return 2;
        }
        if(str.equals("three") || str.contains("3")){
            return 3;
        }
        if(str.equals("four") || str.contains("4")){
            return 4;
        }
        if(str.equals("five") || str.contains("5")){
            return 5;
        }
        if(str.equals("six") || str.contains("6")){
            return 6;
        }
        if(str.equals("seven") || str.contains("7")){
            return 7;
        }
        if(str.equals("eight") || str.contains("8")){
            return 8;
        }
        if(str.equals("nine") || str.contains("9")){
            return 9;
        }
        return 0;
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

	/** Called when the user clicks the Schedule button */
	public void gotoScheduleView(View view) {
		Intent intent = new Intent(this, PillListViewActivity.class);
		ArrayList<String> strArr = new ArrayList<String>();
		for (PillReminder p: realPillList)
			strArr.add(p.toString());
		intent.putExtra("pillList",  strArr);
		startActivity(intent);
	}
	public void gotoScheduleView() {
		Intent intent = new Intent(getApplicationContext(), PillListViewActivity.class);
		ArrayList<String> strArr = new ArrayList<String>();
		for (PillReminder p: realPillList)
			strArr.add(p.toString());
		intent.putExtra("pillList",  strArr);
		startActivity(intent);
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

