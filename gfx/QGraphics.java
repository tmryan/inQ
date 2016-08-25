package tryan.inq.gfx;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
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
//	private BufferedImage bImg;
	private JFrame frame;
	
	// Note: Temporarily here until UI implemented
	int fps;
	long tDelta;
	
	public QGraphics(QGameState gState) {
		this.gState = gState;
		scenes = new HashMap<Integer, QScene>();
		frame = new JFrame();
//		bImg = null;

		// Note: add UI object here
		fps = 0;
		tDelta = 0;

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
		refreshGfx();
	}
	
	public void updateView(long tickTime) {
		scenes.get(gState.getCurrentSceneID()).updateView(tickTime);
	}
	
	public void render() {
		// Note: Only creating this image when no longer compatible saves a few FPS
		BufferedImage bImg = getGraphicsConfiguration().createCompatibleImage(scenes.get(gState.getCurrentSceneID()).getSceneWidth(), 
				scenes.get(gState.getCurrentSceneID()).getSceneHeight());
		Graphics2D bg2 = bImg.createGraphics();
		Graphics2D g2 = null;
		QScene scene = scenes.get(gState.getCurrentSceneID());
		
		do {
			try {
				g2 = (Graphics2D) bufferMan.getDrawGraphics();
				
				// Clearing back buffer before painting
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, scene.getSceneWidth(), scene.getSceneWidth());

				/*
				 *  Note: This call takes around 30-40ms to complete on i5 laptop with Windows 10
				 *  	  and 26ms on desktop with i5 and NVidia card
				 *  	  Problem is drawing too much on bg2, especially transparent pixels
				 *  	  Best solution is use OpenGL, other solutions involve scene tiling
				 *  	  and fixing the art so it has less transparency
				 */
				// Drawing scene to BufferedImage
				scenes.get(gState.getCurrentSceneID()).draw(bg2);

				// Note: Coordinates flipped when creating compatible image from graphics configuration
				// Copying area of scene inside camera viewport onto back buffer
				g2.drawImage(bImg, null, scene.getCamera().getX() * -1, scene.getCamera().getY() * -1);
				
				g2.drawString("FPS: " + fps, 20, 20);
				g2.drawString("tDelta: " + tDelta, 20, 35);
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
		bufferMan = getBufferStrategy();
	}
	
	public void addScene(QScene scene, int id) {
		scenes.put(id, scene);
	}
	
	/////////////
	// Debugging
	//////////
	
	// Note: Moving this here until UI class is implemented
	public void setFPS(int fps) {
		this.fps = fps;
	}
	
	public void setTDelta(long tDelta) {
		this.tDelta = tDelta;
	}
	
	public void enablePathingOverlay(boolean isPathingOverlayEnabled) {
		scenes.get(gState.getCurrentSceneID()).enablePathingOverlay(isPathingOverlayEnabled);
	}
	
	public void enableBoundsOverlay(boolean ispBoundsEnabled) {
		scenes.get(gState.getCurrentSceneID()).enableBoundsOverlay(ispBoundsEnabled);
	}
		
}
