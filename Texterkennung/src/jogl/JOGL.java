package jogl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.swing.JFrame;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

public class JOGL extends JFrame implements GLEventListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GL4 gl;
	
	private int program;

	public JOGL()
	{
		super("Parallisierung");
		System.out.println("new JOGL");
		
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
		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("resource")
	@Override
	public void init(GLAutoDrawable drawable)
	{
		System.out.println("init JOGL");
		this.gl = drawable.getGL().getGL4();
		
		
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
