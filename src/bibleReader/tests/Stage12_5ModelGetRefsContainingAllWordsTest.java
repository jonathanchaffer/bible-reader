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
public class Stage12_5ModelGetRefsContainingAllWordsTest {
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
	public void testGetReferenceContainingWithNoResults() {
		ReferenceList results = model.getReferencesContainingAllWords("fig tree blossom earth");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWords("earth tree fig blossom");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWords("blah");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWords("blah god");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWords("god blah");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWords("blah foo ferzle");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWords("peace piece");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWords("god gods godly");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWords("gods god godly");
		assertEquals(0, results.size());
		results = model.getReferencesContainingAllWords("godly gods god");
		assertEquals(0, results.size());
	}

	@Test(timeout = 250)
	public void testGetReferenceContainingWithOneResult() {
		ReferenceList results = model.getReferencesContainingAllWords("trouble very soon");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Judges, 11, 35), results.get(0));

		results = model.getReferencesContainingAllWords("within month");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Ezra, 10, 9), results.get(0));

		results = model.getReferencesContainingAllWords("trust in the lord with all your heart");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Proverbs, 3, 5), results.get(0));

		results = model.getReferencesContainingAllWords("yesterday was");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Samuel1, 20, 27), results.get(0));

		results = model.getReferencesContainingAllWords("pride before fall");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Proverbs, 16, 18), results.get(0));

		results = model.getReferencesContainingAllWords("grandmother");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Timothy2, 1, 5), results.get(0));

		results = model.getReferencesContainingAllWords("reverend");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Psalms, 111, 9), results.get(0));
	}

	@Test(timeout = 250)
	public void testGetReferenceContainingWithFewResults() {
		ReferenceList results = model.getReferencesContainingAllWords("Melchizedek king");
		assertEquals(2, results.size());
		assertEquals(new Reference(BookOfBible.Genesis, 14, 18), results.get(0));
		assertEquals(new Reference(BookOfBible.Hebrews, 7, 1), results.get(1));

		results = model.getReferencesContainingAllWords("king Melchizedek");
		assertEquals(2, results.size());
		assertEquals(new Reference(BookOfBible.Genesis, 14, 18), results.get(0));
		assertEquals(new Reference(BookOfBible.Hebrews, 7, 1), results.get(1));

		results = model.getReferencesContainingAllWords("CHRISTIAN");
		assertEquals(2, results.size());
		results = model.getReferencesContainingAllWords("pride fall");
		assertEquals(6, results.size());

	}

	@Test(timeout = 250)
	public void testGetReferencesContainingWithManyResults() {
		ReferenceList results = model.getReferencesContainingAllWords("righteousness");
		assertEquals(317, results.size());

		results = model.getReferencesContainingAllWords("righteous");
		assertEquals(311, results.size());

		results = model.getReferencesContainingAllWords("the son of god");
		assertEquals(184, results.size());
		results = model.getReferencesContainingAllWords("god of the son");
		assertEquals(184, results.size());
		results = model.getReferencesContainingAllWords("the god of son");
		assertEquals(184, results.size());

		// make sure the searches aren't messing up the lists in the
		// concordance.
		results = model.getReferencesContainingAllWords("three");
		assertEquals(448, results.size());
		results = model.getReferencesContainingAllWords("king");
		assertEquals(1952, results.size());
		results = model.getReferencesContainingAllWords("son");
		assertEquals(1838, results.size());
		results = model.getReferencesContainingAllWords("king son");
		assertEquals(278, results.size());
		results = model.getReferencesContainingAllWords("son king");
		assertEquals(278, results.size());
		results = model.getReferencesContainingAllWords("son");
		assertEquals(1838, results.size());
		results = model.getReferencesContainingAllWords("king");
		assertEquals(1952, results.size());
	}

	@Test(timeout = 250)
	public void testWithCommas() {
		// Can you deal with commas?
		ReferenceList results = model.getReferencesContainingAllWords("and when jesus was baptized, immediately");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Matthew, 3, 16), results.get(0));
	}

	@Test(timeout = 250)
	public void testConcordanceNotModified() {
		// make sure the searches aren't messing up the lists in the
		// concordance.
		ReferenceList results = model.getReferencesContainingAllWords("three");
		assertEquals(448, results.size());
		results = model.getReferencesContainingAllWords("king");
		assertEquals(1952, results.size());
		results = model.getReferencesContainingAllWords("son");
		assertEquals(1838, results.size());
		results = model.getReferencesContainingAllWords("king son");
		assertEquals(278, results.size());
		results = model.getReferencesContainingAllWords("son king");
		assertEquals(278, results.size());
		results = model.getReferencesContainingAllWords("son");
		assertEquals(1838, results.size());
		results = model.getReferencesContainingAllWords("king");
		assertEquals(1952, results.size());
	}
	
	@Test(timeout = 250)
	public void testEmptyString() {
		// Empty string should return no results.
		ReferenceList results = model.getReferencesContainingAllWords("");
		assertEquals(0, results.size());
	}

	@Test(timeout = 250)
	public void testExtremeSearches1() {
		ReferenceList results = model.getReferencesContainingAllWords("the");
		assertEquals(25245, results.size());
		
		results = model.getReferencesContainingAllWords("of");
		assertEquals(19762, results.size());
		
		results = model.getReferencesContainingAllWords("a");
		assertEquals(8194, results.size());
	}

	@Test(timeout = 250)
	public void testExtremeSearches2() {
		ReferenceList results = model.getReferencesContainingAllWords("the of and or");
		assertEquals(493, results.size());
	}

	@Test(timeout = 250)
	public void testExtremeSearches3() {
		// If you are clever, this one should take no longer than searching for
		// "the".
		// If you are not clever, it will take too long.
		ReferenceList results = model.getReferencesContainingAllWords("the tHE THE ThE THe");
		assertEquals(25245, results.size());
	}

	@Test(timeout = 250)
	public void testExtremeSearches4() {
		// This is the toughest one.
		ReferenceList results = model.getReferencesContainingAllWords("the of and");
		assertEquals(14709, results.size());
	}
}
