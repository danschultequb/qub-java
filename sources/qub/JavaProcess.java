package qub;

/**
 * A Process object that exposes the platform functionality that a Java application can use.
 */
public class JavaProcess implements QubProcess
{
    private final CommandLineArguments commandLineArguments;
    private volatile int exitCode;

    private final Value<CharacterToByteWriteStream> outputWriteStream;
    private final Value<CharacterToByteWriteStream> errorWriteStream;
    private final Value<CharacterToByteReadStream> inputReadStream;
    private final Value<CharacterEncoding> characterEncoding;
    private final Value<String> lineSeparator;
    private final Value<Random> random;
    private final Value<FileSystem> fileSystem;
    private final Value<Network> network;
    private final Value<String> currentFolderPathString;
    private final Value<EnvironmentVariables> environmentVariables;
    private final Value<Synchronization> synchronization;
    private final Value<Function0<Stopwatch>> stopwatchCreator;
    private final Value<Clock> clock;
    private final Value<Iterable<Display>> displays;
    private final Value<ProcessFactory> processFactory;
    private final Value<DefaultApplicationLauncher> defaultApplicationLauncher;
    private final MutableMap<String,String> systemProperties;

    private final AsyncScheduler mainAsyncRunner;
    private final AsyncScheduler parallelAsyncRunner;

    private volatile boolean disposed;

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new JavaProcess.
     */
    public static JavaProcess create(String... commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return JavaProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new JavaProcess.
     */
    public static JavaProcess create(Iterable<String> commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return JavaProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new JavaProcess.
     */
    public static JavaProcess create(CommandLineArguments commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        return JavaProcess.create(commandLineArguments, new ManualAsyncRunner());
    }

    /**
     * Create a new JavaProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new JavaProcess.
     */
    public static JavaProcess create(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        return new JavaProcess(commandLineArguments, mainAsyncRunner);
    }

    private JavaProcess(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        this.commandLineArguments = commandLineArguments;

        this.outputWriteStream = Value.create();
        this.errorWriteStream = Value.create();
        this.inputReadStream = Value.create();
        this.characterEncoding = Value.create();
        this.lineSeparator = Value.create();

        this.random = Value.create();
        this.fileSystem = Value.create();
        this.network = Value.create();
        this.currentFolderPathString = Value.create();
        this.environmentVariables = Value.create();
        this.synchronization = Value.create();
        this.stopwatchCreator = Value.create();
        this.clock = Value.create();
        this.displays = Value.create();
        this.processFactory = Value.create();
        this.defaultApplicationLauncher = Value.create();
        this.systemProperties = Map.create();

        this.mainAsyncRunner = mainAsyncRunner;
        CurrentThread.setAsyncRunner(mainAsyncRunner);

        this.parallelAsyncRunner = new ParallelAsyncRunner();
    }

    @Override
    public int getExitCode()
    {
        return exitCode;
    }

    @Override
    public JavaProcess setExitCode(int exitCode)
    {
        this.exitCode = exitCode;
        return this;
    }

    @Override
    public JavaProcess incrementExitCode()
    {
        return setExitCode(getExitCode() + 1);
    }

    @Override
    public AsyncScheduler getMainAsyncRunner()
    {
        return mainAsyncRunner;
    }

    @Override
    public AsyncScheduler getParallelAsyncRunner()
    {
        return parallelAsyncRunner;
    }

    @Override
    public CommandLineArguments getCommandLineArguments()
    {
        return commandLineArguments;
    }

    @Override
    public CommandLineParameters createCommandLineParameters()
    {
        return new CommandLineParameters()
            .setArguments(getCommandLineArguments());
    }

    @Override
    public CharacterToByteWriteStream getOutputWriteStream()
    {
        if (!outputWriteStream.hasValue())
        {
            outputWriteStream.set(CharacterWriteStream.create(new OutputStreamToByteWriteStream(System.out))
                .setCharacterEncoding(this.getCharacterEncoding())
                .setNewLine(this.getLineSeparator()));
        }
        return outputWriteStream.get();
    }

    @Override
    public JavaProcess setOutputWriteStream(CharacterToByteWriteStream outputWriteStream)
    {
        PreCondition.assertNotNull(outputWriteStream, "outputWriteStream");

        this.outputWriteStream.set(outputWriteStream);

        return this;
    }

    @Override
    public CharacterToByteWriteStream getErrorWriteStream()
    {
        if (!errorWriteStream.hasValue())
        {
            errorWriteStream.set(CharacterWriteStream.create(new OutputStreamToByteWriteStream(System.err))
                .setCharacterEncoding(this.getCharacterEncoding())
                .setNewLine(this.getLineSeparator()));
        }
        return errorWriteStream.get();
    }

    @Override
    public JavaProcess setErrorWriteStream(CharacterToByteWriteStream errorWriteStream)
    {
        PreCondition.assertNotNull(errorWriteStream, "errorWriteStream");

        this.errorWriteStream.set(errorWriteStream);

        return this;
    }

    /**
     * Get the CharacterToByteReadStream that is assigned to this Console.
     * @return The CharacterToByteReadStream that is assigned to this Console.
     */
    public CharacterToByteReadStream getInputReadStream()
    {
        if (!inputReadStream.hasValue())
        {
            final InputStreamToByteReadStream inputByteReadStream = new InputStreamToByteReadStream(System.in);
            final CharacterEncoding characterEncoding = this.getCharacterEncoding();
            inputReadStream.set(CharacterToByteReadStream.create(inputByteReadStream)
                .setCharacterEncoding(characterEncoding));
        }
        return inputReadStream.get();
    }

    public JavaProcess setCharacterEncoding(CharacterEncoding characterEncoding)
    {
        PreCondition.assertNotNull(characterEncoding, "characterEncoding");

        this.characterEncoding.set(characterEncoding);
        if (this.outputWriteStream.hasValue())
        {
            this.outputWriteStream.get().setCharacterEncoding(characterEncoding);
        }
        if (this.errorWriteStream.hasValue())
        {
            this.errorWriteStream.get().setCharacterEncoding(characterEncoding);
        }
        if (this.inputReadStream.hasValue())
        {
            this.inputReadStream.get().setCharacterEncoding(characterEncoding);
        }
        return this;
    }

    public CharacterEncoding getCharacterEncoding()
    {
        if (!this.characterEncoding.hasValue())
        {
            this.characterEncoding.set(CharacterEncoding.UTF_8);
        }
        return this.characterEncoding.get();
    }

    public JavaProcess setLineSeparator(String lineSeparator)
    {
        this.lineSeparator.set(lineSeparator);
        if (this.outputWriteStream.hasValue())
        {
            this.outputWriteStream.get().setNewLine(lineSeparator);
        }
        if (this.errorWriteStream.hasValue())
        {
            this.errorWriteStream.get().setNewLine(lineSeparator);
        }
        return this;
    }

    public String getLineSeparator()
    {
        if (!lineSeparator.hasValue())
        {
            lineSeparator.set(this.onWindows().await() ? "\r\n" : "\n");
        }
        return lineSeparator.get();
    }

    /**
     * Set the ByteReadStream that is assigned to this Console's input.
     * @param inputByteReadStream The ByteReadStream that is assigned to this Console's input.
     * @return This object for method chaining.
     */
    public JavaProcess setInputReadStream(ByteReadStream inputByteReadStream)
    {
        return this.setInputReadStream(CharacterToByteReadStream.create(inputByteReadStream));
    }

    /**
     * Set the CharacterToByteReadStream that is assigned to this Console's input.
     * @param inputCharacterReadStream The CharacterToByteReadStream that is assigned to this Console's
     *                                 input.
     * @return This object for method chaining.
     */
    public JavaProcess setInputReadStream(CharacterToByteReadStream inputCharacterReadStream)
    {
        this.inputReadStream.set(inputCharacterReadStream);
        return this;
    }

    /**
     * Set the Random number generator assigned to this Console.
     * @param random The Random number generator assigned to this Console.
     */
    public JavaProcess setRandom(Random random)
    {
        this.random.set(random);
        return this;
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
            setFileSystem(new JavaFileSystem());
        }
        return fileSystem.get();
    }

    /**
     * Set the FileSystem that is assigned to this Console.
     * @param fileSystem The FileSystem that will be assigned to this Console.
     */
    public JavaProcess setFileSystem(FileSystem fileSystem)
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
        return this;
    }

    public Network getNetwork()
    {
        if (!network.hasValue())
        {
            setNetwork(JavaNetwork.create(this.getClock()));
        }
        return network.get();
    }

    public JavaProcess setNetwork(Network network)
    {
        this.network.set(network);
        return this;
    }

    public String getCurrentFolderPathString()
    {
        if (!currentFolderPathString.hasValue())
        {
            currentFolderPathString.set(java.nio.file.Paths.get(".").toAbsolutePath().normalize().toString());
        }
        return currentFolderPathString.get();
    }

    public JavaProcess setCurrentFolderPathString(String currentFolderPathString)
    {
        this.currentFolderPathString.set(currentFolderPathString);
        return this;
    }

    /**
     * Get the path to the folder that this Console is currently running in.
     * @return The path to the folder that this Console is currently running in.
     */
    public Path getCurrentFolderPath()
    {
        final String currentFolderPathString = getCurrentFolderPathString();
        return Strings.isNullOrEmpty(currentFolderPathString) ? null : Path.parse(currentFolderPathString);
    }

    /**
     * Set the path to the folder that this Console is currently running in.
     * @param currentFolderPath The folder to the path that this Console is currently running in.
     */
    public JavaProcess setCurrentFolderPath(Path currentFolderPath)
    {
        currentFolderPathString.set(currentFolderPath == null ? null : currentFolderPath.toString());
        return this;
    }

    public Result<Folder> getCurrentFolder()
    {
        final FileSystem fileSystem = getFileSystem();
        return fileSystem == null
            ? Result.error(new IllegalArgumentException("No FileSystem has been set."))
            : fileSystem.getFolder(getCurrentFolderPath());
    }

    /**
     * Set the environment variables that will be used by this Application.
     * @param environmentVariables The environment variables that will be used by this application.
     */
    public JavaProcess setEnvironmentVariables(EnvironmentVariables environmentVariables)
    {
        this.environmentVariables.set(environmentVariables);
        return this;
    }

    /**
     * Get the environment variables for this application.
     * @return The environment variables for this application.
     */
    public EnvironmentVariables getEnvironmentVariables()
    {
        if (!environmentVariables.hasValue())
        {
            final EnvironmentVariables environmentVariables = new EnvironmentVariables();
            for (final java.util.Map.Entry<String,String> entry : System.getenv().entrySet())
            {
                environmentVariables.set(entry.getKey(), entry.getValue());
            }
            this.environmentVariables.set(environmentVariables);
        }
        return environmentVariables.get();
    }

    /**
     * Get the value of the provided environment variable.
     * @param variableName The name of the environment variable.
     */
    public Result<String> getEnvironmentVariable(String variableName)
    {
        PreCondition.assertNotNullAndNotEmpty(variableName, "variableName");

        return this.getEnvironmentVariables().get(variableName);
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

    public JavaProcess setStopwatchCreator(Function0<Stopwatch> stopwatchCreator)
    {
        this.stopwatchCreator.set(stopwatchCreator);
        return this;
    }

    public Stopwatch getStopwatch()
    {
        if (!stopwatchCreator.hasValue())
        {
            stopwatchCreator.set(JavaStopwatch::new);
        }
        return stopwatchCreator.get() == null ? null : stopwatchCreator.get().run();
    }

    /**
     * Run the provided action and then write to output how long the action took.
     * @param action The action to run.
     */
    public void showDuration(Action0 action)
    {
        this.showDuration(true, action);
    }

    /**
     * Run the provided action and then write to output how long the action took.
     * @param shouldShowDuration Whether or not the duration will be written.
     * @param action The action to run.
     */
    public void showDuration(boolean shouldShowDuration, Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        Stopwatch stopwatch = null;
        if (shouldShowDuration)
        {
            stopwatch = this.getStopwatch();
            stopwatch.start();
        }
        try
        {
            action.run();
        }
        finally
        {
            if (shouldShowDuration)
            {
                final Duration compilationDuration = stopwatch.stop().toSeconds();
                final CharacterWriteStream output = this.getOutputWriteStream();
                output.writeLine("Done (" + compilationDuration.toString("0.0") + ")").await();
            }
        }
    }

    /**
     * Set the Clock object that this JavaProcess will use.
     * @param clock The Clock object that this JavaProcess will use.
     */
    public JavaProcess setClock(Clock clock)
    {
        this.clock.set(clock);
        return this;
    }

    /**
     * Get the Clock object that has been assigned to this Process.
     * @return The Clock object that has been assigned to this Process.
     */
    public Clock getClock()
    {
        if (!clock.hasValue())
        {
            clock.set(new JavaClock(getParallelAsyncRunner()));
        }
        return clock.get();
    }

    /**
     * Set the displays Iterable that this JavaProcess will use.
     * @param displays The displays Iterable that this JavaProcess will use.
     */
    public JavaProcess setDisplays(Iterable<Display> displays)
    {
        this.displays.set(displays);
        return this;
    }

    /**
     * Get the displays that have been assigned to this Process.
     * @return The displays that have been assigned to this Process.
     */
    public Iterable<Display> getDisplays()
    {
        if (!displays.hasValue())
        {
            final java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
            final int dpi = toolkit.getScreenResolution();

            final java.awt.GraphicsEnvironment graphicsEnvironment = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
            final java.awt.GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
            final List<Display> displayList = new ArrayList<>();

            if (graphicsDevices != null)
            {
                for (final java.awt.GraphicsDevice graphicsDevice : graphicsDevices)
                {
                    if (graphicsDevice != null)
                    {
                        final java.awt.GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
                        final java.awt.geom.AffineTransform defaultTransform = graphicsConfiguration.getDefaultTransform();
                        final double horizontalScale = defaultTransform.getScaleX();
                        final double verticalScale = defaultTransform.getScaleY();

                        final java.awt.DisplayMode displayMode = graphicsDevice.getDisplayMode();
                        displayList.add(new Display(displayMode.getWidth(), displayMode.getHeight(), dpi, dpi, horizontalScale, verticalScale));
                    }
                }
            }

            displays.set(displayList);
        }
        return displays.get();
    }

    /**
     * Get the object that can be used to invoke external processes.
     * @return The object that can be used to invoke external processes.
     */
    public ProcessFactory getProcessFactory()
    {
        if (!this.processFactory.hasValue())
        {
            final AsyncRunner parallelAsyncRunner = this.getParallelAsyncRunner();
            final EnvironmentVariables environmentVariables = this.getEnvironmentVariables();
            final Folder currentFolder = this.getCurrentFolder().await();
            this.processFactory.set(new RealProcessFactory(parallelAsyncRunner, environmentVariables, currentFolder));
        }
        return this.processFactory.get();
    }

    /**
     * Set the object that can be used to invoke external processes.
     * @param processFactory The object that can be used to invoke external processes.
     * @return This object for method chaining.
     */
    public JavaProcess setProcessFactory(ProcessFactory processFactory)
    {
        PreCondition.assertNotNull(processFactory, "processFactory");

        this.processFactory.set(processFactory);

        return this;
    }

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    public Result<ProcessBuilder> getProcessBuilder(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return this.getProcessFactory().getProcessBuilder(executablePath);
    }

    /**
     * Get the ProcessBuilder for the provided executable path.
     * @param executablePath The path to the executable.
     * @return The ProcessBuilder for the provided executable path.
     */
    public Result<ProcessBuilder> getProcessBuilder(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return this.getProcessFactory().getProcessBuilder(executablePath);
    }

    /**
     * Get the ProcessBuilder for the provided executable file.
     * @param executableFile The file to executable.
     * @return The ProcessBuilder for the provided executable file.
     */
    public Result<ProcessBuilder> getProcessBuilder(File executableFile)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");

        return this.getProcessFactory().getProcessBuilder(executableFile);
    }

    /**
     * Get the DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     * @return The DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     */
    public DefaultApplicationLauncher getDefaultApplicationLauncher()
    {
        if (!this.defaultApplicationLauncher.hasValue())
        {
            this.defaultApplicationLauncher.set(new RealDefaultApplicationLauncher());
        }
        return this.defaultApplicationLauncher.get();
    }

    /**
     * Set the DefaultApplicationLauncher that will be used to open files with their registered
     * default application.
     * @param defaultApplicationLauncher The DefaultApplicationLauncher that will be used to open files with their
     *                                   registered default application.
     * @return This object for method chaining.
     */
    public JavaProcess setDefaultApplicationLauncher(DefaultApplicationLauncher defaultApplicationLauncher)
    {
        PreCondition.assertNotNull(defaultApplicationLauncher, "defaultApplicationLauncher");

        this.defaultApplicationLauncher.set(defaultApplicationLauncher);

        return this;
    }

    /**
     * Open the provided file path with the registered default application.
     * @param filePathToOpen The file path to open.
     * @return The result of opening the file path.
     */
    public Result<Void> openFileWithDefaultApplication(String filePathToOpen)
    {
        PreCondition.assertNotNullAndNotEmpty(filePathToOpen, "file");

        final DefaultApplicationLauncher launcher = this.getDefaultApplicationLauncher();
        return launcher.openFileWithDefaultApplication(filePathToOpen);
    }

    /**
     * Open the provided file path with the registered default application.
     * @param filePathToOpen The file path to open.
     * @return The result of opening the file path.
     */
    public Result<Void> openFileWithDefaultApplication(Path filePathToOpen)
    {
        PreCondition.assertNotNull(filePathToOpen, "file");

        final DefaultApplicationLauncher launcher = this.getDefaultApplicationLauncher();
        return launcher.openFileWithDefaultApplication(filePathToOpen);
    }

    /**
     * Open the provided file with the registered default application.
     * @param file The file to open.
     * @return The result of opening the file.
     */
    public Result<Void> openFileWithDefaultApplication(File file)
    {
        PreCondition.assertNotNull(file, "file");

        final DefaultApplicationLauncher launcher = this.getDefaultApplicationLauncher();
        return launcher.openFileWithDefaultApplication(file);
    }

    @Override
    public JavaProcess setSystemProperty(String systemPropertyName, String systemPropertyValue)
    {
        PreCondition.assertNotNullAndNotEmpty(systemPropertyName, "systemPropertyName");
        PreCondition.assertNotNull(systemPropertyValue, "systemPropertyValue");

        this.systemProperties.set(systemPropertyName, systemPropertyValue);

        return this;
    }

    @Override
    public Map<String,String> getSystemProperties()
    {
        final java.util.Properties properties = java.lang.System.getProperties();
        for (final java.util.Map.Entry<Object,Object> property : properties.entrySet())
        {
            final String propertyName = Objects.toString(property.getKey());
            if (!this.systemProperties.containsKey(propertyName))
            {
                this.systemProperties.set(propertyName, Objects.toString(property.getValue()));
            }
        }

        return this.systemProperties;
    }

    @Override
    public Result<String> getSystemProperty(String systemPropertyName)
    {
        PreCondition.assertNotNullAndNotEmpty(systemPropertyName, "systemPropertyName");

        return this.systemProperties.get(systemPropertyName)
                .catchError(NotFoundException.class, () ->
                {
                    final String systemPropertyValue = java.lang.System.getProperty(systemPropertyName);
                    if (systemPropertyValue == null)
                    {
                        throw new NotFoundException("No system property found with the name " + Strings.escapeAndQuote(systemPropertyName) + ".");
                    }
                    this.systemProperties.set(systemPropertyName, systemPropertyValue);
                    return systemPropertyValue;
                });
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
            result = Result.successFalse();
        }
        else
        {
            disposed = true;
            result = Result.successTrue();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
