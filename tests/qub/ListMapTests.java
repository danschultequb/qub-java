package qub;

public interface ListMapTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(ListMap.class, () ->
        {
            MutableMapTests.test(runner, ListMap::create, true, true);
        });
    }
}
