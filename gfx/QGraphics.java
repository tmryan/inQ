package ryan.tom.inq.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class QGraphics extends JFrame {
	private QGameState gState;
	private HashMap<Integer, QScene> scenes;
	private QUserInterface ui;
	private BufferStrategy bufferMan;
	private QCameraActor camera;
	
	public QGraphics(QGameState gState) {
		this.gState = gState;
		scenes = new HashMap<Integer, QScene>();
		camera = null;

		init();
	}
	
	public void init() {
		// JFrame settings
		setSize(QGameSettings.getWinWidth(), QGameSettings.getWinHeight());
		setTitle("inQ Engine Test");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		// Creating double buffering strategy for this JFrame
		createBufferStrategy(2);
		bufferMan = getBufferStrategy();
	}
	
	public void updateView(long tickTime) {
		camera.updateView(tickTime);
		scenes.get(gState.getCurrentSceneID()).updateView(tickTime);
	}
	
	public void render() {
		Graphics2D g2 = null;
		
		do {
			try {
				g2 = (Graphics2D) bufferMan.getDrawGraphics();
				scenes.get(gState.getCurrentSceneID()).draw(g2);
			} finally {
				g2.dispose();
			}
			
			bufferMan.show();
		} while(bufferMan.contentsLost() || bufferMan.contentsRestored());
	}
	
	public void addScene(QScene scene, int id) {
		scenes.put(id, scene);
	}
	
	public void addCamera(QCameraActor cameraActor) {
		camera = cameraActor;
	}
	
	// Note: Don't like this, but for now...
	public QCameraActor getCamera() {
		return camera;
	}
	
}
