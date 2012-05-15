/**
 *
 */
package fr.nicosensei.training.ubeeko;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import fr.nicosensei.training.ubeeko.mockfs.MockFileSystem;
import fr.nicosensei.training.ubeeko.mockfs.MockFileSystemNode;
import fr.nicosensei.training.ubeeko.mockfs.MockFileSystemNode.Type;

/**
 * Detects differences between a file system and a reference file system.
 *
 * NB file renames are detected as deletion + creation operations.
 *
 * @author ngiraud
 *
 */
public final class FileSystemDiff {

    /**
     * The source file system (e.g. a local file system)
     */
    private final MockFileSystem sourceFs;

    /**
     * The reference file system.
     */
    private final MockFileSystem referenceFs;

    /**
     * Files added.
     */
    private List<String> addedFilePaths;

    /**
     * Files deleted.
     */
    private List<String> deletedFilePaths;

    /**
     * Files modified (currently only detects a change in modification date and/or size
     * of files, not folders).
     */
    private List<String> modifiedFilePaths;

    /**
     * Constructor.
     * @param sourceFs the source file system
     * @param referenceFs the reference file system
     */
    private FileSystemDiff(
            final MockFileSystem sourceFs,
            final MockFileSystem referenceFs) {
        this.sourceFs = sourceFs;
        this.referenceFs = referenceFs;
        this.addedFilePaths = new ArrayList<String>();
        this.deletedFilePaths = new ArrayList<String>();
        this.modifiedFilePaths = new ArrayList<String>();
    }

    /**
     * Makes a diff between a source and a reference file system.
     * @param sourceFs the source fs
     * @param referenceFs the reference fs
     * @return the computed diff
     * @throws DatabaseException if the underlying BDB fails.
     */
    public static FileSystemDiff diff(
            final MockFileSystem sourceFs,
            final MockFileSystem referenceFs) throws DatabaseException {

        FileSystemDiff diff = new FileSystemDiff(sourceFs, referenceFs);
        diff.recursiveCompareWithReference(
                sourceFs.getNodeByPath(MockFileSystemNode.PATH_SEPARATOR));
        diff.recursiveCompareWithSource(
                referenceFs.getNodeByPath(MockFileSystemNode.PATH_SEPARATOR));

        return diff;
    }

    /**
     * @return the addedFilePaths
     */
    public List<String> getAddedFilePaths() {
        return addedFilePaths;
    }

    /**
     * @return the deletedFilePaths
     */
    public List<String> getDeletedFilePaths() {
        return deletedFilePaths;
    }

    /**
     * @return the modifiedFilePaths
     */
    public List<String> getModifiedFilePaths() {
        return modifiedFilePaths;
    }

    /**
     * Recursive diff calculation. Compares source to destination to detect added and
     * modified files.
     * @param sourceNode the source node to begin traversal with
     * @throws DatabaseException if the underlying BDB fails.
     */
    private void recursiveCompareWithReference(final MockFileSystemNode sourceNode)
            throws DatabaseException {
        String path = sourceNode.getAbsolutePath();
        MockFileSystemNode refMatch = referenceFs.getNodeByPath(path);
        if (refMatch != null) {
            Type sourceType = sourceNode.getType();
            if (sourceType.equals(refMatch.getType())) {
                if (Type.FILE.equals(sourceType)
                        && (refMatch.getSize() != sourceNode.getSize()
                        || refMatch.getModificationTime()
                        != sourceNode.getModificationTime())) {
                    modifiedFilePaths.add(path);
                } else if (Type.ROOT.equals(sourceType)
                        || Type.FOLDER.equals(sourceType)) {
                    Iterator<MockFileSystemNode> children =
                            sourceFs.getChildrenNodes(sourceNode);
                    while (children.hasNext()) {
                        recursiveCompareWithReference(children.next());
                    }
                }
            } else {
                // The file was deleted and recreated with a different type
                deletedFilePaths.add(path);
                addedFilePaths.add(path);
            }
        } else {
            addedFilePaths.add(sourceNode.getAbsolutePath());
        }
    }

    /**
     * Recursive diff calculation. Compares reference to source to detect deleted files.
     * @param startNode the node to begin traversal with
     * @throws DatabaseException if the underlying BDB fails.
     */
    private void recursiveCompareWithSource(final MockFileSystemNode startNode)
            throws DatabaseException {
        String path = startNode.getAbsolutePath();
        MockFileSystemNode srcMatch = sourceFs.getNodeByPath(path);
        if (srcMatch == null) {
            deletedFilePaths.add(path);
        }
        if (!Type.FILE.equals(startNode.getType())) {
            Iterator<MockFileSystemNode> children =
                    referenceFs.getChildrenNodes(startNode);
            while (children.hasNext()) {
                recursiveCompareWithSource(children.next());
            }
        }
    }

}
