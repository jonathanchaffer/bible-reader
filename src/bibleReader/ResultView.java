package bibleReader;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bibleReader.model.Bible;
import bibleReader.model.BibleReaderModel;
import bibleReader.model.Reference;
import bibleReader.model.ReferenceList;
import bibleReader.model.Verse;
import bibleReader.model.VerseList;

/**
 * The display panel for the Bible Reader.
 * 
 * @author cusack
 * @author Jon Chaffer & Jason Gombas
 */
public class ResultView extends JPanel {
	private BibleReaderModel bibleModel;
	private JScrollPane scrollPane;
	private JEditorPane editorPane;
	private JTextArea stats;

	/**
	 * Construct a new ResultView and set its model to myModel.
	 * 
	 * @param myModel
	 *            The model this view will access to get information.
	 */
	public ResultView(BibleReaderModel myModel) {
		bibleModel = myModel;
		editorPane = new JEditorPane();
		scrollPane = new JScrollPane(editorPane);
		editorPane.setContentType("text/html");
		stats = new JTextArea("");
		setupGUI();
	}

	/**
	 * Sets up the main components of ResultView. Should be called once in the
	 * constructor.
	 */
	private void setupGUI() {
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		add(stats, BorderLayout.SOUTH);
	}

	/**
	 * Updates ResultView when a search has been made.
	 * 
	 * @param input
	 */
	public void updateSearch(String input) {
		editorPane.setText(getSearchHTML(input));
		editorPane.setCaretPosition(0);
	}

	/**
	 * Updates the ResultView when searching a passage.
	 * 
	 * @param input
	 */
	public void updatePassage(String input) {
		editorPane.setText(getPassageHTML(input));
		editorPane.setCaretPosition(0);
	}

	/**
	 * Updates the Stats JTextArea when search is pressed.
	 * 
	 * @param num
	 * @param input
	 */
	private void updateStatsSearch(int num, String input) {
		stats.setText("There are " + num + " verses containing " + input);
	}

	/**
	 * Updates the Stats JTextArea when passage is pressed.
	 * 
	 * @param input
	 */
	private void updateStatsPassage(String input) {
		stats.setText("You requested " + input);
	}

	/**
	 * Returns the HTML text from a search.
	 * 
	 * @param input
	 * @return A String which contains basic HTML code that formats search results
	 *         in a table format.
	 */
	private String getSearchHTML(String input) {
		StringBuffer html = new StringBuffer("");
		int num = 0;
		for (String version : bibleModel.getVersions()) {
			Bible bible = bibleModel.getBible(version);
			VerseList verses = bible.getVersesContaining(input);
			html.append("<table><tbody>");
			html.append("<tr><td valign=\"top\" width=\"100\">Verse</td><td>");
			html.append(version);
			html.append("</td>");
			for (Verse verse : verses) {
				html.append("<tr><td valign=\"top\">");
				html.append(verse.getReference().toString());
				html.append("</td><td>");
				html.append(verse.getText());
				html.append("</td></tr>");
				num += 1;
			}
			html.append("</tbody></table><br>");
		}
		updateStatsSearch(num, input);
		return html.toString();
	}

	/**
	 * Returns the HTML text from a passage search.
	 * 
	 * @param input
	 * @return A String which contains basic HTML code that formats passage search
	 *         results in a table format.
	 */
	private String getPassageHTML(String input) {
		StringBuffer html = new StringBuffer("");
		for (String version : bibleModel.getVersions()) {
			Bible bible = bibleModel.getBible(version);
			ReferenceList refs = bibleModel.getReferencesForPassage(input);
			html.append("<table><tbody>");
			html.append("<tr><td valign=\"top\" width=\"100\">Verse</td><td>");
			html.append(version);
			html.append("</td>");
			for (Reference ref : refs) {
				html.append("<tr><td valign=\"top\">");
				html.append(bible.getVerse(ref).getReference().toString());
				html.append("</td><td>");
				html.append(bible.getVerse(ref).getText());
				html.append("</td></tr>");
			}
			html.append("</tbody></table><br>");
		}
		updateStatsPassage(input);
		return html.toString();
	}
}
