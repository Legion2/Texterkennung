package texterkennung.operator;

import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

import GUI.GuiElements;
import texterkennung.data.Data;
import texterkennung.data.Data_ID;
import texterkennung.data.Data_NPOS;

public class OperatorGPU_IDtoNPOS extends OperatorGPU
{
	private final static String computeShaderPath = "./src/jogl/shader/IDtoNPOS.glsl";
	
	private IntBuffer inputBuffer;
	private IntBuffer outputBuffer;
	
	private Data_ID data_ID_input;
	private Data_NPOS data_NPOS_output;
	
	public OperatorGPU_IDtoNPOS(Data_ID data_ID, GL4 gl4)
	{
		super(gl4, computeShaderPath);
		this.data_ID_input = data_ID;
		this.data_NPOS_output = new Data_NPOS(data_ID, "Data");
		
		this.inputBuffer = Buffers.newDirectIntBuffer(this.data_ID_input.getXlenght() * this.data_ID_input.getYlenght());
		this.outputBuffer = Buffers.newDirectIntBuffer(this.data_ID_input.getXlenght() * this.data_ID_input.getYlenght());
		
		this.setBufferfromData(this.inputBuffer, data_ID);
	}

	@Override
	public String getName()
	{
		return "Konvertieren";
	}

	@Override
	public void run()
	{
		gl.getContext().makeCurrent();//TODO wichtig
		System.out.println(this.data_ID_input.getMaxid());
		this.begin();
		
		IntBuffer buffers = Buffers.newDirectIntBuffer(2);
		
		gl.glGenBuffers(2, buffers);//Generiert zwei neue Buffernamen(int)
		
		//Daten von der cpu zur gpu kopieren
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(0));//Data_ID
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, this.inputBuffer.limit() * 4, this.inputBuffer, GL4.GL_STATIC_READ);
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(1));//Data_NPOS
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, this.outputBuffer.limit() * 4, this.outputBuffer, GL4.GL_STREAM_DRAW);
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		
		//Daten dem shader übergeben
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 0, buffers.get(0));
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 1, buffers.get(1));

        for (int i = 0; i < this.data_ID_input.getMaxid(); i += 10)
        {
        	gl.glUniform1i(this.getUniformLocation("offset"), i);
        	this.compute(this.data_ID_input.getMaxid() - i > 10 ? 10 : this.data_ID_input.getMaxid() - i, 1, 1);
        }

        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 0, 0);
        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 1, 0);
        
        //Daten von der gpu zur cpu kopieren
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(1));
        gl.glGetBufferSubData(GL4.GL_ARRAY_BUFFER, 0, this.outputBuffer.limit() * 4, this.outputBuffer);
        
        setDatafromBuffer(this.data_NPOS_output, this.outputBuffer);

		gl.glDeleteBuffers(2, buffers);
		this.end();
		this.dispose();
		
		GuiElements.MainGUI.setTab(this.data_NPOS_output);
	}

	

	@Override
	public Data getData()
	{
		return this.data_NPOS_output;
	}
}
