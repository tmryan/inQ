package tryan.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import tryan.inq.overhead.QResourceManager;
import tryan.inq.state.QAnimState;

public class QFadeableActor extends QInteractableActor {
	
	public QFadeableActor(BufferedImage img, QResourceManager resMan, int layer) {
		super(img, resMan, layer);		
	}

	@Override
	public void draw(Graphics2D g2) {
		if(getAnimIterator() != null && getAnimIterator().hasNextFrame()) {
			if(getTDelta() < getCurrentFrameLifetime()) {
				g2.drawImage(getCurrentAnimFrame().getImage(), null, getX(), getY());
			} else {
				setTDelta(0);
				setCurrentAnimFrame(getAnimIterator().getNextFrame());
				setCurrentFrameLifetime(getCurrentAnimFrame().getLifetime());
				
				g2.drawImage(getCurrentAnimFrame().getImage(), null, getX(), getY());
			}
		} else if(getActorState().getCurrentAnimState().equals(QAnimState.FADED)) {
			// If actor is faded, no draw
			
			/*
			 *  Note: It might be good to implement a timer that multiple classes can use
			 *  	  Could pause it in cases like this
			 */
			setTDelta(0);
		} else if(getDefaultImg() != null) {
			setTDelta(0);
			setCurrentFrameLifetime(0);
			
			g2.drawImage(getDefaultImg(), null, getX(), getY());
		}
	}
	
}
