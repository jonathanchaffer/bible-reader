package bibleReader.model;


/**
 * The model of the Bible Reader. It stores the Bibles and has methods for searching for verses based on words or
 * references.
 * 
 * @author cusack
 */
public class BibleReaderModel implements MultiBibleModel {

	// ---------------------------------------------------------------------------
	// TODO Add more fields here: Stage 5
	// You need to store several Bible objects.
	// You may need to store other data as well.

	/**
	 * Default constructor. You probably need to instantiate objects and do other assorted things to set up the model.
	 */
	public BibleReaderModel() {
		// TODO implement me: Stage 5
	}

	@Override
	public String[] getVersions() {
		// TODO implement me: Stage 5
		return null;
	}

	@Override
	public int getNumberOfVersions() {
		// TODO Implement me: Stage 5
		return 0;
	}

	@Override
	public void addBible(Bible bible) {
		// TODO implement me: Stage 5
	}

	@Override
	public Bible getBible(String version) {
		// TODO implement me: Stage 5
		return null;
	}

	@Override
	public ReferenceList getReferencesContaining(String words) {
		// TODO Implement me: Stage 5
		return null;
	}
	
	@Override
	public VerseList getVerses(String version, ReferenceList references) {
		// TODO Implement me: Stage 5
		return null;
	}
	//---------------------------------------------------------------------

	@Override
	public String getText(String version, Reference reference) {
		// TODO Implement me: Stage 7
		return null;
	}

	@Override
	public ReferenceList getReferencesForPassage(String reference) {
		// TODO Implement me: Stage 7
		return null;
	}

	// -----------------------------------------------------------------------------
	// The next set of methods are for use by the getReferencesForPassage method above. 
	// After it parses the input string it will call one of these.
	//
	// These methods should be somewhat easy to implement. They are kind of delegate
	// methods in that they call a method on the Bible class to do most of the work.
	// However, they need to do so for every version of the Bible stored in the model.
	// and combine the results.
	//
	// Once you implement one of these, the rest of them should be fairly straightforward.
	// Think before you code, get one to work, and then implement the rest based on
	// that one.
	// -----------------------------------------------------------------------------

	@Override
	public ReferenceList getVerseReferences(BookOfBible book, int chapter, int verse) {
		// TODO Implement me: Stage 7
		return null;
	}

	@Override
	public ReferenceList getPassageReferences(Reference startVerse, Reference endVerse) {
		// TODO Implement me: Stage 7
		return null;
	}

	@Override
	public ReferenceList getBookReferences(BookOfBible book) {
		// TODO Implement me: Stage 7
		return null;
	}

	@Override
	public ReferenceList getChapterReferences(BookOfBible book, int chapter) {
		// TODO Implement me: Stage 7
		return null;
	}

	@Override
	public ReferenceList getChapterReferences(BookOfBible book, int chapter1, int chapter2) {
		// TODO Implement me: Stage 7
		return null;
	}

	@Override
	public ReferenceList getPassageReferences(BookOfBible book, int chapter, int verse1, int verse2) {
		// TODO Implement me: Stage 7
		return null;
	}

	@Override
	public ReferenceList getPassageReferences(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2) {
		// TODO Implement me: Stage 7
		return null;
	}

	//------------------------------------------------------------------
	// These are the better searching methods.
	// 
	@Override
	public ReferenceList getReferencesContainingWord(String word) {
		// TODO Implement me: Stage 12
		return null;
	}

	@Override
	public ReferenceList getReferencesContainingAllWords(String words) {
		// TODO Implement me: Stage 12
		return null;
	}

	@Override
	public ReferenceList getReferencesContainingAllWordsAndPhrases(String words) {
		// TODO Implement me: Stage 12
		return null;
	}
}
