package com.example.solvro.controller;

import com.example.solvro.assignment.TaskAssignmentAlgorithm;
import com.example.solvro.entities.Developer;
import com.example.solvro.entities.Project;
import com.example.solvro.entities.ProjectCredentials;
import com.example.solvro.entities.Task;
import com.example.solvro.enums.TaskState;
import com.example.solvro.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ProjectController {
    private final ProjectService service;

    @Autowired
    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping("/projects")
    public String getProjectsList(Model model) {
        List<Project> projects = service.findAllProjects();
        model.addAttribute("projects", projects);
        return "projectList";
    }

    @GetMapping("/project")
    public String createProjectForm(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        List<Developer> developers = service.findAllDevelopers();
        model.addAttribute("developers", developers);
        return "createProjectForm";
    }

    @GetMapping("/projects/{projectId}")
    public String getProject(@PathVariable int projectId, Model model) {
        Project project = service.findProjectWithDependenciesById(projectId);
        model.addAttribute("project", project);
        return "projectManagement";
    }

    @DeleteMapping("/projects/{projectId}")
    public String deleteProject(@PathVariable int projectId) {
        service.deleteProjectById(projectId);
        return "redirect:/projects";
    }

    @PostMapping("/project")
    public String createOrUpdateProject(Model model, @Valid @ModelAttribute("project") Project project, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Developer> developers = service.findAllDevelopers();
            model.addAttribute("developers", developers);
            if(project.getId() == 0) return "createProjectForm";
            else return "projectManagement";
        }
        if (project.getId() != 0) {
            Project updatedProject = service.findProjectWithDependenciesById(project.getId());
            updatedProject.setProject_credentials(project.getProject_credentials());
            updatedProject.setTasks(project.getTasks());
            service.saveProject(updatedProject);
        }
        else service.saveProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/developers")
    public String getDeveloperList(Model model) {
        List<Developer> developers = service.findAllDevelopers();
        model.addAttribute("developers", developers);
        return "developerList";
    }

    @GetMapping("/developers/{developerId}")
    public String getDeveloper(@PathVariable int developerId, Model model) {
        Developer developer = service.findDeveloperWithProjectCredentialsById(developerId);
        model.addAttribute("developer", developer);
        return "developerManagement";
    }

    @GetMapping("/developer")
    public String createDeveloperForm(Model model) {
        model.addAttribute("developer", new Developer());
        return "createDeveloperForm";
    }

    @PostMapping("/developer")
    public String createOrUpdateDeveloper(@Valid @ModelAttribute("developer") Developer developer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            if (developer.getId() == 0) return "createDeveloperForm";
            else return "developerManagement";
        }
        if (developer.getId() != 0) {
            Developer upatedDeveloper = service.findDeveloperById(developer.getId());
            upatedDeveloper.setEmail(developer.getEmail());
            upatedDeveloper.setFirstName(developer.getFirstName());
            upatedDeveloper.setLastName(developer.getLastName());
            upatedDeveloper.setProjectCredentials(developer.getProjectCredentials());
            upatedDeveloper.setTaskCredentials(developer.getTaskCredentials());
            service.saveDeveloper(upatedDeveloper);
        }
        else service.saveDeveloper(developer);
        return "redirect:/developers";
    }

    @DeleteMapping("/developers/{developerId}")
    public String deleteDeveloper(@PathVariable int developerId) {
        service.deleteDeveloperById(developerId);
        return "redirect:/developers";
    }

    @GetMapping("/projects/{projectId}/task")
    public String createTaskForm(@PathVariable int projectId, Model model) {
        Task task = new Task();
        task.setProject(service.findProjectById(projectId));
        model.addAttribute("task", task);
        List<Developer> projectDevelopers = service.findAllDevelopersByProjectId(projectId);
        model.addAttribute("projectDevelopers", projectDevelopers);
        return "createTaskForm";
    }

    @GetMapping("/projects/{projectId}/{taskId}")
    public String getTask(@PathVariable int projectId, @PathVariable int taskId, Model model) {
        Task task = service.findTaskById(taskId);
        model.addAttribute("task", task);
        return "taskManagement";
    }

    @GetMapping("/developers/{developerId}/completedTasks")
    public String getDeveloperCompletedTasks(@PathVariable int developerId, Model model) {
        List<Task> completedTasks = service.findCompletedTasksByDeveloperId(developerId);
        model.addAttribute("completedTasks", completedTasks);
        return "developerTaskArchive";
    }

    @GetMapping("/projects/{projectId}/completedTasks")
    public String getProjectCompletedTasks(@PathVariable int projectId, Model model) {
        List<Task> completedTasks = service.findCompletedTasksByProjectId(projectId);
        model.addAttribute("completedTasks", completedTasks);
        return "projectTaskArchive";
    }

    @GetMapping("/projects/{projectId}/addDevelopers")
    public String getListOfDevelopersToAdd(@PathVariable int projectId, Model model) {
        Project project = service.findProjectById(projectId);
        model.addAttribute("projectCredentials", project.getProject_credentials());
        List<Developer> allDevelopers = service.findAllDevelopers();
        List<Developer> developersToShow = new ArrayList<>();
        for (Developer developer : allDevelopers) {
            Optional<ProjectCredentials> projectCredentials = developer.getProjectCredentials().stream()
                                                                .filter(ProjectCredentials -> ProjectCredentials.getProject().getId() == projectId)
                                                                .findFirst();
            if (projectCredentials.isEmpty()) developersToShow.add(developer);
        }
        model.addAttribute("developersToShow", developersToShow);
        return "addDevelopersToProject";
    }

    @PostMapping("/projects/{projectId}/addDevelopers")
    public String addDevelopersToProject(@PathVariable int projectId, @ModelAttribute("projectCredentials") ProjectCredentials projectCredentialsFromForm) {
        Project project = service.findProjectById(projectId);

        //ProjectCredentials from form contains only selected developers, so we need to add them to already existing in project.
        project.getProject_credentials().addDevelopersToProject(projectCredentialsFromForm.getDevelopers());

        service.saveProject(project);
        return "redirect:/projects/" + project.getId();
    }

    @PostMapping("/projects/{projectId}/task")
    public String createOrUpdateTask(@PathVariable int projectId, Model model, @Valid @ModelAttribute("task") Task task, BindingResult bindingResult) {
        if (task.getTaskCredentials().getDeveloper() != null && task.getTaskCredentials().getSpecialization() != task.getTaskCredentials().getDeveloper().getSpecialization()) {
            task.setProject(service.findProjectById(projectId));
            model.addAttribute("task", task);
            model.addAttribute("specializationMismatchError", "Task and developer specializations do not match.");
            List<Developer> projectDevelopers = service.findAllDevelopersByProjectId(projectId);
            model.addAttribute("projectDevelopers", projectDevelopers);
            return "createTaskForm";
        }
        if (bindingResult.hasErrors()) {
            task.setProject(service.findProjectById(projectId));
            model.addAttribute("task", task);
            List<Developer> projectDevelopers = service.findAllDevelopersByProjectId(projectId);
            model.addAttribute("projectDevelopers", projectDevelopers);
            if (task.getId() == 0) return "createTaskForm";
            else return "taskManagement";
        }
        if (task.getTaskState() == null) task.setTaskState(TaskState.TODO);
        if(task.getCreatedAt() == null) task.setCreatedAt(new Date());
        else if (task.getTaskState() == TaskState.DONE) {
            if (task.getTaskCredentials().getDeveloper() == null) {
                model.addAttribute("ErrorTaskDoneWhenNoDeveloper", "Task status can't be DONE if there is no attached developer");
                List<Developer> projectDevelopers = service.findAllDevelopersByProjectId(projectId);
                model.addAttribute("projectDevelopers", projectDevelopers);
                return "taskManagement";
            }
            task.setCompletedAt(new Date());
        }
        if (task.getId() != 0) {
            Task updatedTask = service.findTaskById(task.getId());
            updatedTask.setTaskState(task.getTaskState());
            updatedTask.setCreatedAt(task.getCreatedAt());
            updatedTask.setTaskCredentials(task.getTaskCredentials());
            updatedTask.setProject(task.getProject());
            if (task.getCompletedAt() != null) {
                updatedTask.setCompletedAt(task.getCompletedAt());
                updatedTask.setDuration();
            }
            service.saveTask(updatedTask);
        }
        else service.saveTask(task);
        String redirectUrl = "redirect:/projects/" + projectId;
        return redirectUrl;
    }


    @DeleteMapping("/projects/{projectId}/{taskId}")
    public String deleteTask(@PathVariable int taskId, @PathVariable int projectId) {
        service.deleteTaskById(taskId);
        return "redirect:/projects/" + projectId;
    }



    @GetMapping("/projects/{projectId}/assignment")
    public String createAssignment(@PathVariable int projectId, Model model, HttpSession session) {
        TaskAssignmentAlgorithm taskAssignmentAlgorithm = new TaskAssignmentAlgorithm();
        Project project = service.findProjectWithDependenciesById(projectId);
        List<Task> completedTasks = service.findCompletedTasksByProjectId(projectId);
        model.addAttribute("notUpdatedProject", project);
        taskAssignmentAlgorithm.assignTasks(project.getProject_credentials().getDevelopers(), project.getNotAssignedTasks(), completedTasks);
        model.addAttribute("projectWithAssignmentTasks", project);

        session.setAttribute("updatedProject", project);

        return "proposeAssignment";
    }

    @PostMapping("/projects/{projectId}/assignment")
    public String acceptAssignment(@PathVariable("projectId") int projectId, HttpSession session) {
        // Retrieve the updated project from the session
        Project project = (Project) session.getAttribute("updatedProject");
        service.saveProject(project);

        return "redirect:/projects/" + projectId;
    }

}
