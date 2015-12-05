package GUI;

public interface IConfigurable
{
	/**
	 * Get the config string from Erkennung classes
	 * @return configstring
	 */
	public String getConfig();
	
	
	/**
	 * 
	 * @param parameter configstring
	 */
	public void setConfig(String parameter);
}
