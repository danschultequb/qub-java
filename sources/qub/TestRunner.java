package qub;

/**
 * An object that can run tests and test groups.
 */
public interface TestRunner
{
    /**
     * A method that returns a Skip. This method is really used as a flag to skip a TestGroup or
     * a Test.
     * @return The Skip marker.
     */
    Skip skip();

    /**
     * A method that returns a Skip. This method is really used as a flag to skip a TestGroup or
     * a Test.
     * @param message The message to display for why the test or test group is being skipped.
     * @return The Skip marker.
     */
    Skip skip(String message);

    /**
     * Create a new test group with the provided name and action.
     * @param testGroupName The name of the test group.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    void testGroup(String testGroupName, Action0 testGroupAction);

    /**
     * Create a new test group with the name of the provided class and the provided action.
     * @param testClass The class that this test group will be testing.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    void testGroup(Class<?> testClass, Action0 testGroupAction);

    /**
     * Create a new test group with the provided name and action that will be skipped during
     * execution.
     * @param testGroupName The name of the test group.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    void testGroup(String testGroupName, Skip skip, Action0 testGroupAction);

    /**
     * Create a new test group with the name of the provided class and the provided action that will
     * be skipped during execution..
     * @param testClass The class that this test group will be testing.
     * @param testGroupAction The action that should be run to run the tests of the test group.
     */
    void testGroup(Class<?> testClass, Skip skip, Action0 testGroupAction);

    /**
     * Run the test with the provided name and action.
     * @param testName The name of the test.
     * @param testAction The action for the test.
     */
    void test(String testName, Action1<Test> testAction);

    /**
     * Skip the test with the provided name and action.
     * @param testName The name of the test.
     * @param testAction The action for the test.
     */
    void test(String testName, Skip skip, Action1<Test> testAction);

    /**
     * Set an action that will be run before each test within this test group.
     * @param beforeTestAction The action that will be run before each test within this test group.
     */
    void beforeTest(Action0 beforeTestAction);

    /**
     * Set an action that will be run after each test within this test group.
     * @param afterTestAction The action that will be run after each test within this test group.
     */
    void afterTest(Action0 afterTestAction);
}
