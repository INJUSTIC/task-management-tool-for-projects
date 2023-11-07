package com.example.solvro.repository;

import com.example.solvro.entities.Developer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeveloperRepository extends CrudRepository<Developer, Integer>, DeveloperCustomRepository {
    @Query("SELECT d FROM Developer d LEFT JOIN FETCH d.projectCredentials WHERE d.id = :id")
    Developer findWithProjectCredentialsById(@Param("id") int id);

    @Query("SELECT d FROM Developer d " +
            "JOIN d.projectCredentials pc " +
            "WHERE pc.project.id = :projectId")
    List<Developer> findAllByProjectId(@Param("projectId") int projectId);
}
