package up.visulog.analyzer;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import up.visulog.config.Configuration;
import up.visulog.gitrawdata.NumberOfLines;
import up.visulog.gitrawdata.NumberOfLinesPerAuthor;

public class CountLinesPerDay implements AnalyzerPlugin{
	private final Configuration configuration;
    private Result result;
    
    public CountLinesPerDay(Configuration config) {
    	this.configuration = config;
    }
    
 
    
    @Override
    public void run() {
    	result = new Result(NumberOfLines.parseLogFromCommand());
    }
    
    @Override
    public Result getResult() {
    	if(result == null) run();
    	return result;
    }
    
   
    static class Result implements AnalyzerPlugin.Result{
    	 private List<NumberOfLines> data;
    	 
    	 public Result(List<NumberOfLines> gitLog) {
    		 
    	 }

		@Override
		public String getResultAsString() {
			String s= "";
			for(NumberOfLines today:data)
			s += today.toString()+"\n";
			return s;
		}

		@Override
		public String getResultAsHtmlDiv() {
			StringBuilder html = new StringBuilder("<div>Lines per Days: <ul>");
            for (NumberOfLines item : data) {
                html.append("<li>").append(item.getName()).append(": ").append("lines added: ").append(item.getAddDay()).append("lines removed: ").append(item.getDelDay()).append("total").append(item.getAddDay()-item.getDelDay()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
		}
    	
    }
}
