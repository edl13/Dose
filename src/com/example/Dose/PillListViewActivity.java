package com.example.Dose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class PillListViewActivity extends Activity {

	private List<PillReminder> myPills = new ArrayList<PillReminder>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pill_list_view);
		
		//myPills.add(new PillReminder("hello", 1234,1234));
		populatePillList();
		populateListView();
		registerClickCallback();
	}
	private void registerClickCallback() {
		ListView list = (ListView)findViewById(R.id.daily_pill_listview);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// playback sound, right now it only does visual text cues
				PillReminder clickedPill = myPills.get(position);
				String message = "You clicked position "+position+" which is "+clickedPill.getPillName();
				Toast.makeText(PillListViewActivity.this, message, Toast.LENGTH_SHORT/2).show();
			}
		});
	}
	private void populateListView() {
		ArrayAdapter<PillReminder> adapter = new MyListAdapter();
		ListView list = (ListView) findViewById(R.id.daily_pill_listview);
		list.setAdapter(adapter);
	}
	public class MyListAdapter extends ArrayAdapter<PillReminder>{
		public MyListAdapter(){
			super(PillListViewActivity.this, R.layout.pill_view_daily, myPills);
		}
		public View getView(int position, View convertView, ViewGroup parent){
			View itemView = convertView;
			if(itemView == null){
				itemView = getLayoutInflater().inflate(R.layout.pill_view_daily, parent, false);
				
				
			}
			
			PillReminder currentPill = myPills.get(position);
			TextView nameText = (TextView)itemView.findViewById(R.id.pill_name);
			nameText.setText(currentPill.getPillName());
			TextView numText = (TextView)itemView.findViewById(R.id.pill_number);
			numText.setText(""+currentPill.getPillNum());
			return itemView;
			//return super.getView(position, convertView, parent);
		}
	}
	public int populatePillList(){
		Intent intent = getIntent();
		ArrayList<String> s = (ArrayList<String>)intent.getSerializableExtra("pillList");
//		myPills = realPillList;
		//ArrayList<PillReminder> myPills = new ArrayList<PillReminder>();
//		myPills.add(new PillReminder("Hello",1234,12346));
		for (String str: s)
			myPills.add(new PillReminder(str));
		return 1;
		// input pill reminders from firebase
		/*

	    Firebase myFirebaseRef = new Firebase("https://vivid-torch-7263.firebaseio.com/");

		Firebase pillList = myFirebaseRef.child("pillList").child( "-JasFSVxKoO7OQieCPBf");
		

		pillList.addValueEventListener(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot snapshot) {
		        System.out.println(snapshot.getValue());
		        if (snapshot.getValue()!=null){
		        	PillReminder pill = snapshot.getValue(PillReminder.class);
		        	myPills.add(pill);
		        	
		        		
//		        	GenericTypeIndicator<ArrayList<PillReminder>> t = new GenericTypeIndicator<ArrayList<PillReminder>>() {};
//		        	myPills = (ArrayList<PillReminder>) snapshot.getValue(t);
		        }
		    }
		    @Override
		    public void onCancelled(FirebaseError firebaseError) {
		        System.out.println("The read failed: " + firebaseError.getMessage());
		    }
		});
		return 1;*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pill_list_view, menu);
		return true;
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
}
