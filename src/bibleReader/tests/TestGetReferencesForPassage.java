package bibleReader.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

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
 * Tests for the getReferencesForPassage method of BibleReaderModel.
 * 
 * @author Jonathan Chaffer, Jacob Lahr, Gisa Gatera, 2018
 */
public class TestGetReferencesForPassage {
	private static VerseList versesFromFile;
	private BibleReaderModel model;

	/**
	 * Returns a VerseList for the specified reference lookup in KJV.
	 * 
	 * @param reference String of the reference lookup.
	 * @return A VerseList for the specified reference lookup in KJV.
	 */
	public VerseList getVersesForReference(String reference) {
		ReferenceList list = model.getReferencesForPassage(reference);
		VerseList results = model.getVerses("KJV", list);
		return results;
	}

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

	@Test
	public void testSingleVerse() {
		VerseList vl1 = new VerseList("KJV", "");
		vl1.add(model.getBible("KJV").getVerse(new Reference(BookOfBible.John, 3, 16)));
		assertArrayEquals(vl1.toArray(), getVersesForReference("John 3 : 16").toArray());

		VerseList vl2 = new VerseList("KJV", "");
		vl2.add(model.getBible("KJV").getVerse(new Reference(BookOfBible.Genesis, 1, 1)));
		assertArrayEquals(vl2.toArray(), getVersesForReference("Gen 1:1").toArray());

		VerseList vl3 = new VerseList("KJV", "");
		vl3.add(model.getBible("KJV").getVerse(new Reference(BookOfBible.Revelation, 22, 21)));
		assertArrayEquals(vl3.toArray(), getVersesForReference("Revelation 22:21").toArray());
	}

	@Test
	public void testMultipleVersesFromOneChapter() {
		VerseList vl1 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Ecclesiastes, 3, 1),
				new Reference(BookOfBible.Ecclesiastes, 3, 8));
		assertArrayEquals(vl1.toArray(), getVersesForReference("Ecclesiastes 3 : 1 - 8").toArray());

		VerseList vl2 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Joshua, 24, 28),
				new Reference(BookOfBible.Joshua, 24, 33));
		assertArrayEquals(vl2.toArray(), getVersesForReference("Joshua 24:28-33").toArray());

		VerseList vl3 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Psalms, 23, 1),
				new Reference(BookOfBible.Psalms, 23, 6));
		assertArrayEquals(vl3.toArray(), getVersesForReference("Psalm 23:1-6").toArray());
	}

	@Test
	public void testWholeChapters() {
		VerseList vl1 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.SongOfSolomon, 3, 1),
				new Reference(BookOfBible.SongOfSolomon, 3, 11));
		assertArrayEquals(vl1.toArray(), getVersesForReference("Song of Solomon 3").toArray());

		VerseList vl2 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Revelation, 22, 1),
				new Reference(BookOfBible.Revelation, 22, 21));
		assertArrayEquals(vl2.toArray(), getVersesForReference("Revelation 22").toArray());

		VerseList vl3 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Timothy1, 2, 1),
				new Reference(BookOfBible.Timothy1, 4, 16));
		assertArrayEquals(vl3.toArray(), getVersesForReference("1 Tim 2-4").toArray());

		VerseList vl4 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.John1, 2, 1),
				new Reference(BookOfBible.John1, 3, 24));
		assertArrayEquals(vl4.toArray(), getVersesForReference("1 John 2-3").toArray());
	}

	@Test
	public void testVersesFromMultipleChapters() {
		VerseList vl1 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Isaiah, 52, 13),
				new Reference(BookOfBible.Isaiah, 53, 12));
		assertArrayEquals(vl1.toArray(), getVersesForReference("Is 52:13 - 53:12").toArray());

		VerseList vl2 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Malachi, 3, 6),
				new Reference(BookOfBible.Malachi, 4, 6));
		assertArrayEquals(vl2.toArray(), getVersesForReference("Mal 3:6-4:6").toArray());
	}

	@Test
	public void testWholeBook() {
		VerseList vl1 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Kings1, 1, 1),
				new Reference(BookOfBible.Kings1, 22, 54));
		assertArrayEquals(vl1.toArray(), getVersesForReference("1 Kings").toArray());

		VerseList vl2 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Philemon, 1, 1),
				new Reference(BookOfBible.Philemon, 4, 23));
		assertArrayEquals(vl2.toArray(), getVersesForReference("Philemon").toArray());
	}

	@Test
	public void testOddSyntax() {
		VerseList vl1 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Ephesians, 5, 1),
				new Reference(BookOfBible.Ephesians, 6, 9));
		assertArrayEquals(vl1.toArray(), getVersesForReference("Ephesians 5-6:9").toArray());

		VerseList vl2 = model.getBible("KJV").getVersesInclusive(new Reference(BookOfBible.Hebrews, 11, 1),
				new Reference(BookOfBible.Hebrews, 12, 2));
		assertArrayEquals(vl2.toArray(), getVersesForReference("Hebrews 11-12:2").toArray());
	}

	@Test
	public void testInvalidBookChapterOrVerse() {
		VerseList emptyVerseList = new VerseList("", "");
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("Jude 2").toArray());
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("Herman 2").toArray());
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("John 3:163").toArray());
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("Mal 13:6-24:7").toArray());
	}

	@Test
	public void testOutOfOrderChapeterOrVerse() {
		VerseList emptyVerseList = new VerseList("", "");
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("1Tim 3-2").toArray());
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("Deut :2-3").toArray());
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("Josh 6:4- :6").toArray());
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("Ruth : - :").toArray());
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("2 Sam : 4-7 :").toArray());
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("Ephesians 5:2,4").toArray());
		assertArrayEquals(emptyVerseList.toArray(), getVersesForReference("John 3;16").toArray());
	}
}
