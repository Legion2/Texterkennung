package texterkennung.operator;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

import GUI.GuiElements;
import advanced.AColor;
import texterkennung.data.Data;
import texterkennung.data.DataList;
import texterkennung.data.Data_F;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_Image;

/**
 * Sortiert Hintergrundfarben aus und speichert den Grad der übereinstimmung als float (0.0 - 1.0f)
 * 
 * Wird auf der GPU berechnet.
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
	private FloatBuffer outputBufferf;
	
	private final Data_ID data_ID;
	private final Data_F data_F;
	
	private final Data_ID data_Image;
	private final int schwellwert;
	
	public OperatorGPU_Farbzuordnung(Data_ID data_Image, ArrayList<AColor> farbListe, int schwellwert, GL4 gl4)
	{
		super(gl4, computeShaderPath);
		this.data_Image = data_Image;
		this.schwellwert = schwellwert;
		this.data_ID = new Data_ID(this.data_Image, "Data-Farbzuordnung", true);
		this.data_F = new Data_F(data_Image, "Data-Farbübereinstimmung", true);
		this.farbenBuffer = Buffers.newDirectIntBuffer(farbListe.size());
		this.inputBuffer = Buffers.newDirectIntBuffer(this.data_ID.getXlenght() * this.data_ID.getYlenght());
		this.outputBuffer = Buffers.newDirectIntBuffer(this.data_ID.getXlenght() * this.data_ID.getYlenght());
		this.outputBufferf = Buffers.newDirectFloatBuffer(this.data_ID.getXlenght() * this.data_ID.getYlenght());
		
		for (int i = 0; i < farbListe.size(); i++)
		{
			this.farbenBuffer.put(0, farbListe.get(i).getRGB());
		}
		
		this.setBufferfromData(this.inputBuffer, this.data_Image);
	}
	
	@Override
	public String getName()
	{
		return "Operator_Farbzuordnung";
	}

	@Override
	public void run()
	{
		this.begin();
		
		IntBuffer buffers = Buffers.newDirectIntBuffer(4);
		
		gl.glGenBuffers(4, buffers);//Generiert drei neue Buffernamen(int)
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(0));//Farben
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, this.farbenBuffer.limit() * 4, this.farbenBuffer, GL4.GL_STATIC_READ);
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(1));//Originalbild
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, this.inputBuffer.limit() * 4, this.inputBuffer, GL4.GL_STATIC_READ);
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(2));//Data_ID
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, this.outputBuffer.limit() * 4, this.outputBuffer, GL4.GL_STREAM_READ);
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(3));//Data_ID
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, this.outputBufferf.limit() * 4, this.outputBufferf, GL4.GL_STREAM_READ);
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		
		gl.glUniform1i(this.getUniformLocation("schwellwert"), this.schwellwert);
		
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 0, buffers.get(0));
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 1, buffers.get(1));
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 2, buffers.get(2));
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 3, buffers.get(3));

        this.compute(((data_ID.getXlenght() * data_ID.getYlenght()) / 1024) + 1, 1, 1);

        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 0, 0);
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 1, 0);
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 2, 0);
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 3, 0);
        
        //Daten von der gpu zur cpu copieren
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(2));
        gl.glGetBufferSubData(GL4.GL_ARRAY_BUFFER, 0, this.outputBuffer.limit() * 4, this.outputBuffer);
        
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(3));
        gl.glGetBufferSubData(GL4.GL_ARRAY_BUFFER, 0, this.outputBufferf.limit() * 4, this.outputBufferf);
        
        this.setDatafromBuffer(this.data_ID, this.outputBuffer);
        this.setDatafromBuffer(this.data_F, this.outputBufferf);

		gl.glDeleteBuffers(4, buffers);
        
		this.end();
		
		GuiElements.MainGUI.setTab(this.data_ID);
		GuiElements.MainGUI.setTab(this.data_F);
	}

	

	@Override
	public Data getData()
	{
		DataList list = new DataList("return list", false);
		list.add(this.data_ID);
		list.add(this.data_F);
		return list;
	}
}
