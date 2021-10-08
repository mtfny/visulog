package up.visulog.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration implements java.io.Serializable {

	//Paths are stored as a String so they can be serializable
    private final String gitPath;
    private final String configFilePath; //Directory where the configuration file will be stored
    private final HashMap<String, PluginConfig> plugins;
    private static final String configFileName = "config.txt";

    public Configuration(String gitPath, String cfPath, HashMap<String, PluginConfig> plugins) {
        this.gitPath = gitPath;
        this.configFilePath = cfPath;
        this.plugins = deepCopy(plugins);
    }

    
    public Path getGitPath() {
        return Paths.get(gitPath);
    }

    public Map<String, PluginConfig> getPluginConfigs() {
        return deepCopy(plugins);
    }
    
    //Will save a file that contains all plugins variables that can be customized by the user
    /*public void saveConfigFile() {
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
    	
    }*/
    
    public void saveConfigFile() {
    	try {
    		File configFile = getConfigFile();
    		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configFile));
    		oos.writeObject(this);
    		oos.close();
    		System.out.println("Config file saved to " + configFilePath + configFileName);
    	}
    	
    	catch(IOException e) {
    		e.printStackTrace();
    		System.out.println("ERROR : Configuration file couldn't be saved");
    	}
    	
    }
    
    public static Configuration loadConfigFile(String configFilePath) {
    	try {
    		File configFile = new File(configFilePath + configFileName);
    		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile));
    		Configuration res = (Configuration)ois.readObject();
    		ois.close();
    		System.out.println("Config file Loaded");
    		return res;
    	}
    	
    	catch(IOException|ClassNotFoundException e) {
    		e.printStackTrace();
    		System.out.println("ERROR : Configuration file couldn't be loaded");
    		return null;
    	}
    }
    
    //Returns configuration file if it exists, else it will create one
    private File getConfigFile() {
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
    private HashMap<String, PluginConfig> deepCopy(HashMap<String, PluginConfig> m){
    	HashMap<String, PluginConfig> res = new HashMap<String, PluginConfig>();
    	
    	for(Map.Entry<String, PluginConfig> entry : m.entrySet()) {
    		String s = new String(entry.getKey());
    		PluginConfig p = new PluginConfig(entry.getValue());
    		res.put(s, p);
    	}
    	
    	return res;
    }
}
