package com.heuristicSearch.main;

import java.awt.Canvas;
import com.heuristicSearch.main.Window;

public class Search extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;

	public Search(){
		new Window(WIDTH, HEIGHT, "Heuristic Search 8-way", this);
		new DrawGrids(200, 200, 2, 10).setVisible(true);
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String args[]){
		new Search();
	}

}
