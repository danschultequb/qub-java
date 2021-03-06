package qub;

public interface StringsTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Strings.class, () ->
        {
            runner.testGroup("iterable(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertEqual(Iterable.create(), Strings.iterable((String)null));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertEqual(Iterable.create(), Strings.iterable(""));
                });

                runner.test("with one character", (Test test) ->
                {
                    test.assertEqual(Iterable.create('a'), Strings.iterable("a"));
                });

                runner.test("with two characters", (Test test) ->
                {
                    test.assertEqual(Iterable.create('a', 'b'), Strings.iterable("ab"));
                });
            });

            runner.testGroup("endsWith(String,char)", () ->
            {
                final Action3<String,String,Throwable> endsWithErrorTest = (String text, String suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> Strings.endsWith(text, suffix), expected);
                    });
                };

                endsWithErrorTest.run(null, "b", new PreConditionFailure("text cannot be null."));

                final Action3<String,Character,Boolean> endsWithTest = (String text, Character suffix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.endsWith(text, suffix));
                    });
                };

                endsWithTest.run("", 'b', false);
                endsWithTest.run("a", 'b', false);
                endsWithTest.run("b", 'b', true);
                endsWithTest.run("ab", 'b', true);
            });

            runner.testGroup("endsWith(String,String)", () ->
            {
                final Action3<String,String,Throwable> endsWithErrorTest = (String text, String suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> Strings.endsWith(text, suffix), expected);
                    });
                };

                endsWithErrorTest.run(null, "b", new PreConditionFailure("text cannot be null."));
                endsWithErrorTest.run("a", null, new PreConditionFailure("suffix cannot be null."));
                endsWithErrorTest.run("a", "", new PreConditionFailure("suffix cannot be empty."));

                final Action3<String,String,Boolean> endsWithTest = (String text, String suffix, Boolean expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.endsWith(text, suffix));
                    });
                };

                endsWithTest.run("", "b", false);
                endsWithTest.run("a", "b", false);
                endsWithTest.run("b", "b", true);
                endsWithTest.run("ab", "b", true);
                endsWithTest.run("ab", "bc", false);
                endsWithTest.run("ac", "bc", false);
                endsWithTest.run("abc", "bc", true);
            });

            runner.testGroup("ensureEndsWith(String,char)", () ->
            {
                final Action3<String,Character,Throwable> endsWithErrorTest = (String text, Character suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> Strings.ensureEndsWith(text, suffix), expected);
                    });
                };

                endsWithErrorTest.run(null, 'b', new PreConditionFailure("text cannot be null."));

                final Action3<String,Character,String> ensureEndsWithTest = (String text, Character suffix, String expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.ensureEndsWith(text, suffix));
                    });
                };

                ensureEndsWithTest.run("", 'b', "b");
                ensureEndsWithTest.run("a", 'b', "ab");
                ensureEndsWithTest.run("b", 'b', "b");
                ensureEndsWithTest.run("ab", 'b', "ab");
            });

            runner.testGroup("ensureEndsWith(String,String)", () ->
            {
                final Action3<String,String,Throwable> endsWithErrorTest = (String text, String suffix, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertThrows(() -> Strings.ensureEndsWith(text, suffix), expected);
                    });
                };

                endsWithErrorTest.run(null, "b", new PreConditionFailure("text cannot be null."));
                endsWithErrorTest.run("a", null, new PreConditionFailure("suffix cannot be null."));
                endsWithErrorTest.run("a", "", new PreConditionFailure("suffix cannot be empty."));

                final Action3<String,String,String> ensureEndsWithTest = (String text, String suffix, String expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(text, suffix).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.ensureEndsWith(text, suffix));
                    });
                };

                ensureEndsWithTest.run("", "b", "b");
                ensureEndsWithTest.run("a", "b", "ab");
                ensureEndsWithTest.run("b", "b", "b");
                ensureEndsWithTest.run("ab", "b", "ab");
                ensureEndsWithTest.run("ab", "bc", "abbc");
                ensureEndsWithTest.run("ac", "bc", "acbc");
                ensureEndsWithTest.run("abc", "bc", "abc");
            });

            runner.testGroup("containsAny(String,char[])", () ->
            {
                final Action3<String,char[],Boolean> containsAnyTest = (String text, char[] characters, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text) + " and " + (characters == null ? "null" : Array.create(characters).toString()), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.containsAny(text, characters));
                    });
                };

                containsAnyTest.run(null, null, false);
                containsAnyTest.run(null, new char[0], false);
                containsAnyTest.run(null, new char[] { 'a' }, false);

                containsAnyTest.run("", null, false);
                containsAnyTest.run("", new char[0], false);
                containsAnyTest.run("", new char[] { 'a' }, false);

                containsAnyTest.run("apples", null, false);
                containsAnyTest.run("apples", new char[0], false);
                containsAnyTest.run("apples", new char[] { 'b' }, false);
                containsAnyTest.run("apples", new char[] { 'a' }, true);
            });

            runner.testGroup("escape(String)", () ->
            {
                final Action2<String,String> escapeTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.escape(text));
                    });
                };

                escapeTest.run(null, null);
                escapeTest.run("", "");
                escapeTest.run("abc", "abc");
                escapeTest.run("\b\f\n\r\t'\"", "\\b\\f\\n\\r\\t'\\\"");
            });

            runner.testGroup("unescape(String)", () ->
            {
                final Action2<String,String> unescapeTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.unescape(text));
                    });
                };

                unescapeTest.run(null, null);
                unescapeTest.run("", "");
                unescapeTest.run("abc", "abc");
                unescapeTest.run("\b\f\n\r\t'\"", "\b\f\n\r\t'\"");
                unescapeTest.run("\\b\\f\\n\\r\\t'\\\"", "\b\f\n\r\t'\"");
            });

            runner.testGroup("isQuoted(String)", () ->
            {
                final Action2<String,Boolean> isQuotedTest = (String text, Boolean expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.isQuoted(text));
                    });
                };

                isQuotedTest.run(null, false);
                isQuotedTest.run("", false);
                isQuotedTest.run("\"\"", true);
                isQuotedTest.run("''", true);
                isQuotedTest.run("``", false);
                isQuotedTest.run("hello", false);
                isQuotedTest.run("Todd's", false);
                isQuotedTest.run("\"hey", false);
                isQuotedTest.run("hey\"", false);
                isQuotedTest.run("\"hey'", false);
                isQuotedTest.run("\"hey\"", true);
                isQuotedTest.run("'hey\"", false);
                isQuotedTest.run("'hey'", true);
                isQuotedTest.run("\"hey\\\"", false);
                isQuotedTest.run("\"hey\\\\\"", true);
                isQuotedTest.run("\"hey\\\\\\\"", false);
                isQuotedTest.run("\\\"hey\"", false);
            });

            runner.testGroup("unquote(String)", () ->
            {
                final Action2<String,String> unquoteTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.unquote(text));
                    });
                };

                unquoteTest.run(null, null);
                unquoteTest.run("", "");
                unquoteTest.run("\"\"", "");
                unquoteTest.run("''", "");
                unquoteTest.run("``", "``");
                unquoteTest.run("hello", "hello");
                unquoteTest.run("Todd's", "Todd's");
                unquoteTest.run("\"hey", "\"hey");
                unquoteTest.run("hey\"", "hey\"");
                unquoteTest.run("\"hey'", "\"hey'");
                unquoteTest.run("\"hey\"", "hey");
                unquoteTest.run("'hey\"", "'hey\"");
                unquoteTest.run("'hey'", "hey");
                unquoteTest.run("\"hey\\\"", "\"hey\\\"");
                unquoteTest.run("\"hey\\\\\"", "hey\\\\");
                unquoteTest.run("\"hey\\\\\\\"", "\"hey\\\\\\\"");
                unquoteTest.run("\\\"hey\"", "\\\"hey\"");
            });

            runner.testGroup("quote(String)", () ->
            {
                final Action2<String,String> quoteTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.quote(text));
                    });
                };

                quoteTest.run(null, null);
                quoteTest.run("", "\"\"");
                quoteTest.run("abc", "\"abc\"");
                quoteTest.run("\"", "\"\"\"");
            });

            runner.testGroup("escapeAndQuote(String)", () ->
            {
                final Action2<String,String> escapeAndQuoteTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.escapeAndQuote(text));
                    });
                };

                escapeAndQuoteTest.run(null, null);
                escapeAndQuoteTest.run("", "\"\"");
                escapeAndQuoteTest.run("abc", "\"abc\"");
                escapeAndQuoteTest.run("\b\f\n\r\t", "\"\\b\\f\\n\\r\\t\"");
                escapeAndQuoteTest.run("\"", "\"\\\"\"");
            });

            runner.testGroup("unescapeAndUnquote(String)", () ->
            {
                final Action2<String,String> unescapeAndUnquoteTest = (String text, String expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.unescapeAndUnquote(text));
                    });
                };

                unescapeAndUnquoteTest.run(null, null);
                unescapeAndUnquoteTest.run("", "");
                unescapeAndUnquoteTest.run("\"\"", "");
                unescapeAndUnquoteTest.run("abc", "abc");
                unescapeAndUnquoteTest.run("\"abc\"", "abc");
                unescapeAndUnquoteTest.run("\b\f\n\r\t", "\b\f\n\r\t");
                unescapeAndUnquoteTest.run("\"\\b\\f\\n\\r\\t\"", "\b\f\n\r\t");
                unescapeAndUnquoteTest.run("\"", "\"");
                unescapeAndUnquoteTest.run("\"\\\"\"", "\"");
            });

            runner.testGroup("repeat(String,int)", () ->
            {
                runner.test("with \"\" and 10", (Test test) ->
                {
                    test.assertEqual("", Strings.repeat("", 10));
                });

                runner.test("with \"a\" and -1", (Test test) ->
                {
                    test.assertThrows(() -> Strings.repeat("a", -1), new PreConditionFailure("repetitions (-1) must be greater than or equal to 0."));
                });

                runner.test("with \"a\" and 0", (Test test) ->
                {
                    test.assertEqual("", Strings.repeat("a", 0));
                });

                runner.test("with \"a\" and 1", (Test test) ->
                {
                    test.assertEqual("a", Strings.repeat("a", 1));
                });

                runner.test("with \"a\" and 2", (Test test) ->
                {
                    test.assertEqual("aa", Strings.repeat("a", 2));
                });

                runner.test("with \"a\" and 3", (Test test) ->
                {
                    test.assertEqual("aaa", Strings.repeat("a", 3));
                });
                runner.test("with \"ab\" and 1", (Test test) ->
                {
                    test.assertEqual("ab", Strings.repeat("ab", 1));
                });

                runner.test("with \"ab\" and 2", (Test test) ->
                {
                    test.assertEqual("abab", Strings.repeat("ab", 2));
                });

                runner.test("with \"ab\" and 3", (Test test) ->
                {
                    test.assertEqual("ababab", Strings.repeat("ab", 3));
                });
            });

            runner.testGroup("join(java.lang.Iterable<String>)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Strings.join(null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertEqual("", Strings.join(Iterable.create()));
                });

                runner.test("with non-empty values", (Test test) ->
                {
                    test.assertEqual("abc", Strings.join(Iterable.create("a", "b", "c")));
                });
            });

            runner.testGroup("join(char,java.lang.Iterable<String>)", () ->
            {
                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Strings.join('+', null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertEqual("", Strings.join('-', Iterable.create()));
                });

                runner.test("with non-empty values", (Test test) ->
                {
                    test.assertEqual("a+b+c", Strings.join('+', Iterable.create("a", "b", "c")));
                });
            });

            runner.testGroup("join(String,java.lang.Iterable<String>)", () ->
            {
                runner.test("with null separator", (Test test) ->
                {
                    test.assertThrows(() -> Strings.join(null, Iterable.create()),
                        new PreConditionFailure("separator cannot be null."));
                });

                runner.test("with null values", (Test test) ->
                {
                    test.assertThrows(() -> Strings.join("test", null),
                        new PreConditionFailure("values cannot be null."));
                });

                runner.test("with empty values", (Test test) ->
                {
                    test.assertEqual("", Strings.join(" - ", Iterable.create()));
                });

                runner.test("with non-empty values", (Test test) ->
                {
                    test.assertEqual("a and b and c", Strings.join(" and ", Iterable.create("a", "b", "c")));
                });
            });

            runner.testGroup("padLeft()", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    test.assertEqual("aa", Strings.padLeft(null, 2, 'a'));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertEqual("aa", Strings.padLeft("", 2, 'a'));
                });

                runner.test("with non-empty value smaller than the minimum length", (Test test) ->
                {
                    test.assertEqual("zzzab", Strings.padLeft("ab", 5, 'z'));
                });

                runner.test("with non-empty value equal to the minimum length", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padLeft("abc", 3, 'z'));
                });

                runner.test("with non-empty value greater than the minimum length", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padLeft("abc", 1, 'z'));
                });

                runner.test("with negative minimumLength", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padLeft("abc", -1, 'z'));
                });

                runner.test("with zero minimumLength", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padLeft("abc", 0, 'z'));
                });
            });

            runner.testGroup("padRight()", () ->
            {
                runner.test("with null value", (Test test) ->
                {
                    test.assertEqual("aa", Strings.padRight(null, 2, 'a'));
                });

                runner.test("with empty value", (Test test) ->
                {
                    test.assertEqual("aa", Strings.padRight("", 2, 'a'));
                });

                runner.test("with non-empty value smaller than the minimum length", (Test test) ->
                {
                    test.assertEqual("abzzz", Strings.padRight("ab", 5, 'z'));
                });

                runner.test("with non-empty value equal to the minimum length", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padRight("abc", 3, 'z'));
                });

                runner.test("with non-empty value greater than the minimum length", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padRight("abc", 1, 'z'));
                });

                runner.test("with negative minimumLength", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padRight("abc", -1, 'z'));
                });

                runner.test("with zero minimumLength", (Test test) ->
                {
                    test.assertEqual("abc", Strings.padRight("abc", 0, 'z'));
                });
            });

            runner.testGroup("isOneOf(String,String...)", () ->
            {
                runner.test("with null and null String[]", (Test test) ->
                {
                    test.assertThrows(() -> Strings.isOneOf(null, (String[])null), new PreConditionFailure("values cannot be null."));
                });

                runner.test("with null and null String", (Test test) ->
                {
                    test.assertTrue(Strings.isOneOf(null, (String)null));
                });
            });

            runner.testGroup("getWords(String)", () ->
            {
                final Action2<String,Iterable<String>> getWordsTest = (String value, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.getWords(value));
                    });
                };

                getWordsTest.run(null, Iterable.create());
                getWordsTest.run("", Iterable.create());
                getWordsTest.run("     ", Iterable.create());
                getWordsTest.run("./\\\"*", Iterable.create());
                getWordsTest.run("a", Iterable.create("a"));
                getWordsTest.run("abc", Iterable.create("abc"));
                getWordsTest.run("a.a", Iterable.create("a"));
                getWordsTest.run("Disposable.create()", Iterable.create("Disposable", "create"));
                getWordsTest.run("a a", Iterable.create("a"));
                getWordsTest.run("a b", Iterable.create("a", "b"));
                getWordsTest.run("a1 b", Iterable.create("a1", "b"));
            });

            runner.testGroup("getLines(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Strings.getLines(null), new PreConditionFailure("value cannot be null."));
                });

                final Action2<String,Iterable<String>> getLinesTest = (String value, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.getLines(value));
                    });
                };

                getLinesTest.run("", Iterable.create());
                getLinesTest.run("   ", Iterable.create("   "));
                getLinesTest.run("abcd", Iterable.create("abcd"));
                getLinesTest.run("\n\n\n", Iterable.create("", "", ""));
                getLinesTest.run("\r\n\n\r", Iterable.create("", "", "\r"));
                getLinesTest.run("a\nb\r\nc\rd", Iterable.create("a", "b", "c\rd"));
            });

            runner.testGroup("getLines(String,boolean) with includeNewLineCharacters set to false", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Strings.getLines(null), new PreConditionFailure("value cannot be null."));
                });

                final Action2<String,Iterable<String>> getLinesTest = (String value, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.getLines(value, false));
                    });
                };

                getLinesTest.run("", Iterable.create());
                getLinesTest.run("   ", Iterable.create("   "));
                getLinesTest.run("abcd", Iterable.create("abcd"));
                getLinesTest.run("\n\n\n", Iterable.create("", "", ""));
                getLinesTest.run("\r\n\n\r", Iterable.create("", "", "\r"));
                getLinesTest.run("a\nb\r\nc\rd", Iterable.create("a", "b", "c\rd"));
            });

            runner.testGroup("getLines(String,boolean) with includeNewLineCharacters set to true", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Strings.getLines(null), new PreConditionFailure("value cannot be null."));
                });

                final Action2<String,Iterable<String>> getLinesTest = (String value, Iterable<String> expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(value), (Test test) ->
                    {
                        test.assertEqual(expected, Strings.getLines(value, true));
                    });
                };

                getLinesTest.run("", Iterable.create());
                getLinesTest.run("   ", Iterable.create("   "));
                getLinesTest.run("abcd", Iterable.create("abcd"));
                getLinesTest.run("\n\n\n", Iterable.create("\n", "\n", "\n"));
                getLinesTest.run("\r\n\n\r", Iterable.create("\r\n", "\n", "\r"));
                getLinesTest.run("a\nb\r\nc\rd", Iterable.create("a\n", "b\r\n", "c\rd"));
            });
        });
    }
}
