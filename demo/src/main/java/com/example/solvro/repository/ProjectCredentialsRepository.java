package com.example.solvro.repository;

import com.example.solvro.entities.Project;
import com.example.solvro.entities.ProjectCredentials;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProjectCredentialsRepository extends CrudRepository<ProjectCredentials, Integer>, ProjectCredentialsCustomRepository {
    @Query("SELECT pc FROM ProjectCredentials pc " +
            "LEFT JOIN FETCH pc.developers d " +
            "WHERE pc.id = :projectCredentialsId")
    ProjectCredentials findWithDevelopers(@Param("projectCredentialsId") int projectCredentialsId);
}
