package fr.nicosensei.training.ubeeko.xmlfs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import fuse.Filesystem3;
import fuse.FuseDirFiller;
import fuse.FuseException;
import fuse.FuseGetattrSetter;
import fuse.FuseOpenSetter;
import fuse.FuseStatfsSetter;

/**
 * Mock file system implementation.
 * @author nicolas
 *
 */
public class XmlMockFileSystem implements Filesystem3 {

	public int getattr(String path, FuseGetattrSetter getattrSetter)
			throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int readlink(String path, CharBuffer link) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getdir(String path, FuseDirFiller dirFiller)
			throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int mknod(String path, int mode, int rdev) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int mkdir(String path, int mode) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int unlink(String path) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int rmdir(String path) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int symlink(String from, String to) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int rename(String from, String to) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int link(String from, String to) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int chmod(String path, int mode) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int chown(String path, int uid, int gid) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int truncate(String path, long size) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int utime(String path, int atime, int mtime) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int statfs(FuseStatfsSetter statfsSetter) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int open(String path, int flags, FuseOpenSetter openSetter)
			throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int read(String path, Object fh, ByteBuffer buf, long offset)
			throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int write(String path, Object fh, boolean isWritepage,
			ByteBuffer buf, long offset) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int flush(String path, Object fh) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int release(String path, Object fh, int flags) throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int fsync(String path, Object fh, boolean isDatasync)
			throws FuseException {
		// TODO Auto-generated method stub
		return 0;
	}

}
