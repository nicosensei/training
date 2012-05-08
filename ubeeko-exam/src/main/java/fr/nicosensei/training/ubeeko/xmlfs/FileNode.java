package fr.nicosensei.training.ubeeko.xmlfs;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * A file node.
 *
 * @author nicolas
 *
 */
@Entity
public class FileNode extends AbstractNode {

    /**
     * The modification date.
     */
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private long modificationDate;

    /**
     * File size in bytes.
     */
    private long size;

    /**
     * Constructor from parent and name.
     * @param parent the parent node
     * @param name the node name
     */
    public FileNode(final XmlMockFileSystemNode parent, final String name) {
        super(parent, Type.FILE, name);
    }

    /**
     * @return the modificationDate
     */
    public long getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate(long modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public Element getAsXml(Document doc) {
        Element e = doc.createElement("file");
        e.setAttribute("name", getName());
        e.setAttribute("modificationDate", "" + modificationDate);
        e.setAttribute("size", "" + size);
        return e;
    }

}
