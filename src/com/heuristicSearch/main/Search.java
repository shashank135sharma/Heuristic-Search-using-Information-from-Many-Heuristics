package com.heuristicSearch.main;

import java.awt.Canvas;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;

import com.heuristicSearch.main.NewGrid;

public class Search extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280, HEIGHT = 960; //width and height of frame
	public NewGrid grid;
	double weight = 1;
	String tracePathColor = "GOLD";
	String currentAlgo = "A*";
	
	//compares fCost for A* algorithm and picks the next box accordingly
	private Comparator<Square> sorter = new Comparator<Square>(){
		public int compare(Square b1, Square b2) {
			if(b1.getFCost() < b2.getFCost()) return -1;
			else if(b1.getFCost() > b2.getFCost()) return 1;
			else if(b1.gCost > b2.gCost) return 1;
			else return 1;
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
			current.updateHVal(goal, weight);
			
			if(current.equals(goal) || getDistance(goal, current) <= 1.41) {
				double length = 0;
				start.isStart();
				while(!current.parent.equals(start)) {
					try {
					    Thread.sleep(1);
					    current.tracePath(tracePathColor);
						length+=current.gCost;
						current = current.parent;
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
					if(current.parent == start){
						current.tracePath(tracePathColor);
					}
				}
				System.out.println("\nPath Found Using " + currentAlgo+"  With Length: "+length);
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
			sPrime.updateHVal(goal, weight);
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
	
	//main call and search method which creates a grid behind the boxes.
	//I haven't called the A* algorithm yet, just created a GUI of grids
	//But I am confident it works I looked at many tutorials on YouTube
	public Search() throws InvocationTargetException, InterruptedException, FileNotFoundException{
		grid = new NewGrid();
		System.out.print("What type of search would you like to run?\n(1) A*\t(2)Weighted A*\t(3)Uniform Cost Search: ");
		Scanner sc = new Scanner(System.in);
		int option = sc.nextInt();
		switch(option) {
		case 1:
			this.weight = 1;
			System.out.println("\nRegular A* Search started" );
			aStarFindPath(grid.sStart, grid.sGoal);
			break;
		case 2:
			System.out.print("Enter weight for Weighted A*: ");
			this.weight = sc.nextDouble();
			currentAlgo = "Weighted A*";
			System.out.println("\nWeighted A* Search with weight "+weight+" started" );
			aStarFindPath(grid.sStart, grid.sGoal);
			currentAlgo = "Regular A*";
			weight = 1;
			tracePathColor = "WHITE";
			aStarFindPath(grid.sStart, grid.sGoal);
			break;
		case 3:
			weight = 0;
			currentAlgo = "Uniform Cost Search";
			System.out.println("\nUniform Cost Search Started" );
			aStarFindPath(grid.sStart, grid.sGoal);
			break;
		}
	}
	
	public static void main(String args[]) throws InvocationTargetException, InterruptedException, FileNotFoundException{
		new Search();
	}
	
		
	//you have to keep this method because it's a runnable implemented
	public void run() {
		
	}

}
