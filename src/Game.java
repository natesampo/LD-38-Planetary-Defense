import java.applet.AudioClip;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
public class Game extends Canvas implements Runnable {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int WIDTH = (int) screenSize.getWidth();
	int HEIGHT = (int) screenSize.getHeight();
	int stars;
	ArrayList<ArrayList<Integer>> star;
	private static final long serialVersionUID = 2523884061368394963L;
	public MouseInput mouseInput;
	Planet planet;
	private Thread thread;
	private boolean running = false;
	public Handler handler;
	public Random random;
	private KeyInput keyInput;
	public boolean debug = false, menu = true, shipsleft = true, end=false, win = false, lose = false;
	private Window w;
	public Game() {
		this.stars = 50;
		this.star = new ArrayList<ArrayList<Integer>>();
		this.random = new Random();
		this.planet = new Planet(WIDTH/2 - 32, HEIGHT/2 - 32, 0, 0, ID.Planet);
		this.mouseInput = new MouseInput();
		this.keyInput = new KeyInput(this);
		this.addMouseMotionListener(this.mouseInput);
		this.addKeyListener(this.keyInput);
		this.handler = new Handler();
		for(int i=0;i<=WIDTH;i+=1){
			ArrayList<Integer> d = new ArrayList<Integer>();
			this.star.add(d);
			for(int q=0;q<=HEIGHT;q+=1){
				this.star.get(i).add(0);
			}
		}
		for(int i=0;i<=this.stars;i+=1){
			int w = this.random.nextInt(WIDTH - 64);
			int h = this.random.nextInt(HEIGHT - 64);
			this.star.get(w).set(h, 1);
		}
		Window w = new Window(WIDTH, HEIGHT, "Planet Defender", this);
		this.w = w;
		lvl1();
	}
	
	public void lvl1() {
		for(int i=5;i<25;i+=1){
			addShip(i*150, 0);
		}
		lvl2();
	}
	
	public void lvl2() {
		for(int i=20;i<30;i+=1){
			addShip(i*300, 1);
		}
		lvl3();
	}
	
	public void lvl3() {
		for(int i=25;i<45;i+=1){
			addShip(i*230, 2);
		}
		lvl4();
	}
	
	public void lvl4() {
		for(int i=90;i<120;i+=1){
			addShip(i*70, this.random.nextInt(3));
		}
		addShip(20000, 3);
	}
	
	public void lvl5() {
		this.end = true;
		for(int i=5;i<50;i+=1){
			addShip(i*50, this.random.nextInt(5));
		}
	}
	
	public void newGame() {
		w.frame.dispose();
		new Game();
		this.handler = null;
		this.planet = null;
	}
	
	public void addShip(int wait, int shipnum) {
		int r = this.random.nextInt(4);
		if(r==0){
			Ship ship = new Ship(this.random.nextInt(WIDTH), -wait-200, 0, 0, shipnum, ID.Ship);
			this.handler.addObject(ship);
		} else if(r==1){
			Ship ship = new Ship(-wait-200, this.random.nextInt(HEIGHT), 0, 0, shipnum, ID.Ship);
			this.handler.addObject(ship);
		} else if(r==2){
			Ship ship = new Ship(this.random.nextInt(WIDTH)+wait+200, HEIGHT+200, 0, 0, shipnum, ID.Ship);
			this.handler.addObject(ship);
		} else if(r==3){
			Ship ship = new Ship(WIDTH+200, this.random.nextInt(HEIGHT)+wait+200, 0, 0, shipnum, ID.Ship);
			this.handler.addObject(ship);
		}
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	public synchronized void stop() {
		try{
			thread.join();
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				if(!this.menu){
					this.planet.tick(this);
					tick();
				}
				delta--;
			}
			if(running) {
				render();
			}
			frames ++;
			if(System.currentTimeMillis() - timer > 200) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	private void tick() {
		if(!this.win && !this.lose){
			this.shipsleft = false;
			for(int i=0;i<this.handler.object.size();i+=1){
				if(this.handler.object.get(i).getClass() == Ship.class){
					this.shipsleft = true;
				}
			}
			if(!this.shipsleft){
				if(!this.end){
					lvl5();
				} else if(this.end){
					this.win = true;
				}
			}
			if(this.planet.hp <= 0){
				this.lose = true;
			}
			handler.tick(this);
		}
	}
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		if(!this.win && !this.lose){
			if(!this.menu){
				Color cback = new Color(0, 0, 0, 40);
				g.setColor(cback);
				g.fillRect(0, 0, WIDTH, HEIGHT);
				handler.render(g, this);
				this.planet.render(g, this);
				g.setColor(Color.yellow);
				g.setFont(new Font("Times New Roman", Font.PLAIN, 32));
				g.drawString("Score: " + Integer.toString(this.planet.xp), 10, 30);
				g.drawString("HP: " + Integer.toString(this.planet.hp), 10, 60);
				g.setColor(Color.white);
				g.setStroke(new BasicStroke(2));
				for(int i=0;i<WIDTH;i+=1){
					int s = this.star.get(i).indexOf(1);
					if(s != -1){
						g.drawRect(i, s, 1, 1);
					}
				}
			} else {
				g.setFont(new Font("Times New Roman", Font.PLAIN, 128));
				g.setColor(Color.black);
				g.fillRect(0, 0, WIDTH, HEIGHT);
				g.setColor(Color.white);
				g.setStroke(new BasicStroke(2));
				for(int i=0;i<WIDTH;i+=1){
					int s = this.star.get(i).indexOf(1);
					if(s != -1){
						g.drawRect(i, s, 1, 1);
					}
				}
				g.setColor(Color.green);
				g.drawString("PLANETARY DEFENSE", WIDTH/7, HEIGHT/4);
				g.setFont(new Font("Times New Roman", Font.PLAIN, 64));
				g.setColor(Color.yellow);
				g.drawString("Press Any Key to Play", WIDTH/3, (int) (HEIGHT/1.5));
				if(this.keyInput.pressed){
					g.drawString("(Except that one)", WIDTH/3 + 48, (int) (HEIGHT/1.5) + 64);
				}
			}
		} else {
			g.setColor(Color.black);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			if(this.win){
				g.setFont(new Font("Times New Roman", Font.PLAIN, 128));
				g.setColor(Color.yellow);
				g.drawString("YOU WIN!", WIDTH/3, HEIGHT/3);
				g.drawString("SCORE: " + this.planet.xp, WIDTH/8, HEIGHT/3 + 150);
				g.setFont(new Font("Times New Roman", Font.PLAIN, 64));
				g.drawString("Press any key to restart", 10, HEIGHT/3 + 300);
			}
			if(this.lose){
				g.setFont(new Font("Times New Roman", Font.PLAIN, 128));
				g.setColor(Color.yellow);
				g.drawString("GAME OVER!", WIDTH/3, HEIGHT/3);
				g.drawString("SCORE: " + this.planet.xp, WIDTH/8, HEIGHT/3 + 150);
				g.setFont(new Font("Times New Roman", Font.PLAIN, 64));
				g.drawString("Press any key to restart", 10, HEIGHT/3 + 300);
			}
		}
		g.dispose();
		bs.show();
	}
	public static void main(String args[]) {
		new Game();
	}
}