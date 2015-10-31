package texterkennung.data;

public abstract class Data2D extends Data
{
	protected final int xlenght;
	protected final int ylenght;
	
	public Data2D(int x, int y)
	{
		super();
		this.xlenght = x;
		this.ylenght = y;
	}
	
	public int getXlenght()
	{
		return xlenght;
	}
	
	public int getYlenght()
	{
		return ylenght;
	}
}
