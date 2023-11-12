package com.example.solvro.service;

import com.example.solvro.entities.*;

import java.util.List;

public interface ProjectService {
    //developer operations
    Developer saveDeveloper(Developer developer);
    void deleteDeveloperById(int id);
    Developer findDeveloperById(int id);
    List<Developer> findAllDevelopers();
    Developer findDeveloperWithProjectCredentialsById(int id);

    List<Developer> findAllDevelopersByProjectId(int projectId);


    //project operations
    Project saveProject(Project project);
    void deleteProjectById(int id);
    Project findProjectById(int id);
    Project findProjectWithDependenciesById(int id);
    List<Project> findAllProjects();


    //project credentials operations
    ProjectCredentials saveProjectCredentials(ProjectCredentials projectCredentials);
    void deleteProjectCredentialsById(int id);
    ProjectCredentials findProjectCredentialsById(int id);
    List<ProjectCredentials> findAllProjectCredentials();


    //Task operations
    Task saveTask(Task task);
    void deleteTaskById(int id);
    Task findTaskById(int id);
    List<Task> findAllTasks();
    List<Task> findCompletedTasksByProjectId(int projectId);
    public List<Task> findCompletedTasksByDeveloperId(int developerId);


    //Task credentials operations
    TaskCredentials saveTaskCredentials(TaskCredentials taskCredentials);
    void deleteTaskCredentialsById(int id);
    TaskCredentials findTaskCredentialsById(int id);
    List<TaskCredentials> findAllTaskCredentials();
}
