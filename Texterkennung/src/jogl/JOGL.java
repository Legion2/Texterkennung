package jogl;

import javax.swing.JFrame;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import debug.Debugger;
import debug.IInfo;

public class JOGL extends JFrame implements GLEventListener, IInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GL4 gl;
	public GLAutoDrawable drawable;

	public JOGL()
	{
		super("Parallisierung");
		this.setName("Parallisierung");
		//Debugger.info(this, "new JOGL");
		GLProfile profile = GLProfile.get(GLProfile.GL4);
		GLCapabilities capabilities = new GLCapabilities(profile);
		
		GLCanvas canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		
		this.setName("Parallisierung");
		this.getContentPane().add(canvas);
		
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	@Override
	public void display(GLAutoDrawable drawable)
	{
		Debugger.info(this, "display JOGL");
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void init(GLAutoDrawable drawable)
	{
		Debugger.info(this, "init JOGL");
		this.gl = drawable.getGL().getGL4();
		this.drawable = drawable;
		
		gl.glClearColor(0.392f, 0.584f, 0.929f, 1.0f);
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}
	
	public GL4 getGL4()
	{
		return this.gl;
	}
}
