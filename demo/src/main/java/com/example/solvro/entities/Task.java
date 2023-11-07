package com.example.solvro.entities;

import com.example.solvro.enums.TaskState;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name="Tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Valid
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name="task_credentials_id")
    private TaskCredentials taskCredentials;

    @ManyToOne (cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name="task_state")
    private TaskState taskState;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="created_at")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="completedAt")
    private Date completedAt;

    @Transient
    private Duration duration;

    public Duration getDuration() {
        return duration;
    }

    public String getDurationInStringFormat() {
        long days = duration.toDays();
        long hours = duration.minusDays(days).toHours();
        long minutes = duration.minusDays(days).minusHours(hours).toMinutes();
        return days + "d " + hours + "h " + minutes + "m";
    }

    public void setDuration() {
        Instant start = createdAt.toInstant();
        Instant end = completedAt.toInstant();
        duration = Duration.between(start, end);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
        setDuration();
    }

    public TaskCredentials getTaskCredentials() {
        return taskCredentials;
    }

    public void setTaskCredentials(TaskCredentials taskCredentials) {
        this.taskCredentials = taskCredentials;
    }
}
