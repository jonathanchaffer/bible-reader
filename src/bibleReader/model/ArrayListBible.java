package bibleReader.model;

import java.util.ArrayList;

/**
 * A class that stores a version of the Bible.
 * 
 * @author Chuck Cusack (Provided the interface). Modified February 9, 2015.
 * @author Jonathan Chaffer & Jacob Lahr (provided the implementation), 2018.
 */
public class ArrayListBible implements Bible {

	// The Fields
	private String version;
	private String title;
	private ArrayList<Verse> verses;

	/**
	 * Create a new Bible with the given verses.
	 * 
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
				}
			}
			if (reachedBook && reachedChapter && verse.getReference().getChapter() == chapter
					&& verse.getReference().getBookOfBible().equals(book)) {
				num = verse.getReference().getVerse();
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
		for (Verse verse : verses) {
			if (verse.getReference().getBookOfBible().equals(book)) {
				reachedBook = true;
			}
			if (reachedBook && verse.getReference().getBookOfBible().equals(book)) {
				num = verse.getReference().getChapter();
			} else if (reachedBook) {
				return num;
			}
		}
		return num;
	}

	@Override
	public ReferenceList getReferencesInclusive(Reference firstVerse, Reference lastVerse) {
		ReferenceList refs = new ReferenceList();
		if (firstVerse.compareTo(lastVerse) > 0) {
			return refs;
		}
		if (!isValid(firstVerse)) {
			return refs;
		}
		if (!isValid(lastVerse)) {
			return refs;
		}
		refs = getReferencesExclusive(firstVerse, lastVerse);
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
		boolean reachedBook = false;
		for (Verse verse : verses) {
			if (verse.getReference().getBookOfBible().equals(book)) {
				reachedBook = true;
			}
			if (reachedBook && verse.getReference().getBookOfBible().equals(book)) {
				refs.add(verse.getReference());
			} else if (reachedBook) {
				return refs;
			}
		}
		return refs;
	}

	@Override
	public ReferenceList getReferencesForChapter(BookOfBible book, int chapter) {
		ReferenceList refs = new ReferenceList();
		if (book == null) {
			return refs;
		}
		boolean reachedBook = false;
		boolean reachedChapter = false;
		for (Verse verse : verses) {
			if (verse.getReference().getBookOfBible().equals(book)) {
				reachedBook = true;
				if (verse.getReference().getChapter() == chapter) {
					reachedChapter = true;
				}
			}
			if (reachedBook && reachedChapter && verse.getReference().getChapter() == chapter
					&& verse.getReference().getBookOfBible().equals(book)) {
				refs.add(verse.getReference());
			} else if (reachedBook && reachedChapter) {
				return refs;
			}
		}
		return refs;
	}

	@Override
	public ReferenceList getReferencesForChapters(BookOfBible book, int chapter1, int chapter2) {
		ReferenceList refs = new ReferenceList();
		if (book == null) {
			return refs;
		}
		boolean reachedBook = false;
		boolean reachedFirstChapter = false;
		for (Verse verse : verses) {
			if (verse.getReference().getBookOfBible().equals(book)) {
				reachedBook = true;
				if (verse.getReference().getChapter() == chapter1) {
					reachedFirstChapter = true;
				}
			}
			if (reachedBook && reachedFirstChapter && verse.getReference().getChapter() <= chapter2
					&& verse.getReference().getBookOfBible().equals(book)) {
				refs.add(verse.getReference());
			} else if (reachedBook && reachedFirstChapter) {
				return refs;
			}
		}
		return refs;
	}

	@Override
	public ReferenceList getReferencesForPassage(BookOfBible book, int chapter, int verse1, int verse2) {
		ReferenceList refs = new ReferenceList();
		if (book == null) {
			return refs;
		}
		boolean reachedBook = false;
		boolean reachedChapter = false;
		boolean reachedFirstVerse = false;
		for (Verse verse : verses) {
			if (verse.getReference().getBookOfBible().equals(book)) {
				reachedBook = true;
				if (verse.getReference().getChapter() == chapter) {
					reachedChapter = true;
					if (verse.getReference().getVerse() == verse1) {
						reachedFirstVerse = true;
					}
				}
			}
			if (reachedBook && reachedChapter && reachedFirstVerse && verse.getReference().getVerse() <= verse2
					&& verse.getReference().getBookOfBible().equals(book)
					&& verse.getReference().getChapter() == chapter) {
				refs.add(verse.getReference());
			} else if (reachedBook && reachedChapter && reachedFirstVerse) {
				return refs;
			}
		}
		return refs;
	}

	@Override
	public ReferenceList getReferencesForPassage(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2) {
		ReferenceList refs = new ReferenceList();
		if (book == null) {
			return refs;
		}
		boolean reachedBook = false;
		boolean reachedFirstChapter = false;
		boolean reachedFirstVerse = false;
		for (Verse verse : verses) {
			if (verse.getReference().getBookOfBible().equals(book)) {
				reachedBook = true;
				if (verse.getReference().getChapter() == chapter1) {
					reachedFirstChapter = true;
					if (verse.getReference().getVerse() == verse1) {
						reachedFirstVerse = true;
					}
				}
			}
			if (reachedBook && reachedFirstChapter && reachedFirstVerse
					&& verse.getReference().getBookOfBible().equals(book)
					&& verse.getReference().getChapter() <= chapter2) {
				if (verse.getReference().getChapter() != chapter2) {
					refs.add(verse.getReference());
				} else if (verse.getReference().getVerse() <= verse2) {
					refs.add(verse.getReference());
				}
			} else if (reachedBook && reachedFirstChapter && reachedFirstVerse) {
				return refs;
			}
		}
		return refs;
	}

	@Override
	public VerseList getVersesInclusive(Reference firstVerse, Reference lastVerse) {
		// TODO could this reuse some of getVersesExclusive??
		VerseList versesToReturn = new VerseList(this.getVersion(),
				firstVerse.toString() + " to " + lastVerse.toString());
		if (firstVerse.compareTo(lastVerse) > 0) {
			return versesToReturn;
		}
		if (!isValid(firstVerse)) {
			return versesToReturn;
		}
		if (!isValid(lastVerse)) {
			return versesToReturn;
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
				versesToReturn.add(verse);
			} else {
				versesToReturn.add(verse);
				reachedLastVerse = true;
			}
		}
		return versesToReturn;
	}

	@Override
	public VerseList getVersesExclusive(Reference firstVerse, Reference lastVerse) {
		VerseList versesToReturn = new VerseList(this.getVersion(),
				firstVerse.toString() + " to " + lastVerse.toString() + " excluding the final one");
		if (firstVerse.compareTo(lastVerse) > 0) {
			return versesToReturn;
		}
		if (!isValid(firstVerse)) {
			return versesToReturn;
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
				versesToReturn.add(verse);
			} else {
				reachedLastVerse = true;
			}
		}
		return versesToReturn;
	}

	@Override
	public VerseList getBook(BookOfBible book) {
		if (book != null) {
			VerseList versesToReturn = new VerseList(this.getVersion(), book.toString());
			int lastChapter = getLastChapterNumber(book);
			for (Verse verse : getVersesInclusive(new Reference(book, 1, 1),
					new Reference(book, lastChapter, getLastVerseNumber(book, lastChapter)))) {
				versesToReturn.add(verse);
			}
			return versesToReturn;
		}
		return new VerseList(this.getVersion(), "");
	}

	@Override
	public VerseList getChapter(BookOfBible book, int chapter) {
		if (book != null) {
			VerseList versesToReturn = new VerseList(this.getVersion(), book.toString());
			for (Verse verse : getVersesInclusive(new Reference(book, chapter, 1),
					new Reference(book, chapter, getLastVerseNumber(book, chapter)))) {
				versesToReturn.add(verse);
			}
			return versesToReturn;
		}
		return new VerseList(this.getVersion(), "");
	}

	@Override
	public VerseList getChapters(BookOfBible book, int chapter1, int chapter2) {
		if (book != null) {
			VerseList versesToReturn = new VerseList(this.getVersion(), book.toString());
			for (Verse verse : getVersesInclusive(new Reference(book, chapter1, 1),
					new Reference(book, chapter2, getLastVerseNumber(book, chapter2)))) {
				versesToReturn.add(verse);
			}
			return versesToReturn;
		}
		return new VerseList(this.getVersion(), "");
	}

	@Override
	public VerseList getPassage(BookOfBible book, int chapter, int verse1, int verse2) {
		if (book != null) {
			VerseList versesToReturn = new VerseList(this.getVersion(), book.toString());
			for (Verse verse : getVersesInclusive(new Reference(book, chapter, verse1),
					new Reference(book, chapter, verse2))) {
				versesToReturn.add(verse);
			}
			return versesToReturn;
		}
		return new VerseList(this.getVersion(), "");
	}

	@Override
	public VerseList getPassage(BookOfBible book, int chapter1, int verse1, int chapter2, int verse2) {
		if (book != null) {
			VerseList versesToReturn = new VerseList(this.getVersion(), book.toString());
			for (Verse verse : getVersesInclusive(new Reference(book, chapter1, verse1),
					new Reference(book, chapter2, verse2))) {
				versesToReturn.add(verse);
			}
			return versesToReturn;
		}
		return new VerseList(this.getVersion(), "");
	}
}
