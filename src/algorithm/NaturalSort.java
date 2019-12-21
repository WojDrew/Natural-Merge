package algorithm;

import disk.Tape;

public class NaturalSort {
    private Tape inputTape;
    private Tape t1;
    private Tape t2;
    private boolean printAfterPhase;
    private int countOfPhase;
    private boolean isSorted;
    private int countOfSeries;

    public NaturalSort(String inputFileName){
        this.inputTape = new Tape(inputFileName);
        this.t1 = new Tape("t1");
        this.t2 = new Tape("t2");
        this.printAfterPhase = false;
        this.countOfPhase = 0;
        this.isSorted = true;
        this.countOfSeries = 0;
    }

    public Tape sort(){
        while(true) {
            this.split();
            this.merge();
            if(countOfPhase == 0)
                System.out.print("Liczba serii na poczatku: " + (countOfSeries + 1) + "\r\n");
            this.countOfPhase += 1;
            if(printAfterPhase){
                System.out.print("Tasma glowna po fazie " + countOfPhase + "\r\n");
                this.inputTape.printTape();
                System.out.print("Tasma t1 po fazie " + countOfPhase + "\r\n");
                this.t1.printTape();
                System.out.print("Tasma t2 po fazie " + countOfPhase + "\r\n");
                this.t2.printTape();
            }
            if(isSorted)
                break;
        }
        System.out.print("Sortowanie zakończone\r\n");
        System.out.print("Liczba faz: " + this.countOfPhase + "\r\n");
        System.out.print("Liczba zapisow stron na tasmie glownej: " + this.inputTape.getCountOfWrite() + "\r\n");
        System.out.print("Liczba odczytow stron na tasmie glownej: " + this.inputTape.getCountOfRead() + "\r\n");
        System.out.print("Liczba zapisow stron na tasmie t1: " + this.t1.getCountOfWrite() + "\r\n");
        System.out.print("Liczba odczytow stron na tasmie t1: " + this.t1.getCountOfRead() + "\r\n");
        System.out.print("Liczba zapisow stron na tasmie t2: " + this.t2.getCountOfWrite() + "\r\n");
        System.out.print("Liczba odczytow stron na tasmie t2: " + this.t2.getCountOfRead() + "\r\n");
        int sumRead = inputTape.getCountOfRead() + t1.getCountOfRead() + t2.getCountOfRead();
        int sumWrite = inputTape.getCountOfWrite() + t1.getCountOfWrite() + t2.getCountOfWrite();
        System.out.print("Laczna liczba zapisow stron: " + sumWrite + "\r\n");
        System.out.print("Laczna liczba odczytow stron: " + sumRead + "\r\n");
        this.countOfPhase = 0;
        this.inputTape.resetCounters();
        return this.inputTape;
    }

    private void merge(){
        this.inputTape.openWriteStream();
        this.t2.openReadStream();
        this.t1.openReadStream();
        String t1Record = "";
        String t2Record = "";
        String lastT1Record = "00/00/0000";
        String lastT2Record = "00/00/0000";
        boolean t1EndOfSeries = false;
        boolean t2EndOfSeries = false;
        t1Record = this.t1.readNextRecord();
        t2Record = this.t2.readNextRecord();

        String mainRecord = "";
        String lastMainRecord = "00/00/0000";
        this.isSorted = true;

        while(true) {

            if(t1Record.equals("end") && t2Record.equals("end")){
                break;
            }

            if(isLarger(lastT1Record,t1Record)){
                t1EndOfSeries = true;
            }
            if(isLarger(lastT2Record,t2Record)){
                t2EndOfSeries = true;
            }

            if(t1EndOfSeries && t2EndOfSeries){
                lastT1Record = "00/00/0000";
                lastT2Record = "00/00/0000";
                t1EndOfSeries = false;
                t2EndOfSeries = false;
            }

            if((isLarger(t1Record,t2Record) && !t2EndOfSeries && !t2Record.equals("end")) || t1EndOfSeries || t1Record.equals("end")){
                this.inputTape.writeRecord(t2Record);
                mainRecord = t2Record;
                lastT2Record = t2Record;
                t2Record = this.t2.readNextRecord();
            }
            else if((isLarger(t2Record,t1Record)) || t2EndOfSeries || t2Record.equals("end")){
                this.inputTape.writeRecord(t1Record);
                mainRecord = t1Record;
                lastT1Record = t1Record;
                t1Record = this.t1.readNextRecord();
            }
            if(isLarger(lastMainRecord, mainRecord)){
                this.isSorted = false;
            }
            lastMainRecord = mainRecord;
        }
        this.inputTape.closeWriteStream();
        this.t1.closeReadStream();
        this.t2.closeReadStream();
    }

    private void split(){
        this.t1.openWriteStream();
        this.t2.openWriteStream();
        this.inputTape.openReadStream();
        String record = "";
        String lastRecord = "00/00/0000";
        int policeman = 1;
        while(true){
            record = this.inputTape.readNextRecord();
            if(record.equals("end")){
                break;
            }
            if(!this.isLarger(record,lastRecord)){
                policeman += 1;
                this.countOfSeries += 1;
            }

            if(policeman % 2 == 1){
                this.t1.writeRecord(record);
            } else {
                this.t2.writeRecord(record);
            }
            lastRecord = record;
        }
        this.t1.closeWriteStream();
        this.t2.closeWriteStream();
        this.inputTape.closeReadStream();
    }

    private boolean isLarger(String leftRecord, String rightRecord){
        if(leftRecord.equals("end")){
            return false;
        }
        if(rightRecord.equals("end")){
            return true;
        }

        int leftYear = getYear(leftRecord);
        int rightYear = getYear(rightRecord);

        if(leftYear != rightYear)
            return leftYear > rightYear;

        int leftMonth = getMonth(leftRecord);
        int rightMonth = getMonth(rightRecord);

        if(leftMonth != rightMonth)
            return leftMonth > rightMonth;

        int leftDay = getDay(leftRecord);
        int rightDay = getDay(rightRecord);

        return leftDay > rightDay;

    }

    public void setRecords(String s){
        this.inputTape.writeString(s);
    }

    public void changePrintAfterPhase(){
        this.printAfterPhase = !printAfterPhase;
    }

    public boolean getPrintAfterPhase(){
        return this.printAfterPhase;
    }

    private int getYear(String s){
        int year = 0;
        year += Character.getNumericValue(s.charAt(9));
        year += 10*Character.getNumericValue(s.charAt(8));
        year += 100*Character.getNumericValue(s.charAt(7));
        year += 1000*Character.getNumericValue(s.charAt(6));
        return year;
    }

    private int getMonth(String s){
        int month = 0;
        month += Character.getNumericValue(s.charAt(4));
        month += 10*Character.getNumericValue(s.charAt(3));
        return month;
    }

    private int getDay(String s){
        int day = 0;
        day += Character.getNumericValue(s.charAt(1));
        day += 10*Character.getNumericValue(s.charAt(0));
        return day;
    }

    public Tape getInputTape(){
        return this.inputTape;
    }

    public void newFile(String inputFile){
        this.inputTape = new Tape(inputFile);
    }
}
