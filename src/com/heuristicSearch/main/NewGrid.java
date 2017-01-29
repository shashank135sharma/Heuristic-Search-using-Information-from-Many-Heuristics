package com.heuristicSearch.main;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.*;

//Visual for the GRID
public class NewGrid{
	
		static int row = 120;
	    static int column = 160;
	    private JFrame frame = new JFrame();
	    private JPanel panel = new JPanel();
	    public static Square [][] squares = new Square [row][column];
	    Square sStart, sGoal;
	    
	    //makes the grid with specifications listed
	    public NewGrid() {
	        addsquares();
	        addHTTObstacles();
	        addBlockedObstacles();
	        //addUBHighways();
	        addSandG();
	        squares = setNeighbors(squares);
	        panel.setLayout(new GridLayout(row, column));
	        frame.add(panel);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();
	        frame.setVisible(true);
	        
	    }
	    
	    private Square[][] addUBHighways(Square[][] grid) {
	    	Square[][] copy = new Square[row][column];
	    	copy = copyGridInto(copy, grid);
	    	int random = (int) Math.floor(Math.random()*100);
	    	int startX, startY;
	    	if(random>75) {
	    		random = (int) Math.floor(Math.random()*50);
	    		if(random>40) { //Start from left wall and move right
	    			int startVal = (int)Math.random()*120;
	    			for(int i=0; i<20; i++) {
	    				copy[startVal][i].addHighway();
	    			}
	    			random = (int) Math.random()*100;
	    			if(random>40) { //Keep moving right
		    			for(int i=19; i<40; i++) {
		    				copy[startVal][i].addHighway();
		    			}
	    			} else if(random >20 && random<=40) { //move up
	    				for(int i=startVal; i>startVal-20; i--) {
	    					if(i>=0) {
	    						if(copy[startVal][19].hasHighway){
	    							return null;
	    						}
	    						copy[startVal][19].addHighway();
	    					}
	    				}
	    			} else { //move down
	    				for(int i=startVal; i<startVal+20; i++) {
	    					if(i<=120) {
	    						if(copy[startVal][19].hasHighway) {
	    							return null;
	    						}
	    						copy[startVal][19].addHighway();
	    					}
	    				}
	    			}
	    		} else if(random>20 && random<=40){ //left wall moving up
	    			
	    		} else { //left wall moving down
	    			
	    		}
	    	} else if(random>50 && random<=75) {
	    		
	    	} else if(random>25 && random<=50) {
	    		
	    	} else {
	    		
	    	}
	    	return copy;
	    }
	    
	    private Square[][] copyGridInto(Square[][] copy, Square[][] grid) {
	    	for(int i=0; i<grid.length; i++) {
	    		for(int j=0; j<grid[0].length; j++) {
	    			copy[i][j] = grid[i][j];
	    		}
	    	}
	    	return copy;
	    }
	    
		public NewGrid(File inputFile) throws FileNotFoundException {
			Scanner sc = new Scanner(inputFile);
			Square[] hardToTraverseCenters = new Square[8];
			squares = new Square[120][160];
			
			//Get start and goal coords
			StringTokenizer st = new StringTokenizer(sc.nextLine(), ",()\n");
			sStart = new Square('1', Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), true, false);
			st = new StringTokenizer(sc.nextLine(), ",()\n");
			sGoal = new Square('1', Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), false, true);
			
			//Get hardToTraverse cell centers
			for (int i=0 ;i<8; i++) {
				st = new StringTokenizer(sc.nextLine(), ",()\n");
				hardToTraverseCenters[i] = new Square('2', Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), false, false);
			}
			
			
			for(int i=0; i<120; i++) {
				for(int j=0; j<160; j++) {
					squares[i][j] = new Square(sc.next().charAt(0), i, j, false, false);
				}
			}
			squares[sStart.x][sStart.y] = sStart;
			squares[sGoal.x][sStart.y] = sGoal;
			
			//add hard to traverse areas
			for(int i=0; i<8; i++) {
				squares = createHardToTraverseWithCenter(hardToTraverseCenters[i].x, hardToTraverseCenters[i].y, squares);
			}
			squares = setNeighbors(squares);
			
		}
	    
	    //initiates to white squares
	    private void addsquares() {
	        for (int i = 0; i < row; i++) {
	            for (int j = 0; j < column; j++) {
	                Square square = new Square('1', i, j, false, false);
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
							  squares[i][j].typeOfCell = '0';
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
        	squares[rx][ry].isS = true;
        	squares[rx][ry].x = rx;
        	squares[rx][ry].x = rx;
        	System.out.println("Start coord: "+rx + " "+ ry);
        	this.sStart = squares[rx][ry];
    		rx = coordinateG.getX();
    		ry = coordinateG.getY();
        	squares[rx][ry].currColor = Square.setColor('g');
        	squares[rx][ry].isG = true;
        	squares[rx][ry].x = rx;
        	squares[rx][ry].x = rx;
        	System.out.println("Goal coord: "+rx + " "+ ry);
        	this.sGoal = squares[rx][ry];
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
	    
		//Creates the graph data structure by adding each square's neighbors
	    private Square[][] setNeighbors(Square[][] grid) {
	    	int counter = 0;
	    	for(int i=0; i<grid.length; i++) {
	    		for(int j=0; j<grid[0].length; j++) {
	    			counter = 0;
	    			for(int k=0; k<8; k++) {
	    				grid[i][j].neighbors[k] = null;
	    			}
	    			if(i-1>=0 && j-1>=0) {
	    				if(grid[i-1][j-1].typeOfCell != '0') {
	    					grid[i][j].neighbors[0] = grid[i-1][j-1];
		    				counter++;
	    				}
	    			}
	    			if(i-1>=0) {
	    				if(grid[i-1][j].typeOfCell != '0') {
	    					grid[i][j].neighbors[1] = grid[i-1][j];
		    				counter++;
	    				}
	    			}
	    			if(i-1>=0 && j+1 < grid[0].length) {
	    				if(grid[i-1][j+1].typeOfCell != '0') {
	    				grid[i][j].neighbors[2] = grid[i-1][j+1];
	    				counter++;
	    				}	
	    			}
	    			if( j+1 < grid[0].length) {
	    				if(grid[i][j+1].typeOfCell != '0') {
	    					grid[i][j].neighbors[3] = grid[i][j+1];
		    				counter++;
	    				}
	    			}
	    			if(i+1 < grid.length && j+1<grid[0].length) {
	    				if(grid[i+1][j+1].typeOfCell != '0') {
	    					grid[i][j].neighbors[4] = grid[i+1][j+1];
		    				counter++;
	    				}
	    			}
	    			if(i+1 < grid.length) {
	    				if(grid[i+1][j].typeOfCell != '0') {
	    					grid[i][j].neighbors[5] = grid[i+1][j];
		    				counter++;
	    				}
	    			}
	    			if(i+1<grid.length && j-1>=0) {
	    				if(grid[i+1][j-1].typeOfCell != '0') {
	    					grid[i][j].neighbors[6] = grid[i+1][j-1];
		    				counter++;
	    				}
	    			}
	    			if(j-1>=0) {
	    				if(grid[i][j-1].typeOfCell != '0') {
	    					grid[i][j].neighbors[3] = grid[i][j-1];
		    				counter++;
	    				}
	    			}
	    		}
	    	}
	    	return grid;
	    }

		private static Square[][] createHardToTraverseWithCenter(int x, int y, Square[][] cellArray) {
			int topX=x-16, topY = y-16, botX = x+14, botY = y+14;
			//making sure coordinate are in bounds
			if(topX < 0) {
				topX = 0;
			} else if(topX > 119) {
				topX = 119;
			}
			if(topY < 0) {
				topY = 0;
			} else if (topY > 159) {
				topY = 159;
			}
			if(botX < 0) {
				botX = 0;
			} else if(botX > 119) {
				botX = 119;
			}
			if(botY < 0) {
				botY = 0;
			} else if (botY > 159) {
				botY = 159;
			}
			
			//go through the square area and change to hard, if cell isnt start or end
			for(int i = topX; i<=botX; i++) {
				for(int j=topY; j<=botY; j++) {
					double random = Math.random();
					if(random > .5) {
						if(!cellArray[i][j].hasHighway) {
							cellArray[i][j].changeTypeOfCell('2');
						} else {
							cellArray[i][j].changeTypeOfCell('b');
						}
					}
				}
			}
			return cellArray;
		}

}
