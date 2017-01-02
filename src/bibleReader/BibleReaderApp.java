package bibleReader;

import java.io.File;

import javax.swing.JFrame;

import bibleReader.model.ArrayListBible;
import bibleReader.model.Bible;
import bibleReader.model.BibleReaderModel;
import bibleReader.model.TreeMapBible;
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
	private ResultView			resultView;

	// TODO add more fields as necessary

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

		resultView = new ResultView(model);

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
		// TODO textfield and button that allow a word search to be performed: Stage 5
		// TODO Display search results (in the ResulteView): Stage 5

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
