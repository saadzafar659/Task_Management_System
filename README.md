<h1>Task Management System</h1>

<p>The Task Management System, a Spring Boot web and REST API-based project built using Java 17. This project employs a Test-Driven Development (TDD) approach, featuring unit, integration, and end-to-end testing to ensure robust and reliable functionality.</p>

Sonar Cloud: [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=saadzafar659_Task_Management_System&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=saadzafar659_Task_Management_System)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=saadzafar659_Task_Management_System&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=saadzafar659_Task_Management_System)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=saadzafar659_Task_Management_System&metric=coverage)](https://sonarcloud.io/summary/new_code?id=saadzafar659_Task_Management_System)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=saadzafar659_Task_Management_System&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=saadzafar659_Task_Management_System)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=saadzafar659_Task_Management_System&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=saadzafar659_Task_Management_System)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=saadzafar659_Task_Management_System&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=saadzafar659_Task_Management_System)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=saadzafar659_Task_Management_System&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=saadzafar659_Task_Management_System)
Coverall: [![Coverage Status](https://coveralls.io/repos/github/saadzafar659/Task_Management_System/badge.svg?branch=main)](https://coveralls.io/github/saadzafar659/Task_Management_System?branch=main)
Github Action: [![Java CI with Maven](https://github.com/saadzafar659/Task_Management_System/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/saadzafar659/Task_Management_System/actions/workflows/maven-publish.yml)




 <h3>Getting Started</h3>
    <ol>
        <li><strong>Clone the repository</strong>
            <pre><code>git clone https://github.com/saadzafar659/Task_Management_System.git</code></pre>
        </li>        
    </ol>

<h2>API Endpoints</h2>
    <h3>Tasks</h3>
    <ul>
        <li><strong>Create a Task</strong>
            <pre><code>POST: http://localhost:8866/api/tasks/create</code></pre>
        </li>
        <li><strong>Retrieve All Tasks</strong>
            <pre><code>GET: http://localhost:8866/api/tasks/getAll</code></pre>
        </li>
        <li><strong>Retrieve a Task by ID</strong>
            <pre><code>GET: http://localhost:8866/api/tasks/{id}</code></pre>
        </li>
        <li><strong>Delete a Task by ID</strong>
            <pre><code>DELETE: http://localhost:8866/api/tasks/delete/{id}</code></pre>
        </li>
    </ul>
    
<h3>Users</h3>
    <ul>
        <li><strong>Retrieve All Users</strong>
            <pre><code>GET: http://localhost:8866/api/users/getAll</code></pre>
        </li>
        <li><strong>Retrieve a User by ID</strong>
            <pre><code>GET: http://localhost:8866/api/users/{id}</code></pre>
        </li>
        <li><strong>Create a User</strong>
            <pre><code>POST: http://localhost:8866/api/users/create</code></pre>
        </li>
		 <li><strong>Delete a User by ID</strong>
            <pre><code>DELETE: http://localhost:8866/api/users/delete/{id}</code></pre>
        </li>
    </ul>
    
<h2 >Web Interface</h2>
    <ul>
        <li><strong></strong>
            <pre><code>URL: http://localhost:8866/users</code></pre>
        </li>
    </ul>
