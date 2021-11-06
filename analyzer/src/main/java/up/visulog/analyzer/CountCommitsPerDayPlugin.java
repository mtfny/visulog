package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import java.util.Calendar;

import java.util.TreeMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerDayPlugin implements AnalyzerPlugin {
	private final Configuration configuration;
    private Result result;

    public CountCommitsPerDayPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    //Returns a result object that contains the number of commits per day
    static Result processLog(List<Commit> gitLog) {   	
    	Result result = new Result();
    	if(gitLog == null)
    		return result;
    	
    	Calendar cal = Calendar.getInstance();
    	
        for (var commit : gitLog) {
        	if(commit.date != null) {
        		cal.setTime(commit.date);
        		DateObj date = new DateObj(cal.get(cal.DAY_OF_MONTH), cal.get(cal.DAY_OF_WEEK), cal.get(cal.MONTH), cal.get(cal.YEAR));
        		Integer nb = result.commitsPerDate.getOrDefault(date, 0);
        		result.commitsPerDate.put(date, nb + 1);
        	}
        }    	
        return result;
    }

    @Override
    public void run() {
    	if(configuration != null) {
    		result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    		try {
    			Thread.sleep(100);
    		} catch(InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }

    @Override
    public Result getResult() {
    	//If the analysis hasn't already been run, it is run and only then is the result returned
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
    	//On utilise une treeMap car les éléments sont triés par clé (donc par date)
        private final Map<DateObj, Integer> commitsPerDate = new TreeMap<>();

        //Method that returns the hashmap that contains the number of commits associated with each day
        Map<DateObj, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }

        @Override
        //Method that returns the commitsPerDay list in String form
        public String getResultAsString() {
            return commitsPerDate.toString();
        }

        @Override
        //Method that returns a String which can then be used to display the commitsPerAuthor list as an html page
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div class=\"grid\"><p id=\"max-commit\" hidden></p><div class=\"months\">");
            String lastShortDate ="";
            for (var item : commitsPerDate.entrySet()) {
            	String shortDate = item.getKey().monthAndYear();
            	
            	if(!shortDate.equals(lastShortDate)) {
                    html.append("<p>").append(shortDate).append("</p>");
                    lastShortDate = shortDate;
            	}
            }
            html.append("</div><div id=\"days\">");
            
            for(var item : commitsPerDate.entrySet()) {
            	html.append("<div class=\"case\" data-commit-number=\"").append(item.getValue()).append("\" data-date=\"").append(item.getKey().getDay() + " " + item.getKey().getMonth()).append("\"></div>");
            }
            
            html.append("</div></div>");
            return html.toString();
        }
    }
    
    private static class DateObj implements Comparable<DateObj>{
    	private final int day;
    	private final int weekDay;
    	private final int month;
    	private final int year;
    	private final String[] weekDays = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
		private final String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre","Novembre", "Décembre"};
    	
    	public DateObj(int day, int weekDay, int month, int year) {
    		this.day = day;
    		this.year = year;
    		
    		
    		//On applique -1 car ce sont des index de tableau
    		//Si la valeur du jour/mois n'est pas cohérent (ex : mois = 14) on met 0 par défaut
    		this.weekDay = weekDay-1 < 0 || weekDay-1 >= weekDays.length ? 0 : weekDay-1;
    		this.month = month-1 < 0 || month-1 >= months.length ? 0 : month-1;
    	}
    	
    	public int getDay() { return day; }
    	public String getWeekDay() { return weekDays[weekDay]; }
    	public String getMonth() { return months[month]; }
    	public int getYear() { return year; }
    	public String monthAndYear() { return months[month].substring(0,3) + " " + String.valueOf(year);}
    	
    	public String toString() {
    		return weekDays[weekDay] + " " + String.valueOf(day) + " " + months[month] + " " + String.valueOf(year);
    	}
    	
    	@Override
    	public int compareTo(DateObj o) {
    		if(this.year == o.year) {
    			
    			if(this.month == o.month) {
    				
    				if(this.day == o.day)
    					return 0;
    				if(this.day < o.day)
    					return -1;
    				else
    					return 1;
    			}
    			
    			if(this.month < o.month)
    				return -1;
    			else
    				return 1;
    		}
    		
    		if(this.year < o.year)
    			return -1;
    		else
    			return 1;
    	}
    	
    	public boolean equals(Object o) {
    		if(o == null) return false;
    		if(o == this) return true;
    		if(!(o instanceof DateObj)) return false;
    		DateObj d = (DateObj)o;
    		if(d.day == this.day && d.weekDay == this.weekDay && d.month == this.month && d.year == this.year)
    			return true;
    		return false;
    	}
    	
    	public int hashCode() {
    		int result = 17;
            result = 31 * result + day;
            result = 31 * result + weekDay;
            result = 31 * result + month;
            result = 31 * result + year;
            return result;
    	}
    }
}
