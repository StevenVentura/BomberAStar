//package aStar;

/*import aStar.heuristics.AStarHeuristic;
import aStar.heuristics.ClosestHeuristic;
import aStar.heuristics.DiagonalHeuristic;
import aStar.utils.Logger;
import aStar.utils.StopWatch;*/
/*

"svantetobias" java
http://code.google.com/p/a-star-java/source/list?path=/AStar/src/aStar/AreaMap.java&start=8



*/
import java.util.Scanner;
import java.io.File;

public class TestAStar {
        
        private static int mapWith = 320;
        private static int mapHeight = 240;
        
        /*private static int[][] obstacleMap =   {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0},
                                                                                        {0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0},
                                                                                        {0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0},
                                                                                        {0,0,0,0,1,1,0,0,1,0,0,0,0,0,0,1,1,0,0,0},
                                                                                        {0,1,1,1,1,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0},
                                                                                        {0,0,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,0,0,0},
                                                                                        {1,1,1,1,1,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0},
                                                                                        {0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0},
                                                                                        {0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0},
                                                                                        {0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0},
                                                                                        {0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0},
                                                                                        {0,0,0,0,0,0,0,1,1,0,0,0,1,1,1,0,0,0,0,0},
                                                                                        {0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0},
                                                                                        {0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
                                                                                        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                                                                                        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                                                                                        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                                                                                        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                                                                                        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                                                                                        {0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1}};*/
																						
		private int[][] obstacleMap() // stevencode!
		{
		try{
		Scanner scan = new Scanner(new File("input\\level3.txt"));
		int numRows = scan.nextInt(); scan.nextLine();
		int numCols = scan.nextInt(); scan.nextLine();
		
		int[][] out = new int[numRows][numCols];
		
		for (int r = 0; r < numRows; r++)
		{
		String line = scan.nextLine();
		
		for (int c = 0; c < numCols; c++)
		{
		int x = Integer.parseInt(line.substring(c,c+1));
		if (x == 5)
		x = 0;
		if (x == 2)
		{
		// for some reason or another he swapped the R and C....
		startX = r; startY = c;
		x = 0;
		}
		if (x == 3)
		{
		//System.out.println("rrrrrrrrr = " + r);
		//goalX = c; goalY = r;
		goalX = r; goalY = c;
		// for some reason or another he swapped the R and C....
		x = 0;
		}
		
		out[r][c] = x;
		
		}
		}
		return out;
		
		
		}catch(Exception e){e.printStackTrace();}; return null;}
        
        /*private static int startX = 0;
        private static int startY = 1;
        private static int goalX = 319;
        private static int goalY = 239;*/
		
		public int startX=-1, startY=-1, goalX=-1, goalY=-1;
        
		public TestAStar()
		{
		
		}
        
        /*public static void main(String[] args) {
                Logger log = new Logger();
                StopWatch s = new StopWatch();
				
				TestAStar t = new TestAStar(); //stevencode!
                
                log.addToLog("Map initializing...");
                //AreaMap map = new AreaMap(mapWith, mapHeight, obstacleMap);
                AreaMap map = new AreaMap(mapWith, mapHeight, t.obstacleMap());
				
                log.addToLog("Heuristic initializing...");
                AStarHeuristic heuristic = new DiagonalHeuristic();
                
                log.addToLog("Pathfinder initializing...");
                AStar pathFinder = new AStar(map, heuristic);
                
                log.addToLog("Calculating shortest path...");
                s.start();
                pathFinder.calcShortestPath(t.startX, t.startY, t.goalX, t.goalY);
                s.stop();
                
                log.addToLog("Time to calculate path in milliseconds: " + s.getElapsedTime());
                
                log.addToLog("Printing map of shortest path...");
                pathFinder.printPath();
        }*/
		
		public int[][] givePath(int[][] blocked, int sc, int sr, int fc, int fr)
		{
		int[][] out = new int[blocked.length][blocked[0].length];
	//	Logger log = new Logger();
         //       StopWatch s = new StopWatch();
				
				
                
                //log.addToLog("Map initializing...");
                //AreaMap map = new AreaMap(mapWith, mapHeight, obstacleMap);
				
				//
				int mapWith = blocked[0].length, mapHeight = blocked.length;
				
                AreaMap map = new AreaMap(mapWith, mapHeight, blocked);//t.obstacleMap());
				
                //log.addToLog("Heuristic initializing...");
                AStarHeuristic heuristic = new DiagonalHeuristic();
                
                //log.addToLog("Pathfinder initializing...");
                AStar pathFinder = new AStar(map, heuristic);
                
               // log.addToLog("Calculating shortest path...");
              //  s.start();
                pathFinder.calcShortestPath(sc,sr,fc,fr);
              //  s.stop();
                
               // log.addToLog("Time to calculate path in milliseconds: " + s.getElapsedTime());
                
             //   log.addToLog("Printing map of shortest path...");
			 
             out = pathFinder.mixture();//pathFinder.printPath();
		
		return out;
		}

}