package fr.nicosensei.training.ubeeko.xmlfs;

import java.util.List;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

import fr.nicosensei.training.ubeeko.AbstractBDB;


/**
 * Mock file system implementation, backed up by a Berkeley DB.
 * @author nicolas
 *
 */
public class XmlMockFileSystem extends AbstractBDB {

    /**
     * Constant, default max memory usage (percentage of JVM memory) to use for the BDB.
     * TODO this should be configurable
     */
    private static final int DEFAULT_MEM_CACHE_PERCENTAGE = 20;

    /**
     * The class logger.
     */
    private final Logger log = Logger.getLogger(XmlMockFileSystem.class);

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
    private PrimaryIndex<String, XmlMockFileSystemNode> nodesByPath;

    /**
     * Constructor from BDB storage folder.
     * @param fsDbStorageFolder the BDB storage folder
     */
    protected XmlMockFileSystem(final String fsDbStorageFolder) {
        this.fsDbStorageFolder = fsDbStorageFolder;
    }

    /**
     * Factory method. Parses the given XML descriptor to create the file system DB.
     * @param descriptorPath path to the descriptor.
     * @return the file system instance.
     */
    public static XmlMockFileSystem createFromXmlDescriptor(String descriptorPath) {
        return null; // TODO implement
    }

    /**
     * Initializes the file system.
     * @throws DatabaseException if init failed.
     * @return the root node
     */
    public FolderNode init() throws DatabaseException {
        startEnvironment();

        nodesByPath = store.getPrimaryIndex(String.class, XmlMockFileSystemNode.class);

        FolderNode rootNode = new FolderNode();
        nodesByPath.putNoReturn(rootNode);

        return rootNode;

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
                    dbEnv, XmlMockFileSystem.class.getSimpleName(), storeCfg);

            if (log.isInfoEnabled()) {
                log.info("Initialized entity store (allowCreate=" + allowCreate + ").");
            }
        } catch (final DatabaseException e) {
            throw new RuntimeException(e); // TODO proper exception handling
        }
    }

}
