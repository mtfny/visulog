package up.visulog.config;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//Each class that defines the configuration of a plugin extends this class
//This makes it possible for the user to customize a lot of variables used by the plugin
//The name of the variables and their values are stored in numSettings and stringSettings
public class PluginConfig implements java.io.Serializable, Cloneable {
	protected Map<String, String> settings = new HashMap<String, String>();
	protected List<String> options = new LinkedList<String>();
	
	public Map<String, String> getSettings() { return new HashMap<String, String>(settings); }
	public List<String> getOptions() { return new LinkedList<String>(options); }	
	
	@Override
	public PluginConfig clone() { 
		PluginConfig clone = new PluginConfig();
		clone.settings = new HashMap<String, String>(this.settings);
		clone.options = new LinkedList<String>(this.options);
		return clone;
	}
	
	public void addSetting(String s1, String s2) {
		settings.put(s1, s2);
	}
	
	public void addOption(String s) {
		options.add(s);
	}
}
