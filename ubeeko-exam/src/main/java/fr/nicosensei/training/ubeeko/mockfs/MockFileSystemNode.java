package fr.nicosensei.training.ubeeko.mockfs;

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
public final class MockFileSystemNode {

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
         * A root (tree) node.
         */
        ROOT,
        /**
         * A folder (tree) node.
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
    private long creationTime;

    /**
     * The modification date.
     */
    private long modificationTime;

    /**
     * File size in bytes.
     */
    private long size;


    /**
     * Default constructor, required by BDB.
     * Builds the root node.
     */
    private MockFileSystemNode() {
        this.parentPath = "";
        this.name = "";
        this.path = PATH_SEPARATOR;
        this.type = Type.ROOT.ordinal();
    }

    /**
     * Constructor from parent node, name and type.
     * @param parent the parent node
     * @param type the node type
     * @param name the node name
     */
    private MockFileSystemNode(
            final MockFileSystemNode parent,
            final Type type,
            final String name) {

        if (Type.ROOT.equals(parent.getType())) {
            // Direct child to root node
            this.path = PATH_SEPARATOR + name;
        } else {
            this.path = parent.getAbsolutePath() + PATH_SEPARATOR + name;
        }
        this.name = name;
        this.parentPath = parent.getAbsolutePath();
        this.type = type.ordinal();
        this.creationTime = System.currentTimeMillis();
    }

    /**
     * @return a new root node instance
     */
    public static MockFileSystemNode newRoot() {
        return new MockFileSystemNode();
    }

    /**
     * Creates a folder.
     * @param parent parent node
     * @param name name
     * @return the proper instance.
     */
    public static MockFileSystemNode newFolder(
            MockFileSystemNode parent,
            String name) {
        return new MockFileSystemNode(parent, Type.FOLDER, name);
    }

    /**
     * Creates a file.
     * @param parent parent node
     * @param name name
     * @return the proper instance.
     */
    public static MockFileSystemNode newFile(
            MockFileSystemNode parent,
            String name) {
        return new MockFileSystemNode(parent, Type.FILE, name);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MockFileSystemNode other = (MockFileSystemNode) obj;
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MockFileSystemNode [path=" + path + ", parentPath="
                + parentPath + ", name=" + name + ", type=" + type
                + ", creationTime=" + creationTime + ", modificationTime="
                + modificationTime + ", size=" + size + "]";
    }

}
