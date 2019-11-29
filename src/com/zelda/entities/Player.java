package com.zelda.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.zelda.main.Game;
import com.zelda.world.Camera;
import com.zelda.world.World;

public class Player extends Entity{
	
	public boolean right, up, left, down;
	public double speed = 0.7;
	
	private int frames = 0, maxFrames = 5, index =0,maxIndex =3; 
	private boolean moved = false;
	private BufferedImage[] rightPLayer;
	private BufferedImage[] leftPLayer;
	private BufferedImage stopPlayer;
	private BufferedImage upPlayer;
	
	public double life = 100, maxlife = 100;
	
	
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPLayer = new BufferedImage[4];
		leftPLayer =  new BufferedImage[4];
		//stopPlayer = new BufferedImage(16, 16, BufferedImage.TYPE_CUSTOM);
		
		
		stopPlayer = Game.spritesheet.getSprite(96, 0, 16, 16);
		upPlayer = Game.spritesheet.getSprite(112, 0, 16, 16);
		for(int i=0; i<4; i++) {
		rightPLayer[i] = Game.spritesheet.getSprite(32+(i *16), 0, 16, 16);
		leftPLayer[i] = Game.spritesheet.getSprite(64+(i *16), 16, 16, 16);
		}
	}

	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			x+=speed;
		
		}else if(left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			x-=speed;
		}
		
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			y -= speed;		
			
		}else if(down && World.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			y +=speed;
		}
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index == maxIndex)
					index = 0;
			}
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH) ;
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT) ;	
		
	}
	
	
	public void render(Graphics g) {
		if(up) {
			g.drawImage(upPlayer, this.getX()-Camera.x, this.getY()-Camera.y, null);	
		}else if(right) {
		g.drawImage(rightPLayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
		}else if(left) {
		g.drawImage(leftPLayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);	
		}else {
			g.drawImage(stopPlayer	, this.getX()-Camera.x, this.getY()-Camera.y, null);				
		}
		
		
	}
	
}
