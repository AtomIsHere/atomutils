package com.github.atomishere.atomutils.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bukkit.ChatColor;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * A collection of useful methods I've used when creating plugins.
 */
public class GeneralUtils {
    /**
     * Parse a string into a boolean. Detects invalids.
     *
     * @param bool string to be parsed.
     * @return parsed string.
     * @throws IllegalArgumentException if string is invalid.
     */
    public static Boolean parseBoolean(String bool) throws IllegalArgumentException {
        if(bool.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        } else if(bool.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        } else {
            throw new IllegalArgumentException("Invalid arguments for parsing boolean");
        }
    }

    /**
     * Hash a string to md5.
     *
     * @param string string to hash.
     * @return Hashed string.
     */
    public static String getMd5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(string.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            StringBuilder hashText = new StringBuilder(no.toString(16));
            while(hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch(NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Check if a string is valid md5.
     *
     * @param s string to check.
     * @return result.
     */
    public static boolean isValidMD5(String s) {
        return s.matches("^[a-fA-F0-9]{32}$");
    }

    /**
     * Hides text in lore through colour codes, useful for adding data to items.
     *
     * (Credit to spigot user IAlIstannen)
     *
     * @see #revealText(String)
     * @param text text to hide.
     * @return hidden text to use in lore.
     */
    public static String hideText(String text) {
        Objects.requireNonNull(text, "text can not be null!");

        StringBuilder output = new StringBuilder();

        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        String hex = Hex.encodeHexString(bytes);

        for (char c : hex.toCharArray()) {
            output.append(ChatColor.COLOR_CHAR).append(c);
        }

        return output.toString();
    }

    /**
     * Reveal hidden text.
     *
     * @see #hideText(String)
     * @param text hidden text.
     * @return The unhidden forum of the text.
     */
    public static String revealText(String text) {
        Objects.requireNonNull(text, "text can not be null!");

        if (text.isEmpty()) {
            return text;
        }

        char[] chars = text.toCharArray();

        char[] hexChars = new char[chars.length / 2];

        IntStream.range(0, chars.length)
                .filter(value -> value % 2 != 0)
                .forEach(value -> hexChars[value / 2] = chars[value]);

        try {
            return new String(Hex.decodeHex(hexChars), StandardCharsets.UTF_8);
        } catch (DecoderException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Couldn't decode text", e);
        }
    }
}
