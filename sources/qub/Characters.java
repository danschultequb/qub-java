package qub;

public class Characters
{
    /**
     * The minimum value that a character can have.
     */
    public static final char minimumValue = java.lang.Character.MIN_VALUE;

    /**
     * The maximum value that a character can have.
     */
    public static final char maximumValue = java.lang.Character.MAX_VALUE;

    /**
     * Get the Range of characters that includes all characters.
     */
    public static final Range<Character> all = Range.between(minimumValue, maximumValue);

    /**
     * Get whether or not the provided character is a quote character.
     * @param character The character to check.
     * @return Whether or not the provided character is a quote character.
     */
    public static boolean isQuote(char character)
    {
        return character == '\'' || character == '\"';
    }

    /**
     * Surround the provided character with single quotes and textualize any escaped characters.
     * @param character The character to quote and escape.
     * @return The quoted and escaped character.
     */
    public static String escapeAndQuote(char character)
    {
        return Strings.quote(Characters.escape(character));
    }

    /**
     * Escape the provided character if it is an escaped character (such as '\n' or '\t').
     * @param character The character to escape.
     * @return The escaped character.
     */
    public static String escape(char character)
    {
        String result;
        switch (character)
        {
            case '\b':
                result = "\\b";
                break;

            case '\f':
                result = "\\f";
                break;

            case '\n':
                result = "\\n";
                break;

            case '\r':
                result = "\\r";
                break;

            case '\t':
                result = "\\t";
                break;

            case '\'':
                result = "\\\'";
                break;

            case '\"':
                result = "\\\"";
                break;

            default:
                result = java.lang.Character.toString(character);
                break;
        }
        return result;
    }

    /**
     * Surround the provided character with single quotes.
     * @param character The character to quote.
     * @return The quoted text.
     */
    public static String quote(char character)
    {
        return Strings.quote(java.lang.Character.toString(character));
    }

    /**
     * Get the lower-cased version of the provided character.
     * @param value The character.
     * @return The lower-cased version of the provided character.
     */
    public static char toLowerCase(char value)
    {
        return Character.toLowerCase(value);
    }
}
