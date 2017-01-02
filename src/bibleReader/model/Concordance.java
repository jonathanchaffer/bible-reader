package bibleReader.model;

import java.util.ArrayList;

/**
 * Concordance is a class which implements a concordance for a Bible. In other words, it allows the easy lookup of all
 * references which contain a given word.
 * 
 * @author Chuck Cusack, March 2013 (Provided the interface)
 * @author ?, March 2013 (Provided the implementation details)
 */
public class Concordance {
	// Add fields here.  (I actually only needed one field.)

	/**
	 * Construct a concordance for the given Bible.
	 */
	public Concordance(Bible bible) {
		// TODO: Implement me.
	}

	/**
	 * Return the list of references to verses that contain the word 'word' (ignoring case) in the version of the Bible
	 * that this concordance was created with.
	 * 
	 * @param word a single word (no spaces, etc.)
	 * @return the list of References of verses from this version that contain the word, or an empty list if no verses
	 *         contain the word.
	 */
	public ReferenceList getReferencesContaining(String word) {
		// TODO: Implement me.
		// This one should only be a few lines if you implement this class correctly.
		return null;
	}

	/**
	 * Given an array of Strings, where each element of the array is expected to be a single word (with no spaces, etc.,
	 * but ignoring case), return a ReferenceList containing all of the verses that contain <i>all of the words</i>.
	 * 
	 * @param words A list of words.
	 * @return An ReferenceList containing references to all of the verses that contain all of the given words, or an
	 *         empty list if
	 */
	public ReferenceList getReferencesContainingAll(ArrayList<String> words) {
		// TODO: Implement me.
		// This one is a little more complicated, but is similar in many ways to methods you have already implemented.
		return null;
	}
}
