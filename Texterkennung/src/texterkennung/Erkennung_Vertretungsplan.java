package texterkennung;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;

import advanced.AColor;
import javafx.scene.layout.Pane;

public class Erkennung_Vertretungsplan extends Erkennung
{
	public Erkennung_Vertretungsplan(BufferedImage bufferedImage, ArrayList<AColor> farbListe, Font font, GL4 gl4)
	{
		super(bufferedImage, farbListe, font, gl4);
	}

	/*@Override
	public String getName()
	{
		return "Erkennung_Vertretungsplan";
	}*/

	@Override
	public void run()
	{
		System.out.println("run");
		
		
		
		
		
		/*testBild = new ABufferedImage(this.originalBild.getWidth(), this.originalBild.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for (int x = 0; x < this.originalBild.getWidth(); x++)
		{
			for (int y = 0; y < this.originalBild.getHeight(); y++)
			{
				testBild.setRGB(x, y, new Color((this.originalBild.getDifferenzBild().getRot(x, y) < par1) ? 0 : this.originalBild.getDifferenzBild().getRot(x, y), (this.originalBild.getDifferenzBild().getGruen(x, y) < par1) ? 0 : this.originalBild.getDifferenzBild().getGruen(x, y), (this.originalBild.getDifferenzBild().getBlau(x, y) < par1) ? 0 : this.originalBild.getDifferenzBild().getBlau(x, y)).getRGB());
			}
		}
		
		this.testBild.prepare();
		
		
		
		for (int y = 0; y < originalBild.getHeight(); y++)
		{
			int x = 0;			
			for (x = 0; x < originalBild.getWidth(); x++)
			{
				if (this.testBild.getDurchschnitt(x, y, par2) < 1)
				{
					this.originalBild.setRGB(x, y, new Color(this.originalBild.getRot(x, y), this.originalBild.getGruen(x, y), this.originalBild.getBlau(x, y), 0).getRGB());
				}
			}
		}*/
	}

	@Override
	public void gui(Pane pane)
	{
		/*if (testBild!=null)TODO fix this
		{
            int hoeheneu=testBild.getHeight()*w/testBild.getWidth();
            System.out.println("Maße des Bildes original: "+testBild.getWidth()+" "+testBild.getHeight()+" , nach Anpassung: "+w+" "+hoeheneu);
            System.out.println("out");
            g.drawImage(originalBild.getDifferenzBild(),w/2 , 0, w/2,hoeheneu/2, null);
           g.drawImage(originalBild,0 , 0, w/2,hoeheneu/2, null);
            g.drawImage(testBild,0 , hoeheneu/2, w/2,hoeheneu/2, null);
            //g.drawImage(originalBild.getDifferenzBild(),w/2 , hoeheneu/2, w/2,hoeheneu/2, null);
            //g.drawImage(Zeichen.zeichenListe.get("A").getBufferedImage(),w/2 , 0, 20,40, null);
        }*/
	}
}
