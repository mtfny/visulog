package up.visulog.analyzer;
import java.util.HashMap;
import up.visulog.config.*;
import up.visulog.analyzer.*;

public class TestPluginCreation {

	//This class aims to test the creation of plugins via the makePlugin method of the Analyzer class.
	public static void main(String[] args) {
		
		HashMap<String, PluginConfig> m = new HashMap<>();
		
		PluginConfig d = new CountCommitsPerAuthorConfig();
		
		//Test with a String that indicates the name of an existing AnalyzerPlugin class
		m.put("CountCommitsPerAuthor", d);
		//Test with a String that doesn't indicate the name of an existing AnalyzerPlugin
		m.put("CountCommits", d);
		
		Configuration c = new Configuration("", "", m);
		Analyzer a = new Analyzer(c);
		
		a.computeResults();
		//The CountCommitsPerAuthorPlugin was successfully returned by the makePlugin method when the appropriate String "CountCommitsPerAuthor" was passed as parameter, while an attempt to create an instance of a nonexistent AnalyzerPlugin class (as shown in line 20) will make the makePlugin method return an empty Optional instance instead.
	}

}
