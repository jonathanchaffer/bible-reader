package bibleReader.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A list of references. This is just a subclass of ArrayList. It is mostly for
 * convenience. This is done as a subclass rather than a wrapper class so that
 * all of the ArrayList methods are available.
 * 
 * @author cusack, January 21, 2013
 */
public class ReferenceList extends ArrayList<Reference> {
	public ReferenceList() {
		// Needs to be here so we can call the default constructor.
	}

	public ReferenceList(int initialSize) {
		super(initialSize);
	}

	public ReferenceList(Collection<? extends Reference> list) {
		super(list);
	}

}
