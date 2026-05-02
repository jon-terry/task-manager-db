package io.taskmanager.taskmanagerdb.service;

import io.taskmanager.taskmanagerdb.entity.Task;
import io.taskmanager.taskmanagerdb.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public long countCompleted() {
        return taskRepository.countByCompletedTrue();
    }

    public long countTotal() {
        return taskRepository.count();
    }



    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found!"));
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

   public Task createTask(Task task) {
        return taskRepository.save(task);
   }

    public Task updateTask(Long id, Task updatedTask) {
        Task existing = getTaskById(id);

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setCompleted(updatedTask.isCompleted());

        return taskRepository.save(existing);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Page<Task> getTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

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
