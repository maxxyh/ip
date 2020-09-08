package duke;

import static duke.storage.Storage.DEFAULT_STORAGE_FILEPATH;

import duke.commands.Command;
import duke.exceptions.DukeException;
import duke.storage.Storage;
import duke.storage.Storage.StorageOperationException;
import duke.tasks.TaskList;
import duke.ui.Parser;
import duke.ui.Ui;

/**
 * Manages tasks and deadlines in a chat bot style.
 * <p>
 * Duke supports CRUD operations for 3 types of tasks:
 * <li>Todo with description</li>
 * <li>Events with duration</li>
 * <li>Deadlines</li>
 * </p>
 */
public class Duke {

    private Storage storage;
    private TaskList taskList;
    private Parser parser;
    private Ui ui;

    public Duke(Ui ui) {
        this(DEFAULT_STORAGE_FILEPATH, ui);
    }

    /**
     * Constructor for Duke.
     * <p>
     * Loads tasks from save file into taskList.
     * </p>
     *
     * @param filePath save file path.
     * @param ui Ui manager.
     */
    public Duke(String filePath, Ui ui) {
        try {
            storage = new Storage(filePath);
            taskList = storage.loadTasks();
            this.ui = ui;
            parser = new Parser(taskList, this.ui);
        } catch (StorageOperationException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Responds to user string input.
     * <p>
     * Able to handle and print DukeException.
     * If "bye", saves locally and exits.
     * </p>
     *
     * @param input
     * @return true terminate if bye
     */
    public boolean respondToUserInput(String input) {
        if (input.equals("bye")) {
            ui.showGoodbyeMessage();
            try {
                storage.saveTasks(taskList);
            } catch (StorageOperationException soe) {
                System.out.println(soe.getMessage());
            }
            return true;
        }

        try {
            Command command = parser.getCommandFromInput(input);
            command.execute();
        } catch (DukeException e) {
            ui.outputBlockToUser(e.getMessage());
        }
        return false;
    }

    /**
     * Starts Duke by showing the welcome message.
     */
    public void startDuke() {
        ui.showWelcomeMessage();
    }
}
