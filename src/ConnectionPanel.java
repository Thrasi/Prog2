

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConnectionPanel extends JPanel {
	private JTextField nameField;
	private JTextField timeField;
	ConnectionPanel(String from, String to, boolean editableName, boolean editableTime) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel("Connection between " + from + " to " + to));
		
		JPanel namePanel = new JPanel();
		add(namePanel);
		namePanel.add(new JLabel("Name:"));
		nameField = new JTextField(8);
		namePanel.add(nameField);
		nameField.setEditable(editableName);
		
		JPanel timePanel = new JPanel();
		add(timePanel);
		timePanel.add(new JLabel("Time:"));
		timeField = new JTextField(8);
		timePanel.add(timeField);
		timeField.setEditable(editableTime);
		
		if (editableName) {
			nameField.addAncestorListener(new RequestFocusClass());
		} else if (editableTime) {
			timeField.addAncestorListener(new RequestFocusClass());
		}
	}
	
	public String getName() {
		return nameField.getText();
	}
	public void setName(String name) {
		nameField.setText(name);
	}
	
	String getTime() {
		return timeField.getText();
	}
	void setTime(String time) {
		timeField.setText(time);
	}
	
	Integer b = 9;

}
