package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberOfLines{
	String name;
	private int addDay;
	private int delDay;
	final LocalDate date;
	private NumberOfLines yesterday;
	
	 private static List days = new ArrayList<NumberOfLines>();
	
	public NumberOfLines(int add,int del) {
		this.date = LocalDate.now();
		this.name = this.date.toString();
		
		int addlines;
		int delLines;
		
		//if the list is empty it means that is the first time we have counted the lines
		if(getDays().isEmpty()) {
			yesterday = null;
		}
		
		//calculates values compared to yesterday 
		if(yesterday != null) {
			addlines = this.getAddDay() - yesterday.getAddDay();
			delLines = this.getDelDay() - yesterday.getDelDay();
			if(addlines <0)
				addlines = 0;
			if(delLines <0)
				delLines = 0;
			this.setAddDay(addlines);
			this.setDelDay(delLines);
		
		}
			
		//add the day in the list
		getDays().add(this);
		
	}
	
	public NumberOfLines() {
		this.date = null;
		
	}
	
	//git log --pretty=tformat: --numstat | awk '{ add += $1; subs += $2; loc += $1 - $2 } END { printf "added lines: %s, removed lines: %s, total lines: %s\n", add, subs, loc }' -
    //we get all the lines 
    public static BufferedReader executeCommand(List<String> command) {
        Path gitPath = FileSystems.getDefault().getPath(".");
        ProcessBuilder builder = new ProcessBuilder(command).directory(gitPath.toFile());
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            String message="";
            for(String s : command){
                message+=s+" ";
            }
            throw new RuntimeException("Error running \"git "+message+"\".", e);
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader;
    }
    
    public static List<NumberOfLines> parseLogFromCommand() {
        List<String> command = new ArrayList<>();
        command.add("git");
        command.add("log");
        command.add("--pretty=%n %an");
        command.add("--numstat");
        return parseNumberOfLines( executeCommand(command) );
    }
    
    public static List<NumberOfLines> parseNumberOfLines(BufferedReader reader) {
    	
       try {
		String line = reader.readLine();
		
		String split[] = line.split(" ");
		ArrayList<String> splited = new ArrayList();
		int i =0;
		
		for(String s:split) {
			String[] str = s.split(",",0);
			splited.add(str[i]);
		}
		//the 3 values lines added,deleted and total lines are now int a tab
		List <Integer> datas = getData(splited);
		if(((NumberOfLines) getDays().get(getDays().size()-1)).getDate().equals(LocalDate.now())) {
			//if the day has already been analysed we are doing an update 
			Update(getDays().get(getDays().size()-1),datas.get(0),datas.get(1));
		}else {
			//else create the day in the list
			NumberOfLines newDay = new NumberOfLines(datas.get(0),datas.get(1));
		}
		
		
		return getDays();
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}
        
		return getDays();
        
        
    }
    
    public String toString() {
    	return "Data for "+this.name+" lines added: "+this.getAddDay()+"lines deleted: "+this.getDelDay();
    }
	public int getAddDay() {
		return addDay;
	}

	public void setAddDay(int addDay) {
		this.addDay = addDay;
	}

	public int getDelDay() {
		return delDay;
	}

	public void setDelDay(int delDay) {
		this.delDay = delDay;
	}

	public NumberOfLines getYesterday() {
		return yesterday;
	}

	public void setYesterday(NumberOfLines yesterday) {
		this.yesterday = yesterday;
	}

	public LocalDate getDate() {
		return date;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static List<NumberOfLines> getDays() {
		return days;
	}

	private static boolean isNumber(String str){
   
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
  
        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }
  
        // Find match between string and regular expression
        Matcher m = p.matcher(str);
  
        //final result
        return m.matches();
    }
	
	public static List<Integer> getData(ArrayList <String> splited) {
		//put the numbers finded in a list
		List <Integer> result = new ArrayList <Integer>();
		for(String s: splited) {
			if(isNumber(s)) {
				result.add(Integer.valueOf(s));
			}
		}
		return result;
	}
	
	
	
	public static void Update(Object object,int newadd,int newdel) {
		getDays().remove(object);
		object = new NumberOfLines(newadd,newdel);
	}
	
	
	}
	
	
	
	


