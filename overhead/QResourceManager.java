package tryan.inq.overhead;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import tryan.inq.gfx.QAnimMap;
import tryan.inq.gfx.QAnimUtils;
import tryan.inq.gfx.QImage;
import tryan.inq.gfx.QAnimUtils.QTransformation;

public class QResourceManager {
	private HashMap<String, BufferedImage> imgMap;
	private HashMap<String, Color> colorMap;
	private HashMap<String, QAnimMap> animMap;
	
	public QResourceManager() {
		imgMap = new HashMap<String, BufferedImage>();
		colorMap = new HashMap<String, Color>();
		animMap = new HashMap<String, QAnimMap>();
	}
	
	// Note: May need to add another String parameter to these methods for cases where the filepath is long
	/**
	 * Adds a new BufferedImage object to the resource manager map with the file name as its key.
	 * 
	 * @param file		Name of the file which holds the image to be added.
	 */
	public void addImage(String file) {
		imgMap.put(file, QImage.createBufferedImage(file));
	}
	
	/**
	 * Adds a new BufferedImage object to the resource manager map with with the file name as its key. 
	 * The given transformation is applied to the image before it is stored.
	 * 
	 * @param file		Name of the file which holds the image to be added.
	 * @param xForm		The QAnimUtils.QTransformation to be applied to the BufferedImage.
	 */
	public void addImage(String file, QAnimUtils.QTransformation xForm) {
		imgMap.put(file, QAnimUtils.performXFormation(xForm, QImage.createBufferedImage(file)));
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
