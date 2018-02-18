package bibleReader.model;

/**
 * A simple class that stores the book, chapter number, and verse number.
 * 
 * @author Charles Cusack, implemented February 2, 2013.
 */
public class Reference implements Comparable<Reference> {
	private BookOfBible book;
	private int chapter;
	private int verse;

	/**
	 * @param book
	 * @param chapter
	 * @param verse
	 */
	public Reference(BookOfBible book, int chapter, int verse) {
		this.book = book;
		this.chapter = chapter;
		this.verse = verse;
	}

	public String getBook() {
		return book.toString();
	}

	public BookOfBible getBookOfBible() {
		return book;
	}

	public int getChapter() {
		return chapter;
	}

	public int getVerse() {
		return verse;
	}

	/*
	 * This method should return the reference in the usual form (e.g.
	 * "Genesis 2:3").
	 */
	@Override
	public String toString() {
		return getBook() + " " + chapter + ":" + verse;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Reference) {
			Reference ref = (Reference) other;
			return (book == ref.book && chapter == ref.chapter && verse == ref.verse);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	// @Override
	// public int compareTo(Reference otherRef) {
	// int diffBook = book.compareTo(otherRef.book);
	// if (diffBook != 0) {
	// return diffBook;
	// } else {
	// // Taking advantage of Autoboxing here and below.
	// int diffChapter = Integer.valueOf(chapter).compareTo(otherRef.chapter);
	// if (diffChapter != 0) {
	// return diffChapter;
	// } else {
	// return Integer.valueOf(verse).compareTo(otherRef.verse);
	// }
	// }
	// }

	@Override
	public int compareTo(Reference otherRef) {
		int diffBook = book.compareTo(otherRef.book);
		if (diffBook != 0) {
			return diffBook;
		} else {
			int diffChapter = chapter - otherRef.chapter;
			if (diffChapter != 0) {
				return diffChapter;
			} else {
				return verse - otherRef.verse;
			}
		}
	}

}
