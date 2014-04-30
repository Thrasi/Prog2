

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class PathPanel extends JPanel {
	private JTextArea pathArea;
	PathPanel(String from, String to) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel("Connection between " + from + " to " + to));
		pathArea = new JTextArea();
		pathArea.setEditable(false);
		JScrollPane pathScrollPane = new JScrollPane(pathArea);
		add(pathScrollPane);
	}
	
	void append(String text) {
		pathArea.append(text);
	}
}
