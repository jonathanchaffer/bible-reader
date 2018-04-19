package bibleReader.tests;

// If you organize imports, the following import might be removed and you will
// not be able to find certain methods. If you can't find something, copy the
// commented import statement below, paste a copy, and remove the comments.
// Keep this commented one in case you organize imports multiple times.
//
// import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bibleReader.BibleIO;
import bibleReader.model.Bible;
import bibleReader.model.BibleFactory;
import bibleReader.model.BookOfBible;
import bibleReader.model.Concordance;
import bibleReader.model.Reference;
import bibleReader.model.ReferenceList;
import bibleReader.model.VerseList;

/**
 * Tests for the concordance class. They aren't very precise, but should be good
 * enough.
 * 
 * @author Chuck Cusack, March, 2013
 * 
 * @modified April, 2015. Several tests changed due to a slight bug in removing
 *           HTML tags in the 2013 and prior versions. The fix increased the
 *           number of results slightly in several cases.
 */
public class Stage12_2ConcordanceGRCTest {
	// We'll just run the tests on the ESV since it is the hardest version to
	// deal with.
	private static Concordance concordance;

	@BeforeClass
	public static void readFilesAndCreateConcordance() {
		File file = new File("esv.atv");
		VerseList verses = BibleIO.readBible(file);
		Bible bible = BibleFactory.createBible(verses);
		concordance = BibleFactory.createConcordance(bible);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test(timeout = 500)
	public void testCorrectOrder() {
		ReferenceList refs = concordance.getReferencesContaining("eaten");
		// Are the first 4 correct?
		assertEquals(new Reference(BookOfBible.Genesis, 3, 11), refs.get(0));
		assertEquals(new Reference(BookOfBible.Genesis, 3, 17), refs.get(1));
		assertEquals(new Reference(BookOfBible.Genesis, 6, 21), refs.get(2));
		assertEquals(new Reference(BookOfBible.Genesis, 14, 24), refs.get(3));
		// Are the last 3 correct?
		assertEquals(new Reference(BookOfBible.Acts, 27, 38), refs.get(81));
		assertEquals(new Reference(BookOfBible.James, 5, 2), refs.get(82));
		assertEquals(new Reference(BookOfBible.Revelation, 10, 10), refs.get(83));
		// Good. Chances are the rest are correct.
	}

	@Test(timeout = 250)
	public void testGetReferenceContainingWithMultipleWords_SW() {
		// These don't work because it is searching for a single word and these
		// won't match a single word.
		ReferenceList results = concordance.getReferencesContaining("son of god");
		assertEquals(0, results.size());
		results = concordance.getReferencesContaining("three wise men");
		assertEquals(0, results.size());
	}

	@Test(timeout = 250)
	public void testGetReferenceContainingWithNoResults_SW() {
		ReferenceList results = concordance.getReferencesContaining("trinity");
		assertEquals(0, results.size());
		results = concordance.getReferencesContaining("neo");
		assertEquals(0, results.size());
		results = concordance.getReferencesContaining("cat");
		assertEquals(0, results.size());
		results = concordance.getReferencesContaining("reverend");
		assertEquals(0, results.size());
	}

	@Test(timeout = 250)
	public void testGetReferenceContainingWithOneResult_SW() {
		ReferenceList results = concordance.getReferencesContaining("Christians");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Acts, 11, 26), results.get(0));

		results = concordance.getReferencesContaining("grandmother");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Timothy2, 1, 5), results.get(0));
	}

	@Test(timeout = 250)
	public void testGetReferenceContainingWithFewResults_SW() {
		ReferenceList results = concordance.getReferencesContaining("Melchizedek");
		assertEquals(10, results.size());
		assertEquals(new Reference(BookOfBible.Genesis, 14, 18), results.get(0));
		assertEquals(new Reference(BookOfBible.Psalms, 110, 4), results.get(1));

		results = concordance.getReferencesContaining("Christian");
		assertEquals(2, results.size());
	}

	@Test(timeout = 250)
	public void testGetReferencesContainingWithManyResults_SW() {
		// One that occurs 47 times, but change the case of the search string
		ReferenceList results = concordance.getReferencesContaining("righteousness");
		assertEquals(266, results.size());

		results = concordance.getReferencesContaining("righteous");
		assertEquals(265, results.size());

		results = concordance.getReferencesContaining("three");
		assertEquals(343, results.size());
	}

	@Test(timeout = 250)
	public void testExtremeSearches_SW() {
		// Empty string should return no results.
		ReferenceList results = concordance.getReferencesContaining("");
		assertEquals(0, results.size());

		results = concordance.getReferencesContaining("the");
		assertEquals(23755, results.size());
		results = concordance.getReferencesContaining("of");
		assertEquals(17161, results.size());
		results = concordance.getReferencesContaining("a");
		assertEquals(6728, results.size());
	}

	@Test(timeout = 500)
	public void testSingleWord() {
		assertEquals(84, concordance.getReferencesContaining("eaten").size());

		assertEquals(23755, concordance.getReferencesContaining("the").size());

		assertEquals(10, concordance.getReferencesContaining("Melchizedek").size());
	}

	@Test(timeout = 500)
	public void testWordsNotThere() {
		assertEquals(0, concordance.getReferencesContaining("monkey").size());

		assertEquals(0, concordance.getReferencesContaining("").size());

		assertEquals(0, concordance.getReferencesContaining(" ").size());

		// You can get some of these if you don't remember about words like
		// "John's" and/or you don't deal with them properly.
		assertEquals(0, concordance.getReferencesContaining("s").size());
	}

	@Test(timeout = 500)
	public void testWordWithApostropheS() {
		// should be 357, including several with "wife's".
		assertEquals(357, concordance.getReferencesContaining("wife").size());
	}
}