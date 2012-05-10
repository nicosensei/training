/**
 *
 */
package fr.nicosensei.training.ubeeko.mockfs;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML utilities.
 *
 * @author ngiraud
 *
 */
public final class XmlUtils {

    /**
     * Mute default constructor.
     */
    private XmlUtils() {

    }

    /**
     *
     * @return a brand new {@link Document} instance.
     */
    public static Document newDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e); // TODO proper exception handling
        }
        return builder.newDocument();
    }

    /**
     * Writes a {@link Document} as text to the given {@link Writer}.
     * @param doc the document to output
     * @param out the writer to output to
     */
    public static void writeDocument(Document doc, Writer out) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (final TransformerConfigurationException e) {
            throw new RuntimeException(e); // TODO proper exception handling
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(out);

        try {
            transformer.transform(source, result);
        } catch (final TransformerException e) {
            throw new RuntimeException(e); // TODO proper exception handling
        }
    }

    /**
     * Makes a DOM document out of an XML file.
     * @param pathToFile path to the XML file.
     * @return a DOM document
     */
    public static Document parseXmlFile(String pathToFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(pathToFile);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e); // TODO proper exception handling
        } catch (final SAXException e) {
            throw new RuntimeException(e); // TODO proper exception handling
        } catch (final IOException e) {
            throw new RuntimeException(e); // TODO proper exception handling
        }
    }

    /**
     * Fetches children {@link Element}s of the given node.
     * @param e the element to scan
     * @return children {@link Element}s of the given node (non-null list).
     */
    public static List<Element> getChildrenElements(Element e) {
        NodeList nodes = e.getChildNodes();
        List<Element> children = new ArrayList<Element>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (n instanceof Element) {
                children.add((Element) n);
            }
        }
        return children;
    }

}
