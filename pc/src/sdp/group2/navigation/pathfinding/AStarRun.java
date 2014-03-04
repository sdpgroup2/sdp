package sdp.navigation.pathfinding;

import java.util.ArrayList;

public class AStarRun implements Pathfinder {
	
	private int height, width;
	private Node start, target;
	private AStar astar;
	private ArrayList<Node> balls, oppositions, path;

	public AStarRun(int width, int height, Node start, Node target, ArrayList<Node> balls, ArrayList<Node> oppositions) {
		this.height = height;
		this.width = width;
		this.start = start;
        this.target = target;
		this.balls = balls;
		this.oppositions = oppositions;
	
	
		this.astar = new AStar(this.width, this.height, this.start, this.target, this.balls, this.oppositions);
	
		this.path = astar.returnPath();

	}
	
	public ArrayList<Node> getPath(){
		return this.path;
	}
	
	public Node[][] getMap(){
		return this.astar.getMap();
	}
	
	public void printMap(){
		this.astar.printMap(path);
	}
}
