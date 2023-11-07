package com.example.solvro.repository;

import com.example.solvro.entities.TaskCredentials;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class TaskCredentialsCustomRepositoryImpl implements TaskCredentialsCustomRepository{
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public void customDeleteById(int id) {
        TaskCredentials taskCredentials = entityManager.find(TaskCredentials.class, id);
        if (taskCredentials != null && taskCredentials.getTask() == null)
        {
            entityManager.remove(taskCredentials);
        }
    }
}
