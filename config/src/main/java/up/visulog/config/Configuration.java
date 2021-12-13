package up.visulog.config;

import java.nio.file.FileSystems;
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
    private final Map<String, PluginConfig> plugins;
    private static final String configFolder = "../configFiles/"; //Directory where the configuration file will be stored
    private final String configFileName; //Name of the configuration file given by the user
	private boolean openHtml;

    public Configuration(String gitPath, String cfPath, Map<String, PluginConfig> plugins, boolean openHtml) {
        this.gitPath = gitPath;
        this.configFileName = cfPath;
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
    		File configFile = getConfigFile(true);
    		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configFile));
    		oos.writeObject(this);
    		oos.close();
    		System.out.println("Config file saved to " + configFile.getAbsolutePath());
    	}
    	
    	catch(IOException e) {
    		System.out.println("ERROR : Configuration file couldn't be saved");
    	}
    	
    }
    
    //Loads a configuration file at the at the location passed as a parameter
    public static Configuration loadConfigFile(String configFilePath) {
    	try {
    		configFilePath = FileSystems.getDefault().getPath(configFolder + configFilePath).toString();
    		File configFile = new File(configFilePath);
    		if(configFile.exists() && configFile.isFile()) {
    			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile));
    			Configuration res = (Configuration)ois.readObject();    		
    			ois.close();
    			System.out.println("Config file Loaded");
    			return res;	
    		}
    		
    		System.out.println("ERROR : Configuration file doesn't exist");
    		return null;
    	}
    	
    	catch(IOException|ClassNotFoundException e) {
    		e.printStackTrace();
    		System.out.println("ERROR : Configuration file couldn't be loaded");
    		return null;
    	}
    }
    
    //Returns configuration file if it exists, else it will create one
    private File getConfigFile(boolean createIfDoesNotExist) {
    	String path = FileSystems.getDefault().getPath(configFolder + configFileName).toString();
		File configFile = new File(path);
    	try {    		
    		//Doesn't do anything if file already exists
    		if(createIfDoesNotExist)
    			configFile.createNewFile();
    	}
    	
    	catch(IOException e){
    		System.out.println("An error has occured when creating config file");
    	}
    	
    	if(configFile.exists())
    		return configFile;
    	else
    		return null;
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
