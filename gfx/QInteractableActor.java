package ryan.tom.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class QInteractableActor extends QActor {
	
	public QInteractableActor(int x, int y, BufferedImage img, QResourceManager resMan) {
		super(x, y, img, resMan);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		if(((QInteractableState)getActorState()).isHighlighted()) {
			g2.setColor(getResMan().getColor("highlightClr"));
			g2.fillRoundRect(getX()-2, getY()-2, getWidth()+4, getHeight()+4, 3, 3);
		}
		
		if(getAnimIterator() != null && getAnimIterator().hasNextFrame()) {
			g2.drawImage(getAnimIterator().getNextFrame(), null, getX(), getY());		
		} else if(getDefaultImg() != null) {
			g2.drawImage(getDefaultImg(), null, getX(), getY());
		}
	}
	
}
