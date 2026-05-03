package io.taskmanager.taskmanagerdb.controller;

import io.taskmanager.taskmanagerdb.dto.*;
import io.taskmanager.taskmanagerdb.entity.Task;
import io.taskmanager.taskmanagerdb.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.createTask(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    @GetMapping("/{id}")
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        return taskService.getAllTasks(pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analytics/completion")
    public ResponseEntity<?> getCompletionStats() {
        long total = taskService.countTotal();
        long completed = taskService.countCompleted();
        double percentage = total == 0 ? 0: (completed * 100.0) / total;

        return ResponseEntity.ok(
                java.util.Map.of(
                        "total", total,
                        "completed", completed,
                        "percentage", percentage
                )
        );
    }

    @GetMapping("/analytics/created-per-day")
    public ResponseEntity<?> getCreatedPerDay() {
        return ResponseEntity.ok(taskService.getTasksPerDay(7));
    }

}
