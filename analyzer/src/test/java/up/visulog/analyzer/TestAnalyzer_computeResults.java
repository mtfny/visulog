package up.visulog.analyzer;
import java.util.HashMap;
import up.visulog.config.*;

public class TestAnalyzer_computeResults {

	//This class aims to test the computeResults method of the Analyzer class, mainly to make sure that the plugins we want to run are being run in parallel.
	public static void main(String[] args) {
		
		HashMap<String, PluginConfig> pMap = new HashMap<>();
		
		PluginConfig pConfig = new TestPluginConfig();
		PluginConfig pConfig2 = new Test2PluginConfig();
		
		pMap.put("Test", pConfig);
		pMap.put("Test2", pConfig2);
		
		Configuration c = new Configuration("", "", pMap);
		Analyzer a = new Analyzer(c);
		
		a.computeResults();

	}
	
	//Arbitrary AnalyzerPlugin class that is used to easily test whether TestPlugin instances will be run in parallel as expected
	/*
	public static class TestPlugin implements AnalyzerPlugin{
		private final Configuration configuration;
	    private Result result;
		
	    public TestPlugin (Configuration generalConfiguration) {
	    	this.configuration = generalConfiguration;
	    }
	    
		@Override
		public void run() {
			for(int i=1; i<6; i++) {
				System.out.print(i);
			}
		}
		
		@Override
		public Result getResult() {
			return null;
		}
	}
	
	public static class TestPluginConfig extends PluginConfig{
		public TestPluginConfig() {
			super();
		}
		
		public void configure() {
			
		}
		
		public TestPluginConfig clone() {
			TestPluginConfig copie = new TestPluginConfig();
			copie.numSettings = this.getNumSettings();
			copie.stringSettings = this.getStringSettings();
			return copie;
		}
		
	}
	*/

}
