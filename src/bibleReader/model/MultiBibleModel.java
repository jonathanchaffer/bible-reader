package bibleReader.model;

public interface MultiBibleModel {

	/**
	 * @return an array containing the abbreviations for all of the Bibles
	 *         currently stored in the model in alphabetical order.
	 */
	public String[] getVersions();

	/**
	 * @return the number of versions currently stored in the model.
	 */
	public int getNumberOfVersions();

	/**
	 * Add a Bible to the model.
	 * 
	 * @param bible
	 *            The bible you want to add to the model.
	 */
	public void addBible(Bible bible);

	/**
	 * @param version
	 *            the abbreviation for the version of the Bible that you want.
	 * @return the Bible that has the abbreviation "version", or null if it
	 *         isn't in the model.
	 */
	public Bible getBible(String version);

	/**
	 * @param version
	 *            The version of the Bible to look in.
	 * @param references
	 *            The set of References we want the full verses for.
	 * @return The verses from the given Bible for the given list of references,
	 *         or null if the Bible isn't in the model. Since this is a delegate
	 *         method, see the getVerses method from the Bible interface for
	 *         details of how the list is constructed.
	 */
	public VerseList getVerses(String version, ReferenceList references);

	/**
	 * @param version
	 *            The version of the Bible to look in.
	 * @param reference
	 *            The Reference we want the text for from the given version.
	 * @return The text for the given Reference in the given Bible, or the empty
	 *         string if the Reference is not found.
	 */
	public String getText(String version, Reference reference);

	/**
	 * Returns a list of all references <i>r</i> such that <i>words</i> is
	 * contained in <i>r</i> for at least one version of the Bible. In other
	 * words, it finds the references for all verses in each version that
	 * contain <i>words</i> and combines the results together so that a single
	 * list if references is returned. The references are in the order they
	 * occur in the Bible and references are only listed once no matter how many
	 * versions had a match for that reference.
	 * 
	 * @param words
	 *            The words to search for.
	 * @return a ReferenceList containing the results, or an empty list.
	 */
	public ReferenceList getReferencesContaining(String words);

	/**
	 * Returns a ReferenceList for all of the references that contain the exact
	 * word 'word' (ignoring case) in any of the versions of the Bible that are
	 * currently stored in the model. For instance, a search for 'eaten' will
	 * not return a reference to a verse that contains 'beaten' (unless the
	 * verse also contains 'eaten' as a word).
	 * 
	 * @param word
	 *            a single word, assumed to have no spaces or other non-word
	 *            characters.
	 * @return A list of all of the references that contain the exact word
	 *         'word' in any of the versions of the Bible currently stored in
	 *         the model.
	 */
	public ReferenceList getReferencesContainingWord(String word);

	/**
	 * Returns a ReferenceList of all of the references for verses that contain
	 * all of the the exact words in the String 'words' (ignoring case) in any
	 * of the versions of the Bible that are currently stored in the model. By
	 * 'exact words' we mean it matches against whole words, not parts of words.
	 * For instance, if words="Son of God", the returned verses have all of the
	 * words "son", "of", and "god" in at least one version. Since it matches
	 * only whole words, it would <i>not</i> return verses that have "son",
	 * "of", and "godly" but not "god". This method removes extraneous
	 * characters (punctuation, etc.) before searching.
	 * 
	 * @param words
	 *            a list of words separated by spaces.
	 * @return A list of all of the references that contain all of the exact
	 *         words from 'words' in any of the versions of the Bible currently
	 *         stored in the model.
	 */
	public ReferenceList getReferencesContainingAllWords(String words);

	/**
	 * Returns a ReferenceList for all of the references for verses that contain
	 * all of the the exact words in the String 'words' (ignoring case) in any
	 * of the versions of the Bible that are currently stored in the model. By
	 * 'exact words' we mean it matches against whole words, not parts of words.
	 * In addition, any phrases that are in double quotes will be matched
	 * exactly. For instance, if words='"Son of God" was', the returned verses
	 * all have the exact phrase (ignoring case) "Son of God" and the word
	 * "was". Verses that have all of the words "son", "of", and "god" but not
	 * the exact phrase "son of god" will not be included. This method removes
	 * extraneous characters (punctuation, etc.) before searching. However, it
	 * uses punctuation (.,;) when matching phrases.
	 * 
	 * @param words
	 *            a list of words separated by spaces, with possibly phrases in
	 *            double quotes.
	 * @return A list of all of the references that contain all of the exact
	 *         words from 'words', including any exact phrases that are in
	 *         double quotes, in any of the versions of the Bible currently
	 *         stored in the model.
	 */
	public ReferenceList getReferencesContainingAllWordsAndPhrases(String words);

	/**
	 * Returns a list of the references for the given passage, in order. If not
	 * all version contain all of the references, return all of the references
	 * in the passage that are in any of the versions. In other words, combine
	 * the lists from all of the versions, keeping any reference that occurs in
	 * any of the versions, and only listing each reference once no matter how
	 * many versions it appears in.
	 * 
	 * @param reference
	 *            A string representation of the reference (e.g. "Genesis
	 *            1:2-3:4")
	 * @return A ReferenceList containing all of the verses for the given
	 *         passage, in order.
	 */
	public ReferenceList getReferencesForPassage(String reference);

	/**
	 * Returns a list containing the single reference "book chapter:verse" (e.g.
	 * John 3:16) if the reference occurs in any of the versions, or an empty
	 * list if it does not occur in any of the versions.
	 * 
	 * @param book
	 *            The book of the bible
	 * @param chapter
	 *            the chapter
	 * @param verse
	 *            the verse
	 * @return a list containing the single reference requested, or an empty
	 *         list if the reference does not appear in any of the versions.
	 */
	public ReferenceList getVerseReferences(BookOfBible book, int chapter, int verse);

	/**
	 * Returns a list containing all of the references in a particular book of
	 * bible, or an empty list if the book is invalid.
	 * 
	 * @param book
	 *            The book of the bible
	 * @return a ReferenceList containing all of the references in a particular
	 *         book of bible, or an empty list if the book is invalid.
	 */
	public ReferenceList getBookReferences(BookOfBible book);

	/**
	 * Returns a list containing all of the references in a particular chapter
	 * of the bible, or an empty list if the book or chapter is invalid.
	 * 
	 * @param book
	 *            The book of the bible
	 * @param chapter
	 *            The chapter of the book
	 * @return a ReferenceList containing all of the references in a particular
	 *         chapter of the bible, or an empty list if the book or chapter is
	 *         invalid.
	 */
	public ReferenceList getChapterReferences(BookOfBible book, int chapter);

	/**
	 * Returns a list containing all of the references in a particular passage
	 * of the bible, or an empty list if the passage does not appear in any of
	 * the versions.
	 * 
	 * @param startVerse
	 *            The first verse of the selection
	 * @param endVerse
	 *            The last verse of the selection
	 * @return a ReferenceList containing all of the references in a particular
	 *         passage of the bible, or an empty list if the passage does not
	 *         appear in any of the versions.
	 */
	public ReferenceList getPassageReferences(Reference startVerse, Reference endVerse);

	/**
	 * Returns a list containing all of the references between two chapters
	 * (inclusive of the second chapter), or an empty list if the passage does
	 * not occur in any of the versions.
	 * 
	 * @param book
	 *            The book of the bible
	 * @param chapter1
	 *            The starting chapter
	 * @param chapter2
	 *            The ending chapter
	 * @return a ReferenceList containing all of the references between two
	 *         chapters (inclusive of the second chapter), or an empty list if
	 *         the passage does not occur in any of the versions.
	 */
	public ReferenceList getChapterReferences(BookOfBible book, int chapter1, int chapter2);

	/**
	 * Returns a list containing the references for the passage "book
	 * chapter:verse-verse" (e.g. John 3:16-18) if the passage occurs in any of
	 * the versions, or an empty list if it does not occur in any of the
	 * versions. If not all versions contain every verse from the passage,
	 * return the references from this passage that occur in any of the
	 * versions.
	 * 
	 * @param book
	 *            The book of the bible
	 * @param chapter
	 *            the chapter
	 * @param verse1
	 *            the starting verse
	 * @param verse2
	 *            the ending verse
	 * @return a list containing the references for the given passage, or an
	 *         empty list if the reference does not appear in any of the
	 *         versions.
	 */
	public ReferenceList getPassageReferences(BookOfBible book, int chapter, int verse1, int verse2);

	/**
	 * Returns a list containing the references for the passage "book
	 * chapter:verse-chapter:verse" (e.g. 2 Kings 3:4-11:2) if the passage
	 * occurs in any of the versions, or an empty list if it does not occur in
	 * any of the versions. If not all versions contain every verse from the
	 * passage, return the references from this passage that occur in any of the
	 * versions.
	 * 
	 * @param book
	 *            The book of the bible
	 * @param chapter1
	 *            the chapter of the first verse
	 * @param verse1
	 *            the number of the first verse
	 * @param chapter2
	 *            the last chapter of the last verse
	 * @param verse2
	 *            the number of the last verse
	 * @return a list containing the references for the given passage, or an
	 *         empty list if the reference does not appear in any of the
	 *         versions.
	 */
	public ReferenceList getPassageReferences(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2);
}