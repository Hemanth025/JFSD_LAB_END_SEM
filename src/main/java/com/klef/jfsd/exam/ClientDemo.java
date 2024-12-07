package com.klef.jfsd.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;
import java.util.List;

public class ClientDemo {

    public static void main(String[] args) {
        // Create SessionFactory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Department.class)
                .buildSessionFactory();

        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                System.out.println("\n--- Department Management ---");
                System.out.println("1. Add Department");
                System.out.println("2. View All Departments");
                System.out.println("3. Delete Department");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        // Add Department
                        addDepartment(factory, scanner);
                        break;
                    case 2:
                        // View All Departments
                        viewAllDepartments(factory);
                        break;
                    case 3:
                        // Delete Department
                        deleteDepartment(factory, scanner);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } finally {
            factory.close();
            scanner.close();
        }
    }

    private static void addDepartment(SessionFactory factory, Scanner scanner) {
        // Prompt user for department details
        System.out.print("Enter Department Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Department Location: ");
        String location = scanner.nextLine();
        System.out.print("Enter HoD Name: ");
        String hodName = scanner.nextLine();

        // Create Department object
        Department department = new Department();
        department.setName(name);
        department.setLocation(location);
        department.setHodName(hodName);

        // Save to database
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.save(department);
            session.getTransaction().commit();
            System.out.println("Department added successfully!");
        } finally {
            session.close();
        }
    }

    private static void viewAllDepartments(SessionFactory factory) {
        // Retrieve all departments
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            List<Department> departments = session.createQuery("from Department", Department.class).getResultList();
            session.getTransaction().commit();

            // Display departments
            if (departments.isEmpty()) {
                System.out.println("No departments found!");
            } else {
                for (Department department : departments) {
                    System.out.println("ID: " + department.getId() + ", Name: " + department.getName() +
                                       ", Location: " + department.getLocation() +
                                       ", HoD: " + department.getHodName());
                }
            }
        } finally {
            session.close();
        }
    }

    private static void deleteDepartment(SessionFactory factory, Scanner scanner) {
        // Prompt user for department ID to delete
        System.out.print("Enter Department ID to delete: ");
        int id = scanner.nextInt();

        // Delete from database
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            Department department = session.get(Department.class, id);
            if (department != null) {
                session.delete(department);
                System.out.println("Department deleted successfully!");
            } else {
                System.out.println("Department not found!");
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }
}
