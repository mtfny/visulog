package up.visulog.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.util.HashMap;
import java.util.Map;

public class Configuration implements java.io.Serializable {

	//Paths are stored as a String so they can be serializable
    private final String gitPath;
    private final String configFilePath; //Directory where the configuration file will be stored
    private final Map<String, PluginConfig> plugins;
    private static final String configFileName = "config.txt";
	private boolean openHtml;

    public Configuration(String gitPath, String cfPath, Map<String, PluginConfig> plugins, boolean openHtml) {
        this.gitPath = gitPath;
        this.configFilePath = cfPath;
        this.plugins = deepCopy(plugins);
		this.openHtml = openHtml;
    }

    
    public Path getGitPath() {
        return Paths.get(gitPath);
    }

    public Map<String, PluginConfig> getPluginConfigs() {
        return deepCopy(plugins);
    }

	public boolean getOpenHtml() {
		return this.openHtml;
	}
    
    public void setPluginConfig(PluginConfig config) {
    	for(Map.Entry<String, PluginConfig> entry : plugins.entrySet()) {
    		plugins.put(entry.getKey(), config);
    	}
    }
    
    //Saves a configuration file in the folder in configFilePath
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
    
    //Loads a configuration file at the at the location passed as a parameter
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
    		//Doesn't do anything if file already exists
    		configFile.createNewFile();
    	}
    	
    	catch(IOException e){
    		System.out.println("An error has occured when creating config file");
    	}
    	
		return configFile;
    }
    
    //Returns a deep copy of the plugins map
    private Map<String, PluginConfig> deepCopy(Map<String, PluginConfig> m) {
    	Map<String, PluginConfig> res = new HashMap<String, PluginConfig>();
    	
    	for(Map.Entry<String, PluginConfig> entry : m.entrySet()) {
    		String s = new String(entry.getKey());
    		PluginConfig p = entry.getValue().clone();
    		res.put(s, p);
    	}
    	
    	return res;
    }
}
