package up.visulog.analyzer;

import up.visulog.config.PluginConfig;

//PluginConfig counterpart to the testing class Test2Plugin
public class Test2PluginConfig extends PluginConfig{
	public Test2PluginConfig() {
		super();
	}
	
	public void configure() {
		
	}
	
	public Test2PluginConfig clone() {
		Test2PluginConfig copie = new Test2PluginConfig();
		copie.numSettings = this.getNumSettings();
		copie.stringSettings = this.getStringSettings();
		return copie;
	}
	
}
