package com.zelda.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.zelda.world.World;

public class DimensionSelect {

	public String[] options= {"FullScreen", "240x160"};
	public int currentOption = 0;
	public int maxOption =  options.length -1;
	
	public boolean up, down, enter;
	
	public static boolean pause = false;


	
	
	public void tick() {
		
			
		if(up) {
			up = false;
			currentOption --;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if(down) {
			down = false;
			currentOption ++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if(enter) {
			enter = false;
			if(options[currentOption]== options[0]) {
				Game.checkFullScreen = true;
				
				
			
			}else if(options[currentOption]== options[1]) {
				World.restartGame("level1.png");
				Game.checkFullScreen = false;
				Game.gameState = "NORMAL";				
			}
		}
		
	}
	
	
	
	
	public void render(Graphics g) {
		Graphics2D g2 =  (Graphics2D) g;
		g2.setColor(new Color(0,0,0,100));
		g.fillRect(0,0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		
		g.setFont(Game.newFont);
		g.setColor(Color.white);
		g.drawString("SPACE WAR!",Game.WIDTH * Game.SCALE /2 - 220 , Game.HEIGHT * Game.SCALE / 2 - 80);
		
		// opções de menu;
		int j = 10;
		for(int i = 0; i< options.length; i++) {
		j -= 30;
		g.setFont(new Font("arial", Font.BOLD,20));
		g.setColor(Color.white);
		g.drawString(options[i],Game.WIDTH * Game.SCALE /2 - 100 , Game.HEIGHT * Game.SCALE / 2 - j);
				
		}
		j = 10;
		if(options[currentOption] == options[0] ) {
			j -= 30;
			g.drawString(">",Game.WIDTH * Game.SCALE /2 - 180 , Game.HEIGHT * Game.SCALE / 2 - j);						
		}
		if(options[currentOption] == options[1] ) {
			j -= 60;
			g.drawString(">",Game.WIDTH * Game.SCALE /2 - 180 , Game.HEIGHT * Game.SCALE / 2 - j);						
		}

		
	}
	
}
