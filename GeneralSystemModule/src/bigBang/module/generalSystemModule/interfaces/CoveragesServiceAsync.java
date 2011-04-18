package bigBang.module.generalSystemModule.interfaces;

import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.Coverage;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CoveragesServiceAsync
	extends Service
{
	void getLines(AsyncCallback<Line[]> callback);
	void createLine(Line b, AsyncCallback<Line> callback);
	void saveLine(Line b, AsyncCallback<Line> callback);
	void deleteLine(String id, AsyncCallback<Void> callback);

	void createSubLine(SubLine m, AsyncCallback<SubLine> callback);
	void saveSubLine(SubLine m, AsyncCallback<SubLine> callback);
	void deleteSubLine(String id, AsyncCallback<Void> callback);

	void createCoverage(Coverage b, AsyncCallback<Coverage> callback);
	void saveCoverage(Coverage b, AsyncCallback<Coverage> callback);
	void deleteCoverage(String id, AsyncCallback<Void> callback);
}
