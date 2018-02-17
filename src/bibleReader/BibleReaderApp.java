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
 */
public class BibleReaderApp extends JFrame {
	// Change these to suit your needs.
	public static final int	width	= 600;
	public static final int	height	= 600;

	public static void main(String[] args) {
		new BibleReaderApp();
	}

	// Fields
	private BibleReaderModel	model;
	private JFrame				mainFrame;
	private ResultView			resultView;
	private JTextField			inputField;
	private JButton				searchButton;
	private JButton				passageButton;

	/**
	 * Default constructor. We may want to replace this with a different one.
	 */
	public BibleReaderApp() {
		// There is no guarantee that this complete/correct, so take a close
		// look to make sure you understand what this code is doing in case
		// you need to modify or add to it.
		model = new BibleReaderModel(); // For now call the default constructor. This might change.
		File kjvFile = new File("kjv.atv");
		VerseList verses = BibleIO.readBible(kjvFile);
		
		Bible kjv = new ArrayListBible(verses);
		
		model.addBible(kjv);

		setTitle("Bible Reader");
		
		resultView = new ResultView(model);
		inputField = new JTextField("", 20);
		searchButton = new JButton("Search");
		passageButton = new JButton("Passage");
		
		inputField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				resultView.updateSearch(inputField.getText());
			}
		});
		
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				resultView.updateSearch(inputField.getText());
			}
		});
		
		passageButton.addActionListener(new ActionListener(){
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
	 * Set up the main GUI. Make sure you don't forget to put resultView somewhere!
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

		// The stage numbers below may change, so make sure to pay attention to
		// what the assignment says.
		// TODO Add passage lookup: Stage ?
		// TODO Add 2nd version on display: Stage ?
		// TODO Limit the displayed search results to 20 at a time: Stage ?
		// TODO Add 3rd versions on display: Stage ?
		// TODO Format results better: Stage ?
		// TODO Display cross references for third version: Stage ?
		// TODO Save/load search results: Stage ?
	}

}
