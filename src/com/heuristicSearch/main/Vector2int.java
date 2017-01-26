package com.heuristicSearch.main;

public class Vector2int {
	
	private int x, y;
	
	//all the constructors for this vector class which has 2 integers, that's y I called it 2int
	//also includes some vector math (used by A* to check fCost)
	public void set (int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2int(){
		set(0,0);
	}
	
	public Vector2int(int x, int y){
		set(x,y);
	}
	
	public Vector2int(Vector2int vector){
		set(vector.x, vector.y);
	}
	public void add(Vector2int vector){
		this.x += vector.x;
		this.y += vector.y;
	}
	
	public void subtract(Vector2int vector){
		this.x -= vector.x;
		this.y -= vector.y;
	}
	
	public boolean equals(Object object){
		if(!(object instanceof Vector2int)) return false;
		Vector2int vec = (Vector2int) object;
		if(vec.getX() == this.getX() && vec.getY() == this.getY()) return true;
		return false;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Vector2int setX(int x){
		this.x = x;
		return this;
	}
	public Vector2int  setY(int y){
		this.y = y;
		return this;
	}
	
	
}
