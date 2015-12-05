package debug;

public class Debugger
{
	/**
	 * Print a message in the console
	 * 
	 * @param iInfo Object from where the message come
	 * @param message string with the message
	 */
	public static void info(IInfo iInfo, String message)
	{
		System.out.println("[" + ((iInfo != null) ? iInfo.getName() : "null") + "] " + message);
	}
	
	public static void error(IInfo iInfo, String message)
	{
		System.err.println(" >> ERROR << [" + iInfo.getName() + "] " + message);
	}
}
