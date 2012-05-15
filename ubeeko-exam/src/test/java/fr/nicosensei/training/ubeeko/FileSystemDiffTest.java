/**
 *
 */
package fr.nicosensei.training.ubeeko;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import com.sleepycat.je.DatabaseException;

import fr.nicosensei.training.ubeeko.mockfs.MockFileSystem;
import fr.nicosensei.training.ubeeko.mockfs.MockFileSystemNode;

/**
 * Tests the {@link FileSystemDiff} class.
 *
 * @author ngiraud
 *
 */
public class FileSystemDiffTest extends BaseTest {

    /**
     * Test the file addition use case.
     * @throws DatabaseException if BDB operation failed.
     */
    @Test
    public void testFileAdded() throws DatabaseException {

        MockFileSystem srcFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree2.xml",
                TEMP_DIR + File.separator + "srcmfs");

        MockFileSystem refFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "refmfs");

        try {
            FileSystemDiff diff = FileSystemDiff.diff(srcFs, refFs);

            assertEquals(0, diff.getDeletedFilePaths().size());
            assertEquals(0, diff.getModifiedFilePaths().size());

            List<String> addedFilePaths = diff.getAddedFilePaths();
            assertEquals(1, addedFilePaths.size());
            assertEquals("/home/joe/b.txt", addedFilePaths.get(0));
        } finally {
            srcFs.close(true);
            refFs.close(true);
        }
    }

    /**
     * Test the folder addition use case.
     * @throws DatabaseException if BDB operation failed.
     */
    @Test
    public void testFolderAdded() throws DatabaseException {

        MockFileSystem srcFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "srcmfs");

        MockFileSystem refFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "refmfs");

        try {
            srcFs.addOrUpdateNode(MockFileSystemNode.newFolder(
                    srcFs.getNodeByPath(MockFileSystemNode.PATH_SEPARATOR),
                    "etc"));

            FileSystemDiff diff = FileSystemDiff.diff(srcFs, refFs);

            assertEquals(0, diff.getDeletedFilePaths().size());
            assertEquals(0, diff.getModifiedFilePaths().size());

            List<String> addedFilePaths = diff.getAddedFilePaths();
            assertEquals(1, addedFilePaths.size());
            assertEquals("/etc", addedFilePaths.get(0));
        } finally {
            srcFs.close(true);
            refFs.close(true);
        }
    }

    /**
     * Test the file deletion use case.
     * @throws DatabaseException if BDB operation failed.
     */
    @Test
    public void testFileRemoved() throws DatabaseException {

        MockFileSystem srcFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "srcmfs");

        MockFileSystem refFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree2.xml",
                TEMP_DIR + File.separator + "refmfs");

        try {
            FileSystemDiff diff = FileSystemDiff.diff(srcFs, refFs);

            assertEquals(0, diff.getAddedFilePaths().size());
            assertEquals(0, diff.getModifiedFilePaths().size());

            List<String> rmFilePaths = diff.getDeletedFilePaths();
            assertEquals(1, rmFilePaths.size());
            assertEquals("/home/joe/b.txt", rmFilePaths.get(0));
        } finally {
            srcFs.close(true);
            refFs.close(true);
        }
    }

    /**
     * Test the folder deletion use case.
     * @throws DatabaseException if BDB operation failed.
     */
    @Test
    public void testFolderRemoved() throws DatabaseException {

        MockFileSystem srcFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "srcmfs");

        MockFileSystem refFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "refmfs");

        try {
            srcFs.deleteNode(srcFs.getNodeByPath("/home"));

            FileSystemDiff diff = FileSystemDiff.diff(srcFs, refFs);

            assertEquals(0, diff.getAddedFilePaths().size());
            assertEquals(0, diff.getModifiedFilePaths().size());

            List<String> rmFilePaths = diff.getDeletedFilePaths();
            assertEquals(3, rmFilePaths.size());
            assertTrue(rmFilePaths.contains("/home/joe/a.txt"));
            assertTrue(rmFilePaths.contains("/home/joe"));
            assertTrue(rmFilePaths.contains("/home"));
        } finally {
            srcFs.close(true);
            refFs.close(true);
        }
    }

    /**
     * Test the file modification use case (file size modified).
     * @throws DatabaseException if BDB operation failed.
     * @throws ParseException if date parsing failed
     */
    @Test
    public void testFileModified1() throws DatabaseException, ParseException {

        MockFileSystem srcFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "srcmfs");

        MockFileSystem refFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "refmfs");

        long modTime = MockFileSystem.DATE_FORMAT.parse("20120510T1430").getTime();
        try {
            // Alter size
            MockFileSystemNode aTxt = srcFs.getNodeByPath("/home/joe/a.txt");
            aTxt.setSize(4096);
            srcFs.addOrUpdateNode(aTxt);

            FileSystemDiff diff = FileSystemDiff.diff(srcFs, refFs);

            assertEquals(0, diff.getAddedFilePaths().size());
            assertEquals(0, diff.getDeletedFilePaths().size());

            List<String> modFilePaths = diff.getModifiedFilePaths();
            assertEquals(1, modFilePaths.size());
            assertEquals("/home/joe/a.txt", modFilePaths.get(0));
        } finally {
            srcFs.close(true);
            refFs.close(true);
        }
    }

    /**
     * Test the file modification use case (date modified).
     * @throws DatabaseException if BDB operation failed.
     * @throws ParseException if date parsing failed
     */
    @Test
    public void testFileModified2() throws DatabaseException, ParseException {

        MockFileSystem srcFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "srcmfs");

        MockFileSystem refFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "refmfs");

        long modTime = MockFileSystem.DATE_FORMAT.parse("20120510T1430").getTime();
        try {
            // Alter modification date
            MockFileSystemNode aTxt = srcFs.getNodeByPath("/home/joe/a.txt");
            aTxt.setModificationDate(modTime);
            srcFs.addOrUpdateNode(aTxt);

            FileSystemDiff diff = FileSystemDiff.diff(srcFs, refFs);

            assertEquals(0, diff.getAddedFilePaths().size());
            assertEquals(0, diff.getDeletedFilePaths().size());

            List<String> modFilePaths = diff.getModifiedFilePaths();
            assertEquals(1, modFilePaths.size());
            assertEquals("/home/joe/a.txt", modFilePaths.get(0));
        } finally {
            srcFs.close(true);
            refFs.close(true);
        }
    }

    /**
     * Test the file renaming use case.
     * @throws DatabaseException if BDB operation failed.
     */
    @Test
    public void testFileRenamed() throws DatabaseException {

        MockFileSystem srcFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "srcmfs");

        MockFileSystem refFs = MockFileSystem.createFromXmlDescriptor(
                RESOURCE_DIR + File.separator + "tree1.xml",
                TEMP_DIR + File.separator + "refmfs");

        try {
            MockFileSystemNode aTxt = srcFs.getNodeByPath("/home/joe/a.txt");
            srcFs.deleteNode(aTxt);
            MockFileSystemNode bTxt = MockFileSystemNode.newFile(
                    srcFs.getNodeByPath(aTxt.getParentPath()), "b.txt");
            srcFs.addOrUpdateNode(bTxt);

            FileSystemDiff diff = FileSystemDiff.diff(srcFs, refFs);

            assertEquals(0, diff.getModifiedFilePaths().size());
            assertEquals(1, diff.getDeletedFilePaths().size());
            assertEquals("/home/joe/a.txt", diff.getDeletedFilePaths().get(0));
            assertEquals(1, diff.getAddedFilePaths().size());
            assertEquals("/home/joe/b.txt", diff.getAddedFilePaths().get(0));
        } finally {
            srcFs.close(true);
            refFs.close(true);
        }
    }

}
