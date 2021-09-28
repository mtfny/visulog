package up.visulog.config;
import java.util.HashMap;

// TODO: define what this type should be (probably a Map: settingKey -> settingValue)
//Each class that defines the configuration of a plugin extends this class
//This makes it possible for the user to customize a lot of variables used by the plugin
public class PluginConfig {
	private final HashMap<String, PluginSetting> settingMap = new HashMap<String, PluginSetting>();
}
