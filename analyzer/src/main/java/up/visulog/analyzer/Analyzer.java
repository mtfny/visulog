package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.lang.reflect.*;

public class Analyzer {
    private final Configuration config;

    private AnalyzerResult result;

    public Analyzer(Configuration config) {
        this.config = config;
    }

    public AnalyzerResult computeResults() {
        List<AnalyzerPlugin> plugins = new ArrayList<>();
        for (var pluginConfigEntry: config.getPluginConfigs().entrySet()) {
            var pluginName = pluginConfigEntry.getKey();
            var pluginConfig = pluginConfigEntry.getValue();
            var plugin = makePlugin(pluginName, pluginConfig);
            plugin.ifPresent(plugins::add);
        }
        // run all the plugins
        // TODO: try running them in parallel
        for (var plugin: plugins) plugin.run();

        // store the results together in an AnalyzerResult instance and return it
        return new AnalyzerResult(plugins.stream().map(AnalyzerPlugin::getResult).collect(Collectors.toList()));
    }

    //In order for the list of plugins not to be hardcored in this factory with a switch statement, this method will use the String "pluginName" (which is passed as a parameter) in order to find the class name of the object we want to instantiate.
    private Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) {
    	
    	if(this.config.getPluginConfigs().containsKey(pluginName)) {
    		// If the "plugins" Map attribute of the config attribute of the Analyzer class contains a key that is equivalent to the parameter pluginName, a new object of the Class.forName(pluginName) class with the object "this.config" passed as parameter to the class constructor is returned.
    		
    		try {
    			@SuppressWarnings("unchecked")
    			//The class object which corresponds to the class of the plugin we want to create is retrieved.
    			Class<?> c = (Class<AnalyzerPlugin>)Class.forName("up.visulog.analyzer." + pluginName + "Plugin");
    			//The constructor(s) for this class are retrieved in an array.
    			Constructor<?>[] pluginConstructor = c.getConstructors();
    			//A new instance of the type of plugin we want to create is returned.
    			return Optional.of((AnalyzerPlugin)pluginConstructor[0].newInstance(this.config));
    		}
    		catch(Exception e) {
    			//If the method runs into an exception at any point in the try statement, an empty Optional instance is returned instead.
    			return Optional.empty();
    		}
    	}
    	
    	//If the "plugins" Map attribute of the "config" Configuration attribute of the Analyzer class does not contain a key that is equivalent to the String parameter pluginName, an empty Optional instance is returned instead.
    	return Optional.empty();
    	
    }
    
}
