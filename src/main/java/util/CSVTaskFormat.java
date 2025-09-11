package util;

import entities.manager.HistoryManager;
import entities.tasks.Epic;
import entities.tasks.Subtask;
import entities.tasks.Task;
import enums.Status;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
            return String.format("%d,%s,%s,%s,%s,%d,%d,%d".formatted(
                    task.getId(),
                    task.getClass().getSimpleName(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getDescription(),
                    ((Subtask) task).getEpicId(),
                    task.getStartTime().atZone(ZoneId.of("Europe/Moscow")).toEpochSecond(),
                    task.getDuration().toMillis()));
        }
        return String.format("%d,%s,%s,%s,%s,%d,%d".formatted(
                task.getId(),
                task.getClass().getSimpleName(),
                task.getTitle(),
                task.getStatus(),
                task.getDescription(),
                task.getStartTime().atZone(ZoneId.of("Europe/Moscow")).toEpochSecond(),
                task.getDuration().toMillis()));
    }

    public static String toString(Map<Integer, Task> collection) {
        StringBuilder result = new StringBuilder();
        collection.forEach((k, v) -> result.append(toString(v)).append("\n"));
        return result.toString();
    }

    public static String toString(HistoryManager historyManager) {
        if (historyManager.getHistory().isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        historyManager.getHistory().forEach(element -> result.append(",").append(element.getId()));
        if (result.charAt(0) == ',') result.deleteCharAt(0);
        return result.toString();
    }

    public static List<Integer> getHistoryIdsListFromString(String str) {

        return Arrays.stream(str.split(",")).map(Integer::valueOf).toList();
    }

    public static Task getTaskFromString(String line) {
        String[] splitLine = line.split(",");
        return new Task(
                Integer.valueOf(splitLine[0]),
                splitLine[2],
                splitLine[4],
                getStatusFromString(splitLine[3]),
                getLocalDateTimeFromString(splitLine[5]),
                getDurationFromString(splitLine[6]));
    }


    public static Epic getEpicFromString(String line) {
        String[] splitLine = line.split(",");
        final Epic epic = new Epic(
                Integer.parseInt(splitLine[0]),
                splitLine[2],
                splitLine[4]);
        epic.setStatus(getStatusFromString(splitLine[3]));
        return epic;
    }

    public static Subtask getSubtaskFromString(String line) {
        String[] splitLine = line.split(",");
        return new Subtask(Integer.parseInt(
                splitLine[0]),
                splitLine[2],
                splitLine[4],
                getStatusFromString(splitLine[3]),
                Integer.parseInt(splitLine[5]),
                getLocalDateTimeFromString(splitLine[6]),
                getDurationFromString(splitLine[7]));
    }


    private static Status getStatusFromString(String statusAsString) {
        if (statusAsString.equalsIgnoreCase("NEW"))
            return Status.NEW;
        else if (statusAsString.equalsIgnoreCase("IN_PROGRESS"))
            return Status.IN_PROGRESS;
        else
            return Status.DONE;
    }

    private static LocalDateTime getLocalDateTimeFromString(String timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(timestamp)),
                ZoneId.systemDefault()
        );
    }

    private static Duration getDurationFromString(String timestamp) {
        return Duration.ofMillis(Long.parseLong(timestamp));
    }


}

