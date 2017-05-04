package texterkennung.data;

import java.awt.Color;
import java.util.Optional;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class Data_Zeichen extends Data
{
	private final int xstart;
	private final int ystart;
	private final int xend;
	private final int yend;
	private final int hoehe;
	private final int breite;
	private final Data_F data_F;
	private float[] snow_Boden;
	private float[] snow_Wand;
	private Series<Number, Number> xseries;
	private Series<Number, Number> yseries;
	private char c;
	private Optional<Data_Zeichen> gData_Zeichen = Optional.empty();
	
	private String s = "";//Debug

	/**
	 * Zeichen Data beinhaltete Zeichen Informationen
	 * @param c 
	 * @param xstart
	 * @param xend
	 * @param ystart
	 * @param yend
	 * @param data_F_input
	 * @param schwarzweiﬂ AA
	 * @param name Anzeige name in der Gui
	 */
	public Data_Zeichen(char c, int xstart, int xend, int ystart, int yend, Data_F data_F_input, boolean schwarzweiﬂ, String name)
	{
		super(name, false);
		this.c = c;
		this.xstart = xstart;
		this.xend = xend;
		this.ystart = ystart;
		this.yend = yend;
		this.hoehe = yend - ystart + 1;
		this.breite = xend - xstart + 1;
		this.data_F = data_F_input;
		
		this.xseries = new XYChart.Series<Number, Number>();
		this.yseries = new XYChart.Series<Number, Number>();
		this.xseries.setName("Zeichen: " + this.c);
		this.yseries.setName("Zeichen: " + this.c);
		this.snow(schwarzweiﬂ);
	}

	/**
	 * Schneefall ausrechnen
	 * @param schwarzweiﬂ AA an oder aus
	 */
	private void snow(boolean schwarzweiﬂ)
	{
		this.snow_Boden = new float[this.breite];
		this.snow_Wand = new float[this.hoehe];
		
		for (int x = 0; x < this.breite; x++)
		{
			float summe = 0;
			for (int y = 0; y < this.hoehe; y++)
			{
				if (schwarzweiﬂ)
				{
					summe += this.data_F.getFloat(xstart + x, ystart + y) < 0.5f ? 1 : 0;
				}
				else
				{
					summe += 1 - this.data_F.getFloat(xstart + x, ystart + y);
				}
			}
			
			this.snow_Boden[x] = summe / this.hoehe;
			this.xseries.getData().add(new XYChart.Data<Number,Number>((x + 0.5f) / this.breite, this.snow_Boden[x]));
		}
		for (int y = 0; y < this.hoehe; y++)
		{
			float summe = 0;
			for (int x = 0; x < this.breite; x++)
			{
				if (schwarzweiﬂ)
				{
					summe += this.data_F.getFloat(xstart + x, ystart + y) < 0.5f ? 1 : 0;
				}
				else
				{
					summe += 1 - this.data_F.getFloat(xstart + x, ystart + y);
				}
			}
			
			this.snow_Wand[y] = summe / this.breite;
			this.yseries.getData().add(new XYChart.Data<Number,Number>((y + 0.5f) / this.hoehe, this.snow_Wand[y]));
		}
	}
	
	/**
	 * Vergleicht zwei Zeichen und gibt die ‰hnlichkeit als wert zur¸ck.
	 * 
	 * @param zeichen Das Zeichen mit dem an diese Zeichen vergleicht
	 * @return Wert der die den Grad der ¸bereinstimmung an gibt, je kleiner dieser ist, desto ‰hnlicher sind sich die zeichen
	 */
	public double vergleichenmit(Data_Zeichen zeichen)
	{
		float wert;
		float vwert;
		
		//
		float summe = 0;
		for (int x = 0; x < zeichen.breite; x++)
		{
			wert = zeichen.getSnow_Boden(x);
			float x2 = ((x + 0.5f) / zeichen.breite) * this.breite - 0.5f;
			int x2i = (int) x2;
			
			vwert = this.getSnow_Boden(x2i) + (this.getSnow_Boden(x2i + 1) - this.getSnow_Boden(x2i)) * (x2 - x2i);
			
			summe += Math.pow(vwert - wert, 2);
		}
		summe /= zeichen.breite;
		
		//
		float summe2 = 0;
		for (int y = 0; y < zeichen.hoehe; y++)
		{
			wert = zeichen.getSnow_Wand(y);
			float y2 = ((y + 0.5f) / zeichen.hoehe) * this.hoehe - 0.5f;
			int y2i = (int) y2;
			
			vwert = this.getSnow_Wand(y2i) + (this.getSnow_Wand(y2i + 1) - this.getSnow_Wand(y2i)) * (y2 - y2i);
			
			summe2 += Math.pow(vwert - wert, 2);
		}
		summe2 /= zeichen.hoehe;
		
		//Hˆhen-Breiten Verh‰ltnis
		float ver = (zeichen.breite * 1.0f / zeichen.hoehe) / (this.breite * 1.0f / this.hoehe);
		
		ver = ver < 1 ? (1 / ver) - 1 : ver - 1;
		
		zeichen.s += this.c + " s1: " + summe + " s2: " + summe2 + " v: " + ver + " " + (summe + summe2 + ver) + "\n";
		
		return summe + summe2 + ver / 10;
	}
	
	/**
	 * 
	 * @param index 
	 * @return float 0.0 - 1.0f
	 */
	private float getSnow_Wand(int index)
	{
		return index < 0 ? this.snow_Wand[0] : (index < this.snow_Wand.length ? this.snow_Wand[index] : this.snow_Wand[this.snow_Wand.length - 1]);
	}
	
	/**
	 * 
	 * @param index 
	 * @return float 0.0 - 1.0f
	 */
	private float getSnow_Boden(int index)
	{
		return index < 0 ? this.snow_Boden[0] : (index < this.snow_Boden.length ? this.snow_Boden[index] : this.snow_Boden[this.snow_Boden.length - 1]);
	}
	
	public void setchar(Data_Zeichen data_Zeichen)
	{
		this.c = data_Zeichen.c;
		this.gData_Zeichen = Optional.of(data_Zeichen);
	}
	
	public char getchar()
	{
		return this.c;
	}
	
	public Series<Number, Number> getxSeries()
	{
		return this.xseries;
	}
	
	public Series<Number, Number> getySeries()
	{
		return this.yseries;
	}

	@Override
	public void gui(BorderPane borderPane)
	{
		Label l = new Label("Zeichen: " + this.c +"   xstart: " + this.xstart + " xend: " + this.xend + " ystart: " + this.ystart + " yend: " + this.yend);
		l.setFont(new Font(20));
		borderPane.setTop(l);
		
		WritableImage wr = new WritableImage(this.breite, this.hoehe);
        PixelWriter pw = wr.getPixelWriter();
		
		for (int y = 0; y < this.hoehe; y++)
		{
			for (int x = 0; x < this.breite; x++)
			{
				float wert = this.data_F.getFloat(this.xstart + x, this.ystart + y);
				
				pw.setArgb(x, y, new Color(wert, wert, wert).getRGB());
			}
		}
		
		ImageView image = new ImageView(wr);
		borderPane.setCenter(image);
		borderPane.setLeft(new javafx.scene.control.TextArea(this.s));
		
		
        final NumberAxis xAxis = new NumberAxis(0, 1, 0.1);
        final NumberAxis yAxis = new NumberAxis(0, 1, 0.1);
        final AreaChart<Number,Number> ac = new AreaChart<Number,Number>(xAxis,yAxis);
        ac.setTitle("Snow_Boden");
        
        ac.getData().add(this.xseries);
        this.gData_Zeichen.ifPresent(gData_Zeichen -> {
        	XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        	series.setName(gData_Zeichen.getxSeries().getName());
        	for (javafx.scene.chart.XYChart.Data<Number, Number> data : gData_Zeichen.getxSeries().getData())
        	{
        		series.getData().add(new javafx.scene.chart.XYChart.Data<Number, Number>(data.getXValue(), data.getYValue()));
        	}
        	ac.getData().add(series);
        });
        borderPane.setBottom(ac);
        
        final NumberAxis xAxis2 = new NumberAxis(0, 1, 0.1);
        final NumberAxis yAxis2 = new NumberAxis(0, 1, 0.1);
        final AreaChart<Number,Number> ac2 = new AreaChart<Number,Number>(xAxis2,yAxis2);
        ac2.setTitle("Snow_Wand");
        
        ac2.getData().add(this.yseries);
        this.gData_Zeichen.ifPresent(gData_Zeichen -> {
        	XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        	series.setName(gData_Zeichen.getySeries().getName());
        	for (javafx.scene.chart.XYChart.Data<Number, Number> data : gData_Zeichen.getySeries().getData())
        	{
        		series.getData().add(new javafx.scene.chart.XYChart.Data<Number, Number>(data.getXValue(), data.getYValue()));
        	}
        	ac2.getData().add(series);
        });
        borderPane.setRight(ac2);
	}
}
