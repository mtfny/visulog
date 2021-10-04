package up.visulog.config;
import java.util.HashMap;
import java.util.Map;

// TODO: define what this type should be (probably a Map: settingKey -> settingValue)
//Each class that defines the configuration of a plugin extends this class
//This makes it possible for the user to customize a lot of variables used by the plugin
public class PluginConfig {
	private final HashMap<String, Integer> numSettings = new HashMap<String, Integer>();
	private final HashMap<String, String> stringSettings = new HashMap<String, String>();
	public String name;
	
	public HashMap<String, Integer> getNumSettings() { return new HashMap<String, Integer>(numSettings); }
	public HashMap<String, String> getStringSettings() { return new HashMap<String, String>(stringSettings); }
	public String getName() { return new String(name); }
	
	public PluginConfig(String name) {
		this.name = name;
	}
	
	//Used to create a deep copy of this class
	public PluginConfig(PluginConfig p) {
		this.name = new String(p.name);
		
		for(Map.Entry<String, Integer> entry : p.numSettings.entrySet()) {
			String s = new String(entry.getKey());
			Integer i = new Integer(entry.getValue());
			this.numSettings.put(s, i);
		}
		
		for(Map.Entry<String, String> entry : p.stringSettings.entrySet()) {
			String s1 = new String(entry.getKey());
			String s2 = new String(entry.getValue());
			this.stringSettings.put(s1, s2);
		}
	}
	
	public String numSettingsToString() {
		String toWrite = "";
		for(Map.Entry<String, Integer> settingEntry : numSettings.entrySet()) {
			Integer setting = settingEntry.getValue();
			String settingName = settingEntry.getKey();
			toWrite = toWrite + settingName + " -" + setting.toString() + "; ";
		}
		
		return toWrite;
	}
	
	public String stringSettingsToString() {
		String toWrite = "";
		for(Map.Entry<String, String> settingEntry : stringSettings.entrySet()) {
			String setting = settingEntry.getValue();
			String settingName = settingEntry.getKey();
			toWrite = toWrite + settingName + " -" + setting.toString() + "; ";
		}
		
		return toWrite;
	}
	
	//Both of these functions are only for testing
	public void addSetting(String s, Integer i) {
		numSettings.put(s, i);
		
	}
	
	public void addSetting(String s1, String s2) {
		stringSettings.put(s1, s2);
	}
}
