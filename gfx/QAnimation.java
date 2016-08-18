package tryan.inq.gfx;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class QAnimation {
	private int priority;
	private LinkedList<AnimFrame> frames;
	
	public QAnimation(String name, int priority, AnimFrame... frames) {
		this.priority = priority;
		this.frames = new LinkedList<AnimFrame>();
		
		for(AnimFrame frame : frames) {
			this.frames.add(frame);
		}
	}
	
	public QAnimation(String name, int priority, BufferedImage... frames) {
		this.priority = priority;
		this.frames = new LinkedList<AnimFrame>();
		
		for(BufferedImage frame : frames) {
			// Setting default lifetime to 1 for now so anim will play
			this.frames.add(new AnimFrame(frame, 1));
		}
	}
	
	public QAnimation(String name, int priority) {
		this.priority = priority;
		frames = new LinkedList<AnimFrame>();
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
		
		public AnimFrame getNextFrame() {
			return frames.get(index++);
		}
	}
	
	/////////////
	// AnimFrame
	//////////
	
	public static class AnimFrame {
		BufferedImage frame;
		int timing;
		
		public AnimFrame(BufferedImage img, int lifetime) {
			frame = img;
			timing = lifetime;
		}
		
		public BufferedImage getImage() {
			return frame;
		}
		
		public int getLifetime() {
			return timing;
		}
	}
	
}
