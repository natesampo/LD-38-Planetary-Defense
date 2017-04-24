import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

public class Planet extends GameObject {
	Image image;
	private int cd;
	public int bulletspeed, reloadspeed, xp, hp, level;
	public Planet(int x, int y, int velX, int velY, ID id) {
		super(x, y, velX, velY, id);
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
		this.cd = 0;
		this.xp = 0;
		this.hp = 100;
		this.level = 0;
		image = Toolkit.getDefaultToolkit().getImage("Planet.png");
	}

	public void tick(Game game) {
		if(this.xp >= 15 && this.level == 0){
			this.level = 1;
		}
		if(this.xp >= 50 && this.level == 1){
			this.level = 2;
		}
		if(this.xp >= 100 && this.level == 2){
			this.level = 3;
		}
		this.cd += 1;
		if(cd > this.reloadspeed){
			if(this.level == 3){
				Bullet bullet = new Bullet(this.x + 32, this.y + 32, 0, 0, this.level, 2, game, ID.Bullet);
				Bullet bullet1 = new Bullet(this.x + 32, this.y + 32, 0, 0, this.level, 3, game, ID.Bullet);
				Bullet bullet2 = new Bullet(this.x + 32, this.y + 32, 0, 0, 2, 1, game, ID.Bullet);
				game.handler.addObject(bullet);
				game.handler.addObject(bullet1);
				game.handler.addObject(bullet2);
				this.reloadspeed = bullet.reload;
			} else {
				Bullet bullet = new Bullet(this.x + 32, this.y + 32, 0, 0, this.level, 1, game, ID.Bullet);
				game.handler.addObject(bullet);
				this.reloadspeed = bullet.reload;
			}
			cd = 0;
		}
	}

	public void render(Graphics2D g, Game game) {
		g.drawImage(image, this.x, this.y, game);
	}
}
