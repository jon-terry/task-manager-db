package io.taskmanager.taskmanagerdb.controller;

import io.taskmanager.taskmanagerdb.dto.TaskRequestDTO;
import io.taskmanager.taskmanagerdb.dto.TaskResponseDTO;
import io.taskmanager.taskmanagerdb.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "CRUD operations, analytics, and search for tasks")
public class TaskController {

    private final TaskService taskService;

    //
    // GET /tasks (paginated)
    //
    @Operation(
            summary = "Get all tasks (paginated)",
            description = "Returns a paginated list of tasks using TaskResponseDTO."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tasks retrieved successfully",
            content = @Content(schema = @Schema(implementation = TaskResponseDTO.class))
    )
    @GetMapping
    public Page<TaskResponseDTO> getTasks(Pageable pageable) {
        return taskService.getAllTasks(pageable);
    }

    //
    // GET /tasks/{id}
    //
    @Operation(
            summary = "Get a task by ID",
            description = "Returns a single task by its ID."
    )
    @ApiResponse(responseCode = "200", description = "Task retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @Parameter(description = "ID of the task to retrieve")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    //
    // POST /tasks
    //
    @Operation(
            summary = "Create a new task",
            description = "Creates a new task using TaskRequestDTO."
    )
    @ApiResponse(responseCode = "200", description = "Task created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Task data for creation"
            )
            TaskRequestDTO dto
    ) {
        return ResponseEntity.ok(taskService.createTask(dto));
    }

    //
    // PUT /tasks/{id}
    //
    @Operation(
            summary = "Update an existing task",
            description = "Updates a task using TaskRequestDTO."
    )
    @ApiResponse(responseCode = "200", description = "Task updated successfully")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @Parameter(description = "ID of the task to update")
            @PathVariable Long id,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated task data"
            )
            TaskRequestDTO dto
    ) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    //
    // DELETE /tasks/{id}
    //
    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Task deleted successfully")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete")
            @PathVariable Long id
    ) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    //
    // GET /tasks/analytics/completion
    //
    @Operation(
            summary = "Get completion statistics",
            description = "Returns total tasks, completed tasks, and completion percentage."
    )
    @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully")
    @GetMapping("/analytics/completion")
    public ResponseEntity<Map<String, Object>> getCompletionStats() {
        long total = taskService.countTotal();
        long completed = taskService.countCompleted();
        double percentage = total == 0 ? 0 : (completed * 100.0) / total;

        return ResponseEntity.ok(
                Map.of(
                        "total", total,
                        "completed", completed,
                        "percentage", percentage
                )
        );
    }

    //
    // GET /tasks/analytics/created-per-day
    //
    @Operation(
            summary = "Get tasks created per day",
            description = "Returns a map of dates and the number of tasks created on each day."
    )
    @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully")
    @GetMapping("/analytics/created-per-day")
    public ResponseEntity<?> getCreatedPerDay() {
        return ResponseEntity.ok(taskService.getTasksPerDay(7));
    }
}
