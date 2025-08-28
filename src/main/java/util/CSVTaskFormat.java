package util;

import entities.manager.HistoryManager;
import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CSVTaskFormat {

    private CSVTaskFormat() {
    }

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String toString(Task task) {
        if (task instanceof Subtask) {
            return String.format("%d,%s,%s,%s,%s,%d".formatted(
                    task.getId(),
                    task.getClass().getSimpleName(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getDescription(),
                    ((Subtask) task).getEpicId()));
        }
        return String.format("%d,%s,%s,%s,%s".formatted(
                task.getId(),
                task.getClass().getSimpleName(),
                task.getTitle(),
                task.getStatus(),
                task.getDescription()));

    }

    public static String toString(Map<Integer, Task> collection) {
        StringBuilder result = new StringBuilder();
        collection.forEach((k, v) -> {
            result.append(toString(v)).append("\n");
        });
        return result.toString();
    }

    public static String toString(HistoryManager historyManager) {
        if (historyManager.getHistory().isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        historyManager.getHistory().forEach(element -> {
            result.append(",").append(element.getId());
        });
        if (result.charAt(0) == ',') result.deleteCharAt(0);
        return result.toString();
    }

    public static List<Integer> getHistoryIdsListFromString(String str) {

        return Arrays.stream(str.split(",")).map(Integer::valueOf).toList();
    }

    public static Task getTaskFromString(String line) {
        String[] splitLine = line.split(",");
        return new Task(Integer.valueOf(splitLine[0]), splitLine[2], splitLine[4], getStatusFromString(splitLine[3]));
    }

    public static Epic getEpicFromString(String line) {
        String[] splitLine = line.split(",");
        return new Epic(Integer.parseInt(splitLine[0]), splitLine[2], splitLine[3]);
    }

    public static Subtask getSubtaskFromString(String line) {
        String[] splitLine = line.split(",");
        return new Subtask(Integer.parseInt(splitLine[0]), splitLine[2],
                splitLine[4], getStatusFromString(splitLine[3]), Integer.parseInt(splitLine[5]));
    }


    private static Status getStatusFromString(String statusAsString) {
        if (statusAsString.equalsIgnoreCase("NEW"))
            return Status.NEW;
        else if (statusAsString.equalsIgnoreCase("IN_PROGRESS"))
            return Status.IN_PROGRESS;
        else
            return Status.DONE;
    }


}

