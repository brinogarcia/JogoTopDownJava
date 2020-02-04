package com.zelda.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.zelda.entities.Boss;
import com.zelda.entities.Bullet;
import com.zelda.entities.Enemy;
import com.zelda.entities.Entity;
import com.zelda.entities.LifePack;
import com.zelda.entities.Player;
import com.zelda.entities.ShootGun;
import com.zelda.entities.Weapon;
import com.zelda.graficos.Spritesheet;
import com.zelda.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

			for (int xx = 0; xx < map.getWidth(); xx++) {

				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FOOR);

					if (pixelAtual == 0xFF000000) {
						// Floor/ch�o
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FOOR);

					} else if (pixelAtual == 0xFFFFFFFF) {
						// Parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);

					} else if (pixelAtual == 0xFF50777F) {
						// PLayer
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);

					} else if (pixelAtual == 0xFFFF0000) {
						// Enemy
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);

					} else if (pixelAtual == 0xFFFF6A00) {
						// WEAPON
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));

					} else if (pixelAtual == 0xFF00FF21) {
						// LIFE
						LifePack pack = new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN);
						Game.entities.add(pack);

					} else if (pixelAtual == 0xFFFFD800) {
						// Bullet
						Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
					} else if (pixelAtual == 0xFF7C0E79) {
						// Boss
						Boss en = new Boss(xx * 16, yy * 16, 16, 16, Entity.BOSS_EN);
						Game.entities.add(en);
						Game.boss.add(en);

					} else if (pixelAtual == 0xFFFF5349) {
						// shootgun
						Game.entities.add(new ShootGun(xx * 16, yy * 16, 16, 16, Entity.SHOOTGUN_EN));

					}

				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isFree(int xnext, int ynext, int zplayer) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		if (!((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile || tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile
				|| tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile
				|| tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile))) {
			return true;
		}
		if (zplayer > 0) {
			return true;
		}
		return false;
	}

	public static void restartGame(String level) {
		Game.entities.clear();
		Game.boss.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.boss = new ArrayList<Boss>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		// Game Over!
		return;
	}

	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;

		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);

		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
	public static void renderMiniMap() {
		for(int i=0; i< Game.minimapaPixels.length; i++) {
			Game.minimapaPixels[i] = 0;
		}
		for(int xx=0; xx< WIDTH; xx++ ) {
			for(int yy=0; yy<HEIGHT;yy++) {
				if(tiles[xx+(yy*WIDTH)] instanceof WallTile) {
					Game.minimapaPixels[xx+(yy*WIDTH)] = 0xff0000;
				}
			}
		
		}
		int xPLayer = Game.player.getX()/16;
		int yPLayer = Game.player.getY()/16;
		Game.minimapaPixels[xPLayer + (yPLayer*WIDTH)] = 0xFFFFFF;
		
	} 
}
