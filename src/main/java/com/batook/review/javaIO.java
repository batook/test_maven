package com.batook.review;

import java.io.*;

public class javaIO {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        fileInputOutputStream();
        fileReadeWriter();
        bufferedReaderWriter();
        bufferedInputOutputStream();
        objectInputOutputStream();
        inputOutputStreamReaderWriter();
        printStreamPrintWriter();
    }

    static void fileInputOutputStream() throws IOException {
        InputStream input = new FileInputStream("testIN.txt");
        OutputStream output = new FileOutputStream("testOUT.txt");
        int data = input.read();
        while (data != -1) {
            System.out.print((char) data);
            output.write(data);
            data = input.read();
        }
        input.close();
        output.close();
    }

    static void fileReadeWriter() throws IOException {
        Reader reader = new FileReader("testIN.txt");
        Writer writer = new FileWriter("testOUT.txt");
        int data = reader.read();
        while (data != -1) {
            System.out.print((char) data);
            writer.write(data);
            data = reader.read();
        }
        reader.close();
        writer.close();
    }

    static void bufferedReaderWriter() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("testIN.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("testOUT.txt"));
        String line = reader.readLine();
        while (line != null) {
            System.out.print(line);
            writer.write(line);
            line = reader.readLine();
        }
        reader.close();
        writer.close();
    }

    static void bufferedInputOutputStream() throws IOException {
        InputStream input = new BufferedInputStream(new FileInputStream("testIN.txt"));
        OutputStream output = new BufferedOutputStream(new FileOutputStream("testOUT.txt"));
        byte[] buffer = new byte[1024];
        int lengthRead;
        while ((lengthRead = input.read(buffer)) > 0) {
            output.write(buffer, 0, lengthRead);
            output.flush();
        }
        input.close();
        output.close();
    }

    static void objectInputOutputStream() throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(new FileInputStream("testOBJ.txt"));
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("testOBJ.txt"));
        output.writeObject("Hello OBJ");
        String str = "Failed obj";
        Object object = input.readObject();
        if (object instanceof String) str = (String) object;
        System.out.println(str);
        input.close();
        output.close();
    }

    static void inputOutputStreamReaderWriter() throws IOException {
        Reader input = new InputStreamReader(new FileInputStream("testIN.txt"));
        Writer output = new OutputStreamWriter(new FileOutputStream("testOUT.txt"));

        int data = input.read();
        while (data != -1) {
            char ch = (char) data;
            output.write(data);
            data = input.read();
        }
        input.close();
        output.close();
    }

    static void printStreamPrintWriter() throws IOException {
        PrintStream printStream = new PrintStream(new FileOutputStream("testOUT.txt"));
        printStream.print("Hello World");
        printStream.close();

        PrintWriter printWriter = new PrintWriter(new FileWriter("testOUT.txt"));
        printWriter.print("Hello World");
        printWriter.close();
    }
}