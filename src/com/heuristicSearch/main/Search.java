package com.heuristicSearch.main;

import java.awt.Canvas;

//import com.heuristicSearch.main.Window;
import com.heuristicSearch.main.NewGrid;

public class Search extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280, HEIGHT = 960;
	//Square[][] grid = new Square[120][160];
	
	public static void main(String args[]){
		new NewGrid();
	}

	public void run() {
		
	}

}
