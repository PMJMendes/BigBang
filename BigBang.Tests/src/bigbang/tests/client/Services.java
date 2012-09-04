package bigbang.tests.client;

import bigBang.library.interfaces.BigBangPermissionService;
import bigBang.library.interfaces.BigBangPermissionServiceAsync;
import bigBang.library.interfaces.BigBangProcessService;
import bigBang.library.interfaces.BigBangProcessServiceAsync;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.interfaces.ContactsServiceAsync;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.interfaces.DocuShareServiceAsync;
import bigBang.library.interfaces.DocumentService;
import bigBang.library.interfaces.DocumentServiceAsync;
import bigBang.library.interfaces.ExchangeService;
import bigBang.library.interfaces.ExchangeServiceAsync;
import bigBang.library.interfaces.ExternRequestService;
import bigBang.library.interfaces.ExternRequestServiceAsync;
import bigBang.library.interfaces.FileService;
import bigBang.library.interfaces.FileServiceAsync;
import bigBang.library.interfaces.HistoryService;
import bigBang.library.interfaces.HistoryServiceAsync;
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.interfaces.InfoOrDocumentRequestServiceAsync;
import bigBang.library.interfaces.ReportService;
import bigBang.library.interfaces.ReportServiceAsync;
import bigBang.library.interfaces.TransferManagerService;
import bigBang.library.interfaces.TransferManagerServiceAsync;
import bigBang.library.interfaces.TypifiedTextService;
import bigBang.library.interfaces.TypifiedTextServiceAsync;
import bigBang.library.interfaces.ZipCodeService;
import bigBang.library.interfaces.ZipCodeServiceAsync;
import bigBang.module.casualtyModule.interfaces.CasualtyService;
import bigBang.module.casualtyModule.interfaces.CasualtyServiceAsync;
import bigBang.module.casualtyModule.interfaces.SubCasualtyService;
import bigBang.module.casualtyModule.interfaces.SubCasualtyServiceAsync;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.interfaces.ClientServiceAsync;
import bigBang.module.expenseModule.interfaces.ExpenseService;
import bigBang.module.expenseModule.interfaces.ExpenseServiceAsync;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import bigBang.module.generalSystemModule.interfaces.CoveragesServiceAsync;
import bigBang.module.generalSystemModule.interfaces.MediatorService;
import bigBang.module.generalSystemModule.interfaces.MediatorServiceAsync;
import bigBang.module.generalSystemModule.interfaces.UserService;
import bigBang.module.generalSystemModule.interfaces.UserServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectService;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyExerciseService;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyExerciseServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyObjectService;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyObjectServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyService;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyServiceAsync;
import bigBang.module.loginModule.interfaces.AuthenticationService;
import bigBang.module.loginModule.interfaces.AuthenticationServiceAsync;
import bigBang.module.quoteRequestModule.interfaces.NegotiationService;
import bigBang.module.quoteRequestModule.interfaces.NegotiationServiceAsync;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestObjectService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestObjectServiceAsync;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestServiceAsync;
import bigBang.module.receiptModule.interfaces.DASRequestService;
import bigBang.module.receiptModule.interfaces.DASRequestServiceAsync;
import bigBang.module.receiptModule.interfaces.ReceiptService;
import bigBang.module.receiptModule.interfaces.ReceiptServiceAsync;
import bigBang.module.receiptModule.interfaces.SignatureRequestService;
import bigBang.module.receiptModule.interfaces.SignatureRequestServiceAsync;
import bigBang.module.tasksModule.interfaces.TasksService;
import bigBang.module.tasksModule.interfaces.TasksServiceAsync;

import com.google.gwt.core.client.GWT;

public class Services
{
	public static final AuthenticationServiceAsync authenticationService =
			GWT.create(AuthenticationService.class);
	public static final ContactsServiceAsync contactsService =
			GWT.create(ContactsService.class);
	public static final DocumentServiceAsync documentService =
			GWT.create(DocumentService.class);
	public static final DocuShareServiceAsync docuShareService =
			GWT.create(DocuShareService.class);
	public static final ExchangeServiceAsync exchangeService =
			GWT.create(ExchangeService.class);
	public static final FileServiceAsync fileService =
			GWT.create(FileService.class);
	public static final HistoryServiceAsync historyService =
			GWT.create(HistoryService.class);
	public static final BigBangProcessServiceAsync processService =
			GWT.create(BigBangProcessService.class);
	public static final BigBangPermissionServiceAsync permissionService =
			GWT.create(BigBangPermissionService.class);
	public static final TransferManagerServiceAsync transferManagerService =
			GWT.create(TransferManagerService.class);
	public static final InfoOrDocumentRequestServiceAsync infoOrDocumentRequestService =
			GWT.create(InfoOrDocumentRequestService.class);
	public static final ExternRequestServiceAsync externRequestService =
			GWT.create(ExternRequestService.class);
	public static final TypifiedTextServiceAsync typifiedTextService =
			GWT.create(TypifiedTextService.class);
	public static final ReportServiceAsync reportService =
			GWT.create(ReportService.class);
	public static final ZipCodeServiceAsync zipCodeService =
			GWT.create(ZipCodeService.class);
	public static final TasksServiceAsync tasksService =
			GWT.create(TasksService.class);
	public static final CoveragesServiceAsync coveragesService =
			GWT.create(CoveragesService.class);
	public static final UserServiceAsync userService =
			GWT.create(UserService.class);
	public static final MediatorServiceAsync mediatorService =
			GWT.create(MediatorService.class);
	public static final ClientServiceAsync clientService =
			GWT.create(ClientService.class);
	public static final QuoteRequestServiceAsync quoteRequestService =
			GWT.create(QuoteRequestService.class);
	public static final QuoteRequestObjectServiceAsync quoteRequestObjectService =
			GWT.create(QuoteRequestObjectService.class);
	public static final NegotiationServiceAsync negotiationService =
			GWT.create(NegotiationService.class);
	public static final InsurancePolicyServiceAsync policy2Service =
			GWT.create(InsurancePolicyService.class);
	public static final PolicyObjectServiceAsync policyObjectService =
			GWT.create(PolicyObjectService.class);
	public static final PolicyExerciseServiceAsync policyExerciseService =
			GWT.create(PolicyExerciseService.class);
	public static final SubPolicyServiceAsync subPolicyService =
			GWT.create(SubPolicyService.class);
	public static final SubPolicyObjectServiceAsync subPolicyObjectService =
			GWT.create(SubPolicyObjectService.class);
	public static final SubPolicyExerciseServiceAsync subPolicyExerciseService =
			GWT.create(SubPolicyExerciseService.class);
	public static final ReceiptServiceAsync receiptService =
			GWT.create(ReceiptService.class);
	public static final SignatureRequestServiceAsync signatureRequestService =
			GWT.create(SignatureRequestService.class);
	public static final DASRequestServiceAsync dasRequestService =
			GWT.create(DASRequestService.class);
	public static final CasualtyServiceAsync casualtyService =
			GWT.create(CasualtyService.class);
	public static final SubCasualtyServiceAsync subCasualtyService =
			GWT.create(SubCasualtyService.class);
	public static final ExpenseServiceAsync expenseService =
			GWT.create(ExpenseService.class);
}
