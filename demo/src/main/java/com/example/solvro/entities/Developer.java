package com.example.solvro.entities;

import com.example.solvro.enums.Specialization;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "Developers")
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "First name is required")
    @Size(max = 128, message = "First name must not exceed 128 characters")
    @Column(name = "first_name")
    private String firstName;

    @OneToMany(mappedBy = "developer", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<TaskCredentials> taskCredentials;

    @ManyToMany (cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name="projects_developers",
                joinColumns = @JoinColumn(name="developer_id"),
                inverseJoinColumns = @JoinColumn(name="project_credentials_id"))
    private List<ProjectCredentials> projectCredentials;

    @NotBlank(message = "Last name is required")
    @Size(max = 128, message = "Last name must not exceed 128 characters")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
    private Specialization specialization;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<TaskCredentials> getTaskCredentials() {
        return taskCredentials;
    }

    public void setTaskCredentials(List<TaskCredentials> taskCredentials) {
        this.taskCredentials = taskCredentials;
    }

    public List<ProjectCredentials> getProjectCredentials() {
        return projectCredentials;
    }

    public void setProjectCredentials(List<ProjectCredentials> projectCredentials) {
        this.projectCredentials = projectCredentials;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public void addTaskCredentials(TaskCredentials taskCredentials) {
        this.taskCredentials.add(taskCredentials);
    }
}
