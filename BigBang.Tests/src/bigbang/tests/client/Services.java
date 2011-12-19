package bigbang.tests.client;

import bigBang.library.interfaces.BigBangProcessService;
import bigBang.library.interfaces.BigBangProcessServiceAsync;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.interfaces.DocuShareServiceAsync;
import bigBang.library.interfaces.FileService;
import bigBang.library.interfaces.FileServiceAsync;
import bigBang.library.interfaces.HistoryService;
import bigBang.library.interfaces.HistoryServiceAsync;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.interfaces.ClientServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectServiceAsync;
import bigBang.module.loginModule.interfaces.AuthenticationService;
import bigBang.module.loginModule.interfaces.AuthenticationServiceAsync;
import bigBang.module.receiptModule.interfaces.ReceiptService;
import bigBang.module.receiptModule.interfaces.ReceiptServiceAsync;
import bigBang.module.tasksModule.interfaces.TasksService;
import bigBang.module.tasksModule.interfaces.TasksServiceAsync;

import com.google.gwt.core.client.GWT;

public class Services
{
	public static final AuthenticationServiceAsync authenticationService =
			GWT.create(AuthenticationService.class);
	public static final DocuShareServiceAsync docuShareService =
			GWT.create(DocuShareService.class);
	public static final FileServiceAsync fileService =
			GWT.create(FileService.class);
	public static final HistoryServiceAsync historyService =
			GWT.create(HistoryService.class);
	public static final BigBangProcessServiceAsync processService =
			GWT.create(BigBangProcessService.class);
	public static final TasksServiceAsync tasksService =
			GWT.create(TasksService.class);
	public static final ClientServiceAsync clientService =
			GWT.create(ClientService.class);
	public static final InsurancePolicyServiceAsync insurancePolicyService =
			GWT.create(InsurancePolicyService.class);
	public static final PolicyObjectServiceAsync policyObjectService =
			GWT.create(PolicyObjectService.class);
	public static final PolicyExerciseServiceAsync policyExerciseService =
			GWT.create(PolicyExerciseService.class);
	public static final ReceiptServiceAsync receiptService =
			GWT.create(ReceiptService.class);
}
