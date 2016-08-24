package tryan.inq.overhead;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import tryan.inq.controls.QMouseManager;
import tryan.inq.event.QAreaTrigger;
import tryan.inq.event.QGameEvent;
import tryan.inq.event.QTimedEvent;
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
import tryan.inq.mobility.QPathType;
import tryan.inq.mobility.QPathingMap;
import tryan.inq.mobility.QSceneryMover;
import tryan.inq.mobility.QSimpleJumpMover;
import tryan.inq.mobility.QWalkMover;
import tryan.inq.state.QActorState;
import tryan.inq.state.QCameraState;
import tryan.inq.state.QGameState;
import tryan.inq.state.QPlayerState;
import tryan.inq.state.QSceneState;
import tryan.inq.state.QSceneryState;

/**
 * This class manages the loading of game state from disk using the native Java StAX XML parser.
 * 
 * @author Thomas Ryan
 *
 */

public class QStateManager {
	private QGameState gameState;
	private QGraphics gfx;
	private QResourceManager resMan;
	private QMouseManager mouseMan;
	private XMLStreamReader reader;
	private String stateXMLPath;
	
	public QStateManager(QGameState gameState, QGraphics gfx, QResourceManager resMan, QMouseManager mouseMan) {
		this.gameState = gameState;
		this.gfx = gfx;
		this.resMan = resMan;
		this.mouseMan = mouseMan;
		reader = null;
		stateXMLPath = "";		
		// Incomplete
	}
	
	// Note: Temporary hardcoded state loading
	public void loadGameState() {
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
		
		// Temporary hardcoded pathing map generation
		for(int i = 0; i < 27; i++) {
			testSceneMap.setPathingCellType(18, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(19, i, QPathType.NOPATH);
		}

		for(int i = 0; i < 29; i++) {
			testSceneMap.setPathingCellType(20, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(21, i, QPathType.NOPATH);
		}
		
		for(int i = 0; i < 34; i++) {
			testSceneMap.setPathingCellType(22, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(23, i, QPathType.NOPATH);
		}
		
		for(int i = 0; i < 37; i++) {
			testSceneMap.setPathingCellType(24, i, QPathType.NOPATH);
		}
		
		for(int i = 0; i < 40; i++) {
			testSceneMap.setPathingCellType(25, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(26, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(27, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(28, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(29, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(30, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(31, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(32, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(33, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(34, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(35, i, QPathType.NOPATH);
		}
	
		for(int i = 87; i < testSceneMap.getColCount(); i++) {
			testSceneMap.setPathingCellType(29, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(30, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(31, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(32, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(33, i, QPathType.NOPATH);
		}

		for(int i = 84; i < testSceneMap.getColCount(); i++) {
			testSceneMap.setPathingCellType(34, i, QPathType.NOPATH);
		}
		
		for(int i = 0; i < 50; i++) {
			testSceneMap.setPathingCellType(36, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(37, i, QPathType.NOPATH);
		}
		
		for(int i = 82; i < testSceneMap.getColCount(); i++) {
			testSceneMap.setPathingCellType(35, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(36, i, QPathType.NOPATH);
		}
		
		for(int i = 81; i < testSceneMap.getColCount(); i++) {
			testSceneMap.setPathingCellType(37, i, QPathType.NOPATH);
		}
		
		for(int i = 0; i < 53; i++) {
			testSceneMap.setPathingCellType(38, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(39, i, QPathType.NOPATH);
		}
				
		for(int i = 80; i < testSceneMap.getColCount(); i++) {
			testSceneMap.setPathingCellType(38, i, QPathType.NOPATH);
		}
		
		for(int i = 68; i < testSceneMap.getColCount(); i++) {
			testSceneMap.setPathingCellType(39, i, QPathType.NOPATH);
		}
		
		for(int i = 0; i < 55; i++) {
			testSceneMap.setPathingCellType(40, i, QPathType.NOPATH);
		}
		
		for(int i = 64; i < testSceneMap.getColCount(); i++) {
			testSceneMap.setPathingCellType(40, i, QPathType.NOPATH);
		}
		
		for(int i = 0; i < testSceneMap.getColCount(); i++) {
			testSceneMap.setPathingCellType(41, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(42, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(43, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(44, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(45, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(46, i, QPathType.NOPATH);
			testSceneMap.setPathingCellType(47, i, QPathType.NOPATH);
		}
		
		// Adding some mans to test
		QActor peegOne = new QActor(resMan.getImage("peegPlayer.png"), resMan, 10);
		QPlayerState pOneState = new QPlayerState(25, 345, 
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
													 10, 10, 2400, 1235);
		gameState.addSceneState(testSceneState);
		animTestScene.attachSceneState(testSceneState);
		testSceneState.attachPathingMap(testSceneMap);
		
		gameState.attachCameraState(camState);
		gameState.addPlayerState(pOneState);

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
		
		// Adding a test area trigger
		QAreaTrigger trigger = new QAreaTrigger(1012, 677, 166, 200, true);
		trigger.addGameEvent(new QGameEvent(1) {
			@Override
			public void playEvent() {
				System.out.println("A hidden cave! Pigs love hidden caves!");
			}
		});
		testSceneState.addAreaTrigger(trigger);
		
		//Note: Only works with one event right now...
//		QAreaTrigger trigger2 = new QAreaTrigger(1816, 844, 100, 100, true);
//		trigger.addGameEvent(new QGameEvent(1) {
//			@Override
//			public void playEvent() {
//				System.out.println("Another cave! Something smells good here. You know, for a pig...");
//			}
//		});
//		testSceneState.addAreaTrigger(trigger2);
		
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
	
	public void loadGameStateFromXML() {
		// Creating StAX-based reader
		FileInputStream inStream = null;
		
		try {
			inStream = new FileInputStream(stateXMLPath);
			XMLInputFactory factory = XMLInputFactory.newInstance();
			reader = factory.createXMLStreamReader(inStream);
			
			reader.next();
			
			// Read first tag name to determine what data is stored within
			String fileType = reader.getName().toString();

			/* 
			 * Note: This could be changed to hashmap instead of switch and
			 * 		 custom xml reader modules could be attached to this class
			 */
			// Call customized loading functions for each tag type
			switch (fileType) {
				case "game":
					// Note: gameState will be loaded here later on
					break;
				case "scene":
					loadSceneFromXML();
					break;
				default:
					break;
			}
				
			inStream.close();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void setStateXMLPath(String filepath) {
		stateXMLPath = filepath;
	}
	
	public void loadSceneFromXML() {
		class SceneData {
			String bgimg;
			int width, height, minCamX, minCamY, maxCamX, maxCamY;
			ArrayList<String> actors;
			ArrayList<QAreaTrigger> triggers;
			ArrayList<QTimedEvent> timers;
			
			public SceneData() {
				bgimg = null;
				width = 0; height = 0; minCamX = 0; minCamY = 0; maxCamX = 0; maxCamY = 0;
				actors = new ArrayList<String>();
				triggers = new ArrayList<QAreaTrigger>();
				timers = new ArrayList<QTimedEvent>();
			}
		}

		String data = "";
		String endTag = "";
		QScene scene = null;
		QSceneState sceneState = null;
		QPathingMap pathingMap = null;
		SceneData sData = new SceneData();

		try {
			while(reader.hasNext()) {
				if(reader.isStartElement()) {
					data = reader.getName().toString();
					System.out.println(data);
					reader.next();
					
					switch (data) {
						case "actor":
							sData.actors.add(getText());
							break;
						case "trigger":
							sData.triggers.add(createAreaTriggerFromXML());
							break;
						case "timed":
							sData.timers.add(createTimedEventFromXML());
							break;
						case "bgimg":
							sData.bgimg = getText();
							break;
						case "width":
							sData.width = new Integer(getText());
							break;
						case "height":
							sData.height = new Integer(getText());
							break;
						case "minCamX":
							sData.minCamX = new Integer(getText());
							break;
						case "minCamY":
							sData.minCamY = new Integer(getText());
							break;
						case "maxCamX":
							sData.maxCamX = new Integer(getText());
							break;
						case "maxCamY":
							sData.maxCamY = new Integer(getText());
							break;
						case "pathingmap":
							pathingMap = createPathingMapFromXML();
							break;
						default:
							break;
					}
				}
				
				reader.next();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		// Creating scene and scene state
		scene = new QScene(resMan, resMan.getImage(sData.bgimg));
		sceneState = new QSceneState(sData.width, sData.height, gameState.getPlayerState(),
				sData.minCamX, sData.minCamY, sData.maxCamX, sData.maxCamY);
		
		for(String actor : sData.actors) {
			scene.addActor(createActorFromXML(actor));
			sceneState.addActorState(createActorStateFromXML(actor));
		}
		
		for(QAreaTrigger trigger : sData.triggers) {
			sceneState.addAreaTrigger(trigger);
		}
		
		for(QTimedEvent timer : sData.timers) {
			sceneState.addTimedEvent(timer);
		}
		
		sceneState.attachPathingMap(pathingMap);
		scene.attachSceneState(sceneState);
		gameState.addSceneState(sceneState);
	}
	
	public QActor createActorFromXML(String filepath) {
		// need to determine actor type and switch to proper object creation
		
		
		return null;
	}
	
	public QActorState createActorStateFromXML(String filepath) {
		// need to determine actor type and switch to proper object creation
		
		
		
		
		return null;
	}
	
	public QAreaTrigger createAreaTriggerFromXML() {
		class TriggerData {
			int x, y, width, height;
			boolean oneShot;
			ArrayList<String> events;
		}
		
		String data = "";
		String endTag = "";
		QAreaTrigger trigger = null;
		TriggerData tData = new TriggerData();
		
		try {
			while(reader.hasNext() && !endTag.equals("events")) {
				if(reader.isStartElement()) {
					data = reader.getName().toString();
					System.out.println(data);
					reader.next();
					
					if(!data.equals("")) {
						switch (data) {	
							case "x":
								tData.x = new Integer(getText());
								break;
							case "y":
								tData.y = new Integer(getText());
								break;
							case "width":
								tData.width = new Integer(getText());
								break;
							case "height":
								tData.height = new Integer(getText());
								break;
							case "oneShot":
								tData.oneShot = (getText().equals("true")) ? true : false;
								break;
							case "event":
								tData.events.add(getText());
								break;
							default:
								break;
						}
					}
				
					reader.next();
				} else if(reader.isEndElement()) {
					endTag = reader.getName().toString();
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		trigger = new QAreaTrigger(tData.x, tData.y, tData.width, tData.height, tData.oneShot);
		
		for(String event : tData.events) {
			trigger.addGameEvent(createGameEventFromXML(event));
		}
		
		return trigger;
	}
	
	public QTimedEvent createTimedEventFromXML() {
		
		
		
		return null;
	}
	
	private QGameEvent createGameEventFromXML(String filepath) {
		
		
		return null;
	}
	
	private QPathingMap createPathingMapFromXML() {
		QPathingMap pathingMap = new QPathingMap(1, 1);
		
		return pathingMap;
	}
	
	/**
	 * Returns data contained at the current cursor location.
	 * The method assumes that the current cursor is pointing to an attribute value and not a tag name.
	 * 
	 * @return		A String representation of the text contained at the current cursor location
	 * 
	 */
	private String getText() {
		String data = "";
		
		if(!reader.isStartElement() && !reader.isEndElement()) {
			data = reader.getText();
		}
		
		return data;
	}
	
}
