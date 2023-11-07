package com.example.solvro.entities;

import com.example.solvro.enums.TaskState;
import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToMany(mappedBy = "project",
            cascade = CascadeType.ALL)
    private List<Task> tasks;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_credentials_id")
    private ProjectCredentials project_credentials;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> getNotAssignedTasks() {
        return tasks.stream().filter((task) -> task.getTaskState() != TaskState.DONE && task.getTaskCredentials().getDeveloper() == null).toList();
    }
    public List<Task> getNotCompletedTasks() {
        return tasks.stream().filter((task) -> task.getTaskState() != TaskState.DONE).toList();
    }

    public List<Task> getCompletedTasks() {
        List<Task> completedTasks = tasks.stream().filter((task) -> task.getTaskState() == TaskState.DONE).toList();
        completedTasks.forEach(Task::setDuration);
        return completedTasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public ProjectCredentials getProject_credentials() {
        return project_credentials;
    }

    public void setProject_credentials(ProjectCredentials project_credentials) {
        this.project_credentials = project_credentials;
    }
}
