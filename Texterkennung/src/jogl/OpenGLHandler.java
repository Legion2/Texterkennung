package jogl;

import java.util.Optional;

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

	private Optional<JOGL> jogl = Optional.empty();
	
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
			return this.jogl.orElseGet(() ->
			{
				JOGL jogl = new JOGL();
				synchronized (jogl)
				{
					try
					{
						jogl.wait();
					} catch (InterruptedException e) {
						Debugger.error(this, "Kann nicht auf JOGL Thread Warten!");
						e.printStackTrace();
					}
				}
				this.jogl = Optional.of(jogl);
				return jogl;
			}).getGL4();
		}
		else
		{
			Debugger.error(this, "OpenGL 4.0 not supported!");
			return null;
		}
	}
	
	public void stop()
	{
		this.jogl.ifPresent(jogl ->
		{
			jogl.dispose();
			jogl.drawable.destroy();
		});
	}

	@Override
	public String getName()
	{
		return "OpenGLHandler";
	}
}
