package bibleReader;

import java.io.File;

import bibleReader.model.Bible;
import bibleReader.model.VerseList;

/**
 * A utility class that has useful methods to read/write Bibles and Verses.
 * 
 * @author cusack
 */
public class BibleIO {

	/**
	 * Read in a file and create a Bible object from it and return it.
	 * 
	 * @param bibleFile
	 * @return
	 */
	// This method is complete, but it won't work until the methods it uses are
	// implemented.
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
	 * Read in a Bible that is saved in the "ATV" format. The format is
	 * described below.
	 * 
	 * @param bibleFile
	 *            The file containing a Bible with .atv extension.
	 * @return A Bible object constructed from the file bibleFile, or null if
	 *         there was an error reading the file.
	 */
	private static VerseList readATV(File bibleFile) {
		// TODO Implement me: Stage 4
		//
		// The ATV format
		//
		// The first line is a summary of what is in the file.
		// In the case of a Bible, the first line will be of the following form.
		// ABBREVIATION: FULL TITLE
		// where ABBREVIATION is generally the acronym used (e.g. KJV for the
		// King James Version), and FULL TITLE is the full title of the version.
		// If the first line does not contain a colon, the entire first line is
		// the version and the description is the empty string ("").
		// If the first line is blank, set the version to "unknown" and the
		// description to "".
		//
		// Each remaining line is of the form:
		// BOOK@CHAPTER:VERSE@TEXT
		//
		// For instance, here is Genesis 1:19 from the kjv file:
		// Ge@1:19@And the evening and the morning were the fourth day.
		//
		// Notice that the book names are abbreviations.
		//
		// HINTS:
		// Be careful how you read this in. Do not create a large string with
		// the contents of the entire file, especially by using the "+" on
		// Strings. It is really inefficient both in terms of memory and time.
		// The easiest way to parse the verses is to use the split method on
		// String.
		// NOTE: The text of some of the verses contains colons (:), so you have
		// to parse carefully!
		return null;
	}

	/**
	 * Read in the Bible that is stored in the XMV format.
	 * 
	 * @param bibleFile
	 *            The file containing a Bible with .xmv extension.
	 * @return A Bible object constructed from the file bibleFile, or null if
	 *         there was an error reading the file.
	 */
	private static VerseList readXMV(File bibleFile) {
		// TODO Implement me: Stage 8

		// The XMV is sort of XML-like, but it doesn't have end tags.
		// No description of the file format is given here.
		// You need to look at the file to determine how it should be parsed.

		// TODO Documentation: Stage 8 (Update the Javadoc comment to describe
		// the format of the file.)
		return null;
	}

	// Note: In the following methods, we should really ensure that the file
	// extension is correct
	// (i.e. it should be ".atv"). However for now we won't worry about it.
	// Hopefully the GUI code
	// will be written in such a way that it will require the extension to be
	// correct if we are
	// concerned about it.

	/**
	 * Write out the Bible in the ATV format.
	 * 
	 * @param file
	 *            The file that the Bible should be written to.
	 * @param bible
	 *            The Bible that will be written to the file.
	 */
	public static void writeBibleATV(File file, Bible bible) {
		// TODO Implement me: Stage 8
		// Don't forget to write the first line of the file.
		// HINT: This and the next method are very similar. It seems like you
		// might be
		// able to implement one of them and then call it from the other one.
	}

	/**
	 * Write out the given verses in the ATV format, using the description as
	 * the first line of the file.
	 * 
	 * @param file
	 *            The file that the Bible should be written to.
	 * @param description
	 *            The contents that will be placed on the first line of the
	 *            file, formatted appropriately.
	 * @param verses
	 *            The verses that will be written to the file.
	 */
	public static void writeVersesATV(File file, String description, VerseList verses) {
		// TODO Implement me: Stage 8
	}

	/**
	 * Write the string out to the given file. It is presumed that the string is
	 * an HTML rendering of some verses, but really it can be anything.
	 * 
	 * @param file
	 * @param text
	 */
	public static void writeText(File file, String text) {
		// TODO Implement me: Stage 8
		// This one should be really simple.
		// My version is 4 lines of code (not counting the try/catch code).
	}
}
