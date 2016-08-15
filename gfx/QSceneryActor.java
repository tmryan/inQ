package ryan.tom.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/*
 * Note: Need to abstract actors
 */
public class QSceneryActor extends QActor {
	
	public QSceneryActor(int x, int y, BufferedImage img, QResourceManager resMan) {
		super(x, y, img, resMan);
	}

	@Override
	public void draw(Graphics2D g2) {
		if(getAnimIterator() != null && getAnimIterator().hasNextFrame()) {
			g2.drawImage(getAnimIterator().getNextFrame(), null, getX(), getY());		
		} else if(getDefaultImg() != null) {
			g2.drawImage(getDefaultImg(), null, getX(), getY());
		}
	}
}
