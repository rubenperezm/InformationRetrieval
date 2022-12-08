package es.uca.main;

import java.util.Scanner;
import es.uca.indexer.Indexer;
import es.uca.retriever.Retriever;
public class App {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int opt;
        do{
            System.out.println("Options:\n\t1. Index\n\t2. Make Query\n\t3. Exit\n");
            opt = sc.nextInt();

            switch (opt) {
                case 1 -> Indexer.index();
                case 2 -> Retriever.retrieve();
                case 3 -> System.out.println("Bye.");
                default -> System.out.println("Please choose a valid option.");
            }

        }while(opt != 3);
    }

}
