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

@SuppressWarnings("serial")
public class QGraphics extends Canvas {
	private QGameState gState;
	private HashMap<Integer, QScene> scenes;
	private QUserInterface ui;
	private BufferStrategy bufferMan;
	private JFrame frame;
	
	public QGraphics(QGameState gState) {
		this.gState = gState;
		scenes = new HashMap<Integer, QScene>();
		frame = new JFrame();
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
		bufferMan = getBufferStrategy();
	}
	
	public void updateView(long tickTime) {
		scenes.get(gState.getCurrentSceneID()).updateView(tickTime);
	}
	
	public void render() {
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

				// Drawing scene to BufferedImage
				scenes.get(gState.getCurrentSceneID()).draw(bg2);
				
				// Note: Find out why coordinate space is flipped here
				// Copying area of scene inside camera viewport onto back buffer
				g2.drawImage(bImg, null, scene.getCamera().getX() * -1, scene.getCamera().getY());
			} finally {
				g2.dispose();
			}
			
			// Swap or blt back buffer
			bufferMan.show();
		} while(bufferMan.contentsLost() || bufferMan.contentsRestored());
	}
	
	public void addScene(QScene scene, int id) {
		scenes.put(id, scene);
	}
		
}
