package fr.nicosensei.training.ubeeko.mockfs;

import fuse.FuseException;

/**
 * This class wraps all exceptions issued by the package classes.
 * 
 * @author nicolas
 *
 */
public class MockFileSystemException extends FuseException {

	private MockFileSystemException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public MockFileSystemException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MockFileSystemException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
