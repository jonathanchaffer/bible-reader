package bibleReader.tests;

// If you organize imports, the following import might be removed and you will
// not be able to find certain methods. If you can't find something, copy the
// commented import statement below, paste a copy, and remove the comments.
// Keep this commented one in case you organize imports multiple times.
//
// import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import bibleReader.BibleIO;
import bibleReader.model.ArrayListBible;
import bibleReader.model.Bible;
import bibleReader.model.BibleReaderModel;
import bibleReader.model.BookOfBible;
import bibleReader.model.MultiBibleModel;
import bibleReader.model.Reference;
import bibleReader.model.ReferenceList;
import bibleReader.model.Verse;
import bibleReader.model.VerseList;

/**
 * Tests for the Search capabilities of the BibleReaderModel class given
 * multiple versions of the Bible in the model. These tests assume BibleIO is
 * working an can read in all three files.
 * 
 * @author Chuck Cusack, March, 2013
 */
public class Stage09_1ModelBasicTest {
	private static VerseList[] verses;
	private static MultiBibleModel model;
	private static String[] versions = new String[] { "kjv.atv", "asv.xmv", "esv.atv" };
	private static String[] abbreviations = new String[] { "KJV", "ASV", "ESV" };
	private static String[] hab3_17_text = {
			"Although the fig tree shall not blossom, "
					+ "neither shall fruit be in the vines; the labour of the olive shall fail, "
					+ "and the fields shall yield no meat; the flock shall be cut off from the fold, "
					+ "and there shall be no herd in the stalls:",
			"For though the fig-tree shall not flourish, Neither shall fruit be in the vines; "
					+ "The labor of the olive shall fail, And the fields shall yield no food; "
					+ "The flock shall be cut off from the fold, And there shall be no herd in the stalls:",
			"Though the fig tree should not blossom, nor fruit be on the vines, "
					+ "the produce of the olive fail and the fields yield no food, "
					+ "the flock be cut off from the fold and there be no herd in the stalls," };

	// The methods tested in this class don't modify the Bible so we don't have to create a new
	// model before each test.  This will save time.
	@BeforeClass
	public static void readFileAndSetupModel() {
		verses = new VerseList[versions.length];
		for (int i = 0; i < versions.length; i++) {
			File file = new File(versions[i]);
			verses[i] = BibleIO.readBible(file);
		}
		model = new BibleReaderModel();
		for (int i = 0; i < versions.length; i++) {
			// Make a copy of the VerseList
			VerseList copyOfVerseList = new VerseList(verses[i].getVersion(), verses[i].getDescription(),
					new ArrayList<Verse>(verses[i]));
			Bible testBible = new ArrayListBible(copyOfVerseList);
			model.addBible(testBible);
		}
	}

//	@Before
//	public void setUp() throws Exception {
//		model = new BibleReaderModel();
//		for (int i = 0; i < versions.length; i++) {
//			// Make a copy of the VerseList
//			VerseList copyOfVerseList = new VerseList(verses[i].getVersion(), verses[i].getDescription(),
//					new ArrayList<Verse>(verses[i]));
//			Bible testBible = BibleFactory.createBible(copyOfVerseList);
//			model.addBible(testBible);
//		}
//	}

	@Test(timeout = 250)
	public void testAddAndGetBible() {
		model = new BibleReaderModel();
		assertEquals(0, model.getNumberOfVersions());
		for (int i = 0; i < versions.length; i++) {
			// Make a copy of the VerseList
			VerseList copyOfVerseList = new VerseList(verses[i].getVersion(), verses[i].getDescription(),
					new ArrayList<Verse>(verses[i]));
			Bible testBible = new ArrayListBible(copyOfVerseList);

			// Does it add a Bible?
			model.addBible(testBible);
			assertEquals(abbreviations[i] + " not properly added", i + 1, model.getNumberOfVersions());

			// Can I get it back?
			Bible fromModel = model.getBible(abbreviations[i]);
			assertNotNull(abbreviations[i] + " not being returned", fromModel);

			// Spot-check one verse to make sure it is the correct version.
			// This test assumes getVerseText is working, of course.
			Reference hab3_17 = new Reference(BookOfBible.Habakkuk, 3, 17);
			String hab3_17Actual = fromModel.getVerseText(hab3_17);
			assertEquals("Not getting the correct text for Hab 3:17 from the" + abbreviations[i] + " version.",
					hab3_17_text[i], hab3_17Actual);
		}
	}

	@Test(timeout = 50)
	public void testGetVersions() {
		String[] versions = model.getVersions();
		assertEquals(3, versions.length);
		ArrayList<String> vers = new ArrayList<String>(Arrays.asList(versions));
		assertEquals("ASV", vers.get(0));
		assertEquals("ESV", vers.get(1));
		assertEquals("KJV", vers.get(2));
	}

	@Test(timeout = 50)
	public void testGetNumberVersions() {
		assertEquals(3, model.getNumberOfVersions());
	}

	@Test(timeout = 50)
	public void testGetText() {
		// Non-existent bible.
		assertEquals("", model.getText("IDon'tExist", new Reference(BookOfBible.Job, 1, 1)));
		// Non-existent verse.
		assertEquals("", model.getText("ESV", new Reference(BookOfBible.Job, 101, 1)));

		// Valid verse
		assertEquals("The mighty God, even the LORD, hath spoken, and called the earth from the"
				+ " rising of the sun unto the going down thereof.",
				model.getText("KJV", new Reference(BookOfBible.Psalms, 50, 1)));
		assertEquals("And Job said:", model.getText("ESV", new Reference(BookOfBible.Job, 3, 2)));
	}

	@Test(timeout = 50)
	public void testGetVerses() {
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

		VerseList expectedResults = new VerseList("KJV", "Random Verses");
		expectedResults.add(new Verse(new Reference(BookOfBible.Ruth, 1, 1),
				"Now it came to pass in the days when the judges ruled, "
						+ "that there was a famine in the land. And a certain man of Bethlehemjudah went "
						+ "to sojourn in the country of Moab, he, and his wife, and his two sons."));
		expectedResults.add(new Verse(new Reference(BookOfBible.Genesis, 1, 1),
				"In the beginning God created the heaven and the earth."));
		expectedResults.add(new Verse(new Reference(BookOfBible.Revelation, 1, 1),
				"The Revelation of Jesus Christ, which God gave unto him, "
						+ "to shew unto his servants things which must shortly come to pass; "
						+ "and he sent and signified it by his angel unto his servant John:"));
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
				"And Mesha king of Moab was a sheepmaster, and rendered unto the "
						+ "king of Israel an hundred thousand lambs, and an hundred thousand rams, with the wool."));

		VerseList actualResults = model.getVerses("KJV", list);
		assertEquals(expectedResults, actualResults);
	}

	@Test(timeout = 50)
	public void testGetVersesWithInvalidReferences() {
		// Make a list with both valid and invalid references.
		ReferenceList list = new ReferenceList();

		list.add(new Reference(BookOfBible.Ruth, 1, 1));
		list.add(new Reference(BookOfBible.Galatians, 32, -3)); // invalid
		list.add(new Reference(BookOfBible.Job, 12, 143)); // invalid
		list.add(new Reference(BookOfBible.Genesis, 1, 1));
		list.add(new Reference(BookOfBible.Revelation, 1, 1));
		list.add(new Reference(BookOfBible.Revelation, 22, 22)); // invalid
		list.add(new Reference(BookOfBible.Genesis, 1, 0)); // invalid
		list.add(new Reference((BookOfBible) null, 10, 20)); // definitely
																// invalid.

		// Here are the expected results.
		VerseList expectedResults = new VerseList("KJV", "Random Verses");
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

		VerseList actualResults = model.getVerses("KJV", list);
		assertEquals(8, actualResults.size());
		assertEquals(expectedResults, actualResults);
	}
}
