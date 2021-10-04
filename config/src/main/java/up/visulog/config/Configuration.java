package up.visulog.config;

import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

    private final Path gitPath;
    private final Path configFilePath; //Directory where the configuration file will be stored
    private final HashMap<String, PluginConfig> plugins;
    private final String configFileName = "config.txt";

    public Configuration(Path gitPath, Path cfPath, HashMap<String, PluginConfig> plugins) {
        this.gitPath = gitPath;
        this.configFilePath = cfPath;
        this.plugins = deepCopy(plugins);
    }

    public Path getGitPath() {
        return gitPath;
    }

    public Map<String, PluginConfig> getPluginConfigs() {
        return plugins;
    }
    
    //Will save a file that contains all plugins variables that can be customized by the user
    public void saveConfigFile() {
    	File configFile = getConfigFile();
    	
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
			String toWrite ="";
			
			for(Map.Entry<String, PluginConfig> entry : plugins.entrySet()) {
				PluginConfig plugin = entry.getValue();
				toWrite = toWrite + plugin.getName() + ": " + plugin.numSettingsToString() + plugin.stringSettingsToString() + '\n';
			}
			
			writer.write(toWrite);
			writer.close();
			System.out.println("Config file saved at : " + configFile.getAbsolutePath());
		}
    	
    	catch (IOException e) {
			System.out.println("Configuration file couldn't be saved");
			e.printStackTrace();
		}
    	
    }
    
    //Returns configuration file if it exists, else it will create one
    public File getConfigFile() {
		File configFile = new File(configFilePath + configFileName);
    	try {
    		configFile.createNewFile();
    	}
    	
    	catch(IOException e){
    		System.out.println("An error has occured when creating config file");
    	}
    	
		return configFile;
    }
    
    //Returns a deep copy of the plugins map
    public HashMap<String, PluginConfig> deepCopy(HashMap<String, PluginConfig> m){
    	HashMap<String, PluginConfig> res = new HashMap<String, PluginConfig>();
    	
    	for(Map.Entry<String, PluginConfig> entry : m.entrySet()) {
    		String s = new String(entry.getKey());
    		PluginConfig p = new PluginConfig(entry.getValue());
    		res.put(s, p);
    	}
    	
    	return res;
    }
}
