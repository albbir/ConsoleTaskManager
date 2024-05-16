import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskManager {
    private static final String FILENAME = "tasks.ser";
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadTasks();
        boolean exit = false;
        while (!exit) {
            System.out.println("Доступные команды:");
            System.out.println("1. Добавить задачу");
            System.out.println("2. Показать список задач");
            System.out.println("3. Отметить задачу как выполненную");
            System.out.println("4. Удалить задачу");
            System.out.println("5. Выйти из программы");
            System.out.print("Выберите действие (введите номер команды): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    showTasks();
                    break;
                case 3:
                    markTaskCompleted();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Неправильный выбор. Попробуйте еще раз.");
            }
        }
        saveTasks();
        System.out.println("Программа завершена.");
    }

    private static void addTask() {
        System.out.print("Введите название задачи: ");
        String title = scanner.nextLine();
        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine();
        Task task = new Task(title, description);
        tasks.add(task);
        System.out.println("Задача \"" + title + "\" успешно добавлена!");
    }

    private static void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
        } else {
            System.out.println("Список задач:");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.println((i + 1) + ". " + (task.isCompleted() ? "[X] " : "[ ] ") + task.getTitle() + " - " + task.getDescription());
            }
        }
    }

    private static void markTaskCompleted() {
        showTasks();
        System.out.print("Введите номер задачи, которую хотите отметить как выполненную: ");
        int index = scanner.nextInt();
        if (index > 0 && index <= tasks.size()) {
            Task task = tasks.get(index - 1);
            task.markCompleted();
            System.out.println("Задача \"" + task.getTitle() + "\" отмечена как выполненная!");
        } else {
            System.out.println("Неправильный номер задачи.");
        }
    }

    private static void deleteTask() {
        showTasks();
        System.out.print("Введите номер задачи, которую хотите удалить: ");
        int index = scanner.nextInt();
        if (index > 0 && index <= tasks.size()) {
            Task task = tasks.remove(index - 1);
            System.out.println("Задача \"" + task.getTitle() + "\" успешно удалена!");
        } else {
            System.out.println("Неправильный номер задачи.");
        }
    }

    private static void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            tasks = (ArrayList<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Ignore if file not found or tasks cannot be loaded
        }
    }
}
