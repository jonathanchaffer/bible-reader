package bibleReader.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bibleReader.BibleIO;
import bibleReader.model.ArrayListBible;
import bibleReader.model.Bible;
import bibleReader.model.BookOfBible;
import bibleReader.model.Reference;
import bibleReader.model.ReferenceList;
import bibleReader.model.Verse;
import bibleReader.model.VerseList;

/**
 * Tests for the methods of Bible that are related to passage lookup methods.
 * 
 * @author Chuck Cusack, February 12, 2013.
 */
public class Stage07_1BibleBasicAndNumberTest {
	private Bible KJVBible, ESVBible;
	private static VerseList versesFromKJVFile, versesFromESVFile;

	public Bible createBible(VerseList verses) {
		return new ArrayListBible(verses);
	}

	@BeforeClass
	public static void readFile() {
		// Some tests will be based on the KJV version.
		File file = new File("kjv.atv");
		// We read the file here so it isn't done before every test.
		versesFromKJVFile = BibleIO.readBible(file);

		// Other tests will be based on the ESV version.
		file = new File("esv.atv");
		// We read the file here so it isn't done before every test.
		versesFromESVFile = BibleIO.readBible(file);
	}

	@Before
	public void setUp() throws Exception {
		// Make a shallow copy of the verses.
		ArrayList<Verse> copyOfList = new ArrayList<Verse>(versesFromKJVFile);
		// Now make a copy of the VerseList
		VerseList copyOfVerseList = new VerseList(
				versesFromKJVFile.getVersion(),
				versesFromKJVFile.getDescription(), copyOfList);

		// Now make a new Bible. This should ensure that whichever version you
		// implement, the tests should work.
		KJVBible = createBible(copyOfVerseList);

		copyOfList = new ArrayList<Verse>(versesFromESVFile);
		// Now make a copy of the VerseList
		copyOfVerseList = new VerseList(versesFromESVFile.getVersion(),
				versesFromESVFile.getDescription(), copyOfList);
		ESVBible = createBible(copyOfVerseList);
	}

	// ------------------------------------------------------------------------------

	@Test(timeout = 5000)
	public void testConstructor() {
		ArrayList<Verse> copyOfList = new ArrayList<Verse>(versesFromKJVFile);
		// Now make a copy of the VerseList
		VerseList copyOfVerseList = new VerseList(
				versesFromKJVFile.getVersion(),
				versesFromKJVFile.getDescription(), copyOfList);

		// Now make a new Bible. This should ensure that whichever version you
		// implement, the tests should work.
		KJVBible = createBible(copyOfVerseList);
		
		// Do we get the correct number of verses?
		assertEquals(31102, KJVBible.getNumberOfVerses());
		
		// Did the verseList get copied?
		copyOfVerseList.clear();
		assertEquals(31102, KJVBible.getNumberOfVerses());
	}
	
	@Test(timeout = 5000)
	public void testGetAllVerses() {
		VerseList copy = KJVBible.getAllVerses();
		/// Are they the correct verses?
		assertArrayEquals(versesFromKJVFile.toArray(),copy.toArray());
		
		// Does it return a copy of the verses?
		copy.clear();
		assertEquals(31102, KJVBible.getNumberOfVerses());
	}
	
	
	@Test(timeout = 5000)
	public void testGetNumberOfVerses() {
		assertEquals(31102, KJVBible.getNumberOfVerses());
		assertEquals(31086, ESVBible.getNumberOfVerses());
	}

	@Test(timeout = 5000)
	public void testGetVersion() {
		assertEquals("KJV", KJVBible.getVersion());
		assertEquals("ESV", ESVBible.getVersion());
	}

	@Test(timeout = 5000)
	public void testGetTitle() {
		assertEquals("Holy Bible, Authorized (King James) Version", KJVBible.getTitle());
		assertEquals("The Holy Bible, English Standard Version, copyright © 2001 by Crossway Bibles, a publishing ministry of Good News Publishers.", ESVBible.getTitle());
	}

	@Test(timeout = 5000)
	public void testIsValid() {
		assertTrue(KJVBible.isValid(new Reference(BookOfBible.John1, 1, 8)));
		assertTrue(KJVBible.isValid(new Reference(BookOfBible.Jude, 1, 25)));
		assertTrue(KJVBible.isValid(new Reference(BookOfBible.John1, 5, 15)));

		// Not in the KJV Bible.
		assertFalse(KJVBible.isValid(new Reference(BookOfBible.Jude, 5, 15)));
		assertFalse(KJVBible.isValid(new Reference(BookOfBible.Jude, 1, 26)));
		
		// Should be in KJV, but not ESV
		assertTrue(KJVBible.isValid(new Reference(BookOfBible.Mark, 7, 16)));
		assertFalse(ESVBible.isValid(new Reference(BookOfBible.Mark, 7, 16)));
		
		assertTrue(KJVBible.isValid(new Reference(BookOfBible.Romans, 16, 24)));
		assertFalse(ESVBible.isValid(new Reference(BookOfBible.Romans, 16, 24)));
		
		// Should not be in any Bible.
		assertFalse(KJVBible.isValid(new Reference(BookOfBible.Dummy, 1, 1)));
		assertFalse(ESVBible.isValid(new Reference(BookOfBible.Dummy, 1, 1)));
	}
	
//----------------------------------------------------
	@Test(timeout = 50)
	public void testGetLastVerseNumber() {
		assertEquals(6, KJVBible.getLastVerseNumber(BookOfBible.Psalms, 23));
		assertEquals(21,
				KJVBible.getLastVerseNumber(BookOfBible.Revelation, 22));
		assertEquals(33, KJVBible.getLastVerseNumber(BookOfBible.Joshua, 24));

		// These have a number of verses that is not the same number of verses
		// as the last chapter of the given book. It sounds complicated, but one
		// easy way to incorrectly implement the method passes the previous
		// tests due to an oversight.
		assertEquals(45, KJVBible.getLastVerseNumber(BookOfBible.Psalms, 105));
		assertEquals(31, KJVBible.getLastVerseNumber(BookOfBible.Genesis, 1));
	}

	@Test(timeout = 50)
	public void testGetLastVerseNumber_Tricky() {
		// These chapters have missing verses so counting verses to find the
		// last verse number won't work.
		assertEquals(37, ESVBible.getLastVerseNumber(BookOfBible.Mark, 7));
		assertEquals(27, ESVBible.getLastVerseNumber(BookOfBible.Romans, 16));

		// This chapter is missing two verses.
		assertEquals(50, ESVBible.getLastVerseNumber(BookOfBible.Mark, 9));
	}

	@Test(timeout = 50)
	public void testGetLastChapterNumber() {
		assertEquals(50, KJVBible.getLastChapterNumber(BookOfBible.Genesis));
		assertEquals(22, KJVBible.getLastChapterNumber(BookOfBible.Revelation));
		assertEquals(150, KJVBible.getLastChapterNumber(BookOfBible.Psalms));
	}

	@Test(timeout = 50)
	public void testGetLastChapterNumber_OneChapterBooks() {
		assertEquals(1, KJVBible.getLastChapterNumber(BookOfBible.Philemon));
		assertEquals(1, KJVBible.getLastChapterNumber(BookOfBible.Jude));
	}

	// ------------------------------------------------------------------------------------------------
	// Helper methods.
	/**
	 * This is not an efficient method at all, but it is more efficient than
	 * doing it by hand. It does a linear search to find the starting and ending
	 * verses. It also assumes that firstVerse comes before lastVerse and that
	 * both are in the ArrayList. In other words, this method is only useful for
	 * passage lookups that are expected to succeed.
	 * 
	 * @param firstVerse
	 *            The first verse in the passage (inclusive)
	 * @param secondVerse
	 *            The last verse in the passage (inclusive)
	 * @param actualResults
	 *            The list of verses from the passage that is hopefully all of
	 *            those between firstVerse and lastVerse, inclusive.
	 */
	public void compareVerseListWithExpected(Reference firstVerse,
			Reference lastVerse, VerseList actualResults) {
		int i = 0;
		while (!versesFromKJVFile.get(i).getReference().equals(firstVerse)) {
			i++;
		}
		int firstIndex = i;
		while (!versesFromKJVFile.get(i).getReference().equals(lastVerse)) {
			i++;
		}
		int lastIndex = i + 1; // It does not include the last index, so add
								// one.
		List<Verse> passage = versesFromKJVFile.subList(firstIndex, lastIndex);
		assertArrayEquals(passage.toArray(), actualResults.toArray());
	}

	/**
	 * The same as the previous method except that this one checks
	 * ReferenceLists.
	 * 
	 * @param firstVerse
	 * @param lastVerse
	 * @param actualResults
	 */
	public void compareReferenceListWithExpected(Reference firstVerse,
			Reference lastVerse, ReferenceList actualResults) {
		int i = 0;
		while (!versesFromKJVFile.get(i).getReference().equals(firstVerse)) {
			i++;
		}
		int firstIndex = i;
		while (!versesFromKJVFile.get(i).getReference().equals(lastVerse)) {
			i++;
		}
		ReferenceList expected = new ReferenceList();
		int lastIndex = i + 1; // It does not include the last index, so add
								// one.
		for (int j = firstIndex; j < lastIndex; j++) {
			expected.add(versesFromKJVFile.get(j).getReference());
		}
		assertArrayEquals(expected.toArray(), actualResults.toArray());
	}

}
