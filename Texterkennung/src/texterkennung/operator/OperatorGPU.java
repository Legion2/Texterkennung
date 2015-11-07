package texterkennung.operator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL4;

public abstract class OperatorGPU extends Operator
{
	protected GL4 gl;
	
	protected int program;
	private int computeShader;
	
	private Map<String, Integer>  uniformLocations = new HashMap<>();
    private Map<String, Integer>  attribLocations = new HashMap<>();

	
	public OperatorGPU(GL4 gl, String computePath)
	{
		this.gl = gl;
		
		String vsrc = "";
		try
		{
			BufferedReader brv;
			brv = new BufferedReader(new FileReader(computePath));
			
			String line;
			while ((line=brv.readLine()) != null) {
			  vsrc += line + "\n";
			}
			brv.close();
		} catch (FileNotFoundException e) {
			System.out.println("Shaderdatei konnte nicht gelesen werden.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Fehler beim auslesen der Shaderdatei.");
			e.printStackTrace();
		}
		
		this.computeShader = createAndCompileShader(GL4.GL_COMPUTE_SHADER, vsrc);
		
		this.program = gl.glCreateProgram();
		
		gl.glAttachShader(this.program, this.computeShader);
		
		gl.glLinkProgram(this.program);
	}
	
	public int getUniformLocation(String uniform)
	{
        Integer result = uniformLocations.get(uniform);

        if (result == null) {
            result = gl.glGetUniformLocation(program, uniform);

            uniformLocations.put(uniform, result);
        }

        return result;
    }
	
    public int getAttribLocation(String attrib)
	{
        Integer result = attribLocations.get(attrib);

        if (result == null) {
            result = gl.glGetAttribLocation(program, attrib);

            attribLocations.put(attrib, result);
        }

        return result;
    }
	
    public void bindAttributeLocation(int location, String name)
	{
        gl.glBindAttribLocation(this.program, location, name);
    }
	
	/**
	 * @author App Software
	 * 
	 * @param type
	 * @param shaderString
	 * @return
	 */
	private int createAndCompileShader(int type, String shaderString)
	{
        int shader = gl.glCreateShader(type);

        String[] vlines = new String[]{shaderString};
        int[] vlengths = new int[]{vlines[0].length()};

        gl.glShaderSource(shader, vlines.length, vlines, vlengths, 0);
        gl.glCompileShader(shader);

        int[] compiled = new int[1];
        gl.glGetShaderiv(shader, GL2ES2.GL_COMPILE_STATUS, compiled, 0);

        /*if (compiled[0] == 0) {
            int[] logLength = new int[1];
            gl.glGetShaderiv(shader, GL2ES2.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]+1];
            gl.glGetShaderInfoLog(shader, logLength[0]+1, null, 0, log, 0);

            throw new IllegalStateException("Error compiling the shader: " + new String(log) + " ; " + shaderString);
        }*/

        return shader;
    }
	
	protected void compute(int x, int y, int z)
	{
		gl.glDispatchCompute(x, y, z);
	}
	
	protected void begin()
	{
		gl.glUseProgram(this.program);
	}
	
	protected void end()
	{
		gl.glUseProgram(0);
	}
	
	public void dispose()
	{
        gl.glDetachShader(this.program, this.computeShader);
        gl.glDeleteShader(this.computeShader);
        gl.glDeleteProgram(this.program);
    }
}
