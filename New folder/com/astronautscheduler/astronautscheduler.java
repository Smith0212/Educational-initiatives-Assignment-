package com.astronautscheduler;

import com.astronautscheduler.Observer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// ANSI color codes for console output
class ConsoleColors {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
}

// Singleton ScheduleManager
class ScheduleManager {

    private static ScheduleManager instance;
    private final List<Task> tasks;
    private static final Logger LOGGER = Logger.getLogger(ScheduleManager.class.getName());
    private final List<Observer> observers = new ArrayList<>();

    private ScheduleManager() {
        tasks = new ArrayList<>();
    }

    public static synchronized ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void addTask(Task task) throws ScheduleConflictException {
        if (isConflicting(task)) {
            notifyObservers("Conflict detected: Task conflicts with an existing task.");
            throw new ScheduleConflictException("Task conflicts with an existing task.");
        }
        tasks.add(task);
        Collections.sort(tasks);
        LOGGER.info("Task added successfully: " + task);
        notifyObservers("Task added successfully: " + task.getDescription());
    }

    public void removeTask(String description) throws TaskNotFoundException {
        Task taskToRemove = tasks.stream()
                .filter(t -> t.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + description));
        tasks.remove(taskToRemove);
        LOGGER.info("Task removed successfully: " + description);
        notifyObservers("Task removed: " + description);
    }

    public List<Task> viewAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> viewTasksByPriority(Priority priority) {
        return tasks.stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public void editTask(String oldDescription, Task newTask) throws TaskNotFoundException, ScheduleConflictException {
        removeTask(oldDescription);
        addTask(newTask);
        notifyObservers("Task edited: " + oldDescription + " -> " + newTask.getDescription());
    }

    private boolean isConflicting(Task newTask) {
        return tasks.stream().anyMatch(existingTask -> existingTask.conflicts(newTask));
    }
}

// Task Factory
class TaskFactory {

    public static Task createTask(String description, LocalTime startTime, LocalTime endTime, Priority priority) {
        return new Task(description, startTime, endTime, priority);
    }
}

// Task class
class Task implements Comparable<Task> {

    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private Priority priority;
    private boolean completed;

    public Task(String description, LocalTime startTime, LocalTime endTime, Priority priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.completed = false;
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean conflicts(Task other) {
        return (this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime));
    }

    public void markCompleted() {
        this.completed = true;
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Task other) {
        return this.startTime.compareTo(other.startTime);
    }

    @Override
    public String toString() {
        String timeColor = ConsoleColors.PURPLE;
        String descriptionColor = ConsoleColors.BLUE;
        String priorityColor = switch (priority) {
            case HIGH ->
                ConsoleColors.RED;
            case MEDIUM ->
                ConsoleColors.YELLOW;
            case LOW ->
                ConsoleColors.GREEN;
        };
        String statusColor = completed ? ConsoleColors.GREEN : ConsoleColors.RESET;

        return String.format("%s%s - %s: %s%s %s[%s]%s%s",
                timeColor,
                startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                descriptionColor, description,
                priorityColor, priority,
                statusColor, completed ? " (Completed)" : "");
    }
}

enum Priority {
    LOW, MEDIUM, HIGH
}

// Custom exceptions
class ScheduleConflictException extends Exception {

    public ScheduleConflictException(String message) {
        super(message);
    }
}

class TaskNotFoundException extends Exception {

    public TaskNotFoundException(String message) {
        super(message);
    }
}

// Observer Pattern Interface
interface Observer {

    void update(String message);
}

// Concrete Observer
class UserNotification implements Observer {

    @Override
    public void update(String message) {
        System.out.println(ConsoleColors.PURPLE + "Notification: " + message + ConsoleColors.RESET);
    }
}

// Main application class
public class astronautscheduler {

    private static final Logger LOGGER = Logger.getLogger(astronautscheduler.class.getName());
    private static final ScheduleManager scheduleManager = ScheduleManager.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        LOGGER.info("Starting Astronaut Scheduler Application");
        scheduleManager.addObserver(new UserNotification());

        while (true) {
            displayMenu();
            int choice = getUserChoice();
            processUserChoice(choice);
        }
    }

    private static void displayMenu() {
        System.out.println("\n" + ConsoleColors.CYAN + "=== Astronaut Daily Schedule Organizer ===" + ConsoleColors.RESET);
        System.out.println("1. Add Task");
        System.out.println("2. Remove Task");
        System.out.println("3. View All Tasks");
        System.out.println("4. View Tasks by Priority");
        System.out.println("5. Edit Task");
        System.out.println("6. Mark Task as Completed");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println(ConsoleColors.RED + "Invalid input. Please enter a number." + ConsoleColors.RESET);
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void processUserChoice(int choice) {
        switch (choice) {
            case 1 ->
                addTaskInteractive();
            case 2 ->
                removeTaskInteractive();
            case 3 ->
                viewAllTasks();
            case 4 ->
                viewTasksByPriorityInteractive();
            case 5 ->
                editTaskInteractive();
            case 6 ->
                markTaskAsCompletedInteractive();
            case 7 -> {
                System.out.println(ConsoleColors.GREEN + "Thank you for using the Astronaut Scheduler. Goodbye!" + ConsoleColors.RESET);
                System.exit(0);
            }
            default ->
                System.out.println(ConsoleColors.RED + "Invalid choice. Please try again." + ConsoleColors.RESET);
        }
    }

    private static void addTaskInteractive() {
        scanner.nextLine(); // Consume newline
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();

        LocalTime startTime = null;
        LocalTime endTime = null;
        while (startTime == null) {
            System.out.print("Enter start time (HH:mm): ");
            String startTimeInput = scanner.nextLine();
            try {
                startTime = LocalTime.parse(startTimeInput);
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Error: Invalid start time format. Please use HH:mm (e.g., 14:30) between 00:00 to 23:59" + ConsoleColors.RESET);
            }
        }

        while (endTime == null) {
            System.out.print("Enter end time (HH:mm): ");
            String endTimeInput = scanner.nextLine();
            try {
                endTime = LocalTime.parse(endTimeInput);
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Error: Invalid end time format. Please use HH:mm (e.g., 16:30) between 00:00 to 23:59" + ConsoleColors.RESET);
            }
        }

        System.out.print("Enter priority (LOW/MEDIUM/HIGH): ");
        String priority = scanner.nextLine();

        try {
            Task task = TaskFactory.createTask(
                    description,
                    startTime,
                    endTime,
                    Priority.valueOf(priority.toUpperCase())
            );
            scheduleManager.addTask(task);
            System.out.println(ConsoleColors.GREEN + "Task added successfully." + ConsoleColors.RESET);
        } catch (ScheduleConflictException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
            // LOGGER.log(Level.WARNING, "Schedule conflict", e);
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColors.RED + "Error: Invalid input format for priority." + ConsoleColors.RESET);
            // LOGGER.log(Level.WARNING, "Invalid priority input", e);
        }
    }

    private static void removeTaskInteractive() {
        scanner.nextLine();
        System.out.print("Enter task description to remove: ");
        String description = scanner.nextLine();

        try {
            scheduleManager.removeTask(description);
            System.out.println(ConsoleColors.GREEN + "Task removed successfully." + ConsoleColors.RESET);
        } catch (TaskNotFoundException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
            LOGGER.log(Level.WARNING, "Task not found", e);
        }
    }

    private static void viewAllTasks() {
        List<Task> tasks = scheduleManager.viewAllTasks();
        if (tasks.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "No tasks scheduled for the day." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.CYAN + "=== All Tasks ===" + ConsoleColors.RESET);
            tasks.forEach(System.out::println);
        }
    }

    private static void viewTasksByPriorityInteractive() {
        scanner.nextLine(); // Consume newline
        System.out.print("Enter priority level (LOW/MEDIUM/HIGH): ");
        String priority = scanner.nextLine();

        try {
            Priority taskPriority = Priority.valueOf(priority.toUpperCase());
            List<Task> tasks = scheduleManager.viewTasksByPriority(taskPriority);
            if (tasks.isEmpty()) {
                System.out.println(ConsoleColors.YELLOW + "No tasks with priority: " + priority + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.CYAN + "=== Tasks with Priority: " + priority + " ===" + ConsoleColors.RESET);
                tasks.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColors.RED + "Error: Invalid priority level." + ConsoleColors.RESET);
            LOGGER.log(Level.WARNING, "Invalid priority level", e);
        }
    }

    private static void editTaskInteractive() {
        scanner.nextLine();
        System.out.print("Enter task description of you want to edit: ");
        String oldDescription = scanner.nextLine();

        List<Task> tasks = scheduleManager.viewAllTasks();
        Task taskToEdit = tasks.stream()
                .filter(task -> task.getDescription().equals(oldDescription))
                .findFirst()
                .orElse(null);

        if (taskToEdit == null) {
            System.out.println(ConsoleColors.RED + "Error: Task not found." + ConsoleColors.RESET);
            return;
        }

        if (taskToEdit.isCompleted()) {
            System.out.println(ConsoleColors.RED + "Error: Task is completed and cannot be edited." + ConsoleColors.RESET);
            return;
        }

        System.out.println("Which field would you like to edit?");
        System.out.println("1. Description");
        System.out.println("2. Start Time");
        System.out.println("3. End Time");
        System.out.println("4. Priority");
        System.out.print("Enter your choice (1-4): ");
        int fieldChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (fieldChoice) {
            case 1 -> {
                System.out.print("Enter new task description: ");
                String newDescription = scanner.nextLine();
                taskToEdit.setDescription(newDescription);
            }
            case 2 -> {
                System.out.print("Enter new start time (HH:mm): ");
                String newStartTime = scanner.nextLine();
                taskToEdit.setStartTime(LocalTime.parse(newStartTime));
            }
            case 3 -> {
                System.out.print("Enter new end time (HH:mm): ");
                String newEndTime = scanner.nextLine();
                taskToEdit.setEndTime(LocalTime.parse(newEndTime));
            }
            case 4 -> {
                System.out.print("Enter new priority (LOW/MEDIUM/HIGH): ");
                String newPriority = scanner.nextLine();
                taskToEdit.setPriority(Priority.valueOf(newPriority.toUpperCase()));
            }
            default -> {
                System.out.println(ConsoleColors.RED + "Invalid choice." + ConsoleColors.RESET);
                return;
            }
        }

        System.out.println(ConsoleColors.GREEN + "Task updated successfully." + ConsoleColors.RESET);
    }

    private static void markTaskAsCompletedInteractive() {
        scanner.nextLine();
        System.out.print("Enter task description to mark as completed: ");
        String description = scanner.nextLine();

        List<Task> tasks = scheduleManager.viewAllTasks();
        for (Task task : tasks) {
            if (task.getDescription().equals(description)) {
                task.markCompleted();
                System.out.println(ConsoleColors.GREEN + "Task marked as completed: " + description + ConsoleColors.RESET);
                return;
            }
        }
        System.out.println(ConsoleColors.RED + "Error: Task not found." + ConsoleColors.RESET);
    }
}
