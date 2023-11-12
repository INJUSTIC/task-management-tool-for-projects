package com.example.solvro.repository;

import com.example.solvro.entities.Task;
import com.example.solvro.enums.TaskState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Integer>, TaskCustomRepository {
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.taskState = :taskState")
    List<Task> findTasksWithStateByProjectId(@Param("projectId") int projectId, @Param("taskState") TaskState taskState);
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.taskState = 'DONE'")
    List<Task> findCompletedTasksByProjectId(@Param("projectId") int projectId);

    @Query("SELECT t FROM Task t WHERE t.taskCredentials.developer.id = :developerId AND t.taskState = 'DONE'")
    List<Task> findCompletedTasksByDeveloperId(@Param("developerId") int developerId);

}
