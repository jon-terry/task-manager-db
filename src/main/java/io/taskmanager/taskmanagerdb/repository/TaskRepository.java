package io.taskmanager.taskmanagerdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.taskmanager.taskmanagerdb.entity.Task;


public interface TaskRepository extends JpaRepository<Task, Long> {

}
