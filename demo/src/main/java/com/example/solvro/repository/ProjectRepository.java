package com.example.solvro.repository;

import com.example.solvro.entities.Project;
import com.example.solvro.entities.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Integer>, ProjectCustomRepository{
    @Query("SELECT p FROM Project p " +
            "LEFT JOIN FETCH p.tasks t " +
            "WHERE p.id = :projectId ")
    Project findWithTasks(@Param("projectId") int projectId);
}
