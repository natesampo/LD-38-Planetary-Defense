import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

public class Bullet extends GameObject {
	int damage, speed, reload, thickness, bulletseq;
	HashMap<Integer, HashMap<String, String>> bulletStats;
	public Bullet(int x, int y, int velX, int velY, int bulletnum, int bulletseq, Game game, ID id) {
		super(x, y, velX, velY, id);
		bulletStats = new HashMap<Integer, HashMap<String, String>>();
		bulletStats.put(0, new HashMap<String, String>());
		bulletStats.get(0).put("damage",  "1");
		bulletStats.get(0).put("speed",  "20");
		bulletStats.get(0).put("reload",  "20");
		bulletStats.get(0).put("thickness",  "5");
		bulletStats.put(1, new HashMap<String, String>());
		bulletStats.get(1).put("damage",  "1");
		bulletStats.get(1).put("speed",  "20");
		bulletStats.get(1).put("reload",  "10");
		bulletStats.get(1).put("thickness",  "7");
		bulletStats.put(2, new HashMap<String, String>());
		bulletStats.get(2).put("damage",  "2");
		bulletStats.get(2).put("speed",  "15");
		bulletStats.get(2).put("reload",  "15");
		bulletStats.get(2).put("thickness",  "30");
		bulletStats.put(3, new HashMap<String, String>());
		bulletStats.get(3).put("damage",  "2");
		bulletStats.get(3).put("speed",  "22");
		bulletStats.get(3).put("reload",  "11");
		bulletStats.get(3).put("thickness",  "10");
		this.damage = Integer.parseInt(bulletStats.get(bulletnum).get("damage"));
		this.speed = Integer.parseInt(bulletStats.get(bulletnum).get("speed"));
		this.reload = Integer.parseInt(bulletStats.get(bulletnum).get("reload"));
		this.thickness = Integer.parseInt(bulletStats.get(bulletnum).get("thickness"));
		this.bulletseq = bulletseq;
		if(this.bulletseq == 1){
			double spd = Math.sqrt((this.x-game.mouseInput.mouse_x)*(this.x-game.mouseInput.mouse_x) + (this.y-game.mouseInput.mouse_y)*(this.y-game.mouseInput.mouse_y));
			int xspd = (int) (-this.speed * ((this.x-game.mouseInput.mouse_x)/spd));
			int yspd = (int) (-this.speed * ((this.y-game.mouseInput.mouse_y)/spd));
			this.velX = xspd;
			this.velY = yspd;
		} else if(this.bulletseq == 2){
			double spd = Math.sqrt((this.x-game.mouseInput.mouse_x-100)*(this.x-game.mouseInput.mouse_x-100) + (this.y-game.mouseInput.mouse_y-100)*(this.y-game.mouseInput.mouse_y-100));
			int xspd = (int) (-this.speed * ((this.x-game.mouseInput.mouse_x-100)/spd));
			int yspd = (int) (-this.speed * ((this.y-game.mouseInput.mouse_y-100)/spd));
			this.velX = xspd;
			this.velY = yspd;
		} else if(this.bulletseq == 3){
			double spd = Math.sqrt((this.x-game.mouseInput.mouse_x+100)*(this.x-game.mouseInput.mouse_x+100) + (this.y-game.mouseInput.mouse_y+100)*(this.y-game.mouseInput.mouse_y+100));
			int xspd = (int) (-this.speed * ((this.x-game.mouseInput.mouse_x+100)/spd));
			int yspd = (int) (-this.speed * ((this.y-game.mouseInput.mouse_y+100)/spd));
			this.velX = xspd;
			this.velY = yspd;
		}
	}

	public void tick(Game game) {
		this.x += this.velX;
		this.y += this.velY;
		if(this.x > game.WIDTH || this.x < 0 || this.y > game.HEIGHT || this.y < 0){
			game.handler.object.remove(this);
		}
	}

	public void render(Graphics2D g, Game game) {
		g.setColor(Color.white);
		g.setStroke(new BasicStroke(this.thickness));
		g.drawLine(this.x, this.y, this.x + this.velX, this.y + this.velY);
	}

}
