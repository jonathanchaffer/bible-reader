package bibleReader.tests;

// If you organize imports, the following import might be removed and you will
// not be able to find certain methods. If you can't find something, copy the
// commented import statement below, paste a copy, and remove the comments.
// Keep this commented one in case you organize imports multiple times.
//
// import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import bibleReader.model.Reference;
import bibleReader.model.ReferenceList;
import bibleReader.model.Verse;
import bibleReader.model.VerseList;

/**
 * Tests for the Search capabilities of the BibleReaderModel class. These tests assume BibleIO is working an can read in
 * the kjv.atv file.
 * 
 * @author Chuck Cusack, January, 2013
 */
public class Stage05BibleReaderModelTest  {
	private static VerseList	versesFromFile;
	private BibleReaderModel	model;

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

		Bible testBible = BibleFactory.createBible(copyOfVerseList);
		model = new BibleReaderModel();
		model.addBible(testBible);
	}

	@Test (timeout=5000)
	public void testGetVersions() {
		String[] versions = model.getVersions();
		assertEquals(1, versions.length);
		assertEquals("KJV", versions[0]);
	}

	@Test (timeout=5000)
	public void testGetNumberVersions() {
		assertEquals(1, model.getNumberOfVersions());
	}

	@Test (timeout=5000)
	public void testSearchNoResults() {
		ReferenceList results = model.getReferencesContaining("three wise men");
		assertEquals(0, results.size());
		results = model.getReferencesContaining("trinity");
		assertEquals(0, results.size());
		results = model.getReferencesContaining("neo");
		assertEquals(0, results.size());
	}

	@Test (timeout=5000)
	public void testSearchOneResult() {
		ReferenceList results = model.getReferencesContaining("the fig tree shall not blossom");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Habakkuk, 3, 17), results.get(0));
	}

	@Test (timeout=5000)
	public void testGetReferenceContainingWithFewResults() {
		ReferenceList results = model.getReferencesContaining("Melchizedek");
		assertEquals(2, results.size());
		assertEquals(new Reference(BookOfBible.Genesis, 14, 18), results.get(0));
		assertEquals(new Reference(BookOfBible.Psalms, 110, 4), results.get(1));

		results = model.getReferencesContaining("god SO loved");
		assertEquals(2, results.size());
		Reference jhn3_16 = new Reference(BookOfBible.John, 3, 16);
		Reference firstJohn4_11 = new Reference(BookOfBible.John1, 4, 11);
		assertTrue(results.contains(jhn3_16));
		assertTrue(results.contains(firstJohn4_11));

		// Second test
		results = model.getReferencesContaining("Christians");
		assertEquals(1, results.size());
		Reference act11_26 = new Reference(BookOfBible.Acts, 11, 26);
		assertEquals(act11_26, results.get(0));
	}

	@Test (timeout=5000)
	public void testGetVerseContainingWithManyResults() {
		// One that occurs 47 times, but change the case of the search string
		ReferenceList verseResults = model.getReferencesContaining("son oF GoD");
		assertEquals(47, verseResults.size());

		verseResults = model.getReferencesContaining("righteousness");
		// Biblegateway.com gets 291 results. If you don't pass this test, talk to me ASAP!
		assertEquals(307, verseResults.size());

		// Should get 511 verses for the word "three".
		// We'll test 3 known results--the first, last, and one in the middle.
		verseResults = model.getReferencesContaining("three");
		assertEquals(511, verseResults.size());
		assertEquals(new Reference(BookOfBible.Genesis, 5, 22), verseResults.get(0));
		assertEquals(new Reference(BookOfBible.Joshua, 13, 30), verseResults.get(126));
		assertEquals(new Reference(BookOfBible.Revelation, 21, 13), verseResults.get(510));
	}

	@Test (timeout=5000)
	public void testGetVerseContainingWithPartialWords() {
		// This should match eaten as well as beaten, so it should return 143 results.
		ReferenceList verseResults = model.getReferencesContaining("eaten");
		assertEquals(143, verseResults.size());
	}

	@Test (timeout=5000)
	public void testExtremeSearches() {
		// Empty string should return no results.
		ReferenceList verseResults = model.getReferencesContaining("");
		assertEquals(0, verseResults.size());

		// Something that occurs a lot, like "the".
		// Of course, this isn't the number of verses containing the word the,
		// but the string "the", so it also matches verses with "then", etc.
		// Occurs in 28000 verses. How weird is that. Exactly 28,000?
		// Do it with both search methods.
		verseResults = model.getReferencesContaining("the");
		assertEquals(28000, verseResults.size());

		// Space occurs in every verse. That is annoying.
		// Searches for ".", ",". etc. will be similar.
		// For now we won't worry about filtering these.
		// Our next version will take care of it.
		verseResults = model.getReferencesContaining(" ");
		assertEquals(31102, verseResults.size());
	}


	@Test(timeout = 5000)
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
		expectedResults
				.add(new Verse(
						new Reference(BookOfBible.Ruth, 1, 1),
						"Now it came to pass in the days when the judges ruled, that there was a famine in the land. And a certain man of Bethlehemjudah went to sojourn in the country of Moab, he, and his wife, and his two sons."));
		expectedResults.add(new Verse(new Reference(BookOfBible.Genesis, 1, 1),
				"In the beginning God created the heaven and the earth."));
		expectedResults
				.add(new Verse(
						new Reference(BookOfBible.Revelation, 1, 1),
						"The Revelation of Jesus Christ, which God gave unto him, to shew unto his servants things which must shortly come to pass; and he sent and signified it by his angel unto his servant John:"));
		expectedResults
				.add(new Verse(
						new Reference(BookOfBible.Ruth, 1, 2),
						"And the name of the man was Elimelech, and the name of his wife Naomi, and the name of his two sons Mahlon and Chilion, Ephrathites of Bethlehemjudah. And they came into the country of Moab, and continued there."));
		expectedResults
				.add(new Verse(new Reference(BookOfBible.Ruth, 2, 1),
						"And Naomi had a kinsman of her husband's, a mighty man of wealth, of the family of Elimelech; and his name was Boaz."));
		expectedResults
				.add(new Verse(
						new Reference(BookOfBible.Ruth, 2, 2),
						"And Ruth the Moabitess said unto Naomi, Let me now go to the field, and glean ears of corn after him in whose sight I shall find grace. And she said unto her, Go, my daughter."));
		expectedResults.add(new Verse(new Reference(BookOfBible.John, 1, 1),
				"In the beginning was the Word, and the Word was with God, and the Word was God."));
		expectedResults.add(new Verse(new Reference(BookOfBible.Ephesians, 3, 4),
				"Whereby, when ye read, ye may understand my knowledge in the mystery of Christ)"));
		expectedResults
				.add(new Verse(
						new Reference(BookOfBible.Ephesians, 3, 5),
						"Which in other ages was not made known unto the sons of men, as it is now revealed unto his holy apostles and prophets by the Spirit;"));
		expectedResults
				.add(new Verse(new Reference(BookOfBible.Ephesians, 3, 6),
						"That the Gentiles should be fellowheirs, and of the same body, and partakers of his promise in Christ by the gospel:"));
		expectedResults
				.add(new Verse(
						new Reference(BookOfBible.Kings2, 3, 4),
						"And Mesha king of Moab was a sheepmaster, and rendered unto the king of Israel an hundred thousand lambs, and an hundred thousand rams, with the wool."));

		VerseList actualResults = model.getVerses("KJV",list);
		assertEquals(expectedResults, actualResults);
	}

	@Test(timeout = 5000)
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
		list.add(new Reference((BookOfBible) null, 10, 20)); // definitely invalid.

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

		VerseList actualResults = model.getVerses("KJV",list);
		assertEquals(8, actualResults.size());
		assertEquals(expectedResults, actualResults);
	}
}
