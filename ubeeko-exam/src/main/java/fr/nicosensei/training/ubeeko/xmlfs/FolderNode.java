package fr.nicosensei.training.ubeeko.xmlfs;

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
     * Builds a root folder node (no parent).
     */
    public FolderNode() {
        super("", Type.FOLDER, "");
    }

    /**
     * Constructor from parent and name.
     * @param parent the parent node
     * @param name the node name
     */
    public FolderNode(final XmlMockFileSystemNode parent, final String name) {
        super(parent.getAbsolutePath(), Type.FOLDER, name);
    }

}
