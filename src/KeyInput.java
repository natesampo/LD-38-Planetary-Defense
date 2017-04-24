import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class KeyInput extends KeyAdapter{
	private Game game;
	public int key, dead_key;
	public boolean pressed;
	public KeyInput(Game game){
		this.game = game;
		this.pressed = false;
	}
	public void keyReleased(KeyEvent e) {
		this.key = e.getKeyCode();
		if(this.pressed && (key != dead_key)){
			this.game.menu = false;
		}
		if(!this.pressed){
			this.dead_key = this.key;
			this.pressed = true;
		}
		if(this.game.lose || this.game.win){
			this.game.newGame();
			this.game = null;
		}
	}
}