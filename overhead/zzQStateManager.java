package tryan.inq.overhead;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import tryan.inq.controls.QMouseManager;
import tryan.inq.gfx.QActor;
import tryan.inq.gfx.QAnimMap;
import tryan.inq.gfx.QAnimation;
import tryan.inq.gfx.QCameraActor;
import tryan.inq.gfx.QGraphics;
import tryan.inq.gfx.QScene;
import tryan.inq.gfx.QSceneryActor;
import tryan.inq.mobility.QCameraMover;
import tryan.inq.mobility.QFallingMover;
import tryan.inq.mobility.QMoverType;
import tryan.inq.mobility.QPathingMap;
import tryan.inq.mobility.QSceneryMover;
import tryan.inq.mobility.QSimpleJumpMover;
import tryan.inq.mobility.QWalkMover;
import tryan.inq.state.QCameraState;
import tryan.inq.state.QGameState;
import tryan.inq.state.QPlayerState;
import tryan.inq.state.QSceneState;
import tryan.inq.state.QSceneryState;

/**
 * This class manages all loading and saving of the game state.
 * 
 * @author Thomas Ryan
 *
 */

public class QStateManager {
	private QGameState gameState;
	private QGraphics gfx;
	private QResourceManager resMan;
	private XMLStreamReader reader;
	private String stateXMLPath;
	
	public QStateManager(QGameState gameState, QGraphics gfx, QResourceManager resMan) {
		this.gameState = gameState;
		this.gfx = gfx;
		this.resMan = resMan;
		reader = null;
		stateXMLPath = "";		
		// Incomplete
	}
	
	// Note: Temporary hardcoded state loading
	public void loadGameStateFromXML(QResourceManager resMan, QMouseManager mouseMan, QGraphics gfx) {
		// Creating StAX reader
		FileInputStream inStream = null;
		
		try {
			inStream = new FileInputStream(stateXMLPath);
			XMLInputFactory factory = XMLInputFactory.newInstance();
			reader = factory.createXMLStreamReader(inStream);
				
			while(reader.hasNext()) {
				int parseEvent = reader.next();
				handleParserEvent(parseEvent);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(XMLStreamException e) {
			e.printStackTrace();
		}
		
		
		/*
		 * Note: Need a better way to initialize and load game states
		 * 		 Maybe implement a createStateFromActor() factory method?
		 * 		 Can load player chosen state as well, new or saved state
		 * 
		 * Note: Creation of animation maps can be improved
		 * 
		 * Note: Adding an idle frame at the end of animation is currently only way to avoid walking in place
		 */
		
		// Creating anim maps
		QAnimMap peegAnims = new QAnimMap();
		peegAnims.addAnimation("idle", new QAnimation("idle", QGameConstants.DEF_ANIM_PRIORITY, 
				new QAnimation.AnimFrame(resMan.getImage("peegPlayer.png"), 1)));
		peegAnims.addAnimation("walk", new QAnimation("walk", QGameConstants.DEF_ANIM_PRIORITY, 
				new QAnimation.AnimFrame(resMan.getImage("peegPlayerWalk1.png"), 100), new QAnimation.AnimFrame(resMan.getImage("peegPlayerWalk2.png"), 100),
				new QAnimation.AnimFrame(resMan.getImage("peegPlayer.png"), 1)));
		peegAnims.addAnimation("fall", new QAnimation("fall", QGameConstants.DEF_ANIM_PRIORITY, 
				new QAnimation.AnimFrame(resMan.getImage("peegPlayerWalk1.png"), 100), new QAnimation.AnimFrame(resMan.getImage("peegPlayerWalk2.png"), 100),
				new QAnimation.AnimFrame(resMan.getImage("peegPlayer.png"), 1)));
		peegAnims.addAnimation("jump", new QAnimation("jump", QGameConstants.DEF_ANIM_PRIORITY, 
				new QAnimation.AnimFrame(resMan.getImage("peegPlayerWalk1.png"), 100), new QAnimation.AnimFrame(resMan.getImage("peegPlayerWalk2.png"), 100),
				new QAnimation.AnimFrame(resMan.getImage("peegPlayer.png"), 1)));
		resMan.addAnimMap("peegAnims", peegAnims);
		
		// Creating a pathing map in this mess of data
		QPathingMap testSceneMap = new QPathingMap(resMan.getImage("bg1.jpg").getWidth(), resMan.getImage("bg1.jpg").getHeight());
		
		// Adding some mans to test
		QActor peegOne = new QActor(resMan.getImage("peegPlayer.png"), resMan, 10);
		QPlayerState pOneState = new QPlayerState(25, 400, 
				resMan.getImage("peegPlayer.png").getWidth(), resMan.getImage("peegPlayer.png").getHeight(),
				5, peegOne.getActorId(), testSceneMap);
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
		QScene animTestScene = new QScene(resMan, resMan.getImage("bg1.jpg"));
		QSceneState testSceneState = new QSceneState(resMan.getImage("bg1.jpg").getWidth(), resMan.getImage("bg1.jpg").getHeight(), pOneState,
													 25, 25, 2400, 1600);
		gameState.addSceneState(testSceneState);
		animTestScene.attachSceneState(testSceneState);
		testSceneState.attachPathingMap(testSceneMap);
		
		gameState.attachCameraState(camState);
		gameState.addPlayerState(pOneState);
		// Hardcoding first scene load for now, will eventually load main menu scene initially
		gameState.loadScene(1);

		animTestScene.addPlayer(peegOne);
		animTestScene.addCamera(cam);
		
		gfx.addScene(animTestScene, testSceneState.getSceneId());
		
		// Adding scenery		
		QSceneryActor sun = new QSceneryActor(resMan.getImage("sun.png"), resMan, 1);
		QSceneryState sunState = new QSceneryState(1000, 15, 
				resMan.getImage("sun.png").getWidth(), resMan.getImage("sun.png").getHeight(),
				4, sun.getActorId());
		sun.attachActorState(sunState);
		animTestScene.addScenery(sun);
		testSceneState.addSceneryState(sunState);
		
		QSceneryActor bg2 = new QSceneryActor(resMan.getImage("bg2.png"), resMan, 2);
		QSceneryState bg2State = new QSceneryState(0, 0, 
				resMan.getImage("bg2.png").getWidth(), resMan.getImage("bg2.png").getHeight(),
				1, bg2.getActorId());
		bg2State.addMoverModule(QMoverType.SCENERY, new QSceneryMover());
		bg2.attachActorState(bg2State);
		animTestScene.addScenery(bg2);
		testSceneState.addSceneryState(bg2State);
		
		QSceneryActor bg3 = new QSceneryActor(resMan.getImage("bg3.png"), resMan, 2);
		QSceneryState bg3State = new QSceneryState(0, 0, 
				resMan.getImage("bg3.png").getWidth(), resMan.getImage("bg3.png").getHeight(),
				2, bg3.getActorId());
		bg3State.addMoverModule(QMoverType.SCENERY, new QSceneryMover());
		bg3.attachActorState(bg3State);
		animTestScene.addScenery(bg3);
		testSceneState.addSceneryState(bg3State);
		
		QSceneryActor playableArea = new QSceneryActor(resMan.getImage("playableArea.png"), resMan, 3);
		QSceneryState playableAreaState = new QSceneryState(0, 0, 
				resMan.getImage("playableArea.png").getWidth(), resMan.getImage("playableArea.png").getHeight(),
				0, playableArea.getActorId());
		playableArea.attachActorState(playableAreaState);
		animTestScene.addScenery(playableArea);
		testSceneState.addSceneryState(playableAreaState);
		
//		// Adding a test area trigger
//		QAreaTrigger trigger = new QAreaTrigger(600, 595, 100, 100, true);
//		trigger.addGameEvent(new QGameEvent(1) {
//			@Override
//			public void playEvent() {
//				System.out.println("It's a trigger! :V");
//			}
//		});
//		testSceneState.addAreaTrigger(trigger);
//		
//		// Adding a test event timer
//		QTimedEvent timedEvent = new QTimedEvent(5000, true);
//		timedEvent.addGameEvent(new QGameEvent(1) {
//			@Override
//			public void playEvent() {
//				System.out.println("Time is up!");
//			}
//		});
//		testSceneState.addTimedEvent(timedEvent);
	}
	
	public void setStateXMLPath(String filepath) {
		stateXMLPath = filepath;
	}
	
	private void handleParserEvent(int parseEvent) {
		if(reader.isStartElement()) {
			String tagName = reader.getName().toString();
			System.out.println(tagName);
			
			switch (tagName) {
				case "game":
					// Note: gameState will be loaded here later on
					break;
				case "scene":
					loadSceneFromXML();
					break;
				case "actor":
					loadActorFromXML();
					break;
				default:
					break;
			}
		}
	}
	
	public void loadSceneFromXML() {
		String endTag = "";
		String data = "";
		int parseEvent = 0;
		QScene scene = null;
		QSceneState sceneState = null;
		
		while(!endTag.equals("scene")) {
			try {
				parseEvent = reader.next();
				
				if(reader.isStartElement()) {
					data = reader.getName().toString();
					
					switch (data) {
						case "bgimg":
							parseEvent = reader.next();
							
							if(!reader.isEndElement() && parseEvent != XMLStreamReader.END_DOCUMENT) {
								scene = new QScene(resMan, resMan.getImage(reader.getText()));
							}
							
							break;
						case "dims":
							int width = 0, height = 0, minCamX = 0, minCamY = 0, maxCamX = 0, maxCamY = 0;
							
							while(!reader.isEndElement() && reader.getName().toString().equals("dims")) {
								parseEvent = reader.next();
								
								if(!reader.isEndElement() && parseEvent != XMLStreamReader.END_DOCUMENT) {
									data = reader.getText();
									
									switch(data) {
										case "width":
											
											break;
										case "height":
											height = new Integer(data);
											break;
										case "minCamX":
											minCamX = new Integer(data);
											break;
										case "minCamY":
											minCamY = new Integer(data);
											break;
										case "maxCamX":
											maxCamX = new Integer(data);
											break;
										case "maxCamY":
											maxCamY = new Integer(data);
											break;
										default:
											break;
									}
								}
							}
														
							sceneState = new QSceneState(width, height, gameState.getPlayerState(), minCamX, minCamY, maxCamX, maxCamY);
							
							break;
						default:
							break;
					}
				} else if(reader.isEndElement()) {
					endTag = reader.getName().toString();
				}
			} catch(XMLStreamException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadActorFromXML() {
		// Load actor and actor state here
	}
	
	private QPathingMap createPathingMap() {
		QPathingMap pathingMap = new QPathingMap(1, 1);
		
		return pathingMap;
	}
	
	// Note: Implement methods for load from file, save to file, etc
	
}
