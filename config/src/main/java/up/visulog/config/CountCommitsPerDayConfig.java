package up.visulog.config;

public class CountCommitsPerDayConfig extends PluginConfig {

	public CountCommitsPerDayConfig() {
		
	}
	
	public void configure() {
		
	}
	
	public CountCommitsPerDayConfig clone() {
		CountCommitsPerDayConfig copie = new CountCommitsPerDayConfig();
		copie.numSettings = this.getNumSettings();
		copie.stringSettings = this.getStringSettings();
		return copie;
	}
}
