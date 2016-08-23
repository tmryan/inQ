package tryan.inq.controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import tryan.inq.event.QAreaTrigger;
import tryan.inq.event.QGameEvent;
import tryan.inq.event.QTimedEvent;
import tryan.inq.gfx.QActor;
import tryan.inq.gfx.QAnimMap;
import tryan.inq.gfx.QAnimation;
import tryan.inq.gfx.QCameraActor;
import tryan.inq.gfx.QGraphics;
import tryan.inq.gfx.QInteractableActor;
import tryan.inq.gfx.QScene;
import tryan.inq.gfx.QSceneryActor;
import tryan.inq.gfx.QUserInterface;
import tryan.inq.mobility.QCameraMover;
import tryan.inq.mobility.QFallingMover;
import tryan.inq.mobility.QMoverType;
import tryan.inq.mobility.QPathingMap;
import tryan.inq.mobility.QSceneryMover;
import tryan.inq.mobility.QSimpleJumpMover;
import tryan.inq.mobility.QWalkMover;
import tryan.inq.overhead.QGameConstants;
import tryan.inq.overhead.QGameSettings;
import tryan.inq.overhead.QResourceManager;
import tryan.inq.overhead.QStateManager;
import tryan.inq.state.QCameraState;
import tryan.inq.state.QGameState;
import tryan.inq.state.QInteractableState;
import tryan.inq.state.QPlayerState;
import tryan.inq.state.QSceneState;
import tryan.inq.state.QSceneryState;

public class QGameController {
	private QScene animTestScene;
	private QGameState gameState;
	private static long lastUpdate;
	private QResourceManager resMan;
	private QMouseManager mouseMan;
	private QKeyboardManager keyMan;
	private QGameSettings gameSettings;
	private QStateManager stateMan;
	private QGraphics gfx;
	private QUserInterface ui;
		
	// Debugging states here for now
	private boolean isPathingOverlayEnabled;
	private boolean ispBoundsEnabled;
	
	public QGameController(QResourceManager resMan, QGameSettings gameSettings) {
		mouseMan = new QMouseManager();
		keyMan = new QKeyboardManager();
		this.resMan = resMan;
		gameState = new QGameState(mouseMan, keyMan);	
		// Note: This will change when fullscreen and resizable windowed modes are supported
		gfx = new QGraphics(gameState);
		stateMan = new QStateManager(gameState, gfx, resMan, mouseMan);
		
		// Note: QGameSettings has static methods for the time being
		this.gameSettings = gameSettings;
		
		// Initializing listeners, loading game state, and starting game loop
		init();
	}
	
	public void init() {	
		
		///////////////
		// Controller
		///////////
		
		gfx.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keyMan.setKeyState(e.getKeyCode(), true);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				keyMan.setKeyState(e.getKeyCode(), false);
			}
		});
		
		gfx.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					gameState.resolveMouseClick(e.getX(), e.getY());
				}
			}
		});
		
		gfx.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseMan.setCurrentMousePosition(e.getX(), e.getY());
			}
		});
		
		// Note: This will eventually load a specified state
		// Loading given game state
		
		stateMan.setStateXMLPath("data/sceneA1.xml");
		stateMan.loadGameState();
//		stateMan.loadGameStateFromXML();
		// Hardcoding first scene load for now, will eventually load main menu scene initially
		gameState.loadScene(1);
		
		// Initializing debugging tools, moving to QUserInterface soon
		isPathingOverlayEnabled = false;
		ispBoundsEnabled = false;
		new DebuggerWindow();
		
		// Starting game loop
		startGameLoop();
	}
	
	//////////////
	// Game Loop
	//////////
	
	public void startGameLoop() {
		int msDelay = QGameSettings.getDelay();
		boolean run = true;
		long currentTime = 0;
		long deltaTime = 0;
		long secondTime = 0;
		int frames = 0;
		int totalTicks = 0;
		lastUpdate = 0;
			
		/* 
		 * Note: Need game tick and art tick?
		 * 
		 * Note: CPU may be over used, throttle loop?
		 * 
		 * Note: Need to update from game settings here as well
		 * 
		 * Note: Track FPS and add frame counter
		 * 
		 * Note: After some light reading, it sounds like using OpenGL may be the 
		 * 		 best way to improve frame rate
		 */
		
		while(run) {
			// Note: No noticeable difference with currentTime = System.nanoTime() >> 20;
			currentTime = System.nanoTime() / 1000000L;
			deltaTime = (int) (currentTime - lastUpdate);
			secondTime += deltaTime;
			
			if(deltaTime > msDelay) {			
				// Updating model
				gameState.onTick(deltaTime);
				
				// Managing debug flags, temp solution
				gfx.enablePathingOverlay(isPathingOverlayEnabled);
				gfx.enableBoundsOverlay(ispBoundsEnabled);
				
				// Updating view
				gfx.updateView(deltaTime);
				gfx.render();
				frames++;
				
				// Track FPS per 1000 ms
				if(secondTime > 1000) {
					// Note: UI needs an FPS overlay
					// Set current FPS value to frames here
					System.out.println(frames);
					frames = 0;
					secondTime = 0;
				}
				
				// Note: May need to track both gfx update and state updates at some point
				// Tracking update time and game ticks
				lastUpdate = currentTime;
				totalTicks++;
			}
		}
	}
	
	//////////////
	// Debugging
	///////////
	
	// Note: Move debug component drawing to UI class and create QDebugTools class
	class DebuggerWindow {
		public DebuggerWindow() {
			JFrame debugFrame = new JFrame();
			
			JPanel btnPane = new JPanel();
			
			JButton pathingBtn = new JButton("pathing overlay");
			pathingBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					isPathingOverlayEnabled = !isPathingOverlayEnabled;
				}
				
			});
			btnPane.add(pathingBtn);
			
			JButton pBoundsBtn = new JButton("player bounds");
			pBoundsBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ispBoundsEnabled = !ispBoundsEnabled;
				}
			});
			btnPane.add(pBoundsBtn);
			
			debugFrame.add(btnPane);
			debugFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			debugFrame.setTitle("Debug Tools");
			debugFrame.pack();
			debugFrame.setVisible(true);
		}
	}
}
