# task-management-tool-for-projects

##Used technologies:
Spring Boot, Spring MVC, Hibernate, MySQL, REST API, CRUD, Spring Data JPA, Spring Validation, Thymeleaf, HTML, CSS

## Overview

This is a Spring Boot application that allows to auto assign tasks to developers in a project. You can create, manage and delete projects, developers and tasks, assign developers to project
and assign tasks to project and developer, observe task archive . Tasks auto assigning is performing due to algorithm which is described below

## Functionality

### Observing projects list
-GET /projects
-Endpoint allows to observe all projects

### Observing developers list
-GET /developers
-Endpoint allows to observe all developers

### Project creating
-POST /project
-Endpoint allows to create a new project with providing project's data such as name, description and developers.

### Project managing
-POST /projects/{projectId}
-Endpoint allows to edit project's name, description, adding developers to a project. It also shows all current project tasks.

### Project deleting
-DELETE /projects/{projectId}
-Endpoint deletes specified project with all assigned tasks

### Developer creating
-POST /developer
-Endpoint allows to create a new developer with providing developer's data such as first name, last name, email and specialization.
-Specialization is one of following: BACKEND, FRONTEND, DEVOPS, UX_UI

### Developer managing
-POST /developers/{developerId}
-Endpoint allows to edit developer's first and last name, email. It also shows assigned tasks and projects to developer.

### Developer deleting
-DELETE /developers/{developerId}
-Endpoint deletes specified developer and all his task assignings

### Task creating
-POST /projects/{projectId}/task
-Endpoint allows to create a new task for a project with providing task's data such as name, estimation, specialization and assigned developer
-Estimation is a number from Fibonacci sequence (according to Agile methology)
-Specialization is one of following: BACKEND, FRONTEND, DEVOPS, UX_UI
-Developer assigning is optional
-Developer can be assigned to a task only if his specialization matches task's specialization

### Task managing
-POST /projects/{projectId}/{taskId}
-Endpoint allows to edit task's name, estimation and status
-Status is one of following: TODO, IN_PROGRESS, TESTING, DONE
-When task's status changes to DONE, it automatically tranfers to task archive and disappears from project's page.

### Task deleting
-DELETE /projects/{projectId}/{taskId}
-Endpoint allows to delete a task

### Creating task auto assigning proposition
-GET /projects/{projectId}/assignment
-Endpoint creates a task assigning proposal based on the designed algorithm.

### Accepting task auto assigning proposition
-POST /projects/{projectId}/assignment
-Endpoint allows to accept or reject task auto assigning proposition

### Adding developers to existing project
-POST /projects/{projectId}/addDevelopers
-Endpoint allows to add new developers to the project

### Observing project's completed tasks
-GET /projects/{projectId}/completedTasks
-Endpoint allows to observe completed tasks in the project with their durations

### Observing developer's completed tasks
-GET /developers/{developerId}/completedTasks
-Endpoint allows to observe completed tasks by the developer with their durations

## Algorithm

Algorithm is based on evaluating current developer workload and his productivity which is based on duration of his completed tasks with the same estimation.

1. **Sort Tasks:**
   - Sort the list of tasks to assign based on their estimations in ascending order.

2. **Initialize Maps:**
   - Create two maps:
      - `developersBySpecialization`: Specialization as the key, and a list of developers with that specialization as the value.
      - `completedTasksBySpecialization`: Specialization as the key, and a list of completed tasks with that specialization as the value.

3. **Assign Tasks:**
   - For each task `taskToAssign` in the sorted list of tasks:
      - Find the specialization of the task: `taskSpecialization`.
      - Find the list of developers with the same specialization: `developersWithSpecialization`.
      - Evaluate and assign the task to the least workloaded developer in `developersWithSpecialization`.
         - For each developer `currentDeveloper` in `developersWithSpecialization`:
            - Calculate the current workloading of the developer:
               - Find the list of completed tasks by the developer: `completedTasksByDeveloper`.
               - If the size of `completedTasksByDeveloper` is not zero:
                  - Find the number of completed tasks by the developer with the same estimation as `taskToAssign`.
                  - If the number of completed tasks is not zero, find and return the average duration to complete tasks with the same estimation.
                  - If the number of completed tasks is zero, find and return the average duration to complete tasks with the nearest estimation (`tasksWithNearestEstimation`), multiplied by the ratio of the estimation of `taskToAssign` to the estimation of `tasksWithNearestEstimation`.
               - If the size of `completedTasksByDeveloper` is zero:
                  - If there are completed tasks in the project:
                    - Find and return the average duration to complete tasks with the same estimation as `taskToAssign` across all developers.
                    - If there are no completed tasks with the same estimation in the project, find and return the average duration to complete tasks with the nearest estimation (`tasksWithNearestEstimation`), multiplied by the ratio of the estimation of `taskToAssign` to the estimation of `tasksWithNearestEstimation`.
                  - If there are completed tasks in the project:
                    - return estimaton of the task to assign
            - Prognose the duration to complete `taskToAssign` by `currentDeveloper`.
         - Find and return the developer with the least workloading

4. **Assign Developer to Task:**
   - Assign the developer found in step 3 to `taskToAssign`.
  


## Instructions to run the project

To run the project, you need to connect it to the database. For this, follow next steps:
- download [MySQL Database Server](https://dev.mysql.com/downloads/mysql/) and [MySQL Workbench](https://www.mysql.com/products/workbench/)
- create a new MySQL user with next credentials:
  - Username: springstudent
  - Password: springstudent
- Create a database with the name `maindatabase` executing next query: `CREATE DATABASE maindatabase`
- After this, open the project in IDE (e.g. IntelliJ)
- Run the script `CreateDatabase.sql` in the `\demo\src\main\java\com\example\solvro\sql` folder
- Run the project
- In browser, enter "http://localhost:8080/" in the search bar

