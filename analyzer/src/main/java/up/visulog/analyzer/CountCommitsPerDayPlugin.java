package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import java.util.Calendar;
import java.time.LocalDate;

import java.util.TreeMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerDayPlugin implements AnalyzerPlugin {
	private final Configuration configuration;
    private Result result;
    private static final int maxCommit = 10;

    public CountCommitsPerDayPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    //Returns a result object that contains the number of commits per day
    static Result processLog(List<Commit> gitLog) {   	
    	Result result = new Result();
    	if(gitLog == null)
    		return result;
    	
    	Calendar cal = Calendar.getInstance();
    	
    	//Two DateObj variables are created in order to retrieve the dates of both the earliest and latest commits contained in the gitLog list.
    	DateObj dateFirst = null;
    	DateObj dateLast = null;
    	
        for (var commit : gitLog) {
        	if(commit.date != null) {
        		cal.setTime(commit.date);
        		DateObj date = new DateObj(cal.get(cal.DAY_OF_MONTH), cal.get(cal.DAY_OF_WEEK), cal.get(cal.MONTH), cal.get(cal.YEAR));
        		Integer nb = result.commitsPerDate.getOrDefault(date, 0);
        		result.commitsPerDate.put(date, nb + 1);
        		
        		if(dateFirst == null || date.compareTo(dateFirst) == -1) dateFirst = date;
        		if(dateLast == null || date.compareTo(dateLast) == 1) dateLast = date;
        	}
        }
        
        LocalDate first = LocalDate.of(dateFirst.year, dateFirst.month, dateFirst.day);
        LocalDate last = LocalDate.of(dateLast.year, dateLast.month, dateLast.day);
        
        //We iterate between the dates of the earliest and latest commits of the gitLog list. If a date in that interval of time is not a key in the result.commitsPerDate TreeMap (if no commits were published that day), then it is added as a key to the TreeMap with an associated value of 0 (so as to indicate that no commits were published that day).
        for (LocalDate date = first; date.isBefore(last); date = date.plusDays(1)) {
        	DateObj dateIter = new DateObj(date.getDayOfMonth(), date.getDayOfWeek().ordinal(), date.getMonthValue(), date.getYear());
        	if(! result.commitsPerDate.containsKey(dateIter)) {
        		result.commitsPerDate.put(dateIter, 0);
        	}
        }
        return result;
    }

    @Override
    public void run() {
    	if(configuration != null)
    		result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
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
            StringBuilder html = new StringBuilder("<div id=\"module\" hidden>commitActivity</div><div class=\"stat-container\"><div id =\"day-list\"><div id=\"month-space\"></div><div id=\"days-list-name\"><p>Lun</p><p>Mar</p><p>Mer</p><p>Jeu</p><p>Ven</p><p>Sam</p><p>Dim</p></div><div id =\"stats-grid\"><p id=\"max-commit\" hidden>" + maxCommit + "</p><div class=\"months\">");
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
            
            html.append("</div></div></div>");
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
