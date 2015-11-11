package debug;

public class Debugger
{
	public static void info(IInfo iInfo, String message)
	{
		System.out.println("[" + iInfo.getName() + "] " + message);
	}
	
	public static void error(IInfo iInfo, String message)
	{
		System.err.println(" >> ERROR << [" + iInfo.getName() + "] " + message);
	}
}
