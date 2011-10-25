package bigBang.library.server;

import bigBang.library.interfaces.BigBangProcessService;
import bigBang.library.shared.BigBangProcess;

public class BigBangProcessServiceImpl
	extends EngineImplementor
	implements BigBangProcessService
{
	private static final long serialVersionUID = 1L;

	public BigBangProcess[] getSubProcesses(String parentProcessId)
	{
		return null;
	}
}
