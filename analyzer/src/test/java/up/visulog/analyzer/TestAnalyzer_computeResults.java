package up.visulog.analyzer;
import java.util.HashMap;
import up.visulog.config.*;

public class TestAnalyzer_computeResults {

	//This class aims to test the computeResults method of the Analyzer class, mainly to make sure that the plugins we want to run are being run in parallel.
	public static void main(String[] args) {
		
		HashMap<String, PluginConfig> pMap = new HashMap<>();
		
		//TestPluginConfig and Test2PluginConfig classes are instantiated
		PluginConfig pConfig = new PluginConfig();
		PluginConfig pConfig2 = new PluginConfig();
		
		//The instances of TestPluginConfig and Test2PluginConfig are put in the pMap HashMap with the String that corresponds to their respective AnalyzerPlugin counterpart as the key
		pMap.put("Test", pConfig);
		pMap.put("Test2", pConfig2);
		
		Configuration c = new Configuration("", "", pMap, false);
		Analyzer a = new Analyzer(c);
		
		a.computeResults();
		//The TestPlugin and Test2Plugin AnalyzerPlugins are being run in parallel, as evidenced by the overlap that may happen when the integers 1 through 10 are printed as a result of the plugins' respective run methods.
	}
}
