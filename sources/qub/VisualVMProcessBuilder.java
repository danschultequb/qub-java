package qub;

public class VisualVMProcessBuilder extends ProcessBuilderDecorator<VisualVMProcessBuilder> implements VisualVMArguments<VisualVMProcessBuilder>
{
    public static final String executablePathString = "visualvm.exe";
    public static final Path executablePath = Path.parse(VisualVMProcessBuilder.executablePathString);

    private VisualVMProcessBuilder(ProcessBuilder processBuilder)
    {
        super(processBuilder);
    }

    /**
     * Get a VisualVMProcessBuilder from the provided DesktopProcess.
     * @param process The DesktopProcess to get the VisualVMProcessBuilder from.
     * @return The VisualVMProcessBuilder.
     */
    public static Result<VisualVMProcessBuilder> get(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        return Result.create(() ->
        {
            final QubFolder qubFolder = process.getQubFolder().await();
            final QubProjectFolder visualVmFolder = qubFolder.getProjectFolder("oracle", "visualvm").await();
            final QubProjectVersionFolder visualVmVersionFolder = visualVmFolder.getLatestProjectVersionFolder().await();
            final File visualVmExeFile = visualVmVersionFolder.getFile("bin/visualvm.exe").await();
            return VisualVMProcessBuilder.get(process.getProcessFactory(), visualVmExeFile.getPath()).await();
        });
    }

    /**
     * Get a VisualVMProcessBuilder from the provided DesktopProcess.
     * @param process The DesktopProcess to get the VisualVMProcessBuilder from.
     * @return The VisualVMProcessBuilder.
     */
    @Deprecated
    public static Result<VisualVMProcessBuilder> get(Process process)
    {
        PreCondition.assertNotNull(process, "process");

        return Result.create(() ->
        {
            final EnvironmentVariables environmentVariables = process.getEnvironmentVariables();
            final String qubHome = environmentVariables.get("QUB_HOME").await();
            final Folder folder = process.getFileSystem().getFolder(qubHome).await();
            final QubFolder qubFolder = QubFolder.get(folder);
            final QubProjectFolder visualVmFolder = qubFolder.getProjectFolder("oracle", "visualvm").await();
            final QubProjectVersionFolder visualVmVersionFolder = visualVmFolder.getLatestProjectVersionFolder().await();
            final File visualVmExeFile = visualVmVersionFolder.getFile("bin/visualvm.exe").await();
            return VisualVMProcessBuilder.get(process.getProcessFactory(), visualVmExeFile.getPath()).await();
        });
    }

    /**
     * Get a VisualVMProcessBuilder from the provided ProcessFactory.
     * @param processFactory The ProcessFactory to get the VisualVMProcessBuilder from.
     * @return The VisualVMProcessBuilder.
     */
    public static Result<VisualVMProcessBuilder> get(ProcessFactory processFactory, Path visualVmExePath)
    {
        PreCondition.assertNotNull(processFactory, "processFactory");
        PreCondition.assertNotNull(visualVmExePath, "visualVmExePath");
        PreCondition.assertTrue(visualVmExePath.isRooted(), "visualVmExePath.isRooted()");

        return Result.create(() ->
        {
            return new VisualVMProcessBuilder(processFactory.getProcessBuilder(visualVmExePath).await());
        });
    }
}
