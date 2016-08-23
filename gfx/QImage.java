package tryan.inq.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class QImage {
	private QImage(String file) {}
	
	public static BufferedImage createBufferedImage(String file) {
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(new File("res/" + file));
		} catch (IOException e) {
			System.out.println("Failed to load " + file);
			e.printStackTrace();
		}
		
		return img;
	}
}
