package tryan.inq.gfx;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import tryan.inq.overhead.QGameSettings;
import tryan.inq.state.QGameState;

@SuppressWarnings("serial")
public class QGraphics extends Canvas {
	private QGameState gState;
	private HashMap<Integer, QScene> scenes;
	private QUserInterface ui;
	private BufferStrategy bufferMan;
	private BufferedImage bImg;
	private JFrame frame;
	
	public QGraphics(QGameState gState) {
		this.gState = gState;
		scenes = new HashMap<Integer, QScene>();
		frame = new JFrame();
		bImg = null;
		
		// Note: add UI object here
		
		init();
	}
	
	public void init() {
		// JFrame and Canvas settings
		frame.setIgnoreRepaint(true);
		frame.setSize(QGameSettings.getWinWidth(), QGameSettings.getWinHeight());
		frame.setTitle("inQ Engine Test");
		
		setIgnoreRepaint(true);
		setSize(QGameSettings.getWinWidth(), QGameSettings.getWinHeight());
		
		frame.add(this);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		// Creating double buffering strategy for this Canvas
		createBufferStrategy(2);
	}
	
	public void updateView(long tickTime) {
		scenes.get(gState.getCurrentSceneID()).updateView(tickTime);
	}
	
	public void render() {
		// Note: Safe to save bg2 handle in order to improve performance? Maybe not?
		Graphics2D bg2 = bImg.createGraphics();
		Graphics2D g2 = null;
		QScene scene = scenes.get(gState.getCurrentSceneID());
		
		do {
			try {
				g2 = (Graphics2D) bufferMan.getDrawGraphics();
				
				// Clearing back buffer before painting
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, scene.getSceneWidth(), scene.getSceneWidth());

				// Drawing scene to BufferedImage
				scenes.get(gState.getCurrentSceneID()).draw(bg2);
				
				// Note: Find out why coordinate space is flipped about x-axis here
				// Copying area of scene inside camera viewport onto back buffer
				g2.drawImage(bImg, null, scene.getCamera().getX() * -1, scene.getCamera().getY() * -1);
			} finally {
				g2.dispose();
				bg2.dispose();
			}
			
			// Swap or blt back buffer
			bufferMan.show();
		} while(bufferMan.contentsLost() || bufferMan.contentsRestored());
	}
	
	// For resizing window when user adjusts graphics settings
	public void resizeWindow() {
		frame.setSize(QGameSettings.getWinWidth(), QGameSettings.getWinHeight());
		setSize(QGameSettings.getWinWidth(), QGameSettings.getWinHeight());
		
		refreshGfx();
	}
	
	public void refreshGfx() {
		bImg = getGraphicsConfiguration().createCompatibleImage(scenes.get(gState.getCurrentSceneID()).getSceneWidth(), 
				scenes.get(gState.getCurrentSceneID()).getSceneHeight());
		
		bufferMan = getBufferStrategy();
	}
	
	public void addScene(QScene scene, int id) {
		scenes.put(id, scene);
	}
	
	/////////////
	// Debugging
	//////////
	
	// Note: Moving this here until UI class is implemented
	public void enablePathingOverlay(boolean isPathingOverlayEnabled) {
		scenes.get(gState.getCurrentSceneID()).enablePathingOverlay(isPathingOverlayEnabled);
	}
	
	public void enableBoundsOverlay(boolean ispBoundsEnabled) {
		scenes.get(gState.getCurrentSceneID()).enableBoundsOverlay(ispBoundsEnabled);
	}
		
}
