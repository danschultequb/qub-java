package qub;

public class FileSystemTests
{
    public static void test(TestRunner runner, Function1<AsyncRunner,FileSystem> creator)
    {
        runner.testGroup(FileSystem.class, () ->
        {
            runner.testGroup("rootExists(String)", () ->
            {
                final Action3<String,Boolean,Throwable> rootExistsAsyncTest = (String rootPath, Boolean expectedValue, Throwable expectedError) ->
                {
                    runner.test("with " + (rootPath == null ? null : "\"" + rootPath + "\""), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<Boolean> result = fileSystem.rootExists(rootPath);
                        test.assertNotNull(result);

                        test.assertEqual(expectedValue, result.getValue());
                        test.assertEqual(expectedError, result.getError());
                    });
                };

                rootExistsAsyncTest.run(null, null, new IllegalArgumentException("rootPath cannot be null."));
                rootExistsAsyncTest.run("", null, new IllegalArgumentException("rootPath cannot be null."));
                rootExistsAsyncTest.run("notme:\\", false, null);
                rootExistsAsyncTest.run("/", true, null);
            });

            runner.testGroup("rootExists(Path)", () ->
            {
                final Action3<String,Boolean,Throwable> rootExistsTest = (String rootPath, Boolean expectedValue, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(rootPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<Boolean> result = fileSystem.rootExists(Path.parse(rootPath));
                        test.assertNotNull(result);

                        test.assertEqual(expectedValue, result.getValue());
                        test.assertEqual(expectedError, result.getError());
                    });
                };

                rootExistsTest.run(null, null, new IllegalArgumentException("rootPath cannot be null."));
                rootExistsTest.run("", null, new IllegalArgumentException("rootPath cannot be null."));
                rootExistsTest.run("notme:\\", false, null);
                rootExistsTest.run("/", true, null);
            });

            runner.test("getRoot()", (Test test) ->
            {
                FileSystem fileSystem = creator.run(test.getParallelAsyncRunner());
                final Result<Root> rootResult = fileSystem.getRoot("/daffy/");
                test.assertNotNull(rootResult);

                final Root root = rootResult.getValue();
                test.assertNotNull(root);
                test.assertEqual("/", root.toString());
            });

            runner.test("getRoots()", (Test test) ->
            {
                final FileSystem fileSystem = creator.run(test.getParallelAsyncRunner());
                final Result<Iterable<Root>> rootsResult = fileSystem.getRoots();
                test.assertSuccess(rootsResult);
                test.assertTrue(rootsResult.getValue().any());
            });

            runner.testGroup("getFilesAndFolders(String)", () ->
            {
                final Action4<String, Action1<FileSystem>, String[], Throwable> getFilesAndFoldersTest = (String folderPath, Action1<FileSystem> setup, String[] expectedEntryPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFolders(folderPath);
                        test.assertNotNull(result);

                        if (expectedEntryPaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Array.fromValues(expectedEntryPaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertEqual(expectedError.getClass(), result.getErrorType());
                            test.assertEqual(expectedError.getMessage(), result.getErrorMessage());
                        }
                    });
                };

                getFilesAndFoldersTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFilesAndFoldersTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFilesAndFoldersTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                    },
                    new String[0],
                    null);
                getFilesAndFoldersTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB");
                        fileSystem.createFile("/file1.txt");
                        fileSystem.createFile("/folderA/file2.csv");
                    },
                    new String[] { "/folderA", "/file1.txt" },
                    null);
            });

            runner.testGroup("getFilesAndFolders(Path)", () ->
            {
                final Action4<String, Action1<FileSystem>, String[], Throwable> getFilesAndFoldersTest = (String folderPath, Action1<FileSystem> setup, String[] expectedEntryPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFolders(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedEntryPaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Array.fromValues(expectedEntryPaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFilesAndFoldersTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFilesAndFoldersTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFilesAndFoldersTest.run(
                    "/folderA",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA");
                    },
                    new String[0],
                    null);
                getFilesAndFoldersTest.run(
                    "/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folderA/folderB");
                        fileSystem.createFile("/file1.txt");
                        fileSystem.createFile("/folderA/file2.csv");
                    },
                    new String[] { "/folderA", "/file1.txt" },
                    null);
            });

            runner.testGroup("getFolders(String)", () ->
            {
                final Action2<String,Throwable> getFoldersTest = (String path, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(path), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        test.assertError(expectedError, fileSystem.getFolders(path));
                    });
                };

                getFoldersTest.run(null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFoldersTest.run("", new IllegalArgumentException("rootedFolderPath cannot be null."));
            });

            runner.testGroup("getFoldersRecursively(String)", () ->
            {
                final Action4<String,Action1<FileSystem>,String[],Throwable> getFoldersRecursivelyTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFolderPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<Folder>> result = fileSystem.getFoldersRecursively(folderPath);
                        test.assertNotNull(result);

                        if (expectedFolderPaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Array.fromValues(expectedFolderPaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFoldersRecursivelyTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFoldersRecursivelyTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFoldersRecursivelyTest.run("test/folder", null, null, new IllegalArgumentException("rootedFolderPath must be rooted."));
                getFoldersRecursivelyTest.run("F:/test/folder", null, null, new FolderNotFoundException("F:/test/folder"));
                getFoldersRecursivelyTest.run("/test/folder", null, null, new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/");
                    },
                    null,
                    new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder/1.txt");
                        fileSystem.createFolder("/test/folder/2.txt");
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" },
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                        fileSystem.createFile("/test/folder/A/3.csv");
                        fileSystem.createFile("/test/folder/B/C/4.xml");
                        fileSystem.createFile("/test/folder/A/5.png");
                    },
                    new String[] { "/test/folder/A", "/test/folder/B", "/test/folder/B/C" },
                    null);
            });

            runner.testGroup("getFoldersRecursively(Path)", () ->
            {
                final Action4<String,Action1<FileSystem>,String[],Throwable> getFoldersRecursivelyTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFolderPaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<Folder>> result = fileSystem.getFoldersRecursively(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedFolderPaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Array.fromValues(expectedFolderPaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFoldersRecursivelyTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFoldersRecursivelyTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFoldersRecursivelyTest.run("test/folder", null, null, new IllegalArgumentException("rootedFolderPath must be rooted."));
                getFoldersRecursivelyTest.run("F:/test/folder", null, null, new FolderNotFoundException("F:/test/folder"));
                getFoldersRecursivelyTest.run("/test/folder", null, null, new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/");
                    },
                    null,
                    new FolderNotFoundException("/test/folder"));
                getFoldersRecursivelyTest.run(
                    "/test/folder/",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                    },
                    new String[0],
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder/1.txt");
                        fileSystem.createFolder("/test/folder/2.txt");
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" },
                    null);
                getFoldersRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                        fileSystem.createFile("/test/folder/A/3.csv");
                        fileSystem.createFile("/test/folder/B/C/4.xml");
                        fileSystem.createFile("/test/folder/A/5.png");
                    },
                    new String[] { "/test/folder/A", "/test/folder/B", "/test/folder/B/C" },
                    null);
            });

            runner.testGroup("getFiles(String)", () ->
            {
                final Action4<String,Action1<FileSystem>,String[],Throwable> getFilesTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<File>> result = fileSystem.getFiles(folderPath);
                        test.assertNotNull(result);

                        if (expectedFilePaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Array.fromValues(expectedFilePaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFilesTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFilesTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
            });

            runner.testGroup("getFiles(Path)", () ->
            {
                final Action4<String,Action1<FileSystem>,String[],Throwable> getFilesTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<File>> result = fileSystem.getFiles(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedFilePaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Array.fromValues(expectedFilePaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFilesTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFilesTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
            });

            runner.testGroup("getFilesRecursively(String)", () ->
            {
                final Action4<String,Action1<FileSystem>,String[],Throwable> getFilesRecursivelyTest = (String folderPath, Action1<FileSystem> setup, String[] expectedFilePaths, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Iterable<File>> result = fileSystem.getFilesRecursively(folderPath);
                        test.assertNotNull(result);

                        if (expectedFilePaths == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(Array.fromValues(expectedFilePaths), result.getValue().map(FileSystemEntry::toString));
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                getFilesRecursivelyTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFilesRecursivelyTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                getFilesRecursivelyTest.run("test/folder", null, null, new IllegalArgumentException("rootedFolderPath must be rooted."));
                getFilesRecursivelyTest.run("F:/test/folder", null, null, new FolderNotFoundException("F:/test/folder"));
                getFilesRecursivelyTest.run("/test/folder", null, null, new FolderNotFoundException("/test/folder"));
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/test"),
                    null,
                    new FolderNotFoundException("/test/folder"));
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/test/folder"),
                    new String[0],
                    null);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                    },
                    new String[] { "/test/folder/1.txt", "/test/folder/2.txt" },
                    null);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/test/folder");
                        fileSystem.createFolder("/test/folder/1.txt");
                        fileSystem.createFolder("/test/folder/2.txt");
                    },
                    new String[0],
                    null);
                getFilesRecursivelyTest.run(
                    "/test/folder",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFile("/test/folder/1.txt");
                        fileSystem.createFile("/test/folder/2.txt");
                        fileSystem.createFile("/test/folder/A/3.csv");
                        fileSystem.createFile("/test/folder/B/C/4.xml");
                        fileSystem.createFile("/test/folder/A/5.png");
                    },
                    new String[]
                    {
                        "/test/folder/1.txt",
                        "/test/folder/2.txt",
                        "/test/folder/A/3.csv",
                        "/test/folder/A/5.png",
                        "/test/folder/B/C/4.xml"
                    },
                    null);
            });

            runner.testGroup("getFolder(String)", () ->
            {
                final Action2<String,Boolean> getFolderTest = (String folderPath, Boolean folderExpected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Folder folder = fileSystem.getFolder(folderPath).getValue();
                        if (folderExpected)
                        {
                            test.assertNotNull(folder);
                            test.assertEqual(folderPath, folder.toString());
                        }
                        else
                        {
                            test.assertNull(folder);
                        }
                    });
                };

                getFolderTest.run(null, false);
                getFolderTest.run("", false);
                getFolderTest.run("a/b/c", false);
                getFolderTest.run("/", true);
                getFolderTest.run("\\", true);
                getFolderTest.run("Z:\\", true);
                getFolderTest.run("/a/b", true);
            });

            runner.testGroup("folderExists(String)", () ->
            {
                final Action4<String,Action1<FileSystem>,Boolean,Throwable> folderExistsTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Boolean> result = fileSystem.folderExists(folderPath);
                        test.assertNotNull(result);

                        if (expectedFolderExists == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(expectedFolderExists, result.getValue());
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                folderExistsTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                folderExistsTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                folderExistsTest.run("/", null, true, null);
                folderExistsTest.run("/folderName", null, false, null);
                folderExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    true,
                    null);
            });

            runner.testGroup("folderExists(Path)", () ->
            {
                final Action4<String,Action1<FileSystem>,Boolean,Throwable> folderExistsTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedFolderExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Boolean> result = fileSystem.folderExists(Path.parse(folderPath));
                        test.assertNotNull(result);

                        if (expectedFolderExists == null)
                        {
                            test.assertNull(result.getValue());
                        }
                        else
                        {
                            test.assertEqual(expectedFolderExists, result.getValue());
                        }

                        if (expectedError == null)
                        {
                            test.assertNull(result.getError());
                        }
                        else
                        {
                            test.assertError(expectedError, result);
                        }
                    });
                };

                folderExistsTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                folderExistsTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                folderExistsTest.run("/", null, true, null);
                folderExistsTest.run("/folderName", null, false, null);
                folderExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    true,
                    null);
            });

            runner.testGroup("createFolder(String)", () ->
            {
                final Action5<String,String,Action1<FileSystem>,String,Throwable> createFolderTest = (String testName, String folderPath, Action1<FileSystem> setup, String expectedCreatedFolderPath, Throwable expectedError) ->
                {
                    runner.test(testName + " (" + folderPath + ")", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Folder> result = fileSystem.createFolder(folderPath);
                        test.assertNotNull(result);

                        test.assertEqual(expectedCreatedFolderPath, result.getValue() == null ? null : result.getValue().toString());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                createFolderTest.run("with null", null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                createFolderTest.run("with empty string", "", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                createFolderTest.run("with relative path", "folder", null, null, new IllegalArgumentException("rootedFolderPath must be rooted."));
                createFolderTest.run("with rooted path that doesn't exist", "/folder", null, "/folder", null);
                createFolderTest.run(
                    "with rooted path that already exists",
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    "/folder",
                    new FolderAlreadyExistsException("/folder"));
            });

            runner.testGroup("createFolder(Path)", () ->
            {
                final Action5<String,String,Action1<FileSystem>,String,Throwable> createFolderTest = (String testName, String folderPath, Action1<FileSystem> setup, String expectedCreatedFolderPath, Throwable expectedError) ->
                {
                    runner.test(testName + " (" + folderPath + ")", (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Folder> result = fileSystem.createFolder(Path.parse(folderPath));
                        test.assertNotNull(result);

                        test.assertEqual(expectedCreatedFolderPath, result.getValue() == null ? null : result.getValue().toString());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                createFolderTest.run("with null", null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                createFolderTest.run("with empty string", "", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                createFolderTest.run("with relative path", "folder", null, null, new IllegalArgumentException("rootedFolderPath must be rooted."));
                createFolderTest.run("with rooted path that doesn't exist", "/folder", null, "/folder", null);
                createFolderTest.run(
                    "with rooted path that already exists",
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    "/folder",
                    new FolderAlreadyExistsException("/folder"));
            });

            runner.testGroup("deleteFolder(String)", () ->
            {
                final Action4<String,Action1<FileSystem>,Boolean,Throwable> deleteFolderTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedDeleteResult, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Boolean> result = fileSystem.deleteFolder(folderPath);
                        test.assertNotNull(result);

                        test.assertEqual(expectedDeleteResult, result.getValue());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                deleteFolderTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                deleteFolderTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                deleteFolderTest.run("folder", null, null, new IllegalArgumentException("rootedFolderPath must be rooted."));
                deleteFolderTest.run("/folder", null, false, new FolderNotFoundException("/folder"));
                deleteFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    true,
                    null);
                deleteFolderTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a");
                        fileSystem.createFolder("/folder/b");
                        fileSystem.createFolder("/folder/c");
                    },
                    true,
                    null);
            });

            runner.testGroup("deleteFolder(Path)", () ->
            {
                final Action4<String,Action1<FileSystem>,Boolean,Throwable> deleteFolderTest = (String folderPath, Action1<FileSystem> setup, Boolean expectedDeleteResult, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(folderPath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }
                        final Result<Boolean> result = fileSystem.deleteFolder(Path.parse(folderPath));
                        test.assertNotNull(result);

                        test.assertEqual(expectedDeleteResult, result.getValue());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                deleteFolderTest.run(null, null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                deleteFolderTest.run("", null, null, new IllegalArgumentException("rootedFolderPath cannot be null."));
                deleteFolderTest.run("folder", null, null, new IllegalArgumentException("rootedFolderPath must be rooted."));
                deleteFolderTest.run("/folder", null, false, new FolderNotFoundException("/folder"));
                deleteFolderTest.run(
                    "/folder",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folder"),
                    true,
                    null);
                deleteFolderTest.run(
                    "/folder/c",
                    (FileSystem fileSystem) ->
                    {
                        fileSystem.createFolder("/folder/a");
                        fileSystem.createFolder("/folder/b");
                        fileSystem.createFolder("/folder/c");
                    },
                    true,
                    null);
            });

            runner.testGroup("getFile(String)", () ->
            {
                final Action3<String,String,Throwable> getFileTest = (String filePath, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<File> file = fileSystem.getFile(filePath);
                        test.assertNotNull(file);
                        test.assertEqual(expectedFilePath, file.getValue() == null ? null : file.getValue().toString());
                        test.assertEqual(expectedError, file.getError());
                    });
                };

                getFileTest.run(null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                getFileTest.run("", null, new IllegalArgumentException("rootedFilePath cannot be null."));
                getFileTest.run("a/b/c", null, new IllegalArgumentException("rootedFilePath must be rooted."));
                getFileTest.run("/", null, new IllegalArgumentException("rootedFilePath cannot end with '/'."));
                getFileTest.run("\\", null, new IllegalArgumentException("rootedFilePath cannot end with '\\'."));
                getFileTest.run("Z:\\", null, new IllegalArgumentException("rootedFilePath cannot end with '\\'."));
                getFileTest.run("/a/b", "/a/b", null);
            });

            runner.testGroup("getFile(Path)", () ->
            {
                final Action3<String,String,Throwable> getFileTest = (String filePath, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        final Result<File> file = fileSystem.getFile(Path.parse(filePath));
                        test.assertNotNull(file);
                        test.assertEqual(expectedFilePath, file.getValue() == null ? null : file.getValue().toString());
                        test.assertEqual(expectedError, file.getError());
                    });
                };

                getFileTest.run(null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                getFileTest.run("", null, new IllegalArgumentException("rootedFilePath cannot be null."));
                getFileTest.run("a/b/c", null, new IllegalArgumentException("rootedFilePath must be rooted."));
                getFileTest.run("/", null, new IllegalArgumentException("rootedFilePath cannot end with '/'."));
                getFileTest.run("\\", null, new IllegalArgumentException("rootedFilePath cannot end with '\\'."));
                getFileTest.run("Z:\\", null, new IllegalArgumentException("rootedFilePath cannot end with '\\'."));
                getFileTest.run("/a/b", "/a/b", null);
            });

            runner.testGroup("fileExists(String)", () ->
            {
                final Action4<String,Action1<FileSystem>,Boolean,Throwable> fileExistsTest = (String filePath, Action1<FileSystem> setup, Boolean expectedFileExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Boolean> result = fileSystem.fileExists(filePath);
                        test.assertNotNull(result);

                        test.assertEqual(expectedFileExists, result.getValue());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                fileExistsTest.run(null, null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                fileExistsTest.run("", null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                fileExistsTest.run("blah", null, null, new IllegalArgumentException("rootedFilePath must be rooted."));
                fileExistsTest.run("/", null, null, new IllegalArgumentException("rootedFilePath cannot end with '/'."));
                fileExistsTest.run("\\", null, null, new IllegalArgumentException("rootedFilePath cannot end with '\\'."));
                fileExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    false,
                    null);
                fileExistsTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName"),
                    true,
                    null);
            });

            runner.testGroup("fileExists(Path)", () ->
            {
                final Action4<String,Action1<FileSystem>,Boolean,Throwable> fileExistsTest = (String filePath, Action1<FileSystem> setup, Boolean expectedFileExists, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Boolean> result = fileSystem.fileExists(Path.parse(filePath));
                        test.assertNotNull(result);

                        test.assertEqual(expectedFileExists, result.getValue());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                fileExistsTest.run(null, null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                fileExistsTest.run("", null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                fileExistsTest.run("blah", null, null, new IllegalArgumentException("rootedFilePath must be rooted."));
                fileExistsTest.run("/", null, null, new IllegalArgumentException("rootedFilePath cannot end with '/'."));
                fileExistsTest.run("\\", null, null, new IllegalArgumentException("rootedFilePath cannot end with '\\'."));
                fileExistsTest.run(
                    "/folderName",
                    (FileSystem fileSystem) -> fileSystem.createFolder("/folderName"),
                    false,
                    null);
                fileExistsTest.run(
                    "/fileName",
                    (FileSystem fileSystem) -> fileSystem.createFile("/fileName"),
                    true,
                    null);
            });

            runner.testGroup("createFile(String)", () ->
            {
                final Action4<String,Action1<FileSystem>,String,Throwable> createFileTest = (String filePath, Action1<FileSystem> setup, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<File> result = fileSystem.createFile(filePath);
                        test.assertNotNull(result);

                        test.assertEqual(expectedFilePath, result.getValue() == null ? null : result.getValue().toString());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                createFileTest.run(null, null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                createFileTest.run("", null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                createFileTest.run("things.txt", null, null, new IllegalArgumentException("rootedFilePath must be rooted."));
                createFileTest.run("/things.txt", null, "/things.txt", null);
                createFileTest.run(
                    "/things.txt",
                    (FileSystem fileSystem) -> fileSystem.createFile("/things.txt"),
                    "/things.txt",
                    new FileAlreadyExistsException("/things.txt"));
                createFileTest.run("/\u0000?#!.txt", null, null, new IllegalArgumentException("rootedFilePath cannot contain invalid characters [@,#,?]."));
            });

            runner.testGroup("createFile(Path)", () ->
            {
                final Action4<String,Action1<FileSystem>,String,Throwable> createFileTest = (String filePath, Action1<FileSystem> setup, String expectedFilePath, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<File> result = fileSystem.createFile(Path.parse(filePath));
                        test.assertNotNull(result);

                        test.assertEqual(expectedFilePath, result.getValue() == null ? null : result.getValue().toString());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                createFileTest.run(null, null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                createFileTest.run("", null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                createFileTest.run("things.txt", null, null, new IllegalArgumentException("rootedFilePath must be rooted."));
                createFileTest.run("/things.txt", null, "/things.txt", null);
                createFileTest.run(
                    "/things.txt",
                    (FileSystem fileSystem) -> fileSystem.createFile("/things.txt"),
                    "/things.txt",
                    new FileAlreadyExistsException("/things.txt"));
                createFileTest.run("/\u0000?#!.txt", null, null, new IllegalArgumentException("rootedFilePath cannot contain invalid characters [@,#,?]."));
            });

            runner.testGroup("deleteFile(String)", () ->
            {
                final Action4<String,Action1<FileSystem>,Boolean,Throwable> deleteFileTest = (String filePath, Action1<FileSystem> setup, Boolean expectedResult, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<Boolean> result = fileSystem.deleteFile(filePath);
                        test.assertNotNull(result);

                        test.assertEqual(expectedResult, result.getValue());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                deleteFileTest.run(null, null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                deleteFileTest.run("", null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                deleteFileTest.run("relativeFile.txt", null, null, new IllegalArgumentException("rootedFilePath must be rooted."));
                deleteFileTest.run("/idontexist.txt", null, false, new FileNotFoundException("/idontexist.txt"));
                deleteFileTest.run(
                    "/iexist.txt",
                    (FileSystem fileSystem) -> fileSystem.createFile("/iexist.txt"),
                    true,
                    null);
            });

            runner.testGroup("getFileLastModified(String)", () ->
            {
                final Action4<String,Action1<FileSystem>,DateTime,Throwable> getFileLastModifiedTest = (String filePath, Action1<FileSystem> setup, DateTime expectedLastModified, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<DateTime> result = fileSystem.getFileLastModified(filePath);
                        test.assertNotNull(result);

                        test.assertEqual(expectedLastModified, result.getValue());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                getFileLastModifiedTest.run(null, null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                getFileLastModifiedTest.run("", null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                getFileLastModifiedTest.run("relativeFile.txt", null, null, new IllegalArgumentException("rootedFilePath must be rooted."));
                getFileLastModifiedTest.run("/idontexist.txt", null, null, new FileNotFoundException("/idontexist.txt"));

                runner.test("with existing rooted path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt");

                    final Result<DateTime> result = fileSystem.getFileLastModified("/thing.txt");
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertTrue(result.getValue().greaterThan(DateTime.local(2018, 1, 1, 0, 0, 0, 0)));
                    test.assertFalse(result.hasError());
                });
            });

            runner.testGroup("getFileLastModified(Path)", () ->
            {
                final Action4<String,Action1<FileSystem>,DateTime,Throwable> getFileLastModifiedTest = (String filePath, Action1<FileSystem> setup, DateTime expectedLastModified, Throwable expectedError) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(filePath), (Test test) ->
                    {
                        final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                        if (setup != null)
                        {
                            setup.run(fileSystem);
                        }

                        final Result<DateTime> result = fileSystem.getFileLastModified(Path.parse(filePath));
                        test.assertNotNull(result);

                        test.assertEqual(expectedLastModified, result.getValue());

                        test.assertEqual(expectedError, result.getError());
                    });
                };

                getFileLastModifiedTest.run(null, null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                getFileLastModifiedTest.run("", null, null, new IllegalArgumentException("rootedFilePath cannot be null."));
                getFileLastModifiedTest.run("relativeFile.txt", null, null, new IllegalArgumentException("rootedFilePath must be rooted."));
                getFileLastModifiedTest.run("/idontexist.txt", null, null, new FileNotFoundException("/idontexist.txt"));

                runner.test("with existing rooted path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt");

                    final Result<DateTime> result = fileSystem.getFileLastModified(Path.parse("/thing.txt"));
                    test.assertNotNull(result);
                    test.assertNotNull(result.getValue());
                    test.assertTrue(result.getValue().greaterThan(DateTime.local(2018, 1, 1, 0, 0, 0, 0)));
                    test.assertFalse(result.hasError());
                });
            });

            runner.testGroup("getFileContent(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent((String)null);
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("");
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("thing.txt");
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath must be rooted.", result.getErrorMessage());
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent("/thing.txt");
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(FileNotFoundException.class, result.getErrorType());
                    test.assertEqual("The file at \"/thing.txt\" doesn't exist.", result.getErrorMessage());
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt");

                    final Result<byte[]> result = fileSystem.getFileContent("/thing.txt");
                    test.assertNotNull(result);
                    test.assertEqual(new byte[0], result.getValue());
                    test.assertFalse(result.hasError());
                });
            });

            runner.testGroup("getFileContent(Path)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent((Path)null);
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent(Path.parse(""));
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath cannot be null.", result.getErrorMessage());
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent(Path.parse("thing.txt"));
                    test.assertNotNull(result);
                    test.assertNull(result.getValue());
                    test.assertTrue(result.hasError());
                    test.assertEqual(IllegalArgumentException.class, result.getErrorType());
                    test.assertEqual("rootedFilePath must be rooted.", result.getErrorMessage());
                });

                runner.test("with non-existing rooted path", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<byte[]> result = fileSystem.getFileContent(Path.parse("/thing.txt"));
                    test.assertError(new FileNotFoundException("/thing.txt"), result);
                });

                runner.test("with existing rooted path with no contents", (Test test) ->
                {
                    FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/thing.txt");

                    final Result<byte[]> result = fileSystem.getFileContent(Path.parse("/thing.txt"));
                    test.assertSuccess(new byte[0], result);
                });
            });

            runner.testGroup("getFileContentByteReadStream(String)", () ->
            {
                runner.test("with non-existing file", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<ByteReadStream> result = fileSystem.getFileContentByteReadStream("/i/dont/exist.txt");
                    test.assertError(new FileNotFoundException("/i/dont/exist.txt"), result);
                });
            });

            runner.testGroup("setFileContent(String,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.setFileContent((String)null, new byte[] { 0, 1, 2 });
                    test.assertError(new IllegalArgumentException("rootedFilePath cannot be null."), result);
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.setFileContent("", new byte[] { 0, 1, 2 });
                    test.assertError(new IllegalArgumentException("rootedFilePath cannot be null."), result);
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.setFileContent("relative.file", new byte[] { 0, 1, 2 });
                    test.assertError(new IllegalArgumentException("rootedFilePath must be rooted."), result);
                });

                runner.test("with non-existing rooted path with null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", null);
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 });
                    
                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", null);
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", new byte[0]);
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 });

                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", new byte[0]);
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", new byte[] { 0, 1, 2 });
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with non-existing rooted path with non-existing parent folder and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent("/folder/A.txt", new byte[] { 0, 1, 2 });
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.folderExists("/folder").getValue());
                    test.assertTrue(fileSystem.fileExists("/folder/A.txt").getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/folder/A.txt").getValue());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/A.txt");

                    final Result<Boolean> result = fileSystem.setFileContent("/A.txt", new byte[] { 0, 1, 2 });
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").getValue());
                });
            });

            runner.testGroup("setFileContent(Path,byte[])", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent((Path)null, new byte[] { 0, 1, 2 });
                    test.assertError(new IllegalArgumentException("rootedFilePath cannot be null."), result);
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse(""), new byte[] { 0, 1, 2 });
                    test.assertError(new IllegalArgumentException("rootedFilePath cannot be null."), result);
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("relative.file"), new byte[] { 0, 1, 2 });
                    test.assertError(new IllegalArgumentException("rootedFilePath must be rooted."), result);
                });

                runner.test("with non-existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    
                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), null);
                    test.assertSuccess(true, result);
                    
                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with existing rooted path and null contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 });

                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), (byte[])null);
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with non-existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), new byte[0]);
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with existing rooted path and empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.setFileContent("/A.txt", new byte[] { 0, 1 });

                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), new byte[0]);
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[0], fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with non-existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), new byte[] { 0, 1, 2 });
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").getValue());
                });

                runner.test("with existing rooted path and non-empty contents", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/A.txt");

                    final Result<Boolean> result = fileSystem.setFileContent(Path.parse("/A.txt"), new byte[] { 0, 1, 2 });
                    test.assertSuccess(true, result);

                    test.assertTrue(fileSystem.fileExists("/A.txt").getValue());
                    test.assertEqual(new byte[] { 0, 1, 2 }, fileSystem.getFileContent("/A.txt").getValue());
                });
            });

            runner.testGroup("getFilesAndFoldersRecursively(String)", () ->
            {
                runner.test("with null path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively((String)null);
                    test.assertError(new IllegalArgumentException("rootedFolderPath cannot be null."), result);
                });

                runner.test("with empty path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("");
                    test.assertError(new IllegalArgumentException("rootedFolderPath cannot be null."), result);
                });

                runner.test("with relative path", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("test/folder");
                    test.assertError(new IllegalArgumentException("rootedFolderPath must be rooted."), result);
                });

                runner.test("with rooted path when root doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("F:/test/folder");
                    test.assertError(new FolderNotFoundException("F:/test/folder"), result);
                });

                runner.test("with rooted path when parent folder doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertError(new FolderNotFoundException("/test/folder"), result);
                });

                runner.test("with rooted path when folder doesn't exist", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/");

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertError(new FolderNotFoundException("/test/folder"), result);
                });

                runner.test("with rooted path when folder is empty", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder");

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertSuccess(new Array<>(0), result);
                });

                runner.test("with rooted path when folder has files", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder");
                    fileSystem.createFile("/test/folder/1.txt");
                    fileSystem.createFile("/test/folder/2.txt");

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertSuccess(Array.fromValues(new FileSystemEntry[]
                        {
                            fileSystem.getFile("/test/folder/1.txt").getValue(),
                            fileSystem.getFile("/test/folder/2.txt").getValue()
                        }),
                        result);
                });

                runner.test("with rooted path when folder has folders", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFolder("/test/folder/1.txt");
                    fileSystem.createFolder("/test/folder/2.txt");

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertSuccess(
                        Array.fromValues(new FileSystemEntry[]
                        {
                            fileSystem.getFolder("/test/folder/1.txt").getValue(),
                            fileSystem.getFolder("/test/folder/2.txt").getValue()
                        }),
                        result);
                });

                runner.test("with rooted path when folder has grandchild files and folders", (Test test) ->
                {
                    final FileSystem fileSystem = creator.run(test.getMainAsyncRunner());
                    fileSystem.createFile("/test/folder/1.txt");
                    fileSystem.createFile("/test/folder/2.txt");
                    fileSystem.createFile("/test/folder/A/3.csv");
                    fileSystem.createFile("/test/folder/B/C/4.xml");
                    fileSystem.createFile("/test/folder/A/5.png");

                    final Result<Iterable<FileSystemEntry>> result = fileSystem.getFilesAndFoldersRecursively("/test/folder");
                    test.assertSuccess(
                        Array.fromValues(new FileSystemEntry[]
                        {
                            fileSystem.getFolder("/test/folder/A").getValue(),
                            fileSystem.getFolder("/test/folder/B").getValue(),
                            fileSystem.getFile("/test/folder/1.txt").getValue(),
                            fileSystem.getFile("/test/folder/2.txt").getValue(),
                            fileSystem.getFile("/test/folder/A/3.csv").getValue(),
                            fileSystem.getFile("/test/folder/A/5.png").getValue(),
                            fileSystem.getFolder("/test/folder/B/C").getValue(),
                            fileSystem.getFile("/test/folder/B/C/4.xml").getValue()
                        }),
                        result);
                });
            });
        });
    }
}
