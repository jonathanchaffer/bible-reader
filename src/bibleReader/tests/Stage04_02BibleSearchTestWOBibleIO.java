package bibleReader.tests;

// If you organize imports, the following import might be removed and you will
// not be able to find certain methods. If you can't find something, copy the
// commented import statement below, paste a copy, and remove the comments.
// Keep this commented one in case you organize imports multiple times.
//
// import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import bibleReader.model.ArrayListBible;
import bibleReader.model.Bible;
import bibleReader.model.BookOfBible;
import bibleReader.model.Reference;
import bibleReader.model.ReferenceList;
import bibleReader.model.Verse;
import bibleReader.model.VerseList;

/**
 * Tests for the Search capabilities of the Bible class. These tests assume BibleIO is working an can read in the
 * kjv.atv file.
 * 
 * @author Chuck Cusack, January, 2013
 */
public class Stage04_02BibleSearchTestWOBibleIO {
	private VerseList sampleVerses;
	private Bible		testBible;
	private VerseList copyOfVerseList;


	@Test (timeout=5000)
	public void testConstructor() {
		assertEquals(168,testBible.getNumberOfVerses());
		copyOfVerseList.clear();
		assertEquals(168,testBible.getNumberOfVerses());
	}
	
	@Test (timeout=5000)
	public void testGetReferenceContainingWithNoResults() {
		ReferenceList refResults = testBible.getReferencesContaining("three wise men");
		assertEquals(0, refResults.size());
		refResults = testBible.getReferencesContaining("trinity");
		assertEquals(0, refResults.size());
		refResults = testBible.getReferencesContaining("neo"); // Get it?
		assertEquals(0, refResults.size());
	}

	@Test (timeout=5000)
	public void testGetReferenceContainingWithOneResult() {
		ReferenceList refResults = testBible.getReferencesContaining("If we love one another");
		assertEquals(1, refResults.size());
		assertEquals(new Reference(BookOfBible.John1, 4, 12), refResults.get(0));
	}

	@Test (timeout=5000)
	public void testGetReferenceContainingWithFewResults() {
		ReferenceList refResults = testBible.getReferencesContaining("spoken");
		assertEquals(2, refResults.size());
		assertTrue(refResults.contains(new Reference(BookOfBible.Jude, 1, 15)));
		assertTrue(refResults.contains(new Reference(BookOfBible.Jude, 1, 17)));
	}

	@Test (timeout=5000)
	public void testGetVerseContainingWithFewResults() {
		// First Test
		VerseList verseResults = testBible.getVersesContaining("beloved");
		for(Verse v : verseResults) {
			System.out.println(v);
		}
		assertEquals(12, verseResults.size());
		
		// Test for 3 that we know should be there.
		Verse v1=new Verse(BookOfBible.Jude, 1, 20,
				"But ye, beloved, building up yourselves on your most holy faith, praying in the Holy Ghost,");
		Verse v2 =new Verse(BookOfBible.Jude, 1, 17, "But, beloved, remember ye the words which were spoken "
				+ "before of the apostles of our Lord Jesus Christ;");
		Verse v3 = new Verse(BookOfBible.John3, 1, 2, "Beloved, I wish above all things that thou mayest "
				+ "prosper and be in health, even as thy soul prospereth.");
		
		assertTrue(verseResults.contains(v1));
		assertTrue(verseResults.contains(v2));
		assertTrue(verseResults.contains(v3));
	}

	@Test (timeout=5000)
	public void testGetVerseContainingWithManyResults() {
		VerseList verseResults = testBible.getVersesContaining("son oF GoD");
		assertEquals(7, verseResults.size());
		
		verseResults = testBible.getVersesContaining("righteousness");
		// Biblegateway.com gets 291 results.  If you don't pass this test, talk to me ASAP!
		assertEquals(5, verseResults.size());

		
		ReferenceList refResults = testBible.getReferencesContaining("ye have");
		assertEquals(10, refResults.size());
		
		// We'll test 3 known results--the first, 19th (arbitrary), and one in the middle.
		verseResults = testBible.getVersesContaining("the");
		assertEquals(142, verseResults.size());
		assertEquals(new Reference(BookOfBible.Genesis, 1, 1), verseResults.get(0).getReference());
		assertEquals(new Reference(BookOfBible.John1, 2, 2), verseResults.get(19).getReference());
		assertEquals(new Reference(BookOfBible.Revelation, 1, 1), verseResults.get(141).getReference());
	}

	@Test (timeout=5000)
	public void testGetVerseContainingWithPartialWords() {
		// right, light, righteous, etc.
		VerseList verseResults = testBible.getVersesContaining("ight");
		assertEquals(19, verseResults.size());
	}

	@Test (timeout=5000)
	public void testExtremeSearches() {
		// Empty string should return no results.
		VerseList verseResults = testBible.getVersesContaining("");
		assertEquals(0, verseResults.size());

		//Try it with getReferencesContaining as well.
		ReferenceList refResults = testBible.getReferencesContaining("");
		assertEquals(0, refResults.size());

		verseResults = testBible.getVersesContaining("the");
		assertEquals(142, verseResults.size());

		refResults = testBible.getReferencesContaining("the");
		assertEquals(142, refResults.size());
		
		// Space occurs in every verse.
		// Searches for ".", ",". etc. will be similar.
		// For now we won't worry about filtering these.
		// Our next version will take care of it.
		verseResults = testBible.getVersesContaining(" ");
		assertEquals(168, verseResults.size());
		
		refResults = testBible.getReferencesContaining(" ");
		assertEquals(168, refResults.size());
	}
	
	// ------------------------------------------------------------
		// The setup method.
		@Before
		public void setUp() throws Exception {
			sampleVerses = new VerseList("KJV", "Holy Bible, Authorized (King James) Version");
			sampleVerses.add(new Verse(new Reference(BookOfBible.Genesis, 1, 1),
					"In the beginning God created the heaven and the earth."));
			sampleVerses.add(new Verse(new Reference(BookOfBible.Ruth, 1, 1),
					"Now it came to pass in the days when the judges ruled, that there was a famine in the land. "
							+ "And a certain man of Bethlehemjudah went to sojourn in the country "
							+ "of Moab, he, and his wife, " + "and his two sons."));
			sampleVerses.add(new Verse(new Reference(BookOfBible.Ruth, 1, 2),
					"And the name of the man was Elimelech, and the name of his wife Naomi, "
							+ "and the name of his two sons Mahlon and Chilion, Ephrathites of Bethlehemjudah. "
							+ "And they came into the country of Moab, and continued there."));
			sampleVerses.add(new Verse(new Reference(BookOfBible.Ruth, 2, 1),
					"And Naomi had a kinsman of her husband's, a mighty man of wealth, "
							+ "of the family of Elimelech; and his name was Boaz."));
			sampleVerses.add(new Verse(new Reference(BookOfBible.Ruth, 2, 2),
					"And Ruth the Moabitess said unto Naomi, Let me now go to the field, "
							+ "and glean ears of corn after him in whose sight I shall find grace. "
							+ "And she said unto her, Go, my daughter."));
			sampleVerses.add(new Verse(new Reference(BookOfBible.John, 1, 1),
					"In the beginning was the Word, and the Word was with God, and the Word was God."));
			sampleVerses.add(new Verse(new Reference(BookOfBible.Ephesians, 3, 4),
					"Whereby, when ye read, ye may understand my knowledge in the mystery of Christ)"));
			sampleVerses.add(new Verse(new Reference(BookOfBible.Ephesians, 3, 5),
					"Which in other ages was not made known unto the sons of men, "
							+ "as it is now revealed unto his holy apostles and prophets by the Spirit;"));
			sampleVerses.add(new Verse(new Reference(BookOfBible.Ephesians, 3, 6),
					"That the Gentiles should be fellowheirs, and of the same body, "
							+ "and partakers of his promise in Christ by the gospel:"));
			sampleVerses.add(new Verse(new Reference(BookOfBible.Kings2, 3, 4),
					"And Mesha king of Moab was a sheepmaster, and rendered unto the king of Israel an hundred thousand lambs, "
							+ "and an hundred thousand rams, with the wool."));
			sampleVerses.add(new Verse(BookOfBible.John1, 1, 1,
					"That which was from the beginning, which we have heard, which we have seen with our eyes, "
							+ "which we have looked upon, and our hands have handled, of the Word of life;"));
			sampleVerses.add(new Verse(BookOfBible.John1, 1, 2,
					"(For the life was manifested, \"and we have seen it, and bear witness, and shew unto you that eternal life, "
							+ "which was with the Father, and was manifested unto us;)"));
			sampleVerses.add(new Verse(BookOfBible.John1, 1, 3,
					"That which we have seen and heard declare we unto you, that ye also may have fellowship with us,  "
							+ "and truly our fellowship is with the Father, and with his Son Jesus Christ."));
			sampleVerses.add(new Verse(BookOfBible.John1, 1, 4,
					"And these things write we unto you, that your joy may be full."));
			sampleVerses.add(new Verse(BookOfBible.John1, 1, 5,
					"This then is the message which we have heard of him, and declare unto you, that God is light, "
							+ "and in him is no darkness at all."));
			sampleVerses.add(new Verse(BookOfBible.John1, 1, 6,
					"If we say that we have fellowship with him, and walk in darkness, we lie, and do not the truth, "));
			sampleVerses.add(new Verse(BookOfBible.John1, 1, 7,
					"But if we walk in the light, as he is in the light, we have fellowship one with another, "
							+ "and the blood of Jesus Christ his Son cleanseth us from all sin."));
			sampleVerses.add(new Verse(BookOfBible.John1, 1, 8,
					"If we say that we have no sin, we deceive ourselves, and the truth is not in us."));
			sampleVerses.add(new Verse(BookOfBible.John1, 1, 9,
					"If we confess our sins, he is faithful and just to forgive us our sins, "
							+ "and to cleanse us from all unrighteousness."));
			sampleVerses.add(new Verse(BookOfBible.John1, 1, 10,
					"If we say that we have not sinned, we make him a liar, and his word is not in us."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 1,
					"My little children, these things write I unto you, that ye sin not. And if any man sin, "
							+ "we have an advocate with the Father, Jesus Christ the righteous, "));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 2, "And he is the propitiation for our sins,  "
					+ "and not for ours only, but also for the sins of the whole world."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 3,
					"And hereby we do know that we know him, if we keep his commandments."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 4,
					"He that saith, I know him, and keepeth not his commandments, "
							+ "is a liar, and the truth is not in him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 5,
					"But whoso keepeth his word, in him verily is the love of God perfected,  "
							+ "hereby know we that we are in him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 6,
					"He that saith he abideth in him ought himself also so to walk, even as he walked."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 7,
					"Brethren, I write no new commandment unto you, but an old commandment which ye had from the beginning. "
							+ "The old commandment is the word which ye have heard from the beginning."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 8,
					"Again, a new commandment I write unto you, which thing is true in him and in you,  "
							+ "because the darkness is past, and the true light now shineth."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 9,
					"He that saith he is in the light, and hateth his brother, is in darkness even until now."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 10,
					"He that loveth his brother abideth in the light, and there is none occasion of stumbling in him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 11,
					"But he that hateth his brother is in darkness, and walketh in darkness, and knoweth not whither he goeth, "
							+ "because that darkness hath blinded his eyes."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 12,
					"I write unto you, little children, because your sins are forgiven you for his name's sake."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 13,
					"I write unto you, fathers, because ye have known him that is from the beginning. "
							+ "I write unto you, young men, because ye have overcome the wicked one. I write unto you, "
							+ "little children, because ye have known the Father."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 14,
					"I have written unto you, fathers, because ye have known him that is from the beginning. "
							+ "I have written unto you, young men, because ye are strong, and the word of God abideth "
							+ "in you and ye have overcome the wicked one."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 15,
					"Love not the world, neither the things that are in the world. If any man love the world, "
							+ "the love of the Father is not in him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 16,
					"For all that is in the world, the lust of the flesh, and the lust of the eyes, and the pride of life, "
							+ "is not of the Father, but is of the world."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 17, "And the world passeth away, and the lust thereof,  "
					+ "but he that doeth the will of God abideth for ever."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 18,
					"Little children, it is the last time,  and as ye have heard that antichrist shall come, "
							+ "even now are there many antichrists; whereby we know that it is the last time."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 19,
					"They went out from us, but they were not of us; for if they had been of us, "
							+ "they would no doubt have continued with us,  but they went out, "
							+ "that they might be made manifest that they were not all of us."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 20,
					"But ye have an unction from the Holy One, and ye know all things."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 21,
					"I have not written unto you because ye know not the truth, "
							+ "but because ye know it, and that no lie is of the truth."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 22,
					"Who is a liar but he that denieth that Jesus is the Christ? "
							+ "He is antichrist, that denieth the Father and the Son."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 23,
					"Whosoever denieth the Son, the same hath not the Father,  "
							+ "he that acknowledgeth the Son hath the Father also."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 24,
					"Let that therefore abide in you, which ye have heard from the beginning. "
							+ "If that which ye have heard from the beginning shall remain in you, "
							+ "ye also shall continue in the Son, and in the Father."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 25,
					"And this is the promise that he hath promised us, even eternal life."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 26,
					"These things have I written unto you concerning them that seduce you."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 27,
					"But the anointing which ye have received of him abideth in you, "
							+ "and ye need not that any man teach you,  "
							+ "but as the same anointing teacheth you of all things, "
							+ "and is truth, and is no lie, and even as it hath taught you, ye shall abide in him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 28,
					"And now, little children, abide in him; that, when he shall appear, "
							+ "we may have confidence, and not be ashamed before him at his coming."));
			sampleVerses.add(new Verse(BookOfBible.John1, 2, 29,
					"If ye know that he is righteous, ye know that every one that doeth righteousness is born of him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 1,
					"Behold, what manner of love the Father hath bestowed upon us, "
							+ "that we should be called the sons of God,  "
							+ "therefore the world knoweth us not, because it knew him not."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 2,
					"Beloved, now are we the sons of God, and it doth not yet appear what we shall be,  "
							+ "but we know that, when he shall appear, "
							+ "we shall be like him; for we shall see him as he is."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 3,
					"And every man that hath this hope in him purifieth himself, even as he is pure."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 4,
					"Whosoever committeth sin transgresseth also the law,  for sin is the transgression of the law."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 5,
					"And ye know that he was manifested to take away our sins; and in him is no sin."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 6,
					"Whosoever abideth in him sinneth not,  whosoever sinneth hath not seen him, neither known him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 7, "Little children, let no man deceive you,  "
					+ "he that doeth righteousness is righteous, even as he is righteous."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 8,
					"He that committeth sin is of the devil; for the devil sinneth from the beginning. "
							+ "For this purpose the Son of God was manifested, "
							+ "that he might destroy the works of the devil."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 9,
					"Whosoever is born of God doth not commit sin; for his seed remaineth in him,  "
							+ "and he cannot sin, because he is born of God."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 10,
					"In this the children of God are manifest, and the children of the devil,  "
							+ "whosoever doeth not righteousness is not of God, neither he that loveth not his brother."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 11,
					"For this is the message that ye heard from the beginning, that we should love one another."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 12,
					"Not as Cain, who was of that wicked one, and slew his brother. "
							+ "And wherefore slew he him? Because his own works were evil, and his brother's righteous."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 13, "Marvel not, my brethren, if the world hate you."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 14, "We know that we have passed from death unto life, "
					+ "because we love the brethren. He that loveth not his brother abideth in death."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 15, "Whosoever hateth his brother is a murderer,  "
					+ "and ye know that no murderer hath eternal life abiding in him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 16, "Hereby perceive we the love of God, "
					+ "because he laid down his life for us,  and we ought to lay down our lives for the brethren."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 17,
					"But whoso hath this world's good, and seeth his brother have need, "
							+ "and shutteth up his bowels of compassion from him, how dwelleth the love of God in him?"));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 18,
					"My little children, let us not love in word, neither in tongue; but in deed and in truth."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 19,
					"And hereby we know that we are of the truth, and shall assure our hearts before him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 20,
					"For if our heart condemn us, God is greater than our heart, and knoweth all things."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 21,
					"Beloved, if our heart condemn us not, then have we confidence toward God."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 22, "And whatsoever we ask, we receive of him, "
					+ "because we keep his commandments, and do those things that are pleasing in his sight."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 23, "And this is his commandment, "
					+ "That we should believe on the name of his Son Jesus Christ, "
					+ "and love one another, as he gave us commandment."));
			sampleVerses.add(new Verse(BookOfBible.John1, 3, 24,
					"And he that keepeth his commandments dwelleth in him, and he in him. "
							+ "And hereby we know that he abideth in us, by the Spirit which he hath given us."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 1,
					"Beloved, believe not every spirit, but try the spirits whether they are of God,  "
							+ "because many false prophets are gone out into the world."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 2, "Hereby know ye the Spirit of God,  "
					+ "Every spirit that confesseth that Jesus Christ is come in the flesh is of God, "));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 3,
					"And every spirit that confesseth not that Jesus Christ is come in the flesh is not of God,  "
							+ "and this is that spirit of antichrist, whereof ye have heard that it should come; "
							+ "and even now already is it in the world."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 4, "Ye are of God, little children, and have overcome them,  "
					+ "because greater is he that is in you, than he that is in the world."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 5,
					"They are of the world,  therefore speak they of the world, " + "and the world heareth them."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 6, "We are of God,  he that knoweth God heareth us; "
					+ "he that is not of God heareth not us. "
					+ "Hereby know we the spirit of truth, and the spirit of error."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 7, "Beloved, let us love one another,  for love is of God; "
					+ "and every one that loveth is born of God, and knoweth God."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 8, "He that loveth not knoweth not God; for God is love."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 9, "In this was manifested the love of God toward us, "
					+ "because that God sent his only begotten Son into the world, that we might live through him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 10,
					"Herein is love, not that we loved God, but that he loved us, "
							+ "and sent his Son to be the propitiation for our sins."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 11,
					"Beloved, if God so loved us, we ought also to love one another."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 12,
					"No man hath seen God at any time. If we love one another, "
							+ "God dwelleth in us, and his love is perfected in us."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 13,
					"Hereby know we that we dwell in him, and he in us, because he hath given us of his Spirit."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 14,
					"And we have seen and do testify that the Father sent the Son to be the Saviour of the world."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 15,
					"Whosoever shall confess that Jesus is the Son of God, God dwelleth in him, and he in God."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 16,
					"And we have known and believed the love that God hath to us. "
							+ "God is love; and he that dwelleth in love dwelleth in God, and God in him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 17, "Herein is our love made perfect, "
					+ "that we may have boldness in the day of judgment,  because as he is, so are we in this world."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 18,
					"There is no fear in love; but perfect love casteth out fear,  "
							+ "because fear hath torment. He that feareth is not made perfect in love."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 19, "We love him, because he first loved us."));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 20,
					"If a man say, I love God, and hateth his brother, he is a liar,  "
							+ "for he that loveth not his brother whom he hath seen, "
							+ "how can he love God whom he hath not seen?"));
			sampleVerses.add(new Verse(BookOfBible.John1, 4, 21,
					"And this commandment have we from him, That he who loveth God love his brother also."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 1,
					"Whosoever believeth that Jesus is the Christ is born of God,  "
							+ "and every one that loveth him that begat loveth him also that is begotten of him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 2,
					"By this we know that we love the children of God, when we love God, and keep his commandments."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 3,
					"For this is the love of God, that we keep his commandments,  and his commandments are not grievous."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 4, "For whatsoever is born of God overcometh the world,  "
					+ "and this is the victory that overcometh the world, even our faith."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 5,
					"Who is he that overcometh the world, but he that believeth that Jesus is the Son of God?"));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 6,
					"This is he that came by water and blood, even Jesus Christ; "
							+ "not by water only, but by water and blood. "
							+ "And it is the Spirit that beareth witness, because the Spirit is truth."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 7, "For there are three that bear record in heaven, "
					+ "the Father, the Word, and the Holy Ghost,  and these three are one."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 8,
					"And there are three that bear witness in earth, the Spirit, "
							+ "and the water, and the blood,  and these three agree in one."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 9,
					"If we receive the witness of men, the witness of God is greater,  "
							+ "for this is the witness of God which he hath testified of his Son."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 10,
					"He that believeth on the Son of God hath the witness in himself,  "
							+ "he that believeth not God hath made him a liar; "
							+ "because he believeth not the record that God gave of his Son."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 11,
					"And this is the record, that God hath given to us eternal life, and this life is in his Son."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 12,
					"He that hath the Son hath life; and he that hath not the Son of God hath not life."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 13,
					"These things have I written unto you that believe on the name of the Son of God; "
							+ "that ye may know that ye have eternal life, "
							+ "and that ye may believe on the name of the Son of God."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 14, "And this is the confidence that we have in him, that, "
					+ "if we ask any thing according to his will, he heareth us, "));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 15, "And if we know that he hear us, whatsoever we ask, "
					+ "we know that we have the petitions that we desired of him."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 16,
					"If any man see his brother sin a sin which is not unto death, "
							+ "he shall ask, and he shall give him life for them that sin not unto death. "
							+ "There is a sin unto death,  I do not say that he shall pray for it."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 17,
					"All unrighteousness is sin,  and there is a sin not unto death."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 18, "We know that whosoever is born of God sinneth not; "
					+ "but he that is begotten of God keepeth himself, and that wicked one toucheth him not."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 19,
					"And we know that we are of God, and the whole world lieth in wickedness."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 20,
					"And we know that the Son of God is come, and hath given us an understanding, "
							+ "that we may know him that is true, and we are in him that is true, "
							+ "even in his Son Jesus Christ. " + "This is the true God, and eternal life."));
			sampleVerses.add(new Verse(BookOfBible.John1, 5, 21, "Little children, keep yourselves from idols. Amen."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 1,
					"The elder unto the elect lady and her children, whom I love in the truth; "
							+ "and not I only, but also all they that have known the truth;"));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 2,
					"For the truth's sake, which dwelleth in us, and shall be with us for ever."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 3,
					"Grace be with you, mercy, and peace, from God the Father, "
							+ "and from the Lord Jesus Christ, the Son of the Father, in truth and love."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 4,
					"I rejoiced greatly that I found of thy children walking in truth, "
							+ "as we have received a commandment from the Father."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 5,
					"And now I beseech thee, lady, not as though I wrote a new commandment unto thee, "
							+ "but that which we had from the beginning, that we love one another."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 6, "And this is love, that we walk after his commandments. "
					+ "This is the commandment, That, as ye have heard from the beginning, ye should walk in it."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 7,
					"For many deceivers are entered into the world, who confess "
							+ "not that Jesus Christ is come in the flesh. This is a deceiver and an antichrist."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 8,
					"Look to yourselves, that we lose not those things which we have wrought, "
							+ "but that we receive a full reward."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 9,
					"Whosoever transgresseth, and abideth not in the doctrine of Christ, "
							+ "hath not God. He that abideth in the doctrine of Christ, "
							+ "he hath both the Father and the Son."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 10,
					"If there come any unto you, and bring not this doctrine, "
							+ "receive him not into your house, neither bid him God speed, "));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 11,
					"For he that biddeth him God speed is partaker of his evil deeds."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 12, "Having many things to write unto you, "
					+ "I would not write with paper and ink,  but I trust to come unto you, "
					+ "and speak face to face, that our joy may be full."));
			sampleVerses.add(new Verse(BookOfBible.John2, 1, 13, "The children of thy elect sister greet thee. Amen."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 1,
					"The elder unto the wellbeloved Gaius, whom I love in the truth."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 2, "Beloved, I wish above all things that thou mayest "
					+ "prosper and be in health, even as thy soul prospereth."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 3,
					"For I rejoiced greatly, when the brethren came and testified "
							+ "of the truth that is in thee, even as thou walkest in the truth."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 4,
					"I have no greater joy than to hear that my children walk in truth."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 5,
					"Beloved, thou doest faithfully whatsoever thou doest to the brethren, and to strangers;"));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 6,
					"Which have borne witness of thy charity before the church,  "
							+ "whom if thou bring forward on their journey after a godly sort, thou shalt do well, "));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 7,
					"Because that for his name's sake they went forth, taking nothing of the Gentiles."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 8,
					"We therefore ought to receive such, that we might be fellowhelpers to the truth."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 9, "I wrote unto the church,  but Diotrephes, "
					+ "who loveth to have the preeminence among them, receiveth us not."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 10,
					"Wherefore, if I come, I will remember his deeds which he doeth, "
							+ "prating against us with malicious words,  and not content therewith, "
							+ "neither doth he himself receive the brethren, and forbiddeth them that would, "
							+ "and casteth them out of the church."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 11,
					"Beloved, follow not that which is evil, but that which is good. "
							+ "He that doeth good is of God,  but he that doeth evil hath not seen God."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 12,
					"Demetrius hath good report of all men, and of the truth itself,  "
							+ "yea, and we also bear record; and ye know that our record is true."));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 13,
					"I had many things to write, but I will not with ink and pen write unto thee, "));
			sampleVerses.add(new Verse(BookOfBible.John3, 1, 14, "But I trust I shall shortly see thee, "
					+ "and we shall speak face to face. Peace be to thee. "
					+ "Our friends salute thee. Greet the friends by name."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 1, "Jude, the servant of Jesus Christ, and brother of James, "
					+ "to them that are sanctified by God the Father, and preserved in Jesus Christ, and called, "));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 2, "Mercy unto you, and peace, and love, be multiplied."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 3,
					"Beloved, when I gave all diligence to write unto you of the common salvation, "
							+ "it was needful for me to write unto you, and exhort you that ye should earnestly "
							+ "contend for the faith which was once delivered unto the saints."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 4, "For there are certain men crept in unawares, "
					+ "who were before of old ordained to this condemnation, ungodly men, "
					+ "turning the grace of our God into lasciviousness, "
					+ "and denying the only Lord God, and our Lord Jesus Christ."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 5,
					"I will therefore put you in remembrance, though ye once knew this, "
							+ "how that the Lord, having saved the people out of the land of Egypt, "
							+ "afterward destroyed them that believed not."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 6,
					"And the angels which kept not their first estate, but left their own habitation, "
							+ "he hath reserved in everlasting chains under darkness unto the judgment of the great day."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 7,
					"Even as Sodom and Gomorrha, and the cities about them in like manner, "
							+ "giving themselves over to fornication, and going after strange flesh, "
							+ "are set forth for an example, suffering the vengeance of eternal fire."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 8, "Likewise also these filthy dreamers defile the flesh, "
					+ "despise dominion, and speak evil of dignities."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 9, "Yet Michael the archangel, when contending with the devil "
					+ "he disputed about the body of Moses, "
					+ "durst not bring against him a railing accusation, but said, The Lord rebuke thee."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 10,
					"But these speak evil of those things which they know not, "
							+ " but what they know naturally, as brute beasts, in those things they corrupt themselves."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 11, "Woe unto them! for they have gone in the way of Cain, "
					+ "and ran greedily after the error of Balaam for reward, and perished in the gainsaying of Core."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 12, "These are spots in your feasts of charity, "
					+ "when they feast with you, feeding themselves without fear,  "
					+ "clouds they are without water, carried about of winds; trees whose fruit withereth, "
					+ "without fruit, twice dead, plucked up by the roots;"));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 13, "Raging waves of the sea, foaming out their own shame; "
					+ "wandering stars, to whom is reserved the blackness of darkness for ever."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 14, "And Enoch also, the seventh from Adam, "
					+ "prophesied of these, saying, Behold, the Lord cometh with ten thousands of his saints,"));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 15,
					"To execute judgment upon all, and to convince all that are ungodly among "
							+ "them of all their ungodly deeds which they have ungodly committed, and of all their"
							+ " hard speeches which ungodly sinners have spoken against him."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 16,
					"These are murmurers, complainers, walking after their own lusts; "
							+ "and their mouth speaketh great swelling words, "
							+ "having men's persons in admiration because of advantage."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 17, "But, beloved, remember ye the words which were spoken "
					+ "before of the apostles of our Lord Jesus Christ;"));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 18,
					"How that they told you there should be mockers in the last time, "
							+ "who should walk after their own ungodly lusts."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 19,
					"These be they who separate themselves, sensual, having not the Spirit."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 20,
					"But ye, beloved, building up yourselves on your most holy faith, praying in the Holy Ghost,"));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 21,
					"Keep yourselves in the love of God, looking for the mercy "
							+ "of our Lord Jesus Christ unto eternal life."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 22, "And of some have compassion, making a difference, "));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 23, "And others save with fear, pulling them out of the fire; "
					+ "hating even the garment spotted by the flesh."));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 24, "Now unto him that is able to keep you from falling, "
					+ "and to present you faultless before the presence of his glory with exceeding joy,"));
			sampleVerses.add(new Verse(BookOfBible.Jude, 1, 25, "To the only wise God our Saviour, be glory and majesty, "
					+ "dominion and power, both now and ever. Amen."));
			sampleVerses.add(new Verse(new Reference(BookOfBible.Revelation, 1, 1),
					"The Revelation of Jesus Christ, which God gave unto him, "
							+ "to shew unto his servants things which must shortly come to pass; "
							+ "and he sent and signified it by his angel unto his servant John:"));

			// Make a shallow copy of the verses.
			ArrayList<Verse> copyOfList = new ArrayList<Verse>(sampleVerses);
			copyOfVerseList = new VerseList(sampleVerses.getVersion(), sampleVerses.getDescription(),
					copyOfList);
			testBible = new ArrayListBible(copyOfVerseList);
			
		}
		
}
