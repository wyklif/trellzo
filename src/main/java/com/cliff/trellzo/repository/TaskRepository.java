package com.cliff.trellzo.repository;

import com.cliff.trellzo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
