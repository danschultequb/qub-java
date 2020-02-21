package qub;

public interface QubProjectFolderTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(QubPublisherFolder.class, () ->
        {
            runner.testGroup("get(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> QubProjectFolder.get(null),
                        new PreConditionFailure("projectFolder cannot be null."));
                });

                runner.test("with root folder", (Test test) ->
                {
                    final Folder folder = QubProjectFolderTests.getFolder(test, "/");
                    test.assertThrows(() -> QubProjectFolder.get(folder),
                        new PreConditionFailure("projectFolder.getPath().getSegments().getCount() (1) must be greater than or equal to 3."));
                });

                runner.test("with folder that doesn't exist", (Test test) ->
                {
                    final Folder folder = QubProjectFolderTests.getFolder(test, "/qub/me/project/");
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);
                    test.assertEqual(folder.getPath(), projectFolder.getPath());
                    test.assertFalse(projectFolder.exists().await());
                });

                runner.test("with folder that exists", (Test test) ->
                {
                    final Folder folder = QubProjectFolderTests.createFolder(test, "/qub/me/project/");
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);
                    test.assertEqual(folder.getPath(), projectFolder.getPath());
                    test.assertTrue(projectFolder.exists().await());
                });
            });

            runner.test("getQubFolder()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                test.assertEqual("/qub/", projectFolder.getQubFolder().await().toString());
            });

            runner.test("getPublisherFolder()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                test.assertEqual("/qub/" + publisherName + "/", projectFolder.getPublisherFolder().await().toString());
            });

            runner.test("getPublisherName()", (Test test) ->
            {
                final String publisherName = "a";
                final String projectName = "b";
                final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                test.assertEqual(publisherName, projectFolder.getPublisherName().await());
            });

            runner.testGroup("getProjectVersionFolders()", () ->
            {
                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    test.assertEqual(Iterable.create(), projectFolder.getProjectVersionFolders().await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getQubFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectFolder.getProjectVersionFolders().await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getPublisherFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectFolder.getProjectVersionFolders().await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.create().await();
                    test.assertEqual(Iterable.create(), projectFolder.getProjectVersionFolders().await());
                });

                runner.test("with one version folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.createFolder("1").await();
                    test.assertEqual(
                        Iterable.create(
                            projectFolder.getProjectVersionFolder("1").await()),
                        projectFolder.getProjectVersionFolders().await());
                });
            });

            runner.testGroup("getProjectVersionFolders2()", () ->
            {
                runner.test("with non-existing Qub folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    test.assertEqual(Iterable.create(), projectFolder.getProjectVersionFolders2().await());
                });

                runner.test("with non-existing publisher folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getQubFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectFolder.getProjectVersionFolders2().await());
                });

                runner.test("with non-existing project folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.getPublisherFolder().await().create().await();
                    test.assertEqual(Iterable.create(), projectFolder.getProjectVersionFolders2().await());
                });

                runner.test("with non-existing versions folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.create().await();
                    test.assertEqual(Iterable.create(), projectFolder.getProjectVersionFolders2().await());
                });

                runner.test("with empty versions folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    projectFolder.createFolder("versions").await();
                    test.assertEqual(Iterable.create(), projectFolder.getProjectVersionFolders2().await());
                });

                runner.test("with one version folder", (Test test) ->
                {
                    final String publisherName = "a";
                    final String projectName = "b";
                    final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                    final Folder versionsFolder = projectFolder.getFolder("versions").await();
                    versionsFolder.createFolder("1").await();
                    test.assertEqual(
                        Iterable.create(
                            projectFolder.getProjectVersionFolder2("1").await()),
                        projectFolder.getProjectVersionFolders2().await());
                });
            });

            runner.testGroup("getProjectJSONFile(String)", () ->
            {
                final Action4<String,String,String,Throwable> getProjectJSONFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertThrows(() -> projectFolder.getProjectJSONFile(version), expected);
                    });
                };

                getProjectJSONFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getProjectJSONFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getProjectJSONFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertEqual(
                            projectFolder.getFile(version + "/project.json").await(),
                            projectFolder.getProjectJSONFile(version).await());
                    });
                };

                getProjectJSONFileTest.run("a", "b", "c");
            });

            runner.testGroup("getProjectJSONFile2(String)", () ->
            {
                final Action4<String,String,String,Throwable> getProjectJSONFile2ErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertThrows(() -> projectFolder.getProjectJSONFile2(version), expected);
                    });
                };

                getProjectJSONFile2ErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getProjectJSONFile2ErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getProjectJSONFile2Test = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertEqual(
                            projectFolder.getFile("versions/" + version + "/project.json").await(),
                            projectFolder.getProjectJSONFile2(version).await());
                    });
                };

                getProjectJSONFile2Test.run("a", "b", "c");
            });

            runner.testGroup("getCompiledSourcesFile(String)", () ->
            {
                final Action4<String,String,String,Throwable> getCompiledSourcesFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertThrows(() -> projectFolder.getCompiledSourcesFile(version), expected);
                    });
                };

                getCompiledSourcesFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getCompiledSourcesFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getCompiledSourcesFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertEqual(
                            projectFolder.getFile(version + "/" + projectName + ".jar").await(),
                            projectFolder.getCompiledSourcesFile(version).await());
                    });
                };

                getCompiledSourcesFileTest.run("a", "b", "1");
            });

            runner.testGroup("getCompiledSourcesFile2(String)", () ->
            {
                final Action4<String,String,String,Throwable> getCompiledSourcesFile2ErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertThrows(() -> projectFolder.getCompiledSourcesFile2(version), expected);
                    });
                };

                getCompiledSourcesFile2ErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getCompiledSourcesFile2ErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getCompiledSourcesFile2Test = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertEqual(
                            projectFolder.getFile("versions/" + version + "/" + projectName + ".jar").await(),
                            projectFolder.getCompiledSourcesFile2(version).await());
                    });
                };

                getCompiledSourcesFile2Test.run("a", "b", "1");
            });

            runner.testGroup("getSourcesFile(String)", () ->
            {
                final Action4<String,String,String,Throwable> getSourcesFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertThrows(() -> projectFolder.getSourcesFile(version), expected);
                    });
                };

                getSourcesFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getSourcesFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getSourcesFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertEqual(
                            projectFolder.getFile(version + "/" + projectName + ".sources.jar").await(),
                            projectFolder.getSourcesFile(version).await());
                    });
                };

                getSourcesFileTest.run("a", "b", "1");
            });

            runner.testGroup("getSourcesFile2(String)", () ->
            {
                final Action4<String,String,String,Throwable> getSourcesFile2ErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertThrows(() -> projectFolder.getSourcesFile2(version), expected);
                    });
                };

                getSourcesFile2ErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getSourcesFile2ErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getSourcesFile2Test = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertEqual(
                            projectFolder.getFile("versions/" + version + "/" + projectName + ".sources.jar").await(),
                            projectFolder.getSourcesFile2(version).await());
                    });
                };

                getSourcesFile2Test.run("a", "b", "1");
            });

            runner.testGroup("getCompiledTestsFile(String)", () ->
            {
                final Action4<String,String,String,Throwable> getCompiledTestsFileErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertThrows(() -> projectFolder.getCompiledTestsFile(version), expected);
                    });
                };

                getCompiledTestsFileErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getCompiledTestsFileErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getCompiledTestsFileTest = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertEqual(
                            projectFolder.getFile(version + "/" + projectName + ".tests.jar").await(),
                            projectFolder.getCompiledTestsFile(version).await());
                    });
                };

                getCompiledTestsFileTest.run("a", "b", "1");
            });

            runner.testGroup("getCompiledTestsFile2(String)", () ->
            {
                final Action4<String,String,String,Throwable> getCompiledTestsFile2ErrorTest = (String publisherName, String projectName, String version, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertThrows(() -> projectFolder.getCompiledTestsFile2(version), expected);
                    });
                };

                getCompiledTestsFile2ErrorTest.run("a", "b", null, new PreConditionFailure("version cannot be null."));
                getCompiledTestsFile2ErrorTest.run("a", "b", "", new PreConditionFailure("version cannot be empty."));

                final Action3<String,String,String> getCompiledTestsFile2Test = (String publisherName, String projectName, String version) ->
                {
                    runner.test("with " + English.andList(Iterable.create(publisherName, projectName, version).map(Strings::escapeAndQuote)), (Test test) ->
                    {
                        final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/" + publisherName + "/" + projectName + "/");
                        test.assertEqual(
                            projectFolder.getFile("versions/" + version + "/" + projectName + ".tests.jar").await(),
                            projectFolder.getCompiledTestsFile2(version).await());
                    });
                };

                getCompiledTestsFile2Test.run("a", "b", "1");
            });

            runner.testGroup("equals(Object)", () ->
            {
                runner.test("with /qub/me/grapes/ and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/grapes/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);
                    test.assertEqual(false, projectFolder.equals((Object)null));
                });

                runner.test("with /qub/me/grapes/ and \"hello world\"", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/grapes/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);
                    test.assertEqual(false, projectFolder.equals((Object)"hello world"));
                });

                runner.test("with /qub/me/a/ and /other/thing/b/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/a/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/b/").await();
                    final QubProjectFolder projectFolder2 = QubProjectFolder.get(folder2);

                    test.assertEqual(false, projectFolder.equals((Object)projectFolder2));
                });

                runner.test("with /qub/me/c/ and /qub/me/c/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/c/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/c/").await();
                    final QubProjectFolder projectFolder2 = QubProjectFolder.get(folder2);

                    test.assertEqual(true, projectFolder.equals((Object)projectFolder2));
                });
            });

            runner.testGroup("equals(QubProjectFolder)", () ->
            {
                runner.test("with /qub/me/spam and null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);
                    final Folder folder = fileSystem.getFolder("/qub/me/spam/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);
                    test.assertEqual(false, projectFolder.equals((QubProjectFolder)null));
                });

                runner.test("with /qub/me/project1 and /other/thing/project2", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/project1").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/other/thing/project2").await();
                    final QubProjectFolder projectFolder2 = QubProjectFolder.get(folder2);

                    test.assertEqual(false, projectFolder.equals((QubProjectFolder)projectFolder2));
                });

                runner.test("with /qub/me/proj/ and /qub/me/proj/", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = QubPublisherFolderTests.createFileSystem(test);

                    final Folder folder = fileSystem.getFolder("/qub/me/proj/").await();
                    final QubProjectFolder projectFolder = QubProjectFolder.get(folder);

                    final Folder folder2 = fileSystem.getFolder("/qub/me/proj/").await();
                    final QubProjectFolder projectFolder2 = QubProjectFolder.get(folder2);

                    test.assertEqual(true, projectFolder.equals((QubProjectFolder)projectFolder2));
                });
            });

            runner.test("toString()", (Test test) ->
            {
                final QubProjectFolder projectFolder = QubProjectFolderTests.getQubProjectFolder(test, "/qub/me/proj/");
                test.assertEqual("/qub/me/proj/", projectFolder.toString());
            });
        });
    }

    static InMemoryFileSystem createFileSystem(Test test)
    {
        final InMemoryFileSystem fileSystem = new InMemoryFileSystem(test.getClock());
        fileSystem.createRoot("/").await();
        return fileSystem;
    }

    static Folder getFolder(Test test, String folderPath)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNullAndNotEmpty(folderPath, "folderPath");

        final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem(test);
        return fileSystem.getFolder(folderPath).await();
    }

    static Folder createFolder(Test test, String folderPath)
    {
        PreCondition.assertNotNull(test, "test");
        PreCondition.assertNotNullAndNotEmpty(folderPath, "folderPath");

        final InMemoryFileSystem fileSystem = QubProjectFolderTests.createFileSystem(test);
        return fileSystem.createFolder(folderPath).await();
    }

    static QubProjectFolder getQubProjectFolder(Test test, String folderPath)
    {
        return QubProjectFolder.get(QubPublisherFolderTests.getFolder(test, folderPath));
    }

    static QubProjectFolder createQubProjectFolder(Test test, String folderPath)
    {
        return QubProjectFolder.get(QubPublisherFolderTests.createFolder(test, folderPath));
    }
}
