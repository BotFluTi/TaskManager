package com.example.TaskManager;

import com.example.TaskManager.Repository.OwnerRepository;
import com.example.TaskManager.Repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Bean
    CommandLineRunner loadData(TaskRepository taskRepository, OwnerRepository ownerRepository) {
        return args -> {

            // Define the owner email and name
            String email = "john.doe@example.com";
            String name = "John Doe";

            // Check if the owner with this email already exists
            if (!ownerRepository.existsByEmail(email)) {
                // If the owner does not exist, create and save the owner
                Owner owner = new Owner();
                owner.setName(name);
                owner.setEmail(email);
                ownerRepository.save(owner);

                // Create and save a sample task for the owner
                Task task = new Task();
                task.setTitle("Sample Task");
                task.setDescription("This is a test task");
                task.setEstimatedHours(10);
                task.setCompletedHours(5);
                task.setRemainingEffort(5);
                task.setOwner(owner);

                taskRepository.save(task);
            } else {
                // Optionally log or print a message if the owner already exists
                System.out.println("Owner with email " + email + " already exists. Skipping creation.");
            }
        };
    }
}

