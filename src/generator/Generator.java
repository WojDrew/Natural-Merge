package generator;

import java.util.Random;

public class Generator {

    public static String generate(int n){
        Random gen = new Random();
        String date = "";
        for(int i = 0; i < n; i++){
            int year = gen.nextInt(3000);
            int month = gen.nextInt(12) + 1;
            int leapYear = 0;
            int day = gen.nextInt(31) + 1;

            if(year % 4 == 0)
                leapYear = 1;

            if(month % 2 == 0)
                day = gen.nextInt(30);

            if(month == 2)
                day = gen.nextInt(28) + leapYear;

            date = addDayOrMonth(day,date);
            date = addDayOrMonth(month,date);
            date = addYear(year,date);
        }
        return date;
    }
    private static String addDayOrMonth(int n, String date){
        if(n < 10) {
            date += "0";
            date += Integer.toString(n);
        } else {
            date += Integer.toString(n);
        }
        date += "/";
        return date;
    }

    private static String addYear(int year, String date){
        if(year < 10) {
            date += "000";
            date += Integer.toString(year);
        } else if(year < 100) {
            date += "00";
            date += Integer.toString(year);
        } else if(year < 1000) {
            date += "0";
            date += Integer.toString(year);
        } else {
            date += Integer.toString(year);
        }
        return date;
    }
}
