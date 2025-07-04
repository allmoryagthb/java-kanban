//package entities.manager;
//
//import entities.tasks.Epic;
//import entities.tasks.Subtask;
//import enums.Status;
//
//import java.util.Map;
//
//public class EpicManager extends BaseTaskManager<Epic> {
//
//    public Map<Integer, Subtask> getSubtasks(int id) {
//        checkTaskIsExistsById(id);
//        return genericMap.get(id).getSubtasks();
//    }
//
//    @Override
//    public void addTask(Epic task) {
//        if (genericMap.get(task.getTaskId()) != null) {
//            System.out.println(">>>ОШИБКА: Эпик с таким id уже существует!");
//            return;
//        }
//        genericMap.put(task.getTaskId(), task);
//    }
//
//    @Override
//    public void changeTaskStatusById(int id, Status newStatus) {
//        checkTaskIsExistsById(id);
//        System.out.println(">>>ОШИБКА: Невозможно изменить статус у эпика");
//    }
//
//}
