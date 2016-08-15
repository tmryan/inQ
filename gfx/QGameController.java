package ryan.tom.inq.gfx;

import java.awt.BorderLayout;
import java.awt.Color;
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

@SuppressWarnings("serial")
public class QGameController extends JFrame {
	private QScene animTestScene;
	private QGameState gameState;
	private static long lastUpdate;
	private QResourceManager resMan;
	private QMouseManager mouseMan;
	private QKeyboardManager keyMan;
	private QGameSettings gameSettings;
	
	// Debugging states
	private boolean isPathingOverlayEnabled;
	private boolean ispBoundsEnabled;
	
	public QGameController(QResourceManager resMan, QGameSettings gameSettings) {
		mouseMan = new QMouseManager();
		keyMan = new QKeyboardManager();
		this.resMan = resMan;
		gameState = new QGameState(mouseMan, keyMan);
		
		// Note: QGameSettings has static methods for the time being
		this.gameSettings = gameSettings;
		
		// Note: Resource and saved state loading will be abstracted eventually
		// Initializing game components and loading resources
		init();
	}
	
	public void init() {
		setSize(QGameSettings.getWinWidth(), QGameSettings.getWinHeight());
		setTitle("inQ Engine Tester");
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		///////////////
		// Controller
		////////////
		
		// Adding keyboard listener
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keyMan.setKeyState(e.getKeyCode(), true);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				keyMan.setKeyState(e.getKeyCode(), false);
			}
		});
		
		// Adding mouse click controller
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					gameState.resolveMouseClick(e.getX(), e.getY());
				}
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseMan.setCurrentMousePosition(e.getX(), e.getY());
//				gameState.resolveMouseMove(e.getX(), e.getY(), true);
			}
		});

		loadGameState();
		
		// Note: This will change later once graphics and scenes are fully abstracted
		// Adding current scene
		add(animTestScene, BorderLayout.CENTER);

		// Initializing debugging tools
		isPathingOverlayEnabled = false;
		ispBoundsEnabled = false;
		new DebuggerWindow();
		
		// Starting game loop
		startGameLoop();
	}
	
	public void startGameLoop() {
		// Note: Jerkiness as camera pans, need to tighten up the graphics
		int msDelay = 16;
		boolean run = true;
		long currentTime = 0;
		int deltaTime = 0;
		lastUpdate = 0;
		
		/*
		 * Need game tick and art tick?
		 */
		
		while(run) {
			// Note: CPU is over used, throttle loop
			currentTime = System.nanoTime() / 1000000;
			deltaTime = (int) (currentTime - lastUpdate);

			if(deltaTime > msDelay) {
				// debug
				animTestScene.enablePathingOverlay(isPathingOverlayEnabled);
				animTestScene.enableBoundsOverlay(ispBoundsEnabled);
				// update models
				gameState.onTick(deltaTime);
				// update views
				animTestScene.onTick(deltaTime);
				lastUpdate = currentTime;
			}
		}
	}
	
	// Loading resources prior to starting the game
	public void loadGameState() {
		// Lots of data input overhead here! :( 
		// Maybe implement a buildStateFromActor() factory method?

		// Note: creating of animation maps could probably be more elegant!
		// Creating anim maps
		QAnimMap peegAnims = new QAnimMap();
		peegAnims.addAnimation("idle", new QAnimation("idle", QGameConstants.DEF_ANIM_PRIORITY, 
				resMan.getImage("peegPlayer.png")));
		peegAnims.addAnimation("walk", new QAnimation("walk", QGameConstants.DEF_ANIM_PRIORITY, 
				resMan.getImage("peegPlayerWalk1.png"), resMan.getImage("peegPlayerWalk2.png")));
		peegAnims.addAnimation("fall", new QAnimation("fall", QGameConstants.DEF_ANIM_PRIORITY, 
				resMan.getImage("peegPlayerWalk1.png"), resMan.getImage("peegPlayerWalk2.png")));
		peegAnims.addAnimation("jump", new QAnimation("jump", QGameConstants.DEF_ANIM_PRIORITY, 
				resMan.getImage("peegPlayerWalk1.png"), resMan.getImage("peegPlayerWalk2.png")));
		resMan.addAnimMap("peegAnims", peegAnims);
		
		// Creating a pathing map in this mess of data
		QPathingMap testSceneMap = new QPathingMap(resMan.getImage("bg.jpg").getWidth(), resMan.getImage("bg.jpg").getHeight());
		
		// Adding some mans to test
		QActor peegOne = new QActor(450, 600, resMan.getImage("peegPlayer.png"), resMan);
		QPlayerState pOneState = new QPlayerState(450, 600, 
				resMan.getImage("peegPlayer.png").getWidth(), resMan.getImage("peegPlayer.png").getHeight(),
				5, peegOne.getActorId(), testSceneMap, keyMan);
		peegOne.addAnimMap(resMan.getAnimMap("peegAnims"));
		pOneState.addMoverModule(QMoverType.WALK, new QWalkMover());
		pOneState.addMoverModule(QMoverType.FALL, new QFallingMover());
		pOneState.addMoverModule(QMoverType.JUMP, new QSimpleJumpMover());
		peegOne.attachActorState(pOneState);
		
		// Adding camera
		QCameraActor cam = new QCameraActor(0, 0);
		QCameraState camState = new QCameraState(0, 0, 5, cam.getActorId());
		camState.addMoverModule(QMoverType.CAMERA, new QCameraMover(pOneState));
		cam.attachActorState(camState);
		
		// Creating a scene
		animTestScene = new QScene(resMan, mouseMan);
		animTestScene.setBackgroundImg(resMan.getImage("bg.jpg"));
		QSceneState testSceneState = new QSceneState(resMan.getImage("bg.jpg").getWidth(), resMan.getImage("bg.jpg").getHeight(), pOneState,
													 25, 25, 1600, 1600);
		gameState.addSceneState(testSceneState);
		animTestScene.attachSceneState(testSceneState);
		testSceneState.attachPathingMap(testSceneMap);
		// Note: Need a smooth way to initialize and add resources
		animTestScene.addCamera(cam);
		gameState.attachCameraState(camState);
		gameState.loadScene(0);
		animTestScene.addPlayer(peegOne);
		gameState.addPlayerState(pOneState);
		
		// Adding scenery		
		QSceneryActor sun = new QSceneryActor(400, 15, resMan.getImage("sun.png"), resMan);
		QSceneryState sunState = new QSceneryState(400, 15, 
				resMan.getImage("sun.png").getWidth(), resMan.getImage("sun.png").getHeight(),
				4, sun.getActorId());
		sun.attachActorState(sunState);
		animTestScene.addScenery(sun);
		testSceneState.addSceneryState(sunState);
		
		QSceneryActor hills2 = new QSceneryActor(0, 0, resMan.getImage("hills2.png"), resMan);
		QSceneryState hills2State = new QSceneryState(0, 0, 
				resMan.getImage("hills2.png").getWidth(), resMan.getImage("hills2.png").getHeight(),
				1, hills2.getActorId());
		hills2State.addMoverModule(QMoverType.SCENERY, new QSceneryMover());
		hills2.attachActorState(hills2State);
		animTestScene.addScenery(hills2);
		testSceneState.addSceneryState(hills2State);
		
		QSceneryActor hills1 = new QSceneryActor(0, 0, resMan.getImage("hills1.png"), resMan);
		QSceneryState hills1State = new QSceneryState(0, 0, 
				resMan.getImage("hills1.png").getWidth(), resMan.getImage("hills1.png").getHeight(),
				0, hills1.getActorId());
		hills1State.addMoverModule(QMoverType.SCENERY, new QSceneryMover());
		hills1.attachActorState(hills1State);
		animTestScene.addScenery(hills1);
		testSceneState.addSceneryState(hills1State);
		
		QSceneryActor tree1 = new QSceneryActor(10, 0, resMan.getImage("tree1.png"), resMan);
		QSceneryState tree1State = new QSceneryState(10, 0, 
				resMan.getImage("tree1.png").getWidth(), resMan.getImage("tree1.png").getHeight(),
				0, tree1.getActorId());
		tree1.attachActorState(tree1State);
		animTestScene.addScenery(tree1);
		testSceneState.addSceneryState(tree1State);
		
		QSceneryActor tree2 = new QSceneryActor(800, 0, resMan.getImage("tree1.png"), resMan);
		QSceneryState tree2State = new QSceneryState(800, 0, 
				resMan.getImage("tree1.png").getWidth(), resMan.getImage("tree1.png").getHeight(),
				0, tree2.getActorId());
		tree2.attachActorState(tree2State);
		animTestScene.addScenery(tree2);
		testSceneState.addSceneryState(tree2State);
		
		QSceneryActor rocks = new QSceneryActor(500, 400, resMan.getImage("rocks.png"), resMan);
		QSceneryState rocksState = new QSceneryState(500, 400, 
				resMan.getImage("rocks.png").getWidth(), resMan.getImage("rocks.png").getHeight(),
				0, rocks.getActorId());
		rocks.attachActorState(rocksState);
		animTestScene.addScenery(rocks);
		testSceneState.addSceneryState(rocksState);
		
		QSceneryActor rocks2 = new QSceneryActor(1200, 455, resMan.getImage("rocks.png"), resMan);
		QSceneryState rocks2State = new QSceneryState(1200, 455, 
				resMan.getImage("rocks.png").getWidth(), resMan.getImage("rocks.png").getHeight(),
				0, rocks2.getActorId());
		rocks2.attachActorState(rocks2State);
		animTestScene.addScenery(rocks2);
		testSceneState.addSceneryState(rocks2State);
		
		// Testing interactive actors
		QInteractableActor box = new QInteractableActor(750, 600, resMan.getImage("clickyTest.png"), resMan);
		QInteractableState boxState = new QInteractableState(750, 600, 
				resMan.getImage("clickyTest.png").getWidth(), resMan.getImage("clickyTest.png").getHeight(),
				box.getActorId());
		box.attachActorState(boxState);
		animTestScene.addActor(box);
		testSceneState.addActorState(boxState);
		testSceneState.addInteractableState(boxState);
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
