package com.heuristicSearch.main;

import java.awt.Canvas;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.PriorityQueue;
import java.util.ArrayList;
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
	
	public void aStarFindPath(Square start, Square goal) {
		start.gCost = 0;
		start.parent = start;
		PriorityQueue<Square> fringe = new PriorityQueue<Square>(sorter);
		ArrayList<Square> closed = new ArrayList<Square>();
		fringe.add(start);
		start.isStart();
		while(!fringe.isEmpty()) {
			Square current = fringe.remove();
			if(current.typeOfCell != '2' && current.typeOfCell != 'b') {
				current.squareInFringe();
			}
			current.updateGVal(start);
			current.updateHVal(goal);
			
			if(current.equals(goal) || getDistance2(goal, current) < 0.5) {
				double length = 0;
				start.isStart();
				while(!current.parent.equals(start)) {
					try {
					    Thread.sleep(1);
					    current.tracePath();
						length+=current.gCost;
						current = current.parent;
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
					if(current.parent == start){
						current.tracePath();
					}
				}
				System.out.println("Path Found With Length: "+length);
				closed.clear();
				fringe.clear();
				break;
			}
			closed.add(current);
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
			if(current.typeOfCell != '2' && current.typeOfCell != 'b') {
				current.squareInClosed();
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
			if(sPrime.typeOfCell != '2' && sPrime.typeOfCell != 'b') {
				sPrime.squareInFringe();
			}
			fringe.add(sPrime);
		}
	}
	
	public double getCVal(Square current, Square sPrime, int index) {
		if(index%2 == 0) {
			if (current.typeOfCell =='1' && sPrime.typeOfCell == '2') {
				if(current.hasHighway && sPrime.hasHighway) {
					return ((Math.sqrt(2) + Math.sqrt(8))/2)/4;
				} else {
					return (Math.sqrt(2) + Math.sqrt(8))/2;
				}
			} else if(current.typeOfCell == '1' && sPrime.typeOfCell == '1'){
				if(current.hasHighway && sPrime.hasHighway) {
					return Math.sqrt(2)/4;
				} else {
					return Math.sqrt(2);
				}
			} else{ 
				if(current.hasHighway && sPrime.hasHighway) {
					return Math.sqrt(8)/4;
				} else {
					return Math.sqrt(8);
				}
			}
		} else {
			if (current.typeOfCell =='1' && sPrime.typeOfCell == '2') {
				if(current.hasHighway && sPrime.hasHighway) {
					return 1.5/4;
				} else {
					return 1.5;
				}
			} else if(current.typeOfCell == '1' && sPrime.typeOfCell == '1'){
				if(current.hasHighway && sPrime.hasHighway) {
					return 1/4;
				} else {
					return 1;
				}
			} else{ 
				if(current.hasHighway && sPrime.hasHighway) {
					return 2/4;
				} else {
					return 2;
				}			
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
	
	//mainly used for hCost it gets the distance from you to goal box using Pythagorean Theorem
	private double getDistance(Square square1, Square goal){
		double dx = Math.abs(square1.x - goal.x);
		double dy = Math.abs(square1.y - goal.y);
		return Math.sqrt(dx*dx + dy*dy);
	}
	private double getDistance2(Square square1, Square goal){
		double D = 1;
		double D2 = Math.sqrt(2);
		double dx = Math.abs(square1.x - goal.x);
		double dy = Math.abs(square1.y - goal.y);
		return D * (dx + dy) + (D2 - 2 * D) * min(dx, dy);
	}
	private double min(double dx, double dy){
		if(dx <= dy) return dy;
		else return dx;
	}
	
	//main call and search method which creates a grid behind the boxes.
	//I haven't called the A* algorithm yet, just created a GUI of grids
	//But I am confident it works I looked at many tutorials on YouTube
	public Search() throws InvocationTargetException, InterruptedException, FileNotFoundException{
		grid = new NewGrid();
		Thread.sleep(2000);
		aStarFindPath(grid.sStart, grid.sGoal);
	}
	
	public static void main(String args[]) throws InvocationTargetException, InterruptedException, FileNotFoundException{
		new Search();
	}
	
		
	//you have to keep this method because it's a runnable implemented
	public void run() {
		
	}

}
