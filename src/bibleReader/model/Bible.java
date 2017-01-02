package bibleReader.model;

/**
 * An interface for an implementation of a Bible.
 * 
 * @author Chuck
 *
 */
public interface Bible {
	/**
	 * Returns the number of verses that this Bible is currently storing.
	 * 
	 * @return the number of verses in the Bible.
	 */
	public abstract int getNumberOfVerses();

	/**
	 * @return a VerseList containing all of the verses from the Bible, in
	 *         order. The version of the VerseList should be set to the version
	 *         of this Bible and the description should be set to the title of
	 *         this Bible.
	 */
	public abstract VerseList getAllVerses();

	/**
	 * Returns which version this object is storing (e.g. ESV, KJV)
	 * 
	 * @return a string representation of the version.
	 */
	public abstract String getVersion();

	/**
	 * @return a string representation of the full title.
	 */
	public abstract String getTitle();

	/**
	 * @param ref
	 *            any reference object
	 * @return true if and only if ref is actually in this Bible
	 */
	public abstract boolean isValid(Reference ref);

	/**
	 * Return the text of the verse with the given reference
	 * 
	 * @param r
	 *            the Reference to the desired verse.
	 * @return a string representation of the text of the verse, or null if the
	 *         Bible does not contain the verse.
	 */
	public abstract String getVerseText(Reference r);

	/**
	 * Return a Verse object for the corresponding Reference.
	 * 
	 * @param r
	 *            A reference to the desired verse.
	 * @return a Verse object which has Reference r, or null if the verse isn't
	 *         in the Bible.
	 */
	public abstract Verse getVerse(Reference r);

	/**
	 * @param book
	 *            the book of the Bible
	 * @param chapter
	 *            the chapter of the book
	 * @param verse1
	 *            the first verse of the chapter
	 * @return a Verse object with reference "book chapter:verse", or null if
	 *         the verse isn't in the Bible.
	 */
	public abstract Verse getVerse(BookOfBible book, int chapter, int verse);

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
	public abstract VerseList getVersesContaining(String phrase);

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
	public abstract ReferenceList getReferencesContaining(String phrase);

	/**
	 * @param references
	 *            a ReferenceList of references for which verses are being requested
	 * @return a VerseList with each element being the Verse with that Reference
	 *         from this Bible, or null if the particular Reference does not
	 *         occur in this Bible. Thus, the size of the returned list will be
	 *         the same as the size of the references parameter, with the items
	 *         from each corresponding. The version will be set to the version
	 *         of the Bible (via getVersion()) and the description will be set
	 *         "Arbitrary list of Verses".
	 */
	public abstract VerseList getVerses(ReferenceList references);

	/**
	 * @param book
	 *            The book.
	 * @param chapter
	 *            The chapter.
	 * @return the number of the final verse of the given chapter in the given
	 *         book, or -1 if there is a problem (e.g., the book or chapter are
	 *         invalid/null).
	 */
	public abstract int getLastVerseNumber(BookOfBible book, int chapter);

	/**
	 * @param book
	 *            The book
	 * @return the number of the final chapter of the given book, or -1 if there
	 *         is a problem (e.g., the book is invalid/null).
	 */
	public abstract int getLastChapterNumber(BookOfBible book);

	// -------------------------------------------------------------------------------------------------
	// Passage getters that return ReferenceLists.
	/**
	 * @param firstVerse
	 *            the starting verse of the passage
	 * @param lastVerse
	 *            the final verse of the passage
	 * @return a ReferenceList of all references in this Bible between the
	 *         firstVerse and lastVerse, inclusive of both; or an empty list if
	 *         the range of verses is invalid in this Bible. An invalid range
	 *         includes if either firstVerse or lastVerse is invalid or if
	 *         firstVerse.compareTo(lastVerse) > 0.
	 */
	public abstract ReferenceList getReferencesInclusive(Reference firstVerse, Reference lastVerse);

	/**
	 * @param firstVerse
	 *            the starting verse of the passage
	 * @param lastVerse
	 *            the final verse of the passage
	 * @return a ReferenceList of all references between the firstVerse and
	 *         lastVerse, exclusive of lastVerse; or an empty list if the range
	 *         of verses is invalid in this Bible.  An invalid range includes if
	 *         firstVerse is invalid or if firstVerse.compareTo(lastVerse) > 0.
	 *         Notice that lastVerse is allowed to be invalid. This can be
	 *         useful in certain cases to find passages more quickly.
	 */
	public abstract ReferenceList getReferencesExclusive(Reference firstVerse, Reference lastVerse);

	/**
	 * Return a list of all verses from a given book.
	 * 
	 * @param book
	 *            The desired book.
	 * @return a ReferenceList with version set to this.getVersion(),
	 *         description set to a String representation of <i>book</i>, and
	 *         containing all verses from the given book, in order; or an empty
	 *         list if the book is invalid in this Bible.
	 */
	public abstract ReferenceList getReferencesForBook(BookOfBible book);

	/**
	 * @param book
	 *            the book of the Bible
	 * @param chapter
	 *            the chapter of the book
	 * @return a ReferenceList containing all references from the given chapter
	 *         of the given book, in order; or an empty list if the passage is
	 *         invalid in this Bible.
	 */
	public abstract ReferenceList getReferencesForChapter(BookOfBible book, int chapter);

	/**
	 * @param book
	 *            the book of the Bible
	 * @param chapter1
	 *            the chapter of the book
	 * @param chapter2
	 *            the chapter of the book
	 * @return a ReferenceList containing all references from the given chapters
	 *         of the given book, in order; or an empty list if the passage is
	 *         invalid in this Bible.
	 */
	public abstract ReferenceList getReferencesForChapters(BookOfBible book, int chapter1, int chapter2);

	/**
	 * @param book
	 *            the book of the Bible
	 * @param chapter
	 *            the chapter of the book
	 * @param verse1
	 *            the first verse of the chapter
	 * @param verse2
	 *            the second verse of the chapter
	 * @return a ReferenceList containing all references from the given passage,
	 *         in order; or an empty list if the passage is invalid in this
	 *         Bible.
	 */
	public abstract ReferenceList getReferencesForPassage(BookOfBible book, int chapter, int verse1, int verse2);

	/**
	 * @param book
	 *            the book of the Bible
	 * @param chapter1
	 *            the first chapter of the book
	 * @param verse1
	 *            the first verse of the chapter
	 * @param chapter2
	 *            the second chapter of the book
	 * @param verse2
	 *            the second verse of the chapter
	 * @return a ReferenceList containing all references from the given passage,
	 *         in order; or an empty list if the passage is invalid in this
	 *         Bible.
	 */
	public abstract ReferenceList getReferencesForPassage(BookOfBible book, int chapter1, int verse1, int chapter2,
			int verse2);

	// -------------------------------------------------------------------------------------------------
	// Passage getters that return VerseLists.
	/**
	 * @param firstVerse
	 *            the starting verse of the passage
	 * @param lastVerse
	 *            the final verse of the passage
	 * @return a VerseList with version set to this.getVersion(), description
	 *         set to a String representation of the range of verses requested,
	 *         and containing all verses between the firstVerse and lastVerse,
	 *         inclusive of both; or an empty list if the range of verses is
	 *         invalid in this Bible. An invalid range includes if either
	 *         firstVerse or lastVerse is invalid or if
	 *         firstVerse.compareTo(lastVerse) > 0.
	 */
	public abstract VerseList getVersesInclusive(Reference firstVerse, Reference lastVerse);

	/**
	 * @param firstVerse
	 *            the starting verse of the passage
	 * @param lastVerse
	 *            the final verse of the passage
	 * @return a VerseList with version set to this.getVersion(), description
	 *         set to a String representation of the range of verses requested,
	 *         with " excluding the final one" tacked onto the end, and
	 *         containing all verses between the firstVerse and lastVerse,
	 *         exclusive of the last one; or an empty list if the range of
	 *         verses is invalid in this Bible. An invalid range includes if
	 *         firstVerse is invalid or if firstVerse.compareTo(lastVerse) > 0.
	 *         Notice that lastVerse is allowed to be invalid. This can be
	 *         useful in certain cases to find passages more quickly.
	 */
	public abstract VerseList getVersesExclusive(Reference firstVerse, Reference lastVerse);

	/**
	 * Return a list of all verses from a given book.
	 * 
	 * @param book
	 *            The desired book.
	 * @return a VerseList with version set to this.getVersion(), description
	 *         set to a String representation of <i>book</i>, and containing all
	 *         verses from the given book, in order; or an empty list if the
	 *         book is invalid in this Bible.
	 */
	public abstract VerseList getBook(BookOfBible book);

	/**
	 * @param book
	 *            the book of the Bible
	 * @param chapter
	 *            the chapter of the book
	 * @return a VerseList with version set to this.getVersion(), description
	 *         set to a String representing the book followed by the chapter
	 *         (e.g. "Genesis 1"), and containing all verses from the given
	 *         chapter of the given book, in order; or an empty list if the book
	 *         is invalid in this Bible.
	 */
	public abstract VerseList getChapter(BookOfBible book, int chapter);

	/**
	 * @param book
	 *            the book of the Bible
	 * @param chapter1
	 *            the chapter of the book
	 * @param chapter2
	 *            the chapter of the book
	 * @return a VerseList with version set to this.getVersion(), description
	 *         set to a String representing the book followed by the chapters
	 *         (e.g. "Genesis 1-2"), and containing all verses from the given
	 *         chapters of the given book, in order; or an empty list if the
	 *         passage is invalid in this Bible.
	 */
	public abstract VerseList getChapters(BookOfBible book, int chapter1, int chapter2);

	/**
	 * @param book
	 *            the book of the Bible
	 * @param chapter
	 *            the chapter of the book
	 * @param verse1
	 *            the first verse of the chapter
	 * @param verse2
	 *            the second verse of the chapter
	 * @return a VerseList with version set to this.getVersion(), description
	 *         set to a String representing the verses requested (e.g.
	 *         "Genesis 1:3-17"), and containing all verses from the given
	 *         passage, in order; or an empty list if the passage is invalid in
	 *         this Bible.
	 */
	public abstract VerseList getPassage(BookOfBible book, int chapter, int verse1, int verse2);

	/**
	 * @param book
	 *            the book of the Bible
	 * @param chapter1
	 *            the first chapter of the book
	 * @param verse1
	 *            the first verse of the chapter
	 * @param chapter2
	 *            the second chapter of the book
	 * @param verse2
	 *            the second verse of the chapter
	 * @return a VerseList with version set to this.getVersion(), description
	 *         set to a String representing the verses requested (e.g.
	 *         "Genesis 1:3-2:17"), and containing all verses from the given
	 *         passage, in order; or an empty list if the passage is invalid in
	 *         this Bible.
	 */
	public abstract VerseList getPassage(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2);
}