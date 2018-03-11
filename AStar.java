//package aStar;

import java.util.ArrayList;
import java.util.Collections;

import java.io.FileWriter;
import java.io.BufferedWriter;

//import aStar.heuristics.AStarHeuristic;
//import aStar.utils.Logger;

public class AStar {
        private AreaMap map;
        private AStarHeuristic heuristic;
        private int startX;
        private int startY;
        private int goalX;
        private int goalY;
        /**
         * closedList The list of Nodes not searched yet, sorted by their distance to the goal as guessed by our heuristic.
         */
        private ArrayList<Node> closedList;
        private SortedNodeList openList;
        private Path shortestPath;
        Logger log = new Logger();

        AStar(AreaMap map, AStarHeuristic heuristic) {
                this.map = map;
                this.heuristic = heuristic;

                closedList = new ArrayList<Node>();
                openList = new SortedNodeList();
        }

        public Path calcShortestPath(int startX, int startY, int goalX, int goalY) {
                this.startX = startX;
                this.startY = startY;
                this.goalX = goalX;
                this.goalY = goalY;

                //mark start and goal node
                map.setStartLocation(startX, startY);
                map.setGoalLocation(goalX, goalY);

                //Check if the goal node is blocked (if it is, it is impossible to find a path there)
                if (map.getNode(goalX, goalY).isObstacle) {
					//System.out.println("Wow@!#!@#!@#!@#");
                        return null;
                } 

                map.getStartNode().setDistanceFromStart(0);
                closedList.clear();
                openList.clear();
                openList.add(map.getStartNode());

                //while we haven't reached the goal yet
                while(openList.size() != 0) {

                        //get the first Node from non-searched Node list, sorted by lowest distance from our goal as guessed by our heuristic
                        Node current = openList.getFirst();

                        // check if our current Node location is the goal Node. If it is, we are done.
						//System.out.println("XXX:"+map.getGoalLocationX() + "YYY:" + map.getGoalLocationY());
                        if(current.getX() == map.getGoalLocationX() && current.getY() == map.getGoalLocationY()) {
                                return reconstructPath(current);
                        }

                        //move current Node to the closed (already searched) list
                        openList.remove(current);
                        closedList.add(current);

                        //go through all the current Nodes neighbors and calculate if one should be our next step
                        for(Node neighbor : current.getNeighborList()) {
                                boolean neighborIsBetter;

                                //if we have already searched this Node, don't bother and continue to the next one 
                                if (closedList.contains(neighbor))
                                        continue;

                                //also just continue if the neighbor is an obstacle
                                if (!neighbor.isObstacle) {

                                        // calculate how long the path is if we choose this neighbor as the next step in the path 
                                        float neighborDistanceFromStart = (current.getDistanceFromStart() + map.getDistanceBetween(current, neighbor));

                                        //add neighbor to the open list if it is not there
                                        if(!openList.contains(neighbor)) {
                                                openList.add(neighbor);
                                                neighborIsBetter = true;
                                                //if neighbor is closer to start it could also be better
                                        } else if(neighborDistanceFromStart < current.getDistanceFromStart()) {
                                                neighborIsBetter = true;
                                        } else {
                                                neighborIsBetter = false;
                                        }
                                        // set neighbors parameters if it is better
                                        if (neighborIsBetter) {
                                                neighbor.setPreviousNode(current);
                                                neighbor.setDistanceFromStart(neighborDistanceFromStart);
                                                neighbor.setHeuristicDistanceFromGoal(heuristic.getEstimatedDistanceToGoal(neighbor.getX(), neighbor.getY(), map.getGoalLocationX(), map.getGoalLocationY()));
                                        }
                                }

                        }

                }
				//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                return null;
        }

        
        
        public void printPath() {
		try{
		String fileName = "output\\StevenOutput.txt";
		FileWriter fstream = new FileWriter(fileName);
    	BufferedWriter write = new BufferedWriter(fstream);
                Node node;
                for(int x=0; x<map.getMapWith(); x++) {

                        if (x==0) {
                                for (int i=0; i<=map.getMapHeight(); i++)
                                        write.write("-");
                                write.write("\r\n");   
                        }
                        write.write("|");

                        for(int y=0; y<map.getMapHeight(); y++) {
                                node = map.getNode(x, y);
                                if (node.isObstacle) {
                                        write.write("X");
                                } else if (node.isStart) {
                                        write.write("s");
                                } else if (node.isGoal) {
                                        write.write("g");
                                } else if (shortestPath.contains(node.getX(), node.getY())) {
                                        write.write("¤");
                                } else {
                                        write.write(" ");
                                }
                                if (y==map.getMapHeight())
                                        write.write("_");
                        }

                        write.write("|");
                        write.write("\r\n");
                }
                for (int i=0; i<=map.getMapHeight(); i++)
                        write.write("-");
			
			write.close();
			fstream.close();
			}catch(Exception e){e.printStackTrace();};
        }
		public int[][] mixture() {
		try{
		int[][] out = new int[map.getMapHeight()][map.getMapWith()];
		
                Node node;
                for(int x=0; x<map.getMapWith(); x++) {

                     
                        for(int y=0; y<map.getMapHeight(); y++) {
						xxx = x; yyy = y;
                                node = map.getNode(x, y);
								int xx = node.getX();
								//boolean ccc = shortestPath.contains(node.getX(), node.getY());
								out[y][x] = 9;
                                if (node.isObstacle) {
                                        out[y][x] = 1;//write.write("X");
                                } else if (node.isStart) {
                                        //write.write("s");
                                } else if (node.isGoal) {
                                        out[y][x] = 5;//write.write("5");//write.write("g");
                                } else if (shortestPath.contains(node.getX(), node.getY())) {
                                        out[y][x] = 5;//write.write("5");//write.write("¤");
                                } else {
                                        //write.write("0");//write.write(" ");
                                }
                                
                        }

           
                }
                
			
			return out;
			}catch(Exception e){
			e.printStackTrace();
		//	System.out.println("@@:"  + xxx + ", " + yyy);
				for (int r = 0; r < map.getMapHeight(); r++)
				{
				for (int c = 0; c < map.getMapWith(); c++)
				{
				System.out.print(map.obstacleMap[r][c]);
				}
				System.out.println();
				}
				return null;
				}
        }
		private int xxx, yyy;
		
		

        private Path reconstructPath(Node node) {
                Path path = new Path();
                while(!(node.getPreviousNode() == null)) {
                        path.prependWayPoint(node);
                        node = node.getPreviousNode();
                }
                this.shortestPath = path;
                return path;
        }

        private class SortedNodeList {

                private ArrayList<Node> list = new ArrayList<Node>();

                public Node getFirst() {
                        return list.get(0);
                }

                public void clear() {
                        list.clear();
                }

                public void add(Node node) {
                        list.add(node);
                        Collections.sort(list);
                }

                public void remove(Node n) {
                        list.remove(n);
                }

                public int size() {
                        return list.size();
                }

                public boolean contains(Node n) {
                        return list.contains(n);
                }
        }

}
