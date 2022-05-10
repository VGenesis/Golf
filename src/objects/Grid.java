package objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.Display;
import gfx.ImageUtils;
import math.Point;

public class Grid {
	public class Obstacle{
		public Point position;
		public Point size;
		private boolean isTerrain;
		
		public Obstacle(int posX, int posY, int sizeX, int sizeY) {
			this.position = new Point(posX, posY);
			this.size = new Point(sizeX, sizeY);
		}
	}
	
	public final int gridSize = 64;
	public Point gridDimensions;
	private ArrayList<Obstacle> obstacles;
	
	private BufferedImage[][] terrainTexture;
	private String tilesetPath = "/assets/sprites/sprTiles.png";
	
	public Grid() {
		this.gridDimensions = new Point(Display.screenWidth/ this.gridSize, Display.screenHeight / this.gridSize);
		this.obstacles = new ArrayList<Obstacle>();
	}
	
	public void init() {
		for(int i = -1; i < gridDimensions.x + 1; i++) {
			this.addTerrain(i, -1);
			this.addTerrain(i, 0);
			this.addTerrain(i, (int) gridDimensions.y - 1);
			this.addTerrain(i, (int) gridDimensions.y);
		}
		
		for(int i = 1; i < gridDimensions.y - 1; i++) {
			this.addTerrain(-1, i);
			this.addTerrain(0, i);
			this.addTerrain((int) gridDimensions.x - 1, i);
			this.addTerrain((int) gridDimensions.x, i);
		}
		
		this.getTerrain();
	}
	
	public void addTerrain(int posX, int posY) {
		Obstacle o = new Obstacle(posX, posY, 1, 1);
		o.isTerrain = true;
		this.obstacles.add(o);
	}
	
	public void addObstacle(int posX, int posY, int sizeX, int sizeY) {
		if(sizeX == sizeY) this.addBlock(posX, posY, sizeX);
		else this.obstacles.add(new Obstacle(posX, posY, sizeX, sizeY));
	}
	
	public void addBlock(int posX, int posY, int size) {
		this.obstacles.add(new Obstacle(posX, posY, size, size));
	}
	
	public void collides(Ball b, int framerate) {
		for(Obstacle o : obstacles) {
			b.collide(o, this);
		}
	}
	
	public void update(Ball b, int framerate) {
		this.collides(b, framerate);
	}
	
	private int getBitmapIndex(int x, int y) {
		boolean[][] res = new boolean[3][3];
		for(int i = 0; i < 3; i++) 
			for(int j = 0; j < 3; j++)
				res[i][j] = false;
		
		for(Obstacle o : obstacles) {
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					if(o.position.x ==  x + j && o.position.y == y + i) res[i + 1][j + 1] = true;
				}
			}
		}
		
		int bitmapIndex = 0;
		bitmapIndex += (res[0][0]? 1 : 0) + (res[0][1]? 2 : 0) + (res[0][2]? 4 : 0);
		bitmapIndex += (res[1][0]? 8 : 0) + (res[1][2]? 16 : 0);
		bitmapIndex += (res[2][0]? 32 : 0) + (res[2][1]? 64 : 0) + (res[2][2]? 128 : 0);
		
		return bitmapIndex;
	}
	
	//   1    2    4
	
	//   8    x   16
	
	//  32   64  128
	
	private int[][] bitmap = {
			{127,  63,  31, 159, 223},
			{111,  47,   7, 151, 215},
			{107,  41,   0, 148, 214},
			{235, 233, 224, 244, 246},
			{251, 249, 248, 252, 254}
	};
	
	
	private Point findTile(int index) {
		int i, j;
		for(i = 0; i < bitmap.length; i++) {
			for(j = 0; j < bitmap[0].length; j++) {
				if(index == bitmap[i][j]) return new Point(i, j);
			}
		}
		return null;
	}
	
	private void printTerrainBitmap() {
		for(int i = 0; i < this.gridDimensions.y; i++) {
			for(int j = 0; j < this.gridDimensions.x; j++) {
				System.out.printf("%3d ", this.getBitmapIndex(j, i));
			}
			System.out.println();
		}
	}
	
	private void getTerrain() {
		this.terrainTexture = new BufferedImage[(int) gridDimensions.x][(int) gridDimensions.y];
		ImageUtils iu = new ImageUtils();
		BufferedImage tileset = iu.loadImage(tilesetPath);
		
		for(int i = 0; i < this.gridDimensions.x; i++) {
			for(int j = 0; j < this.gridDimensions.y; j++) {
				int bitmapIndex = this.getBitmapIndex(i, j);
				Point tilePosition = this.findTile(bitmapIndex);
				if(tilePosition != null) {
					BufferedImage tile = tileset.getSubimage((int) tilePosition.y * gridSize, (int) tilePosition.x * gridSize, gridSize, gridSize);
					this.terrainTexture[i][j] = tile;
				}else {
					this.terrainTexture[i][j] = null;
				}
			}
		}
	}
	
	private void drawTerrain(Graphics g) {
		for(int i = 0; i < this.gridDimensions.y; i++) {
			for(int j = 0; j < this.gridDimensions.x; j++) {
				g.drawImage(terrainTexture[j][i], j * gridSize, i * gridSize, null);
			}
		}
	}
	
	public void draw(Graphics g) {
		drawTerrain(g);
	}
}
