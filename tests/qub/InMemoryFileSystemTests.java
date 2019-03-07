package qub;

public class InMemoryFileSystemTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(InMemoryFileSystem.class, () ->
        {
            FileSystemTests.test(runner, (AsyncRunner asyncRunner) ->
            {
                final InMemoryFileSystem fileSystem = new InMemoryFileSystem(asyncRunner);
                fileSystem.createRoot("/");
                return fileSystem;
            });

            runner.testGroup("setFileCanDelete(String,boolean)", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    test.assertError(new FileNotFoundException("C:\\folder\\file.bmp"), fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                });
                
                runner.test("when parent folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    test.assertError(new FileNotFoundException("C:\\folder\\file.bmp"), fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                });
                
                runner.test("when file doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    fileSystem.createFolder("C:\\folder");
                    test.assertError(new FileNotFoundException("C:/folder/file.bmp"), fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true));
                });
                
                runner.test("when file exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    fileSystem.createFolder("C:\\folder");
                    fileSystem.createFile("C:\\folder\\file.bmp");
                    fileSystem.setFileCanDelete("C:\\folder\\file.bmp", true).awaitError();
                });
            });

            runner.testGroup("deleteFile(String)", () ->
            {
                runner.test("when file cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/");
                    fileSystem.createFile("Z:/file.png");
                    fileSystem.setFileCanDelete("Z:/file.png", false).awaitError();
                    test.assertError(new FileNotFoundException("Z:/file.png"), fileSystem.deleteFile("Z:/file.png"));
                    test.assertSuccess(true, fileSystem.fileExists("Z:/file.png"));
                });
            });

            runner.testGroup("setFolderCanDelete(String,boolean)", () ->
            {
                runner.test("when root doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    test.assertError(new FolderNotFoundException("C:\\folder\\file.bmp"), fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                });

                runner.test("when parent folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    test.assertError(new FolderNotFoundException("C:\\folder\\file.bmp"), fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                });

                runner.test("when folder doesn't exist", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    fileSystem.createFolder("C:\\folder");
                    test.assertError(new FolderNotFoundException("C:\\folder\\file.bmp"), fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true));
                });

                runner.test("when folder exists", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("C:\\");
                    fileSystem.createFolder("C:\\folder\\file.bmp");
                    fileSystem.setFolderCanDelete("C:\\folder\\file.bmp", true).awaitError();
                });
            });

            runner.testGroup("deleteFolder(String)", () ->
            {
                runner.test("when folder cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/");
                    fileSystem.createFolder("Z:/file.png");
                    fileSystem.setFolderCanDelete("Z:/file.png", false).awaitError();
                    test.assertError(new FolderNotFoundException("Z:/file.png"), fileSystem.deleteFolder("Z:/file.png"));
                    test.assertSuccess(true, fileSystem.folderExists("Z:/file.png"));
                });

                runner.test("when child file cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/");
                    fileSystem.createFolder("Z:/file.png");
                    fileSystem.createFile("Z:/file.png/notme");
                    fileSystem.setFileCanDelete("Z:/file.png/notme", false);
                    test.assertError(new FolderNotFoundException("Z:/file.png"), fileSystem.deleteFolder("Z:/file.png"));
                    test.assertSuccess(true, fileSystem.folderExists("Z:/file.png"));
                });

                runner.test("when child folder cannot be deleted", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getMainAsyncRunner());
                    fileSystem.createRoot("Z:/");
                    fileSystem.createFolder("Z:/file.png");
                    fileSystem.createFolder("Z:/file.png/notme");
                    fileSystem.setFolderCanDelete("Z:/file.png/notme", false);
                    test.assertError(new FolderNotFoundException("Z:/file.png"), fileSystem.deleteFolder("Z:/file.png"));
                    test.assertSuccess(true, fileSystem.folderExists("Z:/file.png"));
                });
            });
        });
    }
}
