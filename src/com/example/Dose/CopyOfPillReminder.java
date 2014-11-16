package com.example.Dose;
import java.sql.Time;
import java.util.*;

public class CopyOfPillReminder {
	
	public String Id;
	public String PillName;
	public int PillNum;
	public int PillTimes;
	
	public CopyOfPillReminder(String s, int pn, int pt){
		PillName = s;
		PillNum = pn;
		PillTimes = pt;
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
	public String getPillName(){
		return this.PillName;
	}
	public int getPillTimes(){
		return this.PillTimes;
	}
	public int getPillNum(){
		return this.PillNum;
	}
	
	public CopyOfPillReminder clone() {
		return new CopyOfPillReminder(PillName, PillNum, PillTimes);
	}
	
}
