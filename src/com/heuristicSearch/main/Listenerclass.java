package com.heuristicSearch.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Listenerclass implements MouseListener{
	int x = 0, y = 0;

	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		//System.out.println(mx + ", " + my);
		//need to generalize this. right now it only does it for the first square.
		//if you click in the first square it'll return the color
		squareCoordinates(mx, my);
		//System.out.println(Square.getSquare(x, y).currColor);
	}
	
	private void squareCoordinates(int mx, int my){
		int i = 0, j = 0;
		while(i != (my-18)){
			if(i % 6 == 0){
				x++;
			}
			i++;
		}
		while(j != (mx-58)){
			if( j % 6 == 0){
				y++;
			}
			j++;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
