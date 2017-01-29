package com.heuristicSearch.main;

import java.awt.Canvas;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.heuristicSearch.main.NewGrid;

public class Search extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280, HEIGHT = 960; //width and height of frame
	public NewGrid grid;
	
	//compares fCost for A* algorithm and picks the next box accordingly
	private Comparator<Square> sorter = new Comparator<Square>(){
		public int compare(Square b1, Square b2) {
			if(b1.getFCost() < b2.getFCost()) return -1;
			if(b1.getFCost() > b2.getFCost()) return 1;
			return 0;
		}
	};
	
	//finds the shortest path using the A* algorithm 
	//which traverses through an array list representation of Boxes on top of squares
//	public List<Square> findPath(Square start, Square goal){
//		List<Square> openList = new ArrayList<Square>();
//		List<Square> closedList = new ArrayList<Square>();
//		Square current = new Square(start, null, 0, getDistance(start, goal));
//		openList.add(current);
//		while(openList.size() > 0){
//			Collections.sort(openList, sorter);
//			current = openList.get(0);
//			if(current.equals(goal)){
//				List<Square> path = new ArrayList<Square>();
//				while(current.parent != null){
//					path.add(current);
//					current = current.parent;
//				}
//				openList.clear();
//				closedList.clear();
//				return path;
//			}
//			openList.remove(current);
//			closedList.add(current);
//			//looks through he 9 boxes around you and including you, but skips you and the box you came from
//			for(int i = 0; i < 9; i++){
//				if(i==4) continue;
//				int x = current.square.getX();
//				int y = current.square.getY();
//				int xi = (i % 3) - 1;
//				int yi = (i / 3) - 1;
//				Square atLocation = Square.getSquare(x + xi, y+yi);
//				if(atLocation == null) continue;
//				if(atLocation.getColor() == "BLACK") continue;
//				Vector2int at = new Vector2int(x + xi, y + yi);
//				double gCost = current.gCost + getDistance(current.square, at);
//				double hCost = getDistance(at, goal);
//				Boxes box = new Boxes(at, current, gCost, hCost);
//				if(vecInList(closedList, at) && gCost >= box.gCost) continue;
//				if(!vecInList(openList, at) || gCost < box.gCost) openList.add(box);
//			}
//		}
//		closedList.clear();		
//		return null;
//	}
	
	public void aStarFindPath(Square start, Square goal) {
		start.gCost = 0;
		start.parent = start;
		PriorityQueue<Square> fringe = new PriorityQueue<Square>(sorter);
		ArrayList<Square> closed = new ArrayList<Square>();
		fringe.add(start);
		start.squareInFringe();
		while(!fringe.isEmpty()) {
			Square current = fringe.remove();
			current.updateGVal(start);
			current.updateHVal(goal);
			
			if(current.equals(goal) || getDistance(goal, current) < 1.41) {
				System.out.println("Path Found");
				break;
			}
			closed.add(current);
			current.squareInClosed();
			for(int i=0; i<8; i++) {
				if(current.neighbors[i] != null) {
					Square sPrime = current.neighbors[i];
					if(!doesClosedContain(sPrime, closed)) {
						if(!doesFringeContain(sPrime, fringe)) {
							sPrime.gCost = Double.MAX_VALUE;
							sPrime.getFCost();
							fringe.remove(sPrime);
							fringe.add(sPrime);
							sPrime.parent = null;
						}
						try {
						    Thread.sleep(1);
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						updateVertex(current,sPrime, start, goal, i, fringe);
					}
				}
			}
		}
		
	}
	
	private void updateVertex(Square current, Square sPrime, Square start, Square goal, int index, PriorityQueue<Square> fringe) {
		if(current.gCost + getCVal(current, sPrime, index) < sPrime.gCost) {
			sPrime.gCost = current.gCost + getCVal(current,sPrime, index);
			sPrime.getFCost();
			sPrime.parent = current;
			if(doesFringeContain(sPrime, fringe)) {
				fringe.remove(sPrime);
			}
			sPrime.updateGVal(current);
			sPrime.updateHVal(goal);
			sPrime.getFCost();
			fringe.add(sPrime);
			sPrime.squareInFringe();
		}
	}
	
	public double getCVal(Square current, Square sPrime, int index) {
		if(index%2 == 0) {
			if (current.typeOfCell =='1' && sPrime.typeOfCell == '2') {
				return (Math.sqrt(2) + Math.sqrt(8))/2;
			} else if(current.typeOfCell == '1' && sPrime.typeOfCell == '1'){
				return Math.sqrt(2);
			} else{ 
				return Math.sqrt(8);
			}
		} else {
			if (current.typeOfCell =='1' && sPrime.typeOfCell == '2') {
				return 1.5;
			} else if(current.typeOfCell == '1' && sPrime.typeOfCell == '1'){
				return 1;
			} else{ 
				return 2;
			}
			
		}
	}
	
	private boolean doesClosedContain(Square curr, ArrayList<Square> closed) {
		for(Square sq: closed) {
			if(sq.equals(curr)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean doesFringeContain(Square curr, PriorityQueue<Square> fringe) {
		for(Square sq: fringe) {
			if(sq.equals(curr)) {
				return true;
			}
		}
		return false;
	}
	
	
	//checks if the vector is in the array list of boxes
//	private boolean vecInList(List<Boxes> list, Vector2int vector){
//		for(Boxes n : list){
//			if(n.square.equals(vector)) return true;
//		}
//		return false;
//	}
	
	//mainly used for hCost it gets the distance from you to goal box using pythagorean theorem
	private double getDistance(Square square1, Square goal){
		double dx = square1.x - goal.x;
		double dy = square1.y - goal.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	
	//main call and search method which creates a grid behind the boxes.
	//I haven't called the A* algorithm yet, just created a GUI of grids
	//But I am confident it works I looked at many tutorials on youtube
	public Search(){
		grid = new NewGrid();
		aStarFindPath(grid.sStart, grid.sGoal);
	}
	
	public static void main(String args[]){
		new Search();
	}
	
		
	//you have to keep this method because it's a runnable implemented
	public void run() {
		
	}

}
