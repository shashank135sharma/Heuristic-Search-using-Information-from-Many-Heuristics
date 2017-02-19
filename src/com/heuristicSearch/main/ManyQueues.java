//package com.heuristicSearch.main;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.PriorityQueue;
//
//public class ManyQueues {
//	static double weight = 1;
//	static String tracePathColor = "GOLD";
//	static String currentAlgo = "A*";
//	static double length;
//	static boolean isSimulation = false;
//	static double min = 9999;
//	
//	//compares fCost for A* algorithm and picks the next box accordingly
//	private static Comparator<Square> sorter = new Comparator<Square>(){
//		public int compare(Square b1, Square b2) {
//			if(b1.getFCost() < b2.getFCost()) return -1;
//			else if(b1.getFCost() > b2.getFCost()) return 1;
//			else if(b1.gCost > b2.gCost) return 1;
//			else return 1;
//		}
//	};
//	
//	public static double Key(Square s){
//		return s.gCost + (weight * s.hCost);
//	}
//	
//	public static void minKey(ArrayList<Square> o){
//		
//		double res = 9999;
//		for(int i = 0; i < o.size(); i++){
//			res = Key(o.get(i));
//			if(res <= min){ min = res; }
//		}
//
//	}
//	
//	public static int aStarFindPath(Square start, Square goal) {
//		start.gCost = 0;
//		goal.gCost = 9999;
//		int numNodesExpanded = 0;
//		start.parent = start;
//		PriorityQueue<Square> Opened = new PriorityQueue<Square>(sorter);
//		ArrayList<Square> closed = new ArrayList<Square>();
//		Opened.add(start);
//		start.isStart();
//		numNodesExpanded++;
//		while(min < 10000) {
//			Square current = Opened.remove();
//			if(current.typeOfCell != '2' && current.typeOfCell != 'b') {
//				current.squareInClosed();
//			}
//			current.updateGVal(start);
//			current.updateHVal(goal, weight);
//			
//			if(current.equals(goal) || Search.getDistance(goal, current) <= 1.41) {
//				//double length = 0;
//				start.isStart();
//				while(!current.parent.equals(start)) {
//					try {
//					    Thread.sleep(1);
//					    current.tracePath(tracePathColor);
//						length+=current.gCost;
//						current = current.parent;
//					} catch(InterruptedException ex) {
//					    Thread.currentThread().interrupt();
//					}
//					if(current.parent == start){
//						current.tracePath(tracePathColor);
//					}
//				}
///*				this.length = length;
//				if(!isSimulation) {
//					System.out.println("\nPath Found Using " + currentAlgo+"  With Length: "+length);
//				}*/
//				closed.clear();
//				Opened.clear();
//				break;
//			}
//			closed.add(current);
//			for(int i=0; i<8; i++) {
//				if(current.neighbors[i] != null) {
//					Square sPrime = current.neighbors[i];
//					if(!Search.doesClosedContain(sPrime, closed)) {
//						if(!Search.doesFringeContain(sPrime, Opened)) {
//							sPrime.gCost = Double.MAX_VALUE;
//							sPrime.getFCost();
//							Opened.remove(sPrime);
//							Opened.add(sPrime);
//							numNodesExpanded++;
//							sPrime.parent = null;
//						}
//						try {
//						    Thread.sleep(1);
//						} catch(InterruptedException ex) {
//						    Thread.currentThread().interrupt();
//						}
//						updateVertex(current,sPrime, start, goal, i, Opened);
//					}
//				}
//			}
//			if(current.typeOfCell != '2' && current.typeOfCell != 'b') {
//				current.squareInClosed();
//			}
//		}
//		return numNodesExpanded;
//	}
//	
//	private static void updateVertex(Square current, Square sPrime, Square start, Square goal, int index, PriorityQueue<Square> Opened) {
//		if(current.gCost + Search.getCVal(current, sPrime, index) < sPrime.gCost) {
//			sPrime.gCost = current.gCost + Search.getCVal(current,sPrime, index);
//			sPrime.getFCost();
//			sPrime.parent = current;
//			if(Search.doesFringeContain(sPrime, Opened)) {
//				Opened.remove(sPrime);
//			}
//			sPrime.updateGVal(current);
//			sPrime.updateHVal(goal, weight);
//			sPrime.getFCost();
//			if(sPrime.typeOfCell != '2' && sPrime.typeOfCell != 'b') {
//				sPrime.squareInClosed();
//			}
//			Opened.add(sPrime);
//		}
//	}
//}
