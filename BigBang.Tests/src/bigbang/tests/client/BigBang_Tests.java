package bigbang.tests.client;

import bigBang.module.loginModule.shared.LoginResponse;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BigBang_Tests
	implements EntryPoint
{
	public void onModuleLoad()
	{
		AsyncCallback<LoginResponse> callback = new AsyncCallback<LoginResponse>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(LoginResponse result)
			{
				DoTests();
			}
		};

//		Services.authenticationService.login("CrediteEGS", callback);
		Services.authenticationService.login("root", "Premium.", "CrediteEGS", callback);
	}

	private void DoTests()
	{
//		TestSpecialChangePassword.DoTest();
//		TestSpecialGetAsImage.DoTest();
//		TestSpecialGetSubProcs.DoTest();
//		TestSpecialGetPermissions.DoTest();
//		TestSpecialGetGlobalPermissions.DoTest();
//		TestSpecialGetExchangeItems.DoTest();
//		TestSpecialGetExchangeSingleItem.DoTest();
//		TestSpecialZipCodeGet.DoTest();
//		TestSpecialFileProcessing.DoTest();
//
//		TestReportGetPrintSet.DoTest();
//		TestReportPrintSet.DoTest();
//		TestReportGetTransactionSet.DoTest();
//		TestReportGetParam.DoTest();
//		TestReportParamHTML.DoTest();
//		TestReportExcel.DoTest();
//		TestReportSettleTransaction.DoTest();
//		TestReportVerb.DoTest();
//
//		TestHistoryGet.DoTest();
//		TestHistoryUndo.DoTest();
//
//		TestMgrXferGet.DoTest();
//		TestMgrXferCreate.DoTest();
//		TestMgrXferAccept.DoTest();
//		TestMgrXferCancel.DoTest();
//		TestMgrXferMassCreate.DoTest();
//
//		TestInfoReqGet.DoTest();
//		TestInfoReqCreate.DoTest();
//		TestInfoReqRepeat.DoTest();
//		TestInfoReqReply.DoTest();
//		TestInfoReqCancel.DoTest();
//
//		TestExternRequestGet.DoTest();
//		TestExternRequestCreate.DoTest();
//		TestExternRequestSend.DoTest();
//		TestExternRequestReceive.DoTest();
//		TestExternRequestClose.DoTest();
//
//		TestContactGet.DoTest();
//		TestContactCreate.DoTest();
//		TestContactEdit.DoTest();
//		TestContactDelete.DoTest();
//
//		TestDocumentGet.DoTest();
//		TestDocumentCreate.DoTest();
//		TestDocumentEdit.DoTest();
//		TestDocumentDelete.DoTest();
//
//		TestTextGet.DoTest();
//		TestTextCreate.DoTest();
//		TestTextEdit.DoTest();
//		TestTextDelete.DoTest();
//
//		TestAgendaGet.DoTest();
//		TestAgendaDismiss.DoTest();
//
//		TestValuesGet.DoTest();
//
//		TestCompanyGet.DoTest();
//		TestCompanyCreate.DoTest();
//		TestCompanyEdit.DoTest();
//		TestCompanyDelete.DoTest();
//
//		TestUserGet.DoTest();
//		TestUserCreate.DoTest();
//		TestUserEdit.DoTest();
//		TestUserDelete.DoTest();
//
//		TestMediatorGet.DoTest();
//		TestMediatorCreate.DoTest();
//		TestMediatorEdit.DoTest();
//		TestMediatorDelete.DoTest();
//
//		TestTaxGet.DoTest();
//		TestTaxCreate.DoTest();
//		TestTaxEdit.DoTest();
//		TestTaxDelete.DoTest();
//
//		TestClientGet.DoTest();
//		TestClientCreate.DoTest();
//		TestClientEdit.DoTest();
//		TestClientMerge.DoTest();
//		TestClientDelete.DoTest();
//
//		TestQuoteRequestGet.DoTest();
//		TestQuoteRequestCreate.DoTest();
//		TestQuoteRequestEdit.DoTest();
//		TestQuoteRequestDelete.DoTest();
//		TestQuoteRequestClose.DoTest();
//
//		TestQRObjectGet.DoTest();
//		TestQRObjectCreate.DoTest();
//		TestQRObjectEdit.DoTest();
//		TestQRObjectDelete.DoTest();
//
//		TestQRSubLineCreate.DoTest();
//		TestQRSubLineDelete.DoTest();
//
//		TestNegotiationGet.DoTest();
//		TestNegotiationCreate.DoTest();
//		TestNegotiationEdit.DoTest();
//		TestNegotiationDelete.DoTest();
//		TestNegotiationReceiveQuote.DoTest();
//		TestNegotiationGrant.DoTest();
//		TestNegotiationCancel.DoTest();
//
//		TestPolicyGet.DoTest();
//		TestPolicyCreate.DoTest();
//		TestPolicyEdit.DoTest();
//		TestPolicyDelete.DoTest();
//		TestPolicyValidate.DoTest();
//		TestPolicyCalculations.DoTest();
//		TestPolicyCreateDN.DoTest();
//		TestPolicyChangeClient.DoTest();
//		TestPolicyVoid.DoTest();
//		TestPolicyDoubleInit.DoTest();
//		TestPolicyCreateSubDebitNotes.DoTest();
//
//		TestObjectGet.DoTest();
//		TestObjectCreate.DoTest();
//		TestObjectEdit.DoTest();
//		TestObjectDelete.DoTest();
//
//		TestSubPolicyGet.DoTest();
//		TestSubPolicyCreate.DoTest();
//		TestSubPolicyEdit.DoTest();
//		TestSubPolicyDelete.DoTest();
//		TestSubPolicyValidate.DoTest();
//		TestSubPolicyCalculations.DoTest();
//		TestSubPolicyChangePolicy.DoTest();
//
//		TestSubObjectGet.DoTest();
//		TestSubObjectCreate.DoTest();
//		TestSubObjectEdit.DoTest();
//		TestSubObjectDelete.DoTest();
//
//		TestReceiptGet.DoTest();
//		TestReceiptCreate.DoTest();
//		TestReceiptEdit.DoTest();
//		TestReceiptDelete.DoTest();
//		TestReceiptChangePolicy.DoTest();
//		TestReceiptCreateSerial.DoTest();
//		TestReceiptReceiveImage.DoTest();
//		TestReceiptDebitNote.DoTest();
//		TestReceiptValidate.DoTest();
//		TestReceiptEditAndValidate.DoTest();
//		TestReceiptSetReturn.DoTest();
//		TestReceiptCreatePaymentNotice.DoTest();
//		TestReceiptPayment.DoTest();
//		TestReceiptSend.DoTest();
//		TestReceiptInsurerAccounting.DoTest();
//		TestReceiptMediatorAccounting.DoTest();
//		TestReceiptReturnToInsurer.DoTest();
//		TestReceiptCreateSignatureRequest.DoTest();
//		TestReceiptSendPayment.DoTest();
//		TestReceiptNoDAS.DoTest();
//		TestReceiptCreateDASRequest.DoTest();
//		TestReceiptNotPayed.DoTest();
//
//		TestSigReqGet.DoTest();
//		TestSigReqRepeat.DoTest();
//		TestSigReqReply.DoTest();
//		TestSigReqCancel.DoTest();
//
//		TestDASReqGet.DoTest();
//		TestDASReqRepeat.DoTest();
//		TestDASReqReply.DoTest();
//		TestDASReqCancel.DoTest();
//
//		TestCasualtyGet.DoTest();
//		TestCasualtyCreate.DoTest();
//		TestCasualtyEdit.DoTest();
//		TestCasualtyDelete.DoTest();
//		TestCasualtyClose.DoTest();
//
//		TestSubCasualtyGet.DoTest();
//		TestSubCasualtyCreate.DoTest();
//		TestSubCasualtyEdit.DoTest();
//		TestSubCasualtyDelete.DoTest();
//		TestSubCasualtyMarkForClosing.DoTest();
//		TestSubCasualtyRejectClosing.DoTest();
//		TestSubCasualtyCloseProcess.DoTest();
//
//		TestExpenseGet.DoTest();
//		TestExpenseCreate.DoTest();
//		TestExpenseEdit.DoTest();
//		TestExpenseDelete.DoTest();
//		TestExpenseSendNotification.DoTest();
//		TestExpenseReceiveAcceptance.DoTest();
//		TestExpenseReceiveReturn.DoTest();
//		TestExpenseNotifyClient.DoTest();
//		TestExpenseReturnToClient.DoTest();
//		TestExpenseReceiveReception.DoTest();
	}
}
