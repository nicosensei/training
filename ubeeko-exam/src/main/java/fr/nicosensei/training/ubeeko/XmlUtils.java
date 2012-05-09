/**
 *
 */
package fr.nicosensei.training.ubeeko;

import java.io.Writer;

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

/**
 * XML utilities.
 *
 * @author ngiraud
 *
 */
public class XmlUtils {

    /**
     * Mute default constructor.
     */
    private XmlUtils() {

    }

    /**
     *
     * @return a brand new {@link Document} instance.
     */
    public static final Document newDocument() {
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
    public static final void writeDocument(Document doc, Writer out) {
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

}
