package com.example.solvro.assignment;

import java.util.*;
import com.example.solvro.entities.Developer;
import com.example.solvro.entities.Task;
import com.example.solvro.entities.TaskCredentials;
import com.example.solvro.enums.Specialization;
import com.example.solvro.enums.TaskState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskAssignmentAlgorithm {
    public void assignTasks(List<Developer> project_developers, List<Task> tasksToAssign, List<Task> completedTasks) {
        List<Task> modifiableTasks = new ArrayList<>(tasksToAssign);
        modifiableTasks.sort(Comparator.comparing(task -> task.getTaskCredentials().getEstimation()));
        HashMap<Specialization, List<Task>> completedTasksBySpecialization = new HashMap<>();
        HashMap<Specialization, List<Developer>> developersBySpecialization = new HashMap<>();
        for (Specialization specialization: Specialization.values()) {
            completedTasksBySpecialization.put(specialization, completedTasks.stream().filter(Task -> Task.getTaskCredentials().getSpecialization() == specialization).collect(Collectors.toList()));
            developersBySpecialization.put(specialization, project_developers.stream().filter(Developer -> Developer.getSpecialization() == specialization).collect(Collectors.toList()));
        }
        for (Task task : modifiableTasks) {
            Specialization taskSpecialization = task.getTaskCredentials().getSpecialization();
            Developer assignedDeveloper = assignTaskToDeveloper(task, developersBySpecialization.get(taskSpecialization), completedTasksBySpecialization.get(taskSpecialization));
            if (assignedDeveloper != null) {
                task.getTaskCredentials().setDeveloper(assignedDeveloper);
                assignedDeveloper.addTaskCredentials(task.getTaskCredentials());
            }
        }
    }

    private Developer assignTaskToDeveloper(Task task, List<Developer> developers, List<Task> completedTasks) {
        Developer bestDeveloper = null;
        double minCompletionTime = Double.MAX_VALUE;

        for (Developer developer : developers) {
            double completionTime = calculateCompletionTime(developer, task, completedTasks);
            if (completionTime < minCompletionTime) {
                minCompletionTime = completionTime;
                bestDeveloper = developer;
            }
        }

        return bestDeveloper;
    }

    private double calculateCompletionTime(Developer developer, Task task, List<Task> completedTasks) {
        double currentLoading = calculateDeveloperLoading(developer, completedTasks);
        double newTaskDuration = prognozeTaskDurationForDeveloper(developer, task, completedTasks);
        return currentLoading + newTaskDuration;
    }

    private List<Task> findTasksWithNearestEstimation(int taskEstimation, List<Task> completedTasks) {
        List<Task> tasksWithNearestEstimation = new ArrayList<>();
        int minEstimationDifference = Integer.MAX_VALUE;

        for (Task task : completedTasks) {
            int completedTaskEstimation = task.getTaskCredentials().getEstimation();
            int estimationDifference = Math.abs(taskEstimation - completedTaskEstimation);

            if (estimationDifference < minEstimationDifference) {
                tasksWithNearestEstimation.clear();
                tasksWithNearestEstimation.add(task);
                minEstimationDifference = estimationDifference;
            }
            else if (estimationDifference == minEstimationDifference) {
                tasksWithNearestEstimation.add(task);
            }
        }
        return tasksWithNearestEstimation;
    }

    private double findAverageDurationForEstimation(int estimation, List<Task> completedTasks) {
        int taskCount = 0;
        int totalDuration = 0;

        for (Task task : completedTasks) {
            if (task.getTaskCredentials().getEstimation() == estimation) {
                totalDuration += task.getDuration().toMinutes();
                taskCount++;
            }
        }
        if (taskCount > 0) {
            return (double) totalDuration / (double) taskCount;
        } else {
            return -1;
        }
    }

    private double prognozeTaskDurationForDeveloper(Developer developer, Task task, List<Task> completedTasks) {
        int taskEstimation = task.getTaskCredentials().getEstimation();
        List<Task> completedTasksByDeveloper = completedTasks.stream().filter(Task -> Task.getTaskCredentials().getDeveloper().getId() == developer.getId()).toList();
        if (!completedTasksByDeveloper.isEmpty()) {
            double totalTime = 0.0;
            int completedTaskCount = 0;
            for (Task completedTask : completedTasksByDeveloper) {
                if (completedTask.getTaskCredentials().getEstimation() == taskEstimation) {
                    double completedTaskDuration = completedTask.getDuration().toMinutes();
                    totalTime += completedTaskDuration;
                    completedTaskCount++;
                }
            }
            if (completedTaskCount > 0) {
                return totalTime / completedTaskCount;
            }
            else {
                List<Task> tasksWithNearestEstimation = findTasksWithNearestEstimation(taskEstimation, completedTasksByDeveloper);
                int durationSum = 0;
                int nearestEstimation = tasksWithNearestEstimation.get(0).getTaskCredentials().getEstimation();
                for (Task nearestTask : tasksWithNearestEstimation) {
                    durationSum += nearestTask.getDuration().toMinutes();
                }
                return (double) durationSum / tasksWithNearestEstimation.size() * ((double) taskEstimation / nearestEstimation);
            }
        }
        else {
            if (!completedTasks.isEmpty()) {
                double averageDuration = findAverageDurationForEstimation(taskEstimation, completedTasks);
                    if (averageDuration == -1) {
                        List<Task> tasksWithNearestEstimation = findTasksWithNearestEstimation(taskEstimation, completedTasks);
                        int durationSum = 0;
                        int nearestEstimation = tasksWithNearestEstimation.get(0).getTaskCredentials().getEstimation();
                        for (Task nearestTask : tasksWithNearestEstimation) {
                            durationSum += nearestTask.getDuration().toMinutes();
                        }
                        averageDuration = (double) durationSum / tasksWithNearestEstimation.size() * ((double) taskEstimation / nearestEstimation);
                    }
                    return averageDuration;
                }
            else return task.getTaskCredentials().getEstimation();
        }
    }

    private double calculateDeveloperLoading(Developer developer, List<Task> completedTasks) {
        double loading = 0;
        for (TaskCredentials taskCredentials: developer.getTaskCredentials()) {
            if (taskCredentials.getTask().getTaskState() != TaskState.DONE) {
                loading += prognozeTaskDurationForDeveloper(developer, taskCredentials.getTask(), completedTasks);
            }

        }
        return loading;
    }
}



/*
public class TaskAssignmentAlgorithm {
    public void assignTasks(List<Developer> project_developers, List<Task> tasks, List<Task> completedTasks) {
        List<Task> modifiableTasks = new ArrayList<>(tasks);
        modifiableTasks.sort(Comparator.comparing(task -> task.getTaskCredentials().getEstimation()));
        Map<Specialization, List<Task>> tasksBySpecialization = new HashMap<>();
        for (Developer developer : project_developers) {
            Specialization specialization = developer.getSpecialization();
            tasksBySpecialization.put(specialization, new ArrayList<>());
        }

        for (Task task : modifiableTasks) {
            Specialization taskSpecialization = task.getTaskCredentials().getSpecialization();

            // Jeśli istnieje specjalizacja dewelopera i pasuje do specjalizacji zadania
            if (tasksBySpecialization.containsKey(taskSpecialization)) {
                tasksBySpecialization.get(taskSpecialization).add(task);
            }
*/
/*            else {
                Optional<Developer> suitableDeveloper = project_developers.stream()
                        .filter(developer -> developer.getSpecialization() == taskSpecialization)
                        .findFirst();

                if (suitableDeveloper.isPresent()) {
                    tasksBySpecialization.get(taskSpecialization).add(task);
                }
            }*//*

        }

        for (Specialization specialization : tasksBySpecialization.keySet()) {
            List<Task> tasksForSpecialization = tasksBySpecialization.get(specialization);
            List<Developer> availableDevelopers = project_developers.stream()
                    .filter(developer -> developer.getSpecialization() == specialization)
                    .collect(Collectors.toList());

            if (!availableDevelopers.isEmpty()) {
                int developerIndex = 0;

                availableDevelopers.sort(Comparator.comparing(developer -> calculateDeveloperPerformance(developer, completedTasks)));

                for (Task task : tasksForSpecialization) {
                    Developer developer = availableDevelopers.get(developerIndex);
                    task.getTaskCredentials().setDeveloper(developer);
                    developerIndex = (developerIndex + 1) % availableDevelopers.size();
                }
            }
        }
    }
    private double calculateDeveloperPerformance(Developer developer, List<Task> completedTasks) {
        long totalDuration = 0;
        long totalCompletedTasks = 0;
        int totalEstimation = 0;

        for (Task task : completedTasks) {
            if (task.getTaskCredentials().getDeveloper() != null && task.getTaskCredentials().getDeveloper().getId() == developer.getId()) {
                totalDuration += task.getDuration().toMinutes();
                totalCompletedTasks++;
                totalEstimation += task.getTaskCredentials().getEstimation();
            }
        }

        if (totalCompletedTasks == 0) {
            return Double.MAX_VALUE;
        }

        double estimationWeight = (double) totalEstimation / totalCompletedTasks;

        double performance = (double) (totalDuration / totalCompletedTasks) * estimationWeight;

        return performance;
    }

}*/
