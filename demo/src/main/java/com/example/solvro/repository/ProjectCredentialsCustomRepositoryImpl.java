package com.example.solvro.repository;

import com.example.solvro.entities.ProjectCredentials;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


@Repository
public class ProjectCredentialsCustomRepositoryImpl implements ProjectCredentialsCustomRepository{

    @PersistenceContext
    EntityManager entityManager;
    @Override
    public void customDeleteById(int id) {
        ProjectCredentials projectCredentials = entityManager.find(ProjectCredentials.class, id);
        if (projectCredentials != null && projectCredentials.getProject() == null)
        {
            entityManager.remove(projectCredentials);
        }
    }
}
