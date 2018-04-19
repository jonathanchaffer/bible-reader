package bibleReader.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bibleReader.BibleIO;
import bibleReader.model.Bible;
import bibleReader.model.BookOfBible;
import bibleReader.model.Reference;
import bibleReader.model.ReferenceList;
import bibleReader.model.TreeMapBible;
import bibleReader.model.Verse;
import bibleReader.model.VerseList;

/**
 * Tests for the methods of Bible that are related to passage lookup methods.
 * 
 * @author Chuck Cusack, February 12, 2013.
 */
public class Stage11_2TreeMapBiblePassageTest {
	private Bible testBible;
	private static VerseList versesFromFile;

	@BeforeClass
	public static void readFile() {
		// Our tests will be based on the KJV version for now.
		File file = new File("kjv.atv");
		// We read the file here so it isn't done before every test.
		versesFromFile = BibleIO.readBible(file);
	}

	@Before
	public void setUp() throws Exception {
		// Make a shallow copy of the verses.
		ArrayList<Verse> copyOfList = new ArrayList<Verse>(versesFromFile);
		// Now make a copy of the VerseList
		VerseList copyOfVerseList = new VerseList(versesFromFile.getVersion(), versesFromFile.getDescription(),
				copyOfList);
		testBible = new TreeMapBible(copyOfVerseList);
	}

	@Test(timeout = 50)
	public void testGetVerse() {
		Verse result = testBible.getVerse(new Reference(BookOfBible.John, 3, 16));
		Verse actual = versesFromFile.get(26136);
		assertEquals(actual, result);

		result = testBible.getVerse(BookOfBible.Genesis, 1, 1);
		actual = versesFromFile.get(0);
		assertEquals(actual, result);

		result = testBible.getVerse(BookOfBible.Revelation, 22, 21);
		actual = versesFromFile.get(31101);
		assertEquals(actual, result);
	}

	@Test(timeout = 50)
	public void getVerses() {
		ReferenceList list = new ReferenceList();
		list.add(new Reference(BookOfBible.Ruth, 1, 1));
		list.add(new Reference(BookOfBible.Genesis, 1, 1));
		list.add(new Reference(BookOfBible.Revelation, 1, 1));
		list.add(new Reference(BookOfBible.Ruth, 1, 2));
		list.add(new Reference(BookOfBible.Ruth, 2, 1));
		list.add(new Reference(BookOfBible.Ruth, 2, 2));
		list.add(new Reference(BookOfBible.John, 1, 1));
		list.add(new Reference(BookOfBible.Ephesians, 3, 4));
		list.add(new Reference(BookOfBible.Ephesians, 3, 5));
		list.add(new Reference(BookOfBible.Ephesians, 3, 6));
		list.add(new Reference(BookOfBible.Kings2, 3, 4));

		VerseList expectedResults = new VerseList(testBible.getVersion(), "Random Verses");
		expectedResults.add(new Verse(new Reference(BookOfBible.Ruth, 1, 1),
				"Now it came to pass in the days when the judges ruled, that there was a famine in the land. "
						+ "And a certain man of Bethlehemjudah went to sojourn in the country of Moab, "
						+ "he, and his wife, and his two sons."));
		expectedResults.add(new Verse(new Reference(BookOfBible.Genesis, 1, 1),
				"In the beginning God created the heaven and the earth."));
		expectedResults.add(new Verse(new Reference(BookOfBible.Revelation, 1, 1),
				"The Revelation of Jesus Christ, which God gave unto him, to shew unto his servants "
						+ "things which must shortly come to pass; and he sent and signified "
						+ "it by his angel unto his servant John:"));
		expectedResults.add(new Verse(new Reference(BookOfBible.Ruth, 1, 2),
				"And the name of the man was Elimelech, and the name of his wife Naomi, "
						+ "and the name of his two sons Mahlon and Chilion, Ephrathites of Bethlehemjudah. "
						+ "And they came into the country of Moab, and continued there."));
		expectedResults.add(new Verse(new Reference(BookOfBible.Ruth, 2, 1),
				"And Naomi had a kinsman of her husband's, a mighty man of wealth, "
						+ "of the family of Elimelech; and his name was Boaz."));
		expectedResults.add(new Verse(new Reference(BookOfBible.Ruth, 2, 2),
				"And Ruth the Moabitess said unto Naomi, Let me now go to the field, "
						+ "and glean ears of corn after him in whose sight I shall find grace. "
						+ "And she said unto her, Go, my daughter."));
		expectedResults.add(new Verse(new Reference(BookOfBible.John, 1, 1),
				"In the beginning was the Word, and the Word was with God, and the Word was God."));
		expectedResults.add(new Verse(new Reference(BookOfBible.Ephesians, 3, 4),
				"Whereby, when ye read, ye may understand my knowledge in the mystery of Christ)"));
		expectedResults.add(new Verse(new Reference(BookOfBible.Ephesians, 3, 5),
				"Which in other ages was not made known unto the sons of men, "
						+ "as it is now revealed unto his holy apostles and prophets by the Spirit;"));
		expectedResults.add(new Verse(new Reference(BookOfBible.Ephesians, 3, 6),
				"That the Gentiles should be fellowheirs, and of the same body, "
						+ "and partakers of his promise in Christ by the gospel:"));
		expectedResults.add(new Verse(new Reference(BookOfBible.Kings2, 3, 4),
				"And Mesha king of Moab was a sheepmaster, and rendered unto the king "
						+ "of Israel an hundred thousand lambs, and an hundred thousand rams, with the wool."));

		VerseList actualResults = testBible.getVerses(list);
		assertEquals(expectedResults, actualResults);
	}

	@Test(timeout = 50)
	public void getVersesWithInvalidReferences() {
		// Make a list with both valid and invalid references.
		ReferenceList list = new ReferenceList();

		list.add(new Reference(BookOfBible.Ruth, 1, 1));
		list.add(new Reference(BookOfBible.Galatians, 32, -3)); // invalid
		list.add(new Reference(BookOfBible.Job, 12, 143)); // invalid
		list.add(new Reference(BookOfBible.Genesis, 1, 1));
		list.add(new Reference(BookOfBible.Revelation, 1, 1));
		list.add(new Reference(BookOfBible.Revelation, 22, 22)); // invalid
		list.add(new Reference(BookOfBible.Genesis, 1, 0)); // invalid
		list.add(new Reference((BookOfBible) null, 10, 20)); // invalid.
		list.add(new Reference(BookOfBible.Dummy, 10, 20)); // invalid.

		// Here are the expected results.
		VerseList expectedResults = new VerseList(testBible.getVersion(), "Random Verses");
		expectedResults.add(new Verse(new Reference(BookOfBible.Ruth, 1, 1),
				"Now it came to pass in the days when the judges ruled, that there was a famine in the land. "
						+ "And a certain man of Bethlehemjudah went to sojourn in the country of Moab, "
						+ "he, and his wife, and his two sons."));
		expectedResults.add(null);
		expectedResults.add(null);
		expectedResults.add(new Verse(new Reference(BookOfBible.Genesis, 1, 1),
				"In the beginning God created the heaven and the earth."));
		expectedResults.add(new Verse(new Reference(BookOfBible.Revelation, 1, 1),
				"The Revelation of Jesus Christ, which God gave unto him, "
						+ "to shew unto his servants things which must shortly come to pass; "
						+ "and he sent and signified it by his angel unto his servant John:"));
		expectedResults.add(null);
		expectedResults.add(null);
		expectedResults.add(null);
		expectedResults.add(null);

		VerseList actualResults = testBible.getVerses(list);
		assertEquals(9, actualResults.size());
		assertArrayEquals(expectedResults.toArray(), actualResults.toArray());
	}

	@Test(timeout = 50)
	public void testGetLastVerseNumber() {
		assertEquals(6, testBible.getLastVerseNumber(BookOfBible.Psalms, 23));
		assertEquals(21, testBible.getLastVerseNumber(BookOfBible.Revelation, 22));
		assertEquals(33, testBible.getLastVerseNumber(BookOfBible.Joshua, 24));

		// These have a number of verses that is not the same number of verses
		// as the last chapter of the given book. It sounds complicated, but one
		// easy way to incorrectly implement the method passes the previous
		// tests due to an oversight.
		assertEquals(45, testBible.getLastVerseNumber(BookOfBible.Psalms, 105));
		assertEquals(31, testBible.getLastVerseNumber(BookOfBible.Genesis, 1));
	}

	@Test(timeout = 50)
	public void testGetLastChapterNumber() {
		assertEquals(50, testBible.getLastChapterNumber(BookOfBible.Genesis));
		assertEquals(22, testBible.getLastChapterNumber(BookOfBible.Revelation));
		assertEquals(1, testBible.getLastChapterNumber(BookOfBible.Philemon));
		assertEquals(150, testBible.getLastChapterNumber(BookOfBible.Psalms));
		assertEquals(25, testBible.getLastVerseNumber(BookOfBible.Matthew, 1));
		assertEquals(23, testBible.getLastVerseNumber(BookOfBible.Matthew, 2));
		assertEquals(17, testBible.getLastVerseNumber(BookOfBible.Matthew, 3));
		assertEquals(50, testBible.getLastVerseNumber(BookOfBible.Mark, 9));
	}

	@Test(timeout = 500)
	public void testGetLastVerseAndChapterNumber2() {
		// Now the tricky part. In the ESV, Mark 9 is missing a few verses,
		// so if getLastVerseNumber is implemented incorrectly, they will
		// get 48 instead of 50.
		VerseList esvVerses = BibleIO.readBible(new File("esv.atv"));
		Bible esv = new TreeMapBible(esvVerses);
		assertEquals(50, esv.getLastVerseNumber(BookOfBible.Mark, 9));
	}

	// -----------------------------------------------------------------------------------
	// Tests for methods that return ReferenceLists.
	@Test(timeout = 50)
	public void testGetReferencesInclusive() {
		ReferenceList results = testBible.getReferencesInclusive(new Reference(BookOfBible.Kings2, 3, 4),
				new Reference(BookOfBible.Kings2, 11, 2));
		testPassageResults(new Reference(BookOfBible.Kings2, 3, 4), new Reference(BookOfBible.Kings2, 11, 2), results);

		results = testBible.getReferencesInclusive(new Reference(BookOfBible.Mark, 2, 2), new Reference(
				BookOfBible.Mark, 2, 12));
		testPassageResults(new Reference(BookOfBible.Mark, 2, 2), new Reference(BookOfBible.Mark, 2, 12), results);

		results = testBible.getReferencesInclusive(new Reference(BookOfBible.Revelation, 11, 2), new Reference(
				BookOfBible.Revelation, 22, 21));
		testPassageResults(new Reference(BookOfBible.Revelation, 11, 2), new Reference(BookOfBible.Revelation, 22, 21),
				results);
	}

	@Test(timeout = 50)
	public void testGetReferencesExclusive() {
		ReferenceList results = testBible.getReferencesExclusive(new Reference(BookOfBible.Kings2, 3, 4),
				new Reference(BookOfBible.Kings2, 11, 3));
		testPassageResults(new Reference(BookOfBible.Kings2, 3, 4), new Reference(BookOfBible.Kings2, 11, 2), results);

		results = testBible.getReferencesExclusive(new Reference(BookOfBible.Mark, 2, 2), new Reference(
				BookOfBible.Mark, 2, 12));
		testPassageResults(new Reference(BookOfBible.Mark, 2, 2), new Reference(BookOfBible.Mark, 2, 11), results);

		results = testBible.getReferencesExclusive(new Reference(BookOfBible.Revelation, 11, 2), new Reference(
				BookOfBible.Revelation, 22, 21));
		testPassageResults(new Reference(BookOfBible.Revelation, 11, 2), new Reference(BookOfBible.Revelation, 22, 20),
				results);
	}

	@Test(timeout = 50)
	public void testGetReferencesInclusiveMultiBooks() {
		ReferenceList results = testBible.getReferencesInclusive(new Reference(BookOfBible.John1, 1, 1), new Reference(
				BookOfBible.John3, 1, 14));
		testPassageResults(new Reference(BookOfBible.John1, 1, 1), new Reference(BookOfBible.John3, 1, 14), results);
	}

	@Test(timeout = 50)
	public void testGetReferencesExclusiveMultiBooks() {
		ReferenceList results = testBible.getReferencesExclusive(new Reference(BookOfBible.John1, 1, 1), new Reference(
				BookOfBible.John3, 1, 14));
		testPassageResults(new Reference(BookOfBible.John1, 1, 1), new Reference(BookOfBible.John3, 1, 13), results);
	}

	@Test(timeout = 50)
	public void testGetReferencesForBook() {
		ReferenceList results = testBible.getReferencesForBook(BookOfBible.getBookOfBible("1 Kings"));
		testPassageResults(new Reference(BookOfBible.Kings1, 1, 1), new Reference(BookOfBible.Kings1, 22, 53), results);
	}

	@Test(timeout = 50)
	public void testGetReferencesForChapter() {
		ReferenceList results = testBible.getReferencesForChapter(BookOfBible.getBookOfBible("Song of Solomon"), 3);
		testPassageResults(new Reference(BookOfBible.SongOfSolomon, 3, 1), new Reference(BookOfBible.SongOfSolomon, 3,
				11), results);
	}

	@Test(timeout = 50)
	public void testGetReferencesForChapters() {
		ReferenceList results = testBible.getReferencesForChapters(BookOfBible.Timothy1, 2, 4);
		testPassageResults(new Reference(BookOfBible.Timothy1, 2, 1), new Reference(BookOfBible.Timothy1, 4, 16),
				results);
	}

	@Test(timeout = 50)
	public void testGetReferencesForPassage() {
		ReferenceList results = testBible.getReferencesForPassage(BookOfBible.Ecclesiastes, 3, 1, 8);
		testPassageResults(new Reference(BookOfBible.Ecclesiastes, 3, 1),
				new Reference(BookOfBible.Ecclesiastes, 3, 8), results);

		results = testBible.getReferencesForPassage(BookOfBible.Joshua, 24, 28, 33);
		testPassageResults(new Reference(BookOfBible.Joshua, 24, 28), new Reference(BookOfBible.Joshua, 24, 33),
				results);
	}

	@Test(timeout = 50)
	public void testGetReferencesForPassageSecondVersion() {
		ReferenceList results = testBible.getReferencesForPassage(BookOfBible.Kings2, 3, 4, 11, 2);
		testPassageResults(new Reference(BookOfBible.Kings2, 3, 4), new Reference(BookOfBible.Kings2, 11, 2), results);

		results = testBible.getReferencesForPassage(BookOfBible.Ecclesiastes, 3, 1, 3, 8);
		testPassageResults(new Reference(BookOfBible.Ecclesiastes, 3, 1),
				new Reference(BookOfBible.Ecclesiastes, 3, 8), results);
	}

	// -----------------------------------------------------------------------------------
	// Tests for methods that return VerseLists.

	@Test(timeout = 50)
	public void testGetVersesInclusive() {
		VerseList results = testBible.getVersesInclusive(new Reference(BookOfBible.Kings2, 3, 4), new Reference(
				BookOfBible.Kings2, 11, 2));
		testPassageResults(new Reference(BookOfBible.Kings2, 3, 4), new Reference(BookOfBible.Kings2, 11, 2), results);

		results = testBible.getVersesInclusive(new Reference(BookOfBible.Mark, 2, 2), new Reference(BookOfBible.Mark,
				2, 12));
		testPassageResults(new Reference(BookOfBible.Mark, 2, 2), new Reference(BookOfBible.Mark, 2, 12), results);

		results = testBible.getVersesInclusive(new Reference(BookOfBible.Revelation, 11, 2), new Reference(
				BookOfBible.Revelation, 22, 21));
		testPassageResults(new Reference(BookOfBible.Revelation, 11, 2), new Reference(BookOfBible.Revelation, 22, 21),
				results);

		results = testBible.getVersesInclusive(new Reference(BookOfBible.Isaiah, 52, 13), new Reference(
				BookOfBible.Isaiah, 53, 12));
		testPassageResults(new Reference(BookOfBible.Isaiah, 52, 13), new Reference(BookOfBible.Isaiah, 53, 12),
				results);
	}

	@Test(timeout = 50)
	public void testGetVersesExclusive() {
		VerseList results = testBible.getVersesExclusive(new Reference(BookOfBible.Kings2, 3, 4), new Reference(
				BookOfBible.Kings2, 11, 3));
		testPassageResults(new Reference(BookOfBible.Kings2, 3, 4), new Reference(BookOfBible.Kings2, 11, 2), results);

		results = testBible.getVersesExclusive(new Reference(BookOfBible.Isaiah, 52, 13), new Reference(
				BookOfBible.Isaiah, 53, 12));
		testPassageResults(new Reference(BookOfBible.Isaiah, 52, 13), new Reference(BookOfBible.Isaiah, 53, 11),
				results);

		results = testBible.getVersesExclusive(new Reference(BookOfBible.Mark, 2, 2), new Reference(BookOfBible.Mark,
				2, 12));
		testPassageResults(new Reference(BookOfBible.Mark, 2, 2), new Reference(BookOfBible.Mark, 2, 11), results);

		results = testBible.getVersesExclusive(new Reference(BookOfBible.Revelation, 11, 2), new Reference(
				BookOfBible.Revelation, 22, 21));
		testPassageResults(new Reference(BookOfBible.Revelation, 11, 2), new Reference(BookOfBible.Revelation, 22, 20),
				results);
	}

	@Test(timeout = 50)
	public void testGetBook() {
		VerseList results = testBible.getBook(BookOfBible.getBookOfBible("1 Kings"));
		testPassageResults(new Reference(BookOfBible.Kings1, 1, 1), new Reference(BookOfBible.Kings1, 22, 53), results);
	}

	@Test(timeout = 50)
	public void testGetChapter() {
		VerseList results = testBible.getChapter(BookOfBible.Revelation, 22);
		testPassageResults(new Reference(BookOfBible.Revelation, 22, 1), new Reference(BookOfBible.Revelation, 22, 21),
				results);
		results = testBible.getChapter(BookOfBible.Genesis, 1);
		testPassageResults(new Reference(BookOfBible.Genesis, 1, 1), new Reference(BookOfBible.Genesis, 1, 31), results);
		results = testBible.getChapter(BookOfBible.SongOfSolomon, 3);
		testPassageResults(new Reference(BookOfBible.SongOfSolomon, 3, 1), new Reference(BookOfBible.SongOfSolomon, 3,
				11), results);
	}

	@Test(timeout = 50)
	public void testGetChapters() {
		VerseList results = testBible.getChapters(BookOfBible.Revelation, 21, 22);
		testPassageResults(new Reference(BookOfBible.Revelation, 21, 1), new Reference(BookOfBible.Revelation, 22, 21),
				results);
		results = testBible.getChapters(BookOfBible.Genesis, 1, 50);
		testPassageResults(new Reference(BookOfBible.Genesis, 1, 1), new Reference(BookOfBible.Genesis, 50, 26),
				results);
		results = testBible.getChapters(BookOfBible.Timothy1, 4, 6);
		testPassageResults(new Reference(BookOfBible.Timothy1, 4, 1), new Reference(BookOfBible.Timothy1, 6, 21),
				results);
		results = testBible.getChapters(BookOfBible.Timothy1, 2, 4);
		testPassageResults(new Reference(BookOfBible.Timothy1, 2, 1), new Reference(BookOfBible.Timothy1, 4, 16),
				results);
	}

	@Test(timeout = 50)
	public void testGetPassage() {
		VerseList results = testBible.getPassage(BookOfBible.Ecclesiastes, 3, 1, 8);
		testPassageResults(new Reference(BookOfBible.Ecclesiastes, 3, 1),
				new Reference(BookOfBible.Ecclesiastes, 3, 8), results);

		results = testBible.getPassage(BookOfBible.Joshua, 24, 28, 33);
		testPassageResults(new Reference(BookOfBible.Joshua, 24, 28), new Reference(BookOfBible.Joshua, 24, 33),
				results);
	}

	@Test(timeout = 50)
	public void testGetPassageSecondVersion() {
		VerseList results = testBible.getPassage(BookOfBible.Kings2, 3, 4, 11, 2);
		testPassageResults(new Reference(BookOfBible.Kings2, 3, 4), new Reference(BookOfBible.Kings2, 11, 2), results);

		results = testBible.getPassage(BookOfBible.Ecclesiastes, 3, 1, 3, 8);
		testPassageResults(new Reference(BookOfBible.Ecclesiastes, 3, 1),
				new Reference(BookOfBible.Ecclesiastes, 3, 8), results);
	}

	@Test(timeout = 50)
	public void testInvalidChapter() {
		// Invalid chapter
		VerseList results = testBible.getChapter(BookOfBible.Jude, 2);
		assertEquals(0, results.size());
	}

	@Test(timeout = 50)
	public void testInvalidVerse() {
		// Invalid verse. This is a tricky case.
		Verse result = testBible.getVerse(new Reference(BookOfBible.John, 3, 163));
		assertEquals(null, result);
	}

	@Test(timeout = 50)
	public void testNullBook() {
		// What happens if the book is null?
		VerseList results = testBible.getBook(null);
		assertEquals(0, results.size());
	}

	@Test(timeout = 50)
	public void testInvalidPassage() {
		// Invalid verse (Malachi ends at 4:6)
		VerseList results = testBible.getPassage(BookOfBible.Malachi, 4, 2, 7);
		// An ArrayList will return no verses, but a TreeMap might return 5.
		if (results.size() != 0 && results.size() != 5) {
			fail("Something went wrong. This should either return an empty list or Mal 4:2-6 (5 verses).");
		}
	}

	@Test(timeout = 50)
	public void testInvalidChapters() {
		// Chapter/verse out of order
		VerseList results = testBible.getChapters(BookOfBible.Timothy1, 3, 2);
		assertEquals(0, results.size());
	}

	@Test(timeout = 50)
	public void testInvalidPassage_B_CH_V_V() {
		VerseList results = testBible.getPassage(BookOfBible.Peter2, 3, 7, 3);
		assertEquals(0, results.size());
	}

	@Test(timeout = 50)
	public void testInvalidVersesInclusive() {
		VerseList results = testBible.getVersesInclusive(new Reference(BookOfBible.Kings2, 13, 4), new Reference(
				BookOfBible.Kings2, 11, 2));
		assertEquals(0, results.size());
		results = testBible.getVersesInclusive(new Reference(BookOfBible.Isaiah, 53, 12), new Reference(
				BookOfBible.Isaiah, 52, 13));
		assertEquals(0, results.size());
	}

	@Test(timeout = 50)
	public void testInvalidVersesExclusive() {
		VerseList results = testBible.getVersesExclusive(new Reference(BookOfBible.Kings2, 13, 4), new Reference(
				BookOfBible.Kings2, 11, 3));
		assertEquals(0, results.size());
	}

	@Test(timeout = 50)
	public void testInvalidReferencesInclusive() {
		// Out of order chapter/verse for the methods that return ReferenceLists
		ReferenceList refResults = testBible.getReferencesInclusive(new Reference(BookOfBible.Kings2, 13, 4),
				new Reference(BookOfBible.Kings2, 11, 2));
		assertEquals(0, refResults.size());
	}

	@Test(timeout = 50)
	public void testInvalidReferencesExclusive() {
		ReferenceList refResults = testBible.getReferencesExclusive(new Reference(BookOfBible.Kings2, 13, 4),
				new Reference(BookOfBible.Kings2, 11, 3));
		assertEquals(0, refResults.size());
	}

	@Test(timeout = 50)
	public void testGetChapter_InvalidChapter() {
		// Invalid chapter
		ReferenceList results = testBible.getReferencesForChapter(BookOfBible.getBookOfBible("Jude"), 2);
		assertEquals(0, results.size());
	}

	@Test(timeout = 50)
	public void testgetRefsForBook_NullBook() {
		// What happens if the book is null?
		ReferenceList results = testBible.getReferencesForBook(BookOfBible.getBookOfBible("Herman"));
		assertEquals(0, results.size());

	}	

	@Test(timeout = 50)
	public void testGetRefsForChapters_InvalidOrder() {
		ReferenceList results = testBible.getReferencesForChapters(BookOfBible.Timothy1, 3, 2);
		assertEquals(0, results.size());
	}	

	@Test(timeout = 50)
	public void testGetRefsForPassage_InvalidOrder() {
		ReferenceList results = testBible.getReferencesForPassage(BookOfBible.Peter2, 3, 7, 3);
		assertEquals(0, results.size());
	}	

	@Test(timeout = 50)
	public void testGetRefsInclusive_InvalidOrder() {
		ReferenceList results = testBible.getReferencesInclusive(new Reference(BookOfBible.Kings2, 13, 4), new Reference(
				BookOfBible.Kings2, 11, 2));
		assertEquals(0, results.size());
	}	

	@Test(timeout = 50)
	public void testGetRefsExclusive_InvalidOrder() {
		ReferenceList results = testBible.getReferencesExclusive(new Reference(BookOfBible.Kings2, 13, 4), new Reference(
				BookOfBible.Kings2, 11, 3));
		assertEquals(0, results.size());
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
	public void testPassageResults(Reference firstVerse, Reference lastVerse, VerseList actualResults) {
		int i = 0;
		while (!versesFromFile.get(i).getReference().equals(firstVerse)) {
			i++;
		}
		int firstIndex = i;
		while (!versesFromFile.get(i).getReference().equals(lastVerse)) {
			i++;
		}
		int lastIndex = i + 1; // It does not include the last index, so add
								// one.
		List<Verse> passage = versesFromFile.subList(firstIndex, lastIndex);
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
	public void testPassageResults(Reference firstVerse, Reference lastVerse, ReferenceList actualResults) {
		int i = 0;
		while (!versesFromFile.get(i).getReference().equals(firstVerse)) {
			i++;
		}
		int firstIndex = i;
		while (!versesFromFile.get(i).getReference().equals(lastVerse)) {
			i++;
		}
		ReferenceList expected = new ReferenceList();
		int lastIndex = i + 1; // It does not include the last index, so add
								// one.
		for (int j = firstIndex; j < lastIndex; j++) {
			expected.add(versesFromFile.get(j).getReference());
		}
		assertArrayEquals(expected.toArray(), actualResults.toArray());
	}
}
