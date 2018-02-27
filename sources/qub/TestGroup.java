package qub;

public class TestGroup
{
    private final String name;
    private final TestGroup parentTestGroup;

    public TestGroup(String name, TestGroup parentTestGroup)
    {
        this.name = name;
        this.parentTestGroup = parentTestGroup;
    }

    public String getName()
    {
        return name;
    }

    public TestGroup getParentTestGroup()
    {
        return parentTestGroup;
    }

    public String getFullName()
    {
        return parentTestGroup == null ? name : parentTestGroup.getFullName() + ' ' + name;
    }

    public boolean matches(PathPattern testPattern)
    {
        return testPattern == null ||
            testPattern.isMatch(getName()) ||
            testPattern.isMatch(getFullName()) ||
            (parentTestGroup != null && parentTestGroup.matches(testPattern));
    }
}