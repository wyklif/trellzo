package com.cliff.trellzo.service;

import com.cliff.trellzo.repository.TaskRepository;
import com.cliff.trellzo.entity.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id){
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task){
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails){
        Task task = taskRepository.findById(id).orElseThrow(()->new RuntimeException("Task Not found"));
        task.setDescription(taskDetails.getDescription());
        task.setTitle(taskDetails.getTitle());
        task.setStatus(taskDetails.getStatus());
        return taskRepository.save(task);
    }

    public void deleteTaskById(Long id){
        Task task = taskRepository.findById(id).orElseThrow(()->new RuntimeException("Task Not found"));
        taskRepository.delete(task);
    }
}
