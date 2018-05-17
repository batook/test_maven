package com.batook.test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

class Record {
    private String name;
    private String status;
    private int executionCount;

    public Record() {
    }

    public Record(String name, String status, int executionCount) {
        setName(name);
        setStatus(status);
        setExecutionCount(executionCount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(int executionCount) {
        this.executionCount = executionCount;
    }
}

class SomeFile {
    private String path;
    private List<Record> records;
    private boolean isEnd;

    public SomeFile() {
        records = new ArrayList<>();
    }

    public SomeFile(String path, boolean isEnd) {
        this();
        setPath(path);
        setEnd(isEnd);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setEnd(boolean end) {
        this.isEnd = end;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    public List<Record> getRecords() {
        return records;
    }
}

class SomeFiles {
    private String name;
    private Map<String, SomeFile> pool;

    public SomeFiles() {
        pool = new HashMap<>();
    }

    public SomeFiles(String name) {
        this();
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void putSomeFile(String name, SomeFile someFile) {
        pool.put(name, someFile);
    }

    public Map<String, SomeFile> getPool() {
        return pool;
    }
}

class JaxonTest {
    public static void main(String[] args) {
        SomeFile f = new SomeFile("/root/secret.txt", false);
        f.addRecord(new Record("Some Record", "Some status", 42));
        f.addRecord(new Record("Another Record", "Same status", 0));

        SomeFiles sf = new SomeFiles("Some Files");
        sf.putSomeFile("Some File", f);

        ObjectMapper mapper = new ObjectMapper();
        // Для вывода с отступами
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            // Здесь происходит самая главная магия
            mapper.writeValue(new File("fileJaxon.json"), sf);
        } catch (JsonGenerationException exc) {
            exc.printStackTrace();
        } catch (JsonMappingException exc) {
            exc.printStackTrace();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}

class DataObject {
    private String data1 = "Some value";
    private int data2 = 123;
    private List<String> list = new ArrayList<String>() {
        {
            add("List item 1");
            add("List item 2");
            add("List item 3");
        }
    };

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public int getData2() {
        return data2;
    }

    public void setData2(int data2) {
        this.data2 = data2;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "[DataObject: data1=" + data1 + ", data2=" + data2 + ", list=" + list + "]";
    }
}

class GsonTest {
    private static final String FILENAME = "fileGson.json";

    public static void main(String[] args) {
        DataObject objToWrite = new DataObject();
        Gson gson = new GsonBuilder().setPrettyPrinting()
                                     .create();
        String string = gson.toJson(objToWrite);
        try (FileWriter fileWriter = new FileWriter(FILENAME)) {
            fileWriter.write(string);
        } catch (IOException ex) {
            Logger.getLogger(GsonTest.class.getName())
                  .log(Level.SEVERE, null, ex);
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            DataObject objToRead = gson.fromJson(reader, DataObject.class);
            System.out.println(objToRead);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GsonTest.class.getName())
                  .log(Level.SEVERE, null, ex);
        }
    }
}

class Item {
    String name;
    float price;
    private List<String> list = new ArrayList<String>() {
        {
            add("List item 1");
            add("List item 2");
            add("List item 3");
        }
    };

    public Item(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public static void main(String[] args) {
        Map<Integer, Item> mapItems = new HashMap<>();
        mapItems.put(1, new Item("Samsung", 51200.6f));
        mapItems.put(2, new Item("Lg", 5400.6f));
        mapItems.put(3, new Item("Alcatel", 4500.6f));

        String jsonStr = new Gson().toJson(mapItems);
        //String jsonStr = new Gson().toJson(mapItems, HashMap.class);
        System.out.println(jsonStr);

        List<Item> listItems = new ArrayList<>();
        listItems.add(new Item("Samsung", 51200.6f));
        listItems.add(new Item("Lg", 5400.6f));
        listItems.add(new Item("Alcatel", 4500.6f));

        String jsonStr2 = new Gson().toJson(listItems);
        System.out.println(jsonStr2);

        Item[] arrItems = new Item[3];
        arrItems[0] = new Item("Samsung", 51200.6f);
        arrItems[1] = new Item("Lg", 5400.6f);
        arrItems[2] = new Item("Alcatel", 4500.6f);

        String jsonStr3 = new Gson().toJson(arrItems);
        System.out.println(jsonStr3);
    }

    @Override
    public String toString() {
        return "GoodsItem{" + "name='" + name + '\'' + ", price=" + price + '}';
    }
}
