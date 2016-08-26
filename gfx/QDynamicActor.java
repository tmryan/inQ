package tryan.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import tryan.inq.gfx.QAnimUtils.QTransformation;
import tryan.inq.mobility.QDirection;
import tryan.inq.overhead.QResourceManager;
import tryan.inq.state.QAnimState;
import tryan.inq.state.QDynamicActorState;

public class QDynamicActor extends QActor {
	private QDynamicActorState actorState;
	private int currentFrameLifetime;
	private QAnimation.AnimFrame currentAnimFrame;
	private QAnimState animState;
	private QAnimMap anims;
	private QAnimation.AnimMapIterator animIter;	
	/* 
	 * Note: This will eventually be pulled from actor state on update and applied to animation frame timings
	 * 		 for cases where the actor speed has increased or decreased and the anim speed needs to reflect 
	 * 		 the change
	 */
	private double animSpeedMultiplier;

	public QDynamicActor(BufferedImage img, QResourceManager resMan, int layer) {
		super(img, resMan, layer);
		
		currentFrameLifetime = 0;
		currentAnimFrame = null;
		animState = QAnimState.IDLE;
		anims = null;
		animIter = null;
		animSpeedMultiplier = 1.0;
		actorState = null;
	}

	// Update view from current model state
	public void updateView(long tickTime) {
		setTDelta(getTDelta() + (int) tickTime);

		setX(actorState.getX());
		setY(actorState.getY());
		
		animState = getActorState().getCurrentAnimState();
		
		if(animIter != null) {
			manageAnims();
		}
	}
	
	@Override
	public void draw(Graphics2D g2) {
		if(animIter != null && animIter.hasNextFrame()) {
			if(getTDelta() < currentFrameLifetime) {
				if(actorState.getDirection().equals(QDirection.E) || actorState.getDirection().equals(QDirection.NE)) {
					g2.drawImage(currentAnimFrame.getImage(), null, getX(), getY());
				} else {
					// Note: Pregenerating these transformed images may save a few ms
					g2.drawImage(QAnimUtils.performXFormation(QTransformation.FLIPX, currentAnimFrame.getImage()), null, getX(), getY());
				}
			} else {
				setTDelta(0);
				currentAnimFrame = animIter.getNextFrame();
				currentFrameLifetime = currentAnimFrame.getLifetime();
				
				if(actorState.getDirection().equals(QDirection.E) || actorState.getDirection().equals(QDirection.NE)) {
					g2.drawImage(currentAnimFrame.getImage(), null, getX(), getY());
				} else {
					g2.drawImage(QAnimUtils.performXFormation(QTransformation.FLIPX, currentAnimFrame.getImage()), null, getX(), getY());
				}
			}
		} else if(getDefaultImg() != null) {
			setTDelta(0);
			currentFrameLifetime = 0;
			
			if(actorState.getDirection().equals(QDirection.E)) {
				g2.drawImage(getDefaultImg(), null, getX(), getY());
			} else {
				g2.drawImage(QAnimUtils.performXFormation(QTransformation.FLIPX, getDefaultImg()), null, getX(), getY());
			}
		}
	}
	
	public void manageAnims() {
		if(!animIter.hasNextFrame()) {
			if(animState.isLooping()) {
				// Loop the animation
				refreshAnimIterator();
			} else {
				// Play idle animation since previous animation was not looping
				animState = QAnimState.IDLE;
				refreshAnimIterator();
			}
		}
	}
	
	public void refreshAnimIterator() {
		if(anims != null) {
			animIter = anims.getAnimSetIerator(animState.getAnimName());
		}
	}
	
	public void addAnimMap(QAnimMap animSet) {
		anims = animSet;
		refreshAnimIterator();
	}
	
	public void attachActorState(QDynamicActorState actorState) {
		this.actorState = actorState;
	}

	@Override
	public QDynamicActorState getActorState() {
		return actorState;
	}
	
	public QAnimation.AnimMapIterator getAnimIterator() {
		return animIter;
	}
	
	public QAnimation.AnimFrame getCurrentAnimFrame() {
		return currentAnimFrame;
	}
	
	public void setCurrentAnimFrame(QAnimation.AnimFrame frame) {
		currentAnimFrame = frame;
	}
	
	public int getCurrentFrameLifetime() {
		return currentFrameLifetime;
	}
	
	public void setCurrentFrameLifetime(int lifetime) {
		currentFrameLifetime = lifetime;
	}
	
	@Override
	public int getWidth() {
		return actorState.getWidth();
	}

	@Override
	public int getHeight() {
		return actorState.getHeight();
	}
	
}
