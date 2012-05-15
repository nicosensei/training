/**
 *
 */
package fr.nicosensei.training.ubeeko;

import java.io.File;

import junit.framework.TestCase;

/**
 * Base test class.
 *
 * @author ngiraud
 *
 */
public abstract class BaseTest extends TestCase {

    /**
     * Temporary directory.
     */
    protected static final String TEMP_DIR  = System.getProperty("java.io.tmpdir");

    /**
     * Resources directory.
     */
    protected static final String RESOURCE_DIR  =
            new File("").getAbsolutePath() + File.separator
            + "src" + File.separator
            + "test" + File.separator
            + "resources" + File.separator
            + BaseTest.class.getPackage().getName()
            .replaceAll("\\.", File.separator);

}
