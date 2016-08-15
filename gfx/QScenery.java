package ryan.tom.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class QScenery {
	private int x;
	private int y;
	BufferedImage img;
	
	public QScenery(int x, int y, BufferedImage img) {
		this.x = x;
		this.y = y;
		this.img = img;
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(img, null, x, y);
	}
	
	public int getWidth() {
		return img.getWidth();
	}
	
	public int getHeight() {
		return img.getHeight();
	}
}
