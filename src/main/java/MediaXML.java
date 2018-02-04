import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MediaXML {
    final static String fileName = "media.xml";
    List<Item> itemList = new ArrayList<>();

    public static void main(String[] args) {
        MediaXML mediaXML = new MediaXML();
        boolean isValid = new XSDValidator().validateXMLSchema("media.xsd", fileName);
        if (isValid) {
            new MediaSAX(mediaXML).parse();
            //mediaXML.checkItems();
            new MediaDOM().createXML(mediaXML.itemList, "test.xml");
            System.out.println(new XSDValidator().validateXMLSchema("media.xsd", "test.xml"));
        }
    }

    void checkItems() {
        itemList.sort(null);
        itemList.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                int result;
                result = o1.getType().compareTo(o2.getType());
                if (result != 0) return result;
                result = o1.getTitle().compareTo(o2.getTitle());
                return result;
            }
        });
        for (Item item : itemList) {
            System.out.println(item);
            System.out.println("\t" + item.getBarcodes());
            System.out.println("\t" + item.getTitle());
            System.out.println("\t" + item.getCoverPath());
            System.out.println("\t" + item.getVideoPath());
            System.out.println("\t" + item.getDescription());
            System.out.println("\t" + item.getType());
            System.out.println("\t" + item.getGenre());
            System.out.println("\t" + item.getHit());
            if (item.getDisk() != null) {
                System.out.println("\t" + item.getDisk());
                for (Track track : item.getDisk().getTracks()) {
                    System.out.println("\t\t" + track);
                    System.out.println("\t\t" + track.getName());
                    System.out.println("\t\t" + track.getPath());
                }
            }
        }
        System.out.println(itemList.size() + " itemList");
    }
}

class XSDValidator {
    boolean validateXMLSchema(String xsdPath, String xmlPath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
            return true;
        } catch (IOException | SAXException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }
}

class XSLTransform {
    public void xml2Html(String xmlFile, String xslFile) {
        try {
            StreamSource xml = new StreamSource(new FileInputStream(xmlFile));
            StreamSource style = new StreamSource(new FileInputStream(xslFile));
            StreamResult result = new StreamResult(new FileOutputStream(xmlFile.replaceAll("\\.xml$", "\\.html")));
            Transformer transformer = TransformerFactory.newInstance().newTransformer(style);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xml, result);
        } catch (IOException | TransformerException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}

class MediaDOM {
    void createXML(List<Item> itemList, String xmlFile) {
        Document doc = getDocument();
        Element root = doc.createElement("REPORT");
        root.setAttribute("MediaStation", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        doc.appendChild(root);
        Element items = doc.createElement("ITEMS");
        root.appendChild(items);
        for (Item i : itemList) {
            Element item = doc.createElement("ITEM");
            item.setAttribute("ID", i.getId());
            items.appendChild(item);
            Element barcodes = doc.createElement("BARCODES");
            item.appendChild(barcodes);
            for (Barcode b : i.getBarcodes()) {
                Element barcode = doc.createElement("BARCODE");
                barcode.setTextContent(b.getBarcode());
                barcodes.appendChild(barcode);
            }
            Element title = doc.createElement("TITLE");
            title.setTextContent(i.getTitle());
            item.appendChild(title);
            Element cover_path = doc.createElement("COVER_PATH");
            cover_path.setTextContent(i.getCoverPath());
            item.appendChild(cover_path);
            Element video_path = doc.createElement("VIDEO_PATH");
            video_path.setTextContent(i.getVideoPath());
            item.appendChild(video_path);
            Element description = doc.createElement("DESCRIPTION");
            description.setTextContent(i.getDescription());
            item.appendChild(description);
            Element type = doc.createElement("TYPE");
            type.setTextContent(i.getType());
            item.appendChild(type);
            Element genre = doc.createElement("GENRE");
            genre.setTextContent(i.getGenre());
            item.appendChild(genre);
            Element hit = doc.createElement("IS_HIT");
            hit.setTextContent(i.getHit());
            item.appendChild(hit);
            if (i.getDisk() != null) {
                Element disk = doc.createElement("DISK");
                disk.setAttribute("NUMBER", i.getDisk().getNumber());
                item.appendChild(disk);
                for (Track t : i.getDisk().getTracks()) {
                    Element track = doc.createElement("TRACK");
                    track.setAttribute("NUMBER", t.getNumber());
                    disk.appendChild(track);
                    Element name = doc.createElement("NAME");
                    name.setTextContent(t.getName());
                    track.appendChild(name);
                    Element path = doc.createElement("PATH");
                    path.setTextContent(t.getPath());
                    track.appendChild(path);
                }
            }
        }
        writeDocument(doc, xmlFile);
    }

    Document getDocument() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            System.out.printf("Version = %s%n", doc.getXmlVersion());
            System.out.printf("Encoding = %s%n", doc.getXmlEncoding());
            System.out.printf("Standalone = %b%n%n", doc.getXmlStandalone());
            return doc;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    void writeDocument(Document doc, String xmlFile) {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(xmlFile));
            tr.transform(source, result);
        } catch (FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        }
    }
}

class MediaSAX {
    MediaXML m;
    Item item;
    Barcode barcode;
    List<Barcode> barcodes;
    Disk disk;
    Track track;
    List<Track> tracks;
    Tag tag = new Tag();

    public MediaSAX(MediaXML m) {
        this.m = m;
    }

    void parse() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                @Override
                public void startDocument() throws SAXException {
                    System.out.println("Start document");
                }

                @Override
                public void endDocument() throws SAXException {
                    System.out.println("End document");
                    System.out.println("Processed " + m.itemList.size() + " itemList");
                }

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    System.out.print("<" + qName + ">");
                    tag.set(qName, true);
                    switch (qName) {
                        case "ITEM":
                            item = new Item();
                            break;
                        case "BARCODES":
                            barcode = new Barcode();
                            barcodes = new ArrayList<>();
                            break;
                        case "DISK":
                            disk = new Disk();
                            tracks = new ArrayList<>();
                            break;
                        case "TRACK":
                            track = new Track();
                            break;
                    }
                    for (int i = 0; i < attributes.getLength(); i++)
                        switch (attributes.getLocalName(i)) {
                            case "ID":
                                if (tag.isSet("ITEM")) {
                                    System.out.print("ID=" + attributes.getValue(i));
                                    item.setId(attributes.getValue(i));
                                }
                                break;
                            case "NUMBER":
                                if (tag.isSet("DISK")) {
                                    System.out.print("NUMBER=" + attributes.getValue(i));
                                    disk.setNumber(attributes.getValue(i));
                                }
                                if (tag.isSet("TRACK")) {
                                    System.out.print("NUMBER=" + attributes.getValue(i));
                                    track.setNumber(attributes.getValue(i));
                                }
                                break;
                        }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    System.out.print("<" + qName + "/>");
                    tag.set(qName, false);
                    switch (qName) {
                        case "ITEM":
                            m.itemList.add(item);
                            break;
                        case "BARCODES":
                            barcodes.add(barcode);
                            item.setBarcodes(barcodes);
                            break;
                        case "DISK":
                            disk.setTracks(tracks);
                            item.setDisk(disk);
                            break;
                        case "TRACK":
                            tracks.add(track);
                            break;
                        case "REPORT":
                            System.out.println(" ");
                            break;
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    String val = new String(ch, start, length);
                    if (tag.isSet("BARCODE")) {
                        barcode.setBarcode(val);
                    }
                    if (tag.isSet("TITLE")) {
                        item.setTitle(val);
                    }
                    if (tag.isSet("COVER_PATH")) {
                        item.setCoverPath(val);
                    }
                    if (tag.isSet("VIDEO_PATH")) {
                        item.setVideoPath(val);
                    }
                    if (tag.isSet("DESCRIPTION")) {
                        item.setDescription(val);
                    }
                    if (tag.isSet("TYPE")) {
                        item.setType(val);
                    }
                    if (tag.isSet("GENRE")) {
                        item.setGenre(val);
                    }
                    if (tag.isSet("IS_HIT")) {
                        item.setHit(val);
                    }
                    if (tag.isSet("NAME")) {
                        track.setName(val);
                    }
                    if (tag.isSet("PATH")) {
                        track.setPath(val);
                    }
                    System.out.print(new String(ch, start, length));
                }

                @Override
                public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
                    characters(ch, start, length);
                }
            };
            parser.parse(MediaXML.fileName, handler);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    class Tag {
        private boolean isSet;
        private String name;

        public void set(String name, boolean isSet) {
            this.name = name;
            this.isSet = isSet;
        }

        public boolean isSet(String name) {
            return this.name.equals(name) && isSet;
        }
    }
}


class Item implements Comparable<Item> {
    private String id;
    private List<Barcode> barcodes;
    private Disk disk;
    private String title;
    private String coverPath;
    private String videoPath;
    private String description;
    private String type;
    private String genre;
    private String isHit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item that = (Item) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Item ID=" + id;
    }

    public List<Barcode> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(List<Barcode> barcodes) {
        this.barcodes = barcodes;
    }

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getHit() {
        return isHit;
    }

    public void setHit(String hit) {
        isHit = hit;
    }

    @Override
    public int compareTo(Item o) {
        return id.compareTo(o.getId());
    }
}

class Barcode {
    private String barcode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Barcode that = (Barcode) o;
        return Objects.equals(this.barcode, that.barcode);
    }

    @Override
    public int hashCode() {
        return barcode.hashCode();
    }

    @Override
    public String toString() {
        return "Barcode=" + barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}

class Disk {
    private List<Track> tracks;
    private String number;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disk that = (Disk) o;
        return this.number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public String toString() {
        return "Disk=" + number;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

class Track {
    private String number;
    private String name;
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track that = (Track) o;
        return Objects.equals(this.number, that.number) &&
                this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return number.hashCode() ^ name.hashCode();
    }

    @Override
    public String toString() {
        return "Track=" + number + "_" + name;

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
