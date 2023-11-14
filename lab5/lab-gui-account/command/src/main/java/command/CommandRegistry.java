package command;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommandRegistry {

	private final ObservableList<Command> commandStack = FXCollections
			.observableArrayList();
	private final ObservableList<Command> undidCommandStack = FXCollections
			.observableArrayList();

	public void executeCommand(Command command) {
		command.execute();
		commandStack.add(command);

		undidCommandStack.retainAll();
	}

	public void redo() {
		if (undidCommandStack.isEmpty()) {
			return;
		}

		Command commandToRedo = undidCommandStack.remove(undidCommandStack.size()-1);
		commandToRedo.execute();
		commandStack.add(commandToRedo);
	}

	public void undo() {
		if (commandStack.isEmpty()) {
			return;
		}

		Command commandToUndo = commandStack.remove(commandStack.size()-1);
		commandToUndo.undo();
		undidCommandStack.add(commandToUndo);
	}

	public ObservableList<Command> getCommandStack() {
		return commandStack;
	}
}
