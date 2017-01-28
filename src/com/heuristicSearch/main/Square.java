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
	public String currColor;																	//for lb=db/db-lb use getAvg
	
	//default to white
	public Square(){
		this.color = '1';
		this.moveXY = 1.0;
		this.moveDiag = 1.41;
		this.currColor = "WHITE";
	}
	
	//if you specified a color set that color
	public Square(char color){
		this.color = color;
		this.currColor = setColor(this.color);
		if(this.currColor == "WHITE") 	   { this.moveXY = 1.0;	this.moveDiag = 1.41; }	//regular unblocked cell
        if(this.currColor == "GOLD")  	   { this.moveXY = 2.0;	this.moveDiag = 2.83; }	//Hard to traverse cell
        if(this.currColor == "BLACK")      { this.moveXY = 0.0;	this.moveDiag = 0.00; }	//blocked cell
        if(this.currColor == "LIGHT_BLUE") { this.moveXY = 0.25;this.moveDiag = 0.35; }	//Regular unblocked with highway
        if(this.currColor == "DARK_BLUE")  { this.moveXY = 0.5;	this.moveDiag = 0.71; }	//Hard to traverse with highway
        if(this.currColor == "PINK")        { this.moveXY = 0.0; this.moveDiag = 0.00; }	//START
        if(this.currColor == "PURPLE")     { this.moveXY = 0.0; this.moveDiag = 0.00; }	//GOAL
	}
	
	//returns appropriate color of square to be set
	public static String setColor(char color){
		if(color == '0') return "BLACK";
		if(color == '1') return "WHITE";
		if(color == '2') return "GOLD";
		if(color == 'a') return "LIGHT_BLUE";
		if(color == 'b') return "DARK_BLUE";
		if(color == 's') return "PINK";
		if(color == 'g') return "PURPLE";
		
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

	    //main GUI stuff to paint the squares to appropriate color and size on grid
	    public void paintComponent(Graphics g) {
	        int margin = 1;
	        Dimension dim = getSize();
	        super.paintComponent(g);
	        if(currColor == "WHITE") g.setColor(Color.white);					//regular unblocked cell
	        if(currColor == "GOLD") g.setColor(new Color(255,215,0));			//Hard to traverse cell
	        if(currColor == "BLACK") g.setColor(Color.black);					//blocked cell
	        if(currColor == "LIGHT_BLUE") g.setColor(Color.blue.brighter());	//Regular unblocked with highway
	        if(currColor == "DARK_BLUE") g.setColor(Color.blue.darker());		//Hard to traverse with highway
	        if(currColor == "PINK") g.setColor(new Color(255,20,147));						//START
	        if(currColor == "PURPLE") g.setColor(new Color(255,0,255));			//GOAL
	        g.fillRect(margin, margin, dim.width-1, dim.height-1);
	    }
}
