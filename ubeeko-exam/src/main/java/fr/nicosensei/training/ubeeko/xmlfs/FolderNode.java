package fr.nicosensei.training.ubeeko.xmlfs;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sleepycat.persist.model.Entity;

/**
 * A folder node.
 *
 * @author nicolas
 *
 */
@Entity
public class FolderNode extends AbstractNode {

    /**
     * Constructor from parent and name.
     * @param parent the parent node
     * @param name the node name
     */
    public FolderNode(final XmlMockFileSystemNode parent, final String name) {
        super(parent, Type.FOLDER, name);
    }

    @Override
    public Element getAsXml(Document doc) {
        Element e = doc.createElement("file");
        e.setAttribute("name", getName());
        return e;
    }

}
