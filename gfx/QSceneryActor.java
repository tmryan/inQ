package tryan.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/*
 * Note: Need to abstract actors
 */
public class QSceneryActor extends QActor {
	
	public QSceneryActor(int x, int y, BufferedImage img, QResourceManager resMan, int layer) {
		super(x, y, img, resMan, layer);
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
