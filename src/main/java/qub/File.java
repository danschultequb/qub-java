package qub;

public class File extends FileSystemEntry
{
    File(FileSystem fileSystem, String path)
    {
        this(fileSystem, Path.parse(path));
    }

    File(FileSystem fileSystem, Path path)
    {
        super(fileSystem, path);
    }

    /**
     * Create this File and return whether or not it was created as a result of this function.
     * @return Whether or not this function created the file.
     */
    public boolean create()
    {
        return getFileSystem().createFile(getPath());
    }

    /**
     * Create this File with the provided contents and return whether or not it was created as a
     * result of this function.
     * @param contents The contents to create the file with if the file is created as a result of
     *                 this function.
     * @return Whether or not this function created the file.
     */
    public boolean create(byte[] contents)
    {
        return getFileSystem().createFile(getPath(), contents);
    }

    /**
     * Get whether or not this File exists.
     */
    @Override
    public boolean exists()
    {
        return getFileSystem().fileExists(getPath());
    }

    /**
     * Get the entire contents of this File as a single byte[]. If this file doesn't exist, then
     * null will be returned.
     * @return The entire contents of this File as a single byte[], or null if this file doesn't
     * exist.
     */
    public byte[] getContents()
    {
        final FileSystem fileSystem = getFileSystem();
        final Path path = getPath();
        return fileSystem.getFileContents(path);
    }
}
