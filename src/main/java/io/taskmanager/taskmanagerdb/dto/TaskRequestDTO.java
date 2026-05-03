package io.taskmanager.taskmanagerdb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// INCOMING data

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
    @NotBlank(message = "Title is required.")
    @Size(max = 100, message = "Title must be 100 characters or less.")
    private String title;

    @NotBlank(message = "Description is required.")
    @Size(max = 500, message = "Description must be 500 characters or less.")
    private String description;

    @FutureOrPresent(message = "Date cannot be in the past.")
    private LocalDate dueDate;

    private boolean completed;

    // Getters and Setters via Lombok



}
