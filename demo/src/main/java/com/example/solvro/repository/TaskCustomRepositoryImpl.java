package com.example.solvro.repository;

import com.example.solvro.entities.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class TaskCustomRepositoryImpl implements TaskCustomRepository{
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public void customDeleteById(int id) {
        Task task = entityManager.find(Task.class, id);
        if (task != null)
        {
            entityManager.remove(task);
        }
    }
}
