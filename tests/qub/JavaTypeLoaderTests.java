package qub;

public interface JavaTypeLoaderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(JavaTypeLoader.class, () ->
        {
            TypeLoaderTests.test(runner, JavaTypeLoader::create);

            runner.testGroup("getTypeContainerPathString(String)", () ->
            {
                runner.test("with type that exists",
                    (TestResources resources) -> Tuple.create(resources.getFileSystem()),
                    (Test test, FileSystem fileSystem) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    final String typeContainerPath = typeLoader.getTypeContainerPathString("qub.TypeLoader").await();
                    test.assertNotNullAndNotEmpty(typeContainerPath);

                    if (typeContainerPath.endsWith("/") || typeContainerPath.endsWith("\\"))
                    {
                        test.assertTrue(fileSystem.folderExists(typeContainerPath).await());
                    }
                    else
                    {
                        test.assertTrue(fileSystem.fileExists(typeContainerPath).await());
                    }
                });

                runner.test("with " + Strings.escapeAndQuote(String.class), (Test test) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    test.assertThrows(() -> typeLoader.getTypeContainerPathString("java.lang.String").await(),
                        new NotFoundException("Could not find a type container for a type named \"java.lang.String\"."));
                });
            });

            runner.testGroup("getTypeContainerPathString(Class<?>)", () ->
            {
                runner.test("with type that exists",
                    (TestResources resources) -> Tuple.create(resources.getFileSystem()),
                    (Test test, FileSystem fileSystem) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    final String typeContainerPath = typeLoader.getTypeContainerPathString(TypeLoader.class).await();
                    test.assertNotNullAndNotEmpty(typeContainerPath);

                    if (typeContainerPath.endsWith("/") || typeContainerPath.endsWith("\\"))
                    {
                        test.assertTrue(fileSystem.folderExists(typeContainerPath).await());
                    }
                    else
                    {
                        test.assertTrue(fileSystem.fileExists(typeContainerPath).await());
                    }
                });

                runner.test("with " + Strings.escapeAndQuote(String.class), (Test test) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    test.assertThrows(() -> typeLoader.getTypeContainerPathString(String.class).await(),
                        new NotFoundException("Could not find a type container for a type named \"java.lang.String\"."));
                });
            });

            runner.testGroup("getTypeContainerPath(String)", () ->
            {
                runner.test("with type that exists",
                    (TestResources resources) -> Tuple.create(resources.getFileSystem()),
                    (Test test, FileSystem fileSystem) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    final Path typeContainerPath = typeLoader.getTypeContainerPath("qub.TypeLoader").await();
                    test.assertNotNull(typeContainerPath);
                    test.assertTrue(typeContainerPath.isRooted());

                    if (typeContainerPath.endsWith("/") || typeContainerPath.endsWith("\\"))
                    {
                        test.assertTrue(fileSystem.folderExists(typeContainerPath).await());
                    }
                    else
                    {
                        test.assertTrue(fileSystem.fileExists(typeContainerPath).await());
                    }
                });
            });

            runner.testGroup("getTypeContainerPath(Class<?>)", () ->
            {
                runner.test("with type that exists",
                    (TestResources resources) -> Tuple.create(resources.getFileSystem()),
                    (Test test, FileSystem fileSystem) ->
                {
                    final JavaTypeLoader typeLoader = JavaTypeLoader.create();
                    final Path typeContainerPath = typeLoader.getTypeContainerPath(TypeLoader.class).await();
                    test.assertNotNull(typeContainerPath);
                    test.assertTrue(typeContainerPath.isRooted());

                    if (typeContainerPath.endsWith("/") || typeContainerPath.endsWith("\\"))
                    {
                        test.assertTrue(fileSystem.folderExists(typeContainerPath).await());
                    }
                    else
                    {
                        test.assertTrue(fileSystem.fileExists(typeContainerPath).await());
                    }
                });
            });
        });
    }
}
