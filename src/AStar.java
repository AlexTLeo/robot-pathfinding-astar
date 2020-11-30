import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * An implementation of the A Star algorithm on a square NxN grid with a
 * "robot" that can only move one adjacent tile at a time
 * @author Thanaphon Leonardi
 */
public class AStar {
  private GridWorld map;
  private int mapSize;
  private int startX;
  private int startY;
  private int targetX;
  private int targetY;

  // Prioritised queue of cells based on the A-Star scoring system
  private PriorityQueue<GridWorld.Coordinate> queue;
  // Contains the parent of the specified cell in list on the shortest path to it
  private GridWorld.Coordinate[][] parentCells;
  // Contains the cost of the shortest path from start to specified cell in array
  private int[][] costFromStart;
  // Estimated best cost (guessed) from start to finish if passing through specified cell in array
  private int[][] costToFinish;
  // Keeps track of visited cells
  private boolean[][] visited;
  
  public AStar(GridWorld map, int mapSize, int startX, int startY, int targetX, int targetY) {
    this.startX = startX;
    this.startY = startY;
    this.targetX = targetX;
    this.targetY = targetY;
    this.map = map;
    this.mapSize = mapSize;
    parentCells = new GridWorld.Coordinate[mapSize][mapSize];
    costFromStart = new int[mapSize][mapSize];
    costToFinish = new int[mapSize][mapSize];
    visited = new boolean[mapSize][mapSize];
    
    queue = new PriorityQueue<GridWorld.Coordinate>(new Comparator<GridWorld.Coordinate>() {
      @Override
      public int compare(GridWorld.Coordinate c1, GridWorld.Coordinate c2) {
        int c1Cost = PathfindingUtil.heuristic(c1.row, c1.col, targetX, targetY) + costFromStart[c1.row][c1.col],
            c2Cost = PathfindingUtil.heuristic(c2.row, c2.col, targetX, targetY) + costFromStart[c2.row][c2.col];
        
        if (c1Cost < c2Cost) {
          return -1;
        } else if (c1Cost == c2Cost) {
          return 0;
        } else {
          return 1;
        }
      }
    });
    
    // Initialising some values
    for (int i = 0; i < mapSize; i++) {
      Arrays.fill(costFromStart[i], Integer.MAX_VALUE);
      Arrays.fill(costToFinish[i], Integer.MAX_VALUE);
      Arrays.fill(visited[i], false);
    }
    
    costFromStart[startX][startY] = 0;
    costToFinish[startX][startY] = PathfindingUtil.heuristic(startX, startY, targetX, targetY);
    visited[0][0] = true;
    queue.add(map.getCurrentCell());
  }
  
  /**
   * A Star algorithm start call
   */
  public LinkedList<GridWorld.Coordinate> start() {
    LinkedList<GridWorld.Coordinate> path = find();
    
    return path;
  }
  
  /**
   * Main A Star exploration algorithm
   */
  private LinkedList<GridWorld.Coordinate> find() {
    while(!queue.isEmpty()) {
      GridWorld.Coordinate nextCell = queue.remove();
      
      moveToCell(nextCell);
      
      if(map.targetReached()) {
        return reconstructPath();
      }
        
      // Explore adjacent cells and update scoring and queue
      for (GridWorld.Coordinate neighbourCell : map.getAdjacentFreeCells()) {
        int x = map.getCurrentCell().row,
            y = map.getCurrentCell().col,
            nbrX = neighbourCell.row,
            nbrY = neighbourCell.col;
        
        int newCostToNeighbour = costFromStart[x][y] + 1;
        
        if (newCostToNeighbour < costFromStart[nbrX][nbrY]) {
          parentCells[nbrX][nbrY] = map.getCurrentCell();
          costFromStart[nbrX][nbrY] = newCostToNeighbour;
          costToFinish[nbrX][nbrY] = newCostToNeighbour + PathfindingUtil.heuristic(nbrX, nbrY, targetX, targetY);
          
          if (!queue.contains(neighbourCell)) {
            queue.add(neighbourCell);
          }
        }
      }
    }
      
    // If path never found
    return null;
  }
  
  /**
   * Moves robot to specified target cell. If target is adjacent, moves directly,
   * otherwise applies a DFS algorithm to travel to target
   * @param target - target cell to move to
   */
  private void moveToCell(GridWorld.Coordinate target) {
    int x = map.getCurrentCell().row;
    int y = map.getCurrentCell().col;
    boolean isTargetAdjacent = true;

    GridWorld.Direction dir = null;
    GridWorld.Coordinate nextCell = target;
    
    if (!map.getCurrentCell().equals(target)) {
      isTargetAdjacent = PathfindingUtil.checkIfAdjacent(map.getCurrentCell(), target);
      if (!isTargetAdjacent) {
        DFS dfs = new DFS(map, mapSize, target.row, target.col, visited, parentCells, costFromStart, costToFinish, queue);
        dfs.start();
      } else {
        dir = PathfindingUtil.getDirectionTowards(map.getCurrentCell(), target);
        moveToAdjacentCell(dir);
      }
    }
  }
  
  /**
   * Wrapper to GridWorld's moveToAdjacentCell() function to include extra operations
   */
  private void moveToAdjacentCell(GridWorld.Direction dir) {
    if (dir != null) {
      map.moveToAdjacentCell(dir);
      visited[map.getCurrentCell().row][map.getCurrentCell().col] = true; 
    }
  }

  /**
   * Reconstructs path from beginning to end
   */
  private LinkedList<GridWorld.Coordinate> reconstructPath() {
    LinkedList<GridWorld.Coordinate> bestPath = new LinkedList<GridWorld.Coordinate>();
    GridWorld.Coordinate currentCell = map.getCurrentCell();
    bestPath.addFirst(currentCell);
    
    while (true) {
      currentCell = parentCells[currentCell.row][currentCell.col];
      bestPath.addFirst(currentCell);
      
      if (currentCell.row == 0 && currentCell.col == 0) {
        break;
      }
    }

    return bestPath;
  }
}

