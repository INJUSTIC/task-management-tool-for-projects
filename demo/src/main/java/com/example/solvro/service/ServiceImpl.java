package com.example.solvro.service;

import com.example.solvro.entities.*;
import com.example.solvro.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceImpl implements ProjectService {
    private final DeveloperRepository developerRepository;
    private final ProjectCredentialsRepository projectCredentialsRepository;
    private final ProjectRepository projectRepository;
    private final TaskCredentialsRepository taskCredentialsRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ServiceImpl(DeveloperRepository developerRepository, ProjectCredentialsRepository projectCredentialsRepository, ProjectRepository projectRepository, TaskCredentialsRepository taskCredentialsRepository, TaskRepository taskRepository) {
        this.developerRepository = developerRepository;
        this.projectCredentialsRepository = projectCredentialsRepository;
        this.projectRepository = projectRepository;
        this.taskCredentialsRepository = taskCredentialsRepository;
        this.taskRepository = taskRepository;
    }

    //developer operations
    @Transactional
    @Override
    public Developer saveDeveloper(Developer developer) {
        return developerRepository.save(developer);
    }

    @Transactional
    @Override
    public void deleteDeveloperById(int id) {
        developerRepository.customDeleteById(id);
    }

    @Override
    public Developer findDeveloperById(int id) {
        Optional<Developer> result = developerRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public Developer findDeveloperWithProjectCredentialsById(int id) {
        Developer developer = developerRepository.findWithProjectCredentialsById(id);
        return developer;
    }

    @Override
    public List<Developer> findAllDevelopers() {
        return (List<Developer>) developerRepository.findAll();
    }

    @Override
    public List<Developer> findAllDevelopersByProjectId(int projectId) {
        return developerRepository.findAllByProjectId(projectId);
    }


    //project operations
    @Transactional
    @Override
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    @Override
    public void deleteProjectById(int id) {
        projectRepository.customDeleteById(id);
    }

    @Override
    public Project findProjectById(int id) {
        Optional<Project> result = projectRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public Project findProjectWithDependenciesById(int id) {
        Project project = projectRepository.findWithTasks(id);
        ProjectCredentials projectCredentials = project.getProject_credentials();
        projectCredentials = projectCredentialsRepository.findWithDevelopers(projectCredentials.getId());
        return project;
    }

    @Override
    public List<Project> findAllProjects() {
        return (List<Project>) projectRepository.findAll();
    }


    //project credentials operations
    @Transactional
    @Override
    public ProjectCredentials saveProjectCredentials(ProjectCredentials projectCredentials) {
        return projectCredentialsRepository.save(projectCredentials);
    }

    @Transactional
    @Override
    public void deleteProjectCredentialsById(int id) {
        projectCredentialsRepository.customDeleteById(id);
    }

    @Override
    public ProjectCredentials findProjectCredentialsById(int id) {
        Optional<ProjectCredentials> result = projectCredentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public List<ProjectCredentials> findAllProjectCredentials() {
        return (List<ProjectCredentials>) projectCredentialsRepository.findAll();
    }


    //task operations
    @Transactional
    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    @Override
    public void deleteTaskById(int id) {
        taskRepository.customDeleteById(id);
    }

    @Override
    public Task findTaskById(int id) {
        Optional<Task> result = taskRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public List<Task> findAllTasks() {
        return (List<Task>) taskRepository.findAll();
    }

    @Override
    public List<Task> findCompletedTasksByProjectId(int projectId) {
        List<Task> tasks = taskRepository.findCompletedTasksByProjectId(projectId);
        for (Task task: tasks) {
            task.setDuration();
        }
        return tasks;
    }

    @Override
    public List<Task> findCompletedTasksByDeveloperId(int developerId) {
        List<Task> tasks = taskRepository.findCompletedTasksByDeveloperId(developerId);
        for (Task task: tasks) {
            task.setDuration();
        }
        return tasks;
    }

    //task credentials operations
    @Transactional
    @Override
    public TaskCredentials saveTaskCredentials(TaskCredentials taskCredentials) {
        return taskCredentialsRepository.save(taskCredentials);
    }

    @Transactional
    @Override
    public void deleteTaskCredentialsById(int id) {
        taskCredentialsRepository.customDeleteById(id);
    }

    @Override
    public TaskCredentials findTaskCredentialsById(int id) {
        Optional<TaskCredentials> result = taskCredentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public List<TaskCredentials> findAllTaskCredentials() {
        return (List<TaskCredentials>) taskCredentialsRepository.findAll();
    }
}
