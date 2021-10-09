package up.visulog.config;

import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

public class TestConfiguration {
    
	public static void main(String[] args) {
		String gitPath = FileSystems.getDefault().getPath(".").toString();
        String configFilePath = FileSystems.getDefault().getPath(".").toString();
        HashMap<String, PluginConfig> plugins = new HashMap<String, PluginConfig>();
        
        PluginConfig p1 = new PluginConfig();
        PluginConfig p2 = new PluginConfig();
        p1.addSetting("Variable 1" , 10);
        p1.addSetting("Variable 2 t" , "test");
        p2.addSetting("Variable 3 test" , 5);
        plugins.put("Plugin1", p1);
        plugins.put("Plugin2", p2);
        for(Map.Entry<String, PluginConfig> entry : plugins.entrySet()) {
        	System.out.println(entry.getKey());
        	System.out.println(entry.getValue() == null);
        }
        
		Configuration c = new Configuration(gitPath, configFilePath, plugins);
		c.saveConfigFile();
		Configuration test = Configuration.loadConfigFile(configFilePath);
		Map<String, PluginConfig> plugins2 = test.getPluginConfigs();
		for(Map.Entry<String,PluginConfig> entry : plugins2.entrySet()) {
			System.out.println(entry.getKey());
			PluginConfig bc = entry.getValue();
			
			for(HashMap.Entry<String, String> stringP : bc.getStringSettings().entrySet()) {
				System.out.println(stringP.getKey() + " " + stringP.getValue());
			}
			
			for(HashMap.Entry<String, Integer> stringP : bc.getNumSettings().entrySet()) {
				System.out.println(stringP.getKey() + " " + stringP.getValue());
			}
		}
	}
}
