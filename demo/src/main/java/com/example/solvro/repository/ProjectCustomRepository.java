package com.example.solvro.repository;

import com.example.solvro.entities.Project;
import com.example.solvro.entities.ProjectCredentials;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectCustomRepository {
    void customDeleteById(int id);
}
