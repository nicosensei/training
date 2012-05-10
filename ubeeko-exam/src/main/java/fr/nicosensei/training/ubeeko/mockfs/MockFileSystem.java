package fr.nicosensei.training.ubeeko.mockfs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.sleepycat.persist.StoreConfig;

import fr.nicosensei.training.ubeeko.AbstractBDB;
import fr.nicosensei.training.ubeeko.mockfs.MockFileSystemNode.Type;


/**
 * Mock file system implementation, backed up by a Berkeley DB.
 * @author nicolas
 *
 */
public class MockFileSystem extends AbstractBDB {

    /**
     * Constant, default max memory usage (percentage of JVM memory) to use for the BDB.
     * TODO this should be configurable
     */
    private static final int DEFAULT_MEM_CACHE_PERCENTAGE = 20;

    /**
     * Constant: date format expected in XML descriptors.
     */
    public static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyyMMdd'T'hhmm");

    /**
     * The class logger.
     */
    private final Logger log = Logger.getLogger(MockFileSystem.class);

    /**
     * Storage folder for the BDB storing the file system description.
     */
    private final String fsDbStorageFolder;

    /**
     * The store used to persist entities.
     */
    private EntityStore store;

    /**
     * Primary index, retrieves nodes by absolute path.
     */
    private PrimaryIndex<String, MockFileSystemNode> nodesByPath;

    /**
     * Secondary index, retrieves nodes by parent path.
     */
    private SecondaryIndex<String, String, MockFileSystemNode> nodesByParentPath;

    /**
     * Secondary index, retrieves nodes by name.
     */
    private SecondaryIndex<String, String, MockFileSystemNode> nodesByName;

    /**
     * Secondary index, retrieves nodes by type.
     */
    private SecondaryIndex<Integer, String, MockFileSystemNode> nodesByType;

    /**
     * Constructor from BDB storage folder.
     * @param fsDbStorageFolder the BDB storage folder
     */
    public MockFileSystem(final String fsDbStorageFolder) {
        this.fsDbStorageFolder = fsDbStorageFolder;
    }

    /**
     * Factory method. Parses the given XML descriptor to create the file system DB.
     * @param xmlFilePath path to the descriptor.
     * @param storageFolderPath the BDB storage path
     * @return the file system instance.
     * @throws DatabaseException if an error occurred during initialization.
     */
    public static MockFileSystem createFromXmlDescriptor(
            String xmlFilePath,
            String storageFolderPath) throws DatabaseException {
        MockFileSystem mfs = new MockFileSystem(storageFolderPath);
        MockFileSystemNode rootNode = mfs.init();

        Document doc = XmlUtils.parseXmlFile(xmlFilePath);
        mfs.recursiveLoad(doc.getDocumentElement(), rootNode);

        return mfs;
    }

    /**
     * Initializes the file system.
     * @throws DatabaseException if init failed.
     * @return the root node
     */
    public MockFileSystemNode init() throws DatabaseException {
        startEnvironment();
        nodesByPath = store.getPrimaryIndex(String.class, MockFileSystemNode.class);
        nodesByParentPath =
                store.getSecondaryIndex(nodesByPath, String.class, "parentPath");
        nodesByName =
                store.getSecondaryIndex(nodesByPath, String.class, "name");
        nodesByType =
                store.getSecondaryIndex(nodesByPath, Integer.class, "type");
        MockFileSystemNode rootNode =
                getNodeByPath(MockFileSystemNode.PATH_SEPARATOR);
        if (rootNode == null) {
            rootNode = MockFileSystemNode.newRoot();
            nodesByPath.putNoReturn(rootNode);
        }
        return rootNode;
    }

    /**
     * Adds a node to the file system, or updates it if already present.
     * @param node the node to add/update
     * @throws DatabaseException if the node is duplicated
     */
    public void addOrUpdateNode(MockFileSystemNode node) throws DatabaseException {
        nodesByPath.putNoReturn(node);
    }

    /**
     * Deletes a node, recursively deleting children.
     * @param node the node to delete
     * @throws DatabaseException if the node could not be deleted
     */
    public void deleteNode(MockFileSystemNode node) throws DatabaseException {
        Type type = node.getType();
        if (Type.ROOT.equals(type)) {
            return; // can't delete root, delete whole FS
        }

        if (Type.FOLDER.equals(type)) {
            Iterator<MockFileSystemNode> children = getChildrenNodes(node);
            while (children.hasNext()) {
                deleteNode(children.next());
            }
        }

        if (!nodesByPath.delete(node.getAbsolutePath())) {
            // TODO proper exception handling
            throw new RuntimeException("Failed to delete node " + node);
        }
    }

    /**
     * Attempts to find a node matching the given path.
     * @param path the path to match.
     * @return the matching node, or null if no match was found.
     * @throws DatabaseException TODO document
     */
    public MockFileSystemNode getNodeByPath(String path) throws DatabaseException {
        return nodesByPath.get(path);
    }

    /**
     * Fetches the direct children of the given node.
     * @param parent the parent node
     * @return the direct children of the given node.
     * @throws DatabaseException if the operation failed.
     */
    public Iterator<MockFileSystemNode> getChildrenNodes(MockFileSystemNode parent)
            throws DatabaseException {
        EntityCursor<MockFileSystemNode> cursor =
                nodesByParentPath.subIndex(parent.getAbsolutePath()).entities();
        List<MockFileSystemNode> children = new ArrayList<MockFileSystemNode>();
        try {
            for (MockFileSystemNode n : cursor) {
                children.add(n);
            }
        } finally {
            cursor.close();
        }
        return children.iterator();
    }

    /**
     * Initializes the file system.
     * @param destroy if true wipes the BDB storage.
     * @throws DatabaseException if close failed.
     */
    public void close(boolean destroy) throws DatabaseException {
        stopEnvironment();
        if (destroy) {
            removeStorageDir();
        }
    }

    @Override
    protected String getStorageFolderPath() {
        return fsDbStorageFolder;
    }

    @Override
    protected int getCachePercentage() {
        return DEFAULT_MEM_CACHE_PERCENTAGE;
    }

    @Override
    protected void closeStores() {
        if (store != null) {
            try {
                store.close();
            } catch (final DatabaseException e) {
                throw new RuntimeException(e); // TODO proper exception handling
            }
            if (log.isInfoEnabled()) {
                log.info("Closed entity store.");
            }
        }
    }

    @Override
    protected void initStores(Environment dbEnv) {
        try {
            StoreConfig storeCfg = new StoreConfig();
            List<String> dbNames = dbEnv.getDatabaseNames();
            boolean allowCreate = dbNames.isEmpty();
            storeCfg.setAllowCreate(allowCreate);

            store = new EntityStore(
                    dbEnv, MockFileSystem.class.getSimpleName(), storeCfg);

            if (log.isInfoEnabled()) {
                log.info("Initialized entity store (allowCreate=" + allowCreate + ").");
            }
        } catch (final DatabaseException e) {
            throw new RuntimeException(e); // TODO proper exception handling
        }
    }

    /**
     * Recursive initlization method.
     * @param root the element to start with.
     * @throws DatabaseException if BDB operation fails
     * @param rootNode the root FS node.
     */
    private void recursiveLoad(Element root, MockFileSystemNode rootNode)
            throws DatabaseException {
        List<Element> rootChildren = XmlUtils.getChildrenElements(root);
        for (Element e : rootChildren) {
            String tagName = e.getTagName();
            String nodeName = e.getAttribute("name");
            if ("tree".equals(tagName)) {
                MockFileSystemNode folder =
                        MockFileSystemNode.newFolder(rootNode, nodeName);
                addOrUpdateNode(folder);
                recursiveLoad(e, folder);
            } else if ("file".equals(tagName)) {
                MockFileSystemNode file =
                        MockFileSystemNode.newFile(rootNode, nodeName);

                file.setSize(Long.parseLong(e.getAttribute("size")));
                Date modifDate = null;
                try {
                    modifDate = DATE_FORMAT.parse(e.getAttribute("modif-date"));
                } catch (final ParseException e1) {
                    if (log.isDebugEnabled()) {
                        log.debug("Failed to parse date token '"
                                + e.getAttribute("modif-date") + "'");
                    }
                }
                file.setModificationDate(modifDate.getTime());
                addOrUpdateNode(file);
            }
        }
    }

}
