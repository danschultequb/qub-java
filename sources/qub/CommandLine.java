package qub;

public class CommandLine implements Indexable<CommandLineArgument>
{
    private final List<CommandLineArgument> arguments;

    private CommandLine(List<CommandLineArgument> arguments)
    {
        this.arguments = arguments;
    }

    public Indexable<String> getArgumentStrings()
    {
        final List<String> result = new ArrayList<>();
        for (final CommandLineArgument argument : arguments)
        {
            result.add(argument.toString());
        }
        return result;
    }

    @Override
    public int getCount()
    {
        return arguments.getCount();
    }

    public Indexable<CommandLineArgument> getArguments()
    {
        return arguments;
    }

    @Override
    public Iterator<CommandLineArgument> iterate()
    {
        return arguments.iterate();
    }

    @Override
    public CommandLineArgument get(int index)
    {
        return arguments.get(index);
    }

    public CommandLineArgument get(String argumentName)
    {
        CommandLineArgument result = null;
        if (argumentName != null && !argumentName.isEmpty() && arguments.any())
        {
            for (final CommandLineArgument argument : arguments)
            {
                if(argumentName.equalsIgnoreCase(argument.getName()))
                {
                    result = argument;
                    break;
                }
            }
        }
        return result;
    }

    public String getValue(String argumentName)
    {
        final CommandLineArgument argument = get(argumentName);
        return argument == null ? null : argument.getValue();
    }

    public CommandLineArgument removeAt(int index)
    {
        return arguments.removeAt(index);
    }

    public CommandLineArgument remove(final String argumentName)
    {
        return arguments.removeFirst(new Function1<CommandLineArgument, Boolean>()
        {
            @Override
            public Boolean run(CommandLineArgument commandLineArgument)
            {
                final String commandLineArgumentName = commandLineArgument.getName();
                return commandLineArgumentName != null && commandLineArgumentName.equalsIgnoreCase(argumentName);
            }
        });
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }

    public static CommandLine parse(String... rawCommandLineArguments)
    {
        PreCondition.assertNotNull(rawCommandLineArguments, "rawCommandLineArguments");

        return parse(Iterable.create(rawCommandLineArguments));
    }

    public static CommandLine parse(Iterable<String> rawCommandLineArguments)
    {
        PreCondition.assertNotNull(rawCommandLineArguments, "rawCommandLineArguments");

        final List<CommandLineArgument> commandLineArguments = new ArrayList<>();
        if (rawCommandLineArguments.any())
        {
            for (final String rawCommandLineArgument : rawCommandLineArguments)
            {
                commandLineArguments.add(new CommandLineArgument(rawCommandLineArgument));
            }
        }
        return new CommandLine(commandLineArguments);
    }
}
