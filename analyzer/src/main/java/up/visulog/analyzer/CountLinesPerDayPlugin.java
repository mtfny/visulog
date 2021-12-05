package up.visulog.analyzer;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import up.visulog.config.Configuration;
import up.visulog.gitrawdata.NumberOfLines;
import up.visulog.gitrawdata.NumberOfLinesPerAuthor;

public class CountLinesPerDayPlugin implements AnalyzerPlugin{
	private final Configuration configuration;
    private Result result;
    
    public CountLinesPerDayPlugin(Configuration config) {
    	this.configuration = config;
    }
    
 
    
    @Override
    public void run() {
    	result = new Result(NumberOfLines.parseLogFromCommand(configuration.getGitPath()));
    }
    
    @Override
    public Result getResult() {
    	if(result == null) run();
    	return result;
    }
    
   
    static class Result implements AnalyzerPlugin.Result{
    	 private List<NumberOfLines> data;
    	 
    	 public Result(List<NumberOfLines> gitLog) {
    		 
    		 this.data = gitLog;
    	 }

		@Override
		public String getResultAsString() {
			
			String s= "";
			for(NumberOfLines today: this.data)
			s += today.toString()+"\n";
			return s;
		}

		@Override
		public String getResultAsHtmlDiv() {
			StringBuilder html = new StringBuilder("<div class=\"module\" hidden>linesPerDay</div><div id=\"data-lines-per-day\" hidden>");
            
            for (var item : data) {
                html.append("<div data-date=\"").append(item.getDate() + "\"").append(item.getAddDay() + item.getDelDay()).append("</div>");
            }
            html.append("</div>");
            return html.toString();
		}
    	
    }
}
