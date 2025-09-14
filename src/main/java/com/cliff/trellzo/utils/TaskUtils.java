package com.cliff.trellzo.utils;

import com.cliff.trellzo.dto.responses.TaskResponseDTO;
import com.cliff.trellzo.entity.Task;

public class TaskUtils {
    public static TaskResponseDTO createTaskResponseDTO(Task task){
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(task.getId());
        taskResponseDTO.setAssignee(UserUtils.getUserNames(task.getUser()));
        taskResponseDTO.setDescription(task.getDescription());
        taskResponseDTO.setStatus(task.getStatus());
        taskResponseDTO.setTitle(task.getTitle());
        return taskResponseDTO;
    }
}
