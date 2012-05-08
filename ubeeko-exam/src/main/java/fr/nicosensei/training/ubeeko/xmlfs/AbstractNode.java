package fr.nicosensei.training.ubeeko.xmlfs;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * Base abstract class for nodes.
 *
 * It is defined a a Berkeley DB entity with useful indices.
 *
 * @author nicolas
 *
 */
@Entity
abstract class AbstractNode implements XmlMockFileSystemNode {

    /**
     * The node path and unique identifier.
     */
    @PrimaryKey
    private String path;

    /**
     * The parent node path.
     */
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String parentPath;

    /**
     * The node name.
     */
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String name;

    /**
     * The node type.
     */
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private final Type type;

    /**
     * Constructor from parent node, name and type.
     * @param parent the parent node
     * @param type the node type
     * @param name the node name
     */
    public AbstractNode(
            final XmlMockFileSystemNode parent,
            final Type type,
            final String name) {
        super();
        this.parentPath = parent.getAbsolutePath();
        this.path = parentPath + PATH_SEPARATOR + name;
        this.name = name;
        this.type = type;
    }

    /**
     * @return the node name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the node name.
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
        this.path = parentPath + PATH_SEPARATOR + name;
    }

    /**
     * @return the path of the node, its unique identifier.
     */
    public String getAbsolutePath() {
        return path;
    }

    /**
     * @return the parent path of the node.
     */
    public String getParentPath() {
        return parentPath;
    }

    /**
     * @return the node type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns an XML representation of the node.
     * @param doc the XML document.
     * @return a DOM Element
     */
    public abstract Element getAsXml(Document doc);

}
