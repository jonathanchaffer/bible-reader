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
 * @author Jonathan Chaffer, Jason Gombas, & Jacob Lahr (2018)
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
		setupGUI();
	}

	/**
	 * Sets up the main components of ResultView. Should be called once in the
	 * constructor.
	 */
	private void setupGUI() {
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		scrollPane = new JScrollPane(editorPane);
		editorPane.setContentType("text/html");
		stats = new JTextArea("");
		stats.setEditable(false);
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
		StringBuffer html = new StringBuffer("");
		int num = 0;
		html.append("<table><tbody>");
		html.append("<tr><td valign=\"top\" width=\"100\">Verse</td>");
		for (String version : bibleModel.getVersions()) {
			html.append("<td>");
			html.append(version);
			html.append("</td>");
		}
		html.append("</tr>");
		for (Reference ref : bibleModel.getReferencesContaining(input)) {
			html.append("<tr><td valign=\"top\">");
			html.append(ref.toString());
			html.append("</td>");
			for (String version : bibleModel.getVersions()) {
				html.append("<td valign=\"top\">");
				html.append(bibleModel.getText(version, ref));
				html.append("</td>");
			}
			html.append("</tr>");
			num += 1;
		}
		html.append("</tbody></table><br>");
		
		stats.setText("There are " + num + " verses containing " + input);
		
		editorPane.setText(html.toString());
		
		if (num == 0) {
			stats.setText("No results.");
		}
		
		editorPane.setCaretPosition(0);
	}

	/**
	 * Updates the ResultView when searching a passage.
	 * 
	 * @param input
	 */
	public void updatePassage(String input) {
		StringBuffer html = new StringBuffer("");
		int num = 0;
		html.append("<table><tbody>");
		html.append("<tr><td valign=\"top\" width=\"100\">Verse</td>");
		for (String version : bibleModel.getVersions()) {
			html.append("<td>");
			html.append(version);
			html.append("</td>");
		}
		html.append("</tr>");
		for (Reference ref : bibleModel.getReferencesForPassage(input)) {
			html.append("<tr><td valign=\"top\">");
			html.append(ref.toString());
			html.append("</td>");
			for (String version : bibleModel.getVersions()) {
				html.append("<td valign=\"top\">");
				html.append(bibleModel.getText(version, ref));
				html.append("</td>");
			}
			html.append("</tr>");
			num += 1;
		}
		html.append("</tbody></table><br>");
		
		stats.setText("You requested " + input + ". There are " + num + " results.");
		
		editorPane.setText(html.toString());
		
		if (num == 0) {
			stats.setText("No results.");
		}
		
		editorPane.setCaretPosition(0);
	}
}