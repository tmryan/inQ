package tryan.inq.gfx;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class QAnimation {
	private int priority;
	private LinkedList<BufferedImage> frames;
	
	public QAnimation(String name, int priority, BufferedImage... imgs) {
		this.priority = priority;
		frames = new LinkedList<BufferedImage>();
		
		for(BufferedImage frame : imgs) {
			frames.add(frame);
		}
	}
	
	public AnimMapIterator getAnimSetIerator(String animName) {
		return new AnimMapIterator();
	}
	
	public int getPriority() {
		return priority;
	}
	
	////////////////////////////
	// External Iterator Class
	/////////////////////////

	public class AnimMapIterator {
		int index;
	
		public AnimMapIterator() {
			index = 0;
		}
		
		public boolean hasNextFrame() {
			return (index < frames.size());
		}
		
		public BufferedImage getNextFrame() {
			return frames.get(index++);
		}
	}
	
}
