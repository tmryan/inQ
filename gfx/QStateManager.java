package ryan.tom.inq.gfx;

/**
 * This class manages all loading and saving of the game state.
 * 
 * @author Thomas Ryan
 *
 */

// Note: Implementation is incomplete
public class QStateManager {
	
	public QStateManager() {
		
	}
	
	// Note: This will eventually load a specified state
	public void loadGameState(QResourceManager resMan) {
		/*
		 * Note: Need a better way to initialize and load game states
		 * 		 Maybe implement a createStateFromActor() factory method?
		 * 		 Can load player chosen state as well, new or saved state
		 * 		 Abstract to QStateManager
		 */

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
	
}
