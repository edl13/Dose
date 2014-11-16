package com.example.Dose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.splash);
		Thread logoTimer = new Thread() {
            public void run(){
                try{
                    int logoTimer = 0;
                    while(logoTimer < 1500){
                        sleep(100);
                        logoTimer = logoTimer +100;
                    };
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                
                } 
                 
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                 
                finally{
                    finish();
                }
            }
        };
         
        logoTimer.start();
	}

}
