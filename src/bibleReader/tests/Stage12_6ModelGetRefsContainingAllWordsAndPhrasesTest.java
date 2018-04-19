package bibleReader.tests;

// If you organize imports, the following import might be removed and you will
// not be able to find certain methods. If you can't find something, copy the
// commented import statement below, paste a copy, and remove the comments.
// Keep this commented one in case you organize imports multiple times.
//
// import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bibleReader.BibleIO;
import bibleReader.model.Bible;
import bibleReader.model.BibleFactory;
import bibleReader.model.BibleReaderModel;
import bibleReader.model.BookOfBible;
import bibleReader.model.MultiBibleModel;
import bibleReader.model.Reference;
import bibleReader.model.ReferenceList;
import bibleReader.model.Verse;
import bibleReader.model.VerseList;

/**
 * Tests for the single-word exact searching.
 * 
 * @author Chuck Cusack, March, 2013
 * 
 * @modified April, 2015. Several tests changed due to a slight bug in removing
 *           HTML tags in the 2013 and prior versions. The fix increased the
 *           number of results slightly in several cases.
 */
public class Stage12_6ModelGetRefsContainingAllWordsAndPhrasesTest {
	private static VerseList[] verses;
	private static MultiBibleModel model;
	private static String[] versions = new String[] { "kjv.atv", "asv.xmv", "esv.atv" };

	@BeforeClass
	public static void readFiles() {
		verses = new VerseList[versions.length];
		for (int i = 0; i < versions.length; i++) {
			File file = new File(versions[i]);
			verses[i] = BibleIO.readBible(file);
		}
		// We create the model once for all searches. That way if the lists in
		// the
		// Concordance class are getting messed up we can catch it.
		// Also because creating the model now takes a few seconds.
		model = new BibleReaderModel();
		for (int i = 0; i < versions.length; i++) {
			// Make a copy of the VerseList
			VerseList copyOfVerseList = new VerseList(verses[i].getVersion(), verses[i].getDescription(),
					new ArrayList<Verse>(verses[i]));
			Bible testBible = BibleFactory.createBible(copyOfVerseList);
			model.addBible(testBible);
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test(timeout = 250)
	public void testWithNoResultsOnePhrase() {
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("\"do unto others\"");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("\"blah\"");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("\"three wise men\"");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("\"pride comes before a fall\"");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("\"the lion shall lie down with the lamb\"");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("\"Jesus slept\"");
		assertEquals(0, results.size());
	}

	@Test(timeout = 250)
	public void testWithNoResultNoPhrases() {
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("righteousness");
		assertEquals(317, results.size());

		results = model.getReferencesContainingAllWordsAndPhrases("godly gods god");
		assertEquals(0, results.size());
		
		results = model.getReferencesContainingAllWordsAndPhrases("the son of god");
		assertEquals(184, results.size());
		
		results = model.getReferencesContainingAllWordsAndPhrases("god of the son");
		assertEquals(184, results.size());
		
		results = model.getReferencesContainingAllWordsAndPhrases("the god of son");
		assertEquals(184, results.size());
	}

	@Test(timeout = 250)
	public void testWithNoResultMultiplePhrases() {
		ReferenceList results = model
				.getReferencesContainingAllWordsAndPhrases("\"john the baptist\" \"kingdom of heaven\" \"greater than he\" \"among those\" \"I say to you\" \"hath not\"");
		assertEquals(0, results.size());
	}

	@Test(timeout = 250)
	public void testWithOneResultOnePhrase() {
		ReferenceList results = model
				.getReferencesContainingAllWordsAndPhrases("\"though the fig tree should not blossom\"");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Habakkuk, 3, 17), results.get(0));

		// Out of context (the LOVE OF money is the root of all evil).
		results = model.getReferencesContainingAllWordsAndPhrases("\"money is the root of all evil\"");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Timothy1, 6, 10), results.get(0));

		results = model.getReferencesContainingAllWordsAndPhrases("\"trust in the lord with all your heart\"");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Proverbs, 3, 5), results.get(0));
		
		results = model.getReferencesContainingAllWordsAndPhrases("\"jesus wept\"");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.John, 11, 35), results.get(0));
	}

	@Test(timeout = 250)
	public void testWithOneResultMultiplePhrases() {
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("\"jesus said\" \"to him\"  \"the sons\"");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Matthew, 17, 26), results.get(0));

		results = model
				.getReferencesContainingAllWordsAndPhrases("\"john the baptist\" \"kingdom of heaven\" \"greater than he\" \"among those\" \"I say to you\"");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Matthew, 11, 11), results.get(0));

	}

	@Test(timeout = 250)
	public void testWithFewResultsOnePhrase() {
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("\"son of god\" jesus");
		assertEquals(16, results.size());
	}	
	
	@Test(timeout = 250)
	public void testWithFewResultsTwoPhrases() {
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("\"son of god\" \"cried out\"");
		assertEquals(4, results.size());
		assertEquals(new Reference(BookOfBible.Matthew, 8, 29), results.get(0));
		assertEquals(new Reference(BookOfBible.Mark, 3, 11), results.get(1));
		assertEquals(new Reference(BookOfBible.Mark, 15, 39), results.get(2));
		assertEquals(new Reference(BookOfBible.Luke, 8, 28), results.get(3));
	}

	@Test(timeout = 250)
	public void testTwoPhrasesWithWeirdSpacing() {
		// Tricky case--when the quotes are right next to each other.
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("\"son of god\"\"cried out\"");
		assertEquals(4, results.size());
		assertEquals(new Reference(BookOfBible.Matthew, 8, 29), results.get(0));
		assertEquals(new Reference(BookOfBible.Mark, 3, 11), results.get(1));
		assertEquals(new Reference(BookOfBible.Mark, 15, 39), results.get(2));
		assertEquals(new Reference(BookOfBible.Luke, 8, 28), results.get(3));

		// Even worse--spaces at the beginning and end
		results = model.getReferencesContainingAllWordsAndPhrases("    \"son of god\"\"cried out\"   ");
		assertEquals(4, results.size());
		assertEquals(new Reference(BookOfBible.Matthew, 8, 29), results.get(0));
		assertEquals(new Reference(BookOfBible.Mark, 3, 11), results.get(1));
		assertEquals(new Reference(BookOfBible.Mark, 15, 39), results.get(2));
		assertEquals(new Reference(BookOfBible.Luke, 8, 28), results.get(3));
	}

	@Test(timeout = 250)
	public void testWithAndWithoutQuotes() {
		// without quotes
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("righteous one");
		assertEquals(34, results.size());
		// with quotes
		results = model.getReferencesContainingAllWordsAndPhrases("\"righteous one\"");
		assertEquals(9, results.size());

	}

	@Test(timeout = 250)
	public void testConcordanceNotModified() {
		// make sure the searches aren't messing up the lists in the
		// concordance.
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("three");
		assertEquals(448, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("king");
		assertEquals(1952, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("son");
		assertEquals(1838, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("king son");
		assertEquals(278, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("son king");
		assertEquals(278, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("son");
		assertEquals(1838, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("king");
		assertEquals(1952, results.size());
	}

	@Test(timeout = 500)
	public void testFirstWordNotInPhraseSearches() {
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("this is \"and he said\"");
		assertEquals(44, results.size());

		results = model.getReferencesContainingAllWordsAndPhrases("twelve \"this is it\"");
		assertEquals(1, results.size());

		results = model.getReferencesContainingAllWordsAndPhrases("and \"he said\" because");
		assertEquals(29, results.size());
		
		results = model.getReferencesContainingAllWordsAndPhrases("and \"he said\" \"because that\"");
		assertEquals(1, results.size());	

		results = model.getReferencesContainingAllWordsAndPhrases("and \"he was\" \"the one\"");
		assertEquals(4, results.size());

		results = model.getReferencesContainingAllWordsAndPhrases("and \"he was\" governor \"the one\"");
		assertEquals(1, results.size());
		
		results = model.getReferencesContainingAllWordsAndPhrases("no \"the of and or\"");
		assertEquals(0, results.size());

		results = model.getReferencesContainingAllWordsAndPhrases("the \"and the\" he was \"one\"");
		assertEquals(27, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("the \"and the\" he was \"the one\"");
		assertEquals(6, results.size());
		
		
	}
	@Test(timeout = 500)
	public void testExtremeSearches() {
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("\"and he said\"");
		assertEquals(627, results.size());

		results = model.getReferencesContainingAllWordsAndPhrases("\"the of and or\"");
		assertEquals(0, results.size());
	}

	@Test(timeout = 500)
	public void testPhraseWithApostropheS() {
		// should be 357, including several with "wife's".
		assertEquals(13, model.getReferencesContainingAllWordsAndPhrases("\"wife's\"").size());
	}

	@Test(timeout = 250)
	public void testTrickySearches() {
		// Empty double quotes.
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("\"\"");
		assertEquals(0, results.size());
		// space in quotes.
		results = model.getReferencesContainingAllWordsAndPhrases("\" \"");
		assertEquals(0, results.size());

		// Can you deal with commas inside quotes?
		// This should find one result
		results = model.getReferencesContainingAllWordsAndPhrases("\"and when jesus was baptized, immediately\"");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Matthew, 3, 16), results.get(0));

		// This should also find one result.
		results = model.getReferencesContainingAllWordsAndPhrases("and when jesus was baptized immediately");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Matthew, 3, 16), results.get(0));

		// But this one should NOT find any.
		results = model.getReferencesContainingAllWordsAndPhrases("\"and when jesus was baptized immediately\"");
		assertEquals(0, results.size());

		// Another one with several variations.
		results = model.getReferencesContainingAllWordsAndPhrases("\"who are you, my son?\"");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Genesis, 27, 18), results.get(0));

		results = model.getReferencesContainingAllWordsAndPhrases("who are you, my son?");
		assertEquals(3, results.size());
		assertEquals(new Reference(BookOfBible.Genesis, 27, 18), results.get(0));
		assertEquals(new Reference(BookOfBible.Jeremiah, 29, 21), results.get(1));
		assertEquals(new Reference(BookOfBible.Hebrews, 5, 5), results.get(2));

		results = model.getReferencesContainingAllWordsAndPhrases("who are you my son");
		assertEquals(3, results.size());
		assertEquals(new Reference(BookOfBible.Genesis, 27, 18), results.get(0));
		assertEquals(new Reference(BookOfBible.Jeremiah, 29, 21), results.get(1));
		assertEquals(new Reference(BookOfBible.Hebrews, 5, 5), results.get(2));
	}

	@Test(timeout = 250)
	public void testOddNumberOfQuotes() {
		// Just a single quote.
		ReferenceList results = model.getReferencesContainingAllWordsAndPhrases("\"");
		assertEquals(0, results.size());
		// Three quotes, without and with spaces.
		results = model.getReferencesContainingAllWordsAndPhrases("\"\"\"");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("\" \" \"");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("\"i am the one\" \"who");
		assertEquals(5, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("\"i am the one\" who");
		assertEquals(5, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("who \"i am the one\"");
		assertEquals(5, results.size());

		results = model.getReferencesContainingAllWordsAndPhrases("who am i");
		assertEquals(177, results.size());

		results = model.getReferencesContainingAllWordsAndPhrases("who am i\"");
		assertEquals(177, results.size());
		results = model.getReferencesContainingAllWordsAndPhrases("\"who am i");
		assertEquals(177, results.size());
	}
}
