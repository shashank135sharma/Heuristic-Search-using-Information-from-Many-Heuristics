package com.heuristicSearch.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

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
	
	//default to white
	public Square(){
		this.color = '1';
		this.moveXY = 1.0;
		this.moveDiag = 1.41;
		this.currColor = "WHITE";
	}
	
	public void updateHVal(Square goal) {
		this.hCost = Math.abs(getDistance(goal));
		fCost = this.gCost + this.hCost;
	}
	
	public void updateGVal(Square start) {
		this.hCost = Math.abs(getDistance(start));
		fCost = this.gCost + this.hCost; 
	}
	
	public double getFCost(){
		fCost = this.gCost + hCost;
		return fCost;
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
		case "WHITE": //Regular unblocked
			typeOfCell = '1';
			this.moveXY = 1.0;	
			this.moveDiag = 1.41;
			break;
			
		case "GOLD": //Regular hard to traverse
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
			
		case "PINK": //Start
			typeOfCell = '1';
			this.moveXY = 1; 
			this.moveDiag = 1.41;
			isS = true;
			break;
			
		case "PURPLE": //Goal
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
			return "WHITE";
		case '2':
			return "GOLD";
		case 'a':
			return "LIGHT_BLUE";
		case 'b':
			return "DARK_BLUE";
		case 's':
			return "PINK";
		case 'g':
			return "PURPLE";
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
	    	if(this.typeOfCell != 'a' && this.typeOfCell != 'b' && this.currColor != "GOLD" && !isS && !isG)
	    		this.currColor = "RED";
	    	repaint();
	    }
	    
	    public void squareInClosed() {
	    	if(this.typeOfCell != 'a' && this.typeOfCell != 'b' && this.currColor != "GOLD" && !isS && !isG)
	    		this.currColor = "GREEN";
	    	repaint();
	    }
	    
	    public void tracePath() {
	    	this.currColor = "YELLOW";
	    	repaint();
	    }
	    
	    public void isStart() {
	    	this.currColor = "PINK";
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
	        if(currColor == "WHITE") g.setColor(Color.white);					//regular unblocked cell
	        if(currColor == "GOLD") g.setColor(new Color(205,133, 63));			//Hard to traverse cell
	        if(currColor == "BLACK") g.setColor(Color.black);					//blocked cell
	        if(currColor == "LIGHT_BLUE") g.setColor(Color.blue.brighter());	//Regular unblocked with highway
	        if(currColor == "DARK_BLUE") g.setColor(Color.blue.darker());		//Hard to traverse with highway
	        if(currColor == "PINK") g.setColor(new Color(255,20,147));		    //START
	        if(currColor == "PURPLE") g.setColor(new Color(255,0,255));			//GOAL
	        if(currColor == "RED") g.setColor(Color.red);
	        if(currColor == "GREEN") g.setColor(Color.green);
	        if(currColor == "YELLOW") g.setColor(Color.yellow);
	        
	        g.fillRect(margin, margin, dim.height, dim.height);
	    }

		public void changeTypeOfCell(char c) {
			currColor = setColor(c);
			switch(currColor) {
			case "WHITE": //Regular unblocked
				typeOfCell = '1';
				this.moveXY = 1.0;	
				this.moveDiag = 1.41;
				break;
				
			case "GOLD": //Regular hard to traverse
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
				
			case "PINK": //Start
				typeOfCell = '1';
				this.moveXY = 1; 
				this.moveDiag = 1.41;
				isS = true;
				break;
				
			case "PURPLE": //Goal
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
