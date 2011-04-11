package bigBang.module.generalSystemModule.server;

import bigBang.module.generalSystemModule.interfaces.GeneralSystemService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GeneralSystemServiceImpl extends RemoteServiceServlet implements GeneralSystemService {

	private static final long serialVersionUID = 1L;

	@Override
	public String getGeneralSystemProcessId() {
		return "49153B77-1391-4E3A-81D2-9EB800CB68B7";
	}
}
