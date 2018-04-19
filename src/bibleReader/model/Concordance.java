package bibleReader.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Concordance is a class which implements a concordance for a Bible. In other
 * words, it allows the easy lookup of all references which contain a given
 * word.
 * 
 * @author Chuck Cusack, March 2013 (Provided the interface)
 * @author Jonathan Chaffer & Jacob Lahr, April 2018 (Provided the
 *         implementation details)
 */
public class Concordance {
	private HashMap<String, TreeSet<Reference>> wordMap;

	/**
	 * Construct a concordance for the given Bible.
	 */
	public Concordance(Bible bible) {
		wordMap = new HashMap<String, TreeSet<Reference>>();
		for (Verse verse : bible.getAllVerses()) {
			ArrayList<String> wordsList = extractWords(verse.getText());
			TreeSet<String> wordsSet = new TreeSet<String>(wordsList);
			for (String word : wordsSet) {
				if (wordMap.containsKey(word)) {
					wordMap.get(word).add(verse.getReference());
				} else {
					ReferenceList rl = new ReferenceList();
					rl.add(verse.getReference());
					wordMap.put(word, new TreeSet<Reference>(rl));
				}
			}
		}
	}

	/**
	 * Removes HTML and other formatting from verses, removes "'s" at the end of
	 * words, and then extracts just the words and returns them as an ArrayList.
	 * 
	 * @param text
	 *            the text to extract words from.
	 */
	public static ArrayList<String> extractWords(String text) {
		text = text.toLowerCase();
		text = text.replaceAll("(<sup>[,\\w]*?</sup>|'s|’s|&#\\w*;|\\d+)", " ");
		text = text.replaceAll(",", "");
		String[] words = text.split("\\W+");
		ArrayList<String> toRet = new ArrayList<String>(Arrays.asList(words));
		toRet.remove("");
		return toRet;
	}

	/**
	 * Return the list of references to verses that contain the word 'word'
	 * (ignoring case) in the version of the Bible that this concordance was
	 * created with.
	 * 
	 * @param word
	 *            a single word (no spaces, etc.)
	 * @return the list of References of verses from this version that contain
	 *         the word, or an empty list if no verses contain the word.
	 */
	public ReferenceList getReferencesContaining(String word) {
		String lowerWord = word.toLowerCase();
		if (wordMap.get(lowerWord) != null) {
			ReferenceList refs = new ReferenceList(wordMap.get(lowerWord));
			if (refs != null) {
				return refs;
			}
		}
		return new ReferenceList();
	}

	/**
	 * Given an array of Strings, where each element of the array is expected to
	 * be a single word (with no spaces, etc., but ignoring case), return a
	 * ReferenceList containing all of the verses that contain <i>all of the
	 * words</i>.
	 * 
	 * @param words
	 *            A list of words.
	 * @return An ReferenceList containing references to all of the verses that
	 *         contain all of the given words, or an empty list if
	 */
	public ReferenceList getReferencesContainingAll(ArrayList<String> words) {
		if (!words.isEmpty()) {
			ArrayList<TreeSet<Reference>> allReferencesSets = new ArrayList<TreeSet<Reference>>();
			for (String word : words) {
				if (wordMap.get(word.toLowerCase()) != null) {
					allReferencesSets.add(wordMap.get(word.toLowerCase()));
				} else {
					allReferencesSets.add(new TreeSet<Reference>());
				}
			}
			ReferenceList refs = new ReferenceList();
			if (allReferencesSets.get(0) != null) {
				refs.addAll(allReferencesSets.get(0));
			}
			TreeSet<Reference> refsSet = new TreeSet<Reference>(refs);
			for (int i = 1; i < words.size(); i++) {
				if (allReferencesSets.get(i) != null) {
					refsSet.retainAll(allReferencesSets.get(i));
				} else {

				}
			}
			return new ReferenceList(refsSet);
		}
		return new ReferenceList();
	}

}
