package service;

/**
 * The general exception to represent condition when the data present in client
 * request can not be stored due to inconsistency. For example, adding a module with
 * a name that already belongs to another module, or passing in empty information
 * that is required to update or add information to data file.
 */
public class InconsistentDataException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public InconsistentDataException(String msg) {
		super(msg);
		
	}	

}
