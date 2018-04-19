package bibleReader;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bibleReader.model.ArrayListBible;
import bibleReader.model.Bible;
import bibleReader.model.BibleFactory;
import bibleReader.model.BibleReaderModel;
import bibleReader.model.VerseList;

/**
 * The main class for the Bible Reader Application.
 * 
 * @author cusack
 * @author Jonathan Chaffer
 */
public class BibleReaderApp extends JFrame {
	// window size constants
	public static final int width = 750;
	public static final int height = 500;

	// GUI components
	private BibleReaderModel model;
	public ResultView resultView;
	public JTextField inputField;
	public JButton searchButton;
	public JButton passageButton;
	private JMenuBar menuBar;
	private JFileChooser fileChooser;

	/**
	 * Start the application.
	 * 
	 * @param args
	 *            Main method arguments.
	 */
	public static void main(String[] args) {
		new BibleReaderApp();
	}

	/**
	 * Set up the bible application and create the GUI.
	 */
	public BibleReaderApp() {
		model = new BibleReaderModel();

		// add kjv
		File kjvFile = new File("kjv.atv");
		VerseList kjvVerses = BibleIO.readBible(kjvFile);
		Bible kjv = BibleFactory.createBible(kjvVerses);
		model.addBible(kjv);

		// add asv
		File asvFile = new File("asv.xmv");
		VerseList asvVerses = BibleIO.readBible(asvFile);
		Bible asv = BibleFactory.createBible(asvVerses);
		model.addBible(asv);

		// add esv
		File esvFile = new File("esv.atv");
		VerseList esvVerses = BibleIO.readBible(esvFile);
		Bible esv = BibleFactory.createBible(esvVerses);
		model.addBible(esv);

		// window title
		setTitle("Bible Reader");
		// result view
		resultView = new ResultView(model);
		// file chooser
		fileChooser = new JFileChooser();
		// buttons and text fields
		inputField = new JTextField("", 20);
		inputField.setName("InputTextField");
		searchButton = new JButton("Search");
		searchButton.setName("SearchButton");
		passageButton = new JButton("Passage");
		passageButton.setName("PassageButton");

		// add action listeners to inputField, searchButton, and passageButton
		inputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultView.updateSearchResults(inputField.getText());
			}
		});
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultView.updateSearchResults(inputField.getText());
			}
		});
		passageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultView.updatePassageResults(inputField.getText());
			}
		});

		setupGUI();
		pack();
		setSize(width, height);

		// exit when you click the "x".
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Set up the main GUI.
	 */
	private void setupGUI() {
		// make the layout
		Container cont = getContentPane();
		cont.setLayout(new BorderLayout());
		JPanel inputAndButtons = new JPanel();
		inputAndButtons.setLayout(new FlowLayout());
		cont.add(inputAndButtons, BorderLayout.NORTH);

		// add input and search buttons to the top area
		inputAndButtons.add(inputField);
		inputAndButtons.add(searchButton);
		inputAndButtons.add(passageButton);

		// add result view to the center
		cont.add(resultView, BorderLayout.CENTER);

		// add a menu bar
		menuBar = new JMenuBar();

		// add a file menu with an exit and open item
		JMenu fileMenu = new JMenu("File", true);
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(openItem);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);

		// add a help menu with an about item
		JMenu helpMenu = new JMenu("Help", true);
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(BibleReaderApp.this,
						"Bible Reader App by Jonathan Chaffer and Jacob Lahr, 2018");
				;
			}
		});
		helpMenu.add(aboutItem);
		menuBar.add(helpMenu);

		// set the window's menu bar
		setJMenuBar(menuBar);
	}

	public void openFile() {
		// This will pop up a window which allows the user to pick a file from
		// the file system.
		int returnVal = fileChooser.showDialog(BibleReaderApp.this, "Open");
		// We check whether or not they clicked the "Open" button
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// We get a reference to the file that the user selected.
			File file = fileChooser.getSelectedFile();
			// Make sure it actually exists.
			if (!file.exists()) {
				JOptionPane.showMessageDialog(this, "That file does not exist!.", "File Error",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// Apparently all is well, so go ahead and read the file.
				model.addBible(new ArrayListBible(BibleIO.readBible(file)));
				if (resultView.getLastSearchType() == ResultView.WORD) {
					resultView.updateSearchResults(resultView.getLastInput());
				} else if (resultView.getLastSearchType() == ResultView.PASSAGE) {
					resultView.updatePassageResults(resultView.getLastInput());
				}
			}
		}
	}
}
