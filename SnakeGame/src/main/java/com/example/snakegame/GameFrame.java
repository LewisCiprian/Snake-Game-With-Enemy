package com.example.snakegame;

import javax.swing.JFrame;

public class GameFrame extends JFrame{

    // standard JFrame use
    GameFrame(){

        this.add(new GamePanel());
        // set title
        this.setTitle("Snake");
        // how to close
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // cannot be resizable
        this.setResizable(false);
        // take all the add component from the JFrame
        this.pack();
        this.setVisible(true);
        // set the location to middle
        this.setLocationRelativeTo(null);
    }

}
