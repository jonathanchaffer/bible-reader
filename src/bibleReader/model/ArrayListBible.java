package bibleReader.model;

import java.util.ArrayList;

/**
 * A class that stores a version of the Bible.
 * 
 * @author Chuck Cusack (Provided the interface). Modified February 9, 2015.
 * @author Jonathan Chaffer (provided the implementation), 2018.
 */
public class ArrayListBible implements Bible {

	// The Fields
	private String version;
	private String title;
	private ArrayList<Verse> verses;

	/**
	 * Create a new Bible with the given verses.
	 * 
	 * @param version
	 *            the version of the Bible (e.g. ESV, KJV, ASV, NIV).
	 * @param verses
	 *            All of the verses of this version of the Bible.
	 */
	public ArrayListBible(VerseList verses) {
		this.verses = new ArrayList<Verse>(verses);
		this.version = verses.getVersion();
		this.title = verses.getDescription();
	}

	/**
	 * Returns the number of verses that this Bible is currently storing.
	 * 
	 * @return the number of verses in the Bible.
	 */
	@Override
	public int getNumberOfVerses() {
		return verses.size();
	}

	/**
	 * Returns which version this object is storing (e.g. ESV, KJV)
	 * 
	 * @return a string representation of the version.
	 */
	@Override
	public String getVersion() {
		return version;
	}

	/**
	 * @return a string representation of the full title.
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * @param ref
	 *            any reference object
	 * @return true if and only if ref is actually in this Bible
	 */
	@Override
	public boolean isValid(Reference ref) {
		for (Verse verse : verses) {
			if (verse.getReference().equals(ref)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the text of the verse with the given reference
	 * 
	 * @param ref
	 *            the Reference to the desired verse.
	 * @return a string representation of the text of the verse, or null if the
	 *         Bible does not contain the verse.
	 */
	@Override
	public String getVerseText(Reference ref) {
		for (Verse verse : verses) {
			if (verse.getReference().equals(ref)) {
				return verse.getText();
			}
		}
		return null;
	}

	/**
	 * Return a Verse object for the corresponding Reference.
	 * 
	 * @param ref
	 *            A reference to the desired verse.
	 * @return a Verse object which has Reference r, or null if the verse isn't
	 *         in the Bible.
	 */
	@Override
	public Verse getVerse(Reference ref) {
		for (Verse verse : verses) {
			if (verse.getReference().equals(ref)) {
				return verse;
			}
		}
		return null;
	}

	/**
	 * @param book
	 *            the book of the Bible
	 * @param chapter
	 *            the chapter of the book
	 * @param verse
	 *            the verse of the chapter
	 * @return a Verse object with reference "book chapter:verse", or null if
	 *         the verse isn't in the Bible.
	 */
	@Override
	public Verse getVerse(BookOfBible book, int chapter, int verse) {
		Reference ref = new Reference(book, chapter, verse);
		return getVerse(ref);
	}

	/**
	 * @return a VerseList containing all of the verses from the Bible, in
	 *         order. The version of the VerseList should be set to the version
	 *         of this Bible and the description should be set to the title of
	 *         this Bible.
	 */
	@Override
	public VerseList getAllVerses() {
		VerseList allVerses = new VerseList(getVersion(), getTitle());
		for (Verse verse : verses) {
			allVerses.add(verse);
		}
		return allVerses;
	}

	/**
	 * Returns a VerseList of all verses containing <i>phrase</i>, which may be
	 * a word, sentence, or whatever. This method just does simple string
	 * matching, so if <i>phrase</i> is <i>eaten</i>, verses with <i>beaten</i>
	 * will be included.
	 * 
	 * @param phrase
	 *            the word/phrase to search for.
	 * @return a VerseList of all verses containing <i>phrase</i>, which may be
	 *         a word, sentence, or whatever. If there are no such verses,
	 *         returns an empty VerseList. In all cases, the version will be set
	 *         to the version of the Bible (via getVersion()) and the
	 *         description will be set to parameter <i>phrase</i>.
	 */
	@Override
	public VerseList getVersesContaining(String phrase) {
		VerseList versesToReturn = new VerseList(getVersion(), phrase);
		phrase = phrase.toLowerCase();
		if (!phrase.equals("")) {
			for (Verse verse : verses) {
				if (verse.getText().toLowerCase().contains(phrase)) {
					versesToReturn.add(verse);
				}
			}
		}
		return versesToReturn;
	}

	/**
	 * Returns a ReferenceList of all references for verses containing
	 * <i>phrase</i>, which may be a word, sentence, or whatever. This method
	 * just does simple string matching, so if <i>phrase</i> is <i>eaten</i>,
	 * verses with <i>beaten</i> will be included.
	 * 
	 * @param phrase
	 *            the phrase to search for
	 * @return a ReferenceList of all references for verses containing
	 *         <i>phrase</i>, which may be a word, sentence, or whatever. If
	 *         there are no such verses, returns an empty ReferenceList.
	 */
	@Override
	public ReferenceList getReferencesContaining(String phrase) {
		ReferenceList refsToReturn = new ReferenceList();
		phrase = phrase.toLowerCase();
		if (!phrase.equals("")) {
			for (Verse verse : verses) {
				if (verse.getText().toLowerCase().contains(phrase)) {
					refsToReturn.add(verse.getReference());
				}
			}
		}
		return refsToReturn;
	}

	/**
	 * @param references
	 *            a ReferenceList of references for which verses are being
	 *            requested
	 * @return a VerseList with each element being the Verse with that Reference
	 *         from this Bible, or null if the particular Reference does not
	 *         occur in this Bible. Thus, the size of the returned list will be
	 *         the same as the size of the references parameter, with the items
	 *         from each corresponding. The version will be set to the version
	 *         of the Bible (via getVersion()) and the description will be set
	 *         "Arbitrary list of Verses".
	 */
	@Override
	public VerseList getVerses(ReferenceList references) {
		VerseList versesToReturn = new VerseList(getVersion(), "Arbitrary list of Verses");
		for (Reference ref : references) {
			versesToReturn.add(getVerse(ref));
		}
		return versesToReturn;
	}

	@Override
	public int getLastVerseNumber(BookOfBible book, int chapter) {
		return -1;
	}

	@Override
	public int getLastChapterNumber(BookOfBible book) {
		return -1;
	}

	@Override
	public ReferenceList getReferencesInclusive(Reference firstVerse, Reference lastVerse) {
		return null;
	}

	@Override
	public ReferenceList getReferencesExclusive(Reference firstVerse, Reference lastVerse) {
		return null;
	}

	@Override
	public ReferenceList getReferencesForBook(BookOfBible book) {
		return null;
	}

	@Override
	public ReferenceList getReferencesForChapter(BookOfBible book, int chapter) {
		return null;
	}

	@Override
	public ReferenceList getReferencesForChapters(BookOfBible book, int chapter1, int chapter2) {
		return null;
	}

	@Override
	public ReferenceList getReferencesForPassage(BookOfBible book, int chapter, int verse1, int verse2) {
		return null;
	}

	@Override
	public ReferenceList getReferencesForPassage(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2) {
		return null;
	}

	@Override
	public VerseList getVersesInclusive(Reference firstVerse, Reference lastVerse) {
		return null;
	}

	@Override
	public VerseList getVersesExclusive(Reference firstVerse, Reference lastVerse) {
		return null;
	}

	@Override
	public VerseList getBook(BookOfBible book) {
		return null;
	}

	@Override
	public VerseList getChapter(BookOfBible book, int chapter) {
		return null;
	}

	@Override
	public VerseList getChapters(BookOfBible book, int chapter1, int chapter2) {
		return null;
	}

	@Override
	public VerseList getPassage(BookOfBible book, int chapter, int verse1, int verse2) {
		return null;
	}

	@Override
	public VerseList getPassage(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2) {
		return null;
	}
}
