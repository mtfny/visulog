package up.visulog.config;

public class CountCommitsPerAuthorConfig extends PluginConfig {
	public CountCommitsPerAuthorConfig() {
		super();
	}
	
	public void configure() {
		
	}
	
	public CountCommitsPerAuthorConfig clone() {
		CountCommitsPerAuthorConfig copie = new CountCommitsPerAuthorConfig();
		copie.numSettings = this.getNumSettings();
		copie.stringSettings = this.getStringSettings();
		return copie;
	}
}
