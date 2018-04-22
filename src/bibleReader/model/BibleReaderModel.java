package bibleReader.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The model of the Bible Reader. It stores the Bibles and has methods for
 * searching for verses based on words or references.
 * 
 * @author cusack
 * @author Jonathan Chaffer & Jacob Lahr (2018)
 */
public class BibleReaderModel implements MultiBibleModel {
	private HashMap<Bible, Concordance> bibles;

	// regex code for a number with/without spaces on either side
	public static final String number = "\\s*(\\d+)\\s*";
	// pattern for the book
	public static Pattern bookPattern = Pattern.compile("((?:1|2|3|I|II|III)\\s*\\w+|(?:\\s*[a-zA-Z]+)+)\\s*(.*)");
	// pattern for one chapter and one verse, e.g. "3:16"
	public static Pattern oneChapterOneVersePattern = Pattern.compile(number + ":" + number);
	// pattern for one chapter and multiple verses, e.g. "3:16-17"
	public static Pattern oneChapterMultipleVersesPattern = Pattern.compile(number + ":" + number + "-" + number);
	// pattern for one chapter, e.g. "1"
	public static Pattern oneChapterPattern = Pattern.compile(number);
	// pattern for multiple chapters, e.g. "1-3"
	public static Pattern multipleChaptersPattern = Pattern.compile(number + "(?:-)" + number);
	// pattern for multiple chapters and multiple verses, e.g. "1:5-3:4"
	public static Pattern multipleChaptersMultipleVersesPattern = Pattern
			.compile(number + ":" + number + "-" + number + ":" + number);
	// pattern for weird syntax, e.g. "2-3:16"
	public static Pattern strangeSyntaxPattern = Pattern.compile(number + "-" + number + ":" + number);

	/**
	 * Default constructor. You probably need to instantiate objects and do
	 * other assorted things to set up the model.
	 */
	public BibleReaderModel() {
		bibles = new HashMap<Bible, Concordance>();
	}

	@Override
	public String[] getVersions() {
		String[] versionsToReturn = new String[bibles.size()];
		for (int i = 0; i < bibles.size(); i++) {
			versionsToReturn[i] = bibles.keySet().toArray(new Bible[bibles.size()])[i].getVersion();
		}
		Arrays.sort(versionsToReturn);
		return versionsToReturn;
	}

	@Override
	public int getNumberOfVersions() {
		return getVersions().length;
	}

	@Override
	public void addBible(Bible bible) {
		bibles.put(bible, BibleFactory.createConcordance(bible));
	}

	@Override
	public Bible getBible(String version) {
		for (Bible bible : bibles.keySet()) {
			if (bible.getVersion().equals(version)) {
				return bible;
			}
		}
		return null;
	}

	@Override
	public ReferenceList getReferencesContaining(String words) {
		TreeSet<Reference> refsToReturn = new TreeSet<Reference>();
		for (Bible bible : bibles.keySet()) {
			for (Reference ref : bible.getReferencesContaining(words)) {
				refsToReturn.add(ref);
			}
		}
		return new ReferenceList(refsToReturn);
	}

	@Override
	public VerseList getVerses(String version, ReferenceList references) {
		for (Bible bible : bibles.keySet()) {
			if (bible.getVersion().equals(version)) {
				return bible.getVerses(references);
			}
		}
		return null;
	}

	@Override
	public String getText(String version, Reference reference) {
		for (Bible bible : bibles.keySet()) {
			if (bible.getVersion().equals(version)) {
				if (bible.getVerse(reference) != null) {
					return bible.getVerse(reference).getText();
				} else {
					return "";
				}
			}
		}
		return "";
	}

	@Override
	public ReferenceList getReferencesForPassage(String reference) {
		TreeSet<Reference> refsToReturn = new TreeSet<Reference>();

		BookOfBible book;
		int chapter1;
		int chapter2;
		int verse1;
		int verse2;
		String other = "";

		Matcher m = bookPattern.matcher(reference);

		if (m.matches()) {
			// It matches. Good.
			book = BookOfBible.getBookOfBible(m.group(1));
			if (book == null) {
				return new ReferenceList(refsToReturn);
			}
			other = m.group(2);
			// Now we need to parse other to see what format it is.
			try {
				if (other.length() == 0) {
					refsToReturn.addAll(getBookReferences(book));
				} else if ((m = oneChapterOneVersePattern.matcher(other)).matches()) {
					chapter1 = Integer.parseInt(m.group(1));
					verse1 = Integer.parseInt(m.group(2));
					refsToReturn.addAll(getVerseReferences(book, chapter1, verse1));
				} else if ((m = oneChapterMultipleVersesPattern.matcher(other)).matches()) {
					chapter1 = Integer.parseInt(m.group(1));
					verse1 = Integer.parseInt(m.group(2));
					verse2 = Integer.parseInt(m.group(3));
					refsToReturn.addAll(getPassageReferences(book, chapter1, verse1, verse2));
				} else if ((m = oneChapterPattern.matcher(other)).matches()) {
					chapter1 = Integer.parseInt(m.group(1));
					refsToReturn.addAll(getChapterReferences(book, chapter1));
				} else if ((m = multipleChaptersPattern.matcher(other)).matches()) {
					chapter1 = Integer.parseInt(m.group(1));
					chapter2 = Integer.parseInt(m.group(2));
					refsToReturn.addAll(getChapterReferences(book, chapter1, chapter2));
				} else if ((m = multipleChaptersMultipleVersesPattern.matcher(other)).matches()) {
					chapter1 = Integer.parseInt(m.group(1));
					verse1 = Integer.parseInt(m.group(2));
					chapter2 = Integer.parseInt(m.group(3));
					verse2 = Integer.parseInt(m.group(4));
					refsToReturn.addAll(getPassageReferences(book, chapter1, verse1, chapter2, verse2));
				} else if ((m = strangeSyntaxPattern.matcher(other)).matches()) {
					chapter1 = Integer.parseInt(m.group(1));
					verse1 = 1;
					chapter2 = Integer.parseInt(m.group(2));
					verse2 = Integer.parseInt(m.group(3));
					refsToReturn.addAll(getPassageReferences(book, chapter1, verse1, chapter2, verse2));
				} else {
				}
			} catch (NumberFormatException e) {
				return new ReferenceList(refsToReturn);
			}
		}
		return new ReferenceList(refsToReturn);
	}

	@Override
	public ReferenceList getVerseReferences(BookOfBible book, int chapter, int verse) {
		Reference ref = new Reference(book, chapter, verse);
		TreeSet<Reference> refs = new TreeSet<Reference>();
		for (Bible bible : bibles.keySet()) {
			Verse v = bible.getVerse(ref);
			if (v != null) {
				refs.add(v.getReference());
			}
		}
		return new ReferenceList(refs);
	}

	@Override
	public ReferenceList getPassageReferences(Reference startVerse, Reference endVerse) {
		TreeSet<Reference> refs = new TreeSet<Reference>();
		for (Bible bible : bibles.keySet()) {
			refs.addAll(bible.getReferencesInclusive(startVerse, endVerse));
		}
		return new ReferenceList(refs);
	}

	@Override
	public ReferenceList getBookReferences(BookOfBible book) {
		TreeSet<Reference> refs = new TreeSet<Reference>();
		for (Bible bible : bibles.keySet()) {
			refs.addAll(bible.getReferencesForBook(book));
		}
		return new ReferenceList(refs);
	}

	@Override
	public ReferenceList getChapterReferences(BookOfBible book, int chapter) {
		TreeSet<Reference> refs = new TreeSet<Reference>();
		for (Bible bible : bibles.keySet()) {
			refs.addAll(bible.getReferencesForChapter(book, chapter));
		}
		return new ReferenceList(refs);
	}

	@Override
	public ReferenceList getChapterReferences(BookOfBible book, int chapter1, int chapter2) {
		TreeSet<Reference> refs = new TreeSet<Reference>();
		for (Bible bible : bibles.keySet()) {
			refs.addAll(bible.getReferencesForChapters(book, chapter1, chapter2));
		}
		return new ReferenceList(refs);
	}

	@Override
	public ReferenceList getPassageReferences(BookOfBible book, int chapter, int verse1, int verse2) {
		TreeSet<Reference> refs = new TreeSet<Reference>();
		for (Bible bible : bibles.keySet()) {
			refs.addAll(bible.getReferencesForPassage(book, chapter, verse1, verse2));
		}
		return new ReferenceList(refs);
	}

	@Override
	public ReferenceList getPassageReferences(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2) {
		TreeSet<Reference> refs = new TreeSet<Reference>();
		for (Bible bible : bibles.keySet()) {
			refs.addAll(bible.getReferencesForPassage(book, chapter1, verse1, chapter2, verse2));
		}
		return new ReferenceList(refs);
	}

	@Override
	public ReferenceList getReferencesContainingWord(String word) {
		TreeSet<Reference> refsSet = new TreeSet<Reference>();
		for (Bible bible : bibles.keySet()) {
			refsSet.addAll(bibles.get(bible).getReferencesContaining(word));
		}
		return new ReferenceList(refsSet);
	}

	@Override
	public ReferenceList getReferencesContainingAllWords(String words) {
		ArrayList<String> wordsList = Concordance.extractWords(words);
		TreeSet<Reference> refsSet = new TreeSet<Reference>();
		for (Bible bible : bibles.keySet()) {
			refsSet.addAll(bibles.get(bible).getReferencesContainingAll(wordsList));
		}
		return new ReferenceList(refsSet);
	}

	@Override
	public ReferenceList getReferencesContainingAllWordsAndPhrases(String words) {
		String input = words;
		boolean thereArePhrases = false;

		TreeSet<Reference> refsForPhrases = new TreeSet<Reference>();

		ArrayList<String> phrases = new ArrayList<String>();
		Pattern quotePattern = Pattern.compile("\"([^\"]*)\"");
		Matcher matcher = quotePattern.matcher(input);
		if (input.matches("(.*)(\"([^\"]*)\")+(.*)")) {
			thereArePhrases = true;
			while (matcher.find()) {
				phrases.add(matcher.group(1).toLowerCase());
			}
		}

		ReferenceList refsForWords = getReferencesContainingAllWords(input);
		if (!thereArePhrases) {
			return refsForWords;
		}

		for (Bible bible : bibles.keySet()) {
			for (Reference ref : refsForWords) {
				int phrasesInTheVerse = 0;
				for (String phrase : phrases) {
					if (bible.getVerseText(ref) != null) {
						if (bible.getVerseText(ref).matches("(?i).*(?<!\\w)" + phrase + "(?!\\w).*")) {
							phrasesInTheVerse++;
						}
					}
				}
				if (phrasesInTheVerse == phrases.size()) {
					refsForPhrases.add(ref);
				}
			}
		}

		input = input.replaceAll("\"([^\"]*)\"", "");

		ArrayList<String> otherWords = Concordance.extractWords(input);
		refsForWords.retainAll(refsForPhrases);
		return new ReferenceList(refsForWords);
	}
}
