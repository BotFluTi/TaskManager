package com.example.TaskManager.Service;

import com.example.TaskManager.Task;
import com.example.TaskManager.Repository.OwnerRepository;
import com.example.TaskManager.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    // List all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // List tasks assigned to a specific owner
    public List<Task> getTasksByOwner(Long ownerId) {
        if (!ownerRepository.existsById(ownerId)) {
            throw new IllegalArgumentException("Owner with id " + ownerId + " does not exist");
        }
        return taskRepository.findByOwnerId(ownerId);
    }

    // Get task by ID
    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    // Update remaining effort (estimated hours) of a task
    @Transactional
    public Task updateEstimatedHours(Long taskId, Integer newEstimatedHours) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        task.setEstimatedHours(newEstimatedHours);
        return taskRepository.save(task);
    }

    // Create a new task
    @Transactional
    public Task createTask(Task task) {
        logger.info("Creating new task: {}", task);

        if (task.getEstimatedHours() != null && task.getEstimatedHours() < 0) {
            throw new IllegalArgumentException("Estimated hours cannot be negative");
        }

        if (task.getCompletedHours() != null && task.getCompletedHours() < 0) {
            throw new IllegalArgumentException("Completed hours cannot be negative");
        }

        if (task.getOwner() == null || !ownerRepository.existsById(task.getOwner().getId())) {
            throw new IllegalArgumentException("Invalid owner: the owner must exist");
        }

        return taskRepository.save(task);
    }

    // Remove a task by ID
    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task with id " + taskId + " does not exist");
        }
        taskRepository.deleteById(taskId);
        logger.info("Deleted task with id {}", taskId);
    }
}

