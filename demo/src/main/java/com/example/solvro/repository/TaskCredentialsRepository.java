package com.example.solvro.repository;

import com.example.solvro.entities.TaskCredentials;
import org.springframework.data.repository.CrudRepository;

public interface TaskCredentialsRepository extends CrudRepository<TaskCredentials, Integer>, TaskCredentialsCustomRepository {
}
