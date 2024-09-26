package org.bharath;


import org.bharath.controller.UsersController;
import org.bharath.utils.CustomUserInput;
import org.bharath.view.UsersView;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    public static final CustomUserInput CUSTOM_USER_INPUT = CustomUserInput.getInstance();
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        initializeConsoleAndFileLogs();
        UsersView usersView = new UsersView();
        usersView.welcomeMessage();
    }

    private static void initializeConsoleAndFileLogs(){
        try(FileInputStream fileInputStream = new FileInputStream("src\\main\\java\\org\\bharath\\config\\log.properties")) {
            LogManager.getLogManager().readConfiguration(fileInputStream);
            FileHandler fileHandler = new FileHandler("src\\main\\java\\org\\bharath\\logs\\application.log");
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.severe(e.toString());
        }
    }
}