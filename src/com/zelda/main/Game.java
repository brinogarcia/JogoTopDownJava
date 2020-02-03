package com.zelda.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.zelda.entities.Boss;
import com.zelda.entities.BulletShoot;
import com.zelda.entities.Enemy;
import com.zelda.entities.Entity;
import com.zelda.entities.Player;
import com.zelda.graficos.Spritesheet;
import com.zelda.graficos.UI;
import com.zelda.world.Camera;
import com.zelda.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {
	

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	public static Player player;

	private int CUR_LEVEL = 1, MAX_LEVEL = 4;
	private BufferedImage image; 

	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Boss> boss;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	
	public static World world;
	
	
	public static Random rand;
	
	public UI ui;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf");
	public static Font newFont;
	
	
	public static String gameState = "MENU";
	
	private int framesGameOver = 0;
	
	private boolean showMessageGameOver = true;
	
	private boolean restartGame = false;
	
	public boolean saveGame =  false;
	
	public Menu menu;
	
	public int[] pixels;
	public BufferedImage lightMap;
	public int[] lightMapPixels;
	
	public int mx, my, xx, yy;
	
	public Game() {
		Sound.musicBackground.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		
		// inicializando objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			lightMap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lightMapPixels = new int[WIDTH*HEIGHT];
		lightMap.getRGB(0, 0,lightMap.getWidth(),lightMap.getHeight(),lightMapPixels,0,lightMap.getWidth());
		pixels =((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		boss = new ArrayList<Boss>();
		bullets =  new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player =  new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));	 
		entities.add(player);
		world = new World("/level1.png");	
		menu = new Menu();
		try {
			newFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(60f);
		} catch (FontFormatException e) {
	
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void initFrame() {
		frame = new JFrame();
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread =  new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
		
	}
	
	public void tick() {
		if(gameState == "NORMAL") {
			
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level", "vida"};
				int[] opt2 = {this.CUR_LEVEL,(int) player.life};
				Menu.saveGame(opt1, opt2, 10);				
			}
			this.restartGame = false;
		for(int i=0 ; i< entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
		if(enemies.size()==0 &&  (boss.size()==0||boss.isEmpty())  ) {
			CUR_LEVEL++;
			if(CUR_LEVEL > MAX_LEVEL) {
				CUR_LEVEL = 1;
			}
			String newWorld = "level" + CUR_LEVEL + ".png";
			World.restartGame(newWorld);
		}
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver ++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver) {
					this.showMessageGameOver = false;
				}else {
					this.showMessageGameOver = true;
				}
			}
			if(restartGame) {
				this.restartGame = false;
				this.gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		}else if(gameState == "MENU") {
			player.updateCamera();
			menu.tick();
		}
		
	}
	
	/*public void drawRectangleExample(int xOff, int yOff) {
		for(int xx =0; xx<32; xx++) {
			for(int yy =0; yy<32; yy++) {
				xOff = xx + xOff;
				yOff = yy + yOff;
				if(xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff >= HEIGHT)
					continue;
				
				pixels[xOff +(yOff * WIDTH)] = 0xff0000;
			}
		}
	}*/
	public void applyLight() {
		for(int xx=0; xx< Game.WIDTH;xx++) {
			for(int yy=0; yy<Game.HEIGHT; yy++) {
				if(lightMapPixels[xx+(yy*Game.WIDTH)] == 0xffffffff) {
				pixels[xx+(yy*Game.WIDTH)] = 0;
				}
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// Renderiza��o do jogo //
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		for(int i=0 ; i< entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i = 0; i< bullets.size(); i ++) {
			bullets.get(i).render(g);
		}
		
		applyLight();
		ui.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		//drawRectangleExample(xx, yy);
		g.drawImage(image, 0, 0, WIDTH* SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial", Font.BOLD,17));
		g.setColor(Color.white);
		g.drawString("Munição: "+ player.ammo,580, 20);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0,0, WIDTH * SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD,30));
			g.setColor(Color.white);
			g.drawString("GAME OVER ! ",250, 230);
			g.setFont(new Font("arial", Font.BOLD,30));
			g.setColor(Color.white);
			if(showMessageGameOver)
				g.drawString("Pressione ENTER para reiniciar ! ",100, 330);
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		/*Graphics2D g2 = (Graphics2D) g;	
		double angleMouse = Math.atan2(my-200+25, mx-200+75);
		g2.rotate(Math.toDegrees(angleMouse),200+25,200+35);
		g.setColor(Color.RED);
		g.fillRect(200, 200, 50, 70);
		*/
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfticks = 60.0;
		double ns = 1000000000 / amountOfticks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >=1) {
				tick();
				render();
				frames++;
				delta --;
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer+=1000;
			}

		}
		stop();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			player.jump = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || 
				e.getKeyCode() == KeyEvent.VK_D) {
			// movimento para direita
			player.right = true;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || 
				e.getKeyCode() == KeyEvent.VK_W) {
			// movimento para cima
			player.up = true;
			
			if(gameState == "MENU") {
				menu.up = true;
			}
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
			
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.options[0]="Continuar";
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F1){
			if(gameState == "NORMAL") {
			this.saveGame = true;
			}
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || 
				e.getKeyCode() == KeyEvent.VK_D) {
			// movimento para direita
			player.right = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || 
				e.getKeyCode() == KeyEvent.VK_W) {
			// movimento para cima
			player.up = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || 
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
			
		}
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX()/3);
		player.my = (e.getY()/3);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	this.mx = e.getX();
	this.my = e.getY();
		
	}

}
