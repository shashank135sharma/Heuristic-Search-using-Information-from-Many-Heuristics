package com.heuristicSearch.main;
import java.awt.*;
import javax.swing.*;

public class NewGrid {
	
	 	private int row = 120;
	    private int column = 160;
	    private JFrame frame = new JFrame();
	    private JPanel panel = new JPanel();

	    NewGrid() {
	        addboxes();
	        panel.setLayout(new GridLayout(row, column));
	        frame.add(panel);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();
	        frame.setVisible(true);
	    }

	    private void addboxes() {
	        for (int i = 0; i < row; i++) {
	            for (int j = 0; j < column; j++) {
	                Boxes box = new Boxes();
	                box.putClientProperty("row", row);
	                box.putClientProperty("column", column);
	                panel.add(box);
	            }
	        }
	    }

}
