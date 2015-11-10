package texterkennung.operator;

import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

import GUI.GuiElements;
import advanced.ABufferedImage;
import advanced.AColor;
import texterkennung.data.Data;
import texterkennung.data.Data_ID;

/**
 * Sortiert Hintergrundfarben aus
 * 
 * Kann Parallelisiert werden
 * 
 * @author Leon
 *
 */
public class OperatorGPU_Farbzuordnung extends OperatorGPU
{
	private final static String computeShaderPath = "./src/jogl/shader/Farbzuordnung.glsl";
	
	private IntBuffer farbenBuffer;
	private IntBuffer inputBuffer;
	private IntBuffer outputBuffer;
	
	private Data_ID data_ID;
	
	private final ABufferedImage originalBild;
	private final int schwellwert;
	
	public OperatorGPU_Farbzuordnung(ABufferedImage originalBild, ArrayList<AColor> farbListe, int schwellwert, GL4 gl4)
	{
		super(gl4, computeShaderPath);
		this.originalBild = originalBild;
		this.schwellwert = schwellwert;
		this.data_ID = new Data_ID(this.originalBild.getWidth(), this.originalBild.getHeight(), "Data-Farbzuordnung");
		this.farbenBuffer = Buffers.newDirectIntBuffer(farbListe.size());
		this.inputBuffer = Buffers.newDirectIntBuffer(this.data_ID.getXlenght() * this.data_ID.getYlenght());
		this.outputBuffer = Buffers.newDirectIntBuffer(this.data_ID.getXlenght() * this.data_ID.getYlenght());
		
		for (int i = 0; i < farbListe.size(); i++)
		{
			this.farbenBuffer.put(0, farbListe.get(i).getRGB());
		}
		
		this.setBufferfromImage(this.inputBuffer, this.originalBild);
	}
	
	public OperatorGPU_Farbzuordnung(ABufferedImage originalBild, ArrayList<AColor> farbListe, GL4 gl4)
	{
		super(gl4, computeShaderPath);
		this.originalBild = originalBild;
		this.schwellwert = -1;
		this.data_ID = new Data_ID(this.originalBild.getWidth(), this.originalBild.getHeight(), "Data-Farbzuordnung");
		this.farbenBuffer = Buffers.newDirectIntBuffer(farbListe.size());
		this.inputBuffer = Buffers.newDirectIntBuffer(this.data_ID.getXlenght() * this.data_ID.getYlenght());
		this.outputBuffer = Buffers.newDirectIntBuffer(this.data_ID.getXlenght() * this.data_ID.getYlenght());
		
		for (int i = 0; i < farbListe.size(); i++)
		{
			this.farbenBuffer.put(0, farbListe.get(i).getRGB());
		}
		
		this.setBufferfromImage(this.inputBuffer, this.originalBild);
	}
	
	@Override
	public String getName()
	{
		return "Operator_Farbzuordnung";
	}

	@Override
	public void run()
	{
		gl.getContext().makeCurrent();//TODO wichtig
		
		this.begin();
		
		IntBuffer buffers = Buffers.newDirectIntBuffer(3);
		
		gl.glGenBuffers(3, buffers);//Generiert drei neue Buffernamen(int)
		System.out.println(buffers.get(2));
		//System.out.println(gl.getContext().toString());
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(0));//Farben
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, this.farbenBuffer.limit() * 4, this.farbenBuffer, GL4.GL_STATIC_READ);
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(1));//Originalbild
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, this.inputBuffer.limit() * 4, this.inputBuffer, GL4.GL_STATIC_READ);
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(2));//Data_ID
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, this.outputBuffer.limit() * 4, this.outputBuffer, GL4.GL_STREAM_READ);
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		
		//TODO SSBO alles
		
		
		
		gl.glUniform1i(this.getUniformLocation("schwellwert"), this.schwellwert);
		
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 0, buffers.get(0));
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 1, buffers.get(1));
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 2, buffers.get(2));

        this.compute(((data_ID.getXlenght() * data_ID.getYlenght()) / 1024) + 1, 1, 1);

        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 0, 0);
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 1, 0);
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 2, 0);
        
        //Daten von der gpu zur cpu copieren
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(2));
        gl.glGetBufferSubData(GL4.GL_ARRAY_BUFFER, 0, this.outputBuffer.limit() * 4, this.outputBuffer);
        
        setDatafromBuffer(this.data_ID, this.outputBuffer);

		gl.glDeleteBuffers(3, buffers);
        
		this.end();
		
		this.dispose();
		
		GuiElements.MainGUI.setTab(this.data_ID);
	}

	@Override
	public Data getData()
	{
		return this.data_ID;
	}
}
