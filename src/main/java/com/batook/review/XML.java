package com.batook.review;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

class DomReader {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        // Создается построитель документа
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
                                                                .newDocumentBuilder();
        // Создается дерево DOM документа из файла
        Document document = documentBuilder.parse("BookCatalogue.xml");
        // Получаем корневой элемент
        Node root = document.getDocumentElement();
        System.out.println("List of books:");
        // Просматриваем все подэлементы корневого - т.е. книги
        NodeList books = root.getChildNodes();
        for (int i = 0; i < books.getLength(); i++) {
            Node book = books.item(i);
            // Если нода не текст, то это книга - заходим внутрь
            if (book.getNodeType() != Node.TEXT_NODE) {
                NodeList bookProps = book.getChildNodes();
                for (int j = 0; j < bookProps.getLength(); j++) {
                    Node bookProp = bookProps.item(j);
                    // Если нода не текст, то это один из параметров книги - печатаем
                    if (bookProp.getNodeType() != Node.TEXT_NODE) {
                        System.out.println(bookProp.getNodeName() + ":" + bookProp.getChildNodes()
                                                                                  .item(0)
                                                                                  .getTextContent());
                    }
                }
            }
        }
    }
}

class DomWriter {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        // Создается построитель документа
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
                                                                .newDocumentBuilder();
        // Создается дерево DOM
        Document document = documentBuilder.newDocument();
        // создаем корневой элемент
        Element root = document.createElement("BookCatalogue");
        document.appendChild(root);
        // <Book>
        Element book = document.createElement("Book");
        // <Title>
        Element title = document.createElement("Title");
        title.setTextContent("Incredible book about Java");
        // <Cost>
        Element cost = document.createElement("Cost");
        cost.setTextContent("499");
        cost.setAttribute("currency", "RUB");
        book.appendChild(title);
        book.appendChild(cost);
        root.appendChild(book);
        // Записываем XML в файл
        writeDocument(document);
    }

    private static void writeDocument(Document document) throws TransformerFactoryConfigurationError {
        try {
            Transformer tr = TransformerFactory.newInstance()
                                               .newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream("other1.xml"));
            tr.transform(source, result);
        } catch (TransformerException | IOException e) {
            e.printStackTrace(System.out);
        }
    }
}

class SaxReader {
    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException {
        SAXParser saxParser = SAXParserFactory.newInstance()
                                              .newSAXParser();

        // Здесь мы определили анонимный класс, расширяющий класс DefaultHandler
        DefaultHandler handler = new DefaultHandler() {
            // Поле для указания, что тэг NAME начался
            boolean name_tag = false;

            // Метод вызывается когда SAXParser "натыкается" на начало тэга
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                // Если тэг имеет имя NAME, то мы этот момент отмечаем - начался тэг NAME
                if (qName.equalsIgnoreCase("NAME")) {
                    name_tag = true;
                }
            }

            // Метод вызывается когда SAXParser считывает текст между тэгами
            @Override
            public void characters(char ch[], int start, int length) throws SAXException {
                // Если перед этим мы отметили, что имя тэга NAME - значит нам надо текст использовать.
                if (name_tag) {
                    System.out.println("Name: " + new String(ch, start, length));
                    name_tag = false;
                }
            }
        };
        saxParser.parse("Phonebook.xml", handler);
    }
}

class StaxReader {
    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        final String fileName = "BookCatalogue.xml";
        XMLStreamReader reader = XMLInputFactory.newInstance()
                                                .createXMLStreamReader(fileName, new FileInputStream(fileName));
        while (reader.hasNext()) {
            reader.next();
            if (reader.isStartElement()) {
                System.out.println(reader.getLocalName());
            } else if (reader.isEndElement()) {
                System.out.println("/" + reader.getLocalName());
            } else if (reader.hasText() && reader.getText()
                                                 .trim()
                                                 .length() > 0) {
                System.out.println("   " + reader.getText());
            }
        }
    }
}

class StaxWriter {
    public static void main(String[] args) throws XMLStreamException, IOException {
        XMLStreamWriter writer = XMLOutputFactory.newInstance()
                                                 .createXMLStreamWriter(new FileWriter("result.xml"));
        // Открываем XML-документ и Пишем корневой элемент BookCatalogue
        writer.writeStartDocument("1.0");
        writer.writeStartElement("BookCatalogue");
        for (int i = 0; i < 5; i++) {
            // Book
            writer.writeStartElement("Book");
            // Title
            writer.writeStartElement("Title");
            writer.writeCharacters("Book #" + i);
            writer.writeEndElement();
            // Cost
            writer.writeStartElement("Cost");
            writer.writeAttribute("currency", "USD");
            writer.writeCharacters("10");
            writer.writeEndElement();
            // Закрываем тэг Book
            writer.writeEndElement();
        }
        // Закрываем корневой элемент
        writer.writeEndElement();
        // Закрываем XML-документ
        writer.writeEndDocument();
        writer.flush();
    }
}

class XsdValidator {
    public static void main(String[] args) throws IOException, SAXException {
        boolean answer = validateXMLSchema("Message.xsd", "Message.xml");
        System.out.println("Result:" + answer);
    }

    public static boolean validateXMLSchema(String xsdPath, String xmlPath) throws SAXException, IOException {
        // Загрузить схему из XSD
        Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                                     .newSchema(new File(xsdPath));
        // Создать валидатор
        Validator validator = schema.newValidator();
        // Запусить проверку
        validator.validate(new StreamSource(new File(xmlPath)));
        return true;
    }
}

class XslConverter {
    public static void main(String[] arg) throws Exception {
        XslConverter c = new XslConverter();
        final String xml = "BookCatalogue.xml";
        final String xsl = "BookCatalogue.xsl";
        String result = c.xmlToString(xml, xsl);
        System.out.println(result);
        c.xmlToFile(xml, xsl);
    }

    public String xmlToString(String xmlFile, String xslFile) throws Exception {
        // Создать источник для транcформации из потоков
        StreamSource xmlSource = new StreamSource(new FileInputStream(xmlFile));
        StreamSource xslSource = new StreamSource(new FileInputStream(xslFile));

        // Создать байтовый поток для результата
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // Создать приемноик для результатат из байтового потока
        StreamResult xmlOutput = new StreamResult(bos);
        // Создать трансформатор и выполнить трансформацию
        Transformer transformer = TransformerFactory.newInstance()
                                                    .newTransformer(xslSource);
        transformer.transform(xmlSource, xmlOutput);
        // вернуть результат в виде строки
        return bos.toString();
    }

    public void xmlToFile(String xmlFile, String xslFile) throws Exception {
        // Создать источник для транформации из потоков
        StreamSource xmlSource = new StreamSource(new FileInputStream(xmlFile));
        StreamSource xslSource = new StreamSource(new FileInputStream(xslFile));

        StreamResult result = new StreamResult(new FileOutputStream("result2.html"));
        Transformer transformer = TransformerFactory.newInstance()
                                                    .newTransformer(xslSource);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(xmlSource, result);
    }
}