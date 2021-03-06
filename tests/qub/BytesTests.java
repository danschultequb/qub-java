package qub;

public interface BytesTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Bytes.class, () ->
        {
            runner.testGroup("toUnsignedInt(byte)", () ->
            {
                final Action2<Integer,Integer> toUnsignedIntTest = (Integer byteValue, Integer expectedUnsignedInteger) ->
                {
                    runner.test("with " + byteValue, (Test test) ->
                    {
                        test.assertEqual(expectedUnsignedInteger, Bytes.toUnsignedInt(byteValue.byteValue()));
                    });
                };

                toUnsignedIntTest.run(0, 0);
                toUnsignedIntTest.run(1, 1);
                toUnsignedIntTest.run(127, 127);
                toUnsignedIntTest.run(128, 128);
                toUnsignedIntTest.run(129, 129);
                toUnsignedIntTest.run(254, 254);
                toUnsignedIntTest.run(255, 255);
                toUnsignedIntTest.run(256, 0);
                toUnsignedIntTest.run(-1, 255);
                toUnsignedIntTest.run(-2, 254);
                toUnsignedIntTest.run(-126, 130);
                toUnsignedIntTest.run(-127, 129);
                toUnsignedIntTest.run(-128, 128);
                toUnsignedIntTest.run(-129, 127);
            });

            runner.testGroup("getLeadingOneBits(byte)", () ->
            {
                final Action2<Byte,Integer> getLeadingOneBitsTest = (Byte b, Integer expected) ->
                {
                    runner.test("with " + b, (Test test) ->
                    {
                        test.assertEqual(expected, Bytes.getLeadingOneBits(b));
                    });
                };

                for (int i = 0; i < 128; ++i)
                {
                    getLeadingOneBitsTest.run((byte)i, 0);
                }

                for (int i = 128; i < 192; ++i)
                {
                    getLeadingOneBitsTest.run((byte)i, 1);
                }

                for (int i = 192; i < 224; ++i)
                {
                    getLeadingOneBitsTest.run((byte)i, 2);
                }

                for (int i = 224; i < 240; ++i)
                {
                    getLeadingOneBitsTest.run((byte)i, 3);
                }

                for (int i = 240; i < 248; ++i)
                {
                    getLeadingOneBitsTest.run((byte)i, 4);
                }

                for (int i = 248; i < 252; ++i)
                {
                    getLeadingOneBitsTest.run((byte)i, 5);
                }

                for (int i = 252; i < 254; ++i)
                {
                    getLeadingOneBitsTest.run((byte)i, 6);
                }

                getLeadingOneBitsTest.run((byte)254, 7);
                getLeadingOneBitsTest.run((byte)255, 8);
            });

            runner.testGroup("toHexChar(int)", () ->
            {
                final Action1<Integer> toHexCharFailureTest = (Integer value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertThrows(() -> Bytes.toHexChar(value),
                            new PreConditionFailure("value (" + value + ") must be between 0 and 15."));
                    });
                };

                toHexCharFailureTest.run(-2);
                toHexCharFailureTest.run(-1);
                toHexCharFailureTest.run(16);
                toHexCharFailureTest.run(17);

                final Action2<Integer,Character> toHexCharTest = (Integer value, Character expected) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expected, Bytes.toHexChar(value));
                    });
                };

                for (int i = 0; i <= 9; ++i)
                {
                    toHexCharTest.run(i, (char)('0' + i));
                }
                for (int i = 10; i <= 15; ++i)
                {
                    toHexCharTest.run(i, (char)('A' + i - 10));
                }
            });

            runner.testGroup("toHexChar(long)", () ->
            {
                final Action1<Integer> toHexCharFailureTest = (Integer value) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertThrows(() -> Bytes.toHexChar(value.longValue()),
                            new PreConditionFailure("value (" + value + ") must be between 0 and 15."));
                    });
                };

                toHexCharFailureTest.run(-2);
                toHexCharFailureTest.run(-1);
                toHexCharFailureTest.run(16);
                toHexCharFailureTest.run(17);

                final Action2<Integer,Character> toHexCharTest = (Integer value, Character expected) ->
                {
                    runner.test("with " + value, (Test test) ->
                    {
                        test.assertEqual(expected, Bytes.toHexChar(value.longValue()));
                    });
                };

                for (int i = 0; i <= 9; ++i)
                {
                    toHexCharTest.run(i, (char)('0' + i));
                }
                for (int i = 10; i <= 15; ++i)
                {
                    toHexCharTest.run(i, (char)('A' + i - 10));
                }
            });

            runner.testGroup("getBit(byte,int)", () ->
            {
                final Action3<Byte,Integer,Throwable> getBitErrorTest = (Byte value, Integer bitIndex, Throwable expected) ->
                {
                    runner.test("with " + English.andList(value + " (" + Bytes.toHexString(value, true) + ")", bitIndex), (Test test) ->
                    {
                        test.assertThrows(() -> Bytes.getBit(value, bitIndex), expected);
                    });
                };

                getBitErrorTest.run((byte)55, -1, new PreConditionFailure("bitIndex (-1) must be between 0 and 7."));
                getBitErrorTest.run((byte)55, 8, new PreConditionFailure("bitIndex (8) must be between 0 and 7."));

                final Action3<Byte,Integer,Integer> getBitTest = (Byte value, Integer bitIndex, Integer expected) ->
                {
                    runner.test("with " + English.andList(value + " (" + Bytes.toHexString(value, true) + ")", bitIndex), (Test test) ->
                    {
                        test.assertEqual(expected, Bytes.getBit(value, bitIndex));
                    });
                };

                for (int i = 0; i < Bytes.bitCount; ++i)
                {
                    getBitTest.run((byte)0x00, i, 0);
                }

                for (int i = 0; i < Bytes.bitCount; ++i)
                {
                    getBitTest.run((byte)0x01, i, (i == 7 ? 1 : 0));
                }

                for (int i = 0; i < Bytes.bitCount; ++i)
                {
                    getBitTest.run((byte)0x02, i, (i == 6 ? 1 : 0));
                }

                for (int i = 0; i < Bytes.bitCount; ++i)
                {
                    getBitTest.run((byte)0x80, i, (i == 0 ? 1 : 0));
                }

                for (int i = 0; i < Bytes.bitCount; ++i)
                {
                    getBitTest.run((byte)0xFF, i, 1);
                }
            });
        });
    }
}
