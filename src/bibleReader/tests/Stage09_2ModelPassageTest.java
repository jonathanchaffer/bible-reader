package bibleReader.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

import org.junit.Before;
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
 * Tests for the methods of Bible that are related to passage lookup methods.
 * Many of the tests perform the same lookup twice--once using a method that
 * takes in details about the passage (e.g. Reference, Book, Chapter, etc.), and
 * once using the method that takes a String. This should help with debugging
 * since if the former passes but the latter fails, then it narrows down where
 * the problem is.
 * 
 * @author Chuck Cusack, February 12, 2013.
 */
public class Stage09_2ModelPassageTest {

	protected static VerseList[] verseListArray;
	protected static ReferenceList[] references;
	protected MultiBibleModel model;
	private static String[] versions = new String[] { "kjv.atv", "asv.xmv", "esv.atv" };
	protected static ReferenceList allRefs;

	@BeforeClass
	public static void readFile() {
		verseListArray = new VerseList[versions.length];
		references = new ReferenceList[versions.length];
		for (int i = 0; i < versions.length; i++) {
			File file = new File(versions[i]);
			verseListArray[i] = BibleIO.readBible(file);
			references[i] = new ReferenceList();
			for (Verse v : verseListArray[i]) {
				references[i].add(v.getReference());
				Reference ref = v.getReference();
				if(ref.equals(new Reference(BookOfBible.Ecclesiastes, 6, 13))) {
					System.out.println(v);
				}
			}
		}
		TreeSet<Reference> combined = new TreeSet<Reference>();
		for (ReferenceList rl : references) {
			combined.addAll(rl);
		}
		allRefs = new ReferenceList(combined);

		
	}
	
	@Before
	public void setUp() throws Exception {
		model = new BibleReaderModel();
		for (int i = 0; i < versions.length; i++) {
			// Make a copy of the VerseList
			VerseList copyOfVerseList = new VerseList(verseListArray[i].getVersion(), verseListArray[i].getDescription(),
					new ArrayList<Verse>(verseListArray[i]));
			Bible testBible = new ArrayListBible(copyOfVerseList);
			model.addBible(testBible);
		}
	}

	// New for Stage 9
	@Test(timeout = 100)
	public void testVerseOnlyInESV() {
		// One verse is only in ESV
		Reference refOnlyInESV = new Reference(BookOfBible.John3, 1, 15);

		// See if we can properly get the first chapter of 3 John even though
		// two versions don't have a 15th verse.
		ReferenceList results = model.getReferencesForPassage("3 John 1");
		assertEquals(15, results.size());

		// Make sure the getText method returns the correct thing in both cases.
		assertEquals("Peace be to you. The friends greet you. Greet the friends, <sup>p</sup>every one of them.",
				model.getText("ESV", refOnlyInESV));
		assertEquals("", model.getText("KJV", refOnlyInESV));
		assertEquals("", model.getText("ASV", refOnlyInESV));

		// Now try the getVerses method. There should be 15 results, but the
		// last one
		// should be null for KJV and ASV. We will just check that the first 14
		// are not null and the last one is.
		VerseList verses = model.getVerses("KJV", results);
		assertEquals(15, verses.size());
		for (int i = 0; i < 14; i++) {
			assertNotNull(verses.get(i));
		}
		assertNull(verses.get(14));

		verses = model.getVerses("ASV", results);
		assertEquals(15, verses.size());
		assertNull(verses.get(14));
	}

	// New for Stage 9
	@Test(timeout = 100)
	public void testVersesMisingInESV() {
		// Try a passage with a missing verse in the middle.
		// (The ESV doesn't have 9:44 or 9:46).
		ReferenceList refs = model.getReferencesForPassage("Mark 9:43-46");
		assertEquals(4, refs.size());
		for (int i = 0; i < refs.size(); i++) {
			// Just double checking the verse.
			assertEquals(43 + i, refs.get(i).getVerse());
		}
	}

	// New for Stage 9
	@Test(timeout = 100)
	public void testVersesMisingInESV2() {
		// Several verses are in both KJV and ASV, but not ESV.
		ReferenceList refsNotInESV = new ReferenceList();
		refsNotInESV.add(new Reference(BookOfBible.Matthew, 12, 47));
		refsNotInESV.add(new Reference(BookOfBible.Matthew, 17, 21));
		refsNotInESV.add(new Reference(BookOfBible.Matthew, 18, 11));
		refsNotInESV.add(new Reference(BookOfBible.Matthew, 23, 14));
		refsNotInESV.add(new Reference(BookOfBible.Mark, 7, 16));
		refsNotInESV.add(new Reference(BookOfBible.Mark, 9, 44));
		refsNotInESV.add(new Reference(BookOfBible.Mark, 9, 46));
		refsNotInESV.add(new Reference(BookOfBible.Mark, 11, 26));
		refsNotInESV.add(new Reference(BookOfBible.Mark, 15, 28));
		refsNotInESV.add(new Reference(BookOfBible.Luke, 17, 36));
		refsNotInESV.add(new Reference(BookOfBible.Luke, 23, 17));
		refsNotInESV.add(new Reference(BookOfBible.John, 5, 4));
		refsNotInESV.add(new Reference(BookOfBible.Acts, 8, 37));
		refsNotInESV.add(new Reference(BookOfBible.Acts, 15, 34));
		refsNotInESV.add(new Reference(BookOfBible.Acts, 24, 7));
		refsNotInESV.add(new Reference(BookOfBible.Acts, 28, 29));
		refsNotInESV.add(new Reference(BookOfBible.Romans, 16, 24));

		// Just a quick check that the ASV has results for all of these.
		// We could be more specific and check contents and also check
		// KJV, but if everything else passes, everything is probably fine.
		VerseList verseList = model.getVerses("ASV", refsNotInESV);
		assertEquals(17, verseList.size());
		for (int i = 0; i < verseList.size(); i++) {
			assertNotNull(verseList.get(i));
		}

		// getVerses should return a bunch of nulls for ESV
		verseList = model.getVerses("ESV", refsNotInESV);
		assertEquals(17, verseList.size());
		for (int i = 0; i < verseList.size(); i++) {
			assertNull(verseList.get(i));
		}
	}

	// The rest are from Stage 7.
	@Test(timeout = 100)
	public void testSingleVerse_String() {
		ReferenceList results = model.getReferencesForPassage("John 3:16");
		assertEquals(1, results.size());
		assertEquals(allRefs.get(26136), results.get(0));

		results = model.getReferencesForPassage("Genesis 1: 1");
		assertEquals(1, results.size());
		assertEquals(allRefs.get(0), results.get(0));

		results = model.getReferencesForPassage("Rev 22 : 21");
		assertEquals(1, results.size());
		assertEquals(allRefs.get(allRefs.size() - 1), results.get(0));
	}

	@Test(timeout = 100)
	public void testSingleVerse() {
		ReferenceList results = model.getVerseReferences(BookOfBible.John, 3, 16);
		assertEquals(1, results.size());
		System.out.println(allRefs.get(16400));
		assertEquals(allRefs.get(26136), results.get(0));

		results = model.getVerseReferences(BookOfBible.Genesis, 1, 1);
		assertEquals(1, results.size());
		assertEquals(allRefs.get(0), results.get(0));

		results = model.getVerseReferences(BookOfBible.Revelation, 22, 21);
		assertEquals(1, results.size());
		assertEquals(allRefs.get(allRefs.size() - 1), results.get(0));

	}

	@Test(timeout = 100)
	public void testGetBook_String() {
		ReferenceList results = model.getReferencesForPassage("Philemon");
		testPassageResults(new Reference(BookOfBible.Philemon, 1, 1), new Reference(BookOfBible.Philemon, 1, 25),
				results);

		results = model.getReferencesForPassage("1 Kings");
		testPassageResults(new Reference(BookOfBible.Kings1, 1, 1), new Reference(BookOfBible.Kings1, 22, 53), results);

		results = model.getReferencesForPassage("Revelation");
		testPassageResults(new Reference(BookOfBible.Revelation, 1, 1), new Reference(BookOfBible.Revelation, 22, 21),
				results);
	}

	@Test(timeout = 100)
	public void testGetBook() {
		ReferenceList results = model.getBookReferences(BookOfBible.Philemon);
		testPassageResults(new Reference(BookOfBible.Philemon, 1, 1), new Reference(BookOfBible.Philemon, 1, 25),
				results);

		results = model.getBookReferences(BookOfBible.Kings1);
		testPassageResults(new Reference(BookOfBible.Kings1, 1, 1), new Reference(BookOfBible.Kings1, 22, 53), results);

		// Revelation could cause problems, depending on implementation.
		results = model.getBookReferences(BookOfBible.Revelation);
		testPassageResults(new Reference(BookOfBible.Revelation, 1, 1), new Reference(BookOfBible.Revelation, 22, 21),
				results);
	}

	@Test(timeout = 100)
	public void testGetChapter_String() {
		ReferenceList results = model.getReferencesForPassage("Song of Solomon 3");
		testPassageResults(new Reference(BookOfBible.SongOfSolomon, 3, 1), new Reference(BookOfBible.SongOfSolomon, 3,
				11), results);

		results = model.getReferencesForPassage("Revelation 22");
		testPassageResults(new Reference(BookOfBible.Revelation, 22, 1), new Reference(BookOfBible.Revelation, 22, 21),
				results);
	}

	@Test(timeout = 100)
	public void testGetChapter() {
		ReferenceList results = model.getChapterReferences(BookOfBible.SongOfSolomon, 3);
		testPassageResults(new Reference(BookOfBible.SongOfSolomon, 3, 1), new Reference(BookOfBible.SongOfSolomon, 3,
				11), results);

		// Revelation 22 might mess up since it is at the end of the Bible.
		results = model.getChapterReferences(BookOfBible.Revelation, 22);
		testPassageResults(new Reference(BookOfBible.Revelation, 22, 1), new Reference(BookOfBible.Revelation, 22, 21),
				results);
	}

	@Test(timeout = 100)
	public void testGetChapters_String() {
		ReferenceList results = model.getReferencesForPassage("1 John 2-3");
		testPassageResults(new Reference(BookOfBible.John1, 2, 1), new Reference(BookOfBible.John1, 3, 24), results);

		results = model.getReferencesForPassage("1 Timothy 2-4");
		testPassageResults(new Reference(BookOfBible.Timothy1, 2, 1), new Reference(BookOfBible.Timothy1, 4, 16),
				results);
	}

	@Test(timeout = 100)
	public void testGetChapters() {
		ReferenceList results = model.getChapterReferences(BookOfBible.John1, 2, 3);
		testPassageResults(new Reference(BookOfBible.John1, 2, 1), new Reference(BookOfBible.John1, 3, 24), results);

		results = model.getChapterReferences(BookOfBible.Timothy1, 2, 4);
		testPassageResults(new Reference(BookOfBible.Timothy1, 2, 1), new Reference(BookOfBible.Timothy1, 4, 16),
				results);
	}

	@Test(timeout = 100)
	public void testGetPassage_Ref_Ref_String() {
		ReferenceList results = model.getReferencesForPassage("2 Kings 3:4-11:2");
		Reference startRef = new Reference(BookOfBible.Kings2, 3, 4);
		Reference endRef = new Reference(BookOfBible.Kings2, 11, 2);
		testPassageResults(startRef, endRef, results);

		results = model.getReferencesForPassage("Isa 52 :  13  -53:12 ");
		startRef = new Reference(BookOfBible.Isaiah, 52, 13);
		endRef = new Reference(BookOfBible.Isaiah, 53, 12);
		testPassageResults(startRef, endRef, results);

		results = model.getReferencesForPassage("Josh 24:28-33");
		startRef = new Reference(BookOfBible.Joshua, 24, 28);
		endRef = new Reference(BookOfBible.Joshua, 24, 33);
		testPassageResults(startRef, endRef, results);
	}

	@Test(timeout = 100)
	public void testGetPassage_Ref_Ref() {
		Reference startRef = new Reference(BookOfBible.Kings2, 3, 4);
		Reference endRef = new Reference(BookOfBible.Kings2, 11, 2);
		ReferenceList results = model.getPassageReferences(startRef, endRef);
		testPassageResults(startRef, endRef, results);
		
		startRef = new Reference(BookOfBible.Isaiah, 52, 13);
		endRef = new Reference(BookOfBible.Isaiah, 53, 12);
		results = model.getPassageReferences(startRef, endRef);
		testPassageResults(startRef, endRef, results);

		startRef = new Reference(BookOfBible.Joshua, 24, 28);
		endRef = new Reference(BookOfBible.Joshua, 24, 33);
		results = model.getPassageReferences(startRef, endRef);
		testPassageResults(startRef, endRef, results);
	}

	@Test(timeout = 100)
	public void testGetPassage_CVCV_String() {
		ReferenceList results = model.getReferencesForPassage("2 Kings 3:4-11:2");
		Reference startRef = new Reference(BookOfBible.Kings2, 3, 4);
		Reference endRef = new Reference(BookOfBible.Kings2, 11, 2);
		testPassageResults(startRef, endRef, results);

		results = model.getReferencesForPassage("Isa 52 :  13  -53:12 ");
		startRef = new Reference(BookOfBible.Isaiah, 52, 13);
		endRef = new Reference(BookOfBible.Isaiah, 53, 12);
		testPassageResults(startRef, endRef, results);
	}

	@Test(timeout = 100)
	public void testGetPassage_CVCV() {
		ReferenceList results = model.getPassageReferences(BookOfBible.Kings2, 3, 4, 11, 2);
		Reference startRef = new Reference(BookOfBible.Kings2, 3, 4);
		Reference endRef = new Reference(BookOfBible.Kings2, 11, 2);
		testPassageResults(startRef, endRef, results);

		results = model.getPassageReferences(BookOfBible.Isaiah, 52, 13, 53, 12);
		startRef = new Reference(BookOfBible.Isaiah, 52, 13);
		endRef = new Reference(BookOfBible.Isaiah, 53, 12);
		testPassageResults(startRef, endRef, results);
	}

	@Test(timeout = 100)
	public void testGetPassage_CVV_String() {
		ReferenceList results = model.getReferencesForPassage(" Eccl 3:1-8");
		Reference startRef = new Reference(BookOfBible.Ecclesiastes, 3, 1);
		Reference endRef = new Reference(BookOfBible.Ecclesiastes, 3, 8);
		testPassageResults(startRef, endRef, results);

		results = model.getReferencesForPassage("Josh 24:28-33");
		startRef = new Reference(BookOfBible.Joshua, 24, 28);
		endRef = new Reference(BookOfBible.Joshua, 24, 33);
		testPassageResults(startRef, endRef, results);
	}

	@Test(timeout = 100)
	public void testGetPassage_CVV() {
		Reference startRef = new Reference(BookOfBible.Ecclesiastes, 3, 1);
		Reference endRef = new Reference(BookOfBible.Ecclesiastes, 3, 8);

		ReferenceList results = model.getPassageReferences(BookOfBible.Ecclesiastes, 3, 1, 8);
		testPassageResults(startRef, endRef, results);

		startRef = new Reference(BookOfBible.Joshua, 24, 28);
		endRef = new Reference(BookOfBible.Joshua, 24, 33);
		results = model.getPassageReferences(BookOfBible.Joshua, 24, 28, 33);
		testPassageResults(startRef, endRef, results);
	}

	@Test(timeout = 100)
	public void testGetPassage_CCV_String() {
		ReferenceList results = model.getReferencesForPassage(" Ephesians 5-6:9");
		testPassageResults(new Reference(BookOfBible.Ephesians, 5, 1), new Reference(BookOfBible.Ephesians, 6, 9), results);
		
		results = model.getReferencesForPassage("Hebrews 11-12:2");
		testPassageResults(new Reference(BookOfBible.Hebrews, 11, 1), new Reference(BookOfBible.Hebrews, 12, 2), results);
	}

	@Test(timeout = 100)
	public void testGetPassage_CCV() {
		ReferenceList results = model.getPassageReferences(BookOfBible.Ephesians, 5, 1, 6, 9);
		testPassageResults(new Reference(BookOfBible.Ephesians, 5, 1), new Reference(BookOfBible.Ephesians, 6, 9), results);

		results = model.getPassageReferences(BookOfBible.Hebrews, 11, 1, 12, 2);
		testPassageResults(new Reference(BookOfBible.Hebrews, 11, 1), new Reference(BookOfBible.Hebrews, 12, 2), results);
	}
	@Test(timeout = 100)
	public void testSongOfSolomonParsing() {
		ReferenceList results = model.getReferencesForPassage("Song of Solomon 3");
		testPassageResults(new Reference(BookOfBible.SongOfSolomon, 3, 1), new Reference(BookOfBible.SongOfSolomon, 3,
				11), results);
		// try another spelling.
		results = model.getReferencesForPassage("Song of Songs 3");
		testPassageResults(new Reference(BookOfBible.SongOfSolomon, 3, 1), new Reference(BookOfBible.SongOfSolomon, 3,
				11), results);
		// try another spelling.
		results = model.getReferencesForPassage("sos 3");
		testPassageResults(new Reference(BookOfBible.SongOfSolomon, 3, 1), new Reference(BookOfBible.SongOfSolomon, 3,
				11), results);
	}
	@Test(timeout = 100)
	public void testInvalidPassages_String() {	
		String[] invalidPassages = { "Jude 2", "John 3:163", "Herman", "Herman 3", "Herman 3-5", "Herman 2:3-7",
				"Herman 3:4-12:45", "Herman 3-4:7", "1 Hess 3-4:7", "1 Timothy 3-2", "2 Peter 3:7-3",
				"2 Kings 13:4-11:2", "Deut :2-3", "Josh 6:4- :6", "Ruth : - :", "2 Sam : 4-7 :", "Ephesians 5:2,4",
				"John 3;16", "Herman 13-4:7", "1 Hess 34, 35", "Isaiah 53:12-52:13" };
		for (String s : invalidPassages) {
			ReferenceList results = model.getReferencesForPassage(s);
			assertEquals(s + " should have returned 0 results but didn't", 0, results.size());
		}
	}

	@Test(timeout = 100)
	public void testInvalidPassages() {

		// Now let's try invalid ones that call the other get methods
		ReferenceList results = model.getVerseReferences(BookOfBible.Revelation, 23, 2);
		assertEquals(0, results.size());
		results = model.getBookReferences(null);
		assertEquals(0, results.size());
		results = model.getChapterReferences(BookOfBible.John3, -4);
		assertEquals(0, results.size());
		results = model.getPassageReferences(new Reference(BookOfBible.Revelation, 1, 1), new Reference(
				BookOfBible.Genesis, 1, 1));
		assertEquals(0, results.size());
		results = model.getPassageReferences(new Reference(BookOfBible.Ephesians, 3, 2), new Reference(
				BookOfBible.Ephesians, 2, 2));
		assertEquals(0, results.size());
		results = model.getPassageReferences(new Reference(BookOfBible.Ephesians, 13, 2), new Reference(
				BookOfBible.Ephesians, 2, 2));
		assertEquals(0, results.size());
		results = model.getChapterReferences(BookOfBible.Samuel2, 12, 10);
		assertEquals(0, results.size());
		results = model.getPassageReferences(BookOfBible.Galatians, 2, 10, 4);
		assertEquals(0, results.size());
		results = model.getPassageReferences(BookOfBible.Romans, 4, 3, 3, 6);
		assertEquals(0, results.size());
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
		while (!allRefs.get(i).equals(firstVerse)) {
			i++;
		}
		int firstIndex = i;
		while (!allRefs.get(i).equals(lastVerse)) {
			i++;
		}
		ReferenceList expected = new ReferenceList();
		int lastIndex = i + 1; // It does not include the last index, so add
								// one.
		for (int j = firstIndex; j < lastIndex; j++) {
			expected.add(allRefs.get(j));
		}
		assertArrayEquals(expected.toArray(), actualResults.toArray());
	}
}
