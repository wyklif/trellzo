package com.cliff.trellzo.service;

import com.cliff.trellzo.dto.requests.TaskRequestDTO;
import com.cliff.trellzo.dto.responses.TaskResponseDTO;
import com.cliff.trellzo.entity.User;
import com.cliff.trellzo.repository.TaskRepository;
import com.cliff.trellzo.entity.Task;
import com.cliff.trellzo.repository.UserRepository;
import com.cliff.trellzo.utils.TaskUtils;
import com.cliff.trellzo.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskResponseDTO> findAll(){

        List<Task> tasks =  taskRepository.findAll();
        List<TaskResponseDTO> taskResponseDTOS = new ArrayList<>();
        for(Task task : tasks){
            TaskResponseDTO taskResponseDTO = TaskUtils.createTaskResponseDTO(task);
            taskResponseDTOS.add(taskResponseDTO);
        }
        return taskResponseDTOS;
    }

    public Optional<TaskResponseDTO> findById(Long id){

        Optional<Task> task =  taskRepository.findById(id);
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        if (task.isPresent()){
            User user = userRepository.findById(task.get().getUser().getId()).get();
            taskResponseDTO.setId(task.get().getId());
            taskResponseDTO.setTitle(task.get().getTitle());
            taskResponseDTO.setDescription(task.get().getDescription());
            taskResponseDTO.setAssignee(UserUtils.getUserNames(user));

        }
        return Optional.of(taskResponseDTO);
    }

    public TaskResponseDTO saveTask(TaskRequestDTO taskRequestDto){
        User user = userRepository.findById(Long.parseLong(taskRequestDto.getUser_id())).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = new Task();
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setStatus(taskRequestDto.getStatus());
        task.setUser(user);


        Task saved =  taskRepository.save(task);

        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(task.getId());
        taskResponseDTO.setTitle(saved.getTitle());
        taskResponseDTO.setDescription(saved.getDescription());
        taskResponseDTO.setAssignee(user.getFirstName()+" "+user.getLastName());
        taskResponseDTO.setStatus(saved.getStatus());

        return taskResponseDTO;

    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO){
        Task task = taskRepository.findById(id).orElseThrow(()->new RuntimeException("Task Not found"));
        task.setDescription(taskRequestDTO.getDescription());
        User user = userRepository.findById(Long.parseLong(taskRequestDTO.getUser_id())).orElseThrow(()->new RuntimeException("User not found"));
        task.setTitle(taskRequestDTO.getTitle());
        task.setStatus(taskRequestDTO.getStatus());
        task.setUser(user);
        Task saved =  taskRepository.save(task);
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(task.getId());
        taskResponseDTO.setTitle(saved.getTitle());
        taskResponseDTO.setDescription(saved.getDescription());
        taskResponseDTO.setAssignee(user.getFirstName()+" "+user.getLastName());
        taskResponseDTO.setStatus(saved.getStatus());
        return taskResponseDTO;
    }

    public void deleteTaskById(Long id){
        Task task = taskRepository.findById(id).orElseThrow(()->new RuntimeException("Task Not found"));
        taskRepository.delete(task);
    }
}
