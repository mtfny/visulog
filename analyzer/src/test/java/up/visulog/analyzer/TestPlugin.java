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
		for(int i=1; i<11; i++) {
			System.out.print(i + " ");
		}
		try {
			Thread.sleep(100);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Result getResult() {
		return null;
	}
}
