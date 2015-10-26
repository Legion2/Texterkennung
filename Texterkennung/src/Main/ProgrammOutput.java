package Main;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import GUI.IGUI;
import advanced.AColor;
import texterkennung.Erkennung;
import texterkennung.Erkennung_Text;

public class ProgrammOutput implements IGUI
{
	public Erkennung erkennung;

	@Override
	public void gui() {
		// TODO Hier muss die gui ausgegeben werden
		
	}

	@Override
	public String getName()
	{
		return "Standart Ausgabe Fenster";
	}
	
	/**
	 * Wird aufgerufen wenn der Knopf zum start der Texterkennung gedrückt wird.
	 */
	public void Knopf_gedrueckt_Texterkennen()
	{
        if (this.erkennung != null)
        {
        	
        	int par1=Integer.parseInt("");
            int par2=Integer.parseInt("");
            
            this.erkennung.run(par1, par2);
        }
      // TODO Hier muss die Anzeige der gui aktuallisiert werden
	}
	
	/**
	 * Wird aufgerufen wenn der Knopf zum laden des Bildes gedrückt wird.
	 */
	public void Knopf_gedrueckt_Bildladen()
	{
        // TODO Pfad zur Bild datei
		String pfad = "";
        if (pfad != null)
        {
            try
            {
            	ArrayList<AColor> farbListe = new ArrayList<AColor>();
            	farbListe.add(new AColor(0, 0, 0));//Farbe Schwarz
            	this.erkennung = new Erkennung_Text(ImageIO.read(new File(pfad)), farbListe, new Font("Arial", Font.PLAIN, 30));

                // TODO Hier muss die Anzeige der gui aktuallisiert werden
            } catch (IOException ex) {
                System.out.println("Fehler aufgetreten beim Lesen der Datei");
            }
        }
	}
}
