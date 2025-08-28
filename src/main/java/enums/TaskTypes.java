package enums;

public enum TaskTypes {
    TASK("TASK"),
    EPIC("EPIC"),
    SUBTASK("SUBTASK");

    private final String value;

    TaskTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }
}
