

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;

class Connection extends JComponent {
	private City from, to;
	private boolean active; 
	Connection(City from, City to) {
		active = false;
		this.from = from;
		this.to = to;
		setBounds(border());
	}
	
	City from() { return from; }
	
	City to() { return to; }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBounds(border());
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (active) { g2d.setColor(Color.RED); }
		else { g2d.setColor(Color.BLACK); }
		
		g2d.setStroke(new BasicStroke(3));
		
		int referenceX = getX() - City.radius;
		int referenceY = getY() - City.radius;
		g2d.drawLine(from.getX() - referenceX, from.getY() - referenceY,
					 to.getX() - referenceX, to.getY() - referenceY);
	}
	
	private Rectangle border() {
		int x0 = Math.min(from.getX(), to.getX()) + City.radius;
		int y0 = Math.min(from.getY(), to.getY()) + City.radius;
		int x1 = Math.max(from.getX(), to.getX()) + City.radius;
		int y1 = Math.max(from.getY(), to.getY()) + City.radius;
		return new Rectangle(x0, y0, x1-x0, y1-y0);
	}
	
	void activate(boolean status) {
		active = status;
		repaint();
	}
	
	@Override
	public boolean contains(int x, int y) {
		int xRef = getX() - City.radius;
		int yRef = getY() - City.radius;
		int x0 = from.getX() - xRef;
		int y0 = from.getY() - yRef;
		int x1 = to.getX() - xRef;
		int y1 = to.getY() - yRef;
		int Y = y0 + (y1 - y0) * (x - x0) / (x1 - x0);
		return Math.abs(Y - y) < 5 && x > City.radius && x < getWidth() - City.radius;
	}
}
