package bibleReader.model;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A class that stores a version of the Bible.
 * 
 * @author Chuck Cusack (Provided the interface)
 * @author ? (provided the implementation)
 */
public class TreeMapBible implements Bible {

	// The Fields
	private String						version;
	private TreeMap<Reference, String>	theVerses;

	// Or replace the above with:
	// private TreeMap<Reference, Verse> theVerses;
	// Add more fields as necessary.

	/**
	 * Create a new Bible with the given verses.
	 * 
	 * @param version the version of the Bible (e.g. ESV, KJV, ASV, NIV).
	 * @param verses All of the verses of this version of the Bible.
	 */
	public TreeMapBible(VerseList verses) {
		// TODO Implement me: Stage 11
	}

	@Override
	public int getNumberOfVerses() {
		// TODO Implement me: Stage 11
		return 0;
	}

	@Override
	public VerseList getAllVerses() {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Implement me: Stage 11
		return "";
	}

	@Override
	public String getTitle() {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public boolean isValid(Reference ref) {
		// TODO Implement me: Stage 11
		return false;
	}

	@Override
	public String getVerseText(Reference r) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public Verse getVerse(Reference r) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public Verse getVerse(BookOfBible book, int chapter, int verse) {
		// TODO Implement me: Stage 11
		return null;
	}

	// ---------------------------------------------------------------------------------------------
	// The following part of this class should be implemented for stage 4.
	//
	// For Stage 11 the first two methods below will be implemented as specified in the comments.
	// Do not over think these methods. All three should be pretty straightforward to implement.
	// For Stage 8 (give or take a 1 or 2) you will re-implement them so they work better.
	// At that stage you will create another class to facilitate searching and use it here.
	// (Essentially these two methods will be delegate methods.)
	// ---------------------------------------------------------------------------------------------

	@Override
	public VerseList getVersesContaining(String phrase) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public ReferenceList getReferencesContaining(String phrase) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public VerseList getVerses(ReferenceList references) {
		// TODO Implement me: Stage 11
		return null;
	}

	// ---------------------------------------------------------------------------------------------
	// The following part of this class should be implemented for Stage 11.
	//
	// HINT: Do not reinvent the wheel. Some of these methods can be implemented by looking up
	// one or two things and calling another method to do the bulk of the work.
	// ---------------------------------------------------------------------------------------------

	@Override
	public int getLastVerseNumber(BookOfBible book, int chapter) {
		// TODO Implement me: Stage 11
		return -1;
	}

	@Override
	public int getLastChapterNumber(BookOfBible book) {
		// TODO Implement me: Stage 11
		return -1;
	}

	@Override
	public ReferenceList getReferencesInclusive(Reference firstVerse, Reference lastVerse) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public ReferenceList getReferencesExclusive(Reference firstVerse, Reference lastVerse) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public ReferenceList getReferencesForBook(BookOfBible book) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public ReferenceList getReferencesForChapter(BookOfBible book, int chapter) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public ReferenceList getReferencesForChapters(BookOfBible book, int chapter1, int chapter2) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public ReferenceList getReferencesForPassage(BookOfBible book, int chapter, int verse1, int verse2) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public ReferenceList getReferencesForPassage(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public VerseList getVersesInclusive(Reference firstVerse, Reference lastVerse) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public VerseList getVersesExclusive(Reference firstVerse, Reference lastVerse) {
		// Implementation of this method provided by Chuck Cusack.
		// This is provided so you have an example to help you get started
		// with the other methods.

		// We will store the resulting verses here. We copy the version from
		// this Bible and set the description to be the passage that was searched for.
		VerseList someVerses = new VerseList(getVersion(), firstVerse + "-" + lastVerse);

		// Make sure the references are in the correct order. If not, return an empty list.
		if (firstVerse.compareTo(lastVerse) > 0) {
			return someVerses;
		}
		// Return the portion of the TreeMap that contains the verses between
		// the first and the last, not including the last.
		SortedMap<Reference, String> s = theVerses.subMap(firstVerse, lastVerse);

		// Get the entries from the map so we can iterate through them.
		Set<Map.Entry<Reference, String>> mySet = s.entrySet();

		// Iterate through the set and put the verses in the VerseList.
		for (Map.Entry<Reference, String> element : mySet) {
			Verse aVerse = new Verse(element.getKey(), element.getValue());
			someVerses.add(aVerse);
		}
		return someVerses;
	}

	@Override
	public VerseList getBook(BookOfBible book) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public VerseList getChapter(BookOfBible book, int chapter) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public VerseList getChapters(BookOfBible book, int chapter1, int chapter2) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public VerseList getPassage(BookOfBible book, int chapter, int verse1, int verse2) {
		// TODO Implement me: Stage 11
		return null;
	}

	@Override
	public VerseList getPassage(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2) {
		// TODO Implement me: Stage 11
		return null;
	}

}
