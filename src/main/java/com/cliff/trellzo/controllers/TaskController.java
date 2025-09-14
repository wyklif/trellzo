package com.cliff.trellzo.controllers;

import com.cliff.trellzo.dto.requests.TaskRequestDTO;
import com.cliff.trellzo.dto.responses.TaskResponseDTO;
import com.cliff.trellzo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponseDTO> findAll(){
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long id){
        return taskService.findById(id).map(task -> ResponseEntity.ok().body(task)).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO taskRequestDto) {
        return taskService.saveTask(taskRequestDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@Valid @PathVariable Long id, @RequestBody TaskRequestDTO taskRequestDto) {
        return ResponseEntity.ok(taskService.updateTask(id, taskRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
         taskService.deleteTaskById(id);
         return ResponseEntity.noContent().build();
    }
}
