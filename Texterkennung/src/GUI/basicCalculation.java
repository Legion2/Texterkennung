package GUI;

import java.awt.image.BufferedImage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class basicCalculation {

	/**
	 * @author https://blog.idrsolutions.com/2012/11/convert-bufferedimage-to-javafx-image/
	 * 
	 * @param bf
	 * @return
	 */
	public static ImageView BufferdToImage (BufferedImage bf) {
	
 
        WritableImage wr = null;
        if (bf != null) {
            wr = new WritableImage(bf.getWidth(), bf.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bf.getWidth(); x++) {
                for (int y = 0; y < bf.getHeight(); y++) {
                    pw.setArgb(x, y, bf.getRGB(x, y));
                }
            }
        }
 
        ImageView imView = new ImageView(wr);
        return imView;
	}
	
}
