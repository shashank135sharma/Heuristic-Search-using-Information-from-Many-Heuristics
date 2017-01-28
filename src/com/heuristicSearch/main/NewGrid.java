package com.heuristicSearch.main;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

//Visual for the GRID
public class NewGrid{
	
		static int row = 120;
	    static int column = 160;
	    private JFrame frame = new JFrame();
	    private JPanel panel = new JPanel();
	    public static Square [][] squares = new Square [row][column];

	    //makes the grid with specifications listed
	    public NewGrid() {
	        addsquares();
	        addHTTObstacles();
	        addBlockedObstacles();
	        addUBHighways();
	        addSandG();
	        panel.setLayout(new GridLayout(row, column));
	        frame.add(panel);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();
	        frame.setVisible(true);
	    }
	    //initiates to white squares
	    private void addsquares() {
	        for (int i = 0; i < row; i++) {
	            for (int j = 0; j < column; j++) {
	                Square square = new Square();
	                squares[i][j] = square;
	                square.putClientProperty("row", row);
	                square.putClientProperty("column", column);
	                panel.add(square);
	            }
	        }
	    }
	    
	    //sets random hard to traverse 31 x 31 squares in the squares array made by NewGrid
	    List<Vector2int> coordinates = new ArrayList<Vector2int>();
	    private void addHTTObstacles() {
	    	coordinates.clear();
	    	for (int i = 0 ; i <8; i++){
	    		Vector2int coordinate = new Vector2int(random(17,119), random(17,159));
	    		while(coordinate.getX() > 119-16 || coordinate.getY() > 159-16
	    				|| coordinate.getX() < 16 || coordinate.getY() < 16 || 
	    				vecInList(coordinates,coordinate) == true){
	    			coordinate = new Vector2int(random(17,119), random(17,159));
	    		}
	    		coordinates.add(coordinate);
	    	}
	    	int rx;
	    	int ry;
	    	for(Vector2int n : coordinates){
	    		rx = n.getX();
	    		ry = n.getY();
	    		int y = ry -16;
	    		int x = rx - 16;
	    		for (int i = 0; i < 31; i++) {
		            for (int j = 0; j < 31; j++) {
		            	if(randomPercent(50) == true){
		            		squares[x+i][y+j].currColor = Square.setColor('2');
		            	}
		            }
		        }	
	    	}
	    }
	    
	    //still needs to hit boundary and have length of 100 min and restart if it hits a path
	    //and need to check if going out of bounds on any turn or extension
	    //sets 4 random 20 cell unblocked highways in the grid from a boundary
	    private void addUBHighways(){
	    	coordinates.clear();
	    	int wall;
	    	for(int i = 0; i < 4; i++){
	    		wall = random(0, 3);
	    		if(wall == 0) { Vector2int coordinate = new Vector2int(random(1,119), 0); coordinates.add(coordinate);}
	    		if(wall == 1) { Vector2int coordinate = new Vector2int(119, random(1, 159)); coordinates.add(coordinate);}
	    		if(wall == 2) { Vector2int coordinate = new Vector2int(random(1,119), 159); coordinates.add(coordinate);}
	    		if(wall == 3) { Vector2int coordinate = new Vector2int(0, random(1,159)); coordinates.add(coordinate);}
	    	}
	    	int rx;
	    	int ry;
	    	for(Vector2int n : coordinates){
	    		rx = n.getX();
	    		ry = n.getY();
	    		System.out.println(rx +" , " + ry);
	    		//on north wall
	    		if(rx == 0){ 
		    		if(randomPercent(50) == true){//away from boundary
		    			riverHelper(rx,ry,'s',20,'a');
		    		}else{
		    			if(randomPercent(50) == true){//right
		    				riverHelper(rx,ry,'w',20,'a');
		    			}else{//left
		    				riverHelper(rx,ry,'e',20,'a');
		    			}
		    		}
	    		}
	    		//on east wall
	    		if(ry == 159){ 
	    			if(randomPercent(50) == true){//away from boundary
		    			riverHelper(rx,ry,'w',20,'a');
		    		}else{
		    			if(randomPercent(50) == true){//right
		    				riverHelper(rx,ry,'n',20,'a');
		    			}else{//left
		    				riverHelper(rx,ry,'s',20,'a');
		    			}
		    		}
	    		}
	    		//on south wall	
	    		if(rx == 119){ 
	    			if(randomPercent(50) == true){//away from boundary
		    			riverHelper(rx,ry,'n',20,'a');
		    		}else{
		    			if(randomPercent(50) == true){//right
		    				riverHelper(rx,ry,'e',20,'a');
		    			}else{//left
		    				riverHelper(rx,ry,'w',20,'a');
		    			}
		    		}
	    		}
	    		//on west wall
	    		if(ry == 0){ 
	    			if(randomPercent(50) == true){//away from boundary
		    			riverHelper(rx,ry,'e',20,'a');
		    		}else{
		    			if(randomPercent(50) == true){//right
		    				riverHelper(rx,ry,'s',20,'a');
		    			}else{//left
		    				riverHelper(rx,ry,'n',20,'a');
		    			}
		    		}
	    		}
	    	}
	    	
	    }
	    //moves as specified in the guidelines (20, the 60% same 20 or 20% perp 20 in direction specified)
	    private void riverHelper(int rx, int ry, char direction, int length, char type){
	    	if(direction == 'n' || direction == 'N'){
	    		travel(rx, ry, 'n', length, type);
		    	if(randomPercent(60) == true){
					travel(rx-20, ry, 'n', length, type);
				}else if(randomPercent(20) == true){
	    			if(randomPercent(50) == true){
	    				travel(rx-20, ry, 'e', length, type);
	    			}else{
	    				travel(rx-20, ry, 'w', length, type);
	    			}
				}
	    	}
	    	if(direction == 'e' || direction == 'E'){
	    		travel(rx, ry, 'e', length, type);
		    	if(randomPercent(60) == true){
					travel(rx, ry+20, 'e', length, type);
				}else if(randomPercent(20) == true){
	    			if(randomPercent(50) == true){
	    				travel(rx, ry+20, 'n', length, type);
	    			}else{
	    				travel(rx, ry+20, 's', length, type);
	    			}
				}
	    	}
	    	if(direction == 's' || direction == 'S'){
	    		travel(rx, ry, 's', length, type);
		    	if(randomPercent(60) == true){
					travel(rx+20, ry, 's', length, type);
				}else if(randomPercent(20) == true){
	    			if(randomPercent(50) == true){
	    				travel(rx+20, ry, 'e', length, type);
	    			}else{
	    				travel(rx+20, ry, 'w', length, type);
	    			}
				}
	    	}
	    	if(direction == 'w' || direction == 'W'){
	    		travel(rx, ry, 'w', length, type);
		    	if(randomPercent(60) == true){
					travel(rx, ry-20, 'w', length, type);
				}else if(randomPercent(20) == true){
	    			if(randomPercent(50) == true){
	    				travel(rx, ry-20, 'n', length, type);
	    			}else{
	    				travel(rx, ry-20, 's', length, type);
	    			}
				}
	    	}
	    }
	    //moves specified distance in direction and marks as type
	    private void travel(int x, int y, char direction, int length, char type){
		  if(direction == 'n' || direction == 'N'){
			  for (int i = 0; i < length; i++) {
					squares[x-i][y].currColor = Square.setColor(type);
			  }  
		  }else if(direction == 'e' || direction == 'E'){
			  for (int i = 0; i < length; i++) {
					squares[x][y+i].currColor = Square.setColor(type);
			  }  
		  }else if(direction == 's' || direction == 'S'){
			  for (int i = 0; i < length; i++) {
					squares[x+i][y].currColor = Square.setColor(type);
			  }  
		  }else if(direction == 'w' || direction == 'W'){
			  for (int i = 0; i < length; i++) {
					squares[x][y-i].currColor = Square.setColor(type);
			  }  
		  }
	  }
	  //adds 3840 blocks randomly with 12% chance is the best number to cover the grid
	  private void addBlockedObstacles(){
		  int count = 0;
		  while(count < 3840){
			  for(int i = 0; i < row; i++){
				  for(int j = 0; j < column; j++){
					  if(randomPercent(12) == true){
						  if(squares[i][j].currColor != "BLUE"){
							  if(count >= 3840) continue;
							  squares[i][j].currColor = Square.setColor('0');
							  count++;
						  }
					  }
				  }
			  }
			  System.out.println(count);
		  }
	  }
	  //marks start and goal squares, later put boxes on everything corresponding to the types and then run A*
	  //but still have to check square properties to see if it's river or HTT etc.
	  private void addSandG(){
		  coordinates.clear();
    		Vector2int coordinateS = new Vector2int(random(0,20), random(0,20));
    		Vector2int coordinateG = new Vector2int(random(0,119), random(0,159));
    		while(getDistance(coordinateS, coordinateG) <= 100){
    			coordinateG = new Vector2int(random(0,119), random(0,159));
    		}
    		
    		int rx;
	    	int ry;
    		rx = coordinateS.getX();
    		ry = coordinateS.getY();
        	squares[rx][ry].currColor = Square.setColor('s');
    		rx = coordinateG.getX();
    		ry = coordinateG.getY();
        	squares[rx][ry].currColor = Square.setColor('g');

	  }
	    
	  //checks if the vector is in the array list of Vectors
	  private boolean vecInList(List<Vector2int> list, Vector2int vector){
		for(Vector2int n : list){
			if(n.equals(vector)) return true;
		}
		return false;
	  }
	  //gets distance between 2 vectors
	  private double getDistance(Vector2int square, Vector2int goal){
		double dx = square.getX() - goal.getX();
		double dy = square.getY() - goal.getY();
		return Math.sqrt(dx*dx + dy*dy);
	 }
	    //makes whole grid white
	    public void clearGrid() {
	        for (int i = 0; i < row; i++) {
	            for (int j = 0; j < column; j++) {
	            	squares[i][j].currColor = Square.setColor('1');
	            }
	        }
	    }
	    
	    //random number in that range
	    public int random(int min, int max){
	    	int randomNum = min + (int)(Math.random() * max);
	    	
			return randomNum;
			
	    }
	    //returns probability of planding below specified percent
	    public boolean randomPercent(double percent){
	    	double per = percent/10;
	    	int ran = random(0, 10);
	    	if(ran <= per){
	    		return true;
	    	}else{
	    		return false;
	    	}
	    }
	    //didn't use but it's there
	    public char randomColor(){
	    	int randomNum = random(0,4);
	    	
	    	if(randomNum == 0) return '0';
			if(randomNum == 1) return '1';
			if(randomNum == 2) return '2';
			if(randomNum == 3) return 'a';
			if(randomNum == 4) return 'b';
			
			return '1';
	    }

}
