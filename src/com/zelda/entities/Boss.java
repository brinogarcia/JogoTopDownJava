package com.zelda.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.zelda.main.Game;
import com.zelda.world.Astar;
import com.zelda.world.Camera;
import com.zelda.world.Vector2i;

public class Boss  extends Entity{
	
	private double speed = 0.4;

	private int maskx = 8, masky = 8, maskw = 10, maskh = 10;

	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;

	private BufferedImage[] sprites;

	private int life = 60;

	private boolean isDamaged = false;
	private int damageFrames = 40, damageCurrent = 0;

	public Boss(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(64, 48, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(80, 48, 16, 16);
	}

	public void tick() {
		maskx =4;
		masky=4;
		mwidth =8;
		mheight =8;
		if(!isColiddingWithPlayer()) {
			if(path == null || path.size() == 0) {
				Vector2i start = new Vector2i(((int)(x/16)),((int)(y/16)));
				Vector2i end = new Vector2i(((int)(Game.player.x/16)),((int)(Game.player.y/16)));
				path = Astar.findPath(Game.world, start, end);
			}
		}else {
			if(new Random().nextInt(100) < 50) {
				//Sound.hurtEffect.play();
				Game.player.life-=Game.rand.nextInt(4);
				Game.player.isDamage = true;
			}
		}
			if(new Random().nextInt(100) < 70)
				followPath(path);
			
			if(x % 16 == 0 && y % 16 == 0) {
				if(new Random().nextInt(100) < 10) {
					Vector2i start = new Vector2i(((int)(x/16)),((int)(y/16)));
					Vector2i end = new Vector2i(((int)(Game.player.x/16)),((int)(Game.player.y/16)));
					path = Astar.findPath(Game.world, start, end);
				}
			}
				
			
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
			
			collidingBullet();
			
			if(life <= 0) {
				destroySelf();
				return;
			}
			
			if(isDamaged) {
				this.damageCurrent++;
				if(this.damageCurrent == this.damageFrames) {
					this.damageCurrent = 0;
					this.isDamaged = false;
				}
			}
		
	}

	public void destroySelf() {
		Game.boss.remove(this);
		Game.entities.remove(this);
	}

	public void collidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if (Entity.isColidding(this, e)) {
				isDamaged = true;
				life--;
				Game.bullets.remove(i);
				return;
			}
		}
	}

	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, mwidth, mheight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

		return enemyCurrent.intersects(player);
	}

	public void render(Graphics g) {
		if (!isDamaged)
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		else
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		// g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y,
		// null);
		 //g.setColor(Color.blue);
		 //g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y,
		 //maskw,maskh);
	}

}
