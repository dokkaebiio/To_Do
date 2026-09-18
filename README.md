# Task Tracker - Starter Project

This is a minimal Jakarta EE / Maven / Payara project used to verify your
development toolchain before building out the real Task Tracker application.

## What this proves

If you can build this project and see a JSON response at `/api/ping`, then
your JDK, Maven, VS Code, and Payara setup are all correctly wired together.
Everything after this is just adding more servlets, a service layer, and a
database repository on top of the same pattern.

## Prerequisites

- JDK 21
- Apache Maven 3.9.x
- Payara Server Community 6.x
- VS Code with Extension Pack for Java + Payara Tools

## Build

From the project root:

```
mvn clean package
```

This produces `target/task-tracker.war`.

## Run

1. Start Payara Server (via Payara Tools in VS Code, or `asadmin start-domain`).
2. Deploy `target/task-tracker.war` (drag into Payara Tools' deploy target,
   or `asadmin deploy target/task-tracker.war`).
3. Open http://localhost:8080/task-tracker/ in a browser — you should see
   the static index page.
4. Open http://localhost:8080/task-tracker/api/ping — you should see:

```json
{"status":"ok","message":"Task Tracker backend is running"}
```

## Known limitations

This starter has no database connection, no real business logic, and no
error handling beyond the framework defaults. It exists purely to confirm
the build-and-deploy pipeline works.
