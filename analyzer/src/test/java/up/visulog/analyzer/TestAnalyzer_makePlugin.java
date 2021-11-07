package up.visulog.analyzer;

import java.util.HashMap;
import up.visulog.config.*;

public class TestAnalyzer_makePlugin {

	//This class aims to test the creation of plugins via the makePlugin method of the Analyzer class.
	public static void main(String[] args) {
		
		HashMap<String, PluginConfig> m = new HashMap<>();
		
		PluginConfig countAuthorConfig = new CountCommitsPerAuthorConfig();
		PluginConfig countDayConfig = new CountCommitsPerDayConfig();
		
		//Test with Strings that indicate the names of existing AnalyzerPlugin classes
		m.put("CountCommitsPerAuthor", countAuthorConfig);
		m.put("CountCommitsPerDay", countDayConfig);
		
		//Test with a String that doesn't indicate the name of an existing AnalyzerPlugin class
		m.put("CountCommits", countAuthorConfig);
		
		Configuration c = new Configuration("", "", m);
		Analyzer a = new Analyzer(c);
		
		a.computeResults();
		//The CountCommitsPerAuthorPlugin and CountCommitsPerDay AnalyzerPlugin instances were successfully returned by the makePlugin method when the appropriate Strings were passed as parameter, while an attempt to create an instance of a nonexistent AnalyzerPlugin class (as shown in line 21) will make the makePlugin method return an empty Optional instance instead.
	}

}
