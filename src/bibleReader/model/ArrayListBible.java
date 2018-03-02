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

	@Override
	public int getNumberOfVerses() {
		return verses.size();
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
		for (Verse verse : verses) {
			if (verse.getReference().equals(ref)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getVerseText(Reference ref) {
		for (Verse verse : verses) {
			if (verse.getReference().equals(ref)) {
				return verse.getText();
			}
		}
		return null;
	}

	@Override
	public Verse getVerse(Reference ref) {
		for (Verse verse : verses) {
			if (verse.getReference().equals(ref)) {
				return verse;
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
	public VerseList getAllVerses() {
		VerseList allVerses = new VerseList(getVersion(), getTitle());
		for (Verse verse : verses) {
			allVerses.add(verse);
		}
		return allVerses;
	}

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
		for (Verse verse : verses) {
			if (verse.getReference().getBookOfBible().equals(book)) {
				reachedBook = true;
				if (verse.getReference().getChapter() == chapter) {
					reachedChapter = true;
					num = verse.getReference().getVerse();
				}
			}
			if (reachedBook && reachedChapter && verse.getReference().getChapter() > chapter) {
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
		for (Verse verse : verses) {
			if (verse.getReference().getBookOfBible().equals(book)) {
				reachedBook = true;
				num = verse.getReference().getChapter();
			}
			if (reachedBook && !verse.getReference().getBook().equals(book)) {
				return num;
			}
		}
		return num;
	}

	@Override
	public ReferenceList getReferencesInclusive(Reference firstVerse, Reference lastVerse) {
		ReferenceList refs = getReferencesExclusive(firstVerse, lastVerse);
		if (!isValid(lastVerse)) {
			return refs;
		}
		refs.add(lastVerse);
		return refs;
	}

	@Override
	public ReferenceList getReferencesExclusive(Reference firstVerse, Reference lastVerse) {
		ReferenceList refs = new ReferenceList();
		if (firstVerse.compareTo(lastVerse) > 0) {
			return refs;
		}
		
		if (!isValid(firstVerse)) {
			return refs;
		}
		
		boolean reachedFirstVerse = false;
		boolean reachedLastVerse = false;
		
		int firstVerseIndex = -1;
		for (int i = 0; reachedFirstVerse == false; i++) {
			Verse verse = verses.get(i);
			if (verse.getReference().equals(firstVerse)) {
				reachedFirstVerse = true;
				firstVerseIndex = i;
			}
		}
		for (int i = firstVerseIndex; reachedLastVerse == false; i++) {
			Verse verse = verses.get(i);
			if (verse.getReference().compareTo(lastVerse) < 0) {
				refs.add(verse.getReference());
			} else {
				reachedLastVerse = true;
			}
		}
		return refs;
	}

	@Override
	public ReferenceList getReferencesForBook(BookOfBible book) {
		ReferenceList refs = new ReferenceList();
		if (book == null) {
			return refs;
		}
		
		for (Verse verse : verses) {
			if (verse.getReference().getBookOfBible().equals(book)) {
				while (verse.getReference().getBookOfBible().equals(book)) {
					refs.add(verse.getReference());
				}
				return refs;
			}
		}
		return refs;
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
