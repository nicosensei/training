/**
 *
 */
package fr.nicosensei.training.ubeeko.xmlfs;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;
XmlMockFileSystemNode
import com.sleepycat.je.DatabaseException;

import fr.nicosensei.training.ubeeko.xmlfs.XmlMockFileSystemNode.Type;

/**
 * @author ngiraud
 *
 */
public class XmlMockFileSystemTest extends TestCase {

    /**
     * Test basic node objects.
     */
    @Test
    public final void testBasicNodes() {
        try {
            XmlMockFileSystemNode.newFolder(null, "");
            XmlMockFileSystemNode.newFolder(null, "foo");
            XmlMockFileSystemNode.newFile(null, "name");
        } catch (final Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        XmlMockFileSystemNode root = XmlMockFileSystemNode.newFolder("", "");
        assertEquals(XmlMockFileSystemNode.PATH_SEPARATOR, root.getAbsolutePath());
        assertEquals("", root.getParentPath());
        assertEquals("", root.getName());

        long arbitraryCreationTime = 123456789L;
        XmlMockFileSystemNode folderA = new XmlMockFileSystemNode(root, "folderA");
        folderA.setCreationTime(arbitraryCreationTime);
        assertEquals(arbitraryCreationTime, folderA.getCreationTime());
        assertEquals("/folderA", folderA.getAbsolutePath());
        assertEquals("/", folderA.getParentPath());
        assertEquals("folderA", folderA.getName());

        XmlMockFileSystemNode folderB = new XmlMockFileSystemNode(root, "folderB");
        folderB.setCreationTime(arbitraryCreationTime);
        assertEquals(arbitraryCreationTime, folderB.getCreationTime());
        assertEquals("/folderB", folderB.getAbsolutePath());
        assertEquals("/", folderB.getParentPath());
        assertEquals("folderB", folderB.getName());


        XmlMockFileSystemNode folderC = new XmlMockFileSystemNode(folderA, "folderC");
        folderC.setCreationTime(arbitraryCreationTime);
        assertEquals(arbitraryCreationTime, folderC.getCreationTime());
        assertEquals("/folderA/folderC", folderC.getAbsolutePath());
        assertEquals("/folderA", folderC.getParentPath());
        assertEquals("folderC", folderC.getName());

        XmlMockFileSystemNode foo1Txt = new XmlMockFileSystemNode(folderA, "foo1.txt");
        foo1Txt.setCreationTime(arbitraryCreationTime);
        assertEquals(arbitraryCreationTime, foo1Txt.getCreationTime());
        assertEquals("/folderA/foo1.txt", foo1Txt.getAbsolutePath());
        assertEquals("/folderA", foo1Txt.getParentPath());
        assertEquals("foo1.txt", foo1Txt.getName());
        assertEquals(0, foo1Txt.getModificationTime());
        assertEquals(0, foo1Txt.getSize());

        XmlMockFileSystemNode foo2Txt = new XmlMockFileSystemNode(folderC, "foo2.txt");
        foo2Txt.setCreationTime(arbitraryCreationTime);
        long time = 987654321L;
        foo2Txt.setModificationDate(time);
        foo2Txt.setSize(4096);
        assertEquals(arbitraryCreationTime, foo2Txt.getCreationTime());
        assertEquals("/folderA/folderC/foo2.txt", foo2Txt.getAbsolutePath());
        assertEquals("/folderA/folderC", foo2Txt.getParentPath());
        assertEquals("foo2.txt", foo2Txt.getName());
        assertEquals(time, foo2Txt.getModificationTime());
        assertEquals(4096, foo2Txt.getSize());

    }

    /**
     * Test a basic file system structure.
     * @throws DatabaseException
     */
    @Test
    public final void testBasicFs() throws DatabaseException {
        String folder = System.getProperty("java.io.tmpdir")
                + File.separator + "mfs1";
        XmlMockFileSystem mfs = new XmlMockFileSystem(folder);
        try {
            XmlMockFileSystemNode root = mfs.init();

        } finally {
            mfs.close(true);
        }
    }

}
