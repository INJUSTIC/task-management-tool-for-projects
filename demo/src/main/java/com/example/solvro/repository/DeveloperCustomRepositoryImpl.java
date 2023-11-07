package com.example.solvro.repository;

import com.example.solvro.entities.Developer;
import com.example.solvro.entities.TaskCredentials;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeveloperCustomRepositoryImpl implements DeveloperCustomRepository {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public void customDeleteById(int id) {
        Developer developer = entityManager.find(Developer.class, id);
        if (developer != null) {
            List<TaskCredentials> taskCredentialsList = developer.getTaskCredentials();
            for (TaskCredentials taskCredentialsTemp : taskCredentialsList) {
                taskCredentialsTemp.setDeveloper(null);
            }
            entityManager.remove(developer);
        }
    }
}
