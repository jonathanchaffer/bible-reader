package bibleReader.model;

/**
 * A class that allows us to choose one of the Bible implementations as the
 * default one.
 * 
 * @author cusack
 *
 */
public class BibleFactory {

	public static Bible createBible(VerseList verses) {
		return new ArrayListBible(verses);
		// return new TreeMapBible(verses);
	}

	public static Concordance createConcordance(Bible bible) {
		throw new UnsupportedOperationException("Method not implemented");
	}
}
