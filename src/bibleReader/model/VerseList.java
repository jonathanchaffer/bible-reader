package bibleReader.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A list of verses. This is just a subclass of ArrayList. It is mostly for
 * convenience. This is done as a subclass rather than a wrapper class so that
 * all of the ArrayList methods are available.
 * 
 * @author cusack, January 21, 2013
 */
public class VerseList extends ArrayList<Verse> {
	private String version;
	private String description;

	/**
	 * Create a list of verses from the given version.
	 * 
	 * @param version
	 *            The version of the Bible the verses will be from.
	 * @param description
	 *            A description of the verses. Depending on context, this might be
	 *            the title of the Bible (e.g. "ASV"), the term(s) common to all of
	 *            the Verses (e.g. "God loved"), or the passage the verses are from
	 *            (e.g. "Ecclesiastes 3:1-8").
	 */
	public VerseList(String version, String description) {
		super();
		this.version = version;
		this.description = description;
	}

	/**
	 * Create a list of verses from the given version.
	 * 
	 * @param version
	 *            The version of the Bible the verses will be from.
	 * @param description
	 *            description A description of the verses. Depending on context,
	 *            this might be the title of the Bible (e.g. "ASV"), the term(s)
	 *            common to all of the Verses (e.g. "God loved"), or the passage the
	 *            verses are from (e.g. "Ecclesiastes 3:1-8").
	 * @param verses
	 *            the list of verses to place in the list.
	 */
	public VerseList(String version, String description, Collection<? extends Verse> verses) {
		super(verses);
		this.version = version;
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public String getDescription() {
		return description;
	}

}
