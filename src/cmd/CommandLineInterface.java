/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cmd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author aleja
 */

public class CommandLineInterface extends JFrame {
    private JTextField textField;
    private JTextArea textArea;
    private File currentDirectory;

    public CommandLineInterface() {
        setTitle("CLI");
        setSize(913, 526);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String text = textField.getText();
                textArea.append(currentDirectory.getAbsolutePath() + ">" + text + "\n");
                processCommand(text);
                textField.setText("");
            }
        });
        getContentPane().add(textField, BorderLayout.SOUTH);
        textField.setColumns(10);

        textArea = new JTextArea();
        getContentPane().add(textArea, BorderLayout.CENTER);

        currentDirectory = new File(System.getProperty("user.dir"));
    }

    private void processCommand(String command) {
        String[] parts = command.split(" ");
        switch (parts[0].toLowerCase()) {
            case "mkdir":
                createDirectory(parts[1]);
                break;
            case "mfile":
                createFile(parts[1]);
                break;
            case "rm":
                deleteFileOrDirectory(parts[1]);
                break;
            case "cd":
                changeDirectory(parts[1]);
                break;
            case "dir":
                listFilesAndDirectories();
                break;
            case "date":
                showCurrentDate();
                break;
            case "time":
                showCurrentTime();
                break;
            case "escribir":
                writeToFile(parts[1]);
                break;
            case "leer":
                readFile(parts[1]);
                break;
            default:
                textArea.append("Comando desconocido: " + parts[0] + "\n");
                break;
        }
    }

    private void createDirectory(String name) {
        File dir = new File(currentDirectory, name);
        if (dir.mkdir()) {
            textArea.append("Directorio creado: " + dir.getAbsolutePath() + "\n");
        } else {
            textArea.append("Error al crear el directorio.\n");
        }
    }

    private void createFile(String name) {
        File file = new File(currentDirectory, name);
        try {
            if (file.createNewFile()) {
                textArea.append("Archivo creado: " + file.getAbsolutePath() + "\n");
            } else {
                textArea.append("Error al crear el archivo.\n");
            }
        } catch (IOException e) {
            textArea.append("Error al crear el archivo: " + e.getMessage() + "\n");
        }
    }

    private void deleteFileOrDirectory(String name) {
        File file = new File(currentDirectory, name);
        try {
            Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            textArea.append("Archivo o directorio eliminado: " + file.getAbsolutePath() + "\n");
        } catch (IOException e) {
            textArea.append("Error al eliminar el archivo o directorio: " + e.getMessage() + "\n");
        }
    }

    private void changeDirectory(String name) {
        File dir = new File(currentDirectory, name);
        if (dir.isDirectory()) {
            currentDirectory = dir;
            textArea.append("Directorio cambiado a: " + dir.getAbsolutePath() + "\n");
        } else {
            textArea.append("No es un directorio: " + dir.getAbsolutePath() + "\n");
        }
    }

    private void listFilesAndDirectories() {
        File[] files = currentDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                textArea.append(file.getName() + "\n");
            }
        }
    }

    private void showCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(date);
        textArea.append(strDate + "\n");
    }

    private void showCurrentTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
        String strTime = formatter.format(date);
        textArea.append(strTime + "\n");
    }

    private void writeToFile(String name) {
        File file = new File(currentDirectory, name);
        String input = JOptionPane.showInputDialog("Por favor, ingresa el texto que quieres escribir en el archivo:");
        if (input != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                writer.println(input);
                textArea.append("Texto escrito en el archivo: " + file.getAbsolutePath() + "\n");
            } catch (IOException e) {
                textArea.append("Error al escribir en el archivo: " + e.getMessage() + "\n");
            }
        }
    }

    private void readFile(String name) {
        File file = new File(currentDirectory, name);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException e) {
            textArea.append("Error al leer el archivo: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        cli.setVisible(true);
    }
}
