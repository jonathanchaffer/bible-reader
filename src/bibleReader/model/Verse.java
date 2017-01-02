package bibleReader.model;

/**
 * A class which stores a verse.
 * 
 * @author Chuck Cusack, Implemented February 2, 2013
 */
public class Verse implements Comparable<Verse> {
	private Reference	reference;
	private String		text;

	/**
	 * Construct a verse given the reference and the text.
	 * 
	 * @param r The reference for the verse
	 * @param t The text of the verse
	 */
	public Verse(Reference r, String t) {
		reference = r;
		text = t;
	}

	/**
	 * Construct a verse given the book, chapter, verse, and text.
	 * 
	 * @param book The book of the Bible
	 * @param chapter The chapter number
	 * @param verse The verse number
	 * @param text The text of the verse
	 */
	public Verse(BookOfBible book, int chapter, int verse, String text) {
		reference = new Reference(book, chapter, verse);
		this.text = text;
	}

	/**
	 * Returns the Reference object for this Verse.
	 * 
	 * @return A reference to the Reference for this Verse.
	 */
	public Reference getReference() {
		return reference;
	}

	/**
	 * Returns the text of this Verse.
	 * 
	 * @return A String representation of the text of the verse.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Returns a String representation of this Verse, which is a String representation of the Reference followed by a
	 * space and then the String representation of the text of the verse.
	 */
	@Override
	public String toString() {
		return reference + " " + text;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Verse) {
			Verse v = (Verse) other;
			if (text != null) {
				return reference.equals(v.reference) && text.equals(v.text);
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public int compareTo(Verse other) {
		int diffReference = reference.compareTo(other.reference);
		if (diffReference != 0) {
			return diffReference;
		} else {
			if (text != null && other.text != null) {
				return text.compareTo(other.text);
			} else if (text != null) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	/**
	 * Return true if and only if this verse the other verse have the same reference. (So the text is ignored).
	 * 
	 * @param other the other Verse.
	 * @return true if and only if this verse and the other have the same reference.
	 */
	public boolean sameReference(Verse other) {
		return getReference().equals(other.getReference());
	}

}
