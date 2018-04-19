package bibleReader.tests;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bibleReader.BibleIO;
import bibleReader.model.Bible;
import bibleReader.model.BibleFactory;
import bibleReader.model.VerseList;

/**
 * Tests for the single-word exact searching.
 * 
 * @author Chuck Cusack, March, 2013
 */
public class Stage12_1ConcordanceCreationTest {

	private static Bible	bible;

	@BeforeClass
	public static void readFilesAndCreateConcordance() {
			File file = new File("esv.atv");
			VerseList verses = BibleIO.readBible(file);
			bible = BibleFactory.createBible(verses);
	}

	@Before
	public void setUp() throws Exception {}

	@Test(timeout = 2500)
	public void testConcordanceCreationTime() {
			BibleFactory.createConcordance(bible);
	}
}