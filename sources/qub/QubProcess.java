package qub;

/**
 * An interface for a process/application that runs from the Qub folder.
 */
@Deprecated
public interface QubProcess extends Process
{
    /**
     * Create a new QubProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new QubProcess.
     */
    static QubProcess create(String... commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return QubProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new QubProcess object with the provided command line arguments.
     * @param commandLineArgumentStrings The command line arguments provided to the new QubProcess.
     */
    static QubProcess create(Iterable<String> commandLineArgumentStrings)
    {
        PreCondition.assertNotNull(commandLineArgumentStrings, "commandLineArgumentStrings");

        return QubProcess.create(CommandLineArguments.create(commandLineArgumentStrings));
    }

    /**
     * Create a new QubProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new QubProcess.
     */
    static QubProcess create(CommandLineArguments commandLineArguments)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");

        return QubProcess.create(commandLineArguments, new ManualAsyncRunner());
    }

    /**
     * Create a new QubProcess object with the provided command line arguments.
     * @param commandLineArguments The command line arguments provided to the new QubProcess.
     */
    static QubProcess create(CommandLineArguments commandLineArguments, AsyncScheduler mainAsyncRunner)
    {
        PreCondition.assertNotNull(commandLineArguments, "commandLineArguments");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        return DesktopProcess.create(commandLineArguments, mainAsyncRunner);
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param args The String arguments provided.
     * @param main The main function that will be run.
     */
    public static void run(String[] args, Action1<DesktopProcess> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        DesktopProcess.run(() -> DesktopProcess.create(args), main);
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param main The main function that will be run.
     */
    public static <TProcess extends DesktopProcess> void run(Function0<TProcess> processCreator, Action1<TProcess> main)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(main, "main");

        final TProcess process = processCreator.run();
        try
        {
            main.run(process);
        }
        catch (Throwable error)
        {
            Exceptions.writeErrorString(process.getErrorWriteStream(), error).await();
            process.setExitCode(1);
        }
        finally
        {
            process.dispose().await();
            java.lang.System.exit(process.getExitCode());
        }
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param args The String arguments provided.
     * @param main The main function that will be run.
     */
    public static void run(String[] args, Function1<DesktopProcess,Integer> main)
    {
        PreCondition.assertNotNull(args, "args");
        PreCondition.assertNotNull(main, "main");

        DesktopProcess.run(() -> DesktopProcess.create(args), DesktopProcess.getMainAction(main));
    }

    /**
     * Run the provided console main function using the provided String arguments. This function
     * will not return because it calls java.lang.System.exit() using the exit code set in the main
     * function.
     * @param main The main function that will be run.
     */
    public static <TProcess extends DesktopProcess> void run(Function0<TProcess> processCreator, Function1<TProcess,Integer> main)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(main, "main");

        DesktopProcess.run(processCreator, DesktopProcess.getMainAction(main));
    }

    public static <TProcess extends DesktopProcess> Action1<TProcess> getMainAction(Function1<TProcess,Integer> runFunction)
    {
        PreCondition.assertNotNull(runFunction, "runFunction");

        return (TProcess process) ->
        {
            process.setExitCode(runFunction.run(process));
        };
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param arguments The command line arguments to the application.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runAction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    public static <TParameters> void run(String[] arguments, Function1<DesktopProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        DesktopProcess.run(arguments, DesktopProcess.getMainAction(getParametersFunction, runAction));
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runAction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    public static <TProcess extends DesktopProcess,TParameters> void run(Function0<TProcess> processCreator, Function1<TProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        DesktopProcess.run(processCreator, DesktopProcess.getMainAction(getParametersFunction, runAction));
    }

    public static <TProcess extends DesktopProcess,TParameters> Action1<TProcess> getMainAction(Function1<TProcess,TParameters> getParametersFunction, Action1<TParameters> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (TProcess process) ->
        {
            final TParameters parameters = getParametersFunction.run(process);
            if (parameters != null)
            {
                runAction.run(parameters);
            }
        };
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param arguments The command line arguments to the application.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runFunction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    public static <TParameters> void run(String[] arguments, Function1<DesktopProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
    {
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runFunction, "runFunction");

        DesktopProcess.run(arguments, DesktopProcess.getMainAction(getParametersFunction, runFunction));
    }

    /**
     * Invoke the provided runAction using the parsed command line parameters that are provided by
     * the getParametersFunction. If the command line parameters are null, then the runAction will
     * not be invoked.
     * @param getParametersFunction The function that will parse the command line parameters from
     *                              the Process's command line arguments.
     * @param runFunction The action that implements the application's main logic.
     * @param <TParameters> The type of the command line parameters object.
     */
    public static <TProcess extends DesktopProcess,TParameters> void run(Function0<TProcess> processCreator, Function1<TProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runFunction)
    {
        PreCondition.assertNotNull(processCreator, "processCreator");
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runFunction, "runFunction");

        DesktopProcess.run(processCreator, DesktopProcess.getMainAction(getParametersFunction, runFunction));
    }

    public static <TProcess extends DesktopProcess,TParameters> Action1<TProcess> getMainAction(Function1<TProcess,TParameters> getParametersFunction, Function1<TParameters,Integer> runAction)
    {
        PreCondition.assertNotNull(getParametersFunction, "getParametersFunction");
        PreCondition.assertNotNull(runAction, "runAction");

        return (TProcess process) ->
        {
            final TParameters parameters = getParametersFunction.run(process);
            if (parameters != null)
            {
                process.setExitCode(runAction.run(parameters));
            }
        };
    }

    /**
     * Get the Qub folder that contains the main binaries for this process.
     * @return The Qub folder that contains the main binaries for this process.
     */
    default Result<QubFolder> getQubFolder()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getQubFolder().await();
        });
    }

    /**
     * Get the name of the current process's publisher.
     * @return The name of the current process's publisher.
     */
    default Result<String> getPublisherName()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getPublisherName().await();
        });
    }

    default Result<QubPublisherFolder> getQubPublisherFolder()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getPublisherFolder().await();
        });
    }

    /**
     * Get the name of the current process's project.
     * @return The name of the current process's project.
     */
    default Result<String> getProjectName()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getProjectName().await();
        });
    }

    default Result<QubProjectFolder> getQubProjectFolder()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getProjectFolder().await();
        });
    }

    /**
     * Get the data folder that is associated with the current process's project.
     * @return The data folder that is associated with the current process's project.
     */
    default Result<Folder> getQubProjectDataFolder()
    {
        return Result.create(() ->
        {
            final QubProjectFolder projectFolder = this.getQubProjectFolder().await();
            return projectFolder.getProjectDataFolder().await();
        });
    }

    /**
     * Get the version of the current process's project.
     * @return The version of the current process's project.
     */
    default Result<VersionNumber> getVersion()
    {
        return Result.create(() ->
        {
            return this.getQubProjectVersionFolder().await()
                .getVersion().await();
        });
    }

    default String getMainClassFullName()
    {
        final String javaApplicationArguments = this.getSystemProperty("sun.java.command").await();
        final int firstSpaceIndex = javaApplicationArguments.indexOf(' ');
        final String result = firstSpaceIndex == -1 ? javaApplicationArguments : javaApplicationArguments.substring(0, firstSpaceIndex);

        PostCondition.assertNotNullAndNotEmpty(result, "result");

        return result;
    }

    /**
     * Get the QubProjectVersionFolder for the current process.
     * @return The QubProjectVersionFolder for the current process.
     */
    default Result<QubProjectVersionFolder> getQubProjectVersionFolder()
    {
        return Result.create(() ->
        {
            QubProjectVersionFolder result;

            final String mainClassFullName = this.getMainClassFullName();
            final TypeLoader typeLoader = this.getTypeLoader();
            final Path typeContainerPath = typeLoader.getTypeContainerPath(mainClassFullName).await();
            final FileSystem fileSystem = this.getFileSystem();
            final File projectVersionFile = fileSystem.getFile(typeContainerPath).await();
            if (projectVersionFile.exists().await())
            {
                result = QubProjectVersionFolder.get(projectVersionFile.getParentFolder().await());
            }
            else
            {
                final Folder projectVersionFolder = fileSystem.getFolder(typeContainerPath).await();
                if (projectVersionFolder.exists().await() || typeContainerPath.endsWith('/') || typeContainerPath.endsWith('\\'))
                {
                    result = QubProjectVersionFolder.get(projectVersionFolder);
                }
                else
                {
                    result = QubProjectVersionFolder.get(projectVersionFile.getParentFolder().await());
                }
            }

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    /**
     * Create a CommandLineParameters object that can be used to create CommandLineParameter
     * objects. These CommandLineParameter objects can parse the CommandLineArguments that are
     * passed on the command line.
     * @return A new CommandLineParameters object.
     */
    default CommandLineParameters createCommandLineParameters()
    {
        return new CommandLineParameters()
            .setArguments(this.getCommandLineArguments());
    }

    /**
     * Get the CommandLineArguments that were passed on the command line.
     * @return The CommandLineArguments that were passed on the command line.
     */
    CommandLineArguments getCommandLineArguments();

    @Deprecated
    default Stopwatch getStopwatch()
    {
        return this.getClock().createStopwatch();
    }

    @Deprecated
    Process setOutputWriteStream(CharacterToByteWriteStream output);

    @Deprecated
    Process setErrorWriteStream(CharacterToByteWriteStream error);

    /**
     * Get the version of Java that is running this application.
     * @return The version of Java that is running this application.
     */
    @Deprecated
    default VersionNumber getJavaVersion()
    {
        final String javaVersionString = this.getSystemProperty("java.version").await();
        final VersionNumber result = VersionNumber.parse(javaVersionString).await();

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    /**
     * Set the exit code that this process will return when it finishes.
     * @param exitCode The exit code that this process will return when it finishes.
     * @return This DesktopProcess for method chaining.
     */
    @Deprecated
    Process setExitCode(int exitCode);
}
