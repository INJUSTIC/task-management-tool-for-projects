CREATE DATABASE IF NOT EXISTS MainDatabase;
USE MainDatabase;

-- Create the Developers table
CREATE TABLE Developers (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    email VARCHAR(255),
    specialization ENUM('FRONTEND', 'BACKEND', 'DEVOPS', 'UX_UI')
);

-- Create the Task_Credentials table
CREATE TABLE Task_Credentials (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    developer_id INT UNSIGNED,
    estimation INT UNSIGNED,
    specialization ENUM('FRONTEND', 'BACKEND', 'DEVOPS', 'UX_UI'),
    FOREIGN KEY (developer_id) REFERENCES Developers(id)
);

-- Create the Project_Credentials table
CREATE TABLE Project_Credentials (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(1000)
);

-- Create the Projects table
CREATE TABLE Projects (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    project_credentials_id INT UNSIGNED,
    FOREIGN KEY (project_credentials_id) REFERENCES Project_Credentials(id)
);

-- Create the Tasks table
CREATE TABLE Tasks (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    task_credentials_id INT UNSIGNED,
    project_id INT UNSIGNED,
    task_state ENUM('TODO', 'IN_PROGRESS', 'TESTING', 'DONE'),
    created_at TIMESTAMP,
    completed_at TIMESTAMP,
    FOREIGN KEY (task_credentials_id) REFERENCES Task_Credentials(id),
    FOREIGN KEY (project_id) REFERENCES Projects(id)
);

-- Create the many-to-many relationship table for Developers and Project_Credentials
CREATE TABLE projects_developers (
    project_credentials_id INT UNSIGNED,
    developer_id INT UNSIGNED,
    FOREIGN KEY (project_credentials_id) REFERENCES Project_Credentials(id),
    FOREIGN KEY (developer_id) REFERENCES Developers(id)
);