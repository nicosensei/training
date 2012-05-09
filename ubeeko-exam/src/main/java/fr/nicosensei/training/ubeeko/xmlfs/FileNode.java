package fr.nicosensei.training.ubeeko.xmlfs;

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
    private long modificationTime;

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
        super(parent.getAbsolutePath(), Type.FILE, name);
    }

    /**
     * @return the modificationDate
     */
    public long getModificationDate() {
        return modificationTime;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate(long modificationDate) {
        this.modificationTime = modificationDate;
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

}
