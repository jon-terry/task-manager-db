package io.taskmanager.taskmanagerdb.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import io.taskmanager.taskmanagerdb.entity.Task;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByCompletedTrue();

    List<Task> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);


}
