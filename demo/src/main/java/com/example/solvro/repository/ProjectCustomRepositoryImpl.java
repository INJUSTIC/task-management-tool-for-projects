package com.example.solvro.repository;

import com.example.solvro.entities.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectCustomRepositoryImpl implements ProjectCustomRepository{
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public void customDeleteById(int id) {
        Project project = entityManager.find(Project.class, id);
        if (project != null)
        {
            entityManager.remove(project);
        }
    }

/*    @Override
    public Project findWithTasks(int projectId) {

        TypedQuery<Project> typedQuery = entityManager.createQuery("SELECT p FROM Project p JOIN FETCH p.tasks WHERE p.id = :projectId", Project.class);
        typedQuery.setParameter("projectId", projectId);
        Project project = typedQuery.getSingleResult();
        return project;
    }*/
}
