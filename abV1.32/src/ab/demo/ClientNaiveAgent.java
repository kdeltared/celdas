/*****************************************************************************
 ** ANGRYBIRDS AI AGENT FRAMEWORK
 ** Copyright (c) 2014, XiaoYu (Gary) Ge, Stephen Gould, Jochen Renz
 **  Sahan Abeyasinghe,Jim Keys,  Andrew Wang, Peng Zhang
 ** All rights reserved.
**This work is licensed under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
**To view a copy of this license, visit http://www.gnu.org/licenses/
 *****************************************************************************/
package ab.demo;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ab.demo.other.ClientActionRobot;
import ab.demo.other.ClientActionRobotJava;
import ab.planner.TrajectoryPlanner;
import ab.vision.ABObject;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.Vision;
//Naive agent (server/client version)

public class ClientNaiveAgent implements Runnable {


	//Wrapper of the communicating messages
	private ClientActionRobotJava ar;
	public byte currentLevel = -1;
	public int failedCounter = 0;
	public int[] solved;
	TrajectoryPlanner tp; 
	private int id = 28888;
	private boolean firstShot;
	private Point prevTarget;
	private Random randomGenerator;
	private Solver solver;
	/**
	 * Constructor using the default IP
	 * */
	public ClientNaiveAgent() {
		// the default ip is the localhost
		ar = new ClientActionRobotJava("127.0.0.1");
		tp = new TrajectoryPlanner();
		randomGenerator = new Random();
		prevTarget = null;
		firstShot = true;

	}
	/**
	 * Constructor with a specified IP
	 * */
	public ClientNaiveAgent(String ip) {
		ar = new ClientActionRobotJava(ip);
		tp = new TrajectoryPlanner();
		randomGenerator = new Random();
		prevTarget = null;
		firstShot = true;

	}
	public ClientNaiveAgent(String ip, int id)
	{
		ar = new ClientActionRobotJava(ip);
		tp = new TrajectoryPlanner();
		randomGenerator = new Random();
		prevTarget = null;
		firstShot = true;
		this.id = id;
	}
	public int getNextLevel()
	{
		int level = 0;
		boolean unsolved = false;
		//all the level have been solved, then get the first unsolved level
		for (int i = 0; i < solved.length; i++)
		{
			if(solved[i] == 0 )
			{
					unsolved = true;
					level = i + 1;
					if(level <= currentLevel && currentLevel < solved.length)
						continue;
					else
						return level;
			}
		}
		if(unsolved)
			return level;
	    level = (currentLevel + 1)%solved.length;
		if(level == 0)
			level = solved.length;
		return level; 
	}
    /* 
     * Run the Client (Naive Agent)
     */
	private void checkMyScore()
	{
		
		int[] scores = ar.checkMyScore();
		System.out.println(" My score: ");
		int level = 1;
		for(int i: scores)
		{
			System.out.println(" level " + level + "  " + i);
			if (i > 0)
				solved[level - 1] = 1;
			level ++;
		}
	}
	public void run() {	
		byte[] info = ar.configure(ClientActionRobot.intToByteArray(id));
		solved = new int[info[2]];
		
		//load the initial level (default 1)
		//Check my score
		checkMyScore();
		
		currentLevel = (byte)getNextLevel(); 
		ar.loadLevel(currentLevel);
		//ar.loadLevel((byte)9);
		GameState state;
		this.solver = new Solver();
		
		while (true) {	
            System.out.println("**** Entrando a solve");
			state = solve();
			System.out.println("**** Solve resuleto con estado: "+state);
			//If the level is solved , go to the next level
			if (state == GameState.WON) {
							
				///System.out.println(" loading the level " + (currentLevel + 1) );
				checkMyScore();
				System.out.println();
				currentLevel = (byte)getNextLevel(); 
				ar.loadLevel(currentLevel);
				//ar.loadLevel((byte)9);
				//display the global best scores
				int[] scores = ar.checkScore();
				System.out.println("Global best score: ");
				for (int i = 0; i < scores.length ; i ++)
				{
				
					System.out.print( " level " + (i+1) + ": " + scores[i]);
				}
				System.out.println();
				
				// make a new trajectory planner whenever a new level is entered
				tp = new TrajectoryPlanner();

				// first shot on this level, try high shot first
				firstShot = true;
				
			} else 
				//If lost, then restart the level
				if (state == GameState.LOST) {
					failedCounter++;
					if(failedCounter > 3) {
						failedCounter = 0;
						currentLevel = (byte)getNextLevel(); 
						ar.loadLevel(currentLevel);
					
					//ar.loadLevel((byte)9);
					} else {		
						System.out.println("restart");
						ar.restartLevel();
					}
						
				} else if (state == GameState.LEVEL_SELECTION) {
					System.out.println("unexpected level selection page, go to the last current level : "
								+ currentLevel);
					ar.loadLevel(currentLevel);
				} else if (state == GameState.MAIN_MENU) {
					System.out
						.println("unexpected main menu page, reload the level : "
								+ currentLevel);
					ar.loadLevel(currentLevel);
				} else if (state == GameState.EPISODE_MENU) {
					System.out.println("unexpected episode menu page, reload the level: "
									+ currentLevel);
					ar.loadLevel(currentLevel);
				}

			}

		}


	  /** 
	   * Solve a particular level by shooting birds directly to pigs
	   * @return GameState: the game state after shots.
     */
	public GameState solve()
	{

		// capture Image
		BufferedImage screenshot = ar.doScreenShot();

		// process image
		Vision vision = new Vision(screenshot);
		
		Rectangle sling = vision.findSlingshotMBR();

		//If the level is loaded (in PLAYING　state)but no slingshot detected, then the agent will request to fully zoom out.
		while (sling == null && ar.checkState() == GameState.PLAYING) {
            
			System.out.println("no slingshot detected. Please remove pop up or zoom out, estado :" + ar.checkState());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			ar.fullyZoomOut();
			screenshot = ar.doScreenShot();
			vision = new Vision(screenshot);
			sling = vision.findSlingshotMBR();
		}

		
		 // get all the pigs
 		List<ABObject> pigs = vision.findPigsMBR();

        //get all the blocks
		List<ABObject> blocks = vision.findBlocksMBR();
        
		GameState state = ar.checkState();
		// if there is a sling, then play, otherwise skip.
		if (sling != null) {
			
			//If there are pigs, we pick up a pig randomly and shoot it. 
			if (!pigs.isEmpty()) {						
				Point releasePoint = null;
	//-------------------------------------------------------------------------//
				System.out.println("** Calculando accion");
                int cantidadInicialChanchos = pigs.size();
				Sensor sensor = new Sensor(pigs, ar.getBirdTypeOnSling(),blocks);
                List<Estado> estados = sensor.getEstados();
                Teoria teoria_bis = solver.getTeoriaParaAplicar(estados);
				                
                ABObject pig = teoria_bis.getPig();
                int accion = teoria_bis.getAccion();
                System.out.println("--Datos de la teoria--");
                System.out.println("Accion elejida: " + accion);
                System.out.println("Chancho elejido: " + pig);
                System.out.println("Usos: " + teoria_bis.getUsos());
                System.out.println("Puntaje: " + teoria_bis.getPuntaje());
                System.out.println("Birdtype: " + teoria_bis.getEstado().birdType);
                System.out.println("maderaIzqNormalizada: " + teoria_bis.getEstado().maderaIzqNormalizada);
                System.out.println("maderaArribaNormalizada: " + teoria_bis.getEstado().maderaArribaNormalizada);
                System.out.println("hieloIzqNormalizada: " + teoria_bis.getEstado().hieloIzqNormalizada);
                System.out.println("hieloArribaNormalizada: " + teoria_bis.getEstado().hieloArribaNormalizada);
                System.out.println("piedraIzqNormalizada: " + teoria_bis.getEstado().piedraIzqNormalizada);
                System.out.println("piedraArribaNormalizada: " + teoria_bis.getEstado().piedraArribaNormalizada);
                System.out.println("--Fin Datos de la teoria--");

				//Comenzar a apuntar con el chancho elegido
				Point _tpt = pig.getCenter();
	//------------------------------------------------------------------------------//
					
					// if the target is very close to before, randomly choose a
					// point near it
					if (prevTarget != null && distance(prevTarget, _tpt) < 10) {
						double _angle = randomGenerator.nextDouble() * Math.PI * 2;
						_tpt.x = _tpt.x + (int) (Math.cos(_angle) * 10);
						_tpt.y = _tpt.y + (int) (Math.sin(_angle) * 10);
						System.out.println("Randomly changing to " + _tpt);
					}

					prevTarget = new Point(_tpt.x, _tpt.y);

					// estimate the trajectory
					ArrayList<Point> pts = tp.estimateLaunchPoint(sling, _tpt);

					// seleccionar si el tiro es parabolico o no
					if (pts.size() > 1) {
						releasePoint = pts.get(accion);
					} else{
                        releasePoint = pts.get(0);
                    }
                    
					Point refPoint = tp.getReferencePoint(sling);

					// Get the release point from the trajectory prediction module
					int tapTime = 0;
					if (releasePoint != null) {
						double releaseAngle = tp.getReleaseAngle(sling,
								releasePoint);
						System.out.println("Release Point: " + releasePoint);
						System.out.println("Release Angle: "
								+ Math.toDegrees(releaseAngle));
						int tapInterval = 0;
						switch (ar.getBirdTypeOnSling()) 
						{

							case RedBird:
								tapInterval = 0; break;               // start of trajectory
							case YellowBird:
								tapInterval = 75 + randomGenerator.nextInt(15);break; // 65-90% of the way
							case WhiteBird:
								tapInterval =  50 + randomGenerator.nextInt(20);break; // 50-70% of the way
							case BlackBird:
								tapInterval = 70 + randomGenerator.nextInt(20);break; // 70-90% of the way
							case BlueBird:
								tapInterval =  65 + randomGenerator.nextInt(20);break; // 65-85% of the way
							default:
								tapInterval =  60;
						}
						
						tapTime = tp.getTapTime(sling, releasePoint, _tpt, tapInterval);
						
					} else
						{
							System.err.println("No Release Point Found");
							return ar.checkState();
						}
				
				
					// check whether the slingshot is changed. the change of the slingshot indicates a change in the scale.
					ar.fullyZoomOut();
					screenshot = ar.doScreenShot();
					vision = new Vision(screenshot);
					Rectangle _sling = vision.findSlingshotMBR();
					if(_sling != null)
					{
						double scale_diff = Math.pow((sling.width - _sling.width),2) +  Math.pow((sling.height - _sling.height),2);
						if(scale_diff < 25)
						{
							int dx = (int) releasePoint.getX() - refPoint.x;
							int dy = (int) releasePoint.getY() - refPoint.y;
							if(dx < 0)
							{
								long timer = System.currentTimeMillis();
								ar.shoot(refPoint.x, refPoint.y, dx, dy, 0, tapTime, false);
								System.out.println("It takes " + (System.currentTimeMillis() - timer) + " ms to take a shot");
								state = ar.checkState();
								if ( state == GameState.PLAYING )
								{
									screenshot = ar.doScreenShot();
									vision = new Vision(screenshot);
									List<Point> traj = vision.findTrajPoints();
									tp.adjustTrajectory(traj, sling, releasePoint);
									firstShot = false;
								}
							}
						//-------------------------------------------------------------------------//
                        //evaluar puntaje obtenido
                        //grabar teoria
                        System.out.println("** Guardando resultados");
                        ar.fullyZoomOut();
                        screenshot = ar.doScreenShot();

                        // process image
                        vision = new Vision(screenshot);
                        pigs = vision.findPigsMBR();
                        int cantidadFinalChanchos = pigs.size();
                        int cantidadChanchosMatados = cantidadInicialChanchos - cantidadFinalChanchos;
                        if( cantidadChanchosMatados < 0 ){
                            cantidadChanchosMatados = 0;
                        }
                        System.out.println("Chanchos matados: " + cantidadChanchosMatados);
                        teoria_bis.setUsos(teoria_bis.getUsos()+1);
                        teoria_bis.setPuntaje(teoria_bis.getPuntaje()+cantidadChanchosMatados);
                        solver.grabar();	
						
                        //-------------------------------------------------------------------------//
                        
						}
						else
							System.out.println("Scale is changed, can not execute the shot, will re-segement the image");
					}
					else
						System.out.println("no sling detected, can not execute the shot, will re-segement the image");
				
			}
		}
		//Si era una teoria Naive Calculo los chanchos que mato
        state = ar.checkState();
		return state;
	}

	private double distance(Point p1, Point p2) {
		return Math.sqrt((double) ((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)* (p1.y - p2.y)));
	}

	public static void main(String args[]) {

		ClientNaiveAgent na;
		if(args.length > 0)
			na = new ClientNaiveAgent(args[0]);
		else
			na = new ClientNaiveAgent();
		na.run();
	}
}
