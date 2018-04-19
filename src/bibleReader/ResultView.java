package bibleReader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bibleReader.model.BibleReaderModel;
import bibleReader.model.Reference;
import bibleReader.model.ReferenceList;

/**
 * The display panel for the Bible Reader.
 * 
 * @author cusack
 * @author Jonathan Chaffer
 */
public class ResultView extends JPanel {
	// constants for searchType
	public static final int NONE = 0;
	public static final int WORD = 1;
	public static final int PASSAGE = 2;

	// constant for number of results per page
	private static final int RESULTS_PER_PAGE = 20;

	// GUI components
	private BibleReaderModel bibleModel;
	private JScrollPane scrollPane;
	private JEditorPane editorPane;
	private JTextArea stats;
	private JTextArea pageDisplay;
	public JButton nextButton;
	public JButton previousButton;

	// other fields
	private int currentPage = 1;
	private ReferenceList results;
	private int searchType = NONE;
	private String lastInput = "";

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
		// make the layout
		setLayout(new BorderLayout());
		JPanel lower = new JPanel();
		lower.setLayout(new FlowLayout());
		add(lower, BorderLayout.SOUTH);

		// results pane
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setName("OutputEditorPane");
		scrollPane = new JScrollPane(editorPane);
		editorPane.setContentType("text/html");
		add(scrollPane, BorderLayout.CENTER);

		// stats
		stats = new JTextArea("");
		pageDisplay = new JTextArea("");
		stats.setEditable(false);
		lower.add(stats);

		// previous button
		previousButton = new JButton("Previous");
		previousButton.setEnabled(false);
		previousButton.setName("PreviousButton");
		lower.add(previousButton);

		// next button
		nextButton = new JButton("Next");
		nextButton.setEnabled(false);
		nextButton.setName("NextButton");
		lower.add(nextButton);

		// page display
		lower.add(pageDisplay);

		// add listeners to next and previous buttons
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				incrementPage();
			}
		});

		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decrementPage();
			}
		});
	}

	/**
	 * Updates the results when a word search has been made.
	 * 
	 * @param input
	 *            The string according to which to update results.
	 */
	public void updateSearchResults(String input) {
		searchType = WORD;
		lastInput = input;
		results = bibleModel.getReferencesContaining(input);
		currentPage = 1;
		renderSearch();
	}

	/**
	 * Updates the results when a passage search has been made.
	 * 
	 * @param input
	 *            The string according to which to update results.
	 */
	public void updatePassageResults(String input) {
		searchType = PASSAGE;
		lastInput = input;
		results = bibleModel.getReferencesForPassage(input);
		currentPage = 1;
		renderPassage();
	}

	/**
	 * Renders word search results in the results pane.
	 */
	private void renderSearch() {
		StringBuffer html = new StringBuffer("");

		if (results.isEmpty()) {
			stats.setText("No results for \"" + lastInput + "\".");
			editorPane.setText("");
		} else {
			// make the html
			html.append("<div style=\"font-family:arial\"><table><tbody>");
			html.append("<tr><td valign=\"top\" width=\"100\">Verse</td>");
			for (String version : bibleModel.getVersions()) {
				html.append("<td><center>");
				html.append(version);
				html.append("</center></td>");
			}
			html.append("</tr>");
			for (Reference ref : getPageSublist(currentPage)) {
				html.append("<tr><td valign=\"top\">");
				html.append(ref.toString());
				html.append("</td>");
				for (String version : bibleModel.getVersions()) {
					html.append("<td valign=\"top\">");
					html.append(bibleModel.getText(version, ref));
					html.append("</td>");
				}
				html.append("</tr>");
			}
			html.append("</tbody></table><br></div>");

			String bolded = html.toString().replaceAll("(?i)" + lastInput, "<b>$0</b>");
			editorPane.setText(bolded);

			stats.setText("There are " + results.size() + " verses containing \"" + lastInput + "\".");
			pageDisplay.setText("Displaying page " + currentPage + " of " + getNumberOfPages() + ".");
		}

		setPrevNextButtons();

		editorPane.setCaretPosition(0);
	}

	/**
	 * Renders passage search results in the results pane.
	 */
	private void renderPassage() {
		StringBuffer html = new StringBuffer("");

		if (results.isEmpty()) {
			stats.setText("No results for \"" + lastInput + "\".");
			editorPane.setText("");
		} else {
			// make the page title
			String title;
			Reference firstRef = getPageSublist(currentPage).get(0);
			Reference lastRef = getPageSublist(currentPage).get(getPageSublist(currentPage).size() - 1);
			// TODO make ur own
			if (firstRef.getBookOfBible().equals(lastRef.getBookOfBible())) {
				if (firstRef.getChapter() == lastRef.getChapter()) {
					title = firstRef.toString() + "-" + lastRef.getVerse();
				} else {
					title = firstRef.toString() + "-" + lastRef.getChapter() + ":" + lastRef.getVerse();
				}
			} else {
				title = firstRef.toString() + "-" + lastRef.toString();
			}

			// make the html
			html.append("<div style=\"font-family:arial\"><br><center><b>" + title + "</b></center><br>");
			html.append("<table><tbody>");
			html.append("<tr>");
			for (String version : bibleModel.getVersions()) {
				html.append("<td><center>");
				html.append(version);
				html.append("</center></td>");
			}
			html.append("</tr>");
			html.append("<tr>");
			for (String version : bibleModel.getVersions()) {
				html.append("<td valign=\"top\">");
				for (Reference ref : getPageSublist(currentPage)) {
					if (!bibleModel.getText(version, ref).equals("")) {
						if (ref.getVerse() == 1) {
							html.append("<br><b>");
							html.append(ref.getChapter());
							html.append("</b> ");
						} else {
							html.append("<sup>");
							html.append(ref.getVerse());
							html.append("</sup>");
						}
						html.append(bibleModel.getText(version, ref));
					}
				}
				html.append("</td>");
			}
			html.append("</tr></tbody></table><br></div>");

			stats.setText("You requested \"" + lastInput + "\". There are " + results.size() + " results.");
			pageDisplay.setText("Displaying page " + currentPage + " of " + getNumberOfPages() + ".");

			editorPane.setText(html.toString());
		}

		setPrevNextButtons();

		editorPane.setCaretPosition(0);
	}

	/**
	 * Increments the page and renders the results for the new page (unless
	 * already at the last page).
	 */
	private void incrementPage() {
		if (currentPage + 1 > getNumberOfPages())
			return;
		currentPage++;
		if (searchType == WORD) {
			renderSearch();
		} else if (searchType == PASSAGE) {
			renderPassage();
		}
	}

	/**
	 * Decrements the page and renders the results for the new page (unless
	 * already at the first page).
	 */
	private void decrementPage() {
		if (currentPage == 1)
			return;
		currentPage--;
		if (searchType == WORD) {
			renderSearch();
		} else if (searchType == PASSAGE) {
			renderPassage();
		}
	}

	/**
	 * Returns a sublist of references for a specified page.
	 * 
	 * @return A sublist of references for a specified page.
	 */
	private ReferenceList getPageSublist(int page) {
		if (results.size() <= RESULTS_PER_PAGE) {
			return results;
		}
		int startIndex = ((page - 1) * RESULTS_PER_PAGE);
		int endIndex = (page * RESULTS_PER_PAGE);
		if (endIndex >= results.size()) {
			endIndex = results.size();
		}
		return new ReferenceList(results.subList(startIndex, endIndex));
	}

	/**
	 * Returns the last search type.
	 * 
	 * @return The last search type.
	 */
	public int getLastSearchType() {
		return searchType;
	}

	/**
	 * Returns the last input.
	 * 
	 * @return The last input.
	 */
	public String getLastInput() {
		return lastInput;
	}

	/**
	 * Returns the number of pages.
	 * 
	 * @return The number of pages.
	 */
	private int getNumberOfPages() {
		return (int) Math.ceil(results.size() / 20.0);
	}

	/**
	 * Set whether the previous and next buttons should be enabled based on the
	 * current page.
	 */
	private void setPrevNextButtons() {
		if (currentPage == 1) {
			previousButton.setEnabled(false);
		} else {
			previousButton.setEnabled(true);
		}
		if (currentPage >= getNumberOfPages()) {
			nextButton.setEnabled(false);
		} else {
			nextButton.setEnabled(true);
		}
	}
}