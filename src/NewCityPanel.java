

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

class NewCityPanel extends JPanel {
	private JTextField nameField;
	NewCityPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel("Name: "));
		nameField = new JTextField();
		add(nameField);
		nameField.addAncestorListener(new RequestFocusClass());
	}
	
	public String getName() {
		return nameField.getText();
	}
}


