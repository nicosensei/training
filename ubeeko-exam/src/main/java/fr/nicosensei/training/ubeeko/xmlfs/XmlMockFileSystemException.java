package fr.nicosensei.training.ubeeko.xmlfs;

import fuse.FuseException;

/**
 * This class wraps all exceptions issued by the package classes.
 * 
 * @author nicolas
 *
 */
public class XmlMockFileSystemException extends FuseException {

	private XmlMockFileSystemException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public XmlMockFileSystemException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public XmlMockFileSystemException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
