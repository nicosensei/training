package fr.nicosensei.training.ubeeko.xmlfs;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A node in the file system.
 *
 * @author nicolas
 *
 */
public interface XmlMockFileSystemNode {

    /**
     * Constant: path separator.
     */
    String PATH_SEPARATOR = "/";

    /**
     * The available node types.
     * @author nicolas
     *
     */
    public enum Type {
        /**
         * A tree (tree) node.
         */
        FOLDER,
        /**
         * A file (leaf) node.
         */
        FILE
    }

    /**
     * @return the name of the node.
     */
    String getName();

    /**
     * Sets the node name.
     * @param name the name to set.
     */
    void setName(String name);

    /**
     * @return the path of the node, its unique identifier.
     */
    String getAbsolutePath();

    /**
     * @return the parent path of the node.
     */
    String getParentPath();

    /**
     * @return the node type.
     */
    Type getType();

    /**
     * Returns an XML representation of the node.
     * @param doc the XML document.
     * @return a DOM Element
     */
    Element getAsXml(Document doc);

}
