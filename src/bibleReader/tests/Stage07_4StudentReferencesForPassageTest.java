package bibleReader.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bibleReader.BibleIO;
import bibleReader.model.ArrayListBible;
import bibleReader.model.Bible;
import bibleReader.model.BibleReaderModel;
import bibleReader.model.ReferenceList;
import bibleReader.model.TreeMapBible;
import bibleReader.model.Verse;
import bibleReader.model.VerseList;

/**
 * Test Cases for stage 7 - passage lookup (Based on solution to Stage 6)
 * 
 * @author Joshua Swett
 * @author Brant Bechtel
 * @author Nicolas Hoover
 * 
 */

public class Stage07_4StudentReferencesForPassageTest {
	private static VerseList versesFromFile;
	private BibleReaderModel model;

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

		// Now make a new Bible.
		Bible kjvBible = new ArrayListBible(copyOfVerseList);

		model = new BibleReaderModel();
		model.addBible(kjvBible);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(timeout = 50)
	/**
	 * Part A.  Invalid references should return 0 results.
	 */
	public void testInvalidRefence() {
		VerseList results = kjvSearch("Jude 2");
		assertEquals(0, results.size());
		results = kjvSearch("Herman 2");
		assertEquals(0, results.size());
		results = kjvSearch("John 3:163");
		assertEquals(0, results.size());
		// This one is too tricky. Some implementation of Bible
		// will return none, some will return all of the valid ones
		// (in this case Mal 3:6-4:6).
		// results = kjvSearch("Mal 3:6-24:7");
		// assertEquals(0, results.size());
	}

	@Test(timeout = 50)
	/**
	 * Part B.  Invalid references should return 0 results
	 */
	public void testFindPassage_InvalidSyntax() {
		VerseList vers = kjvSearch("1Tim 3-2");
		assertEquals(0, vers.size());

		vers = kjvSearch("Deut :2-3");
		assertEquals(0, vers.size());

		vers = kjvSearch("Josh 6:4- :6");
		assertEquals(0, vers.size());

		vers = kjvSearch("Ruth : - :");
		assertEquals(0, vers.size());

		vers = kjvSearch("2 Sam : 4-7 :");
		assertEquals(0, vers.size());

		vers = kjvSearch("Ephesians 5:2,4");
		assertEquals(0, vers.size());

		vers = kjvSearch("John 3;16");
		assertEquals(0, vers.size());

	}

	@Test(timeout = 50)
	/**
	 * Part C. Should return a single that is equivalent to the expectation.
	 * Arrays were still used to allow for the possibilty of the passage search returning
	 * more than one verse.
	 */
	public void testSingleVerse() {
		VerseList results = kjvSearch("John 3 : 16");
		Verse resultsArray[] = results.toArray(new Verse[0]);
		Verse expected[] = { versesFromFile.get(26136) };
		assertArrayEquals(expected, resultsArray);

		results = kjvSearch("Gen 1:1");
		resultsArray = results.toArray(new Verse[0]);
		expected[0] = versesFromFile.get(0);
		assertArrayEquals(expected, resultsArray);

		results = kjvSearch("Revelation 22:21");
		resultsArray = results.toArray(new Verse[0]);
		expected[0] = versesFromFile.get(31101);
		assertArrayEquals(expected, resultsArray);
	}

	@Test(timeout = 50)
	/**
	 * Part D. Should return multiple verses from a single chapter.
	 */
	public void testFindPassage_VersesFromASingleChapter() {

		VerseList vers = kjvSearch("Ecclesiastes 3 : 1 - 8");
		assertEquals(8, vers.size());
		Verse[] arrayVers = vers.toArray(new Verse[0]);

		List<Verse> sub = versesFromFile.subList(17360, 17368);
		Verse[] testVersesFrom = sub.toArray(new Verse[0]);

		assertArrayEquals(testVersesFrom, arrayVers);

		vers = kjvSearch("Joshua 24:28-33");
		assertEquals(6, vers.size());
		arrayVers = vers.toArray(new Verse[0]);

		sub = versesFromFile.subList(6504, 6510);
		testVersesFrom = sub.toArray(new Verse[0]);

		assertArrayEquals(testVersesFrom, arrayVers);

		vers = kjvSearch("Psalm 23:1-6");
		assertEquals(6, vers.size());
		arrayVers = vers.toArray(new Verse[0]);

		sub = versesFromFile.subList(14236, 14242);
		testVersesFrom = sub.toArray(new Verse[0]);

		assertArrayEquals(testVersesFrom, arrayVers);
	}

	@Test(timeout = 100)
	/**
	 * Part E. Should return entire chapters
	 */
	public void testEntireChapters() {
		VerseList results = kjvSearch("Song of Solomon 3");
		Object resultsArray[] = results.toArray();
		List<Verse> expected = versesFromFile.subList(17572, 17583);
		Object expectedArray[] = expected.toArray();
		assertArrayEquals(expectedArray, resultsArray);

		results = kjvSearch("1 Tim 2-4");
		Object resultsArray2[] = results.toArray();
		expected = versesFromFile.subList(29717, 29764);
		Object expectedArray2[] = expected.toArray();
		assertArrayEquals(expectedArray2, resultsArray2);

		results = kjvSearch("1 John 2-3");
		Object resultsArray3[] = results.toArray();
		expected = versesFromFile.subList(30551, 30604);
		Object expectedArray3[] = expected.toArray();
		assertArrayEquals(expectedArray3, resultsArray3);
	}

	@Test(timeout = 50)
	/**
	 * Part F. Should return verses from multiple chapters.
	 */
	public void testFindReferenceDifferentChapters() {
		Object[] isaiahResults = kjvSearch("Isa 52:13 - 53:12").toArray();
		// get actual verses from Isaiah 52:13 - 53:12
		List<Verse> actualVerses = versesFromFile.subList(18709, 18724);
		assertArrayEquals(actualVerses.toArray(), isaiahResults);

		Object[] malachiResults = kjvSearch("Mal 3:6-4:6").toArray();
		// get actual verses from Malachi 3:6-4:6
		actualVerses = versesFromFile.subList(23126, 23145);
		assertArrayEquals(actualVerses.toArray(), malachiResults);
	}

	@Test(timeout = 100)
	/**
	 * Part G. Should return a whole book!
	 */
	public void testFindReferenceWholeBook() {
		Object[] kings1Results = kjvSearch("1 Kings").toArray();
		// get actual verses from 1 Kings
		List<Verse> actualVerses = versesFromFile.subList(8718, 9534);
		assertArrayEquals(actualVerses.toArray(), kings1Results);

		Object[] philemonResults = kjvSearch("Philemon").toArray();
		// get actual verses from Philemon
		actualVerses = versesFromFile.subList(29939, 29964);
		assertArrayEquals(actualVerses.toArray(), philemonResults);

	}

	@Test(timeout = 50)
	/**
	 * Part H. Return verses spanning multiple chapters using odd syntax.
	 */
	public void testFindReferenceStrangeSyntax() {
		Object[] ephesiansResults = kjvSearch("Ephesians 5-6:9").toArray();
		// get actual verses from Ephesians
		List<Verse> actualVerses = versesFromFile.subList(29305, 29347);
		assertArrayEquals(actualVerses.toArray(), ephesiansResults);

		Object[] hebrewsResults = kjvSearch("Hebrews 11-12:2").toArray();
		// get actual verses from Hebrews
		actualVerses = versesFromFile.subList(30173, 30215);
		assertArrayEquals(actualVerses.toArray(), hebrewsResults);
	}

	/**
	 * A helper method. Searching via the model requires performing the search
	 * followed by asking for the results for a given version. To make the test
	 * cases a little simpler to write, I call this method which does both
	 * things for me.
	 * 
	 * @param words
	 * @return
	 */
	public VerseList kjvSearch(String reference) {
		ReferenceList rl = model.getReferencesForPassage(reference);
		VerseList results = model.getVerses("KJV", rl);
		return results;
	}

}
