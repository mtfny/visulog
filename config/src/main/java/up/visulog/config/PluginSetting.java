package up.visulog.config;

public class PluginSetting<T> {
	private final String description;
	private final T value;
	
	public PluginSetting(String d, T val){
		this.description = d;
		this.value = val;
	}
	
	public String getDescription() {return description;}
	public T getValue() {return value;}
}
