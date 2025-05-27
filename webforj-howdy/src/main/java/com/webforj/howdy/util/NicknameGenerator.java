package com.webforj.howdy.util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The NicknameGenerator class provides functionality to generate unique nicknames
 * by combining random adjectives, nouns, and numeric identifiers. It ensures that
 * the generated nicknames are unique by maintaining a record of previous generations.
 *
 * Using a local class because I don't want to introduce a dependency on a third-party.
 * Of course, I know there are plenty of libraries out there that do this. ;-)
 */
public class NicknameGenerator {

    // Prevent instantiation
    private NicknameGenerator() {}

    /** Array of predefined adjectives used in nickname generation. */
    private static final String[] ADJECTIVES = {
        "Brave", "Happy", "Clever", "Mighty", "Witty", "Sunny", "Zesty", "Lucky", "Chill", "Swift",
        "Fuzzy", "Silly", "Snappy", "Jolly", "Peppy", "Bouncy", "Cheery", "Sassy", "Zany", "Giddy",
        "Perky", "Spunky", "Nifty", "Feisty", "Groovy", "Peachy", "Dandy", "Jazzy", "Nimble", "Bubbly"
    };

    /** Array of predefined nouns used in nickname generation. */
    private static final String[] NOUNS = {
        "Panda", "Falcon", "Wizard", "Ninja", "Koala", "Otter", "Dragon", "Unicorn", "Sailor", "Guitar",
        "Rocket", "Turtle", "Cactus", "Cloud", "Comet", "Yeti", "Phoenix", "Sloth", "Pineapple", "Octopus",
        "Marble", "Dolphin", "Chameleon", "Robot", "Llama", "Walrus", "Parrot", "Zebra", "Squirrel", "Tiger"
    };

    /** Set to keep track of previously generated nicknames to ensure uniqueness. */
    private static final Set<String> usedNicknames = new HashSet<>();

    /**
     * Generates a unique nickname by combining a random adjective, noun, and number.
     * The method ensures that the generated nickname has not been previously used.
     *
     * @return A unique nickname string in the format [Adjective][Noun][Number]
     * @throws NicknameGenerationException if unable to generate a unique nickname after 1000 attempts
     */
    public static String generateUniqueNickname() throws NicknameGenerationException {
        int maxAttempts = 1000;

        for (int i = 0; i < maxAttempts; i++) {
            String nickname = generateNickname();
            if (!usedNicknames.contains(nickname)) {
                usedNicknames.add(nickname);
                return nickname;
            }
        }

        throw new NicknameGenerationException("Nickname pool exhausted or too many collisions.");
    }

    /**
     * Generates a nickname by randomly combining an adjective and a noun from predefined
     * arrays, followed by a three-digit number.
     *
     * @return A string representing a randomly generated nickname in the format [Adjective][Noun][Number]
     */
    private static String generateNickname() {
        String adjective = ADJECTIVES[ThreadLocalRandom.current().nextInt(ADJECTIVES.length)];
        String noun = NOUNS[ThreadLocalRandom.current().nextInt(NOUNS.length)];
        int number = ThreadLocalRandom.current().nextInt(100, 999); // optional suffix
        return adjective + noun + number;
    }
}
