package duke.ui;

import duke.DukeException;
import duke.Task;
import duke.TaskList;
import duke.TaskToDo;
import duke.TaskDeadline;
import duke.TaskEvent;
import duke.command.Command;
import duke.command.Command;
import duke.command.CommandAddDeadline;
import duke.command.CommandAddEvent;
import duke.command.CommandAddToDo;
import duke.command.CommandDelete;
import duke.command.CommandDone;
import duke.command.CommandList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final Pattern COMMAND_INPUT_FORMAT = Pattern.compile("(?<command>^[ltde]\\w+)" + "\\s?"
            + "(?<arguments>.*)");

    private TaskList taskList;
    private Ui ui;

    public Parser(TaskList taskList, Ui ui) {
        this.taskList = taskList;
        this.ui = ui;
    }

    public Command getCommandFromInput(String input) throws DukeException {
        Matcher matcher = COMMAND_INPUT_FORMAT.matcher(input);
        if (!matcher.matches()) {
            throw new DukeException("☹ BLEHHHHHH!!! I'm (not) sorry, but I don't know what that means :/");
        }
        String command = matcher.group("command").toLowerCase();
        String arguments = matcher.group("arguments");
        switch (command) {
        case CommandList.COMMAND_STRING:
            return new CommandList(taskList, ui);
        case CommandDone.COMMAND_STRING:
            return new CommandDone(taskList, ui, parseDoneOrDelete(arguments));
        case CommandDelete.COMMAND_STRING:
            return new CommandDelete(taskList, ui, parseDoneOrDelete(arguments));
        case CommandAddToDo.COMMAND_STRING:
            return new CommandAddToDo(taskList, ui, new TaskToDo(arguments));
        case CommandAddEvent.COMMAND_STRING:
            return new CommandAddEvent(taskList, ui, parseEvent(arguments));
        case CommandAddDeadline.COMMAND_STRING:
            return new CommandAddDeadline(taskList, ui, parseDeadline(arguments));
        default:
            throw new DukeException("☹ BLEHHHHHH!!! I'm (not) sorry, but I don't know what that means :/");
        }
    }

    private int parseDoneOrDelete(String arguments) throws DukeException {
        Pattern integerPattern = Pattern.compile("(?<integer>\\d+)\\s?");
        Matcher matcher = integerPattern.matcher(arguments);
        if (matcher.matches()) {
            int index = Integer.parseInt(matcher.group("integer"));
            if (index < 0 || index > taskList.getSize() - 1) {
                throw new DukeException(String.format("☹ BLEHHHHHH. Task no. %d does not exist. Please try again.", (index + 1)));
            } else {
                return index;
            }
        } else {
            throw new DukeException("☹ BLEHHHHHH. What are you talking about?? Tell me which task??");
        }
    }


    private static Task parseEvent(String arguments) throws DukeException {
        String[] output = arguments.split(" /");
        if (output.length < 2) {
            throw new DukeException(String.format("☹ BLEHHHHHH!!! The description of an event cannot be empty."));
        }
        String description = output[0];
        String pattern = ("(at?)(\\s)(\\S+)(\\s)(\\d{4})([-])(\\d{4})");
        LocalDate eventDate = LocalDate.parse(output[1].replaceAll(pattern, "$3"));
        String startTemp = output[1].replaceAll(pattern, "$5");
        String endTemp = output[1].replaceAll(pattern, "$7");
        LocalTime startTime = LocalTime.parse(startTemp.substring(0,2) + ":"
                + startTemp.substring(2,4));
        LocalTime endTime = LocalTime.parse(endTemp.substring(0,2) + ":"
                + endTemp.substring(2,4));
        return new TaskEvent(description, eventDate, startTime, endTime);
    }

    private static Task parseDeadline(String arguments) throws DukeException {
        String[] output = arguments.split(" /");
        if (output.length < 2) {
            throw new DukeException(String.format("☹ BLEHHHHHH!!! The description of a deadline cannot be empty."));
        }
        String description = output[0];
        String pattern = ("(by?)(\\s)([\\S]+)(\\s)([\\S]+)");
        LocalDate deadlineDate = LocalDate.parse(output[1].replaceAll(pattern, "$3"));
        String timeTemp = output[1].replaceAll(pattern, "$5");
        LocalTime deadlineTime = LocalTime.parse(timeTemp.substring(0,2)
                + ":" + timeTemp.substring(2,4));
        LocalDateTime deadline = deadlineDate.atTime(deadlineTime);
        return new TaskDeadline(description, deadline);
    }
}