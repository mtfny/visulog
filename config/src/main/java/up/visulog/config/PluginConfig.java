package up.visulog.config;
import java.util.HashMap;
import java.util.Map;

//Each class that defines the configuration of a plugin extends this class
//This makes it possible for the user to customize a lot of variables used by the plugin
public abstract class PluginConfig implements java.io.Serializable {
	protected Map<String, Integer> numSettings = new HashMap<String, Integer>();
	protected Map<String, String> stringSettings = new HashMap<String, String>();
	
	public Map<String, Integer> getNumSettings() { return new HashMap<String, Integer>(numSettings); }
	public Map<String, String> getStringSettings() { return new HashMap<String, String>(stringSettings); }
	
	protected abstract PluginConfig clone();
	protected abstract void configure(); //Fonction dans laquelle le pluginConfig peut demander les valeurs dont il a besoin via ligne de commande
	
	//Both of these functions are only for testing
	public void addSetting(String s, Integer i) {
		numSettings.put(s, i);
		
	}
	
	public void addSetting(String s1, String s2) {
		stringSettings.put(s1, s2);
	}
}
