package tryan.inq.overhead;

import java.awt.Color;

public class QResourceLoader {
	
	public QResourceLoader() {}
	
	// Note: Need an XML or JSON loader system so files and paths not hardcoded
	public void loadResources(QResourceManager resMan) {	
		resMan.addImage("peegPlayer.png");
		resMan.addImage("peegPlayerWalk1.png");
		resMan.addImage("peegPlayerWalk2.png");
		resMan.addImage("bg1.jpg");
		resMan.addImage("bg2.png");
		resMan.addImage("bg3.png");
		resMan.addImage("playableArea.png");
		resMan.addImage("sun.png");
		resMan.addImage("clickyTest.png");
		
		resMan.addColor("highlightClr", new Color(23, 164, 255));
	}
	
}
