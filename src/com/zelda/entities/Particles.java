package com.zelda.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.zelda.main.Game;
import com.zelda.world.Camera;

public class Particles extends Entity{

	public int lifetime =15 ;
	public int curLife = 0 ;
	
	public int spd =2;
	public double dx = 0;
	public double dy = 0;
	
	public Particles(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		dx = new Random().nextGaussian();
		dy = new Random().nextGaussian();
	}
	
	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		curLife++;
		if(lifetime == curLife) {
			Game.entities.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(this.getX() - Camera.x, this.getY()-Camera.y, width,height);
	}

}
