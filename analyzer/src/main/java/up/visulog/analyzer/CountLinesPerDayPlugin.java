package up.visulog.analyzer;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.NumberOfLinesPerAuthor;

public class CountLinesPerDayPlugin extends DateAnalyzerPlugin{
	private final Configuration configuration;
    private Result result;
    
    public CountLinesPerDayPlugin(Configuration config) {
    	this.configuration = config;
    }
    
    static Result processLog(List<NumberOfLinesPerAuthor> gitLog) {   	
    	Result result = new Result();
    	if(gitLog == null || gitLog.isEmpty())
    		return result;
    	
    	Calendar cal = Calendar.getInstance();
		
    	//Two DateObj variables are created in order to retrieve the dates of both the earliest and latest commits contained in the gitLog list.
    	DateObj dateFirst = null;
    	DateObj dateLast = null;
    	
        for (var line : gitLog) {
        	if(line.getDate() != null) {
        		cal.setTime(line.getDate());
        		DateObj date = null;
        		//Since the value DAY_OF_WEEK attribute of Calendar objects start with Sundays, an if/else statement for the case in which the day of the week is a Sunday is needed
        		int dayOfWeek = cal.get(cal.DAY_OF_WEEK);
        		if(dayOfWeek == 1) {
        			dayOfWeek = 7;
        		}else {
        			dayOfWeek--;
        		}
        		date = new DateObj(cal.get(cal.DAY_OF_MONTH), dayOfWeek, cal.get(cal.MONTH)+1, cal.get(cal.YEAR));
        		Integer nb = result.linesPerDay.getOrDefault(date, 0);
        		result.linesPerDay.put(date, nb + line.getLines_added() + line.getLines_removed());
        		
        		if(dateFirst == null || date.compareTo(dateFirst) == -1) dateFirst = date;
        		if(dateLast == null || date.compareTo(dateLast) == 1) dateLast = date;
        	}
        }
        
        //The dates of the first and last commits of the gitlog list are retrieved.
        LocalDate first = LocalDate.of(dateFirst.getYear(), dateFirst.getIntMonth()+1, dateFirst.getDay());
        LocalDate last = LocalDate.of(dateLast.getYear(), dateLast.getIntMonth()+1, dateLast.getDay()+1);
        
        //We iterate between the dates of the earliest and latest commits of the gitLog list. If a date in that interval of time is not a key in the result.commitsPerDate TreeMap (if no commits were published that day), then it is added as a key to the TreeMap with an associated value of 0 (so as to indicate that no commits were published that day).
        for (LocalDate date = first; date.isBefore(last); date = date.plusDays(1)) {
        	DateObj dateIter = new DateObj(date.getDayOfMonth(), date.getDayOfWeek().ordinal()+1, date.getMonthValue(), date.getYear());
        	if(! result.linesPerDay.containsKey(dateIter)) {
        		result.linesPerDay.put(dateIter, 0);
        	}
        }
        return result;
    }
 
    
    @Override
    public void run() {
    	List<String> command = new LinkedList<String>();
    	command.add("log");
    	
    	if(configuration != null) {
    		command.add("--pretty=%nName :%an %nDate :%ad");
            command.add("--numstat");
            
			Map<String, String> settings = configuration.getPluginConfigs().get("CountLinesPerDay").getSettings();
			if(settings.containsKey(dateDebutOption) || settings.containsKey(dateFinOption))
				command = dateAnalysis(command, settings);
			
			List<NumberOfLinesPerAuthor> lines = NumberOfLinesPerAuthor.getAllLinesPerAuthor(Commit.executeGitCommand(configuration.getGitPath(), command));
	    	if(lines.size() != 0)
	    		result = processLog(lines);
		}
    }
    
    @Override
    public Result getResult() {
    	if(result == null) run();
    	return result;
    }
    
   
    static class Result implements AnalyzerPlugin.Result{
    	 private TreeMap<DateObj, Integer> linesPerDay = new TreeMap<DateObj, Integer>();

		@Override
		public String getResultAsString() {
			
			return linesPerDay.toString();
		}

		@Override
		public String getResultAsHtmlDiv() {
			StringBuilder html = new StringBuilder("<div class=\"module\" hidden>linesPerDay</div><div id=\"data-lines-per-day\" hidden>");
            
            for (var item : linesPerDay.entrySet()) {
            	DateObj date = item.getKey();
                html.append("<div data-date=\"").append(date.getDay() + " " + date.getMonth() + "\">").append(item.getValue()).append("</div>");
            }
            
            
            
            html.append("</div>");
            return html.toString();
		}
    	
    }
}
