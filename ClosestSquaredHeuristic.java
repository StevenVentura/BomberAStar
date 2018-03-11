//package aStar.heuristics;

//import aStar.AStarHeuristic;

/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile. In this case the sqrt is removed
 * and the distance squared is used instead
 */
public class ClosestSquaredHeuristic implements AStarHeuristic {

        /**
         * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
         */
        public float getEstimatedDistanceToGoal(int startX, int startY, int goalX, int goalY) {         
                float dx = goalX - startX;
                float dy = goalY - startY;
                
                return ((dx*dx)+(dy*dy));
        }

}