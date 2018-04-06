package qub;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

/**
 * A Console platform that can be used to write Console applications.
 */
public class Process extends DisposableBase
{
    private final CommandLine commandLine;

    private final Value<CharacterEncoding> characterEncoding;
    private final Value<String> lineSeparator;
    private final Value<Boolean> includeNewLines;

    private final Value<ByteWriteStream> outputWriteStream;
    private final Value<ByteWriteStream> errorWriteStream;

    private final Value<ByteReadStream> byteReadStream;
    private final Value<CharacterReadStream> characterReadStream;
    private final Value<LineReadStream> lineReadStream;

    private final Value<Random> random;
    private final Value<FileSystem> fileSystem;
    private final Value<String> currentFolderPathString;
    private final Value<Map<String,String>> environmentVariables;
    private final Value<Synchronization> synchronization;
    private final Value<Function0<Stopwatch>> stopwatchCreator;

    private final AsyncRunner mainAsyncRunner;
    private volatile AsyncRunner parallelAsyncRunner;

    private volatile boolean disposed;

    /**
     * Create a new Process applications can be written with.
     */
    public Process()
    {
        this((String[])null);
    }

    /**
     * Create a new Process that Console applications can be written with.
     */
    public Process(String[] commandLineArgumentStrings)
    {
        this(CommandLine.parse(commandLineArgumentStrings));
    }

    public Process(Iterable<String> commandLineArgumentStrings)
    {
        this(CommandLine.parse(commandLineArgumentStrings));
    }

    /**
     * Create a new Process that Console applications can be written with.
     */
    public Process(CommandLine commandLine)
    {
        this(commandLine, new ManualAsyncRunner());
    }

    Process(CommandLine commandLine, AsyncRunner mainAsyncRunner)
    {
        this.commandLine = commandLine;

        characterEncoding = new Value<>();
        lineSeparator = new Value<>();
        includeNewLines = new Value<>();
        outputWriteStream = new Value<>();
        errorWriteStream = new Value<>();
        byteReadStream = new Value<>();
        characterReadStream = new Value<>();
        lineReadStream = new Value<>();
        random = new Value<>();
        fileSystem = new Value<>();
        currentFolderPathString = new Value<>();
        environmentVariables = new Value<>();
        synchronization = new Value<>();
        stopwatchCreator = new Value<>();

        this.mainAsyncRunner = mainAsyncRunner;
        AsyncRunnerRegistry.setCurrentThreadAsyncRunner(mainAsyncRunner);
    }

    public AsyncRunner getMainAsyncRunner()
    {
        return mainAsyncRunner;
    }

    public AsyncRunner getParallelAsyncRunner()
    {
        if (parallelAsyncRunner == null)
        {
            parallelAsyncRunner = new ParallelAsyncRunner();
        }
        return parallelAsyncRunner;
    }

    public void await()
    {
        while (mainAsyncRunner.getScheduledTaskCount() > 0 || (parallelAsyncRunner != null && parallelAsyncRunner.getScheduledTaskCount() > 0))
        {
            mainAsyncRunner.await();

            if (parallelAsyncRunner != null)
            {
                parallelAsyncRunner.await();
            }
        }
    }

    public Indexable<String> getCommandLineArgumentStrings()
    {
        return commandLine.getArgumentStrings();
    }

    public CommandLine getCommandLine()
    {
        return commandLine;
    }

    public void setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        this.characterEncoding.set(characterEncoding);
    }

    public CharacterEncoding getCharacterEncoding()
    {
        if (!characterEncoding.hasValue())
        {
            characterEncoding.set(CharacterEncoding.UTF_8);
        }
        return characterEncoding.get();
    }

    public void setLineSeparator(String lineSeparator)
    {
        this.lineSeparator.set(lineSeparator);
    }

    public String getLineSeparator()
    {
        if (!lineSeparator.hasValue())
        {
            lineSeparator.set(onWindows() ? "\r\n" : "\n");
        }
        return lineSeparator.get();
    }

    public void setIncludeNewLines(boolean includeNewLines)
    {
        this.includeNewLines.set(includeNewLines);
    }

    public boolean getIncludeNewLines()
    {
        if (!includeNewLines.hasValue())
        {
            includeNewLines.set(false);
        }
        return includeNewLines.get();
    }

    /**
     * Set the ByteWriteStream that is assigned to this Console's output.
     * @param writeStream The ByteWriteStream that is assigned to this Console's output.
     */
    public void setOutput(ByteWriteStream writeStream)
    {
        this.outputWriteStream.set(writeStream);
    }

    /**
     * Get the ByteWriteStream that is assigned to this Console.
     * @return The ByteWriteStream that is assigned to this Console.
     */
    public ByteWriteStream getOutputAsByteWriteStream()
    {
        if (!outputWriteStream.hasValue())
        {
            outputWriteStream.set(new OutputStreamToByteWriteStream(System.out));
        }
        return outputWriteStream.get();
    }

    public void setOutput(CharacterWriteStream writeStream)
    {
        setOutput(writeStream == null ? null : writeStream.asByteWriteStream());
    }

    public CharacterWriteStream getOutputAsCharacterWriteStream()
    {
        final ByteWriteStream outputByteWriteStream = getOutputAsByteWriteStream();
        return outputByteWriteStream == null ? null : outputByteWriteStream.asCharacterWriteStream(getCharacterEncoding());
    }

    public void setOutput(LineWriteStream writeStream)
    {
        setOutput(writeStream == null ? null : writeStream.asCharacterWriteStream());
    }

    public LineWriteStream getOutputAsLineWriteStream()
    {
        final ByteWriteStream outputByteWriteStream = getOutputAsByteWriteStream();
        return outputByteWriteStream == null ? null : outputByteWriteStream.asLineWriteStream(getCharacterEncoding(), getLineSeparator());
    }

    /**
     * Set the ByteWriteStream that is assigned to this Console's error.
     * @param writeStream The ByteWriteStream that is assigned to this Console's error.
     */
    public void setError(ByteWriteStream writeStream)
    {
        this.errorWriteStream.set(writeStream);
    }

    /**
     * Get the error ByteWriteStream that is assigned to this Console.
     * @return The error ByteWriteStream that is assigned to this Console.
     */
    public ByteWriteStream getErrorAsByteWriteStream()
    {
        if (!errorWriteStream.hasValue())
        {
            errorWriteStream.set(new OutputStreamToByteWriteStream(System.err));
        }
        return errorWriteStream.get();
    }

    public void setError(CharacterWriteStream writeStream)
    {
        setError(writeStream == null ? null : writeStream.asByteWriteStream());
    }

    public CharacterWriteStream getErrorAsCharacterWriteStream()
    {
        final ByteWriteStream errorByteWriteStream = getErrorAsByteWriteStream();
        return errorByteWriteStream == null ? null : errorByteWriteStream.asCharacterWriteStream(getCharacterEncoding());
    }

    public void setError(LineWriteStream writeStream)
    {
        setError(writeStream == null ? null : writeStream.asCharacterWriteStream());
    }

    public LineWriteStream getErrorAsLineWriteStream()
    {
        final ByteWriteStream errorByteWriteStream = getErrorAsByteWriteStream();
        return errorByteWriteStream == null ? null : errorByteWriteStream.asLineWriteStream(getCharacterEncoding(), getLineSeparator());
    }

    /**
     * Set the ByteReadStream that is assigned to this Console.
     * @param readStream The ByteReadStream that is assigned to this Console.
     */
    public void setInput(ByteReadStream readStream)
    {
        setInput(readStream == null ? null : readStream.asLineReadStream(getCharacterEncoding(), getIncludeNewLines()));
    }

    /**
     * Set the CharacterReadStream that is assigned to this Console.
     * @param readStream The CharacterReadStream that is assigned to this Console.
     */
    public void setInput(CharacterReadStream readStream)
    {
        setInput(readStream == null ? null : readStream.asLineReadStream(getIncludeNewLines()));
    }

    /**
     * Set the LineReadStream that is assigned to this Console.
     * @param readStream The LineReadStream that is assigned to this Console.
     */
    public void setInput(LineReadStream readStream)
    {
        lineReadStream.set(readStream);
        characterReadStream.set(readStream == null ? null : readStream.asCharacterReadStream());
        byteReadStream.set(readStream == null ? null : readStream.asByteReadStream());
    }

    /**
     * Get the TextReadStream that is assigned to this Console.
     * @return The TextReadStream that is assigned to this Console.
     */
    public ByteReadStream getInputAsByteReadStream()
    {
        if (!byteReadStream.hasValue())
        {
            setInput(new InputStreamToByteReadStream(System.in));
        }
        return byteReadStream.get();
    }

    public CharacterReadStream getInputAsCharacterReadStream()
    {
        if (!characterReadStream.hasValue())
        {
            getInputAsByteReadStream();
        }
        return characterReadStream.get();
    }

    public LineReadStream getInputAsLineReadStream()
    {
        if (!lineReadStream.hasValue())
        {
            getInputAsByteReadStream();
        }
        return lineReadStream.get();
    }

    /**
     * Set the Random number generator assigned to this Console.
     * @param random The Random number generator assigned to this Console.
     */
    void setRandom(Random random)
    {
        this.random.set(random);
    }

    /**
     * Get the Random number generator assigned to this Console.
     * @return The Random number generator assigned to this Console.
     */
    public Random getRandom()
    {
        if (!random.hasValue())
        {
            random.set(new JavaRandom());
        }
        return random.get();
    }

    /**
     * Get the FileSystem assigned to this Console.
     * @return The FileSystem assigned to this Console.
     */
    public FileSystem getFileSystem()
    {
        if (!fileSystem.hasValue())
        {
            setFileSystem(new JavaFileSystem(getParallelAsyncRunner()));
        }
        return fileSystem.get();
    }

    /**
     * Set the FileSystem that is assigned to this Console.
     * @param fileSystem The FileSystem that will be assigned to this Console.
     */
    public void setFileSystem(FileSystem fileSystem)
    {
        this.fileSystem.set(fileSystem);
        if (fileSystem == null)
        {
            setCurrentFolderPathString(null);
        }
        else
        {
            currentFolderPathString.clear();
        }
    }

    public void setFileSystem(Function1<AsyncRunner,FileSystem> creator)
    {
        setFileSystem(creator.run(getParallelAsyncRunner()));
    }

    public String getCurrentFolderPathString()
    {
        if (!currentFolderPathString.hasValue())
        {
            currentFolderPathString.set(Paths.get(".").toAbsolutePath().normalize().toString());
        }
        return currentFolderPathString.get();
    }

    public void setCurrentFolderPathString(String currentFolderPathString)
    {
        this.currentFolderPathString.set(currentFolderPathString);
    }

    /**
     * Get the path to the folder that this Console is currently running in.
     * @return The path to the folder that this Console is currently running in.
     */
    public Path getCurrentFolderPath()
    {
        final String currentFolderPathString = getCurrentFolderPathString();
        return Path.parse(currentFolderPathString);
    }

    /**
     * Set the path to the folder that this Console is currently running in.
     * @param currentFolderPath The folder to the path that this Console is currently running in.
     */
    public void setCurrentFolderPath(Path currentFolderPath)
    {
        currentFolderPathString.set(currentFolderPath == null ? null : currentFolderPath.toString());
    }

    public Result<Folder> getCurrentFolder()
    {
        final FileSystem fileSystem = getFileSystem();
        return fileSystem == null
            ? Result.<Folder>error(new IllegalArgumentException("No FileSystem has been set."))
            : fileSystem.getFolder(getCurrentFolderPath());
    }

    /**
     * Set the environment variables that will be used by this Application.
     * @param environmentVariables The environment variables that will be used by this application.
     */
    public void setEnvironmentVariables(Map<String,String> environmentVariables)
    {
        this.environmentVariables.set(environmentVariables);
    }

    /**
     * Get the environment variables for this application.
     * @return The environment variables for this application.
     */
    public Map<String,String> getEnvironmentVariables()
    {
        if (!environmentVariables.hasValue())
        {
            final Map<String,String> envVars = new ListMap<>();
            for (final java.util.Map.Entry<String,String> entry : System.getenv().entrySet())
            {
                envVars.set(entry.getKey(), entry.getValue());
            }
            environmentVariables.set(envVars);
        }
        return environmentVariables.get();
    }

    /**
     * Get the value of the provided environment variable. If no environment variable exists with
     * the provided name, then null will be returned.
     * @param variableName The name of the environment variable.
     * @return The value of the environment variable, or null if the variable doesn't exist.
     */
    public String getEnvironmentVariable(String variableName)
    {
        String result = null;
        if (variableName != null && !variableName.isEmpty())
        {
            final String lowerVariableName = variableName.toLowerCase();
            final Map<String,String> environmentVariables = getEnvironmentVariables();
            final MapEntry<String,String> resultEntry = environmentVariables.first(new Function1<MapEntry<String, String>, Boolean>()
            {
                @Override
                public Boolean run(MapEntry<String, String> entry)
                {
                    return entry.getKey().toLowerCase().equals(lowerVariableName);
                }
            });
            result = resultEntry == null ? null : resultEntry.getValue();
        }
        return result;
    }

    /**
     * Get the Synchronization factory for creating synchronization objects.
     * @return The Synchronization factory for creating synchronization objects.
     */
    public Synchronization getSynchronization()
    {
        if (!synchronization.hasValue())
        {
            synchronization.set(new Synchronization());
        }
        return synchronization.get();
    }

    public void setStopwatchCreator(Function0<Stopwatch> stopwatchCreator)
    {
        this.stopwatchCreator.set(stopwatchCreator);
    }

    public Stopwatch getStopwatch()
    {
        if (!stopwatchCreator.hasValue())
        {
            stopwatchCreator.set(new Function0<Stopwatch>()
            {
                @Override
                public Stopwatch run()
                {
                    return new JavaStopwatch();
                }
            });
        }
        return stopwatchCreator.get() == null ? null : stopwatchCreator.get().run();
    }

    /**
     * Get a ProcessBuilder that references the provided executablePath.
     * @param executablePath The path to the executable to run from the returned ProcessBuilder.
     * @return The ProcessBuilder.
     */
    public AsyncFunction<Result<ProcessBuilder>> getProcessBuilder(String executablePath)
    {
        return getProcessBuilder(Path.parse(executablePath));
    }

    private AsyncFunction<Result<File>> getExecutableFile(final Path executablePath, boolean checkExtensions)
    {
        AsyncFunction<Result<File>> result;
        if (executablePath == null)
        {
            result = null;
        }
        else if (checkExtensions)
        {
            final File executableFile = getFileSystem().getFile(executablePath).getValue();
            result = executableFile.exists()
                .then(new Function1<Result<Boolean>, Result<File>>()
                {
                    @Override
                    public Result<File> run(Result<Boolean> fileExistsResult)
                    {
                        Result<File> result;
                        if (fileExistsResult.hasError())
                        {
                            result = Result.error(fileExistsResult.getError());
                        }
                        else if (!fileExistsResult.getValue())
                        {
                            result = Result.error(new FileNotFoundException(executablePath.toString()));
                        }
                        else
                        {
                            result = Result.success(executableFile);
                        }
                        return result;
                    }
                });
        }
        else
        {
            final Path executablePathWithoutExtension = executablePath.withoutFileExtension();

            final Path folderPath = executablePath.getParent();
            final Folder folder = getFileSystem().getFolder(folderPath).getValue();
            result = folder.getFiles()
                .then(new Function1<Result<Iterable<File>>, Result<File>>()
                {
                    @Override
                    public Result<File> run(Result<Iterable<File>> getFilesResult)
                    {
                        Result<File> executableFileResult;
                        if (getFilesResult.hasError())
                        {
                            executableFileResult = Result.error(getFilesResult.getError());
                        }
                        else
                        {
                            final File executableFile = getFilesResult.getValue().first(new Function1<File, Boolean>()
                            {
                                @Override
                                public Boolean run(File file)
                                {
                                    return executablePathWithoutExtension.equals(file.getPath().withoutFileExtension());
                                }
                            });
                            if (executableFile == null)
                            {
                                executableFileResult = Result.error(new FileNotFoundException(executablePathWithoutExtension.toString()));
                            }
                            else
                            {
                                executableFileResult = Result.success(executableFile);
                            }
                        }
                        return executableFileResult;
                    }
                });
        }
        return result;
    }

    private AsyncFunction<Result<File>> findExecutableFile(final Path executablePath, final boolean checkExtensions)
    {
        AsyncFunction<Result<File>> result;

        if(executablePath.isRooted())
        {
            result = getExecutableFile(executablePath, checkExtensions);
        }
        else
        {
            final Path currentFolderPath = getCurrentFolderPath();
            final Path currentFolderExecutablePath = currentFolderPath.concatenateSegment(executablePath);
            result = getExecutableFile(currentFolderExecutablePath, checkExtensions)
                .thenAsyncFunction(new Function1<Result<File>, AsyncFunction<Result<File>>>()
                {
                    @Override
                    public AsyncFunction<Result<File>> run(Result<File> getExecutableFileResult)
                    {
                        AsyncFunction<Result<File>> executableFileResult;
                        if (!getExecutableFileResult.hasError())
                        {
                            executableFileResult = Async.success(getFileSystem().getAsyncRunner(), getExecutableFileResult.getValue());
                        }
                        else
                        {
                            final String pathEnvironmentVariable = getEnvironmentVariable("path");
                            final Iterator<String> pathStrings = Array.fromValues(pathEnvironmentVariable.split(";")).iterate();
                            pathStrings.ensureHasStarted();

                            executableFileResult = checkNextPathString(pathStrings, executablePath, checkExtensions);
                        }
                        return executableFileResult;
                    }
                });
        }

        return result;
    }

    private AsyncFunction<Result<File>> checkNextPathString(final Iterator<String> pathStrings, final Path executablePath, final boolean checkExtensions)
    {
        AsyncFunction<Result<File>> result;
        if (!pathStrings.hasCurrent())
        {
            result = Async.error(getFileSystem().getAsyncRunner(), new FileNotFoundException(executablePath.toString()));
        }
        else
        {
            final Path path = Path.parse(pathStrings.takeCurrent());
            if (path == null)
            {
                result = checkNextPathString(pathStrings, executablePath, checkExtensions);
            }
            else
            {
                final Path resolvedExecutablePath = path.concatenateSegment(executablePath);
                result = getExecutableFile(resolvedExecutablePath, checkExtensions)
                    .thenAsyncFunction(new Function1<Result<File>, AsyncFunction<Result<File>>>()
                    {
                        @Override
                        public AsyncFunction<Result<File>> run(Result<File> getExecutableFileResult)
                        {
                            AsyncFunction<Result<File>> executableFileResult;
                            if (!getExecutableFileResult.hasError())
                            {
                                executableFileResult = Async.success(getFileSystem().getAsyncRunner(), getExecutableFileResult.getValue());
                            }
                            else
                            {
                                executableFileResult = checkNextPathString(pathStrings, executablePath, checkExtensions);
                            }
                            return executableFileResult;
                        }
                    });
            }
        }
        return result;
    }

    /**
     * Get a ProcessBuilder that references the provided executablePath.
     * @param executablePath The path to the executable to run from the returned ProcessBuilder.
     * @return The ProcessBuilder.
     */
    public AsyncFunction<Result<ProcessBuilder>> getProcessBuilder(final Path executablePath)
    {
        final AsyncRunner asyncRunner = getParallelAsyncRunner();
        AsyncFunction<Result<ProcessBuilder>> result;
        if (executablePath == null)
        {
            result = Async.error(asyncRunner, new IllegalArgumentException("executablePath cannot be null."));
        }
        else
        {
            result = findExecutableFile(executablePath, true)
                .thenAsyncFunction(new Function1<Result<File>, AsyncFunction<Result<File>>>()
                {
                    @Override
                    public AsyncFunction<Result<File>> run(Result<File> findExecutablePathResult)
                    {
                        return !findExecutablePathResult.hasError()
                            ? Async.success(asyncRunner, findExecutablePathResult.getValue())
                            : findExecutableFile(executablePath, false);
                    }
                })
                .then(new Function1<Result<File>, Result<ProcessBuilder>>()
                {
                    @Override
                    public Result<ProcessBuilder> run(Result<File> findExecutablePathResult)
                    {
                        return !findExecutablePathResult.hasError()
                            ? Result.success(new ProcessBuilder(getParallelAsyncRunner(), findExecutablePathResult.getValue()))
                            : Result.<ProcessBuilder>error(findExecutablePathResult.getError());
                    }
                });
        }
        return result;
    }

    /**
     * Get whether or not this application is running in a Windows environment.
     * @return Whether or not this application is running in a Windows environment.
     */
    public boolean onWindows()
    {
        final String osName = System.getProperty("os.name");
        return osName.toLowerCase().contains("windows");
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;
        if (disposed)
        {
            result = Result.<Boolean>success(false);
        }
        else
        {
            disposed = true;

            Throwable error = null;
            try
            {
                mainAsyncRunner.close();
            }
            catch (Throwable e)
            {
                error = e;
            }
            finally
            {
                if (parallelAsyncRunner != null)
                {
                    try
                    {
                        parallelAsyncRunner.close();
                    }
                    catch (Exception e)
                    {
                        error = (error == null ? e : ErrorIterable.from(Array.fromValues(new Throwable[] { error, e })));
                    }
                }
            }

            result = Result.done(disposed, error);
        }
        return result;
    }
}
