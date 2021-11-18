package up.visulog.analyzer;

import up.visulog.config.Configuration;

public class CountLinesPerDay implements AnalyzerPlugin{
	private final Configuration configuration;
    private Result result;
    
    public CountLinesPerDay(Configuration config) {
    	this.configuration = config;
    }
    
   /* static Result processLog() {
    	
    }*/
    
    @Override
    public void run() {
    	
    }
    
    @Override
    public Result getResult() {
    	if(result == null) run();
    	return result;
    }
}
