package up.visulog.config;

import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

public class TestConfiguration {
    
	public static void main(String[] args) {
		var gitPath = FileSystems.getDefault().getPath(".");
        var configFilePath = FileSystems.getDefault().getPath(".");
        HashMap<String, PluginConfig> plugins = new HashMap<String, PluginConfig>();
        
        PluginConfig p1 = new PluginConfig("rez");
        PluginConfig p2 = new PluginConfig("rez");
        p1.name = "Test plugin 1";
        p2.name = "Test plugin 2";
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
	}
}
