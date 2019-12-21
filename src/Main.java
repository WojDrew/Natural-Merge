
import disk.Tape;
import algorithm.NaturalSort;
import generator.Generator;

import java.util.Scanner;


public class Main {


    public static void main(String[] args){

        NaturalSort ns = new NaturalSort("dates");
        Scanner in = new Scanner(System.in);
        String command;
        boolean isRunning = true;
        System.out.print("Wojciech Drewek 171838 \n");
        System.out.print("Struktury Baz Danych - Projekt 1 \n");
        while(isRunning){
            System.out.print("Wypisywanie po każdej fazie jest ");
            if(ns.getPrintAfterPhase())
                System.out.print("wlaczone\r\n");
            else
                System.out.print("wylaczone\r\n");
            System.out.print(">");
            command = in.next();
            switch (command){
                case "q":
                    isRunning = false;
                    break;
                case "s":
                    Tape t = ns.getInputTape();
                    System.out.print("Plik przed posortowaniem:\r\n");
                    t.printTape();
                    t = ns.sort();
                    System.out.print("Plik po posortowaniu:\r\n");
                    t.printTape();
                    break;
                case "r":
                    System.out.print("Podaj liczbe rekordow: ");
                    int n = in.nextInt();
                    String randomRecords = Generator.generate(n);
                    ns.setRecords(randomRecords);
                    break;
                case "w":
                    System.out.print("Podaj liczbe rekordow: ");
                    int n2 = in.nextInt();
                    String records = "";
                    for(int i = 0; i <= n2; i++){
                        String r = in.nextLine();
                        records += r;
                    }
                    ns.setRecords(records);
                    break;
                case "p":
                    ns.changePrintAfterPhase();
                    break;
                case "h":
                    System.out.print("Lista komend:\r\n");
                    System.out.print("q - wylacz program\r\n");
                    System.out.print("s - posortuj\r\n");
                    System.out.print("r - wygeneruj losowe dane\r\n");
                    System.out.print("w - wpisz dane do psortowania\r\n");
                    System.out.print("p - wl/wyl wypisywania tasm po kazdej fazie\r\n");
                    System.out.print("l - zaladowanie rekordow z pliku\r\n");
                    break;
                case "l":
                    System.out.println("Podaj pelna sciezke do pliku:");
                    in.nextLine();
                    String inputFile = in.nextLine();
                    ns.newFile(inputFile);
                    break;
                default:
                    System.out.print("Brak takiej komendy, wprowadź 'h' by uzyskac liste komend\r\n");
                    break;

            }
        }
    }
}
