package com.zelda.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	public String[] options= {"Novo jogo", "Carregar Jogo", "Sair"};
	public int currentOption = 0;
	public int maxOption =  options.length -1;
	
	public boolean up, down, enter;
	
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
				Game.gameState = "NORMAL";
			
			}
		}
		
	}
	public void render(Graphics g) {
		Graphics2D g2 =  (Graphics2D) g;
		g2.setColor(new Color(0,0,0,100));
		g.fillRect(0,0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		
		g.setFont(new Font("arial", Font.BOLD,30));
		g.setColor(Color.white);
		g.drawString("SPACE WAR!",Game.WIDTH * Game.SCALE /2 - 100 , Game.HEIGHT * Game.SCALE / 2 - 180);
		
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
		if(options[currentOption] == options[2] ) {
			j -= 90;
			g.drawString(">",Game.WIDTH * Game.SCALE /2 - 180 , Game.HEIGHT * Game.SCALE / 2 - j);						
		}

		
	}
}
