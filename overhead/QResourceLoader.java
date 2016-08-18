package tryan.inq.overhead;

import java.awt.Color;

public class QResourceLoader {
	
	public QResourceLoader() {}
	
	// Note: Need an XML or JSON loader system so files and paths not hardcoded
	public void loadResources(QResourceManager resMan) {	
		resMan.addImage("peegPlayer.png");
		resMan.addImage("peegPlayerWalk1.png");
		resMan.addImage("peegPlayerWalk2.png");
		resMan.addImage("bg.jpg");
		resMan.addImage("hills1.png");
		resMan.addImage("hills2.png");
		resMan.addImage("rocks.png");
		resMan.addImage("tree1.png");
		resMan.addImage("clickyTest.png");
		resMan.addImage("sun.png");
		
		resMan.addColor("highlightClr", new Color(23, 164, 255));
	}
	
}
