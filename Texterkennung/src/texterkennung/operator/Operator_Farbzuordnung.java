package texterkennung.operator;

import java.util.ArrayList;

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
public class Operator_Farbzuordnung extends OperatorGPU
{
	private final static String computeShaderPath = "./src/jogl/shader/Farbzuordnung.glsl";
	
	private Data_ID data_ID;
	
	private final ABufferedImage originalBild;
	private final ArrayList<AColor> farbListe;
	private final int schwellwert;
	
	public Operator_Farbzuordnung(ABufferedImage originalBild, ArrayList<AColor> farbListe, int schwellwert, GL4 gl4)
	{
		super(gl4, computeShaderPath);
		this.originalBild = originalBild;
		this.farbListe = farbListe;
		this.schwellwert = schwellwert;
		this.data_ID = new Data_ID(this.originalBild.getWidth(), this.originalBild.getHeight());
	}
	
	@SuppressWarnings("null")
	public Operator_Farbzuordnung(ABufferedImage originalBild, ArrayList<AColor> farbListe, GL4 gl4)
	{
		super(gl4, computeShaderPath);
		this.originalBild = originalBild;
		this.farbListe = farbListe;
		this.schwellwert = (Integer) null;
		this.data_ID = new Data_ID(this.originalBild.getWidth(), this.originalBild.getHeight());
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
		
		int[] buffers = new int[1];
		gl.glGenBuffers(1, buffers, 0);
		gl.glBindBuffer(GL4.GL_SHADER_STORAGE_BUFFER, buffers[0]);
		
		
		
		//TODO SSBO alles
		
		
		
		gl.glUniform1i(this.getUniformLocation("schwellwert"), this.schwellwert);

        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 0, buffers[0]);
        

        this.compute((data_ID.getXlenght() / 16) + 1, (data_ID.getYlenght() / 16) + 1, 1);

        gl.glBindBufferBase(GL4.GL_SHADER_STORAGE_BUFFER, 0, 0);

		
		this.end();
		
		
		
		
		
		
		
		
		
		
		
		
		//TODO parallelisieren??? möglich ist es
		for (int y = 0; y < this.originalBild.getHeight(); y++)
		{
			for (int x = 0; x < this.originalBild.getWidth(); x++)
			{
				int i = 0;
				while (i < farbListe.size() && !farbListe.get(i).isColor(this.originalBild.getRGB(x, y), schwellwert))
				{
					i++;
				}
				if (i != farbListe.size())
				{
					this.data_ID.setInt(x, y, i + 1);
				}
				else
				{
					this.data_ID.setInt(x, y, 0);
				}
			}
		}
		
		GuiElements.MainGUI.setTab(this.data_ID);
	}
	
	@Override
	public Data getData()
	{
		return this.data_ID;
	}
}
