package up.visulog.analyzer;

import up.visulog.config.Configuration;

//Simplified AnalyzerPlugin class that is used to easily test whether AnalyzerPlugin instances are being run in parallel as expected
public class Test2Plugin extends TestPlugin{
	private final Configuration configuration;
    private Result result;
	
    public Test2Plugin (Configuration generalConfiguration) {
    	super(generalConfiguration);
    	this.configuration = generalConfiguration;
    }
}
