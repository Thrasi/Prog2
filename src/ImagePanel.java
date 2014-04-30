

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private ImageIcon img;
	
	public ImagePanel(String fileName) {
		setLayout(null);
		img = new ImageIcon(fileName);
		setPreferredSize(new Dimension(img.getIconWidth(), img.getIconHeight()));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
