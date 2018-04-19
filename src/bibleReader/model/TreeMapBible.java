package bibleReader.model;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A class that stores a version of the Bible.
 * 
 * @author Chuck Cusack (Provided the interface)
 * @author Jonathan Chaffer (provided the implementation)
 */
public class TreeMapBible implements Bible {

	// The Fields
	private String version;
	private String title;
	private TreeMap<Reference, String> verses;

	/**
	 * Create a new Bible with the given verses.
	 * 
	 * @param verses
	 *            All of the verses of this version of the Bible.
	 */
	public TreeMapBible(VerseList verses) {
		this.verses = new TreeMap<Reference, String>();
		for (Verse verse : verses) {
			this.verses.put(verse.getReference(), verse.getText());
		}
		this.version = verses.getVersion();
		this.title = verses.getDescription();
	}

	@Override
	public int getNumberOfVerses() {
		return verses.size();
	}

	@Override
	public VerseList getAllVerses() {
		VerseList allVerses = new VerseList(getVersion(), getTitle());
		Set<Map.Entry<Reference, String>> entries = verses.entrySet();
		for (Map.Entry<Reference, String> entry : entries) {
			allVerses.add(new Verse(entry.getKey(), entry.getValue()));
		}
		return allVerses;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public boolean isValid(Reference ref) {
		Set<Map.Entry<Reference, String>> entries = verses.entrySet();
		for (Map.Entry<Reference, String> entry : entries) {
			if (entry.getKey().equals(ref)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getVerseText(Reference ref) {
		if (getVerse(ref) != null) {
			return getVerse(ref).getText();
		}
		return null;
	}

	@Override
	public Verse getVerse(Reference ref) {
		Set<Map.Entry<Reference, String>> entries = verses.entrySet();
		for (Map.Entry<Reference, String> entry : entries) {
			if (entry.getKey().equals(ref)) {
				return new Verse(entry.getKey(), entry.getValue());
			}
		}
		return null;
	}

	@Override
	public Verse getVerse(BookOfBible book, int chapter, int verse) {
		Reference ref = new Reference(book, chapter, verse);
		return getVerse(ref);
	}

	@Override
	public VerseList getVersesContaining(String phrase) {
		VerseList versesToReturn = new VerseList(getVersion(), phrase);
		phrase = phrase.toLowerCase();
		if (!phrase.equals("")) {
			Set<Map.Entry<Reference, String>> entries = verses.entrySet();
			for (Map.Entry<Reference, String> entry : entries) {
				if (entry.getValue().toLowerCase().contains(phrase)) {
					versesToReturn.add(new Verse(entry.getKey(), entry.getValue()));
				}
			}
		}
		return versesToReturn;
	}

	@Override
	public ReferenceList getReferencesContaining(String phrase) {
		ReferenceList refsToReturn = new ReferenceList();
		phrase = phrase.toLowerCase();
		if (!phrase.equals("")) {
			Set<Map.Entry<Reference, String>> entries = verses.entrySet();
			for (Map.Entry<Reference, String> entry : entries) {
				if (entry.getValue().toLowerCase().contains(phrase)) {
					refsToReturn.add(entry.getKey());
				}
			}
		}
		return refsToReturn;
	}

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
		int num = -1;
		if (book == null) {
			return num;
		}
		boolean reachedBook = false;
		boolean reachedChapter = false;
		Set<Map.Entry<Reference, String>> entries = verses.entrySet();
		for (Map.Entry<Reference, String> entry : entries) {
			if (entry.getKey().getBookOfBible().equals(book)) {
				reachedBook = true;
				if (entry.getKey().getChapter() == chapter) {
					reachedChapter = true;
				}
			}
			if (reachedBook && reachedChapter && entry.getKey().getChapter() == chapter
					&& entry.getKey().getBookOfBible().equals(book)) {
				num = entry.getKey().getVerse();
			} else if (reachedBook && reachedChapter) {
				return num;
			}
		}
		return num;
	}

	@Override
	public int getLastChapterNumber(BookOfBible book) {
		int num = -1;
		if (book == null) {
			return num;
		}
		boolean reachedBook = false;
		Set<Map.Entry<Reference, String>> entries = verses.entrySet();
		for (Map.Entry<Reference, String> entry : entries) {
			if (entry.getKey().getBookOfBible().equals(book)) {
				reachedBook = true;
			}
			if (reachedBook && entry.getKey().getBookOfBible().equals(book)) {
				num = entry.getKey().getChapter();
			} else if (reachedBook) {
				return num;
			}
		}
		return num;
	}

	@Override
	public ReferenceList getReferencesInclusive(Reference firstVerse, Reference lastVerse) {
		ReferenceList results = new ReferenceList();
		if (firstVerse.compareTo(lastVerse) > 0) {
			return results;
		}
		SortedMap<Reference, String> s = verses.subMap(firstVerse, lastVerse);
		Set<Map.Entry<Reference, String>> entries = s.entrySet();
		for (Map.Entry<Reference, String> entry : entries) {
			results.add(entry.getKey());
		}
		if (isValid(lastVerse)) {
			results.add(lastVerse);
		}
		return results;
	}

	@Override
	public ReferenceList getReferencesExclusive(Reference firstVerse, Reference lastVerse) {
		ReferenceList results = new ReferenceList();
		if (firstVerse.compareTo(lastVerse) > 0) {
			return results;
		}
		SortedMap<Reference, String> s = verses.subMap(firstVerse, lastVerse);
		Set<Map.Entry<Reference, String>> entries = s.entrySet();
		for (Map.Entry<Reference, String> entry : entries) {
			results.add(entry.getKey());
		}
		return results;
	}

	@Override
	public ReferenceList getReferencesForBook(BookOfBible book) {
		ReferenceList refs = new ReferenceList();
		if (book == null) {
			return refs;
		}
		int lastChapter = getLastChapterNumber(book);
		refs.addAll(getReferencesInclusive(new Reference(book, 1, 1),
				new Reference(book, lastChapter, getLastVerseNumber(book, lastChapter))));
		return refs;
	}

	@Override
	public ReferenceList getReferencesForChapter(BookOfBible book, int chapter) {
		ReferenceList refs = new ReferenceList();
		if (book == null) {
			return refs;
		}
		refs.addAll(getReferencesInclusive(new Reference(book, chapter, 1),
				new Reference(book, chapter, getLastVerseNumber(book, chapter))));
		return refs;
	}

	@Override
	public ReferenceList getReferencesForChapters(BookOfBible book, int chapter1, int chapter2) {
		ReferenceList refs = new ReferenceList();
		if (book == null) {
			return refs;
		}
		refs.addAll(getReferencesInclusive(new Reference(book, chapter1, 1),
				new Reference(book, chapter2, getLastVerseNumber(book, chapter2))));
		return refs;
	}

	@Override
	public ReferenceList getReferencesForPassage(BookOfBible book, int chapter, int verse1, int verse2) {
		ReferenceList refs = new ReferenceList();
		if (book == null) {
			return refs;
		}
		refs.addAll(getReferencesInclusive(new Reference(book, chapter, verse1), new Reference(book, chapter, verse2)));
		return refs;
	}

	@Override
	public ReferenceList getReferencesForPassage(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2) {
		ReferenceList refs = new ReferenceList();
		if (book == null) {
			return refs;
		}
		refs.addAll(
				getReferencesInclusive(new Reference(book, chapter1, verse1), new Reference(book, chapter2, verse2)));
		return refs;
	}

	@Override
	public VerseList getVersesInclusive(Reference firstVerse, Reference lastVerse) {
		VerseList versesToReturn = new VerseList(getVersion(), firstVerse + "-" + lastVerse);
		if (firstVerse.compareTo(lastVerse) > 0) {
			return versesToReturn;
		}
		SortedMap<Reference, String> s = verses.subMap(firstVerse, lastVerse);
		Set<Map.Entry<Reference, String>> entries = s.entrySet();
		for (Map.Entry<Reference, String> entry : entries) {
			versesToReturn.add(new Verse(entry.getKey(), entry.getValue()));
		}
		if (isValid(lastVerse)) {
			versesToReturn.add(new Verse(lastVerse, getVerseText(lastVerse)));
		}
		return versesToReturn;
	}

	@Override
	public VerseList getVersesExclusive(Reference firstVerse, Reference lastVerse) {
		VerseList versesToReturn = new VerseList(getVersion(), firstVerse + "-" + lastVerse);
		if (firstVerse.compareTo(lastVerse) > 0) {
			return versesToReturn;
		}
		SortedMap<Reference, String> s = verses.subMap(firstVerse, lastVerse);
		Set<Map.Entry<Reference, String>> entries = s.entrySet();
		for (Map.Entry<Reference, String> entry : entries) {
			versesToReturn.add(new Verse(entry.getKey(), entry.getValue()));
		}
		return versesToReturn;
	}

	@Override
	public VerseList getBook(BookOfBible book) {
		if (book != null) {
			VerseList versesToReturn = new VerseList(this.getVersion(), book.toString());
			int lastChapter = getLastChapterNumber(book);
			versesToReturn.addAll(getVersesInclusive(new Reference(book, 1, 1),
					new Reference(book, lastChapter, getLastVerseNumber(book, lastChapter))));
			return versesToReturn;
		}
		return new VerseList(this.getVersion(), "");
	}

	@Override
	public VerseList getChapter(BookOfBible book, int chapter) {
		if (book != null) {
			VerseList versesToReturn = new VerseList(this.getVersion(), book.toString());
			versesToReturn.addAll(getVersesInclusive(new Reference(book, chapter, 1),
					new Reference(book, chapter, getLastVerseNumber(book, chapter))));
			return versesToReturn;
		}
		return new VerseList(this.getVersion(), "");
	}

	@Override
	public VerseList getChapters(BookOfBible book, int chapter1, int chapter2) {
		if (book != null) {
			VerseList versesToReturn = new VerseList(this.getVersion(), book.toString());
			versesToReturn.addAll(getVersesInclusive(new Reference(book, chapter1, 1),
					new Reference(book, chapter2, getLastVerseNumber(book, chapter2))));
			return versesToReturn;
		}
		return new VerseList(this.getVersion(), "");
	}

	@Override
	public VerseList getPassage(BookOfBible book, int chapter, int verse1, int verse2) {
		if (book != null) {
			VerseList versesToReturn = new VerseList(this.getVersion(), book.toString());
			versesToReturn.addAll(
					getVersesInclusive(new Reference(book, chapter, verse1), new Reference(book, chapter, verse2)));
			return versesToReturn;
		}
		return new VerseList(this.getVersion(), "");
	}

	@Override
	public VerseList getPassage(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2) {
		if (book != null) {
			VerseList versesToReturn = new VerseList(this.getVersion(), book.toString());
			versesToReturn.addAll(
					getVersesInclusive(new Reference(book, chapter1, verse1), new Reference(book, chapter2, verse2)));
			return versesToReturn;
		}
		return new VerseList(this.getVersion(), "");
	}
}
