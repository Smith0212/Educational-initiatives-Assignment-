# Astronaut Daily Schedule Organizer

## Overview

The Astronaut Daily Schedule Organizer is a console-based application designed to help astronauts manage their daily tasks efficiently. This application allows users to add, remove, view, and manage tasks with specific time slots and priority levels.

## Features

- Add new tasks with description, start time, end time, and priority level
- Remove existing tasks
- View all tasks sorted by start time
- Validate task conflicts (no overlapping tasks)
- Provide error messages for invalid operations
- Edit existing tasks
- Mark tasks as completed
- View tasks by priority level

## Technical Details

- Language: Java
- Design Patterns:
  - Singleton Pattern: Used for the ScheduleManager class
  - Factory Pattern: Implemented in the TaskFactory class
  - Observer Pattern: Used for notifying users of task conflicts or updates

## Setup and Installation

- Clone the repository:
   ```bash
   git clone https://github.com/Smith0212/Educational-initiatives-Assignment-
   ```

## Usage

Run the main application file:
```bash
cd ".\New folder"
```
```bash
javac com/astronautscheduler/astronautscheduler.java
```
```bash
java com.astronautscheduler.astronautscheduler
```

## Screenshots

### Add Task
![Add Task Screenshot](assets/image1.png)

### View All Tasks
![View All Tasks Screenshot](assets/image2.png)

### Remove Task
![Remove Task Screenshot1](assets/image31.png)
![Remove Task Screenshot2](assets/image32.png)

### Task Conflict Error Handling
![Conflict Handling Screenshot](assets/image4.png)

### Edit Task
![Edit Task Screenshot1](assets/image51.png)
![Edit Task Screenshot2](assets/image52.png)

### Mark Task as Completed
![Mark Task Completed Screenshot](assets/image6.png)

### View Tasks by Priority
![View Tasks by Priority Screenshot](assets/image7.png)

## Error Handling

The application includes robust error handling to manage:
- Task conflicts
- Non-existent task operations

## Logging

A logging mechanism is implemented to track application usage and errors.

