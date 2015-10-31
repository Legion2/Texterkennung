package debug;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sun.prism.Image;

import GUI.IGUI;

public class Debugger implements IGUI
{
	ArrayList<IDebugger> list;
	
	public Debugger()
	{
		list = new ArrayList<IDebugger>();
		// TODO nichtfertig
	}

	@Override
	public String getName()
	{
		return "Debugger";
	}

	@Override
	public void gui() {
		// TODO Hier die Gui für den debugger erstellen
		
	}

	public void add(IDebugger data)
	{
		list.add(data);
	}
	
	public void savelist()
	{
		int id = 0;
		for (IDebugger idebugger : list)
		{
			try {
				ImageIO.write(idebugger.visualisieren(), "png", new File("D:/GitHub/Texterkennung/Texterkennung/res/test_" + idebugger.getName() + "_" + id++ + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
