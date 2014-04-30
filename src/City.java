

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;

public class City extends JComponent {
	private String name;
	private boolean active;
	public static int radius = 10;
	private double posX, posY;  //position of the component / parentcomponent.getWidth/Height
	
	public City(int x, int y, String name, ImagePanel imagePanel) {
		this.name = name;
		setBounds(x-radius, y-radius, 40, 40);
		posX = x/(double)imagePanel.getWidth();
		posY = y/(double)imagePanel.getHeight();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBounds((int)(getParent().getWidth()*posX)-radius, (int)(getParent().getHeight()*posY)-radius, 40, 40);
		if (active) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLACK);
		}
		g.fillOval(0, 0, 2*radius, 2*radius);
		Font font = Font.getFont("courier");
		g.setFont(font);
		g.drawString(name, 0, 40);
	}

	public void activate(boolean status) {
		active = status;
		repaint();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean contains(int x, int y) {
		double dist = Math.sqrt(Math.pow(x-radius,2) + Math.pow(y-radius,2));
		return dist < radius;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof City) { 
			City other = (City)o;
			return name.equalsIgnoreCase(other.name);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// hasCode() is different for upper and lower case strings i.e.
		// "A" and "a" do not have the same hashcode so if we want the 
		// city names to be case sensitive we need to make sure that the 
		// hashcode is the same for lower and upper case.
		return name.toLowerCase().hashCode();
	}
}
