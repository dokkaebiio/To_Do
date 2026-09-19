What it does
Create projects
Create tasks under a project
Update a task's status (todo / in-progress / done)
Delete a task
All data is stored in a MySQL database and survives page reloads/restarts

Software required
JDK 21
Apache Maven 3.9.x
Payara Server 7.x
MySQL Connector/J 9.7 (placed in <Payara>/glassfish/domains/domain1/lib/)
A MySQL database 

Running the system
mvn clean package
Deploy the resulting target/task-tracker.war to Payara
http://localhost:8080/task-tracker/
Testing the main functions

Open the app in a browser and:
Create a project (e.g. "Website Redesign")
Click the project to select it
Create a task under it (e.g. "Design homepage")
Click "Mark done" to update its status
Click "Delete" to remove it