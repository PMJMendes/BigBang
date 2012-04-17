package bigBang.library.client.userInterface.reports;

import java.util.Map;

import bigBang.definitions.shared.ReportParam;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.view.View;

public class ParamReportPanel extends FiltersPanel {
	
	public ParamReportPanel(Map<Enum<?>, String> sorts) {
		super(sorts);
	}

	@Override
	protected void initializeView() {
		return;
	}

	public void setAvailableParameters(ReportParam[] parameters) {
		// TODO Auto-generated method stub
		
	}

	public ReportParam[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public void clearParameters() {
		// TODO Auto-generated method stub
		
	}

}
