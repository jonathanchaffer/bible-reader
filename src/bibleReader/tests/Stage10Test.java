package bibleReader.tests;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JTextField;

import student.GUITestCase;
import bibleReader.BibleReaderApp;

/**
 * Tests for the BibleReader Stage 10--Limiting results, formatting output, etc.
 * 
 * @author cusack
 */
public class Stage10Test extends GUITestCase {

	BibleReaderApp br;
	private JTextField input;
	private JButton search;
	private JButton passage;
	private JButton next;
	private JButton previous;
	private JEditorPane output;

	public void setUp() throws Exception {
		br = new BibleReaderApp();
		input = getComponent(JTextField.class, "InputTextField");
		search = getComponent(JButton.class, "SearchButton");
		passage = getComponent(JButton.class, "PassageButton");
		next = getComponent(JButton.class, "NextButton");
		previous = getComponent(JButton.class, "PreviousButton");
		output = getComponent(JEditorPane.class, "OutputEditorPane");
	}

	public void testSearchResultsPage1() {
		// Just check that the number of words on the first page is correct.
		enterText(input, "eaten");
		click(search);
		// Page 1
		assertEquals("Wrong number of occurrences on page 1", 57, countOccurrences(output.getText(), "eaten"));
	}

	public void testSearchResultsPage1Bolded() {
		// Just check that the number of words on the first page is correct and
		// that they are bolded.
		enterText(input, "eaten");
		click(search);
		assertEquals("Wrong number of occurrences on page 1", 57, countOccurrences(output.getText(), "<b>eaten</b>"));
	}

	public void testSearchResultsPages2AndBeyond() {
		// Pages 2-8
		int[] timesOnPage = { 57, 56, 54, 61, 49, 39, 49, 34, 28 };

		enterText(input, "eaten");
		click(search);
		for (int i = 1; i < timesOnPage.length; i++) {
			int page = i + 1;
			click(next);
			// Make sure it occurs the right number of times.
			assertEquals("Wrong number of occurrences on page " + page, timesOnPage[i],
					countOccurrences(output.getText(), "eaten"));
		}
	}

	public void testSearchResultsPages2AndBeyondBolded() {
		// Pages 2-8
		int[] timesOnPage = { 57, 56, 54, 61, 49, 39, 49, 34, 28 };

		enterText(input, "eaten");
		click(search);
		for (int i = 1; i < timesOnPage.length; i++) {
			int page = i + 1;
			click(next);
			// Make sure it occurs the right number of times, bolded each time.
			assertEquals("Wrong number of occurrences on page " + page + ".  I think you forgot to bold it.",
					timesOnPage[i], countOccurrences(output.getText(), "<b>eaten</b>"));
		}
	}

	public void testPreviousNextEnabledAndDisabledCorrectly() {
		// Navigation works and previous/next enabled and disabled as they
		// should be.
		enterText(input, "eaten");
		click(search);
		// Page 1
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertTrue("Next should be enabled", next.isEnabled());

		// Pages 2-8
		for (int i = 1; i < 8; i++) {
			int page = i + 1;
			click(next);
			assertTrue("Previous should be enabled on page " + page, previous.isEnabled());
			assertTrue("Next should be enabled on page " + page, next.isEnabled());
		}
		// Page 9
		// The last page is different so we do it after the loop.
		click(next);
		assertTrue("Previous should be enabled", previous.isEnabled());
		assertFalse("Next should be disabled", next.isEnabled());
	}

	public void testPreviousAndNextBackAndForthWorks() {
		// Navigation works and previous/next enabled and disabled as they
		// should be.
		// (Doesn't test the actual results on the page).
		enterText(input, "eaten");
		click(search);
		clickMultiple(next, 8);
		assertTrue("Previous should be enabled", previous.isEnabled());
		assertFalse("Next should be disabled", next.isEnabled());

		// Make sure you can go back and forth and have the buttons still work
		// properly.
		clickMultiple(previous, 3);

		clickMultiple(next, 3);
		assertFalse("Next should be disabled", next.isEnabled());
		assertTrue("Previous should be enabled", previous.isEnabled());

		clickMultiple(previous, 9);
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertTrue("Next should be enabled", next.isEnabled());
	}

	public void testSinglePageSearchResults() {
		// -----------------------------------------------------------------------------
		// Try a word search that returns one page.
		//
		enterText(input, "wagon");
		click(search);
		assertEquals("Wrong number of occurrences of wagon", 36, countOccurrences(output.getText(), "wagon"));
	}

	public void testSinglePageSearchNavigation() {
		// -----------------------------------------------------------------------------
		// Try a word search that returns one page.
		//
		enterText(input, "wagon");
		click(search);
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertFalse("Next should be disabled", next.isEnabled());
	}

	public void testSinglePagePassageResults() {
		// -----------------------------------------------------------------------------------
		// Test that with a single passage result, the previous/next are
		// disabled as they should be.
		enterText(input, "3 John");
		click(passage);
		assertEquals("Wrong number of occurrences of wagon", 3, countOccurrences(output.getText(), "Diotrephes"));
	}

	public void testSinglePagePassageNavigation() {
		// -----------------------------------------------------------------------------------
		// Test that with a single passage result, the previous/next are
		// disabled as they should be.
		enterText(input, "3 John");
		click(passage);
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertFalse("Next should be disabled", next.isEnabled());
	}

	public void testSearchResultsPage11() {
		// -----------------------------------------------------------------------------------
		// Test when the number of results is exactly a multiple of 20.
		// We'll just check the number of occurrences on the final page.
		enterText(input, "fool");
		click(search);
		clickMultiple(next, 10);
		assertEquals("Wrong number of occurrences on page 10", 54, countOccurrences(output.getText(), "fool"));
	}

	public void testnoResultsForPassage() {
		// Put some valid content in the results
		enterText(input, "fool");
		click(search);

		// Click the passage button. Now it shouldn't have any results and
		// buttons
		// should disable.
		enterText(input, "fool");
		click(passage);
		// Can only test that the buttons are disabled since I didn't give them
		// explicit instructions
		// about what to display if there were no results.
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertFalse("Next should be disabled", next.isEnabled());
	}

	public void testNoResultsForSearch() {
		// Click the passage button. It shouldn't have any results and buttons
		// should disable.
		enterText(input, "3 John");
		click(search);
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertFalse("Next should be disabled", next.isEnabled());
	}

	public void testPreviousNextEnabledAndDisabledWithMultipleof20() {
		// -----------------------------------------------------------------------------------
		// Test when the number of results is exactly a multiple of 20.
		enterText(input, "fool");
		click(search);
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertTrue("Next should be enabled", next.isEnabled());

		// Just check one in the middle.
		clickMultiple(next, 5);
		assertTrue("Previous should be enabled", previous.isEnabled());
		assertTrue("Next should be enabled", next.isEnabled());

		// Check at the end.
		clickMultiple(next, 5);
		assertTrue("Previous should be enabled", previous.isEnabled());
		assertFalse("Next should be disabled", next.isEnabled());
	}

	public void testPassageDisplaysReferencePage1() {
		// When displaying 3 John, the reference "3 John 1:1-1:15" should occur
		// at the top of the results page.
		enterText(input, "3 John");
		click(passage);

		String text = output.getText();
		assertTrue("The title of the third page should be '3 John 1:1-15'", text.contains("3 John 1:1-15"));
	}

	public void testPassageDisplaysReferencePage3() {
		// On the third page of Ephesians, the reference "Ephesians 2:18-3:15"
		// should occur at the top of page.
		enterText(input, "Ephesians");
		click(passage);
		click(next);
		click(next);
		String page3 = output.getText();
		assertTrue("The title of the third page should be 'Ephesians 2:18-3:15'", page3.contains("Ephesians 2:18-3:15"));
	}

	public void testSuptagesWithMissingVerse() {
		enterText(input, "3 John");
		click(passage);
		String text = output.getText();

		int numberFifteens = countOccurrences(text, ">15<");
		assertEquals("Only ESV has verse 15, so the verse number should only appear once in your results.", 1,
				numberFifteens);
	}

	public void testSupTagsPage1() {
		enterText(input, "3 John");
		click(passage);
		String text = output.getText();
		// Spot check a few.
		int numFours = countOccurrences(text, ">4<");
		assertEquals(3, numFours);
		int numFourteens = countOccurrences(text, ">14<");
		assertEquals(3, numFourteens);
	}

	public void testSupTagsForChapter() {
		enterText(input, "James");
		click(passage);
		click(next);
		String page2 = output.getText();
		// We don't know if there will be bold, sup, or whatever tags
		// immediately around the verse numbers,
		// but we do expect SOME tag to be around them.
		int number2s = countOccurrences(page2, ">2<");
		assertEquals("2 should occur inside 6 tags on this page--3 times to indicate chapter 2, "
				+ "and 3 times to indicate verse 2.", 6, number2s);
	}

	public void testPassageResultsPage1() {
		enterText(input, "Ephesians");
		click(passage);
		String page1 = output.getText();
		assertEquals(6, countOccurrences(page1, "heavenly"));
	}

	public void testPassageResultsPage8() {
		enterText(input, "Ephesians");
		click(passage);
		clickMultiple(next, 7);
		assertEquals(2, countOccurrences(output.getText(), "incorruptible"));
	}

	public void testPassageResultsNavigation() {
		enterText(input, "Ephesians");
		click(passage);
		for (int i = 1; i < 7; i++) {
			int page = i + 1;
			click(next);
			assertTrue("Previous should be enabled on page " + page, previous.isEnabled());
			assertTrue("Next should be enabled on page " + page, next.isEnabled());
		}
		// Page 8
		// The last page is different so we do it after the loop.
		click(next);
		assertTrue("Previous should be enabled", previous.isEnabled());
		assertFalse("Next should be disabled", next.isEnabled());
	}

	public void testPassageResultOrderOneVerse() {
		// Test order based on text from the same verse.
		enterText(input, "Ephesians");
		click(passage);
		String page1 = output.getText();
		// The Java HTML renderer modifies the text, so it will not be exactly
		// what we expect.
		// Hopefully if we remove all of the white spaces we will get more
		// accurate comparisons.
		page1 = page1.replaceAll("\\s+", "");

		int asvIndex = page1.indexOf("tosumupallthingsinChrist,");
		int esvIndex1 = page1.indexOf("touniteallthingsinhim,");
		int kjvIndex = page1.indexOf("hemightgathertogetherinoneallthingsinChrist,");
		assertTrue(asvIndex >= 0);
		assertTrue(esvIndex1 >= 0);
		assertTrue(kjvIndex >= 0);
		assertTrue("The results for ASV should be before the results for ESV", asvIndex < esvIndex1);
		assertTrue("The results for ESV should be before the results for KJV", esvIndex1 < kjvIndex);

	}

	public void testPassageResultOrderWholePassage() {
		// -----------------------------------------------------------------------------------------------
		// Test order based on the whole passage (So the end of the first result
		// should occur before the
		// beginning of the second result, and similarly for the 2nd and 3rd).
		enterText(input, "Habakkuk 3");
		click(passage);
		String outputText = output.getText();

		// Make sure the results for the 3 versions appear in the correct order.
		// This one is more demanding than the previous one. We compare the
		// ending of
		// the ASV version with the beginning of the ESV one since they should
		// be in
		// paragraph form so the entire text of the ASV result should be before
		// the ESV result.
		// Similarly for the ESV/KJV.
		// The Java HTML renderer modifies the text, so it will not be exactly
		// what we expect.
		// Hopefully if we remove all of the white spaces we will get more
		// accurate comparisons.
		outputText = outputText.replaceAll("\\s+", "");
		int asvIndex = outputText.indexOf("Andwillmakemetowalkuponmyhighplaces.");
		int esvIndex1 = outputText.indexOf("Ihaveheardthereportofyou,");
		int esvIndex2 = outputText.indexOf("Tothechoirmaster:");
		int kjvIndex = outputText.indexOf("AprayerofHabakkuktheprophetuponShigionoth");
		assertTrue(asvIndex >= 0);
		assertTrue(esvIndex1 >= 0);
		assertTrue(esvIndex2 >= 0);
		assertTrue(kjvIndex >= 0);
		assertTrue("For passages, all of the results for ASV should be before all of the results for ESV",
				asvIndex < esvIndex1);
		assertTrue("For passages, all of the results for ESV should be before all of the results for KJV",
				esvIndex2 < kjvIndex);
	}

	// ---------------------------------------------------------------------
	// The following 4 tests were written by Hana Vanderveen, edited by Chuck
	// Cusack.
	public void testMultiplePassagesResults() {
		// Test that run multiple passage searches back to back returns the
		// proper results
		enterText(input, "John 1-10");
		click(passage);

		// Check that the first page contains the expected passage selection
		assertTrue("The title of the results should contain John 1:1-20", output.getText().contains("John 1:1-20"));

		// Go to the end of the results.
		clickMultiple(next, 23);
		
		// There should be no more results but there should be previous results
		assertFalse("Next should be disabled", next.isEnabled());
		assertTrue("Previous should be enabled", previous.isEnabled());

		// Now redo the same search.
		click(passage);
		// Did we revert back to the beginning of the results as we should have?
		assertTrue("The title of the results should contain John 1:1-20", output.getText().contains("John 1:1-20"));
		// Are the previous/next buttons disabled/enabled as they should be
		// after the new search?
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertTrue("Next should be enabled", next.isEnabled());
		
		click(next);
		assertTrue("The title of the results should contain John 1:21-40", output.getText().contains("John 1:21-40"));
	}

	public void testMultipleSearchResults() {
		// Test the running multiple searches for phrases back to back returns
		// the proper results
		enterText(input, "goats");
		click(search);
		clickMultiple(next, 5);
		// There should be no more results but there should be previous results
		assertFalse("Next should be disabled", next.isEnabled());
		assertTrue("Previous should be enabled", previous.isEnabled());

		// Do a new search
		enterText(input, "golden");
		click(search);
		// Are the previous/next buttons disabled/enabled as they should be
		// after the new search?
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertTrue("Next should be enabled", next.isEnabled());

		// Check whether a completely new search was conducted, or if the page
		// navigation retrieves results from the previous search
		assertEquals("Wrong number of occurrences of golden on page 1", 46,
				countOccurrences(output.getText(), "golden"));
		clickMultiple(next, 3);
		assertEquals("Wrong number of occurrences of golden on page 4", 66,
				countOccurrences(output.getText(), "golden"));
	}

	public void testSearchThenPassage() {
		enterText(input, "granted");
		click(search);
		
		clickMultiple(next, 2);
		// There should be no more results but there should be previous results
		assertFalse("Next should be disabled", next.isEnabled());
		assertTrue("Previous should be enabled", previous.isEnabled());

		enterText(input, "Psalm 27-28");
		click(passage);
		// Are the previous/next buttons disabled/enabled as they should be
		// after the new search?
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertTrue("Next should be enabled", next.isEnabled());
		// Are the contents of the page correct (We'll just check the title)
		assertTrue("The title of the first page should contain Psalms 27:1-28:6",
				output.getText().contains("Psalms 27:1-28:6"));
		// Try the next page, too.
		click(next);
		assertTrue("The title of the second page should contain Psalms 28:7-9",
				output.getText().contains("Psalms 28:7-9"));
	}

	public void testPassageThenSearch() {
		// Start with a passage search
		enterText(input, "Ruth");
		click(passage);
		clickMultiple(next, 4);
		// There should be no more results, but there should be previous
		// results.
		assertFalse("Next should be disabled", next.isEnabled());
		assertTrue("Previous should be enabled", previous.isEnabled());

		// New search--this time a word search.
		enterText(input, "goats");
		click(search);

		// Are the previous/next buttons disabled/enabled as they should be
		// after the new search?
		assertFalse("Previous should be disabled", previous.isEnabled());
		assertTrue("Next should be enabled", next.isEnabled());

		assertEquals("Wrong number of occurrences of goats on page 1.", 53, countOccurrences(output.getText(), "goats"));
		click(next);
		assertEquals("Wrong number of occurrences of goats on page 2.", 34, countOccurrences(output.getText(), "goats"));

	}

	// ------------------------------------------------------------------
	// Helper methods.
	private void clickMultiple(JButton button, int times) {
		for (int i = 0; i < times; i++) {
			click(button);
		}
	}

	private int countOccurrences(String text, String wordToCount) {
		int occurrences = 0;
		int index = 0;
		while (index < text.length() && (index = text.indexOf(wordToCount, index)) >= 0) {
			occurrences++;
			index += wordToCount.length();
		}
		return occurrences;
	}
}
