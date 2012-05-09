package fr.nicosensei.training.ubeeko.xmlfs;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * A file system node. *
 * It is defined a a Berkeley DB entity with useful indices.
 *
 * @author nicolas
 *
 */
@Entity
public final class XmlMockFileSystemNode {

    /**
     * Constant: path separator.
     */
    public static final String PATH_SEPARATOR = "/";

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
     * Should be an ordinal of {@link Type}.
     */
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private int type;

    /**
     * The creation time.
     */
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private long creationTime;

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
     * Default constructor, required by BDB.
     */
    private XmlMockFileSystemNode() {

    }

    /**
     * Constructor from parent node, name and type.
     * @param parentPath the parent node path
     * @param type the node type
     * @param name the node name
     */
    private XmlMockFileSystemNode(
            final String parentPath,
            final Type type,
            final String name) {
        super();
        this.parentPath = parentPath;
        // If parent is FS root, don't add separator twice
        this.path = (PATH_SEPARATOR.equals(parentPath) ? "" : parentPath)
                + PATH_SEPARATOR + name;
        this.name = name;
        this.type = type.ordinal();
        this.creationTime = System.currentTimeMillis();
    }

    /**
     * Creates a folder.
     * @param parentPath parent path
     * @param name name
     * @return the proper instance.
     */
    public static XmlMockFileSystemNode newFolder(String parentPath, String name) {
        return new XmlMockFileSystemNode(parentPath, Type.FOLDER, name);
    }

    /**
     * Creates a file.
     * @param parentPath parent path
     * @param name name
     * @return the proper instance.
     */
    public static XmlMockFileSystemNode newFile(String parentPath, String name) {
        return new XmlMockFileSystemNode(parentPath, Type.FILE, name);
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
        return Type.values()[type];
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

    /**
     * @return the modificationDate
     */
    public long getModificationTime() {
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
