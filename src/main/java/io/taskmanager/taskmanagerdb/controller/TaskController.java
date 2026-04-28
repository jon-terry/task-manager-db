package io.taskmanager.taskmanagerdb.controller;

import io.taskmanager.taskmanagerdb.entity.Task;
import io.taskmanager.taskmanagerdb.service.TaskService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/analytics/completion")
    public Map<String, Object> getCompletionStats() {
        long total = taskService.countTotal();
        long completed = taskService.countCompleted();

        double percentage = total == 0 ? 0 : (completed * 100.0 ) / total;

        return Map.of(
                "total", total,
                "completed", completed,
                "percentage", percentage
        );
    }

    @GetMapping("/analytics/created-per-day")
    public Map<String, Long> getCreatedPerDay() {
        return taskService.getTasksPerDay(7);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }


}
