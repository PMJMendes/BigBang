package bigBang.module.generalSystemModule.interfaces;

import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.Coverage;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CoveragesServiceAsync extends Service {

	void deleteCoverage(String id, AsyncCallback<Void> callback);

	void deleteLine(String id, AsyncCallback<Void> callback);

	void deleteSubLine(String id, AsyncCallback<Void> callback);

	void createBranch(Line b, AsyncCallback<Line> callback);

	void createModality(SubLine m, AsyncCallback<SubLine> callback);

	void createCoverage(Coverage b, AsyncCallback<Coverage> callback);

	void getLines(AsyncCallback<Line[]> callback);

	
}
