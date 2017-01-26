package com.heuristicSearch.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Square extends JPanel{

	private static final long serialVersionUID = 1L;
	public int color;
	public String currColor;
	
	//default to white
	public Square(){
		this.color = 0;
		this.currColor = "WHITE";
	}
	
	//if you specified a color set that color
	public Square(int color){
		this.color = color;
		this.currColor = setColor(this.color);
	}
	
	//returns appropriate color of square to be set
	public static String setColor(int color){
		if(color == 0){
			return "WHITE";
		}
		if(color == 1){
			return "GRAY";
		}
		if(color == 2){
			return "DARK_GRAY";
		}
		if(color == 3){
			return "BLUE";
		}
		if(color == 4){
			return "BLACK";
		}
		if(color == 5){
			return "PINK";
		}
		if(color == 6){
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
	        if(currColor == "WHITE") g.setColor(Color.white);
	        if(currColor == "GRAY") g.setColor(Color.gray);
	        if(currColor == "DARK_GRAY") g.setColor(Color.darkGray);
	        if(currColor == "BLUE") g.setColor(Color.blue);
	        if(currColor == "BLACK") g.setColor(Color.black);
	        if(currColor == "PINK") g.setColor(Color.pink);
	        if(currColor == "RED") g.setColor(Color.red);
	        g.fillRect(margin, margin, dim.width-1, dim.height-1);
	    }
}
