

import graphs.Edge;
import graphs.Graph;
import graphs.GraphMethods;
import graphs.ListGraph;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;


import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;



class PathFinderGUI extends JFrame {
	private ImagePanel mapImagePanel;
	private City from, to;
	private Connection route;
	private ActivationListener activationListener;
	private Graph<City> graph;
	private JFileChooser fileChooser;
	private FileNameExtensionFilter imageFileFilter;
	private FileNameExtensionFilter pathFinderFilter;
	private File workingFile;
	private String pffExtension;
	private JMenuItem quitMenuItem;

	private boolean unSavedChanges;
	
	PathFinderGUI() {
		super("PathFinder");
		
		activationListener = new ActivationListener();
		quitMenuItem = new JMenuItem();
		graph =  new ListGraph<City>();
		unSavedChanges = false;
		fileChooser = new JFileChooser(".");
		fileChooser.setAcceptAllFileFilterUsed(false);
		imageFileFilter = new FileNameExtensionFilter("images(jpg/png/gif)","jpg","png","gif");
		pffExtension = ".pff";
		pathFinderFilter = new FileNameExtensionFilter(pffExtension, pffExtension);
		
		setLayout(new BorderLayout());
		
		add(createButtons(), BorderLayout.NORTH);
		
		setJMenuBar(createMenuBar());
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new SaveOnClose());
		pack();
		setVisible(true);
	}
	
	private JPanel createButtons() {
		JPanel buttonPanel = new JPanel();
		
		
		JButton findPath = new JButton("Find a path");
		findPath.addActionListener(new FindPathListener());
		buttonPanel.add(findPath);
		
		JButton showConnections = new JButton("Show connections");
		showConnections.addActionListener(new ShowConnectionsListener());
		buttonPanel.add(showConnections);
		
		JButton newPlace = new JButton("New place");
		newPlace.addActionListener(new NewPlaceListener());
		buttonPanel.add(newPlace);
		
		JButton newConnection = new JButton("New connection");
		newConnection.addActionListener(new NewConnectionListener());
		buttonPanel.add(newConnection);
		
		JButton alterConnection = new JButton("Alter a connection");
		alterConnection.addActionListener(new AlterConnectionListener());
		buttonPanel.add(alterConnection);
		
		return buttonPanel;
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar  = new JMenuBar();
		
		JMenu archive = new JMenu("Archive");
		menuBar.add(archive);
		
		JMenuItem newMap = new JMenuItem("New");
		newMap.addActionListener(new NewMapListener());
		archive.add(newMap);
		
		JMenuItem openMap = new JMenuItem("Open");
		openMap.addActionListener(new openListener());
		archive.add(openMap);
		
		JMenuItem saveMap = new JMenuItem("Save");
		saveMap.addActionListener(new saveListener());
		archive.add(saveMap);
		
		JMenuItem saveAs = new JMenuItem("Save as...");
		saveAs.addActionListener(new saveAsListener());
		archive.add(saveAs);
		
		quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new QuitListener());
		archive.add(quitMenuItem);
		
		
		JMenu operations = new JMenu("Operations");
		menuBar.add(operations);
		
		JMenuItem findPath = new JMenuItem("Find a path");
		findPath.addActionListener(new FindPathListener());
		operations.add(findPath);
		
		JMenuItem showConnections = new JMenuItem("Show connections");
		showConnections.addActionListener(new ShowConnectionsListener());
		operations.add(showConnections);
		
		JMenuItem newPlace = new JMenuItem("New place");
		newPlace.addActionListener(new NewPlaceListener());
		operations.add(newPlace);
		
		JMenuItem newConnection = new JMenuItem("New connection");
		newConnection.addActionListener(new NewConnectionListener());
		operations.add(newConnection);
		
		JMenuItem alterConnection = new JMenuItem("Alter a connection");
		alterConnection.addActionListener(new AlterConnectionListener());
		operations.add(alterConnection);
		
		return menuBar;
	}
	
	private class FindPathListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				checkMap();
				checkSelection();
				
				ArrayList<Edge<City>> path = GraphMethods.getPath(graph, from, to);
				if (path == null) 
					{ displayNotConnectedError(); return; }
				
				PathPanel pathPanel = new PathPanel(from.getName(), to.getName());
				for (Edge<City> edge : path) {
					pathPanel.append(edge.toString());
					pathPanel.append("\n");
				}
				
				JOptionPane.showMessageDialog(null, pathPanel, "Shortest path", JOptionPane.INFORMATION_MESSAGE);
			} catch(IllegalStateException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	private class ShowConnectionsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				checkMap();
				checkSelection();
				
				Edge<City> edge = graph.getEdgeBetween(from, to);
				if (edge == null) 
					{ displayNotConnectedError(); return; }
				
				ConnectionPanel connectionPanel = new ConnectionPanel(from.getName(), to.getName(), false, false);
				connectionPanel.setName(edge.getName());
				connectionPanel.setTime(""+edge.getWeight());
				
				JOptionPane.showMessageDialog(null, connectionPanel, "Connection ", JOptionPane.INFORMATION_MESSAGE);
			} catch(NoSuchElementException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch(IllegalStateException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	private class NewPlaceListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				checkMap();
				
				mapImagePanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				mapImagePanel.addMouseListener(new NewCityListener());
			} catch(IllegalStateException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	private class saveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				checkMap();
				save();
			} catch(FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Can not write to this file!");
			} catch(IOException e) {
				JOptionPane.showMessageDialog(null, "ERROR :(");
			} catch(IllegalStateException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	private class saveAsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				checkMap();
				saveAs();
			} catch(FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Can not write to this file!");
			} catch(IOException e) {
				JOptionPane.showMessageDialog(null, "ERROR :(");
			} catch(IllegalStateException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	private class openListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (unSavedChanges) {
					boolean abort = saveChanges();
					if (abort) { return; }
				}
				
				fileChooser.removeChoosableFileFilter(imageFileFilter);
				fileChooser.setFileFilter(pathFinderFilter);
				
				int answer = fileChooser.showOpenDialog(PathFinderGUI.this);
				if (answer != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				workingFile = fileChooser.getSelectedFile();
				
				FileInputStream fis = new FileInputStream(workingFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				
				clearData();
				mapImagePanel = (ImagePanel)ois.readObject();
				graph = (Graph<City>)ois.readObject();
				ois.close();

				add(mapImagePanel, BorderLayout.CENTER);
				
				// Add listeners
				for (Component comp : mapImagePanel.getComponents()) {
					comp.addMouseListener(activationListener);
				}

				unSavedChanges = false;
				pack();
				repaint();
				
			} catch(FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Can not write to this file!");
			} catch(IOException e) {
				JOptionPane.showMessageDialog(null, "ERROR :(\n"+e.getMessage());
			} catch(ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Data Class has changed!");
			}
		}
	}
	
	private class NewConnectionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				checkMap();
				checkSelection();
				
				// add conction error
				if (graph.getEdgeBetween(from, to) != null) 
					{ displayNotConnectedError(); return; }
				ConnectionPanel connectionPanel = new ConnectionPanel(from.getName(), to.getName(), true, true);
				int respons = JOptionPane.showConfirmDialog(null, connectionPanel,"New Place ", JOptionPane.OK_CANCEL_OPTION);
				
				if (respons == JOptionPane.OK_OPTION) {
					String name = connectionPanel.getName();
					if (name.isEmpty()) { throw new IllegalArgumentException("The name can not be empty!"); }
					
					int time = Integer.parseInt( connectionPanel.getTime() );
					graph.connect(from, to, name, time);
					
					Connection connection = new Connection(from, to);
					connection.addMouseListener(activationListener);
					mapImagePanel.add(connection);
					unSavedChanges = true;
					repaint();
				}
				
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Time must be a positive integer!");
			} catch(IllegalStateException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch(NoSuchElementException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch(IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	private class AlterConnectionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				checkMap();
				checkSelection();
				
				Edge<City> edge = graph.getEdgeBetween(from, to);
				if (edge == null) 
					{ displayNotConnectedError(); return; }
				
				ConnectionPanel connectionPanel = new ConnectionPanel(from.getName(), to.getName(), false, true);
				connectionPanel.setName(edge.getName());
				connectionPanel.setTime(""+edge.getWeight());
				
				int respons = JOptionPane.showConfirmDialog(null, connectionPanel,"New Place ", JOptionPane.OK_CANCEL_OPTION);
				if (respons == JOptionPane.OK_OPTION) {
					int time = Integer.parseInt( connectionPanel.getTime() );
					graph.setConnectionWeight(from, to, time);
					unSavedChanges = true;
				}
				
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Time must be a positive integer!");
			} catch(IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch(NoSuchElementException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch(IllegalStateException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
	
	
	
	private class NewMapListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (unSavedChanges) {
					boolean abort = saveChanges();
					if (abort) { return; }
				}
				fileChooser.removeChoosableFileFilter(pathFinderFilter);
				fileChooser.setFileFilter(imageFileFilter);
				
				int answer = fileChooser.showOpenDialog(PathFinderGUI.this);
				if (answer != JFileChooser.APPROVE_OPTION) { return; }
				
				File file =  fileChooser.getSelectedFile();
				String path = file.getAbsolutePath();
				clearData();

				mapImagePanel = new ImagePanel(path);
				add(mapImagePanel, BorderLayout.CENTER);
				
				unSavedChanges = true;
				pack();
				repaint();
				
			} catch(FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Can not write to this file!");
			} catch(IOException e) {
				JOptionPane.showMessageDialog(null, "ERROR :(\n"+e.getMessage());
			}
		}
	}
	
	private class QuitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				boolean abort = false;
				if (unSavedChanges) {
					abort = saveChanges();
				}
				if (abort) { return; }
				System.exit(0);
			} catch(FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Can not write to this file!");
			} catch(IOException e) {
				JOptionPane.showMessageDialog(null, "ERROR :(");
			}
		}
	}
	
	private class NewCityListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {
			try {
				checkMap();
				
				NewCityPanel namePanel = new NewCityPanel();
				int respons = JOptionPane.showConfirmDialog(null, namePanel,"New Place ", JOptionPane.OK_CANCEL_OPTION);
	
				if (respons == JOptionPane.OK_OPTION) {
					String name = namePanel.getName();
				
					if (name.isEmpty()) { throw new IllegalArgumentException("The name can not be empty!"); }
					
					City newCity = new City( mev.getX(), mev.getY(), name, mapImagePanel); 
					newCity.addMouseListener(activationListener);
					
					if (graph.contains(newCity)) 
						{ throw new IllegalArgumentException("This City already exists!"); }

					graph.add(newCity); 
					
					mapImagePanel.add(newCity);
					unSavedChanges = true;
					repaint();
				}
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch (IllegalStateException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
			mapImagePanel.removeMouseListener(this);
			mapImagePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private class ActivationListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {
			if (mev.getSource() instanceof City) {
				City city = (City)mev.getSource();
				
				if (route != null) { route.activate(false); route = null; }
				
				if (from == null && !city.equals(to)) {
					from = city; city.activate(true);
				} else if (to == null && !city.equals(from)) {
					to = city; city.activate(true);
				} else if (city.equals(from)) {
					from = null; city.activate(false);
				} else if (city.equals(to)) {
					to = null; city.activate(false);
				}
				
			} else {
				Connection connection = (Connection)mev.getSource();
				
				if (from != null) { from.activate(false); from = null; }
				
				if (to != null) { to.activate(false); to = null; }
				
				if (route == null) {
					route = connection;
					connection.activate(true);
				} else if (connection.equals(route)) { 
					route = null;
					connection.activate(false);
				} else {
					route.activate(false);
					route = connection;
					connection.activate(true);
				}
			}
		}
	}
	
	private class SaveOnClose extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent we) {
        	quitMenuItem.doClick();
        }
    }
	
	private void clearData() {
		if (mapImagePanel != null) {
			this.remove(mapImagePanel);
		}
		graph = new ListGraph<City>();
		from = null;
		to = null;
		route = null;
		workingFile = null;
	}
	
	private int save() throws FileNotFoundException, IOException {
		if (workingFile == null) {
			return saveAs();
		}
		
		saveData(workingFile);
		return JFileChooser.APPROVE_OPTION;
	}
	
	private void saveData(File file) throws FileNotFoundException, IOException {
		String fileName = file.getAbsolutePath();
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		// Remove listeners
		for (Component comp : mapImagePanel.getComponents()) {
			comp.removeMouseListener(activationListener);
		}

		oos.writeObject(mapImagePanel);
		oos.writeObject(graph);
		oos.close();
		
		// Add listeners back
		for (Component comp : mapImagePanel.getComponents()) {
			comp.addMouseListener(activationListener);
		}
		
		unSavedChanges = false;
	}
	
	private int saveAs() throws FileNotFoundException, IOException {
		fileChooser.removeChoosableFileFilter(imageFileFilter);
		fileChooser.setFileFilter(pathFinderFilter);
		
		int answer = fileChooser.showSaveDialog(PathFinderGUI.this);
		
		if (answer == JFileChooser.APPROVE_OPTION) {
			File tmp = fileChooser.getSelectedFile();
			String tmpPath = tmp.getAbsolutePath();

			int index = tmpPath.lastIndexOf(".");
			tmpPath = tmpPath.substring(0, index);
			tmpPath += pffExtension;
			
			workingFile = new File(tmpPath);
			
			saveData(workingFile);
		} 
		
		return answer;
	}
	
	private boolean saveChanges() throws FileNotFoundException, IOException{
		int respons = JOptionPane.showConfirmDialog(null, "There are unsaved changes in the current map. Do you want to save them?");
		switch (respons) {
		case JOptionPane.YES_OPTION:
			int answer = save();
			if (answer == JFileChooser.CANCEL_OPTION) 
				{ return true; }
		case JOptionPane.NO_OPTION:
			return false;
		case JOptionPane.CANCEL_OPTION:
			return true;
		default:
			return false;
		}
	}
	
	private void displayNotConnectedError() {
		JOptionPane.showMessageDialog(null, "The two cities are not connected!");
	}
	
	private void checkMap() {
		boolean condition = mapImagePanel == null;
		if (condition) 
			{ throw new IllegalStateException("You must open a map before atempting to interact with one!"); }
	}
	
	private void checkSelection() {
		if (route != null) {
			from = route.from();
			to = route.to();
		}
		if (from == null || to == null) 
			{ throw new IllegalStateException("Two locations or a connection must be selected!"); }
	}
}

