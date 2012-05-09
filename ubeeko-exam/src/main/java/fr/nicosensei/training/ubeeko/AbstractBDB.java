package fr.nicosensei.training.ubeeko;

import java.io.File;
import java.io.FileFilter;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;

/**
 * Abstract Berkeley DB storage.
 */
public abstract class AbstractBDB {

    /**
     * The class logger.
     */
    private final Logger log = Logger.getLogger(AbstractBDB.class);

    /**
     * A file filter used to detect if the storage folder contains an existing BDB.
     */
    private class BDBFileFilter implements FileFilter {

        /**
         * Accept method.
         * @param f the file to test for acceptance.
         * @return true if the file is accepted, false otherwise.
         */
        public boolean accept(File f) {
            String name = f.getName();
            return name.endsWith(".jdb") || name.endsWith(".lck");
        }

    }

    /**
     * The Berkeley DB JE environment.
     */
    private Environment dbEnvironment;

    /**
     * @return the path to the storage folder.
     */
    protected abstract String getStorageFolderPath();

    /**
     * @return the percentage of JVM memory to use for the cache.
     */
    protected abstract int getCachePercentage();

    /**
     * Closes the BDB.
     * @throws DatabaseException when the operation failed.
     */
    protected void stopEnvironment() throws DatabaseException {
        closeStores();
        if (dbEnvironment != null) {
            dbEnvironment.cleanLog();
            dbEnvironment.close();
            dbEnvironment = null;
        }
        if (log.isInfoEnabled()) {
            log.info("Closed BDB located in '" + getStorageFolderPath() + "'");
        }
    }

    /**
     * Dispose of any allocated resource before the environment is closed.
     */
    protected abstract void closeStores();

    /**
     * Initialize entity stores.
     * @param dbEnv the db environment.
     */
    protected abstract void initStores(Environment dbEnv);

    /**
     * Initializes the DB environment, by creating the DB if the storage
     * folder does not exist, and attempt to load the DB otherwise.
     * @throws DatabaseException when the operation failed
     * @throws EnvironmentLockedException if the environment is already in use
     */
    protected void startEnvironment() throws DatabaseException {

        if (dbEnvironment != null) {
            return; // already initialized
        }

        File storageFolder = new File(getStorageFolderPath());
        boolean createEnv = checkStorageFolder(storageFolder);

        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(createEnv);
        envConfig.setCachePercent(getCachePercentage());
        dbEnvironment = new Environment(storageFolder, envConfig);

        initStores(dbEnvironment);

        if (log.isInfoEnabled()) {
            log.info("Initialised BDB located in '" + getStorageFolderPath() + "'"
                + (createEnv ? " (created new environment)" : ""));
        }
    }

    /**
     * Resets the BDB, creating a new empty BDB.
     * @throws DatabaseException if the operation failed.
     */
    public void reset() throws DatabaseException {
        stopEnvironment();

        if (!removeStorageDir()) {
            throw new RuntimeException("Failed to delete storage folder ("
                    + getStorageFolderPath() + ")");
        }
        startEnvironment();
    }

    /**
     * Creates the folder if needed, and checks for access permissions.
     * @return true if the database has to be created, false if it already exists.
     * @param folder the folder to check.
     */
    private boolean checkStorageFolder(File folder) {
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new RuntimeException("Storage folder creation failed ("
                        + folder.getAbsolutePath() + ")");
            }
            return true;
        } else if (!folder.isDirectory()) {
            throw new RuntimeException("Storage folder is no a directory ("
                    + folder.getAbsolutePath() + ")");
        } else if (!folder.canRead() || !folder.canWrite()) {
            throw new RuntimeException("Storage folder has wrong access rights ("
                    + folder.getAbsolutePath() + ")");
        } else {
            return folder.listFiles(new BDBFileFilter()).length == 0;
        }

    }

    /**
     * Recursively removes the storage folder and its contents.
     * @return true if deletion succeeded, false otherwise.
     */
    protected boolean removeStorageDir() {
        File bdbDir = new File(getStorageFolderPath());
        if (!bdbDir.exists()) {
            return false;
        }
        for (File f : bdbDir.listFiles()) {
            if (!f.delete()) {
                f.deleteOnExit();
            }
        }
        return bdbDir.delete();
    }

}
