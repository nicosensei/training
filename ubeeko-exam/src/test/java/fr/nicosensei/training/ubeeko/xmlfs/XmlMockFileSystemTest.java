/**
 *
 */
package fr.nicosensei.training.ubeeko.xmlfs;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author ngiraud
 *
 */
public class XmlMockFileSystemTest extends TestCase {

    /**
     * Test a basic file system structure.
     */
    @Test
    public final void testBasicNodes() {
        try {
            new FolderNode(null, "");
            new FolderNode(null, "foo");
            new FileNode(null, "name");
        } catch (final Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        FolderNode root = new FolderNode();
        assertEquals(XmlMockFileSystemNode.PATH_SEPARATOR, root.getAbsolutePath());
        assertEquals("", root.getParentPath());
        assertEquals("", root.getName());

        long arbitraryCreationTime = 123456789L;
        FolderNode folderA = new FolderNode(root, "folderA");
        folderA.setCreationTime(arbitraryCreationTime);
        assertEquals(arbitraryCreationTime, folderA.getCreationTime());
        assertEquals("/folderA", folderA.getAbsolutePath());
        assertEquals("/", folderA.getParentPath());
        assertEquals("folderA", folderA.getName());

        FolderNode folderB = new FolderNode(root, "folderB");
        folderB.setCreationTime(arbitraryCreationTime);
        FolderNode folderC = new FolderNode(folderA, "folderB");
        folderC.setCreationTime(arbitraryCreationTime);
        FileNode foo1Txt = new FileNode(folderA, "foo1.txt");
        foo1Txt.setCreationTime(arbitraryCreationTime);
        FileNode foo2Txt = new FileNode(folderA, "foo2.txt");
        foo2Txt.setCreationTime(arbitraryCreationTime);
        FileNode foo3Txt = new FileNode(folderC, "foo1.txt");
        foo3Txt.setCreationTime(arbitraryCreationTime);

    }

}
;