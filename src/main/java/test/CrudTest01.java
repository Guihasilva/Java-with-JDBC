package test;

import serivce.AnimeService;
import serivce.ProducerService;

import java.util.Scanner;

public class CrudTest01 {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static void main(String[] args) {
        int op;


    while (true) {
        menu();
        op = Integer.parseInt(SCANNER.nextLine());
        if (op == 0) break;
        switch (op){
            case 1 -> {
                producerMenu();
                op = Integer.parseInt(SCANNER.nextLine());
                ProducerService.buildMenu(op);
            }
            case 2 ->{
                animeMenu();
                op = Integer.parseInt(SCANNER.nextLine());
                AnimeService.buildMenu(op);

            }

        }




    }
    }
    private static void menu() {
        System.out.println("Type the number of your operation");
        System.out.println("1. Producer");
        System.out.println("2. Anime");
        System.out.println("3. Exit");
    }

    private static void producerMenu() {
        System.out.println("Type the number of your operation");
        System.out.println("1. Search for Producer");
        System.out.println("2. Delete Producer By Id");
        System.out.println("3. Insert Producer");
        System.out.println("4. Update Producer");
        System.out.println("9. Go Back");
    }

    private static void animeMenu() {
        System.out.println("Type the number of your operation");
        System.out.println("1. Search for anime");
        System.out.println("2. Delete anime");
        System.out.println("3. Insert anime");
        System.out.println("4. Update anime");
        System.out.println("9. Go Back");
    }
}
