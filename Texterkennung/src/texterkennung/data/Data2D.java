package texterkennung.data;

public abstract class Data2D extends Data
{
	protected final int xlenght;
	protected final int ylenght;
	
	public Data2D(int x, int y, String name, boolean b)
	{
		super(name, b);
		this.xlenght = x;
		this.ylenght = y;
		this.init();
		this.setDefault();
	}
	
	public Data2D(Data2D data, String name, boolean b)
	{
		super(name, b);
		this.xlenght = data.xlenght;
		this.ylenght = data.ylenght;
		this.init();
		this.setDefault();
	}
	
	protected abstract void init();
	
	public abstract void setDefault();
	
	public int getXlenght()
	{
		return xlenght;
	}
	
	public int getYlenght()
	{
		return ylenght;
	}
}
