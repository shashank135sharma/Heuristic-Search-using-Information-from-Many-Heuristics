package com.heuristicSearch.main;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.*;

//Visual for the GRID
public class NewGrid{
	
		static int row = 121;
	    static int column = 160;
	    private JFrame frame = new JFrame();
	    private JPanel panel = new JPanel();
	    public static Square [][] squares = new Square [row][column];
	    Square sStart, sGoal;
	    int numHighways = 0;
	    
	    //makes the grid with specifications listed
	    public NewGrid() {
		    addsquares();
	    	addHTTObstacles();
			addUBHighways(squares);
	    	addBlockedObstacles();
	    	addSandG();
	    	squares = setNeighbors(squares);
	    	panel.setLayout(new GridLayout(row, column));
	    	Listenerclass listener = new Listenerclass();
	        panel.addMouseListener(listener);
	        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	        frame.setResizable(false);
	    	frame.add(panel);
	    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	frame.pack();
	    	frame.setVisible(true);


	    }
	    
	    private Square[][] addUBHighways(Square[][] grid) {
	    	for(int i=0; i<4; i++) {
	    		while(!randomWalls(grid));
	    	}
	    	return grid;
	    }
	    
	    private boolean randomWalls(Square[][] grid) {
	    	int random;
	    	Random rand = new Random();
	    	random = rand.nextInt(100);
	    	
	    	if(random>75) { //Origin is left wall
	    		if(Wall(grid, "left")) {
	    		} else {
	    			return false;
	    		}
	    	} else if(random>50 && random<=75) {
	    		if(Wall(grid, "right")) {//Origin is right wall
	    		} else {
	    			return false;
	    		}
	    	} else if(random>25 && random<=50) {//Origin is top wall
	    		if(Wall(grid, "top")) {
	    		} else {
	    			return false;
	    		}
	    	} else {//Origin is bottom wall
	    		if(Wall(grid, "bottom")) {
	    		} else {
	    			return false;
	    		}
	    	}
	    	return true;
	    }
	    
	    
	    private static boolean isSquareBoundry(int x, int y) {
	    	if(x-1 < 0 || x+1>120 || y-1 < 0 || y+1 > 160) {
	    		return true;
	    	}
	    	return false;
	    }
	    
	    private void removeHighways(ArrayList<Square> currentPath, Square[][] grid) {
	    	for(Square sq: currentPath) {
	    		grid[sq.x][sq.y].removeHighway();
	    	}
	    }
	    
	    private boolean Wall(Square[][] grid, String side) {
	    	Random rand = new Random();
	    	int randX = 0;
	    	int randY = 0;
	    	if(side == "left" || side == "Left"){
	    		randX = rand.nextInt(120);
	    		randY = 0;
	    	}else if(side == "right" || side == "Right"){
	    		randX = rand.nextInt(120);
	    		randY = 159;
	    	}else if(side == "top" || side == "Top"){
	    		randX = 0;
	    		randY = rand.nextInt(160);
	    	}else if(side == "bottom" || side == "Bottom"){
	    		randX = 119;
	    		randY = rand.nextInt(160);
	    	}
	    	
	    	int random;
	    	ArrayList<Square> currentPath = new ArrayList<Square>();
	    	for(int i=0; i<21; i++) {
	    		if(!grid[randX][randY].hasHighway) {
		    		grid[randX][randY].addHighway();
		    		currentPath.add(grid[randX][randY]);
		    		if(side == "left" || side == "Left"){
			    		randY++;
			    	}else if(side == "right" || side == "Right"){
			    		randY--;
			    	}else if(side == "top" || side == "Top"){
			    		randX++;
			    	}else if(side == "bottom" || side == "Bottom"){
			    		randX--;
			    	}
	    		} else {
	    			removeHighways(currentPath,grid);
	    			return false;
	    		}
	    	}
	    	
	    	while(true) {
	    		random = rand.nextInt(10);
	    		if(random>=4) { //keep moving away for 20 blocks
	    			for(int i=0; i<20; i++) {
	    				if(isSquareBoundry(randX, randY)) {
	    					return true;
	    				}
			    		if(!grid[randX][randY].hasHighway) {
				    		grid[randX][randY].addHighway();
				    		currentPath.add(grid[randX][randY]);
				    		if(side == "left" || side == "Left"){
					    		randY++;
					    	}else if(side == "right" || side == "Right"){
					    		randY--;
					    	}else if(side == "top" || side == "Top"){
					    		randX++;
					    	}else if(side == "bottom" || side == "Bottom"){
					    		randX--;
					    	}
				    		
			    		} else {
			    			removeHighways(currentPath,grid);
			    			return false;
			    		}
	    			}
	    		} else if(random>=2 && random<4) { //move right
	    			for(int i=0; i<20; i++) {
	    				if(isSquareBoundry(randX, randY)) {
	    					return true;
	    				}
			    		if(!grid[randX][randY].hasHighway) {
				    		grid[randX][randY].addHighway();
				    		currentPath.add(grid[randX][randY]);
				    		if(side == "left" || side == "Left"){
					    		randX++;
					    	}else if(side == "right" || side == "Right"){
					    		randX--;
					    	}else if(side == "top" || side == "Top"){
					    		randY--;
					    	}else if(side == "bottom" || side == "Bottom"){
					    		randY++;
					    	}
			    		} else {
			    			removeHighways(currentPath,grid);
			    			return false;
			    		}
	    			}
	    		} else { //move left
	    			for(int i=0; i<20; i++) {
	    				if(isSquareBoundry(randX, randY)) {
	    					return true;
	    				}
			    		if(!grid[randX][randY].hasHighway) {
				    		grid[randX][randY].addHighway();
				    		currentPath.add(grid[randX][randY]);
				    		if(side == "left" || side == "Left"){
					    		randX--;
					    	}else if(side == "right" || side == "Right"){
					    		randX++;
					    	}else if(side == "top" || side == "Top"){
					    		randY++;
					    	}else if(side == "bottom" || side == "Bottom"){
					    		randY--;
					    	}
				    		
			    		} else {
			    			removeHighways(currentPath,grid);
			    			return false;
			    		}
	    			}
	    		}
	    	}
	    }
    
	    //if file is provided do this
		public NewGrid(File inputFile) throws FileNotFoundException {
			Scanner sc = new Scanner(inputFile);
			Square[] hardToTraverseCenters = new Square[8];
			squares = new Square[row][column];
			
			//Get start and goal coordinates
			StringTokenizer st = new StringTokenizer(sc.nextLine(), ",()\n");
			sStart = new Square('s', Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), true, false);
			st = new StringTokenizer(sc.nextLine(), ",()\n");
			sGoal = new Square('g', Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), false, true);
			
			//Get hardToTraverse cell centers
			for (int i=0 ;i<8; i++) {
				st = new StringTokenizer(sc.nextLine(), ",()\n");
				hardToTraverseCenters[i] = new Square('2', Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), false, false);
			}
			
			
			for(int i=0; i<row; i++) {
				for(int j=0; j<column; j++) {
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
			sc.close();
			
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
						  if(squares[i][j].typeOfCell != 'a' && squares[i][j].typeOfCell != 'b'){
							  if(count >= 3840) continue;
							  squares[i][j].currColor = Square.setColor('0');
							  squares[i][j].typeOfCell = '0';
							  count++;
						  }
					  }
				  }
			  }
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
        	this.sStart = squares[rx][ry];
    		rx = coordinateG.getX();
    		ry = coordinateG.getY();
        	squares[rx][ry].currColor = Square.setColor('g');
        	squares[rx][ry].isG = true;
        	squares[rx][ry].x = rx;
        	squares[rx][ry].x = rx;
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
	    //returns probability of landing below specified percent
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
	    	for(int i=0; i<grid.length; i++) {
	    		for(int j=0; j<grid[0].length; j++) {
	    			for(int k=0; k<8; k++) {
	    				grid[i][j].neighbors[k] = null;
	    			}
	    			if(i-1>=0 && j-1>=0) {
	    				if(grid[i-1][j-1].typeOfCell != '0') {
	    					grid[i][j].neighbors[0] = grid[i-1][j-1];
	    				}
	    			}
	    			if(i-1>=0) {
	    				if(grid[i-1][j].typeOfCell != '0') {
	    					grid[i][j].neighbors[1] = grid[i-1][j];
	    				}
	    			}
	    			if(i-1>=0 && j+1 < grid[0].length) {
	    				if(grid[i-1][j+1].typeOfCell != '0') {
	    				grid[i][j].neighbors[2] = grid[i-1][j+1];
	    				}	
	    			}
	    			if( j+1 < grid[0].length) {
	    				if(grid[i][j+1].typeOfCell != '0') {
	    					grid[i][j].neighbors[3] = grid[i][j+1];
	    				}
	    			}
	    			if(i+1 < grid.length && j+1<grid[0].length) {
	    				if(grid[i+1][j+1].typeOfCell != '0') {
	    					grid[i][j].neighbors[4] = grid[i+1][j+1];
	    				}
	    			}
	    			if(i+1 < grid.length) {
	    				if(grid[i+1][j].typeOfCell != '0') {
	    					grid[i][j].neighbors[5] = grid[i+1][j];
	    				}
	    			}
	    			if(i+1<grid.length && j-1>=0) {
	    				if(grid[i+1][j-1].typeOfCell != '0') {
	    					grid[i][j].neighbors[6] = grid[i+1][j-1];
	    				}
	    			}
	    			if(j-1>=0) {
	    				if(grid[i][j-1].typeOfCell != '0') {
	    					grid[i][j].neighbors[3] = grid[i][j-1];
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
