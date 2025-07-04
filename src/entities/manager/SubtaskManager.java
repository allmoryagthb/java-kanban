//package entities.manager;
//
//import entities.tasks.Epic;
//import entities.tasks.Subtask;
//import enums.Status;
//
//import static enums.Status.*;
//
//public class SubtaskManager extends BaseTaskManager<Subtask> {
//
//    @Override
//    public void addTask(Subtask subtask) {
//        if (genericMap.containsKey(subtask.getTaskId())) {
//            if (genericMap.get(subtask.getTaskId()).getStatus() == NEW) {
//                subtask.setStatus(IN_PROGRESS);
//            } else {
//                subtask.setStatus(DONE);
//            }
//        }
//        genericMap.put(subtask.getTaskId(), subtask);
//        changeEpicStatus(subtask);
//    }
//
//    @Override
//    public void changeTaskStatusById(int id, Status newStatus) {
//        super.changeTaskStatusById(id, newStatus);
//        changeEpicStatus(genericMap.get(id));
//    }
//
//    @Override
//    public void deleteAllTasks() {
//        genericMap.values().forEach(e -> {
//            e.getEpic().setStatus(NEW);
//            e.getEpic().getSubtasks().clear();
//        });
//        this.genericMap.clear();
//    }
//
//    @Override
//    public void deleteTaskById(int id) {
//        if (checkTaskIsExistsById(id)) {
//            Epic epic = genericMap.get(id).getEpic();
//            epic.getSubtasks().remove(id);
//
//            if (epic.getSubtasks().isEmpty()) {
//                epic.setStatus(NEW);
//            } else {
//                epic.getSubtasks().values().forEach(e -> {
//                    if (e.getStatus() == NEW) {
//                        epic.setStatus(NEW);
//                    } else if (e.getStatus() == IN_PROGRESS) {
//                        epic.setStatus(IN_PROGRESS);
//                    } else {
//                        epic.setStatus(DONE);
//                    }
//                });
//            }
//            genericMap.remove(id);
//        }
//    }
//
//    private void changeEpicStatus(Subtask subtask) {
//        Epic epic = genericMap.get(subtask.getTaskId()).getEpic();
//
//        if (epic.getSubtasks().values().stream().anyMatch(e -> e.getStatus() == NEW)) {
//            epic.setStatus(NEW);
//        } else if (epic.getSubtasks().values().stream().anyMatch(e -> e.getStatus() == IN_PROGRESS)) {
//            epic.setStatus(IN_PROGRESS);
//        } else {
//            epic.setStatus(DONE);
//        }
//    }
//}
