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
    private final Path configFilePath;
    private final Map<String, PluginConfig> plugins;
    private final String configFileName = "config.txt";

    public Configuration(Path gitPath, Path cfPath, Map<String, PluginConfig> plugins) {
        this.gitPath = gitPath;
        this.configFilePath = cfPath;
        this.plugins = Map.copyOf(plugins);
    }

    public Path getGitPath() {
        return gitPath;
    }

    public Map<String, PluginConfig> getPluginConfigs() {
        return plugins;
    }
    
    public void saveConfigFile() {
    	File configFile = getConfigFile();
    	
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
			String toWrite ="";
			
			for(Map.Entry<String, PluginConfig> entry : plugins.entrySet()) {
				PluginConfig plugin = entry.getValue();
				toWrite = plugin.getName() + ": ";
				
				HashMap<String, PluginSetting> settingMap = plugin.getSettings();
				for(Map.Entry<String, PluginSetting> settingEntry : settingMap.entrySet()) {
					PluginSetting setting = settingEntry.getValue();
					String settingName = settingEntry.getKey();
					toWrite = toWrite + settingName + " -" + setting.getValue().toString();
				}
			}
			
			writer.write(toWrite);
			writer.close();
		}
    	
    	catch (IOException e) {
			System.out.println("Configuration file couldn't be saved");
			e.printStackTrace();
		}
    	
    }
    
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
}
