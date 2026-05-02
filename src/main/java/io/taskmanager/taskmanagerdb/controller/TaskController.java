package io.taskmanager.taskmanagerdb.controller;

import io.taskmanager.taskmanagerdb.entity.Task;
import io.taskmanager.taskmanagerdb.service.TaskService;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
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
    public Page<Task> getTasks(Pageable pageable) {
        return taskService.getTasks(pageable);
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

    @GetMapping("/search")
    public List<Task> searchTasks (
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        LocalDateTime fromDate = (from != null) ? LocalDateTime.parse(from) : null;
        LocalDateTime toDate = (to != null) ? LocalDateTime.parse(to) : null;

        return taskService.searchTasks(completed, title, fromDate, toDate);

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
