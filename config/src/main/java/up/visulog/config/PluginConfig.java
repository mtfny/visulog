package up.visulog.config;
import java.util.HashMap;
import java.util.Map;

//Each class that defines the configuration of a plugin extends this class
//This makes it possible for the user to customize a lot of variables used by the plugin
//The name of the variables and their values are stored in numSettings and stringSettings
public class PluginConfig implements java.io.Serializable, Cloneable {
	protected Map<String, String> settings = new HashMap<String, String>();
	
	public Map<String, String> getSettings() { return new HashMap<String, String>(settings); }
	protected void setSettings(Map<String, String> s) { this.settings = new HashMap<String, String>(s); }
	
	@Override
	public PluginConfig clone() { 
		PluginConfig clone = new PluginConfig();
		clone.settings = new HashMap<String, String>(this.settings);
		return clone;
	}
	
	public void addSetting(String s1, String s2) {
		settings.put(s1, s2);
	}
}
