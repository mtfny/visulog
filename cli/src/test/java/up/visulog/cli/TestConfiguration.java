package up.visulog.cli;

import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;
import up.visulog.analyzer.Analyzer;
import up.visulog.config.*;

public class TestConfiguration {
    
	public static void main(String[] args) {
		String gitPath = FileSystems.getDefault().getPath(".").toString();
        String configFilePath = FileSystems.getDefault().getPath(".").toString();
        HashMap<String, PluginConfig> plugins = new HashMap<String, PluginConfig>();
        
        PluginConfig p1 = new PluginConfig();
        p1.addSetting("Variable 2 t" , "test");
        plugins.put("Plugin1", p1);
        plugins.put("Plugin2", p1);
        
        for(Map.Entry<String, PluginConfig> entry : plugins.entrySet()) {
        	System.out.println(entry.getKey());
        	System.out.println(entry.getValue() == null);
        }
        
		Configuration c = new Configuration(gitPath, configFilePath, plugins, false);
 		c.setPluginConfig(p1);
		c.saveConfigFile();
		Configuration test = Configuration.loadConfigFile(configFilePath);
		Map<String, PluginConfig> plugins2 = test.getPluginConfigs();
		for(Map.Entry<String,PluginConfig> entry : plugins2.entrySet()) {
			System.out.println(entry.getKey());
			PluginConfig bc = entry.getValue();
			
			for(HashMap.Entry<String, String> stringP : bc.getSettings().entrySet()) {
				System.out.println(stringP.getKey() + " " + stringP.getValue());
			}
		}
		
        HashMap<String, PluginConfig> pluginsTest = new HashMap<String, PluginConfig>();
        pluginsTest.put("CountCommitsPerDay", new PluginConfig());
		Analyzer ana = new Analyzer(new Configuration(gitPath, configFilePath, pluginsTest, false));
		var results = ana.computeResults();
		System.out.println(results.toHTML());
	}
}
