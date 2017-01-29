package com.heuristicSearch.main;

import javax.swing.JPanel;

class Boxes extends JPanel {

    private static final long serialVersionUID = 1L;
    public Vector2int square;
    public Boxes parent;
    public double fCost, gCost, hCost;
    
    //box objects on top of the grid GUI
    public Boxes(){
    	this.square = new Vector2int();
    	this.parent = new Boxes();
    }
    
    public Boxes(Vector2int square, Boxes parent, double gCost, double hCost){
    	this.square = square;
    	this.parent = parent;
    	this.gCost = gCost;
    	this.hCost = hCost;
    	this.fCost = this.gCost + this.hCost;
    }
    
    
    
}
