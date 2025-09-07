package com.cliff.trellzo.controllers;

import com.cliff.trellzo.entity.Task;
import com.cliff.trellzo.service.TaskService;
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
    public List<Task> findAll(){
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        return taskService.findById(id).map(task -> ResponseEntity.ok().body(task)).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    private Task createTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @PutMapping("/{id}")
    private ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id,task));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteTask(@PathVariable Long id) {
         taskService.deleteTaskById(id);
         return ResponseEntity.noContent().build();
    }
}
