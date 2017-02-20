package com.heuristicSearch.main;

import java.awt.Canvas;
import java.io.File;
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
	double weight2 = 1;
	String tracePathColor = "GOLD";
	String currentAlgo = "A*";
	double length;
	static boolean isSimulation = false;
	int currentHeuristic = 0;
	Square start, goal;
	//compares fCost for A* algorithm and picks the next box accordingly
	private Comparator<Square> sorter = new Comparator<Square>(){
		public int compare(Square b1, Square b2) {
			if(b1.getFCost() < b2.getFCost()) return -1;
			else if(b1.getFCost() > b2.getFCost()) return 1;
			else if(b1.gCost > b2.gCost) return 1;
			else return -1;
		}
	};
	
	private Comparator<Square> sorter2 = new Comparator<Square>() {
		public int compare(Square b1, Square b2) {
			if(b1.getFCost2(goal, weight) < b2.getFCost2(goal, weight)) return -1;
			else if(b1.getFCost2(goal, weight) > b2.getFCost2(goal, weight)) return 1;
			else if(b1.gCosts[b1.currentI] > b2.gCosts[b2.currentI]) return 1;
			else return -1;
		}
	};

	private Comparator<Square> sorter3 = new Comparator<Square>() {
		public int compare(Square b1, Square b2) {
			if(b1.getFCost3(goal, weight) < b2.getFCost3(goal, weight)) return -1;
			else if(b1.getFCost3(goal, weight) > b2.getFCost3(goal, weight)) return 1;
			else if(b1.gCost > b2.gCost) return 1;
			else return -1;
		}
	};
	
	
	
	public Square algorithmTwo(Square start, Square goal) throws InterruptedException {
		this.start = start;
		this.goal = goal;
		for(int i=0; i<NewGrid.squares.length;i++) {
			for(int j=0; j<NewGrid.squares[0].length; j++) {
				NewGrid.squares[i][j].sequentialSearchInitialize(5);
				for(int k=0; k<5; k++) {
					NewGrid.squares[i][j].gCosts[k] = 0;
					NewGrid.squares[i][j].hCosts[k] = 0;
					NewGrid.squares[i][j].fCosts[k] = 0;
					NewGrid.squares[i][j].bp[k] = null;

				}
			}
		}
		PriorityQueue<Square>[] open = new PriorityQueue[5];
		ArrayList<Square>[] closed = new ArrayList[5];
		start.isStart();
		
		for(int i=0; i<5; i++) {
			start.currentI = i;
			open[i] = new PriorityQueue<Square>(sorter2);
			closed[i] = new ArrayList<Square>();
			start.gCosts[i] = 0;
			goal.gCosts[i] = Double.MAX_VALUE;
			start.bp = null;
			goal.bp = null;
			start.updateHVal2(goal, weight, i);
			start.getFCost2(goal, weight);
			open[i].add(start);	
		}
		while(minKey(open[0], goal, 0) < Double.MAX_VALUE) {
			for(int i=0; i<5; i++) {
				if(minKey(open[i], goal, i) <= weight2*minKey(open[0], goal, 0)) {
					if(goal.gCosts[i] <= minKey(open[i], goal, i)) {
						if(goal.gCosts[i] < Double.MAX_VALUE) {
							goal.currentI = i;
							goal.bp[i].currentI = i;
							return goal.bp[i];
						}
					} else {
						if(!open[i].isEmpty()) {
							Square s = open[i].peek();
							expandStates(open[i], closed[i], s, i);
							s.currentI = i;
							closed[i].add(s);
							s.squareInClosed();
						} else {
							Square s = open[0].peek();
							expandStates(open[0], closed[0], s, 0);
							s.currentI = i;
							closed[0].add(s);
							s.squareInClosed();
						}
					}
				} else {
					if(goal.gCosts[0] <= minKey(open[0], goal, 0)) {
						if(goal.gCosts[0]< Double.MAX_VALUE) {
							goal.currentI = 0;
							goal.bp[0].currentI = 0;
							return goal.bp[0];
						} else {
							if(!open[0].isEmpty()) {
								Square s = open[0].peek();
								expandStates(open[0], closed[0], s, 0);
								s.currentI = i;
								closed[0].add(s);
								s.squareInClosed();
							}
						}
					}
				}
			}
		}
		System.out.println("Exit 3");
		
		return null;
	}
	
	public double minKey(PriorityQueue<Square> open, Square goal, int counter) {
		open.peek().getFCost2(goal, weight);
		return open.peek().fCosts[counter];
	}
	
	public double minKey2(PriorityQueue<Square> open, Square goal, int counter) {
		open.peek().currentI = counter;
		open.peek().getFCost3(goal, counter);
		return open.peek().fCost;
	}
	
	public void expandStates(PriorityQueue<Square> open, ArrayList<Square> closed, Square s, int counter){
		open.remove(s);
		for(int i=0; i<8; i++) {
			Square current = s.neighbors[i];
			if(current!=null) {
				if(!open.contains(current) && !closed.contains(current)) {
					if(!current.init) { 
						System.out.println("Current was not init");
					}
					if(current.gCosts!=null) {
						current.gCosts[counter] = Double.MAX_VALUE;
					} else {
						current.gCosts= new double[5];
						current.gCosts[counter] = Double.MAX_VALUE;
					}
					if(current.bp!=null) {
						current.bp[counter] = null;
					} else {
						current.bp= new Square[5];
						current.bp[counter] =null;
					}
				}
				if(current.gCosts[counter] > s.gCosts[counter] + getDistance(s, current)) {
					current.gCosts[counter] = s.gCosts[counter] + getDistance(s, current);
					if(current.bp !=null) {
						current.bp[counter]=s;
					}
					current.currentI = counter;
					if(!closed.contains(current)) {
						if(open.contains(current)) {
							open.remove(current);
						}
						current.squareInFringe();
						open.add(current);
					}
				}
			}
		}
	}
	
	
	public Square algorithmThree(Square start, Square goal) {
		this.goal = goal;
		this.start = start;
		
		for(int i=0; i<NewGrid.squares.length;i++) {
			for(int j=0; j<NewGrid.squares[0].length; j++) {
				NewGrid.squares[i][j].sequentialSearchInitialize(5);
				for(int k=0; k<5; k++) {
					NewGrid.squares[i][j].gCosts[k] = 0;
					NewGrid.squares[i][j].hCosts[k] = 0;
					NewGrid.squares[i][j].fCosts[k] = 0;
					NewGrid.squares[i][j].bp[k] = null;

				}
			}
		}
		
		ArrayList<Square> anchor = new ArrayList<Square>();
		ArrayList<Square> inad = new ArrayList<Square>();
		PriorityQueue<Square>[] open = new PriorityQueue[5];
		start.gCost = 0;
		goal.gCost = Double.MAX_VALUE;
		start.bp2 = null;
		goal.bp2 = null;
		
		for(int i=0; i<5; i++) {
			start.currentI = i;
			open[i] = new PriorityQueue<Square>(sorter3);
			start.updateHVal2(goal, weight, i);
			start.getFCost3(goal, weight);
			open[i].add(start);
		}
		
		while(this.minKey2(open[0], goal, 0) < Double.MAX_VALUE) {
			for(int i=0; i<5; i++) {
				int num = i;
				if(open[i].isEmpty()) {
					i=0;
				}
				if(minKey2(open[i], goal, i) <= weight2*minKey2(open[0], goal, 0)) {
					if(goal.gCost<= minKey2(open[i], goal, i)) {
						if(goal.gCost<Double.MAX_VALUE) {
							goal.currentI = i;
							return goal;
						}
					}else {
							Square s = open[i].peek();
							s.currentI = i;
							expandStates2(open, anchor, inad, s, i);
							inad.add(s);
							s.squareInClosed();
					}
				} else {
					goal.getFCost3(goal, weight);
					if(goal.gCost<=minKey2(open[0], goal, 0)) {
						if(goal.gCost< Double.MAX_VALUE) {
							goal.currentI = 0;
							return goal;
						}
					} else {
							Square s = open[0].peek();
							s.currentI = 0;
							expandStates2(open, anchor, inad, s, 0);
							anchor.add(s);
							s.squareInClosed();
					}
				}
				i=num;
			}
		}
		
		return null;
	}
	
	public double keys(Square s, Square goal,int i) {
		s.currentI = i;
		s.getFCost3(goal, weight);
		return s.fCost;
	}
	
	private boolean generated(PriorityQueue<Square>[] open, ArrayList<Square> anchor, ArrayList<Square> inad, Square s) {
		if(anchor.contains(s) || inad.contains(s)) {
			return true;
		}
		
		for(int i=0; i<5; i++) {
			if(open[i].contains(s)) {
				return true;
			}
		}
		return false;
	}
	
	public void expandStates2(PriorityQueue<Square>[] open, ArrayList<Square> anchor, ArrayList<Square> inad, Square s, int counter){
		for(int i=0; i<5; i++) {
			open[i].remove(s);
		}
		
		for(int i=0; i<8; i++) {
			Square current = s.neighbors[i];
			
			if(current!=null) {
				if(!generated(open, anchor, inad, s)) {
					current.gCost = Double.MAX_VALUE;
					current.bp2 = null;
				} 
				if(current.gCost > s.gCost + getDistance(s, current)) {
					current.gCost = s.gCost + getDistance(s, current);
					current.bp2 = s;
					
					if(!anchor.contains(current)) {
						if(open[0].contains(current)) {
							open[0].remove(current);
						}
						current.getFCost3(goal, weight);
						current.currentI = 0;
						open[0].add(current);
						current.squareInFringe();
						if(!inad.contains(current)) {
							for(int j=1; j<5; j++) {
								s.currentI = j;
								if(keys(current,this.goal, j) <= weight2* keys(current,this.goal, 0)) {
									current.currentI = j;
									current.getFCost3(goal, weight);
									if(open[j].contains(current)) {
										open[j].remove(current);
									}
									open[j].add(current);
									current.squareInFringe();
								}
							}
						}
					}
				}
			}
		}
		
		
		
	}
	
	private void findLongestPath(Square goal) {
		System.out.println("Finding path");
		Square maxSquare = goal.bp2;
		
		
		while(maxSquare!=null) {
			System.out.println("Current Block: "+maxSquare.x + " "+maxSquare.y);
			maxSquare.tracePath("GOLD");
			maxSquare = maxSquare.bp2;
		}
	}
	
	
	public int aStarFindPath(Square start, Square goal) {
		start.gCost = 0;
		int numNodesExpanded = 0;
		start.parent = start;
		PriorityQueue<Square> fringe = new PriorityQueue<Square>(sorter);
		ArrayList<Square> closed = new ArrayList<Square>();
		fringe.add(start);
		start.isStart();
		numNodesExpanded++;
		while(!fringe.isEmpty()) {
			Square current = fringe.remove();
			if(current.typeOfCell != '2' && current.typeOfCell != 'b') {
				current.squareInFringe();
			}
			current.updateHVal(goal, weight, currentHeuristic);
			current.updateGVal(start);
			
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
				this.length = length;
				if(!isSimulation) {
					System.out.println("\nPath Found Using " + currentAlgo+"  With Length: "+length);
				}
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
							numNodesExpanded++;
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
		return numNodesExpanded;
	}
	
	private void updateVertex(Square current, Square sPrime, Square start, Square goal, int index, PriorityQueue<Square> fringe) {
		if(current.gCost + getCVal(current, sPrime, index) < sPrime.gCost) {
			sPrime.gCost = current.gCost + getCVal(current,sPrime, index);
			sPrime.getFCost();
			sPrime.parent = current;
			if(doesFringeContain(sPrime, fringe)) {
				fringe.remove(sPrime);
			}
			sPrime.updateHVal(goal, weight, currentHeuristic);
			sPrime.updateGVal(current);
			sPrime.getFCost();
			if(sPrime.typeOfCell != '2' && sPrime.typeOfCell != 'b') {
				sPrime.squareInFringe();
			}
			fringe.add(sPrime);
		}
	}
	
	public static double getCVal(Square current, Square sPrime, int index) {
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
	
	public static boolean doesClosedContain(Square curr, ArrayList<Square> closed) {
		for(Square sq: closed) {
			if(sq.equals(curr)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean doesFringeContain(Square curr, PriorityQueue<Square> fringe) {
		for(Square sq: fringe) {
			if(sq.equals(curr)) {
				return true;
			}
		}
		return false;
	}
	
	//mainly used for hCost it gets the distance from you to goal box using Pythagorean Theorem
	public static double getDistance(Square square1, Square goal){
		double dx = Math.abs(square1.x - goal.x);
		double dy = Math.abs(square1.y - goal.y);
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	private void tracePath2(Square sq) {
		while(sq!= null) {
			sq.tracePath("GOLD");
			if(sq.bp==null || sq.bp[sq.currentI].equals(start)){
				System.out.println("Path completed");
				return;
			}
			sq=sq.bp[sq.currentI];
		}
		System.out.println("Path completed");
	}
	
	//main call and search method which creates a grid behind the boxes.
	//I haven't called the A* algorithm yet, just created a GUI of grids
	//But I am confident it works I looked at many tutorials on YouTube
	public Search() throws InvocationTargetException, InterruptedException, FileNotFoundException{
		System.out.print("Use a: (1) randomly generated grid\t(2)An input file?: ");
		Scanner sc = new Scanner(System.in);
		int option = sc.nextInt();
		switch(option) {
		case 1:
			grid = new NewGrid();
			System.out.println("Start: "+grid.sStart.x + " " + grid.sStart.y);
			System.out.println("End: "+grid.sGoal.x + " " + grid.sGoal.y);
			break;
		case 2:
			System.out.print("Enter file name: ");
			String name = sc.next();
			File inputFile = new File(name);
			if(inputFile.exists() && inputFile.isDirectory() ) {
				grid = new NewGrid(inputFile);
			} else {
				System.out.println("File does not exist. Creating random grid.");
				grid = new NewGrid();
			}
			break;
		}
		
		System.out.print("\nWhat type of search would you like to run?\n(1) A*\t(2) Weighted A*\t(3) Uniform Cost Search\t(4) Sequential A* \t(5) Two-Queues A*: ");
		option = sc.nextInt();

		switch(option){
		case 1:
			this.weight = 1;
			System.out.println("\nRegular A* Search started" );
			Thread.sleep(100);
			aStarFindPath(grid.sStart, grid.sGoal);

			break;
		case 2:
			System.out.print("Enter weight for Weighted A*: ");
			this.weight = sc.nextDouble();
			currentAlgo = "Weighted A*";
			System.out.println("\nWeighted A* Search with weight "+weight+" started" );
			Thread.sleep(1000);
			aStarFindPath(grid.sStart, grid.sGoal);

			currentAlgo = "Regular A*";
			weight = 1;
			tracePathColor = "WHITE";
			Thread.sleep(100);
			aStarFindPath(grid.sStart, grid.sGoal);
			break;
		case 3:
			weight = 0;
			currentAlgo = "Uniform Cost Search";
			System.out.println("\nUniform Cost Search Started" );
			Thread.sleep(1000);
			aStarFindPath(grid.sStart, grid.sGoal);
			break;
		case 4:
			System.out.print("Enter weight for Sequential A*: ");
			this.weight = sc.nextDouble();
			System.out.print("Enter weight2 for Weighted A*: ");
			this.weight2 = sc.nextDouble();
			System.out.println("\nSequential A* Search started" );
			Thread.sleep(100);
			Square path = algorithmTwo(grid.sStart, grid.sGoal);
			tracePath2(path);
			break;
		case 5:
			System.out.print("Enter weight for Two-Queues A*: ");
			this.weight = sc.nextDouble();
			System.out.print("Enter weight2 for Two-Queues A*: ");
			this.weight2 = sc.nextDouble();
			System.out.println("\nTwo-Queues A* Search started" );
			Square path2 = algorithmThree(grid.sStart, grid.sGoal);
			findLongestPath(path2);
			aStarFindPath(grid.sStart, grid.sGoal);
			System.out.println("Two-Queues A* Search Completed");
			
			break;
		default:
			System.out.println("Please enter a valid case");
		}
		
		System.out.print("\n Would you like to get cost information about a cell? (Y/N): ");
		char ch = sc.next().toLowerCase().charAt(0);
		switch(ch) {
		case 'y':
			while(ch == 'y') {
				System.out.print("\nEnter X and Y value for cell in format X Y (ex: 112 114):  ");
				int getX = sc.nextInt();
				int getY = sc.nextInt();
				Square curr = NewGrid.squares[getX][getY];
				if(option == 1 || option == 2 || option == 3) {
					System.out.println("\n("+getX+","+getY+") G Cost: "+curr.gCost+" HCost: "+curr.hCost+" FCost: "+curr.getFCost());
					System.out.print("Get information about more cells?(Y/N): ");
					ch = sc.next().toLowerCase().charAt(0);
				} else if(option == 4) {
					curr.currentI = 0;
					System.out.println("\n("+getX+","+getY+") G Cost: "+curr.gCosts[0]+" HCost: "+curr.hCosts[0]+" FCost: "+curr.getFCost2(goal, weight));
					System.out.print("Get information about more cells?(Y/N): ");
				} else if(option == 5) {
					curr.currentI = 0;
					System.out.println("\n("+getX+","+getY+") G Cost: "+curr.gCost+" HCost: "+curr.hCosts[0]+" FCost: "+curr.getFCost3(goal, weight));
					System.out.print("Get information about more cells?(Y/N): ");
				}
					
			}
			System.out.println("\nAlright. Bye!");
			System.exit(0);
			break;
		case 'n':
			System.out.println("\nAlright. Bye!");
			System.exit(0);
			break;
		}
		sc.close();
	}
	
	public Search(int numOfMaps, int numOfStarts) {
		//Regular A*
		ArrayList<Long> regularDuration = new ArrayList<Long>();
		ArrayList<Long> weightedDuration = new ArrayList<Long>();
		ArrayList<Long> uniformDuration = new ArrayList<Long>();
		
		ArrayList<Integer> regularExpanded = new ArrayList<Integer>();
		ArrayList<Integer> weightedExpanded = new ArrayList<Integer>();
		ArrayList<Integer> uniformExpanded = new ArrayList<Integer>();

		
		ArrayList<Double> regularLength = new ArrayList<Double>();
		ArrayList<Double> weightedLength = new ArrayList<Double>();
		ArrayList<Double> uniformLength = new ArrayList<Double>();

		
		int counter = 0;
		
		for(int i=0; i<5; i++) {
			grid = new NewGrid();
			for(int j=0 ;j<10; j++) {
				grid.addSandG();
				System.out.println("Counter: "+counter);
				counter++;
				//regular a*
				this.weight = 1;
				regularExpanded.add(aStarFindPath(grid.sStart, grid.sGoal));
				regularLength.add(this.length);
				System.out.println("Counter: "+counter);
				counter++;

				//weighted a*
				this.weight = 1.2;
				weightedExpanded.add(aStarFindPath(grid.sStart, grid.sGoal));
				weightedLength.add(this.length);
				System.out.println("Counter: "+counter);
				counter++;

				//uniform cost search
				this.weight = 0;
				uniformExpanded.add(aStarFindPath(grid.sStart, grid.sGoal));
				weightedLength.add(this.length);
			}
		}
		System.out.println("\nAverage run time for Regular: "+getLongAvg(regularDuration));		
		System.out.println("Average run time for Weighted: "+getLongAvg(weightedDuration));
		System.out.println("Average run time for Uniform: "+getLongAvg(uniformDuration));
		
		System.out.println("\nAverage Expanded for Regular: "+getIntegerAvg(regularExpanded));		
		System.out.println("Average Expanded for Weighted: "+getIntegerAvg(weightedExpanded));
		System.out.println("Average Expanded for Uniform: "+getIntegerAvg(uniformExpanded));

		System.out.println("\nAverage length for Regular: "+getDoubleAvg(regularLength));		
		System.out.println("Average length for Weighted: "+getDoubleAvg(weightedLength));
		System.out.println("Average length for Uniform: "+getDoubleAvg(uniformLength));

	}
	
	public long getLongAvg(ArrayList<Long> list) {
		long sum = 0;
		for(int i=0; i<list.size(); i++) {
			sum+=list.get(i);
		}
		return sum/list.size();
	}
	
	public double getDoubleAvg(ArrayList<Double> list) {
		double sum = 0;
		for(int i=0; i<list.size(); i++) {
			sum+=list.get(i);
		}
		return sum/list.size();
	}
	
	public double getIntegerAvg(ArrayList<Integer> list) {
		double sum = 0;
		for(int i=0; i<list.size(); i++) {
			sum+=list.get(i);
		}
		return (sum*1.0)/list.size();
	}


	
	public static void main(String args[]) throws InvocationTargetException, InterruptedException, FileNotFoundException{
//		isSimulation = true;
//		new Search(5,10);
		new Search();
	}
	
		
	//you have to keep this method because it's a runnable implemented
	public void run() {
		
	}

}
