package bibleReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import bibleReader.model.Bible;
import bibleReader.model.BookOfBible;
import bibleReader.model.Reference;
import bibleReader.model.Verse;
import bibleReader.model.VerseList;

/**
 * A utility class that has useful methods to read/write Bibles and Verses.
 * 
 * @author cusack
 * @author Jonathan Chaffer, 2018
 */
public class BibleIO {

	/**
	 * Read in a file and create a Bible object from it and return it.
	 * 
	 * @param bibleFile
	 * @return
	 */
	public static VerseList readBible(File bibleFile) { // Get the extension of
														// the file
		String name = bibleFile.getName();
		String extension = name.substring(name.lastIndexOf('.') + 1, name.length());

		// Call the read method based on the file type.
		if ("atv".equals(extension.toLowerCase())) {
			return readATV(bibleFile);
		} else if ("xmv".equals(extension.toLowerCase())) {
			return readXMV(bibleFile);
		} else {
			return null;
		}
	}

	/**
	 * Read in a Bible that is saved in the "ATV" format. The format is described
	 * below.
	 * 
	 * @param bibleFile
	 *            The file containing a Bible with .atv extension.
	 * @return A Bible object constructed from the file bibleFile, or null if there
	 *         was an error reading the file.
	 */
	private static VerseList readATV(File bibleFile) {
		try {
			FileReader fr = new FileReader(bibleFile);
			BufferedReader br = new BufferedReader(fr);

			String version = "unknown";
			String title = "";

			// read the first line
			String line = br.readLine();
			// if the first line is not empty...
			if (!line.equals("")) {
				// split the first line by ": "
				String[] firstLineElements = line.split(": ");
				version = firstLineElements[0];
				if (firstLineElements.length == 2) {
					title = firstLineElements[1];
				}
			}

			// construct the VerseList
			VerseList verses = new VerseList(version, title);

			line = br.readLine();
			while (line != null) {
				// split the line by "@"
				String[] lineElements = line.split("@");
				// if it did not successfully split into 3 strings, return null
				if (lineElements.length != 3) {
					return null;
				}

				// split the second element of lineElements by ":"
				String[] refElements = lineElements[1].split(":");
				// if it did not successfully split into 2 strings, return null
				if (refElements.length != 2) {
					return null;
				}

				// create local variable for book
				BookOfBible book = BookOfBible.getBookOfBible(lineElements[0]);
				// if book was null, return null
				if (book == null) {
					return null;
				}

				// create local variable for chapter, verse, text
				int chapter = Integer.parseInt(refElements[0]);
				int verse = Integer.parseInt(refElements[1]);
				String text = lineElements[2];

				// add the Verse to verses
				verses.add(new Verse(new Reference(book, chapter, verse), text));

				// go to the next line
				line = br.readLine();
			}
			br.close();
			return verses;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Read in the Bible that is stored in the XMV format.
	 * 
	 * @param bibleFile
	 *            The file containing a Bible with .xmv extension.
	 * @return A Bible object constructed from the file bibleFile, or null if there
	 *         was an error reading the file.
	 */
	private static VerseList readXMV(File bibleFile) {
		return null;
	}

	/**
	 * Write out the Bible in the ATV format.
	 * 
	 * @param file
	 *            The file that the Bible should be written to.
	 * @param bible
	 *            The Bible that will be written to the file.
	 */
	public static void writeBibleATV(File file, Bible bible) {
	}

	/**
	 * Write out the given verses in the ATV format, using the description as the
	 * first line of the file.
	 * 
	 * @param file
	 *            The file that the Bible should be written to.
	 * @param description
	 *            The contents that will be placed on the first line of the file,
	 *            formatted appropriately.
	 * @param verses
	 *            The verses that will be written to the file.
	 */
	public static void writeVersesATV(File file, String description, VerseList verses) {
	}

	/**
	 * Write the string out to the given file. It is presumed that the string is an
	 * HTML rendering of some verses, but really it can be anything.
	 * 
	 * @param file
	 * @param text
	 */
	public static void writeText(File file, String text) {
	}
}
