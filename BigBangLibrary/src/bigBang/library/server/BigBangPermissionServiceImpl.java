package bigBang.library.server;

import bigBang.library.interfaces.BigBangPermissionService;
import bigBang.library.shared.Permission;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class BigBangPermissionServiceImpl extends RemoteServiceServlet implements BigBangPermissionService {

	private static final long serialVersionUID = 1L;

	@Override
	public String[] getProcessPermissions(String id) {
		// TODO Auto-generated method stub
		return null;
	}
}
