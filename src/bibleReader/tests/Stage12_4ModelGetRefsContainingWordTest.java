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
public class Stage12_4ModelGetRefsContainingWordTest {
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
	public void testGetReferenceContainingWithMultipleWords() {
		// These don't work because it is searching for a single word and these
		// won't match a single word.
		ReferenceList results = model.getReferencesContainingWord("son of god");
		assertEquals(0, results.size());
		
		results = model.getReferencesContainingWord("three wise men");
		assertEquals(0, results.size());
	}

	@Test(timeout = 250)
	public void testGetReferenceContainingWithNoResults() {
		ReferenceList results = model.getReferencesContainingWord("trinity");
		assertEquals(0, results.size());
		
		results = model.getReferencesContainingWord("neo");
		assertEquals(0, results.size());
		
		results = model.getReferencesContainingWord("cat");
		assertEquals(0, results.size());
	}

	@Test(timeout = 250)
	public void testGetReferenceContainingWithOneResult() {
		ReferenceList results = model.getReferencesContainingWord("Christians");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Acts, 11, 26), results.get(0));

		results = model.getReferencesContainingWord("reverend");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Psalms, 111, 9), results.get(0));

		results = model.getReferencesContainingWord("grandmother");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Timothy2, 1, 5), results.get(0));
	}

	@Test(timeout = 250)
	public void testGetReferenceContainingWithFewResults() {
		ReferenceList results = model.getReferencesContainingWord("Melchizedek");
		assertEquals(10, results.size());
		assertEquals(new Reference(BookOfBible.Genesis, 14, 18), results.get(0));
		assertEquals(new Reference(BookOfBible.Psalms, 110, 4), results.get(1));

		results = model.getReferencesContainingWord("Christian");
		assertEquals(2, results.size());

	}

	@Test(timeout = 250)
	public void testGetReferencesContainingWithManyResults() {
		ReferenceList results = model.getReferencesContainingWord("righteousness");
		assertEquals(317, results.size());

		results = model.getReferencesContainingWord("righteous");
		assertEquals(311, results.size());

		results = model.getReferencesContainingWord("three");
		assertEquals(448, results.size());
	}

	@Test(timeout = 250)
	public void testEmptyString() {
		// Empty string should return no results.
		ReferenceList results = model.getReferencesContainingWord("");
		assertEquals(0, results.size());
	}

	@Test(timeout = 250)
	public void testExtremeSearches() {
		ReferenceList results = model.getReferencesContainingWord("the");
		assertEquals(25245, results.size());
		
		results = model.getReferencesContainingWord("of");
		assertEquals(19762, results.size());
		
		results = model.getReferencesContainingWord("a");
		assertEquals(8194, results.size());
	}
}
