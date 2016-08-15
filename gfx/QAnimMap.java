package ryan.tom.inq.gfx;

import java.util.HashMap;

public class QAnimMap {
	HashMap<String, QAnimation> animations;
	
	public QAnimMap() {
		animations = new HashMap<String, QAnimation>();
	}
	
	public void addAnimation(String animName, QAnimation anim) {
		animations.put(animName, anim);
	}
	
	public void setDefaultAnimation(String animName) {
		animations.put("default", animations.get(animName));
	}
	
	public QAnimation.AnimMapIterator getAnimSetIerator(String animName) {
		return animations.get(animName).getAnimSetIerator(animName); 
	}
	
}
