package Application;

import java.util.ArrayList;

public class CompositeCommand implements Command {

    private final ArrayList<Command> commands;

    public CompositeCommand() {
        this.commands = new  ArrayList<Command>();
    }

    public CompositeCommand(ArrayList<Command> commands) {
        this.commands = new ArrayList<>(commands);
    }

    public void add(Command command) {
        commands.add(command);
    }

    @Override
    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }
}
