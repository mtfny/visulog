package up.visulog.analyzer;

import up.visulog.config.Configuration;

//Simplified AnalyzerPlugin class that is used to easily test whether AnalyzerPlugin instances are being run in parallel as expected
public class TestPlugin implements AnalyzerPlugin{
	private final Configuration configuration;
    private Result result;
	
    public TestPlugin (Configuration generalConfiguration) {
    	this.configuration = generalConfiguration;
    }
    
	@Override
	public void run() {
		//The run method of this class consists in printing integers 1 through 10, so as to check whether there is overlap during printing when both a TestPlugin and a Test2Plugin instance are being run. If there is, the plugins are being run in parallel as expected.
		for(int i=1; i<11; i++) {
			System.out.print(i + " ");
		}
	}
	
	@Override
	public Result getResult() {
		return null;
	}
}
