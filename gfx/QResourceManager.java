package ryan.tom.inq.gfx;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class QResourceManager {
	private HashMap<String, BufferedImage> imgMap;
	private HashMap<String, Color> colorMap;
	private HashMap<String, QAnimMap> animMap;
	
	public QResourceManager() {
		imgMap = new HashMap<String, BufferedImage>();
		colorMap = new HashMap<String, Color>();
		animMap = new HashMap<String, QAnimMap>();
	}
	
	public void addImage(String file) {
		imgMap.put(file, QImage.createBufferedImage(file));
	}
	
	public BufferedImage getImage(String file) {
		return imgMap.get(file);
	}
	
	public void addColor(String name, Color color) {
		colorMap.put(name, color);
	}
	
	public Color getColor(String name) {
		return colorMap.get(name);
	}
	
	public void addAnimMap(String name, QAnimMap animSet) {
		animMap.put(name, animSet);
	}
	
	public QAnimMap getAnimMap(String name) {
		return animMap.get(name);
	}
}
