package bibleReader.tests;

// If you organize imports, the following import might be removed and you will
// not be able to find certain methods. If you can't find something, copy the
// commented import statement below, paste a copy, and remove the comments.
// Keep this commented one in case you organize imports multiple times.
//
// import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bibleReader.BibleIO;
import bibleReader.model.ArrayListBible;
import bibleReader.model.Bible;
import bibleReader.model.BibleReaderModel;
import bibleReader.model.BookOfBible;
import bibleReader.model.MultiBibleModel;
import bibleReader.model.Reference;
import bibleReader.model.ReferenceList;
import bibleReader.model.Verse;
import bibleReader.model.VerseList;

/**
 * Tests for the Search capabilities of the BibleReaderModel class given multiple versions of the Bible in the model.
 * These tests assume BibleIO is working an can read in all three files.
 * 
 * @author Chuck Cusack, March, 2013
 */
public class Stage09_3ModelSearchTest  {
	private static VerseList[]	verses;
	private MultiBibleModel		model;
	private static String[]		versions	= new String[] { "kjv.atv", "asv.xmv", "esv.atv" };

	@BeforeClass
	public static void readFile() {
		verses = new VerseList[versions.length];
		for (int i = 0; i < versions.length; i++) {
			File file = new File(versions[i]);
			verses[i] = BibleIO.readBible(file);
		}
	}

	@Before
	public void setUp() throws Exception {
		model = new BibleReaderModel();
		for (int i = 0; i < versions.length; i++) {
			// Make a copy of the VerseList
			VerseList copyOfVerseList = new VerseList(verses[i].getVersion(), verses[i].getDescription(),
					new ArrayList<Verse>(verses[i]));
			Bible testBible = new ArrayListBible(copyOfVerseList);
			model.addBible(testBible);
		}
	}

	@Test (timeout = 1000)
	public void testGetReferenceContainingWithNoResults() {
		ReferenceList results = model.getReferencesContaining("three wise men");
		assertEquals(0, results.size());
		results = model.getReferencesContaining("trinity");
		assertEquals(0, results.size());
		results = model.getReferencesContaining("neo");
		assertEquals(0, results.size());
}

	@Test (timeout = 1000)
	public void testGetReferenceContainingWithOneResult() {
		ReferenceList results = model.getReferencesContaining("Christians");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Acts, 11, 26), results.get(0));
		
		results = model.getReferencesContaining("the fig tree shall not blossom");
		assertEquals(1, results.size());
		assertEquals(new Reference(BookOfBible.Habakkuk, 3, 17), results.get(0));
	}

	@Test (timeout = 1000)
	public void testGetReferenceContainingWithFewResults() {
		ReferenceList results = model.getReferencesContaining("Melchizedek");
		assertEquals(10, results.size());
		assertEquals(new Reference(BookOfBible.Genesis, 14, 18), results.get(0));
		assertEquals(new Reference(BookOfBible.Psalms, 110, 4), results.get(1));

		results = model.getReferencesContaining("god SO loved");
		assertEquals(2, results.size());
		Reference jhn3_16 = new Reference(BookOfBible.John, 3, 16);
		Reference firstJohn4_11 = new Reference(BookOfBible.John1, 4, 11);
		assertTrue(results.contains(jhn3_16));
		assertTrue(results.contains(firstJohn4_11));
	}

	@Test (timeout = 1000)
	public void testGetReferencesContainingWithManyResults() {
		// One that occurs 47 times, but change the case of the search string
		ReferenceList verseResults = model.getReferencesContaining("son oF GoD");
		assertEquals(47, verseResults.size());

		verseResults = model.getReferencesContaining("righteousness");
		// Biblegateway.com gets 348 results. If you don't pass this test, talk to me ASAP!
		assertEquals(348, verseResults.size());

		// Should get 532 verses for the word "three".
		// We'll test 3 known results--the first, last, and one in the middle.
		verseResults = model.getReferencesContaining("three");
		assertEquals(532, verseResults.size());
		assertEquals(new Reference(BookOfBible.Genesis, 5, 22), verseResults.get(0));
		assertEquals(new Reference(BookOfBible.Joshua, 7, 4), verseResults.get(126));
		assertEquals(new Reference(BookOfBible.Revelation, 21, 13), verseResults.get(531));
}

	@Test (timeout = 1000)
	public void testGetReferencesContainingWithPartialWords() {
		// This should match eaten as well as beaten, so it should return 143 results.
		ReferenceList verseResults = model.getReferencesContaining("eaten");
		assertEquals(170, verseResults.size());
}

	@Test (timeout = 1000)
	public void testExtremeSearches() {
		// Empty string should return no results.
		ReferenceList verseResults = model.getReferencesContaining("");
		assertEquals(0, verseResults.size());

		// Something that occurs a lot, like "the".
		// Of course, this isn't the number of verses containing the word the,
		// but the string "the", so it also matches verses with "then", etc.
		// Occurs in 28792 verses.
		// Do it with both search methods.
		verseResults = model.getReferencesContaining("the");
		assertEquals(28792, verseResults.size());

		// Space occurs in every verse. That is annoying.
		// Searches for ".", ",". etc. will be similar.
		// For now we won't worry about filtering these.
		// Our next version will take care of it.
		verseResults = model.getReferencesContaining(" ");
		assertEquals(31103, verseResults.size());

	}

	@Test (timeout = 1000)
	public void testCombinedSearch1() {
		VerseList kjvList = new VerseList("KJV", "mighty one");
		kjvList.add(new Verse(new Reference(BookOfBible.Genesis, 10, 8),
				"And Cush begat Nimrod: he began to be a mighty one in the earth."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 49, 24),
				"But his bow abode in strength, and the arms of his hands were made strong by the hands of the mighty God of Jacob; (from thence is the shepherd, the stone of Israel:)"));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Joshua, 22, 22),
				"The LORD God of gods, the LORD God of gods, he knoweth, and Israel he shall know; if it be in rebellion, or if in transgression against the LORD, (save us not this day,)"));
		kjvList.add(new Verse(new Reference(BookOfBible.Judges, 5, 22),
				"Then were the horsehoofs broken by the means of the pransings, the pransings of their mighty ones."));
		kjvList.add(new Verse(new Reference(BookOfBible.Chronicles1, 1, 10),
				"And Cush begat Nimrod: he began to be mighty upon the earth."));
		kjvList.add(new Verse(new Reference(BookOfBible.Psalms, 45, 3),
				"Gird thy sword upon thy thigh, O most mighty, with thy glory and thy majesty."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Psalms, 50, 1),
				"The mighty God, even the LORD, hath spoken, and called the earth from the rising of the sun unto the going down thereof."));
		kjvList.add(new Verse(new Reference(BookOfBible.Psalms, 89, 8),
				"O LORD God of hosts, who is a strong LORD like unto thee? or to thy faithfulness round about thee?"));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Psalms, 103, 20),
				"Bless the LORD, ye his angels, that excel in strength, that do his commandments, hearkening unto the voice of his word."));
		kjvList.add(new Verse(new Reference(BookOfBible.Psalms, 132, 2),
				"How he sware unto the LORD, and vowed unto the mighty God of Jacob;"));
		kjvList.add(new Verse(new Reference(BookOfBible.Psalms, 132, 5),
				"Until I find out a place for the LORD, an habitation for the mighty God of Jacob."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 1, 24),
				"Therefore saith the LORD, the LORD of hosts, the mighty One of Israel, Ah, I will ease me of mine adversaries, and avenge me of mine enemies:"));
		kjvList.add(new Verse(new Reference(BookOfBible.Isaiah, 10, 34),
				"And he shall cut down the thickets of the forest with iron, and Lebanon shall fall by a mighty one."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 13, 3),
				"I have commanded my sanctified ones, I have also called my mighty ones for mine anger, even them that rejoice in my highness."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 30, 29),
				"Ye shall have a song, as in the night when a holy solemnity is kept; and gladness of heart, as when one goeth with a pipe to come into the mountain of the LORD, to the mighty One of Israel."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 40, 10),
				"Behold, the Lord GOD will come with strong hand, and his arm shall rule for him: behold, his reward is with him, and his work before him."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 49, 26),
				"And I will feed them that oppress thee with their own flesh; and they shall be drunken with their own blood, as with sweet wine: and all flesh shall know that I the LORD am thy Saviour and thy Redeemer, the mighty One of Jacob."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 60, 16),
				"Thou shalt also suck the milk of the Gentiles, and shalt suck the breast of kings: and thou shalt know that I the LORD am thy Saviour and thy Redeemer, the mighty One of Jacob."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Jeremiah, 20, 11),
				"But the LORD is with me as a mighty terrible one: therefore my persecutors shall stumble, and they shall not prevail: they shall be greatly ashamed; for they shall not prosper: their everlasting confusion shall never be forgotten."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Jeremiah, 46, 5),
				"Wherefore have I seen them dismayed and turned away back? and their mighty ones are beaten down, and are fled apace, and look not back: for fear was round about, saith the LORD."));
		kjvList.add(new Verse(new Reference(BookOfBible.Jeremiah, 46, 15),
				"Why are thy valiant men swept away? they stood not, because the LORD did drive them."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 31, 11),
				"I have therefore delivered him into the hand of the mighty one of the heathen; he shall surely deal with him: I have driven him out for his wickedness."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 31, 14),
				"To the end that none of all the trees by the waters exalt themselves for their height, neither shoot up their top among the thick boughs, neither their trees stand up in their height, all that drink water: for they are all delivered unto death, to the nether parts of the earth, in the midst of the children of men, with them that go down to the pit."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 32, 12),
				"By the swords of the mighty will I cause thy multitude to fall, the terrible of the nations, all of them: and they shall spoil the pomp of Egypt, and all the multitude thereof shall be destroyed."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Daniel, 8, 24),
				"And his power shall be mighty, but not by his own power: and he shall destroy wonderfully, and shall prosper, and practise, and shall destroy the mighty and the holy people."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Joel, 3, 11),
				"Assemble yourselves, and come, all ye heathen, and gather yourselves together round about: thither cause thy mighty ones to come down, O LORD."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Zephaniah, 3, 17),
				"The LORD thy God in the midst of thee is mighty; he will save, he will rejoice over thee with joy; he will rest in his love, he will joy over thee with singing."));

		VerseList asvList = new VerseList("KJV", "mighty one");
		asvList.add(new Verse(new Reference(BookOfBible.Genesis, 10, 8),
				"And Cush begat Nimrod: he began to be a mighty one in the earth."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 49, 24),
				"But his bow abode in strength, And the arms of his hands were made strong, By the hands of the Mighty One of Jacob, (From thence is the shepherd, the stone of Israel),"));
		asvList.add(new Verse(
				new Reference(BookOfBible.Joshua, 22, 22),
				"The Mighty One, God, Jehovah, the Mighty One, God, Jehovah, he knoweth; and Israel he shall know: if it be in rebellion, or if in trespass against Jehovah (save thou us not this day,)"));
		asvList.add(new Verse(new Reference(BookOfBible.Judges, 5, 22),
				"Then did the horsehoofs stamp By reason of the prancings, the prancings of their strong ones."));
		asvList.add(new Verse(new Reference(BookOfBible.Chronicles1, 1, 10),
				"And Cush begat Nimrod; he began to be a mighty one in the earth."));
		asvList.add(new Verse(new Reference(BookOfBible.Psalms, 45, 3),
				"Gird thy sword upon thy thigh, O mighty one, Thy glory and thy majesty."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Psalms, 50, 1),
				"The Mighty One, God, Jehovah, hath spoken, And called the earth from the rising of the sun unto the going down thereof."));
		asvList.add(new Verse(new Reference(BookOfBible.Psalms, 89, 8),
				"O Jehovah God of hosts, Who is a mighty one, like unto thee, O Jehovah?  And thy faithfulness is round about thee."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Psalms, 103, 20),
				"Bless Jehovah, ye his angels, That are mighty in strength, that fulfil his word, Hearkening unto the voice of his word."));
		asvList.add(new Verse(new Reference(BookOfBible.Psalms, 132, 2),
				"How he sware unto Jehovah, And vowed unto the Mighty One of Jacob:"));
		asvList.add(new Verse(new Reference(BookOfBible.Psalms, 132, 5),
				"Until I find out a place for Jehovah, A tabernacle for the Mighty One of Jacob."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 1, 24),
				"Therefore saith the Lord, Jehovah of hosts, the Mighty One of Israel, Ah, I will ease me of mine adversaries, and avenge me of mine enemies;"));
		asvList.add(new Verse(new Reference(BookOfBible.Isaiah, 10, 34),
				"And he will cut down the thickets of the forest with iron, and Lebanon shall fall by a mighty one."));
		asvList.add(new Verse(new Reference(BookOfBible.Isaiah, 13, 3),
				"I have commanded my consecrated ones, yea, I have called my mighty men for mine anger, even my proudly exulting ones."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 30, 29),
				"Ye shall have a song as in the night when a holy feast is kept; and gladness of heart, as when one goeth with a pipe to come unto the mountain of Jehovah, to the Rock of Israel."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 40, 10),
				"Behold, the Lord Jehovah will come as a mighty one, and his arm will rule for him: Behold, his reward is with him, and his recompense before him."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 49, 26),
				"And I will feed them that oppress thee with their own flesh; and they shall be drunken with their own blood, as with sweet wine: and all flesh shall know that I, Jehovah, am thy Saviour, and thy Redeemer, the Mighty One of Jacob."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 60, 16),
				"Thou shalt also suck the milk of the nations, and shalt suck the breast of kings; and thou shalt know that I, Jehovah, am thy Saviour, and thy Redeemer, the Mighty One of Jacob."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Jeremiah, 20, 11),
				"But Jehovah is with me as a mighty one and a terrible: therefore my persecutors shall stumble, and they shall not prevail; they shall be utterly put to shame, because they have not dealt wisely, even with an everlasting dishonor which shall never be forgotten."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Jeremiah, 46, 5),
				"Wherefore have I seen it? they are dismayed and are turned backward; and their mighty ones are beaten down, and are fled apace, and look not back: terror is on every side, saith Jehovah."));
		asvList.add(new Verse(new Reference(BookOfBible.Jeremiah, 46, 15),
				"Why are thy strong ones swept away? they stood not, because Jehovah did drive them."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 31, 11),
				"I will even deliver him into the hand of the mighty one of the nations; he shall surely deal with him; I have driven him out for his wickedness."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 31, 14),
				"to the end that none of all the trees by the waters exalt themselves in their stature, neither set their top among the thick boughs, nor that their mighty ones stand up on their height, even all that drink water: for they are all delivered unto death, to the nether parts of the earth, in the midst of the children of men, with them that go down to the pit."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 32, 12),
				"By the swords of the mighty will I cause thy multitude to fall; the terrible of the nations are they all: and they shall bring to nought the pride of Egypt, and all the multitude thereof shall be destroyed."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Daniel, 8, 24),
				"And his power shall be mighty, but not by his own power; and he shall destroy wonderfully, and shall prosper and do his pleasure; and he shall destroy the mighty ones and the holy people."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Joel, 3, 11),
				"Haste ye, and come, all ye nations round about, and gather yourselves together: thither cause thy mighty ones to come down, O Jehovah."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Zephaniah, 3, 17),
				"Jehovah thy God is in the midst of thee, a mighty one who will save; he will rejoice over thee with joy; he will rest in his love; he will joy over thee with singing."));

		VerseList esvList = new VerseList("KJV", "mighty one");
		esvList.add(new Verse(new Reference(BookOfBible.Genesis, 10, 8),
				"Cush fathered Nimrod; he was the first on earth to be a mighty man."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 49, 24),
				"yet <sup>d</sup>his bow remained unmoved; his arms were made agile by the hands of the <sup>e</sup>Mighty One of Jacob (from there is <sup>f</sup>the Shepherd, <sup>g</sup>the Stone of Israel),"));
		esvList.add(new Verse(
				new Reference(BookOfBible.Joshua, 22, 22),
				"&#8220;The Mighty One, <sup>q</sup>God, the LORD! The Mighty One, God, the LORD! <sup>r</sup>He knows; and let Israel itself know! If it was in rebellion or in breach of faith against the LORD, do not spare us today"));
		esvList.add(new Verse(new Reference(BookOfBible.Judges, 5, 22),
				"&#8220;Then loud beat the horses' hoofs with the galloping, galloping of his steeds."));
		esvList.add(new Verse(new Reference(BookOfBible.Chronicles1, 1, 10),
				"Cush fathered Nimrod. He was the first on earth to be a mighty man."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Psalms, 45, 3),
				"<sup>z</sup>Gird your <sup>a</sup>sword on your thigh, O <sup>b</sup>mighty one, in <sup>c</sup>your splendor and majesty!"));
		esvList.add(new Verse(
				new Reference(BookOfBible.Psalms, 50, 1),
				"A Psalm of <sup>n</sup>Asaph. <sup>o</sup>The Mighty One, God the LORD, speaks and summons the earth <sup>p</sup>from the rising of the sun to its setting."));
		esvList.add(new Verse(new Reference(BookOfBible.Psalms, 89, 8),
				"O LORD God of hosts, <sup>x</sup>who is mighty as you are, O <sup>y</sup>LORD, with your faithfulness all around you?"));
		esvList.add(new Verse(
				new Reference(BookOfBible.Psalms, 103, 20),
				"Bless the LORD, O you <sup>e</sup>his angels, you <sup>f</sup>mighty ones who <sup>g</sup>do his word, obeying the voice of his word!"));
		esvList.add(new Verse(new Reference(BookOfBible.Psalms, 132, 2),
				"how he swore to the LORD and <sup>z</sup>vowed to <sup>a</sup>the Mighty One of Jacob,"));
		esvList.add(new Verse(new Reference(BookOfBible.Psalms, 132, 5),
				"until I <sup>c</sup>find a place for the LORD, a dwelling place for <sup>a</sup>the Mighty One of Jacob.&#8221;"));
		esvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 1, 24),
				"Therefore the <sup>w</sup>Lord declares, the LORD of hosts, the <sup>x</sup>Mighty One of Israel: &#8220;Ah, I will get relief from my enemies <sup>y</sup>and avenge myself on my foes."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 10, 34),
				"He will cut down <sup>j</sup>the thickets of the forest with an axe, and <sup>k</sup>Lebanon will fall by the Majestic One."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 13, 3),
				"I myself have commanded my consecrated ones, and have summoned my mighty men to execute my anger, my proudly exulting ones."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 30, 29),
				"You shall have a song as in the night when a holy feast is kept, and gladness of heart, <sup>j</sup>as when one sets out to the sound of the flute to go to <sup>k</sup>the mountain of the LORD, to <sup>l</sup>the Rock of Israel."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 40, 10),
				"<sup>l</sup>Behold, the Lord GOD comes with might, and his arm rules for him; <sup>m</sup>behold, his reward is with him, and his recompense before him."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 49, 26),
				"<sup>d</sup>I will make your oppressors eat their own flesh, and they shall be drunk <sup>e</sup>with their own blood as with wine. Then all flesh shall know that <sup>f</sup>I am the LORD your Savior, and your Redeemer, the Mighty One of Jacob.&#8221;"));
		esvList.add(new Verse(
				new Reference(BookOfBible.Isaiah, 60, 16),
				"<sup>f</sup>You shall suck the milk of nations; you shall nurse at the breast of kings; and you shall know that <sup>g</sup>I, the LORD, am your Savior and your Redeemer, <sup>h</sup>the Mighty One of Jacob."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Jeremiah, 20, 11),
				"But <sup>i</sup>the LORD is with me as a dread warrior; therefore my persecutors will stumble; <sup>i</sup>they will not overcome me. <sup>j</sup>They will be greatly shamed, for they will not succeed. Their <sup>k</sup>eternal dishonor will never be forgotten."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Jeremiah, 46, 5),
				"Why have I seen it? They are dismayed and have turned backward. Their <sup>h</sup>warriors are beaten down and have fled in haste; <sup>i</sup>they look not back&#8212; <sup>j</sup>terror on every side! declares the LORD."));
		esvList.add(new Verse(new Reference(BookOfBible.Jeremiah, 46, 15),
				"Why are your <sup>e</sup>mighty ones face down? They do not stand because the LORD thrust them down."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 31, 11),
				"I will give it into the hand of a mighty one of the nations. He shall surely deal with it as its wickedness deserves. I have cast it out."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 31, 14),
				"<sup>r</sup>All this is in order that no trees by the waters may grow to towering height or set their tops among the clouds, and that no trees that drink water may reach up to them in height. For they are all given over to death, <sup>s</sup>to the world <sup>t</sup>below, among the children of man, with those who go down to the pit."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 32, 12),
				"I will cause <sup>y</sup>your multitude to fall by the swords of mighty ones, all of them <sup>z</sup>most ruthless of nations. <sup>a</sup>&#8220;They shall bring to ruin the pride of Egypt, and all its multitude shall perish."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Daniel, 8, 24),
				"His power shall be great&#8212;<sup>o</sup>but not by his own power; and he shall cause fearful destruction <sup>p</sup>and shall succeed in what he does, <sup>q</sup>and destroy mighty men and the people who are the saints."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Joel, 3, 11),
				"<sup>u</sup>Hasten and come, all you surrounding nations, and gather yourselves there. <sup>v</sup>Bring down your warriors, O LORD."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Zephaniah, 3, 17),
				"<sup>l</sup>The LORD your God is in your midst, <sup>n</sup>a mighty one who will save; <sup>o</sup>he will rejoice over you with gladness; he will quiet you by his love; he will exult over you with loud singing."));

		ReferenceList actualRefs = model.getReferencesContaining("mighty one");
		assertEquals(27, actualRefs.size());

		VerseList kjvActual = model.getVerses("KJV", actualRefs);
		assertArrayEquals(kjvList.toArray(), kjvActual.toArray());

		VerseList asvActual = model.getVerses("ASV", actualRefs);
		assertArrayEquals(asvList.toArray(), asvActual.toArray());

		VerseList esvActual = model.getVerses("ESV", actualRefs);
		assertArrayEquals(esvList.toArray(), esvActual.toArray());

	}

	@Test (timeout = 1000)
	public void testCombinedSearch2() {
		VerseList kjvList = new VerseList("KJV", "wagon");
		kjvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 45, 19),
				"Now thou art commanded, this do ye; take you wagons out of the land of Egypt for your little ones, and for your wives, and bring your father, and come."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 45, 21),
				"And the children of Israel did so: and Joseph gave them wagons, according to the commandment of Pharaoh, and gave them provision for the way."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 45, 27),
				"And they told him all the words of Joseph, which he had said unto them: and when he saw the wagons which Joseph had sent to carry him, the spirit of Jacob their father revived:"));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 46, 5),
				"And Jacob rose up from Beersheba: and the sons of Israel carried Jacob their father, and their little ones, and their wives, in the wagons which Pharaoh had sent to carry him."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Numbers, 7, 3),
				"And they brought their offering before the LORD, six covered wagons, and twelve oxen; a wagon for two of the princes, and for each one an ox: and they brought them before the tabernacle."));
		kjvList.add(new Verse(new Reference(BookOfBible.Numbers, 7, 6),
				"And Moses took the wagons and the oxen, and gave them unto the Levites."));
		kjvList.add(new Verse(new Reference(BookOfBible.Numbers, 7, 7),
				"Two wagons and four oxen he gave unto the sons of Gershon, according to their service:"));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Numbers, 7, 8),
				"And four wagons and eight oxen he gave unto the sons of Merari, according unto their service, under the hand of Ithamar the son of Aaron the priest."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Samuel1, 17, 20),
				"And David rose up early in the morning, and left the sheep with a keeper, and took, and went, as Jesse had commanded him; and he came to the trench, as the host was going forth to the fight, and shouted for the battle."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Samuel1, 26, 5),
				"And David arose, and came to the place where Saul had pitched: and David beheld the place where Saul lay, and Abner the son of Ner, the captain of his host: and Saul lay in the trench, and the people pitched round about him."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Samuel1, 26, 7),
				"So David and Abishai came to the people by night: and, behold, Saul lay sleeping within the trench, and his spear stuck in the ground at his bolster: but Abner and the people lay round about him."));
		kjvList.add(new Verse(new Reference(BookOfBible.Psalms, 65, 11),
				"Thou crownest the year with thy goodness; and thy paths drop fatness."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 23, 24),
				"And they shall come against thee with chariots, wagons, and wheels, and with an assembly of people, which shall set against thee buckler and shield and helmet round about: and I will set judgment before them, and they shall judge thee according to their judgments."));
		kjvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 26, 10),
				"By reason of the abundance of his horses their dust shall cover thee: thy walls shall shake at the noise of the horsemen, and of the wheels, and of the chariots, when he shall enter into thy gates, as men enter into a city wherein is made a breach."));

		VerseList asvList = new VerseList("ASV", "wagon");
		asvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 45, 19),
				"Now thou art commanded, this do ye: take you wagons out of the land of Egypt for your little ones, and for your wives, and bring your father, and come."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 45, 21),
				"And the sons of Israel did so: and Joseph gave them wagons, according to the commandment of Pharaoh, and gave them provision for the way."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 45, 27),
				"And they told him all the words of Joseph, which he had said unto them: and when he saw the wagons which Joseph had sent to carry him, the spirit of Jacob their father revived:"));
		asvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 46, 5),
				"And Jacob rose up from Beer-sheba: and the sons of Israel carried Jacob their father, and their little ones, and their wives, in the wagons which Pharaoh had sent to carry him."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Numbers, 7, 3),
				"and they brought their oblation before Jehovah, six covered wagons, and twelve oxen; a wagon for every two of the princes, and for each one an ox: and they presented them before the tabernacle."));
		asvList.add(new Verse(new Reference(BookOfBible.Numbers, 7, 6),
				"And Moses took the wagons and the oxen, and gave them unto the Levites."));
		asvList.add(new Verse(new Reference(BookOfBible.Numbers, 7, 7),
				"Two wagons and four oxen he gave unto the sons of Gershon, according to their service:"));
		asvList.add(new Verse(
				new Reference(BookOfBible.Numbers, 7, 8),
				"and four wagons and eight oxen he gave unto the sons of Merari, according unto their service, under the hand of Ithamar the son of Aaron the priest."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Samuel1, 17, 20),
				"And David rose up early in the morning, and left the sheep with a keeper, and took, and went, as Jesse had commanded him; and he came to the place of the wagons, as the host which was going forth to the fight shouted for the battle."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Samuel1, 26, 5),
				"And David arose, and came to the place where Saul had encamped; and David beheld the place where Saul lay, and Abner the son of Ner, the captain of his host: and Saul lay within the place of the wagons, and the people were encamped round about him."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Samuel1, 26, 7),
				"So David and Abishai came to the people by night: and, behold, Saul lay sleeping within the place of the wagons, with his spear stuck in the ground at his head; and Abner and the people lay round about him."));
		asvList.add(new Verse(new Reference(BookOfBible.Psalms, 65, 11),
				"Thou crownest the year with thy goodness; And thy paths drop fatness."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 23, 24),
				"And they shall come against thee with weapons, chariots, and wagons, and with a company of peoples; they shall set themselves against thee with buckler and shield and helmet round about; and I will commit the judgment unto them, and they shall judge thee according to their judgments."));
		asvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 26, 10),
				"By reason of the abundance of his horses their dust shall cover thee: thy walls shall shake at the noise of the horsemen, and of the wagons, and of the chariots, when he shall enter into thy gates, as men enter into a city wherein is made a breach."));

		VerseList esvList = new VerseList("ESV", "wagon");
		esvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 45, 19),
				"And you, Joseph, are commanded to say, &#8216;Do this: take <sup>b</sup>wagons from the land of Egypt for your little ones and for your wives, and bring your father, and come."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 45, 21),
				"The sons of Israel did so: and Joseph gave them <sup>b</sup>wagons, according to the command of Pharaoh, and gave them provisions for the journey."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 45, 27),
				"But when they told him all the words of Joseph, which he had said to them, and when he saw <sup>f</sup>the wagons that Joseph had sent to carry him, the spirit of their father Jacob revived."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Genesis, 46, 5),
				"Then Jacob set out from Beersheba. The sons of Israel carried Jacob their father, their little ones, and their wives, in the wagons <sup>n</sup>that Pharaoh had sent to carry him."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Numbers, 7, 3),
				"and brought their offerings before the LORD, six wagons and twelve oxen, a wagon for every two of the chiefs, and for each one an ox. They brought them before the tabernacle."));
		esvList.add(new Verse(new Reference(BookOfBible.Numbers, 7, 6),
				"So Moses took the wagons and the oxen and gave them to the Levites."));
		esvList.add(new Verse(new Reference(BookOfBible.Numbers, 7, 7),
				"Two wagons and four oxen <sup>i</sup>he gave to the sons of Gershon, according to their service."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Numbers, 7, 8),
				"And four wagons and eight oxen <sup>j</sup>he gave to the sons of Merari, according to their service, under the direction of Ithamar the son of Aaron the priest."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Samuel1, 17, 20),
				"And David rose early in the morning and left the sheep with a keeper and took the provisions and went, as Jesse had commanded him. And he came to <sup>q</sup>the encampment as the host was going out to the battle line, shouting the war cry."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Samuel1, 26, 5),
				"Then David rose and came to the place where Saul had encamped. And David saw the place where Saul lay, with <sup>w</sup>Abner the son of Ner, the commander of his army. Saul was lying within <sup>x</sup>the encampment, while the army was encamped around him."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Samuel1, 26, 7),
				"So David and Abishai went to the army by night. And there lay Saul sleeping within <sup>x</sup>the encampment, with his spear stuck in the ground <sup>a</sup>at his head, and Abner and the army lay around him."));
		esvList.add(new Verse(new Reference(BookOfBible.Psalms, 65, 11),
				"You crown the year with your bounty; your wagon tracks <sup>w</sup>overflow with abundance."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 23, 24),
				"And they shall come against you from the north with chariots and wagons and a host of peoples. <sup>m</sup>They shall set themselves against you on every side with buckler, shield, and helmet; and <sup>n</sup>I will commit the judgment to them, and <sup>o</sup>they shall judge you according to their judgments."));
		esvList.add(new Verse(
				new Reference(BookOfBible.Ezekiel, 26, 10),
				"His horses will be so many that their dust will cover you. Your walls will shake at the noise of the horsemen and wagons and chariots, when he enters your gates as men enter a city that has been breached."));

		ReferenceList actualRefs = model.getReferencesContaining("wagon");
		assertEquals(14, actualRefs.size());
		VerseList kjvActual = model.getVerses("KJV", actualRefs);
		assertArrayEquals(kjvList.toArray(), kjvActual.toArray());

		VerseList asvActual = model.getVerses("ASV", actualRefs);
		assertArrayEquals(asvList.toArray(), asvActual.toArray());

		VerseList esvActual = model.getVerses("ESV", actualRefs);
		assertArrayEquals(esvList.toArray(), esvActual.toArray());
	}
}

