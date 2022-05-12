package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import input.Mouse;
import math.Point;

public class Display extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private Thread thread;
	private static String title = "Golf";
	public final static int screenWidth = 576;
	public final static int screenHeight = 960;
	public static final int FPS = 60;
	private static boolean running = false;
	
	private enum MouseStatus{
		BUSY,
		READY,
		HOLD
	}
	
	private Mouse mouse;
	public static Level level;
	private String level1path = "D:\\Tools\\Eclipse\\Golf\\src\\levels\\level1";
	
	public Display() {
		this.frame = new JFrame();
		
		Dimension size = new Dimension(screenWidth, screenHeight);
		this.setPreferredSize(size);
	}
	
	public static void main(String[] args) {
		Display display = new Display();
		display.frame.setTitle(title);
		display.frame.add(display);
		display.frame.pack();
		display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.frame.setLocationRelativeTo(null);
		display.frame.setResizable(false);
		display.frame.setVisible(true);
		display.start();
	}

	static int timeElapsed = 0;
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double nano = 1e9 / FPS;
		double delta = 0;
		int frames = 0;
		
		init();
		
		while(running) {
			long nowTime = System.nanoTime();
			delta += (nowTime - lastTime) / nano;
			lastTime = nowTime;
			while(delta >= 1.0) {
				update();
				draw();
				delta--;
				frames++;
			}
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				this.frame.setTitle(title + " | " + frames + "fps" + " | " + "Time: " + timeElapsed++ + "s");
				frames = 0;
			}
		}
	}
	
	public synchronized void start() {
		running = true;
		this.thread = new Thread(this, "Display");
		this.thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

	private void init() {
		this.mouse = new Mouse();
		this.addMouseListener(this.mouse);
		this.addMouseMotionListener(this.mouse);
		
		level = new Level();
		level.loadLevel(level1path);
		level.init();
	}

	private MouseStatus ms = MouseStatus.READY;
	private Point mousePos = new Point(0, 0), mouseTemp = new Point(0, 0);
	private void update() {
		level.update(FPS);
		
		if(level.ball.canHit()) {
			int click = mouse.getClick();
			if(click != 0) {
				mousePos = mouse.getPosition();
				if(ms == MouseStatus.READY) ms = MouseStatus.HOLD;
			}else {
				if(ms == MouseStatus.BUSY) ms = MouseStatus.READY;
				if(ms == MouseStatus.HOLD) {
					Point dist = new Point(mousePos.x - mouseTemp.x, mousePos.y - mouseTemp.y).scale(3);
					level.ball.setVelocity(dist.x, dist.y);
					ms = MouseStatus.BUSY;
				}
				mouseTemp = mouse.getPosition();
			}
			
		}
	}
	
	private void draw() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(new Color(0, 196, 0));
		g.fillRect(0, 0, screenWidth, screenHeight);
		
		level.draw(g);
		
		g.dispose();
		bs.show();
		
	}
}
