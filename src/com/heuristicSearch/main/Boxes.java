package com.heuristicSearch.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

class Boxes extends JPanel {

    private static final long serialVersionUID = 1L;

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(8, 8);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(10, 10);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(12, 12);
    }

    @Override
    public void paintComponent(Graphics g) {
        int margin = 1;
        Dimension dim = getSize();
        super.paintComponent(g);
        g.setColor(Color.gray);
        g.fillRect(margin, margin, dim.width - margin * 2, 
           dim.height - margin * 2);
    }
}
