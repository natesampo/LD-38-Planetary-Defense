import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;
public class Window extends Canvas{
	private static final long serialVersionUID = -4865465904816517088L;
	public JFrame frame;
	public Window(int width, int height, String title, Game game) {
		JFrame frame = new JFrame(title);
		this.frame = frame;
		this.frame.setPreferredSize(new Dimension(width, height));
		this.frame.setMaximumSize(new Dimension(width, height));
		this.frame.setMinimumSize(new Dimension(width, height));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setLocationRelativeTo(null);
		this.frame.add(game);
		this.frame.setVisible(true);
		game.start();
	}
}