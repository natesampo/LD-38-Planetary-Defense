import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Ship extends GameObject {
	public int hp, speed, strategy, damage, xpdrop, shipnum;
	public double rotationRequired, step;
	Image image;
	HashMap<Integer, HashMap<String, String>> shipStats;
	public Ship(int x, int y, int velX, int velY, int shipnum, ID id) {
		super(x, y, velX, velY, id);
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
		shipStats = new HashMap<Integer, HashMap<String, String>>();
		shipStats.put(0, new HashMap<String, String>());
		shipStats.get(0).put("image",  "Ship1.png");
		shipStats.get(0).put("hp",  "1");
		shipStats.get(0).put("speed",  "4");
		shipStats.get(0).put("strategy",  "0");
		shipStats.get(0).put("damage",  "1");
		shipStats.get(0).put("xpdrop",  "1");
		shipStats.put(1, new HashMap<String, String>());
		shipStats.get(1).put("image",  "Ship2.png");
		shipStats.get(1).put("hp",  "1");
		shipStats.get(1).put("speed",  "6");
		shipStats.get(1).put("strategy",  "0");
		shipStats.get(1).put("damage",  "2");
		shipStats.get(1).put("xpdrop",  "3");
		shipStats.get(1).put("image",  "Ship2.png");
		shipStats.put(2, new HashMap<String, String>());
		shipStats.get(2).put("image",  "Ship3.png");
		shipStats.get(2).put("hp",  "3");
		shipStats.get(2).put("speed",  "4");
		shipStats.get(2).put("strategy",  "1");
		shipStats.get(2).put("damage",  "2");
		shipStats.get(2).put("xpdrop",  "4");
		shipStats.put(3, new HashMap<String, String>());
		shipStats.get(3).put("image",  "Ship4.png");
		shipStats.get(3).put("hp",  "7");
		shipStats.get(3).put("speed",  "10");
		shipStats.get(3).put("strategy",  "0");
		shipStats.get(3).put("damage",  "5");
		shipStats.get(3).put("xpdrop",  "20");
		shipStats.put(4, new HashMap<String, String>());
		shipStats.get(4).put("image",  "Ship5.png");
		shipStats.get(4).put("hp",  "12");
		shipStats.get(4).put("speed",  "5");
		shipStats.get(4).put("strategy",  "2");
		shipStats.get(4).put("damage",  "0");
		shipStats.get(4).put("xpdrop",  "30");
		this.hp = Integer.parseInt(shipStats.get(shipnum).get("hp"));
		this.speed = Integer.parseInt(shipStats.get(shipnum).get("speed"));
		this.strategy = Integer.parseInt(shipStats.get(shipnum).get("strategy"));
		this.damage = Integer.parseInt(shipStats.get(shipnum).get("damage"));
		this.xpdrop = Integer.parseInt(shipStats.get(shipnum).get("xpdrop"));
		this.shipnum = shipnum;
		try {
			image = ImageIO.read(new File(shipStats.get(shipnum).get("image")));
		} catch (IOException e) {
		}
	}

	public void tick(Game game) {
		this.step += 0.1;
		this.x += this.velX;
		this.y += this.velY;
		if(this.strategy == 0){
			double dist = Math.sqrt((this.x-game.planet.x)*(this.x-game.planet.x) + (this.y-game.planet.y)*(this.y-game.planet.y));
			this.velX = (int) (-this.speed * ((this.x-game.planet.x)/dist));
			this.velY = (int) (-this.speed * ((this.y-game.planet.y)/dist));
		}
		if(this.strategy == 1){
			double dist = Math.sqrt((this.x-game.planet.x)*(this.x-game.planet.x) + (this.y-game.planet.y)*(this.y-game.planet.y));
			this.velX = (int) (-this.speed * ((this.x-game.planet.x)/dist));
			this.velY = (int) (-this.speed * ((this.y-game.planet.y)/dist) + 10*Math.sin(step));
		}
		if(this.strategy == 2){
			double dist = Math.sqrt((this.x-game.planet.x)*(this.x-game.planet.x) + (this.y-game.planet.y)*(this.y-game.planet.y));
			if(dist > 450){
				this.velX = (int) (-this.speed * ((this.x-game.planet.x)/dist));
				this.velY = (int) (-this.speed * ((this.y-game.planet.y)/dist));
			} else {
				this.velY = (int) -(-this.speed * ((this.x-game.planet.x)/dist));
				this.velX = (int) (-this.speed * ((this.y-game.planet.y)/dist));
				if(((int)this.step)%40 == 0){
					Ship ship = new Ship(this.x, this.y, 0, 0, game.random.nextInt(3), ID.Ship);
					game.handler.addObject(ship);
					this.step += 1;
				}
			}
		}
		for(int i=0;i<game.handler.object.size();i+=1){
			if(game.handler.object.get(i).getClass() == Bullet.class){
				Rectangle r = new Rectangle(this.x, this.y, image.getWidth(null), image.getHeight(null));
				Rectangle p = new Rectangle(game.handler.object.get(i).x, game.handler.object.get(i).y, ((Bullet) game.handler.object.get(i)).thickness*2, ((Bullet) game.handler.object.get(i)).thickness*2);
				if(p.intersects(r)){
					this.hp -= ((Bullet) game.handler.object.get(i)).damage;
					game.handler.object.remove(i);
				}
			}
		}
		Rectangle r = new Rectangle(this.x, this.y, image.getWidth(null), image.getHeight(null));
		Rectangle p = new Rectangle(game.planet.x, game.planet.y, 64, 64);
		if(p.intersects(r)){
			game.planet.hp -= this.damage;
			game.handler.object.remove(this);
		}
		if(this.hp <= 0){
			game.planet.xp += this.xpdrop;
			game.handler.object.remove(this);
		}
	}

	public void render(Graphics2D g, Game game) {
		if(shipnum == 3){
			rotationRequired = step;
			double locationX = image.getWidth(null) / 2;
			double locationY = image.getHeight(null) / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter((BufferedImage) image, null), this.x, this.y, null);
			if(game.debug == true){
				g.setColor(Color.red);
				g.drawRect(this.x, this.y, image.getWidth(null), image.getHeight(null));
			}
		} else if(shipnum == 4){
			if(this.x <= game.planet.x){
				rotationRequired = 1.5*Math.PI+Math.atan(velY/(velX + 0.01));
				} else {
				rotationRequired = (2.5*Math.PI+Math.atan(velY/(velX + 0.01)));
				}
			double locationX = image.getWidth(null) / 2;
			double locationY = image.getHeight(null) / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter((BufferedImage) image, null), this.x, this.y, null);
			if(game.debug == true){
				g.setColor(Color.red);
				g.drawRect(this.x, this.y, image.getWidth(null), image.getHeight(null));
			}
		} else if(this.x <= game.planet.x){
			rotationRequired = Math.PI/2+Math.atan(velY/(velX + 0.01));
		} else {
			rotationRequired = (1.5*Math.PI+Math.atan(velY/(velX + 0.01)));
			}
			double locationX = image.getWidth(null) / 2;
			double locationY = image.getHeight(null) / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter((BufferedImage) image, null), this.x, this.y, null);
			if(game.debug == true){
				g.setColor(Color.red);
				g.drawRect(this.x, this.y, image.getWidth(null), image.getHeight(null));
		}
	}
}
