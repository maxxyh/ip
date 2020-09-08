package duke.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import duke.commands.CommandAdd;
import duke.commands.CommandAddDeadline;
import duke.commands.CommandAddEvent;
import duke.tasks.TaskDeadline;
import duke.tasks.TaskEvent;
import duke.tasks.TaskList;
import javafx.scene.layout.VBox;

public class ParserTest {

    @Test
    public void getCommandFromInput_eventDescriptionDurationString_createsAddEventCommand() {
        String input = "event john mayer concert /at 2020-09-10 1800-2200";
        TaskList taskList = new TaskList();
        Ui ui = new Ui(System.in, new VBox());
        Parser parser = new Parser(taskList, ui);
        CommandAdd expectedCommand = new CommandAddEvent(taskList, ui,
                new TaskEvent("john mayer concert", LocalDate.parse("2020-09-10"),
                        LocalTime.parse("18:00"), LocalTime.parse("22:00")));
        assertEquals(expectedCommand.getTask().toString(), (
                (CommandAdd) parser.getCommandFromInput(input)).getTask().toString());
    }

    @Test
    public void getCommandFromInput_deadlineDescriptionDeadlineString_createsAddDeadlineCommand() {
        String input = "deadline mop floor /by 2020-09-10 2000";
        TaskList taskList = new TaskList();
        Ui ui = new Ui(System.in, new VBox());
        Parser parser = new Parser(taskList, ui);
        CommandAdd expectedCommand = new CommandAddDeadline(taskList, ui,
                new TaskDeadline("mop floor", LocalDateTime.of(2020, 9, 10, 20, 0)));
        assertEquals(expectedCommand.getTask().toString(), (
                (CommandAdd) parser.getCommandFromInput(input)).getTask().toString());
    }
}
