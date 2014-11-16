import java.io.*;
import java.util.*;

public class Parser1{
    static String[] todayArr = {"what is my schedule today", "what am i taking today"};
    static String[] weekArr = {"what is my schedule this week", "what am i taking this week"};


    static ArrayList<String> today = new ArrayList<String>(Arrays.asList(todayArr));
    static ArrayList<String> week = new ArrayList<String>(Arrays.asList(weekArr));

    
    public Parser1(){}
    
    public static void parse(String str){
        str = str.toLowerCase();
        if(today.contains(str)){
            dayLayout();
        }
        else if(week.contains(str)){
            weeklyLayout();
        }
        else if(str.equals("what medication is next")){
            nextLayout();
        }
        else if(str.startsWith("remind me to take ")){
            String[] rest = str.split("remind me to take ")[0].split("on");
            if(rest.length!=2){
                idkLayout();
            }
            else{
                String[] numberDrug = restArr[0].split();
                writePill(new PillReminder(numberDrug[numberDrug.length-1], parseDate(restArr[1])*countPills(numberDrug[0])));
            }
        }
        else{
            idkLayout();
        }
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
        if(str.equals("three")){
            return 3;
        }
        if(str.equals("four")){
            return 4;
        }
        if(str.equals("five")){
            return 5;
        }
        if(str.equals("six")){
            return 6;
        }
        if(str.equals("seven")){
            return 7;
        }
        if(str.equals("eight")){
            return 8;
        }
        if(str.equals("nine")){
            return 9;
        }
        return 0;
    }

    // next few methods determine the layouts depending the speech that was recognized, but i don't know what exactly they're supposed to return/do
    public static void writePill(){
        // don't know what goes here
    }
    public static void dayLayout(){
        // don't know what goes here
    }
    public static void weeklyLayout(){
        // don't know what goes here
    }
    public static void idkLayout(){
        // don't know what goes here
    }
}
