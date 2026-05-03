package io.taskmanager.taskmanagerdb.mapper;

import io.taskmanager.taskmanagerdb.dto.*;
import io.taskmanager.taskmanagerdb.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    // DTO -> Entity
    public Task toEntity(TaskRequestDTO dto);

    // Entity -> DTO
    public TaskResponseDTO toDTO(Task entity);


}
