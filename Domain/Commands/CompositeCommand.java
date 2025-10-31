package Domain.Commands;

import java.util.ArrayList;

public class CompositeCommand implements Command {

    private final ArrayList<Command> commands;

    /**
     * Lege ctor
     */
    public CompositeCommand() {
        this.commands = new  ArrayList<Command>();
    }

    /**
     * Ctor met commands lijst
     */
    public CompositeCommand(ArrayList<Command> commands) {
        this.commands = new ArrayList<>(commands);
    }

    /**
     * Voeg een base command toe aan het composite command
     */
    public void add(Command command) {
        commands.add(command);
    }

    /**
     * Return de lijst van commands in de composite
     */
    public ArrayList<Command> getCommands() {
        return commands;
    }

    /**
     * Voer de commands uit
     */
    @Override
    public void execute() {
        //Loop over alle commands en execute ze afzonderlijk
        for (Command command : commands) {
            command.execute();
        }
    }
}
