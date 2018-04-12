package qub;

public abstract class FileSystemBase implements FileSystem
{
    @Override
    public AsyncFunction<Result<Boolean>> rootExists(String rootPath)
    {
        return FileSystemBase.rootExists(this, rootPath);
    }

    @Override
    public AsyncFunction<Result<Boolean>> rootExists(Path rootPath)
    {
        return FileSystemBase.rootExists(this, rootPath);
    }

    @Override
    public Result<Root> getRoot(String rootPath)
    {
        return FileSystemBase.getRoot(this, rootPath);
    }

    @Override
    public Result<Root> getRoot(Path rootPath)
    {
        return FileSystemBase.getRoot(this, rootPath);
    }

    @Override
    public Result<Iterable<Root>> getRoots()
    {
        return FileSystemBase.getRoots(this);
    }

    @Override
    public abstract AsyncFunction<Result<Iterable<Root>>> getRootsAsync();

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(String folderPath)
    {
        return FileSystemBase.getFilesAndFolders(this, folderPath);
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path folderPath)
    {
        return FileSystemBase.getFilesAndFolders(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(String folderPath)
    {
        return FileSystemBase.getFilesAndFoldersAsync(this, folderPath);
    }

    @Override
    public abstract AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(Path folderPath);

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(String folderPath)
    {
        return FileSystemBase.getFilesAndFoldersRecursively(this, folderPath);
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(Path folderPath)
    {
        return FileSystemBase.getFilesAndFoldersRecursively(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursivelyAsync(String folderPath)
    {
        return FileSystemBase.getFilesAndFoldersRecursivelyAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursivelyAsync(Path folderPath)
    {
        return FileSystemBase.getFilesAndFoldersRecursivelyAsync(this, folderPath);
    }

    @Override
    public Result<Iterable<Folder>> getFolders(String folderPath)
    {
        return FileSystemBase.getFolders(this, folderPath);
    }

    @Override
    public Result<Iterable<Folder>> getFolders(Path folderPath)
    {
        return FileSystemBase.getFolders(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync(String folderPath)
    {
        return FileSystemBase.getFoldersAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync(Path folderPath)
    {
        return FileSystemBase.getFoldersAsync(this, folderPath);
    }

    @Override
    public Result<Iterable<Folder>> getFoldersRecursively(String folderPath)
    {
        return FileSystemBase.getFoldersRecursively(this, folderPath);
    }

    @Override
    public Result<Iterable<Folder>> getFoldersRecursively(Path folderPath)
    {
        return FileSystemBase.getFoldersRecursively(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync(String folderPath)
    {
        return FileSystemBase.getFoldersRecursivelyAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync(Path folderPath)
    {
        return FileSystemBase.getFoldersRecursivelyAsync(this, folderPath);
    }

    @Override
    public Result<Iterable<File>> getFiles(String folderPath)
    {
        return FileSystemBase.getFiles(this, folderPath);
    }

    @Override
    public Result<Iterable<File>> getFiles(Path folderPath)
    {
        return FileSystemBase.getFiles(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesAsync(String folderPath)
    {
        return FileSystemBase.getFilesAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesAsync(Path folderPath)
    {
        return FileSystemBase.getFilesAsync(this, folderPath);
    }

    @Override
    public Result<Iterable<File>> getFilesRecursively(String folderPath)
    {
        return FileSystemBase.getFilesRecursively(this, folderPath);
    }

    @Override
    public Result<Iterable<File>> getFilesRecursively(Path folderPath)
    {
        return FileSystemBase.getFilesRecursively(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesRecursivelyAsync(String folderPath)
    {
        return FileSystemBase.getFilesRecursivelyAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Iterable<File>>> getFilesRecursivelyAsync(Path folderPath)
    {
        return FileSystemBase.getFilesRecursivelyAsync(this, folderPath);
    }

    @Override
    public Result<Folder> getFolder(String folderPath)
    {
        return FileSystemBase.getFolder(this, folderPath);
    }

    @Override
    public Result<Folder> getFolder(Path folderPath)
    {
        return FileSystemBase.getFolder(this, folderPath);
    }

    @Override
    public Result<Boolean> folderExists(String folderPath)
    {
        return FileSystemBase.folderExists(this, folderPath);
    }

    @Override
    public Result<Boolean> folderExists(Path folderPath)
    {
        return FileSystemBase.folderExists(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Boolean>> folderExistsAsync(String folderPath)
    {
        return FileSystemBase.folderExistsAsync(this, folderPath);
    }

    @Override
    public abstract AsyncFunction<Result<Boolean>> folderExistsAsync(Path folderPath);

    @Override
    public Result<Folder> createFolder(String folderPath)
    {
        return FileSystemBase.createFolder(this, folderPath);
    }

    @Override
    public Result<Folder> createFolder(Path folderPath)
    {
        return FileSystemBase.createFolder(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Folder>> createFolderAsync(String folderPath)
    {
        return FileSystemBase.createFolderAsync(this, folderPath);
    }

    @Override
    public abstract AsyncFunction<Result<Folder>> createFolderAsync(Path folderPath);

    @Override
    public Result<Boolean> deleteFolder(String folderPath)
    {
        return FileSystemBase.deleteFolder(this, folderPath);
    }

    @Override
    public Result<Boolean> deleteFolder(Path folderPath)
    {
        return FileSystemBase.deleteFolder(this, folderPath);
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(String folderPath)
    {
        return FileSystemBase.deleteFolderAsync(this, folderPath);
    }

    @Override
    public abstract AsyncFunction<Result<Boolean>> deleteFolderAsync(Path folderPath);

    @Override
    public Result<File> getFile(String filePath)
    {
        return FileSystemBase.getFile(this, filePath);
    }

    @Override
    public Result<File> getFile(Path filePath)
    {
        return FileSystemBase.getFile(this, filePath);
    }

    @Override
    public Result<Boolean> fileExists(String filePath)
    {
        return FileSystemBase.fileExists(this, filePath);
    }

    @Override
    public Result<Boolean> fileExists(Path filePath)
    {
        return FileSystemBase.fileExists(this, filePath);
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExistsAsync(String filePath)
    {
        return FileSystemBase.fileExistsAsync(this, filePath);
    }

    @Override
    public abstract AsyncFunction<Result<Boolean>> fileExistsAsync(Path filePath);

    @Override
    public Result<File> createFile(String filePath)
    {
        return FileSystemBase.createFile(this, filePath);
    }

    @Override
    public Result<File> createFile(Path filePath)
    {
        return FileSystemBase.createFile(this, filePath);
    }

    @Override
    public AsyncFunction<Result<File>> createFileAsync(String filePath)
    {
        return FileSystemBase.createFileAsync(this, filePath);
    }

    @Override
    public abstract AsyncFunction<Result<File>> createFileAsync(Path filePath);

    @Override
    public AsyncFunction<Result<Boolean>> deleteFile(String filePath)
    {
        return FileSystemBase.deleteFile(this, filePath);
    }

    @Override
    public abstract AsyncFunction<Result<Boolean>> deleteFile(Path filePath);

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModified(String filePath)
    {
        return FileSystemBase.getFileLastModified(this, filePath);
    }

    @Override
    public abstract AsyncFunction<Result<DateTime>> getFileLastModified(Path filePath);

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStream(String rootedFilePath)
    {
        return FileSystemBase.getFileContentByteReadStream(this, rootedFilePath);
    }

    @Override
    public abstract AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStream(Path rootedFilePath);

    @Override
    public AsyncFunction<Result<byte[]>> getFileContent(String rootedFilePath)
    {
        return FileSystemBase.getFileContent(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<byte[]>> getFileContent(Path rootedFilePath)
    {
        return FileSystemBase.getFileContent(this, rootedFilePath);
    }

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStream(String rootedFilePath)
    {
        return FileSystemBase.getFileContentByteWriteStream(this, rootedFilePath);
    }

    @Override
    public abstract AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStream(Path rootedFilePath);

    @Override
    public AsyncFunction<Result<Boolean>> setFileContent(String rootedFilePath, byte[] content)
    {
        return FileSystemBase.setFileContent(this, rootedFilePath, content);
    }

    @Override
    public AsyncFunction<Result<Boolean>> setFileContent(Path rootedFilePath, byte[] content)
    {
        return FileSystemBase.setFileContent(this, rootedFilePath, content);
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Result<Boolean>> rootExists(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.rootExists(Path.parse(rootPath));
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Result<Boolean>> rootExists(final FileSystem fileSystem, final Path rootPath)
    {
        AsyncFunction<Result<Boolean>> result;
        if (rootPath == null)
        {
            result = Async.error(fileSystem.getAsyncRunner(), new IllegalArgumentException("rootPath cannot be null."));
        }
        else if (!rootPath.isRooted())
        {
            result = Async.error(fileSystem.getAsyncRunner(), new IllegalArgumentException("rootPath must be rooted."));
        }
        else
        {
            result = asyncFunction(fileSystem, new Function1<AsyncRunner, AsyncFunction<Result<Boolean>>>()
            {
                @Override
                public AsyncFunction<Result<Boolean>> run(AsyncRunner asyncRunner)
                {
                    final Path onlyRootPath = rootPath.getRoot();
                    return fileSystem.getRootsAsync()
                        .then(new Function1<Result<Iterable<Root>>, Result<Boolean>>()
                        {
                            @Override
                            public Result<Boolean> run(Result<Iterable<Root>> getRootsResult)
                            {
                                Result<Boolean> rootExistsResult;
                                if (getRootsResult.hasError())
                                {
                                    rootExistsResult = Result.error(getRootsResult.getError());
                                }
                                else
                                {
                                    final Iterable<Root> roots = getRootsResult.getValue();
                                    rootExistsResult = Result.success(roots.contains(new Function1<Root, Boolean>()
                                    {
                                        @Override
                                        public Boolean run(Root root)
                                        {
                                            return root.getPath().equals(onlyRootPath);
                                        }
                                    }));
                                }
                                return rootExistsResult;
                            }
                        });
                }
            });
        }

        return result;
    }

    /**
     * Get a reference to a Root with the provided path. The returned Root may or may not exist.
     * @param rootPath The path to the Root to return.
     * @return The Root with the provided path.
     */
    public static Result<Root> getRoot(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.getRoot(Path.parse(rootPath));
    }

    /**
     * Get a reference to a Root with the provided Path. The returned Root may or may not exist.
     * @param rootPath The Path to the Root to return.
     * @return The Root with the provided Path.
     */
    public static Result<Root> getRoot(FileSystem fileSystem, Path rootPath)
    {
        Result<Root> result;
        if (rootPath == null)
        {
            result = Result.error(new IllegalArgumentException("rootPath cannot be null."));
        }
        else if (!rootPath.isRooted())
        {
            result = Result.error(new IllegalArgumentException("rootPath doesn't have a root specified."));
        }
        else
        {
            result = Result.success(new Root(fileSystem, rootPath));
        }
        return result;
    }

    /**
     * Get the roots of the provided FileSystem.
     * @param fileSystem The file system.
     * @return The roots of the provided FileSystem.
     */
    public static Result<Iterable<Root>> getRoots(FileSystem fileSystem)
    {
        return fileSystem.getRootsAsync().awaitReturn();
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    public static Result<Iterable<FileSystemEntry>> getFilesAndFolders(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesAndFolders(Path.parse(folderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    public static Result<Iterable<FileSystemEntry>> getFilesAndFolders(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesAndFoldersAsync(folderPath).awaitReturn();
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    public static AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesAndFoldersAsync(Path.parse(folderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    public static Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesAndFoldersRecursively(Path.parse(folderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    public static Result<Iterable<FileSystemEntry>> getFilesAndFoldersRecursively(FileSystem fileSystem, Path rootedFolderPath)
    {
        return fileSystem.getFilesAndFoldersRecursivelyAsync(rootedFolderPath).awaitReturn();
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    public static AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursivelyAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesAndFoldersRecursivelyAsync(Path.parse(folderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param rootedFolderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    public static AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersRecursivelyAsync(final FileSystem fileSystem, final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Iterable<FileSystemEntry>>> result;
        if (rootedFolderPath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else
        {
            final AsyncRunner fileSystemAsyncRunner = fileSystem.getAsyncRunner();
            result = fileSystemAsyncRunner.schedule(new Function0<Result<Iterable<FileSystemEntry>>>()
            {
                @Override
                public Result<Iterable<FileSystemEntry>> run()
                {
                    final List<Throwable> resultErrors = new ArrayList<Throwable>();
                    List<FileSystemEntry> resultEntries = null;

                    final Folder folder = fileSystem.getFolder(rootedFolderPath).getValue();
                    final Result<Iterable<FileSystemEntry>> folderEntriesResult = folder.getFilesAndFolders();

                    boolean folderExists = true;
                    if (folderEntriesResult.hasError())
                    {
                        final Throwable error = folderEntriesResult.getError();
                        folderExists = !(error instanceof FolderNotFoundException);
                        resultErrors.add(error);
                    }

                    if (folderExists)
                    {
                        resultEntries = new ArrayList<>();

                        final Queue<Folder> foldersToVisit = new ArrayListQueue<Folder>();
                        foldersToVisit.enqueue(folder);

                        while (foldersToVisit.any())
                        {
                            final Folder currentFolder = foldersToVisit.dequeue();
                            final Iterable<FileSystemEntry> currentFolderEntries = currentFolder.getFilesAndFolders().getValue();
                            for (final FileSystemEntry entry : currentFolderEntries)
                            {
                                resultEntries.add(entry);

                                if (entry instanceof Folder)
                                {
                                    foldersToVisit.enqueue((Folder)entry);
                                }
                            }
                        }
                    }

                    return Result.<Iterable<FileSystemEntry>>done(resultEntries, ErrorIterable.from(resultErrors));
                }
            });
        }
        return result;
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    public static Result<Iterable<Folder>> getFolders(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFolders(Path.parse(folderPath));
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    public static Result<Iterable<Folder>> getFolders(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFoldersAsync(folderPath).awaitReturn();
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    public static AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFoldersAsync(Path.parse(folderPath));
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    public static AsyncFunction<Result<Iterable<Folder>>> getFoldersAsync(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesAndFoldersAsync(folderPath)
            .then(new Function1<Result<Iterable<FileSystemEntry>>, Result<Iterable<Folder>>>()
            {
                @Override
                public Result<Iterable<Folder>> run(Result<Iterable<FileSystemEntry>> result)
                {
                    final Iterable<FileSystemEntry> entries = result.getValue();
                    return Result.done(entries == null ? null : entries.instanceOf(Folder.class), result.getError());
                }
            });
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    public static Result<Iterable<Folder>> getFoldersRecursively(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFoldersRecursively(Path.parse(folderPath));
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    public static Result<Iterable<Folder>> getFoldersRecursively(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFoldersRecursivelyAsync(folderPath).awaitReturn();
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    public static AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFoldersRecursivelyAsync(Path.parse(folderPath));
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    public static AsyncFunction<Result<Iterable<Folder>>> getFoldersRecursivelyAsync(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesAndFoldersRecursivelyAsync(folderPath)
            .then(new Function1<Result<Iterable<FileSystemEntry>>, Result<Iterable<Folder>>>()
            {
                @Override
                public Result<Iterable<Folder>> run(Result<Iterable<FileSystemEntry>> result)
                {
                    final Iterable<FileSystemEntry> entries = result.getValue();
                    return Result.done(entries == null ? null : entries.instanceOf(Folder.class), result.getError());
                }
            });
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    public static Result<Iterable<File>> getFiles(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFiles(Path.parse(folderPath));
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    public static Result<Iterable<File>> getFiles(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesAsync(folderPath).awaitReturn();
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    public static AsyncFunction<Result<Iterable<File>>> getFilesAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesAsync(Path.parse(folderPath));
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    public static AsyncFunction<Result<Iterable<File>>> getFilesAsync(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesAndFoldersAsync(folderPath)
            .then(new Function1<Result<Iterable<FileSystemEntry>>, Result<Iterable<File>>>()
            {
                @Override
                public Result<Iterable<File>> run(Result<Iterable<FileSystemEntry>> result)
                {
                    final Iterable<FileSystemEntry> entries = result.getValue();
                    return Result.done(entries == null ? null : entries.instanceOf(File.class), result.getError());
                }
            });
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    public static Result<Iterable<File>> getFilesRecursively(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesRecursively(Path.parse(folderPath));
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    public static Result<Iterable<File>> getFilesRecursively(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesRecursivelyAsync(folderPath).awaitReturn();
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    public static AsyncFunction<Result<Iterable<File>>> getFilesRecursivelyAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesRecursivelyAsync(Path.parse(folderPath));
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    public static AsyncFunction<Result<Iterable<File>>> getFilesRecursivelyAsync(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesAndFoldersRecursivelyAsync(folderPath)
            .then(new Function1<Result<Iterable<FileSystemEntry>>, Result<Iterable<File>>>()
            {
                @Override
                public Result<Iterable<File>> run(Result<Iterable<FileSystemEntry>> result)
                {
                    final Iterable<FileSystemEntry> entries = result.getValue();
                    final Iterable<File> resultEntries = entries == null ? null : entries.instanceOf(File.class);
                    return Result.done(resultEntries, result.getError());
                }
            });
    }

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param folderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    public static Result<Folder> getFolder(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFolder(Path.parse(folderPath));
    }

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param folderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    public static Result<Folder> getFolder(FileSystem fileSystem, Path folderPath)
    {
        Result<Folder> result;
        if (folderPath == null)
        {
            result = Result.error(new IllegalArgumentException("folderPath cannot be null."));
        }
        else if (!folderPath.isRooted())
        {
            result = Result.error(new IllegalArgumentException("folderPath doesn't have a root specified."));
        }
        else
        {
            result = Result.success(new Folder(fileSystem, folderPath));
        }
        return result;
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public static Result<Boolean> folderExists(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.folderExists(Path.parse(folderPath));
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public static Result<Boolean> folderExists(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.folderExistsAsync(folderPath).awaitReturn();
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Result<Boolean>> folderExistsAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.folderExistsAsync(Path.parse(folderPath));
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public static Result<Folder> createFolder(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.createFolder(Path.parse(folderPath));
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public static Result<Folder> createFolder(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.createFolderAsync(folderPath).awaitReturn();
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Result<Folder>> createFolderAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.createFolderAsync(Path.parse(folderPath));
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    public static Result<Boolean> deleteFolder(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.deleteFolder(Path.parse(folderPath));
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    public static Result<Boolean> deleteFolder(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.deleteFolderAsync(folderPath).awaitReturn();
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    public static AsyncFunction<Result<Boolean>> deleteFolderAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.deleteFolderAsync(Path.parse(folderPath));
    }

    /**
     * Get a reference to the File at the provided folderPath.
     * @param filePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    public static Result<File> getFile(FileSystem fileSystem, String filePath)
    {
        return fileSystem.getFile(Path.parse(filePath));
    }

    /**
     * Get a reference to the File at the provided folderPath.
     * @param filePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    public static Result<File> getFile(FileSystem fileSystem, Path filePath)
    {
        Result<File> result;
        if (filePath == null)
        {
            result = Result.error(new IllegalArgumentException("filePath cannot be null."));
        }
        else if (!filePath.isRooted())
        {
            result = Result.error(new IllegalArgumentException("filePath doesn't have a root specified."));
        }
        else if (filePath.endsWith("/"))
        {
            result = Result.error(new IllegalArgumentException("filePath cannot end with '/'."));
        }
        else if (filePath.endsWith("\\"))
        {
            result = Result.error(new IllegalArgumentException("filePath cannot end with '\\'."));
        }
        else
        {
            result = Result.success(new File(fileSystem, filePath));
        }
        return result;
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static Result<Boolean> fileExists(FileSystem fileSystem, String filePath)
    {
        return fileSystem.fileExists(Path.parse(filePath));
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static Result<Boolean> fileExists(FileSystem fileSystem, Path filePath)
    {
        return fileSystem.fileExistsAsync(filePath).awaitReturn();
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Result<Boolean>> fileExistsAsync(FileSystem fileSystem, String filePath)
    {
        return fileSystem.fileExistsAsync(Path.parse(filePath));
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    public static Result<File> createFile(FileSystem fileSystem, String filePath)
    {
        return fileSystem.createFile(Path.parse(filePath));
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    public static Result<File> createFile(FileSystem fileSystem, Path filePath)
    {
        return fileSystem.createFileAsync(filePath).awaitReturn();
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Result<File>> createFileAsync(FileSystem fileSystem, String filePath)
    {
        return fileSystem.createFileAsync(Path.parse(filePath));
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    public static AsyncFunction<Result<Boolean>> deleteFile(FileSystem fileSystem, String filePath)
    {
        return fileSystem.deleteFile(Path.parse(filePath));
    }

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param filePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    public static AsyncFunction<Result<DateTime>> getFileLastModified(FileSystem fileSystem, String filePath)
    {
        return fileSystem.getFileLastModified(Path.parse(filePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    public static AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStream(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentByteReadStream(Path.parse(rootedFilePath));
    }

    public static AsyncFunction<Result<byte[]>> getFileContent(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContent(Path.parse(rootedFilePath));
    }

    public static AsyncFunction<Result<byte[]>> getFileContent(FileSystem fileSystem, Path rootedFilePath)
    {
        return fileSystem.getFileContentByteReadStream(rootedFilePath)
            .then(new Function1<Result<ByteReadStream>, Result<byte[]>>()
            {
                @Override
                public Result<byte[]> run(Result<ByteReadStream> byteReadStreamResult)
                {
                    Result<byte[]> result;

                    if (byteReadStreamResult.getError() != null)
                    {
                        result = Result.error(byteReadStreamResult.getError());
                    }
                    else
                    {
                        try (final ByteReadStream byteReadStream = byteReadStreamResult.getValue())
                        {
                            result = Result.success(byteReadStream.readAllBytes());
                        }
                    }

                    return result;
                }
            });
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    public static AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStream(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentByteWriteStream(Path.parse(rootedFilePath));
    }



    public static AsyncFunction<Result<Boolean>> setFileContent(FileSystem fileSystem, String rootedFilePath, byte[] content)
    {
        return fileSystem.setFileContent(Path.parse(rootedFilePath), content);
    }

    public static AsyncFunction<Result<Boolean>> setFileContent(FileSystem fileSystem, Path rootedFilePath, final byte[] content)
    {
        return fileSystem.getFileContentByteWriteStream(rootedFilePath)
            .then(new Function1<Result<ByteWriteStream>, Result<Boolean>>()
            {
                @Override
                public Result<Boolean> run(Result<ByteWriteStream> byteWriteStreamResult)
                {
                    Result<Boolean> result;

                    if (byteWriteStreamResult.getError() != null)
                    {
                        result = Result.error(byteWriteStreamResult.getError());
                    }
                    else
                    {
                        try (final ByteWriteStream byteWriteStream = byteWriteStreamResult.getValue())
                        {

                            result = Result.success(byteWriteStream.write(content));
                        }
                    }

                    return result;
                }
            });
    }

    protected static <T> AsyncFunction<T> async(FileSystem fileSystem, final Function1<AsyncRunner,T> function)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final AsyncRunner fileSystemAsyncRunner = fileSystem.getAsyncRunner();
        return fileSystemAsyncRunner
            .schedule(new Function0<T>()
            {
                @Override
                public T run()
                {
                    return function.run(fileSystemAsyncRunner);
                }
            })
            .thenOn(currentAsyncRunner);
    }

    protected static <T> AsyncFunction<T> asyncFunction(FileSystem fileSystem, final Function1<AsyncRunner,AsyncFunction<T>> function)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final AsyncRunner fileSystemAsyncRunner = fileSystem.getAsyncRunner();
        return fileSystemAsyncRunner
            .scheduleAsyncFunction(new Function0<AsyncFunction<T>>()
            {
                @Override
                public AsyncFunction<T> run()
                {
                    return function.run(fileSystemAsyncRunner);
                }
            })
            .thenOn(currentAsyncRunner);
    }
}
