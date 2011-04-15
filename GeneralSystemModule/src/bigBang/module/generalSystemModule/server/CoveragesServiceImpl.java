package bigBang.module.generalSystemModule.server;

import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import bigBang.module.generalSystemModule.shared.Coverage;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CoveragesServiceImpl extends RemoteServiceServlet implements CoveragesService {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Line[] getLines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coverage createCoverage(Coverage b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubLine createModality(SubLine m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Line createBranch(Line b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCoverage(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteLine(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSubLine(String id) {
		// TODO Auto-generated method stub
		
	}

}
