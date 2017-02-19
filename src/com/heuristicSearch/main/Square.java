package com.heuristicSearch.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.PriorityQueue;

import javax.swing.JPanel;

public class Square extends JPanel{

	private static final long serialVersionUID = 1L;
	public char color;
	public double moveXY;		//1 w-w, 2 g-g, 1.5 w-g/g-w |||||||||| WITH HIGHWAY 0.25 lb-lb, 0.5 db-db, 0.375 lb-db/dblbw
	public double moveDiag; //1.41 w-w, 2.83 g-g, 2.12 w-g/g-w ||||||| WITH HIGHWAY 0.35 lb-lb, 0.71 db-db, 0.53 lb-db/dblbw
	public String currColor;														//for lb=db/db-lb use getAvg
	char typeOfCell = '1';
	int x, y;
	boolean isS = false; //Is start node
	boolean isG = false; //Is goal node
	boolean hasHighway = false; 
	Square[] neighbors = new Square[8];
	double gCost = 0;
	double hCost = 0;
	double fCost = this.gCost + this.hCost;
	Square parent = null;
	Dimension dim = getSize();
	public double dimW = dim.getHeight();
	Square[] bp = new Square[5];
	double[] gCosts;
	double[] hCosts;
	double[] fCosts;
	int currentI=0;
	boolean init;
	
	//default to white
	public Square(){
		this.color = '1';
		this.moveXY = 1.0;
		this.moveDiag = 1.41;
		this.currColor = "GREEN";
	}
	
	public void sequentialSearchInitialize(int i) {
		gCosts = new double[i];
		hCosts = new double[i];
		fCosts = new double[i];
		init = true;
	}
	
	public void updateHVal(Square goal, double weight, int hNum) {
		switch(hNum) {
		case 1: //octile distance
			this.hCost = Math.abs(getDistance2(this,goal)) * weight;
			break;
		case 0: //Manhattan distance / 4
			this.hCost = (Math.abs(this.x-goal.x) + Math.abs(this.y - goal.y))/4 * weight;
			break;
		case 2: //Euclidian distance
			this.hCost = Math.sqrt(Math.pow(Math.abs(this.x-goal.x),2) + Math.pow(Math.abs(this.y-goal.y), 2)) * weight;
			break;
		case 3: //Uniform Cost Diagonal Heuristic
			this.hCost = Math.max(Math.abs(x-goal.x), Math.abs(y-goal.y)) * weight;
			break;
		case 4: //0 Distance
			this.hCost = 0;
			break;
		}
		fCost = this.gCost + hCost;
	}
	
	public void updateHVal2(Square goal, double weight, int hNum) {
		switch(hNum) {
		case 1: //octile distance
			this.hCosts[currentI] = Math.abs(getDistance2(this,goal)) * weight;
			break;
		case 0: //Manhattan distance / 4
			this.hCosts[currentI] = (Math.abs(this.x-goal.x) + Math.abs(this.y - goal.y))/4.0 * weight;
			break;
		case 2: //Euclidian distance
			this.hCosts[currentI] = Math.sqrt(Math.pow(Math.abs(this.x-goal.x),2) + Math.pow(Math.abs(this.y-goal.y), 2)) * weight;
			break;
		case 3: //Uniform Cost Diagonal Heuristic
			this.hCosts[currentI] = (double) Math.max(Math.abs(x-goal.x), Math.abs(y-goal.y)) * weight;
			break;
		case 4: //0 Distance
			this.hCosts[currentI] = 0.0;
			break;
		}
		fCosts[currentI] = this.gCosts[currentI] + this.hCosts[currentI];
	}

	
	public void updateGVal(Square start) {
		//this.hCost = Math.abs(getDistance(start));
		fCost = this.gCost + this.hCost; 
	}
	
	public double getFCost(){
		fCost = this.gCost + hCost;
		return fCost;
	}
	
	public double getFCost2(Square goal, double weight){
		updateHVal2(goal, weight, currentI);
		fCosts[currentI]= this.gCosts[currentI] + this.hCosts[currentI];
		return fCosts[currentI];
	}
	
	private double getDistance2(Square square1, Square goal){
		double D2 = Math.sqrt(2);
		double dx = Math.abs(square1.x - goal.x);
		double dy = Math.abs(square1.y - goal.y);
		return (dx + dy) + (D2 - 2) * Math.min(dx, dy);
	}
	
	private double getDistance(Square goal){
		double dx = this.x - goal.x;
		double dy = this.y - goal.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	//if you specified a color set that color
	public Square(char color, int x, int y, boolean isStart, boolean isGoal){
		if(isStart){
			this.color = 's';
		} else if (isGoal) {
			this.color = 'g';
		}
		this.color = color;
		this.currColor = setColor(this.color);
		this.x = x;
		this.y = y;
		this.isG = isGoal;
		this.isS = isStart;
		
		switch(currColor) {
		case "GREEN": 					//Regular unblocked
			typeOfCell = '1';
			this.moveXY = 1.0;	
			this.moveDiag = 1.41;
			break;
			
		case "DARK_GREEN": 				//Regular hard to traverse
			typeOfCell = '2';
			this.moveXY = 2.0;	
			this.moveDiag = 2.83;
			break;
			
		case "BLACK": 					//Blocked
			typeOfCell = '0';
			this.moveXY = 0.0;	
			this.moveDiag = 0.00;
			break;
			
		case "LIGHT_BLUE": 				//Highway unblocked
			typeOfCell = 'a';
			this.moveXY = 0.25;
			this.moveDiag = 0.35;
			hasHighway = true;
			break;
			
		case "DARK_BLUE": 				//Highway hard to traverse
			typeOfCell = 'b';
			this.moveXY = 0.5;	
			this.moveDiag = 0.71;
			hasHighway = true;
			break;
			
		case "PURPLE": 					//Start
			typeOfCell = '1';
			this.moveXY = 1; 
			this.moveDiag = 1.41;
			isS = true;
			break;
			
		case "RED": 					//Goal
			typeOfCell = '1';
			this.moveXY = 1; 
			this.moveDiag = 1.41;
			isG = true;
			break;
		}
		
	}
	
	//returns appropriate color of square to be set
	public static String setColor(char color){
		switch(color) {
		case '0':
			return "BLACK";
		case '1':
			return "GREEN";
		case '2':
			return "DARK_GREEN";
		case 'a':
			return "LIGHT_BLUE";
		case 'b':
			return "DARK_BLUE";
		case 's':
			return "PURPLE";
		case 'g':
			return "RED";
		}		
		return null;
	}
	
	//gets current color of square
	public String getColor(){
		return currColor;
	}
	
	public static Square voidSquare(){
    	return null;
    }
	
    public static Square getSquare(int x, int y){
    	if(x < 0 || y < 0 || x >= NewGrid.column || y >= NewGrid.row){
    		return voidSquare();
    	}
    	
		return NewGrid.squares[x][y];
    }
    
    public double getAvg(double currP, double nextP){
    	return ((currP + nextP)/2);
    }

	 public Dimension getMinimumSize() {
	        return new Dimension(8, 8);
	    }

	    public Dimension getPreferredSize() {
	        return new Dimension(10, 10);
	    }

	    public Dimension getMaximumSize() {
	        return new Dimension(12, 12);
	    }
	    
	    public void squareInFringe() {
	    	if(this.typeOfCell != 'a' && this.typeOfCell != 'b' && this.currColor != "DARK_GREEN" && !isS && !isG && this.currColor != "GOLD")
	    		this.currColor = "BLUE_GREEN";
	    	repaint();
	    }
	    
	    public void squareInClosed() {
	    	if(this.typeOfCell != 'a' && this.typeOfCell != 'b' && this.currColor != "DARK_GREEN" && !isS && !isG && this.currColor != "GOLD")
	    		this.currColor = "LIGHT_BLUE_GREEN";
	    	repaint();
	    }
	    
	    public void tracePath(String color) {
	    	if(!isG && !isS)
	    		this.currColor = color;
	    	repaint();
	    }
	    
	    public void isStart() {
	    	this.currColor = "PURPLE";
	    	repaint();
	    }
	    
	    public void addHighway() {
	    	if(typeOfCell == '1') {
		    	hasHighway = true;
	    		typeOfCell = 'a';
	    		currColor = setColor(typeOfCell);
	    		
	    	} else if(this.typeOfCell == '2') {
		    	hasHighway = true;
	    		typeOfCell = 'b'; 
	    		currColor = setColor(typeOfCell);
	    	}
	    	repaint();
	    }
	    
	    public void removeHighway() {
	    	if(typeOfCell == 'a') {
	    		hasHighway = false;
	    		typeOfCell = '1';
	    		currColor = setColor(typeOfCell);
	    	} else if(typeOfCell == 'b') {
	    		hasHighway = false;
	    		typeOfCell = '1';
	    		currColor = setColor(typeOfCell);
	    	}
	    	repaint();
	    }
	    
	    //main GUI stuff to paint the squares to appropriate color and size on grid
	    public void paintComponent(Graphics g) {
	        int margin = 1;
	        Dimension dim = getSize();
	        super.paintComponent(g);
	        if(currColor == "GREEN") g.setColor(new Color(108, 196, 84));			//regular unblocked cell
	        if(currColor == "DARK_GREEN") g.setColor(new Color(57, 104, 33));		//Hard to traverse cell
	        if(currColor == "BLACK") g.setColor(Color.black);						//blocked cell
	        if(currColor == "LIGHT_BLUE") g.setColor(Color.blue.brighter());		//Regular unblocked with highway
	        if(currColor == "DARK_BLUE") g.setColor(Color.blue.darker());			//Hard to traverse with highway
	        if(currColor == "PURPLE") g.setColor(new Color(204, 0, 204));			//START
	        if(currColor == "RED") g.setColor(new Color(255, 0, 0));				//GOAL
	        if(currColor == "BLUE_GREEN") g.setColor(new Color(0, 102, 102));		//FRINGE
	        if(currColor == "LIGHT_BLUE_GREEN") g.setColor(new Color(51, 255, 156));//SEARCHED new Color(0, 204, 153)
	        if(currColor == "GOLD") g.setColor(new Color(255, 217, 0));				//PATH
	        if(currColor == "WHITE") g.setColor(Color.WHITE);
	        g.fillRect(margin, margin, dim.width, dim.height);
	    }

		public void changeTypeOfCell(char c) {
			currColor = setColor(c);
			switch(currColor) {
			case "GREEN": 		//Regular unblocked
				typeOfCell = '1';
				this.moveXY = 1.0;	
				this.moveDiag = 1.41;
				break;
				
			case "DARK_GREEN": //Regular hard to traverse
				typeOfCell = '2';
				this.moveXY = 2.0;	
				this.moveDiag = 2.83;
				break;
				
			case "BLACK": //Blocked
				typeOfCell = '0';
				this.moveXY = 0.0;	
				this.moveDiag = 0.00;
				break;
				
			case "LIGHT_BLUE": //Highway unblocked
				typeOfCell = 'a';
				this.moveXY = 0.25;
				this.moveDiag = 0.35;
				hasHighway = true;
				break;
				
			case "DARK_BLUE": //Highway hard to traverse
				typeOfCell = 'b';
				this.moveXY = 0.5;	
				this.moveDiag = 0.71;
				hasHighway = true;
				break;
				
			case "PURPLE": //Start
				typeOfCell = '1';
				this.moveXY = 1; 
				this.moveDiag = 1.41;
				isS = true;
				break;
				
			case "RED": //Goal
				typeOfCell = '1';
				this.moveXY = 1; 
				this.moveDiag = 1.41;
				isG = true;
				break;
			}

		}

		
		public boolean equals(Object object){
			if(!(object instanceof Square)) return false;
			Square vec = (Square) object;
			if(vec.x == this.x && vec.y == this.y) return true;
			return false;
		}

}
