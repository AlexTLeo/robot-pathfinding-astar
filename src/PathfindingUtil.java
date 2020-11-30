import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PathfindingUtil {
  /**
   * Calculates Manhattan Distance from start cell to end cell
   */
  public static int heuristic(int startX, int startY, int endX, int endY) {
    return (endX - startX) + (endY - startY);
  }

  /**
   * Sort list prioritising EAST and SOUTH cells
   */
  public static void sortList(ArrayList<GridWorld.Coordinate> list) {
    Collections.sort(list, new Comparator<GridWorld.Coordinate>() {

      @Override
      public int compare(GridWorld.Coordinate c1, GridWorld.Coordinate c2) {
        if (((c2.row - c1.row) > 0) || (c2.col - c1.col) > 0) {
          return 1;
        } else {
          return -1;
        }
      }
      
    });
  }
  
  /**
   * Calculate direction from c1 to c2 as a GridWorld.Direction
   */
  public static GridWorld.Direction getDirectionTowards(GridWorld.Coordinate c1, GridWorld.Coordinate c2) {
    int dirX = c1.row - c2.row,
        dirY = c1.col - c2.col;
        
    GridWorld.Direction dir = null;
    
    if (dirX > 0) {
      dir = GridWorld.Direction.NORTH;
    }
    else if (dirX < 0) {
      dir = GridWorld.Direction.SOUTH;
    }
    else if (dirY > 0) {
      dir = GridWorld.Direction.WEST;
    }
    else if (dirY < 0) {
      dir = GridWorld.Direction.EAST;
    }
    
    return dir;
  }
  
  
  /**
   * Checks if two cells are adjacent
   */
  public static boolean checkIfAdjacent(GridWorld.Coordinate c1, GridWorld.Coordinate c2) {
    int dirX = c1.row - c2.row;
    int dirY = c1.col - c2.col;
    
    // If diagonal
    if (Math.abs(dirX) == 1 && Math.abs(dirY) == 1) {
      return false;
    } else if (Math.abs(dirX) > 1 || Math.abs(dirY) > 1) {
      return false;
    }
    
    return true;
  }
}
