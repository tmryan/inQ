package tryan.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import tryan.inq.overhead.QResourceManager;
import tryan.inq.state.QInteractableState;

public class QInteractableActor extends QActor {
	
	public QInteractableActor(int x, int y, BufferedImage img, QResourceManager resMan, int layer) {
		super(x, y, img, resMan, layer);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		// Note: Need to implement timings for animated interactables
		if(((QInteractableState)getActorState()).isHighlighted()) {
			g2.setColor(getResMan().getColor("highlightClr"));
			g2.fillRoundRect(getX()-2, getY()-2, getWidth()+4, getHeight()+4, 3, 3);
		}
		
		if(getAnimIterator() != null && getAnimIterator().hasNextFrame()) {
			g2.drawImage(getAnimIterator().getNextFrame().getImage(), null, getX(), getY());		
		} else if(getDefaultImg() != null) {
			g2.drawImage(getDefaultImg(), null, getX(), getY());
		}
	}
	
}
