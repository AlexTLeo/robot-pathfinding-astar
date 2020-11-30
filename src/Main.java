import java.util.LinkedList;

/**
 * Main function of a pathfinding algorithm through a NxN grid
 * by a "robot" that can only explore one adjacent tile at a time
 * @author Thanaphon Leonardi
 */
public class Main {
  public static void main(String[] args) {
    int size = Integer.parseInt(args[0]);
    double density = Double.parseDouble(args[1]);
    long seed = Long.parseLong(args[2]);
    LinkedList<GridWorld.Coordinate> path = new LinkedList<GridWorld.Coordinate>();        
    GridWorld gridWorld = new GridWorld(size, density, seed);
    AStar pathFinder = new AStar(gridWorld, size, 0, 0, size - 1, size - 1);

    long timeStart = System.nanoTime();
    path = pathFinder.start();
    
    if (path != null && gridWorld.checkPath(path) && gridWorld.checkPathAcyclic(path)) {
      System.out.print("Percorso: ");
      for (GridWorld.Coordinate elem : path) {
        System.out.print(elem + " ");
      }
    } else {
      System.out.println("Nessun percorso!");
    }
  }
}
