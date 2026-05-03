package io.taskmanager.taskmanagerdb.service;

import io.taskmanager.taskmanagerdb.dto.TaskRequestDTO;
import io.taskmanager.taskmanagerdb.dto.TaskResponseDTO;
import io.taskmanager.taskmanagerdb.entity.Task;
import io.taskmanager.taskmanagerdb.mapper.TaskMapper;
import io.taskmanager.taskmanagerdb.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    //
    // ANALYTICS
    //

    public long countCompleted() {
        return taskRepository.countByCompletedTrue();
    }

    public long countTotal() {
        return taskRepository.count();
    }

    public Map<String, Long> getTasksPerDay(int days) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);

        List<Task> tasks = taskRepository.findByCreatedAtBetween(start, end);

        Map<String, Long> result = new LinkedHashMap<>();

        for (int i = days; i >= 0; i--) {
            String day = end.minusDays(i).toLocalDate().toString();
            result.put(day, 0L);

        }

        for (Task task : tasks) {
            String day = task.getCreatedAt().toLocalDate().toString();
            result.put(day, result.get(day) + 1);
        }

        return result;

    }

    //
    // CRUD : DTO + MapStruct
    //

    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        Task task = taskMapper.toEntity(dto);
        Task saved = taskRepository.save(task);
        return taskMapper.toDTO(saved);
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO dto) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setDueDate(dto.getDueDate());
        existing.setCompleted(dto.isCompleted());

        Task updated = taskRepository.save(existing);
        return taskMapper.toDTO(updated);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return taskMapper.toDTO(task);
    }

    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskMapper::toDTO);
    }

    //
    // SEARCH
    //

    public List<Task> searchTasks(Boolean completed, String title, LocalDateTime from, LocalDateTime to ) {
        // Start with all tasks
        List<Task> tasks = taskRepository.findAll();
        if (completed != null) {
            tasks = tasks.stream()
                    .filter(t -> t.isCompleted() == completed)
                    .toList();
        }

        if (title != null && !title.isBlank()) {
            tasks = tasks.stream()
                    .filter(t -> t.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .toList();
        }

        if (from != null && to != null) {
            tasks = tasks.stream()
                    .filter(t -> !t.getCreatedAt().isBefore(from) && !t.getCreatedAt().isAfter(to))
                    .toList();
        }

        return tasks;

    }

}
