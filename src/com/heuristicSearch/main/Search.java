package com.heuristicSearch.main;

import java.awt.Canvas;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.heuristicSearch.main.NewGrid;

public class Search extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280, HEIGHT = 960; //width and height of frame
	public NewGrid grid;
	
	//compares fCost for A* algorithm and picks the next box accordingly
	private Comparator<Boxes> sorter = new Comparator<Boxes>(){
		public int compare(Boxes b1, Boxes b2) {
			if(b1.fCost < b2.fCost) return 1;
			if(b1.fCost > b2.fCost) return -1;
			return 0;
		}
	};
	
	//finds the shortest path using the A* algorithm 
	//which traverses through an array list representation of Boxes on top of squares
	public List<Boxes> findPath(Vector2int start, Vector2int goal){
		List<Boxes> openList = new ArrayList<Boxes>();
		List<Boxes> closedList = new ArrayList<Boxes>();
		Boxes current = new Boxes(start, null, 0, getDistance(start, goal));
		openList.add(current);
		while(openList.size() > 0){
			Collections.sort(openList, sorter);
			current = openList.get(0);
			if(current.square.equals(goal)){
				List<Boxes> path = new ArrayList<Boxes>();
				while(current.parent != null){
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			//looks through he 9 boxes around you and including you, but skips you and the box you came from
			for(int i = 0; i < 9; i++){
				if(i==4) continue;
				int x = current.square.getX();
				int y = current.square.getY();
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Square atLocation = Square.getSquare(x + xi, y+yi);
				if(atLocation == null) continue;
				if(atLocation.getColor() == "BLACK") continue;
				Vector2int at = new Vector2int(x + xi, y + yi);
				double gCost = current.gCost + getDistance(current.square, at);
				double hCost = getDistance(at, goal);
				Boxes box = new Boxes(at, current, gCost, hCost);
				if(vecInList(closedList, at) && gCost >= box.gCost) continue;
				if(!vecInList(openList, at) || gCost < box.gCost) openList.add(box);
			}
		}
		closedList.clear();		
		return null;
	}
	
	//checks if the vector is in the array list of boxes
	private boolean vecInList(List<Boxes> list, Vector2int vector){
		for(Boxes n : list){
			if(n.square.equals(vector)) return true;
		}
		return false;
	}
	
	//mainly used for hCost it gets the distance from you to goal box using pythagorean theorem
	private double getDistance(Vector2int square, Vector2int goal){
		double dx = square.getX() - goal.getX();
		double dy = square.getY() - goal.getY();
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	
	//main call and search method which creates a grid behind the boxes.
	//I haven't called the A* algorithm yet, just created a GUI of grids
	//But I am confident it works I looked at many tutorials on youtube
	public Search(){
		grid = new NewGrid();
	}
	
	public static void main(String args[]){
		new Search();
	}
	//you have to keep this method because it's a runnable implemented
	public void run() {
		
	}

}
