package jogl;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLProfile;

import debug.Debugger;
import debug.IInfo;

public class OpenGLHandler implements IInfo
{
	/**
	 * true if OpenGL 4.0 is supported
	 */
	private final boolean openGLsupport;

	private JOGL jogl;
	
	public OpenGLHandler()
	{
		this.openGLsupport = this.openGLVersioncheck();
	}

	private boolean openGLVersioncheck()
	{
		Debugger.info(this, GLProfile.getDefault().toString());
		
		return GLProfile.getDefault().isGL4();
	}
	
	public boolean supportOpenGL()
	{
		return this.openGLsupport;
	}
	
	public GL4 getGL4()
	{
		if (this.openGLsupport)
		{
			if (this.jogl == null)
			{
				this.jogl = new JOGL();
				synchronized (this.jogl)
				{
					try
					{
						this.jogl.wait();
					} catch (InterruptedException e) {
						Debugger.error(this, "Kann nicht auf JOGL Thread Warten!");
						e.printStackTrace();
					}
				}
			}
			return this.jogl.getGL4();
		}
		else
		{
			Debugger.error(this, "OpenGL 4.0 not supported!");
			return null;
		}
	}
	
	public void stop()
	{
		if (this.jogl != null)
		{
			jogl.dispose();
			jogl.drawable.destroy();
		}
	}

	@Override
	public String getName()
	{
		return "OpenGLHandler";
	}
}
