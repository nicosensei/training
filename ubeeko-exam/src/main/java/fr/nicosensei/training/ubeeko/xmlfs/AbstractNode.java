package fr.nicosensei.training.ubeeko.xmlfs;

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
     * The creation time.
     */
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private long creationTime;

    /**
     * Constructor from parent node, name and type.
     * @param parentPath the parent node path
     * @param type the node type
     * @param name the node name
     */
    public AbstractNode(
            final String parentPath,
            final Type type,
            final String name) {
        super();
        this.parentPath = parentPath;
        this.path = parentPath + PATH_SEPARATOR + name;
        this.name = name;
        this.type = type;
        this.creationTime = System.currentTimeMillis();
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
     * @return the creation time (standard milliseconds since reference time).
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the creation time.
     * @param timeInMillis the time to set
     */
    protected void setCreationTime(long timeInMillis) {
        creationTime = timeInMillis;
    }

}
