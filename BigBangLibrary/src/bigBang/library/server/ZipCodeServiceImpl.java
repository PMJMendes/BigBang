package bigBang.library.server;

import bigBang.library.interfaces.ZipCodeService;
import bigBang.library.shared.ZipCode;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ZipCodeServiceImpl extends RemoteServiceServlet implements ZipCodeService {

	private static final long serialVersionUID = 1L;

	@Override
	public ZipCode getZipCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}
}
