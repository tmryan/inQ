package tryan.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import tryan.inq.overhead.QResourceManager;

/*
 * Note: Need to differentiate dynamic and static scenery actors
 */
public class QSceneryActor extends QDynamicActor {
	
	public QSceneryActor(BufferedImage img, QResourceManager resMan, int layer) {
		super(img, resMan, layer);
	}

	@Override
	public void draw(Graphics2D g2) {
		// Note: Need to implement timings for animated scenery
		if(getAnimIterator() != null && getAnimIterator().hasNextFrame()) {
			QAnimation.AnimFrame frame = getAnimIterator().getNextFrame();
			setCurrentFrameLifetime(frame.getLifetime());
			
			g2.drawImage(frame.getImage(), null, getX(), getY());		
		} else if(getDefaultImg() != null) {
			g2.drawImage(getDefaultImg(), null, getX(), getY());
		}
	}
}
