package com.Superlee.Supply.Presentation;

import java.util.Objects;
import java.util.Scanner;

import com.google.gson.Gson;

import com.Superlee.Supply.Service.PresentService;
import com.Superlee.Supply.Service.Responses.Response;
import com.Superlee.Supply.DataAccess.DataBaseCreator;
public class CLI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PresentService prService = PresentService.getInstance();
    private static final Gson gson = new Gson();

    public static void start(String[] args) {
        DataBaseCreator dataBaseCreator = new DataBaseCreator();
        try {
            dataBaseCreator.CreateAllTables();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        String input;
        Response output;
        prService.printMenu();
        while (true) {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("31")) {
                System.out.println("Goodbye!");
                break;
            }
            output = prService.call(input);
            // print the output response as string
            if (!Objects.equals(gson.toJson(output), "{}"))
                System.out.println(gson.toJson(output));
        }
    }
}
