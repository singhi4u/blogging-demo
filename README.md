# blogging-demo
This project is to expose a blogging/social media site APIs and support below operations

1. register a user and assign user/admin role
2. User can have different roles (USER, ADMIN, MODERATOR) and role based authentication for apis is in place
3. Register api do not require authentication and can be invoked to create a new user in the system
4. Other APIs need authentication credentials to be passed with each request ( http basic authentication is in place)
5. Authenticated user can create blog posts for itself
6. Authenticated user can add comments to any post
7. other CRUD operations for Post and Comments are provided
8. Project is build using Spring-Boot framework so a built project can be executed from any machine with Java installed on it.
9. DB used is embedded in memory H2 database and its console can be accessed @http://<server ip>:8080/h2-console/  ( provide JDBC URS as: jdbc:h2:mem:blogdb to connect)
10. project exposes basic API documentation build with Swagger and can be accessed @ http://<server ip>:8080/swagger-ui.html (API documentation in html format) and http://<server ip>:8080/v2/api-docs (API documentation in JSON format) , These URLs can be accessed only when application is running on the server.
11. http://localhost:8080/api/user/register - This API will Register a new User to the Nandus Chicken blogging/Social Media site. Mandatory field to provide is userName and password.

