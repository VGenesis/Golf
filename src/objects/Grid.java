package objects;

import java.awt.Color;
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
	private int[][] bitmap;
	
	private String tilesetPath = "/assets/sprites/sprTiles.png";
	private String tilesetBitmapPath = "/assets/sprites/sprTilesBitmap.png";
	
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
		
		this.loadBitmap();
		this.getTerrain();
		
	}
	
	public void addTerrain(int posX, int posY) {
		Obstacle o = new Obstacle(posX, posY, 1, 1);
		o.isTerrain = true;
		this.obstacles.add(o);
	}
	
	public void addObstacle(int posX, int posY, int sizeX, int sizeY) {
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				this.addTerrain(posX + i, posY + j);
			}
		}
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
		bitmapIndex += (res[1][0]? 8 : 0) + (res[1][1]? 16 : 0) + (res[1][2]? 32 : 0);
		bitmapIndex += (res[2][0]? 64 : 0) + (res[2][1]? 128 : 0) + (res[2][2]? 256 : 0);
		
		return bitmapIndex;
	}

	private void loadBitmap() {
		ImageUtils iu = new ImageUtils();
		BufferedImage bitmapImage = iu.loadImage(tilesetBitmapPath);
		this.bitmap = new int[bitmapImage.getWidth() / 3][bitmapImage.getHeight() / 3];
		for(int j = 0; j < bitmapImage.getWidth() / 3; j++) {
			for(int i = 0; i < bitmapImage.getHeight() / 3; i++) {
				BufferedImage bitmapTile = bitmapImage.getSubimage(i * 3, j * 3, 3, 3);
				
				bitmap[i][j] = 0;
				
				Color c1 = new Color(bitmapTile.getRGB(0, 0));
				Color c2 = new Color(bitmapTile.getRGB(1, 0));
				Color c3 = new Color(bitmapTile.getRGB(2, 0));
				bitmap[i][j] += (c1.getRed() == c1.getGreen() && c1.getGreen() == c1.getBlue() && c1.getBlue() == 0)? 1 : 0;
				bitmap[i][j] += (c2.getRed() == c2.getGreen() && c2.getGreen() == c2.getBlue() && c2.getBlue() == 0)? 2 : 0;
				bitmap[i][j] += (c3.getRed() == c3.getGreen() && c3.getGreen() == c3.getBlue() && c3.getBlue() == 0)? 4 : 0;

				c1 = new Color(bitmapTile.getRGB(0, 1));
				c2 = new Color(bitmapTile.getRGB(1, 1));
				c3 = new Color(bitmapTile.getRGB(2, 1));
				bitmap[i][j] += (c1.getRed() == c1.getGreen() && c1.getGreen() == c1.getBlue() && c1.getBlue() == 0)? 8 : 0;
				bitmap[i][j] += (c2.getRed() == c2.getGreen() && c2.getGreen() == c2.getBlue() && c2.getBlue() == 0)? 16 : 0;
				bitmap[i][j] += (c3.getRed() == c3.getGreen() && c3.getGreen() == c3.getBlue() && c3.getBlue() == 0)? 32 : 0;

				c1 = new Color(bitmapTile.getRGB(0, 2));
				c2 = new Color(bitmapTile.getRGB(1, 2));
				c3 = new Color(bitmapTile.getRGB(2, 2));
				bitmap[i][j] += (c1.getRed() == c1.getGreen() && c1.getGreen() == c1.getBlue() && c1.getBlue() == 0)? 64 : 0;
				bitmap[i][j] += (c2.getRed() == c2.getGreen() && c2.getGreen() == c2.getBlue() && c2.getBlue() == 0)? 128 : 0;
				bitmap[i][j] += (c3.getRed() == c3.getGreen() && c3.getGreen() == c3.getBlue() && c3.getBlue() == 0)? 256 : 0;
			}
		}
	}
	
	private void printBitmap() {
		for(int i = 0; i < bitmap[0].length; i++) {
			for(int j = 0; j < bitmap.length; j++) {
				System.out.printf("%3d ", bitmap[j][i]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private Point findTile(int index) {
		int i, j;
		for(j = 0; j < bitmap.length; j++) {
			for(i = 0; i < bitmap[0].length; i++) {
				if(index == bitmap[i][j]) return new Point(j, i);
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
		
		this.loadBitmap();
		
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
