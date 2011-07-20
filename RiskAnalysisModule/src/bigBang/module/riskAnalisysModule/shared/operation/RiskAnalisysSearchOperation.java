package bigBang.module.riskAnalisysModule.shared.operation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.riskAnalisysModule.client.resources.Resources;
import bigBang.module.riskAnalisysModule.client.userInterface.presenter.RiskAnalisysSearchOperationViewPresenter;
import bigBang.module.riskAnalisysModule.client.userInterface.view.RiskAnalisysSearchOperationView;

public class RiskAnalisysSearchOperation implements Operation {

	public static final String ID = "riskAnalisysSearchOperation";
	private final String DESCRIPTION = "A operação de pesquisa por Análises de risco";
	private final String SHORT_DESCRIPTION = "Pesquisa";
	private final String OWNER_PROCESS_ID = "";// ;

	private boolean permission;

	private View view;
	private ViewPresenter presenter;

	public void init() {

	}

	public String getId() {
		return ID;
	}

	public AbstractImagePrototype getIcon() {
		Resources r = GWT.create(Resources.class);
		return AbstractImagePrototype.create(r.searchIcon());
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getShortDescription() {
		return SHORT_DESCRIPTION;
	}

	public ViewPresenter getPresenter() {
		if(this.presenter == null){
			this.presenter = (ViewPresenter) new RiskAnalisysSearchOperationViewPresenter(null, null, this.getView());
			((RiskAnalisysSearchOperationViewPresenter)this.presenter).setOperation(this);
		}
		return this.presenter;
	}

	public View getView() {
		if(this.view == null)
			this.view = new RiskAnalisysSearchOperationView();
		return this.view;
	}

	public String getOwnerProcessId() {
		return this.OWNER_PROCESS_ID;
	}

	@Override
	public boolean getPermission() {
		return permission;
	}

	@Override
	public void setPermission(boolean p) {
		this.permission = p;
	}
}
