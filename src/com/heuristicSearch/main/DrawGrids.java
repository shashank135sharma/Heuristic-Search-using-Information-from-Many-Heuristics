package com.heuristicSearch.main;

import java.awt.Frame;
import com.heuristicSearch.main.Grids;

public class DrawGrids extends Frame {

	private static final long serialVersionUID = 1L;

	DrawGrids(int w, int h, int rows, int columns) {
	    Grids grid = new Grids(w, h, rows, columns);
	    add(grid);
	  }
	
}