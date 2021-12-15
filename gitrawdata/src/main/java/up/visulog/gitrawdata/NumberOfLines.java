package up.visulog.gitrawdata;

import java.io.BufferedReader;
//import up.visulog.analyzer.CountCommitsPerDayPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;
import java.text.ParseException;



public class NumberOfLines{
	String name;
	private int addDay;
	private int delDay;
	private LocalDate date;
	//private Date date;
	private NumberOfLines yesterday;
	
	 private static List<NumberOfLines> days = new ArrayList<NumberOfLines>();
	
	public NumberOfLines(int add,int del) {
		this.date = LocalDate.now() ;
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
		
		}else {
			this.addDay = add;
			this.delDay = del;
		}
			
		//add the day in the list
		days.add(this);
		
	}
	
	public NumberOfLines(LocalDate date) {
		this.date = date;
		
	}
	
	//git diff --shortstat
    //we get all the lines deleted and added today
    public static BufferedReader executeCommand(Path gitPath,List<String> command) {
        File f =gitPath.toFile();
        
        ProcessBuilder builder = new ProcessBuilder(command).directory(gitPath.toFile());
        
        Process process ;
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
    
    public static List<NumberOfLines> parseLogFromCommand(Path gitPath) {
        List<String> command = new ArrayList<>();
        
        //command.add("log");
       // command.add("--pretty=%nName :%an %nDate :%ad");
        //command.add("--numstat");
        command.add("diff");
        command.add("--shortstat");
        
       
        
        return parseNumberOfLines( Commit.executeGitCommand(gitPath, command) );
    }
    
    public static List<NumberOfLines> parseNumberOfLines(BufferedReader reader) {
    	
       try {
    	   String line = reader.readLine();
    	   // line look like " 3 files changed, 68 insertions(+), 43 deletions(-)"
    	   String split[] = line.split(" ");
			ArrayList<String> splited = new ArrayList<String>();
			for(String s:split) {
				splited.add(s);
			}
			//the 2 values lines added and deleted are now into a list
			ArrayList<Integer> datas = getData(splited);
			
			//if the day has already been analyzed we are doing an update 
			if(!days.isEmpty()) {
				if(days.get(days.size()).date == LocalDate.now()) {
					days.get(days.size()).Update(datas.get(1),datas.get(2));
				}else {
					NumberOfLines newDay = new NumberOfLines(datas.get(1),datas.get(2));
				}
			}
			
			NumberOfLines newDay = new NumberOfLines(datas.get(1),datas.get(2));
		
			return days;
		}catch (Exception e) {
			e.printStackTrace();	
		
	}
        
		return getDays();
        
        
    }
    
    public String toString() {
    	int total = this.getAddDay()-this.getDelDay();
    	return "Data for "+this.name+" lines added: "+this.getAddDay()+" lines deleted: "+this.getDelDay()+" total today: "+total;
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
	
	public static ArrayList<Integer> getData(ArrayList <String> splited) {
		//put the numbers finded in a list
		ArrayList <Integer> result = new ArrayList <Integer>();
		for(String s: splited) {
			if(isNumber(s)) {
				result.add(Integer.valueOf(s));
			}
		}
		return result;
	}
	
	
	
	public void Update(int newadd,int newdel) {
		//today is deleted and replace in the list
		 NumberOfLines up = new NumberOfLines(this.date);
		 
		 if(this.yesterday!=null) {
			 up.setYesterday(this.getYesterday());
			 up.setAddDay(this.yesterday.getAddDay()-newadd);
			 up.setAddDay(this.yesterday.getDelDay()-newdel);
			 
			 if(up.getAddDay() < 0)
				 up.setAddDay(0);
			 if(up.getDelDay()<0)
				 up.setDelDay(0);
		 }else {
			 up.setAddDay(newadd);
			 up.setDelDay(newdel);
		 }
		
		 up.name = LocalDate.now().toString();
		 
		 days.remove(this);
		 
		 days.add(up);
	}
	
	private static String getLastLine(BufferedReader reader) throws IOException {
	    String line = null;
	    String nextLine;
	    while ((nextLine = reader.readLine()) != null) {
	        line = nextLine;
	    }
	    return line;
	}
	 public static void main(String[] args) {
	        Path gitPath = FileSystems.getDefault().getPath(".");
	        var res=parseLogFromCommand(gitPath);
	        System.out.println(res.toString());
	    }
	
	
}
	
	
	
	


