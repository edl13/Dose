package com.example.Dose;
import com.firebase.client.ServerValue;
import java.sql.Time;
import java.util.*;

import com.shaded.fasterxml.jackson.annotation.JsonIgnore;

public class PillReminder {
	
	public String Id;
	public String PillName;
	public int PillNum;
	public int PillTimes;
	
	public PillReminder(String s, int pn, int pt){
		PillName = s;
		PillNum = pn;
		PillTimes = pt;
	}
	
	public PillReminder(String s) {
		this(s.split(" ")[0], Integer.parseInt(s.split(" ")[1]), Integer.parseInt(s.split(" ")[2]));
	}
	
	

	public void setPillName(String s){
		this.PillName = s;
	}
	public void setPillTimes(int pt){
		this.PillTimes = pt;
	}
	public void setPillNum(int pn){
		this.PillNum = pn;
	}
	
	@JsonIgnore
	public String getPillName(){
		return this.PillName;
	}
	@JsonIgnore
	public int getPillTimes(){
		return this.PillTimes;
	}
	@JsonIgnore
	public int getPillNum(){
		return this.PillNum;
	}
	
	public PillReminder clone() {
		return new PillReminder(PillName, PillNum, PillTimes);
	}
	
	public String toString() {
		return PillName + " " + PillNum + " " + PillTimes;
	}
	
}
