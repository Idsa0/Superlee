# Superlee

This project was developed as part of the course _Analysis and Design of Software Systems_. The objective was to design and integrate a system composed of four independent modules, following industry-standard practices.

Due to a shortened semester, the project's scope was adjusted, leading to the final implementation described here.

### Authors

[Idan Saltzman](https://github.com/Idsa0)

[Idan Goldberg](https://github.com/Goldymen2002)

[Almog Zhivov](https://github.com/AlmogZhivov)

[Yali Fink](https://github.com/finkya)

### Libraries Used

[Maven](https://github.com/apache/maven)

[Gson](https://github.com/google/gson)

[JUnit 4](https://github.com/junit-team/junit4)

[JUnit 5](https://github.com/junit-team/junit5)

[SQLite](https://github.com/xerial/sqlite-jdbc)

## HR Module

### Instructions

Upon launching the program, the user will be required to log in.
When a user logs in, they will be presented with a menu of options according to their role.

The interface is separated into four main sections:
1. Login Screen
2. HR Manager Menu
3. Supply Manager Menu
4. Employee Menu

By default there are 7 workers registered into the system.

**List of available commands before login**:


load the data from the database
```sh
load
```

login to the system
```sh
login <username> <password>
```

print this message
```sh
help
```

exit the program
```sh
exit
```

#### Login

To login into the system use the command:
```sh
login <id> <password>
```

Worker id 0 is the HR manager, login with:
```sh
login 0 123
```

Workers with id 1 to 5 are regular workers the worker with id 6 is the Supply Manager, they all have the password "password", login with:
```sh
login <id> password
```

<div style="page-break-after: always"></div>

### HR manager Commands

**The following commands are available to the HR manager**:

add a new worker with the specified id, first name and second name
```sh
addw <id> <firstname> <surname>
```

assign a worker to a shift
```sh
assw <worker_id> <shift_id> <role>
```

unassign a worker from a shift
```sh
unassw <worker_id> <shift_id>
```

get a shift with the specified id
```sh
shift <id>
```

get a list of all workers assignable to the specified shift
```sh
shift -a <id>
```

set the amount of workers with the specified role needed for a shift
```sh
shift -r <id> <role> <amount>
```

get a list of all shifts at the specified branch between the specified start and end time\
must be formatted as yyyy-MM-ddTHH:mm (e.g. 1996-04-15T00:00)
```sh
shift <start> <end> <branch>
```

add a new shift with the specified start time, end time and branch\
must be formatted as yyyy-MM-ddTHH:mm (e.g. 1996-04-15T00:00)
```sh
adds <start> <end> <branch>
```

get a list of all workers
```sh
workers
```

get a list of all workers with the specified role
```sh
workers -r <role>
```

get a worker with the specified name
```sh
workers -n <firstname> <surname>
```

get a worker with the specified id
```sh
workers -i <id>
```

get a list of all workers assigned to the specified shift
```sh
workers -s <id>
```

get the work history of a worker with the specified id
```
wh <id>
```

add the given role to the specified worker
```sh
addr <worker> <role>
```

add a new branch with the specified name, address and manager
```sh
addb <branch> <address> <manager>
```

get a branch with the specified name
```sh
branch <name>
```

get a list of all branches
```sh
branches
```

get a list of all roles or all roles for a worker if specified
```sh
roles [worker]
```

add a new role with the specified name
```sh
roles -a <role>
```

print this message
```sh
help
```

logout from the system and return to the login screen
```sh
logout
```

exit the program
```sh
exit
```

<div style="page-break-after: always"></div>

### Regular Worker Commands

**The following commands are available to regular workers**:

add availability for a shift with the specified id
```sh
avlb <shift_id>
```

remove availability for a shift with the specified id
```sh
unavlb <shift_id>
```

change your email to the specified email
```sh
email <email>
```

change your phone number to the specified phone number
```sh
phone <phone>
```

change your password to the specified password
```sh
password <password>
```

change your bank account to the specified bank account
```sh
bank <bank>
```

change your main branch to the specified branch
```sh
branch <branch>
```

print your details
```sh
details [all]
```

print this message
```sh
help
```

logout from the system and return to the login screen
```sh
logout
```

exit the program
```sh
exit
```
