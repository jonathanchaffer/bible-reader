package bibleReader;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bibleReader.model.ArrayListBible;
import bibleReader.model.Bible;
import bibleReader.model.BibleReaderModel;
import bibleReader.model.VerseList;

/**
 * The main class for the Bible Reader Application.
 * 
 * @author cusack
 * @author Jonathan Chaffer & Jason Gombas (2018)
 */
public class BibleReaderApp extends JFrame {

	public static final int width = 600;
	public static final int height = 600;

	public static void main(String[] args) {
		new BibleReaderApp();
	}

	// Fields
	private BibleReaderModel model;
	private ResultView resultView;
	private JTextField inputField;
	private JButton searchButton;
	private JButton passageButton;

	/**
	 * Set-up the bible application and create the GUI.
	 */
	public BibleReaderApp() {
		model = new BibleReaderModel();
		File kjvFile = new File("kjv.atv");
		VerseList verses = BibleIO.readBible(kjvFile);

		Bible kjv = new ArrayListBible(verses);

		model.addBible(kjv);

		setTitle("Bible Reader");

		resultView = new ResultView(model);
		inputField = new JTextField("", 20);
		searchButton = new JButton("Search");
		passageButton = new JButton("Passage");

		// Add action listeners to inputField, searchButton, and passageButton
		inputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultView.updateSearch(inputField.getText());
			}
		});

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultView.updateSearch(inputField.getText());
			}
		});

		passageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultView.updatePassage(inputField.getText());
			}
		});

		setupGUI();
		pack();
		setSize(width, height);

		// So the application exits when you click the "x".
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Set up the main GUI.
	 */
	private void setupGUI() {
		Container cont = getContentPane();
		cont.setLayout(new BorderLayout());

		JPanel inputAndButtons = new JPanel();
		inputAndButtons.setLayout(new FlowLayout());
		cont.add(inputAndButtons, BorderLayout.NORTH);

		inputAndButtons.add(inputField);
		inputAndButtons.add(searchButton);
		inputAndButtons.add(passageButton);

		cont.add(resultView, BorderLayout.CENTER);
	}

}
