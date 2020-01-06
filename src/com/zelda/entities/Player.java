package com.zelda.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.zelda.graficos.Spritesheet;
import com.zelda.main.Game;
import com.zelda.world.Camera;
import com.zelda.world.World;

public class Player extends Entity{
	
	public boolean right, up, left, down;
	public int right_dir = 0,left_dir = 1;
	public int dir = right_dir;
	public double speed = 0.7;
	
	private int frames = 0, maxFrames = 5, index =0,maxIndex =3; 
	private boolean moved = false;
	private BufferedImage[] rightPLayer;
	private BufferedImage[] leftPLayer;
	private BufferedImage playerDemage;
	private BufferedImage stopPlayer;
	private BufferedImage upPlayer;
	
	private boolean hasGun = false;
	
	public boolean shoot = false, mouseShoot = false;
	
	public int ammo = 0;
	
	public double life = 100, maxlife = 100;
	public int mx, my;
	
	public boolean isDamage = false;
	private int damageFrames = 0;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPLayer = new BufferedImage[4];
		leftPLayer =  new BufferedImage[4];
		playerDemage = Game.spritesheet.getSprite(32, 32, 16, 16);
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
			dir = right_dir;
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))){
			moved = true;
			y-=speed;
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed))){
			moved = true;
			y+=speed;
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
		
		checkCollisionLifePack();
		checkCollisionLifeAmmo();
		checkCollisionLifeGun();
		
		if(isDamage) {
			this.damageFrames++;
			if(this.damageFrames == 07) {
				this.damageFrames = 0;
				isDamage = false;
			}
		}
		
		if(shoot) {
		
			shoot = false;
			if( hasGun && ammo > 0) {
				ammo--;
			int dx = 0;
			int px = 0;
			int py = 6;

			if(right) {

				dx = 1;
				System.out.println(right);
			}else {
				
				dx = -1;}

			BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,0);
			Game.bullets.add(bullet);
			}
		}
		
		if(mouseShoot) {
			mouseShoot = false;
			if( hasGun && ammo > 0) {
				ammo--;
			double angle = 0;
			int px = 0;
			int py = 8;

			if(right) {

				px = 18;
				angle = Math.atan2(my - (this.getY()+py - Camera.y),mx - (this.getX()+px - Camera.x));
			}else {
				px = -8;
				angle = Math.atan2(my - (this.getY()+py - Camera.y),mx - (this.getX()+px - Camera.x));
				}
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			
			BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,dy);
			Game.bullets.add(bullet);
			}
			
		}
		
		if(life <= 0){
			Game.gameState = "GAME_OVER";
		}
		
		updateCamera();
		
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH) ;
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT) ;	
	}

	
	public void checkCollisionLifePack(){
		for(int i = 0; i< Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePack) {
				if(Entity.isColidding(this, atual)) {
					life+=10;
					if(life>100)
						life = 100;
					Game.entities.remove(atual);
				}
			}
		}
	}

	public void checkCollisionLifeGun(){
		for(int i = 0; i< Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {
					hasGun = true;
					System.out.println("Munição atual:" + ammo);
					Game.entities.remove(atual);
				}
			}
		}
	}
	public void checkCollisionLifeAmmo(){
		for(int i = 0; i< Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColidding(this, atual)) {
					ammo+= 20;
					System.out.println("Munição atual:" + ammo);
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if(!isDamage) {
		if(up) {
			g.drawImage(upPlayer, this.getX()-Camera.x, this.getY()-Camera.y, null);	
		}else if(right) {
		g.drawImage(rightPLayer[index], this.getX() -Camera.x, this.getY()-Camera.y, null);
		if(hasGun) {	
			g.drawImage(Entity.GUN_RIGHT, this.getX()+8 -Camera.x, this.getY()+1-Camera.y, null);
		}
		}else if(left) {
		g.drawImage(leftPLayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);	
		if(hasGun) {
			g.drawImage(Entity.GUN_LEFT, this.getX()-8- Camera.x, this.getY()+1-Camera.y, null);
		}
		}else {
			g.drawImage(stopPlayer	, this.getX()-Camera.x, this.getY()-Camera.y, null);				
			if(hasGun) {	
				g.drawImage(Entity.GUN_RIGHT, this.getX()- Camera.x, this.getY()+1-Camera.y, null);
			}
		}
		}else {
			g.drawImage(playerDemage, this.getX()-Camera.x, this.getY()-Camera.y, null);
		}
		
	}
	
}
