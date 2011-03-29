package bigBang.library.shared;

import java.io.Serializable;

public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	public boolean create;
	public boolean read;
	public boolean update;
	public boolean delete;
	
	public boolean hasPermissions() {
		return create || read || update || delete;
	}
	
}
