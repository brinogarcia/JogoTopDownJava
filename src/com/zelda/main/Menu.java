package com.zelda.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.zelda.world.World;

public class Menu {
	
	public String[] options= {"Novo jogo", "Carregar Jogo", "Sair"};
	public int currentOption = 0;
	public int maxOption =  options.length -1;
	
	public boolean up, down, enter;
	
	public static boolean pause = false;
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		
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
			
			}else if(options[currentOption]== options[1]) {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}else if(options[currentOption]== options[2]) {
				System.exit(1);
			}
		}
		
	}
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i=0; i<spl.length;i++) {
			String[] spl2 = spl[i].split(":");
			switch (spl2[0]) 
			{
			case "level":
				World.restartGame("level"+spl2[1]+".png");
				Game.gameState = "NORMAL";
				pause = false;
				break;
			}
		}
		
	}
	
	public static String loadGame(int encode) {
		String line= "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine =  null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] transition = singleLine.split(":");
						char[] val = transition[1].toCharArray();
						transition[1] = "";
						for(int i=0; i< val.length; i++) {
							val[i] -= encode;
							transition[1]  += val[i];
						}
						line+=transition[0];
						line+=":";
						line+=transition[1];
						line+="/";
					}
				} catch (IOException e) {
					// TODO: handle exception
				}
			} catch (FileNotFoundException e) {
				// TODO: handle exception
			}
		}
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i=0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int j = 0; i< value.length; i++) {
				value[j] +=encode;
				current += value[j];
				
			}
			try {
				write.write(current);
				if(i < val1.length -1 ) {
					write.newLine();
				}
			} catch (IOException e) {
				// TODO: handle exception
			}
		}
		try {
			write.flush();
			write.close();
		} catch (IOException e) {
			// TODO: handle exception
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
