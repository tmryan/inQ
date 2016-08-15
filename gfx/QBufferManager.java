package ryan.tom.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/*
 * Note: When this is eventually not good enough, then abstract buffer
 * 		 create threads for getting and creating buffers
 */

public class QBufferManager {
	BufferedImage bufferOne;
	BufferedImage bufferTwo;
	BufferedImage frontBuffer;
	boolean isBufferOneBack;

	public QBufferManager(int width, int height) {
		bufferOne = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		bufferTwo = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		frontBuffer = bufferOne;
		isBufferOneBack = false;
	}
	
	public void swap() {
		if(isBufferOneBack == true) {
			frontBuffer = bufferOne;
		} else {
			frontBuffer = bufferTwo;
		}
	}
	
	public Graphics2D getGraphics2D() {
		return (Graphics2D) frontBuffer.getGraphics();
	}
	
	public BufferedImage push() {		
		BufferedImage frameBuffer = frontBuffer;
		
		isBufferOneBack = !isBufferOneBack;
		swap();
		
		return frameBuffer;
	}
	
	
}
