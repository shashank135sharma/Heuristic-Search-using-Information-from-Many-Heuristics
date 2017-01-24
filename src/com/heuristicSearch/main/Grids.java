package com.heuristicSearch.main;

import java.awt.*;

class Grids extends Canvas {

private static final long serialVersionUID = 1L;
int width, height, rows, columns;

  Grids(int w, int h, int r, int c) {
    setSize(width = w, height = h);
    rows = r;
    columns = c;
  }
  
  public void paint(Graphics g) {
    int k;
    width = getSize().width;
    height = getSize().height;

    int htOfRow = height / (rows);
    for (k = 0; k < rows; k++)
      g.drawLine(0, k * htOfRow , width, k * htOfRow );

    int wdOfRow = width / (columns);
    for (k = 0; k < columns; k++)
      g.drawLine(k*wdOfRow , 0, k*wdOfRow , height);
  }
  
  public void Paint(Graphics g,int w, int h) {
  
	  int width = w / 10;
	  int height= h / 10;
	  for(int row=0;row<10;row++){
	      for(int col=0;col<10;col++){
	          g.drawRect(row*width,col*height,width,height);
	      }
	  }
  
  }

}