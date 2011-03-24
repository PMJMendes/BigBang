package bigBang.module.generalSystemModule.shared.operation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import bigBang.library.client.Operation;
import bigBang.module.generalSystemModule.client.resources.Resources;

public class InsuranceAgencyManagementOperation implements Operation {

	public static final String ID = "INSURANCE_AGENCY_MANAGEMENT_OPERATION";
	public static final String DESCRIPTION = "Gestão de Seguradoras";
	public static final String SHORT_DESCRIPTION = "Seguradoras";
	public static final String OWNER_PROCESS_ID = "PROCESS_GENERAL_SYSTEM";
	
	public void init() {
		
	}

	public String getId() {
		return ID;
	}

	public AbstractImagePrototype getIcon() {
		Resources r = GWT.create(Resources.class);
		return AbstractImagePrototype.create(r.insuranceAgencyIcon());
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getShortDescription() {
		return SHORT_DESCRIPTION;
	}

	public String getOwnerProcessId() {
		return OWNER_PROCESS_ID;
	}

}
