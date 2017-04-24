import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {
	int mouse_x, mouse_y;
	public void mouseMoved(MouseEvent e){
		this.mouse_x = e.getX();
		this.mouse_y = e.getY();
	}
}
