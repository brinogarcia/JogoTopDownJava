package com.zelda.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zelda.main.Game;

public class Npc extends Entity {
	
	public boolean showMessage = false;
	public String[] frases = new String[5];
	public boolean show = false;
	
	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = "Olá você está em um mundo sombrio, seja bem vindo!";
	
	}
	
	public void tick() {
		int xPLayer = Game.player.getX();
		int yPLayer = Game.player.getY();
		
		int xNpc = (int)x;
		int yNpc = (int)y;
		if(Math.abs(xPLayer-xNpc)<10 && 
				Math.abs(yPLayer-yNpc)<10) {
			if(show == false)
			showMessage = true;
			show = true;
		}else {
			showMessage = false;
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
		if(showMessage) {
			g.setColor(Color.blue);
			g.fillRect(9, 9, Game.WIDTH-18, Game.HEIGHT-18);
			g.setColor(Color.white);
			g.fillRect(10, 10, Game.WIDTH-20, Game.HEIGHT-20);
			
			g.setFont(new Font("Arial",Font.CENTER_BASELINE,9));
			g.setColor(Color.black);
			g.drawString(frases[0],(int) x,(int) y);
			
			g.drawString("Pessione X para fechar ou abrir a mensagem!",(int) x,(int) y+10);
		}
	}

}
