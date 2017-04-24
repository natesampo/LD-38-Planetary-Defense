import java.awt.Graphics2D;
public abstract class GameObject {
	protected int x, y, velX, velY;
	protected ID id;
	public GameObject(int x, int y, int velX, int velY, ID id){
		this.x = x;
		this.y = y;
		this.id = id;
		this.velX = velX;
		this.velY = velY;
	}
	
	public abstract void tick(Game game);
	public abstract void render(Graphics2D g, Game game);

	public int getX() {
		return x;
	}

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getVelX() {
		return velX;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public int getVelY() {
		return velY;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}
}
