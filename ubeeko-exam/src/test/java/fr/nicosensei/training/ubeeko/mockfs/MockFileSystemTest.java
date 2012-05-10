package fr.nicosensei.training.ubeeko.mockfs;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Test;

import com.sleepycat.je.DatabaseException;

import fr.nicosensei.training.ubeeko.BaseTest;
import fr.nicosensei.training.ubeeko.FileSystemDiff;
import fr.nicosensei.training.ubeeko.mockfs.MockFileSystem;
import fr.nicosensei.training.ubeeko.mockfs.MockFileSystemNode;
import fr.nicosensei.training.ubeeko.mockfs.MockFileSystemNode.Type;

/**
 * @author ngiraud
 *
 */
public class MockFileSystemTest extends BaseTest {

    /**
     * Test basic node objects.
     */
    @Test
    public final void testBasicNodes() {
        try {
            MockFileSystemNode.newFolder(null, "");
            MockFileSystemNode.newFolder(null, "foo");
            MockFileSystemNode.newFile(null, "name");
        } catch (final Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        MockFileSystemNode root = MockFileSystemNode.newRoot();
        assertEquals(MockFileSystemNode.PATH_SEPARATOR, root.getAbsolutePath());
        assertEquals("", root.getParentPath());
        assertEquals("", root.getName());

        long arbitraryCreationTime = 123456789L;
        MockFileSystemNode folderA = MockFileSystemNode.newFolder(root, "folderA");
        folderA.setCreationTime(arbitraryCreationTime);
        assertEquals(arbitraryCreationTime, folderA.getCreationTime());
        assertEquals("/folderA", folderA.getAbsolutePath());
        assertEquals("/", folderA.getParentPath());
        assertEquals("folderA", folderA.getName());

        MockFileSystemNode folderB = MockFileSystemNode.newFolder(root, "folderB");
        folderB.setCreationTime(arbitraryCreationTime);
        assertEquals(arbitraryCreationTime, folderB.getCreationTime());
        assertEquals("/folderB", folderB.getAbsolutePath());
        assertEquals("/", folderB.getParentPath());
        assertEquals("folderB", folderB.getName());


        MockFileSystemNode folderC =
                MockFileSystemNode.newFolder(folderA, "folderC");
        folderC.setCreationTime(arbitraryCreationTime);
        assertEquals(arbitraryCreationTime, folderC.getCreationTime());
        assertEquals("/folderA/folderC", folderC.getAbsolutePath());
        assertEquals("/folderA", folderC.getParentPath());
        assertEquals("folderC", folderC.getName());

        MockFileSystemNode foo1Txt =
                MockFileSystemNode.newFile(folderA, "foo1.txt");
        foo1Txt.setCreationTime(arbitraryCreationTime);
        assertEquals(arbitraryCreationTime, foo1Txt.getCreationTime());
        assertEquals("/folderA/foo1.txt", foo1Txt.getAbsolutePath());
        assertEquals("/folderA", foo1Txt.getParentPath());
        assertEquals("foo1.txt", foo1Txt.getName());
        assertEquals(0, foo1Txt.getModificationTime());
        assertEquals(0, foo1Txt.getSize());

        MockFileSystemNode foo2Txt =
                MockFileSystemNode.newFile(folderC, "foo2.txt");
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
        String folder = TEMP_DIR + File.separator + "mfs1";
        MockFileSystem mfs = new MockFileSystem(folder);
        long arbitraryCreationTime = 123456789L;
        long arbitraryTime = 987654321L;
        try {
            MockFileSystemNode root = mfs.init();

            MockFileSystemNode folderA =
                    MockFileSystemNode.newFolder(root, "folderA");
            folderA.setCreationTime(arbitraryCreationTime);
            mfs.addOrUpdateNode(folderA);

            MockFileSystemNode folderB =
                    MockFileSystemNode.newFolder(root, "folderB");
            folderB.setCreationTime(arbitraryCreationTime);
            mfs.addOrUpdateNode(folderB);

            MockFileSystemNode folderC =
                    MockFileSystemNode.newFolder(folderA, "folderC");
            folderC.setCreationTime(arbitraryCreationTime);
            mfs.addOrUpdateNode(folderC);

            MockFileSystemNode foo1Txt =
                    MockFileSystemNode.newFile(folderA, "foo1.txt");
            foo1Txt.setCreationTime(arbitraryCreationTime);
            mfs.addOrUpdateNode(foo1Txt);

            MockFileSystemNode foo2Txt =
                    MockFileSystemNode.newFile(folderC, "foo2.txt");
            foo2Txt.setCreationTime(arbitraryCreationTime);
            foo2Txt.setModificationDate(arbitraryTime);
            foo2Txt.setSize(4096);
            mfs.addOrUpdateNode(foo2Txt);

        } finally {
            mfs.close(false);
            assertTrue(new File(folder).exists());
            assertTrue(new File(folder).isDirectory());
        }

        try {
            mfs.init();

            MockFileSystemNode root =
                    mfs.getNodeByPath(MockFileSystemNode.PATH_SEPARATOR);
            assertNotNull(root);
            assertEquals(MockFileSystemNode.PATH_SEPARATOR, root.getAbsolutePath());
            assertEquals("", root.getName());
            assertEquals("", root.getParentPath());

            assertNull(mfs.getNodeByPath("/foo"));

            MockFileSystemNode folderA = mfs.getNodeByPath("/folderA");
            assertNotNull(folderA);
            assertEquals(arbitraryCreationTime, folderA.getCreationTime());
            assertEquals("/folderA", folderA.getAbsolutePath());
            assertEquals("/", folderA.getParentPath());
            assertEquals("folderA", folderA.getName());

            MockFileSystemNode folderB = mfs.getNodeByPath("/folderB");
            assertNotNull(folderB);
            assertEquals(arbitraryCreationTime, folderB.getCreationTime());
            assertEquals("/folderB", folderB.getAbsolutePath());
            assertEquals("/", folderB.getParentPath());
            assertEquals("folderB", folderB.getName());

            MockFileSystemNode folderC = mfs.getNodeByPath("/folderA/folderC");
            assertNotNull(folderC);
            assertEquals(arbitraryCreationTime, folderC.getCreationTime());
            assertEquals("/folderA/folderC", folderC.getAbsolutePath());
            assertEquals("/folderA", folderC.getParentPath());
            assertEquals("folderC", folderC.getName());

            MockFileSystemNode foo1Txt = mfs.getNodeByPath("/folderA/foo1.txt");
            assertNotNull(foo1Txt);
            assertEquals(arbitraryCreationTime, foo1Txt.getCreationTime());
            assertEquals("/folderA/foo1.txt", foo1Txt.getAbsolutePath());
            assertEquals("/folderA", foo1Txt.getParentPath());
            assertEquals("foo1.txt", foo1Txt.getName());
            assertEquals(0, foo1Txt.getModificationTime());
            assertEquals(0, foo1Txt.getSize());

            MockFileSystemNode foo2Txt =
                    mfs.getNodeByPath("/folderA/folderC/foo2.txt");
            assertNotNull(foo2Txt);
            assertEquals(arbitraryCreationTime, foo2Txt.getCreationTime());
            assertEquals("/folderA/folderC/foo2.txt", foo2Txt.getAbsolutePath());
            assertEquals("/folderA/folderC", foo2Txt.getParentPath());
            assertEquals("foo2.txt", foo2Txt.getName());
            assertEquals(arbitraryTime, foo2Txt.getModificationTime());
            assertEquals(4096, foo2Txt.getSize());

            Iterator<MockFileSystemNode> children = mfs.getChildrenNodes(root);
            int count = 0;
            while (children.hasNext()) {
                children.next();
                count++;
            }
            assertEquals(2, count);

        } finally {
            mfs.close(true);
            assertFalse(new File(folder).exists());
        }
    }

    /**
     * Test a basic file system structure, loaded from an XML file.
     * @throws DatabaseException if BDB thropws exception
     */
    @Test
    public final void testBasicFsFromXMLFile() throws DatabaseException {

        String xmlFile = RESOURCE_DIR + File.separator + "tree1.xml";

        String folder = TEMP_DIR + File.separator + "mfs1";
        MockFileSystem mfs =
                MockFileSystem.createFromXmlDescriptor(xmlFile, folder);
        try {
            MockFileSystemNode rootNode =
                    mfs.getNodeByPath(MockFileSystemNode.PATH_SEPARATOR);
            assertNotNull(rootNode);

            assertNotNull(mfs.getNodeByPath("/home"));
            assertNotNull(mfs.getNodeByPath("/home/joe"));

            MockFileSystemNode atxt = mfs.getNodeByPath("/home/joe/a.txt");
            assertNotNull(atxt);
            assertEquals(Type.FILE, atxt.getType());
            assertEquals("/home/joe/a.txt", atxt.getAbsolutePath());
            assertEquals("/home/joe", atxt.getParentPath());
            assertEquals("a.txt", atxt.getName());
            assertEquals(5032, atxt.getSize());
            assertEquals(
                    "20120102T1030",
                    MockFileSystem.DATE_FORMAT.format(
                            new Date(atxt.getModificationTime())));

        } finally {
            mfs.close(true);
        }
    }

}
