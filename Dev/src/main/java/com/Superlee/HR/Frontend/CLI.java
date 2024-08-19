package com.Superlee.HR.Frontend;

import com.Superlee.HR.Backend.Service.HRService;
import com.Superlee.HR.Backend.Service.Response;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Command Line Interface for interacting with the HR system
 * everything is hardcoded and no error handling is done
 * this is a naive implementation and will be replaced with a GUI
 */
public class CLI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final HRService hrService = HRService.getInstance();
    private static final Gson gson = new Gson();

    private static final String help_login = """
            List of available commands:

            load
                load the data from the database

            login <username> <password>
                login to the system
            
            help
                print this message
            
            exit
                exit the program
            """;

    private static final String help_worker = """
            List of available commands:
            
            avlb <shift_id>
                add availability for a shift with the specified id
            
            unavlb <shift_id>
                remove availability for a shift with the specified id
            
            email <email>
                change your email to the specified email
            
            phone <phone>
                change your phone number to the specified phone number
            
            password <password>
                change your password to the specified password
            
            bank <bank>
                change your bank account to the specified bank account
            
            branch <branch>
                change your main branch to the specified branch
            
            details [all]
                print your details
            
            help
                print this message
            
            logout
                logout from the system and return to the login screen
            
            exit
                exit the program
            """;
    private static final String help_hr_manager = """
            List of available commands:
            
            addw <id> <firstname> <surname>
                add a new worker with the specified id, first name and second name
            
            assw <worker_id> <shift_id> <role>
                assign a worker to a shift
            
            unassw <worker_id> <shift_id>
                unassign a worker from a shift
            
            shift <id>
                get a shift with the specified id
            
            shift -a <id>
                get a list of all workers assignable to the specified shift
            
            shift -r <id> <role> <amount>
                set the amount of workers with the specified role needed for a shift
            
            shift <start> <end> <branch>
                get a list of all shifts at the specified branch between the specified start and end time
                must be formatted as yyyy-MM-ddTHH:mm (e.g. 1996-04-15T00:00)
            
            adds <start> <end> <branch>
                add a new shift with the specified start time, end time and branch
                must be formatted as yyyy-MM-ddTHH:mm (e.g. 1996-04-15T00:00)
            
            workers
                get a list of all workers
            
            workers -r <role>
                get a list of all workers with the specified role
            
            workers -n <firstname> <surname>
                get a worker with the specified name
            
            workers -i <id>
                get a worker with the specified id
            
            workers -s <id>
                get a list of all workers assigned to the specified shift
            
            wh <id>
                get the work history of a worker with the specified id
            
            addr <worker> <role>
                add the given role to the specified worker
            
            addb <branch> <address> <manager>
                add a new branch with the specified name, address and manager
            
            branch <name>
                get a branch with the specified name
            
            branches
                get a list of all branches
            
            roles [worker]
                get a list of all roles or all roles for a worker if specified
            
            roles -a <role>
                add a new role with the specified name
            
            help
                print this message
            
            logout
                logout from the system and return to the login screen
            
            exit
                exit the program
            """;

    public static void main(String[] args) {
        System.out.println("Type 'help' for a list of available commands, or 'exit' to quit");
        String input, output;
        do {
            input = scanner.nextLine();

            if (input.equals("exit"))
                break;

            if (input.equals("help"))
                System.out.println(help_login);

            else if (input.equals("load")) {
                output = hrService.loadData();
                Response r = gson.fromJson(output, Response.class);
                System.out.println(Objects.requireNonNullElse(r.errMsg, "Data loaded"));
            } else if (input.startsWith("login")) {
                String[] parts = input.split("\\s+");
                if (parts.length == 3) {
                    output = hrService.login(parts[1], parts[2]);
                    Response r = gson.fromJson(output, Response.class);
                    if (r.errMsg != null)
                        System.out.println(r.errMsg);

                    else {
                        WorkerModel worker = ModelFactory.createWorkerModel(output);
                        System.out.println("Welcome, " + worker.firstname() + " " + worker.surname());
                        if (worker.roles().contains(0))
                            hrManagerMenu(worker);
                        else if (worker.roles().contains(7)) {
                            com.Superlee.Supply.Presentation.CLI.start(args);
                            System.out.println("Logged out");
                        } else
                            workerMenu(worker);
                    }
                } else
                    System.out.println("Invalid number of args");
            } else
                System.out.println("Invalid command");
        } while (true);
    }

    private static void workerMenu(WorkerModel worker) {
        String input, output;
        do {
            input = scanner.nextLine();
            if (input.equals("exit"))
                System.exit(0);

            if (input.equals("help"))
                System.out.println(help_worker);

            else if (input.equals("logout")) {
                output = hrService.logout(worker.id());
                Response r = gson.fromJson(output, Response.class);
                System.out.println(Objects.requireNonNullElse(r.errMsg, "Logged out"));
                return;
            } else {
                String[] parts = input.split("\\s+");
                switch (parts[0]) {
                    case "avlb": {
                        if (parts.length != 2) {
                            System.out.println("Invalid number of args");
                            break;
                        } else if (!tryParseInt(parts[1])) {
                            System.out.println("Invalid ID");
                            break;
                        } else {
                            output = hrService.addAvailability(worker.id(), Integer.parseInt(parts[1]));
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Availability added"));
                        }
                        break;
                    }
                    case "unavlb": {
                        if (parts.length != 2 || !tryParseInt(parts[1])) {
                            System.out.println("Invalid number of args");
                            break;
                        } else {
                            output = hrService.removeAvailability(worker.id(), Integer.parseInt(parts[1]));
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Availability removed"));
                        }
                        break;
                    }
                    case "email": {
                        if (parts.length != 2) {
                            System.out.println("Invalid number of args");
                            break;
                        } else {
                            output = hrService.updateWorkerEmail(worker.id(), parts[1]);
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Email changed"));
                        }
                    }
                    break;
                    case "phone": {
                        if (parts.length != 2) {
                            System.out.println("Invalid number of args");
                            break;
                        } else {
                            output = hrService.updateWorkerPhone(worker.id(), parts[1]);
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Phone number changed"));
                        }
                    }
                    break;
                    case "password": {
                        if (parts.length != 2) {
                            System.out.println("Invalid number of args");
                            break;
                        } else {
                            output = hrService.updateWorkerPassword(worker.id(), parts[1]);
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Password changed"));
                        }
                    }
                    break;
                    case "bank": {
                        if (parts.length != 2) {
                            System.out.println("Invalid number of args");
                            break;
                        } else {
                            output = hrService.updateWorkerBankDetails(worker.id(), parts[1]);
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Bank account changed"));
                        }
                    }
                    break;
                    case "branch": {
                        if (parts.length != 2) {
                            System.out.println("Invalid number of args");
                            break;
                        } else {
                            output = hrService.updateWorkerMainBranch(worker.id(), parts[1]);
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Branch changed"));
                        }
                    }
                    break;

                    // Frontend cases
                    case "details": {
                        if (parts.length == 2)
                            if (Objects.equals(parts[1], "all"))
                                System.out.println(worker.fullDetails());
                            else
                                System.out.println("Unknown argument");
                        else if (parts.length == 1)
                            System.out.println(worker.toString());
                        else
                            System.out.println("Invalid number of args");
                    }
                    break;

                    default:
                        System.out.println("Invalid command");
                        break;
                }
            }
        }
        while (true);
    }

    private static void hrManagerMenu(WorkerModel worker) {
        String input, output;
        do {
            input = scanner.nextLine();
            if (input.equals("exit"))
                System.exit(0);

            if (input.equals("help"))
                System.out.println(help_hr_manager);

            else if (input.equals("logout")) {
                output = hrService.logout(worker.id());
                Response r = gson.fromJson(output, Response.class);
                System.out.println(Objects.requireNonNullElse(r.errMsg, "Logged out"));
                return;
            } else {
                String[] parts = input.split("\\s+");
                switch (parts[0]) {
                    case "addw": {
                        if (parts.length != 4) {
                            System.out.println("Invalid number of args");
                            break;
                        } else {
                            output = hrService.addNewWorker(parts[1], parts[2], parts[3]);
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Worker added"));
                        }
                    }
                    break;
                    case "assw": {
                        if (parts.length != 4 || !tryParseInt(parts[2])) {
                            System.out.println("Invalid number of args");
                            break;
                        } else {
                            output = hrService.assignWorker(parts[1], Integer.parseInt(parts[2]), parts[3]);
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Worker assigned"));
                        }
                    }
                    break;
                    case "unassw": {
                        if (parts.length != 3) {
                            System.out.println("Invalid number of args");
                            break;
                        } else if (!tryParseInt(parts[1]) || !tryParseInt(parts[2])) {
                            System.out.println("Invalid ID");
                            break;
                        } else {
                            output = hrService.unassignWorker(parts[1], Integer.parseInt(parts[2]));
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Worker unassigned"));
                        }
                    }
                    break;
                    case "shift": {
                        if (parts.length == 2) {
                            if (parts[1].equals("-a") || parts[1].equals("-r")) {
                                System.out.println("Invalid number of args");
                                break;
                            } else {
                                if (!tryParseInt(parts[1])) {
                                    System.out.println("Invalid ID");
                                    break;
                                }
                                output = hrService.getShift(Integer.parseInt(parts[1]));
                                Response r = gson.fromJson(output, Response.class);
                                if (r.errMsg != null)
                                    System.out.println(r.errMsg);
                                else {
                                    ShiftModel shift = ModelFactory.createShiftModel(output);
                                    System.out.println(shift);
                                }
                            }
                        } else if (parts.length == 3) {
                            if (parts[1].equals("-r"))
                                System.out.println("Invalid number of args");
                            else if (parts[1].equals("-a")) {
                                if (!tryParseInt(parts[2])) {
                                    System.out.println("Invalid ID");
                                    break;
                                }
                                output = hrService.getAssignableWorkersForShift(Integer.parseInt(parts[2]));
                                Response r = gson.fromJson(output, Response.class);
                                if (r.errMsg != null)
                                    System.out.println(r.errMsg);
                                else {
                                    List<WorkerModel> workers = ModelFactory.createWorkerModelList(output);
                                    printList(workers);
                                }
                            }
                        } else if (parts.length == 4) {
                            if (!tryParseDT(parts[1]) || !tryParseDT(parts[2])) {
                                System.out.println("Invalid datetime format");
                                break;
                            } else {
                                output = hrService.getShiftsByBranchAndDate(parts[1], parts[2], parts[3]);
                                Response r = gson.fromJson(output, Response.class);
                                if (r.errMsg != null)
                                    System.out.println(r.errMsg);
                                else {
                                    List<ShiftModel> shifts = ModelFactory.createShiftModelList(output);
                                    printList(shifts);
                                }
                            }
                        } else if (parts.length == 5) {
                            if (parts[1].equals("-r")) {
                                if (tryParseInt(parts[2]) && tryParseInt(parts[4])) {
                                    output = hrService.setShiftRequiredWorkersOfRole(Integer.parseInt(parts[2]), parts[3], Integer.parseInt(parts[4]));
                                    Response r = gson.fromJson(output, Response.class);
                                    System.out.println(Objects.requireNonNullElse(r.errMsg, "Shift updated"));
                                } else
                                    System.out.println("Invalid ID or amount");
                            } else
                                System.out.println("Invalid argument");
                        } else
                            System.out.println("Invalid number of args");
                    }
                    break;
                    case "adds": {
                        if (parts.length == 4) {
                            if (tryParseDT(parts[1]) && tryParseDT(parts[2])) {
                                output = hrService.addNewShift(parts[1], parts[2], parts[3]);
                                Response r = gson.fromJson(output, Response.class);
                                System.out.println(Objects.requireNonNullElse(r.errMsg, "Shift added"));
                            } else
                                System.out.println("Invalid datetime format");
                        } else
                            System.out.println("Invalid number of args");
                    }
                    break;
                    case "workers": {
                        if (parts.length == 1) {
                            output = hrService.getAllWorkers();
                            Response r = gson.fromJson(output, Response.class);
                            if (r.errMsg != null)
                                System.out.println(r.errMsg);
                            else {
                                List<WorkerModel> workers = ModelFactory.createWorkerModelList(output);
                                printList(workers);
                            }
                        } else if (parts.length >= 3) {
                            switch (parts[1]) {
                                case "-r": {
                                    if (parts.length != 3) {
                                        System.out.println("Invalid number of args");
                                        break;
                                    } else {
                                        output = hrService.getWorkersByRole(parts[2]);
                                        Response r = gson.fromJson(output, Response.class);
                                        if (r.errMsg != null)
                                            System.out.println(r.errMsg);
                                        else {
                                            List<WorkerModel> workers = ModelFactory.createWorkerModelList(output);
                                            printList(workers);
                                        }
                                    }
                                }
                                break;
                                case "-n":
                                    if (parts.length == 4) {
                                        output = hrService.getWorkersByName(parts[2], parts[3]);
                                        Response r = gson.fromJson(output, Response.class);
                                        if (r.errMsg != null)
                                            System.out.println(r.errMsg);
                                        else {
                                            List<WorkerModel> workers = ModelFactory.createWorkerModelList(output);
                                            printList(workers);
                                        }
                                    } else
                                        System.out.println("Invalid number of args");
                                    break;
                                case "-i": {
                                    if (parts.length != 3) {
                                        System.out.println("Invalid number of args");
                                        break;
                                    } else {
                                        output = hrService.getWorkerById(parts[2]);
                                        Response r = gson.fromJson(output, Response.class);
                                        if (r.errMsg != null)
                                            System.out.println(r.errMsg);
                                        else {
                                            WorkerModel workerModel = ModelFactory.createWorkerModel(output);
                                            System.out.println(workerModel);
                                        }
                                    }
                                }
                                break;
                                case "-s": {
                                    if (parts.length == 3) {
                                        if (tryParseInt(parts[2])) {
                                            output = hrService.getWorkersByShift(Integer.parseInt(parts[2]));
                                            Response r = gson.fromJson(output, Response.class);
                                            if (r.errMsg != null)
                                                System.out.println(r.errMsg);
                                            else {
                                                List<WorkerModel> workers = ModelFactory.createWorkerModelList(output);
                                                printList(workers);
                                            }
                                        } else {
                                            System.out.println("Invalid ID");
                                            break;
                                        }
                                    } else
                                        System.out.println("Invalid number of args");
                                }
                                break;
                                default:
                                    System.out.println("Invalid flag");
                                    break;
                            }
                        }
                    }
                    break;
                    case "wh": {
                        if (parts.length == 2) {
                            output = hrService.getWorkerHistory(parts[1]);
                            Response r = gson.fromJson(output, Response.class);
                            if (r.errMsg != null)
                                System.out.println(r.errMsg);
                            else {
                                List<ShiftModel> shifts = ModelFactory.createShiftModelList(output);
                                printList(shifts);
                            }
                        } else
                            System.out.println("Invalid number of args");
                    }
                    break;
                    case "addr": {
                        if (parts.length != 3) {
                            System.out.println("Invalid number of args");
                            break;
                        }
                        output = hrService.addWorkerRole(parts[1], parts[2]);
                        Response r = gson.fromJson(output, Response.class);
                        System.out.println(Objects.requireNonNullElse(r.errMsg, "Role added"));
                    }
                    break;
                    case "addb": {
                        if (parts.length != 4) {
                            System.out.println("Invalid number of args");
                            break;
                        } else {
                            output = hrService.addNewBranch(parts[1], parts[2], parts[3]);
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Branch added"));
                        }
                    }
                    break;
                    case "branch": {
                        if (parts.length != 2) {
                            System.out.println("Invalid number of args");
                            break;
                        } else {
                            output = hrService.getBranch(parts[1]);
                            Response r = gson.fromJson(output, Response.class);
                            if (r.errMsg != null)
                                System.out.println(r.errMsg);
                            else {
                                BranchModel branch = ModelFactory.createBranchModel(output);
                                System.out.println(branch);
                            }
                        }
                    }
                    break;
                    case "branches": {
                        if (parts.length == 1) {
                            output = hrService.getAllBranches();
                            Response r = gson.fromJson(output, Response.class);
                            if (r.errMsg != null)
                                System.out.println(r.errMsg);
                            else {
                                List<BranchModel> branches = ModelFactory.createBranchModelList(output);
                                printList(branches);
                            }
                        } else
                            System.out.println("Invalid number of args");
                    }
                    break;
                    case "roles": {
                        if (parts.length == 1) {
                            output = hrService.getAllRoles();
                            Response r = gson.fromJson(output, Response.class);
                            if (r.errMsg != null)
                                System.out.println(r.errMsg);
                            else {
                                List<String> roles = ModelFactory.createStringList(output);
                                printList(roles);
                            }
                        } else if (parts.length == 2) {
                            output = hrService.getWorkerRoles(parts[1]);
                            Response r = gson.fromJson(output, Response.class);
                            if (r.errMsg != null)
                                System.out.println(r.errMsg);
                            else {
                                List<String> roles = ModelFactory.createStringList(output);
                                printList(roles);
                            }
                        } else if (parts.length == 3 && parts[1].equals("-a")) {
                            output = hrService.addNewRole(parts[2]);
                            Response r = gson.fromJson(output, Response.class);
                            System.out.println(Objects.requireNonNullElse(r.errMsg, "Role added"));
                        } else
                            System.out.println("Invalid number of args");
                    }
                    break;
                    default:
                        System.out.println("Invalid command");
                        break;
                }
            }
        } while (true);
    }

    private static boolean tryParseDT(String part) {
        try {
            LocalDateTime.parse(part);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static boolean tryParseInt(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static <T> void printList(List<T> list) {
        if (list.isEmpty())
            System.out.println("No results");
        else
            for (T item : list)
                System.out.println(item);
    }
}
