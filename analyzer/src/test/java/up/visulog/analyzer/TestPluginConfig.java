package up.visulog.analyzer;

import up.visulog.config.PluginConfig;

//PluginConfig counterpart to the testing class TestPlugin
public class TestPluginConfig extends PluginConfig{
	public TestPluginConfig() {
		super();
	}
	
	public void configure() {
		
	}
	
	public TestPluginConfig clone() {
		TestPluginConfig copie = new TestPluginConfig();
		copie.numSettings = this.getNumSettings();
		copie.stringSettings = this.getStringSettings();
		return copie;
	}
	
}
