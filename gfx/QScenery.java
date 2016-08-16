package tryan.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class QScenery {
	private int x;
	private int y;
	private int layer;
	private BufferedImage img;
	
	public QScenery(int x, int y, BufferedImage img, int layer) {
		this.x = x;
		this.y = y;
		this.layer = layer;
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
	
	public int getLayer() {
		return layer;
	}
}
