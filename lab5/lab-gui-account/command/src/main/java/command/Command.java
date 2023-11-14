package command;

public interface Command {

	void execute();

	public void undo();

	public void redo();

	String getName();
}
