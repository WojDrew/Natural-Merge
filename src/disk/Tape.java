package disk;

import java.io.*;

public class Tape {
    static public int pageSize = 100;
    static public int recordSize = 10;

    private File file;
    private FileReader reader;
    private FileWriter writer;

    private byte[] buffer;

    private int position;
    private int countOfRead;
    private int countOfWrite;


    public Tape(String fileName){
        buffer = new byte[pageSize];
        position = 0;
        this.file = new File(fileName + ".txt");
        this.countOfWrite = 0;
        this.countOfRead = 0;
    }

    private void readPage(){
        for(int i = 0; i < pageSize; i++){
            try {
                buffer[i] = (byte)reader.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.countOfRead += 1;
        position = 0;
    }

    private void writePage(){
        for(int i = 0; i < position; i++){
            try {
                 writer.write(buffer[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.countOfWrite += 1;
        position = 0;
    }

    public String readNextRecord(){
        if(position == pageSize)
            this.readPage();
        String record = "";
        for(int i = 0; i < recordSize; i++) {
            byte c = buffer[position];
            if(c != -1)
                record += (char)c;
            else
                record = "end";
            position += 1;
        }
        return record;
    }

    public void writeRecord(String str){
        if(position == pageSize)
            this.writePage();
        for(int i = 0; i < recordSize; i++){
            buffer[position] = (byte)str.codePointAt(i);
            position += 1;
        }
    }

    public void printTape(){
        this.openRead();
        int i = 0;
        while(true){

            int s = 0;
            try {
                s = this.reader.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(s == -1)
            {
                break;
            }
            System.out.print((char)s);
            if(i == 9) {
                System.out.print("\r\n");
                i = 0;
            } else
                i++;
        }
        this.closeReadStream();
    }

    public void writeString(String s){
        this.openWriteStream();
        for(int i = 0; i < s.length(); i++){
            try {
                this.writer.write(s.codePointAt(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.closeWrite();
    }

    private void openRead(){
        try {
            this.reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeWrite(){
        try {
            this.writer.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void openReadStream(){
        try {
            this.reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        position = 0;
        readPage();
    }

    public void openWriteStream(){
        try {
            this.writer = new FileWriter(file);
        }  catch (IOException e){
            System.out.println(e.getMessage());
        }
        position = 0;
    }

    public void closeReadStream(){
        try {
            this.reader.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void closeWriteStream(){
        writePage();
        try {
            this.writer.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void resetCounters(){
        this.countOfRead = 0;
        this.countOfWrite = 0;
    }

    public int getCountOfRead() {
        return countOfRead;
    }

    public int getCountOfWrite() {
        return countOfWrite;
    }

}
