package com.heuristicSearch.main;
import java.awt.*;
/*
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;*/

import javax.swing.*;

//Visual for the GRID
//also trying to add Mouse Listener and Adapter so that it changes to gray when clicked but I failed at that
public class NewGrid{
	
		static int row = 120;
	    static int column = 160;
	    private JFrame frame = new JFrame();
	    private JPanel panel = new JPanel();
	    public static Square [][] squares = new Square [row][column];

	    //makes all white, then calls addObstacles but for now it's just random colors, we can change that later
	    public NewGrid() {
	        addsquares();
	        addObstacles();
	        panel.setLayout(new GridLayout(row, column));
	        frame.add(panel);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();
	        frame.setVisible(true);
	    }

	    private void addsquares() {
	        for (int i = 0; i < row; i++) {
	            for (int j = 0; j < column; j++) {
	                Square square = new Square(6);
	                squares[i][j] = square;
	                square.putClientProperty("row", row);
	                square.putClientProperty("column", column);
	                panel.add(square);
	            }
	        }
	    }
	    
	    //sets random colors to the squares in the squares array made by NewGrid
	    private void addObstacles() {
	        for (int i = 0; i < row; i++) {
	            for (int j = 0; j < column; j++) {
	            	squares[i][j].currColor = Square.setColor(random());
	            }
	        }
	    }
	    
	    //random number from 0 to 6
	    public int random(){
	    	int randomNum = 0 + (int)(Math.random() * 6);	
	    	
	    	return randomNum;
	    }
	    
	    //I failed at this LOL
	    /*
	    public void mouseClicked(MouseEvent e){
			int mx = e.getX();
			int my = e.getY();
			
		    System.out.print("HELLO I was clicked");
			
		}
		
		public void mouseReleased(MouseEvent e){
			
		}
		
		//this would simply say if you are in a certain box area then return true
		private boolean mouseOver(int mx, int my, int x, int y, int width, int height){
			if(mx > x && mx < x + width){
				if(my > y && my < y + height){
					return true;
				}else {
					return false;
				}
			}else{
				return false;
			}
		}*/

}
