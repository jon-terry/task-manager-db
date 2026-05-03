package io.taskmanager.taskmanagerdb.mapper;

import io.taskmanager.taskmanagerdb.dto.*;
import io.taskmanager.taskmanagerdb.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public class TaskMapper {

    // DTO -> Entity
    Task toEntity (TaskRequestDTO dto);

    // Entity -> DTO
    TaskResponseDTO toDTO (Task entity);


}
