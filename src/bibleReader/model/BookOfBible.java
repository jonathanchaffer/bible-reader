package bibleReader.model;

import java.util.TreeMap;

/**
 * An Enum that stores all 66 books of the bible. This class helps us in several ways. It gives us a way to order the
 * books by the normal order they appear as well as allowing us to store several abbreviations for each.
 * 
 * @author Charles Cusack
 * @version 3.0 Written November, 2005 Modified and improved, November 2006. Modified again, October 2007. Modified and
 *          improved, March 2013 (Increased the possible inputs that will return a valid book). Permission is given to
 *          use and/or modify this code.
 */
public enum BookOfBible {
	// The 66 books of the Bible
	Genesis("Genesis"), Exodus("Exodus"), Leviticus("Leviticus"), Numbers("Numbers"), Deuteronomy("Deuteronomy"), Joshua(
			"Joshua"), Judges("Judges"), Ruth("Ruth"), Samuel1("1 Samuel"), Samuel2("2 Samuel"), Kings1("1 Kings"), Kings2(
			"2 Kings"), Chronicles1("1 Chronicles"), Chronicles2("2 Chronicles"), Ezra("Ezra"), Nehemiah("Nehemiah"), Esther(
			"Esther"), Job("Job"), Psalms("Psalms"), Proverbs("Proverbs"), Ecclesiastes("Ecclesiastes"), SongOfSolomon(
			"Song of Solomon"), Isaiah("Isaiah"), Jeremiah("Jeremiah"), Lamentations("Lamentations"), Ezekiel("Ezekiel"), Daniel(
			"Daniel"), Hosea("Hosea"), Joel("Joel"), Amos("Amos"), Obadiah("Obadiah"), Jonah("Jonah"), Micah("Micah"), Nahum(
			"Nahum"), Habakkuk("Habakkuk"), Zephaniah("Zephaniah"), Haggai("Haggai"), Zechariah("Zechariah"), Malachi(
			"Malachi"), Matthew("Matthew"), Mark("Mark"), Luke("Luke"), John("John"), Acts("Acts"), Romans("Romans"), Corinthians1(
			"1 Corinthians"), Corinthians2("2 Corinthians"), Galatians("Galatians"), Ephesians("Ephesians"), Philippians(
			"Philippians"), Colossians("Colossians"), Thessalonians1("1 Thessalonians"), Thessalonians2(
			"2 Thessalonians"), Timothy1("1 Timothy"), Timothy2("2 Timothy"), Titus("Titus"), Philemon("Philemon"), Hebrews(
			"Hebrews"), James("James"), Peter1("1 Peter"), Peter2("2 Peter"), John1("1 John"), John2("2 John"), John3(
			"3 John"), Jude("Jude"), Revelation("Revelation"), Dummy("Dummy");
	/*
	 * We add a dummy book at the end because it makes the nextBook method easier to implement since then Revelation has
	 * a nextBook--Dummy. I also add a Dummy 1:1 at the end of my Bible to make implementing a few methods easier.
	 */

	// The title of the book.
	private String	title;

	/*
	 * private constructor. This means that only the ones above can be created. This is good since nobody should be
	 * adding books to the Bible anyway.
	 */
	private BookOfBible(String t) {
		title = t;
	}

	@Override
	public String toString() {
		return title;
	}

	// Given the Enum value of a book, give the Enum value
	// of the next book.
	public static BookOfBible nextBook(BookOfBible b) {
		int ord = b.ordinal();
		if (ord <= Revelation.ordinal()) {
			return BookOfBible.values()[b.ordinal() + 1];
		} else {
			return Dummy;
		}
	}

	/*
	 * We create an array of the names of the books for use wherever someone might want it. It is a static field because
	 * we only really need to create it once.
	 */
	private static String[]	names	= null;

	/**
	 * @return an array of Strings of the book names, not including Dummy.
	 */
	public static String[] getBookNames() {
		// Only create the array once.
		if (names == null) {
			// This gives us an array of all of the BookOfBible objects, including Dummy.
			// We iterate over it and create an array of strings, one for each BookOfBible.
			BookOfBible[] books = BookOfBible.values();
			int numberOfBooks = books.length - 1; // We don't count Dummy.
			names = new String[numberOfBooks];
			for (int i = 0; i < numberOfBooks; i++) {
				names[i] = books[i].toString();
			}
		}
		return names;
	}

	/**
	 * @param abbrev The abbreviation of the desired book.
	 * @return The corresponding BookOfBible object, or null if it isn't listed.
	 */
	public static BookOfBible getBookOfBible(String abbrev) {
		String abb = abbrev.toLowerCase().replaceAll("\\s", "");
		BookOfBible book = theBooks.get(abb);
		return book;
	}

	/*
	 * We create a Map of abbreviations to BookOfBible objects so we can use it to look up abbreviations (see method
	 * above).
	 */
	private static TreeMap<String, BookOfBible>	theBooks	= null;

	static {
		theBooks = new TreeMap<String, BookOfBible>();
		theBooks.clear();
		theBooks.put("ge", Genesis);
		theBooks.put("gen", Genesis);
		theBooks.put("gn", Genesis);
		theBooks.put("genesis", Genesis);
		theBooks.put("exo", Exodus);
		theBooks.put("exodus", Exodus);
		theBooks.put("ex", Exodus);
		theBooks.put("lev", Leviticus);
		theBooks.put("lv", Leviticus);
		theBooks.put("leviticus", Leviticus);
		theBooks.put("num", Numbers);
		theBooks.put("numb", Numbers);
		theBooks.put("nm", Numbers);
		theBooks.put("numbers", Numbers);
		theBooks.put("dt", Deuteronomy);
		theBooks.put("deu", Deuteronomy);
		theBooks.put("deut", Deuteronomy);
		theBooks.put("deuteronomy", Deuteronomy);
		theBooks.put("josh", Joshua);
		theBooks.put("jos", Joshua);
		theBooks.put("joshua", Joshua);
		theBooks.put("jdgs", Judges);
		theBooks.put("jgs", Judges);
		theBooks.put("judges", Judges);
		theBooks.put("ru", Ruth);
		theBooks.put("ruth", Ruth);
		theBooks.put("1sm", Samuel1);
		theBooks.put("1samuel", Samuel1);
		theBooks.put("2sm", Samuel2);
		theBooks.put("2samuel", Samuel2);
		theBooks.put("1ki", Kings1);
		theBooks.put("1kings", Kings1);
		theBooks.put("1kgs", Kings1);
		theBooks.put("2ki", Kings2);
		theBooks.put("2kgs", Kings2);
		theBooks.put("2kings", Kings2);
		theBooks.put("1chr", Chronicles1);
		theBooks.put("1chronicles", Chronicles1);
		theBooks.put("2chr", Chronicles2);
		theBooks.put("2chronicles", Chronicles2);
		theBooks.put("ezra", Ezra);
		theBooks.put("ezr", Ezra);
		theBooks.put("nehemiah", Nehemiah);
		theBooks.put("neh", Nehemiah);
		theBooks.put("est", Esther);
		theBooks.put("esther", Esther);
		theBooks.put("job", Job);
		theBooks.put("jb", Job);
		theBooks.put("psa", Psalms);
		theBooks.put("ps", Psalms);
		theBooks.put("psalms", Psalms);
		theBooks.put("psalm", Psalms);
		theBooks.put("prv", Proverbs);
		theBooks.put("proverbs", Proverbs);
		theBooks.put("eccl", Ecclesiastes);
		theBooks.put("ecclesiastes", Ecclesiastes);
		theBooks.put("ssol", SongOfSolomon);
		theBooks.put("sg", SongOfSolomon);
		theBooks.put("sos", SongOfSolomon);
		theBooks.put("songofsolomon", SongOfSolomon);
		theBooks.put("songofsongs", SongOfSolomon);
		theBooks.put("isa", Isaiah);
		theBooks.put("is", Isaiah);
		theBooks.put("isaiah", Isaiah);
		theBooks.put("jer", Jeremiah);
		theBooks.put("jeremiah", Jeremiah);
		theBooks.put("lam", Lamentations);
		theBooks.put("lamentations", Lamentations);
		theBooks.put("eze", Ezekiel);
		theBooks.put("ez", Ezekiel);
		theBooks.put("ezekiel", Ezekiel);
		theBooks.put("dan", Daniel);
		theBooks.put("dn", Daniel);
		theBooks.put("daniel", Daniel);
		theBooks.put("hos", Hosea);
		theBooks.put("hosea", Hosea);
		theBooks.put("joel", Joel);
		theBooks.put("jl", Joel);
		theBooks.put("amos", Amos);
		theBooks.put("am", Amos);
		theBooks.put("obad", Obadiah);
		theBooks.put("obadiah", Obadiah);
		theBooks.put("ob", Obadiah);
		theBooks.put("jonah", Jonah);
		theBooks.put("jon", Jonah);
		theBooks.put("mic", Micah);
		theBooks.put("mi", Micah);
		theBooks.put("micah", Micah);
		theBooks.put("nahum", Nahum);
		theBooks.put("na", Nahum);
		theBooks.put("hab", Habakkuk);
		theBooks.put("hb", Habakkuk);
		theBooks.put("habakkuk", Habakkuk);
		theBooks.put("zep", Zephaniah);
		theBooks.put("zephaniah", Zephaniah);
		theBooks.put("hag", Haggai);
		theBooks.put("haggai", Haggai);
		theBooks.put("zec", Zechariah);
		theBooks.put("zechariah", Zechariah);
		theBooks.put("hag", Haggai);
		theBooks.put("hg", Haggai);
		theBooks.put("zec", Zechariah);
		theBooks.put("mal", Malachi);
		theBooks.put("malachi", Malachi);
		theBooks.put("mat", Matthew);
		theBooks.put("mt", Matthew);
		theBooks.put("matthew", Matthew);
		theBooks.put("mark", Mark);
		theBooks.put("mk", Mark);
		theBooks.put("luke", Luke);
		theBooks.put("lk", Luke);
		theBooks.put("john", John);
		theBooks.put("jn", John);
		theBooks.put("acts", Acts);
		theBooks.put("rom", Romans);
		theBooks.put("romans", Romans);
		theBooks.put("1cor", Corinthians1);
		theBooks.put("1corinthians", Corinthians1);
		theBooks.put("2cor", Corinthians2);
		theBooks.put("2corinthians", Corinthians2);
		theBooks.put("gal", Galatians);
		theBooks.put("galatians", Galatians);
		theBooks.put("eph", Ephesians);
		theBooks.put("ephesians", Ephesians);
		theBooks.put("phi", Philippians);
		theBooks.put("philippians", Philippians);
		theBooks.put("phil", Philippians);
		theBooks.put("col", Colossians);
		theBooks.put("colossians", Colossians);
		theBooks.put("1th", Thessalonians1);
		theBooks.put("1thessalonians", Thessalonians1);
		theBooks.put("1thes", Thessalonians1);
		theBooks.put("2th", Thessalonians2);
		theBooks.put("2thes", Thessalonians2);
		theBooks.put("2thessalonians", Thessalonians2);
		theBooks.put("1timothy", Timothy1);
		theBooks.put("1tm", Timothy1);
		theBooks.put("1tim", Timothy1);
		theBooks.put("2tim", Timothy2);
		theBooks.put("2tm", Timothy2);
		theBooks.put("2timothy", Timothy2);
		theBooks.put("titus", Titus);
		theBooks.put("ti", Titus);
		theBooks.put("phmn", Philemon);
		theBooks.put("phlm", Philemon);
		theBooks.put("philemon", Philemon);
		theBooks.put("heb", Hebrews);
		theBooks.put("hebrews", Hebrews);
		theBooks.put("jas", James);
		theBooks.put("james", James);
		theBooks.put("1pet", Peter1);
		theBooks.put("1pt", Peter1);
		theBooks.put("1peter", Peter1);
		theBooks.put("2pet", Peter2);
		theBooks.put("2pt", Peter2);
		theBooks.put("2peter", Peter2);
		theBooks.put("1jn", John1);
		theBooks.put("1john", John1);
		theBooks.put("2jn", John2);
		theBooks.put("2john", John2);
		theBooks.put("3jn", John3);
		theBooks.put("3john", John3);
		theBooks.put("jude", Jude);
		theBooks.put("rev", Revelation);
		theBooks.put("rv", Revelation);
		theBooks.put("revelation", Revelation);
	}
}
