package bigBang.library.server;

import bigBang.library.interfaces.BigBangProcessService;
import bigBang.library.shared.BigBangProcess;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class BigBangProcessServiceImpl extends RemoteServiceServlet implements BigBangProcessService {

	private static final long serialVersionUID = 1L;

	@Override
	public BigBangProcess[] getProcesses(String processTypeId) {
		return null;
	}
}
