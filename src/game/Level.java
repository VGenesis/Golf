package game;

import java.awt.Graphics;
import java.io.File;
import java.io.FileReader;

import math.Point;
import objects.Ball;
import objects.Grid;
import objects.Hole;

public class Level {
	public Ball ball;
	public Grid grid;
	public Hole hole;
	
	public Level() {
		this.ball = new Ball(0, 0);
		this.grid = new Grid();
		this.hole = new Hole(0, 0);
	}
	
	public void loadLevel(String filepath) {
		try {
			File f = new File(filepath);
			if(f.exists() && f.canRead()) {
				FileReader reader = new FileReader(f);
				String rawData = "";
				int c;
				while((c = reader.read()) != -1) 
					rawData += (char) c;
				
				reader.close();

				String[] dataLines = rawData.split("\n");
				
				for(int i = 0; i < dataLines.length; i++) {
					String currentLine = dataLines[i].trim();
					String[] data;
					switch(i) {
					case 0:
						data = currentLine.split(" ");
						this.ball.position = new Point(Double.parseDouble(data[0]) * grid.gridSize, Double.parseDouble(data[1]) * grid.gridSize);
						break;
					case 1:
						data = currentLine.split(" ");
						this.hole.position = new Point(Double.parseDouble(data[0]) * grid.gridSize, Double.parseDouble(data[1]) * grid.gridSize);
						break;
					default:
						data = currentLine.split(" ");
						int[] obstacleData = new int[data.length];
						for(int j = 0; j < data.length; j++)
							obstacleData[j] = Integer.parseInt(data[j]);
						
						if(data.length == 4) this.grid.addObstacle(obstacleData[0], obstacleData[1], obstacleData[2], obstacleData[3]);
						else this.grid.addBlock(obstacleData[0], obstacleData[1], obstacleData[2]);
					}
				}
			}
			
			this.grid.init();
			this.ball.init();
			this.hole.init();
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void update(int framerate) {
		this.ball.update(framerate);
		this.grid.update(ball, framerate);
	}
	
	public void draw(Graphics g) {
		this.grid.draw(g);
		this.hole.draw(g);
		this.ball.draw(g);
	}
	
}
