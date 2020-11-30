import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * An implementation of the Depth-First-Search algorithm originally
 * written to be used in conjunction with the A Star algorithm for
 * non-adjacent cell movements 
 * @author Thanaphon Leonardi
 */
public class DFS {
  // Note: many of these variables and objects are just a reference to the calling A Star algorithm
  private int targetX;
  private int targetY;
  private int mapSize;
  private int[][] costFromStart;
  private int[][] costToFinish;
  private boolean[][] visited;
  private Color[][] marked;
  private GridWorld map;
  private GridWorld.Coordinate[][] parentCells;
  private PriorityQueue<GridWorld.Coordinate> queue;
  
  // Flags if DFS is done searching to interrupt all previous recursive searches
  private boolean isFinished = false;

  public DFS (GridWorld map, int mapSize, int targetX, int targetY, boolean[][] visited, GridWorld.Coordinate[][] parentCells, 
              int[][] costFromStart, int[][] costToFinish, PriorityQueue<GridWorld.Coordinate> queue) {
    this.targetX = targetX;
    this.targetY = targetY;
    this.mapSize = mapSize;
    this.costFromStart = costFromStart;
    this.costToFinish = costToFinish;
    this.visited = visited;
    marked = new Color[mapSize][mapSize];
    this.map = map;
    this.parentCells = parentCells;
    this.queue = queue;
    
    for(int i = 0; i < marked.length; i++) {
      Arrays.fill(marked[i], Color.WHITE);
    }
  }
  
  /**
   * DFS starts here
   */
  public void start() {
    DFSVisit(0, 0);
  }
  
  /**
   * Main DFS algorithm
   */
  private boolean DFSVisit(int row, int col) {
    marked[row][col] = Color.GREY;
    
    // Prioritising EAST and SOUTH cells first
    ArrayList<GridWorld.Coordinate> orderedAdj = new ArrayList<GridWorld.Coordinate>();
    orderedAdj = (ArrayList<GridWorld.Coordinate>) map.getAdjacentFreeCells();
    PathfindingUtil.sortList(orderedAdj);
    
    // Visiting all unvisited cells
    for (GridWorld.Coordinate adjCell : orderedAdj) {
      // Making sure A Star data structures remain updated
      int x = map.getCurrentCell().row,
          y = map.getCurrentCell().col,
          nbrX = adjCell.row,
          nbrY = adjCell.col;
      
      // Making sure DFS only travels along previously visited cells
      if(visited[nbrX][nbrY] || (adjCell.row == targetX && adjCell.col == targetY)) {
        if (marked[adjCell.row][adjCell.col] == Color.WHITE) {         
          GridWorld.Coordinate currentCell = map.getCurrentCell();
          moveToAdjacentCell(adjCell);
          DFSVisit(nbrX, nbrY);

          // If target is found, interrupt all recursions and terminate DFS search
          if (isFinished || targetReached()) {
            isFinished = true;
            return true;
          }          
          
          // Moves back to "starting" cell to check other adjacencies
          if (!targetReached()) {
            moveToAdjacentCell(currentCell);
          }
        }
      }
    }
    
    marked[row][col] = Color.BLACK;
    return false;
  }
  
  /**
   * @return true is target is reached, false otherwise
   */
  private boolean targetReached() {
    if (map.getCurrentCell().row == targetX && map.getCurrentCell().col == targetY) {
      return true;
    }
    
    return false;
  }
  
  /**
   * "Redefined" movement to include extra commands
   */
  private void moveToAdjacentCell(GridWorld.Coordinate cell) {
    GridWorld.Direction dir = PathfindingUtil.getDirectionTowards(map.getCurrentCell(), cell);
    if (dir != null) {
      map.moveToAdjacentCell(dir);
      visited[map.getCurrentCell().row][map.getCurrentCell().col] = true;
    }
  }
}
