package bigbang.tests.client;

import bigBang.definitions.shared.SignatureRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSigReqCancel
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<SignatureRequest> callback = new AsyncCallback<SignatureRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(SignatureRequest result)
			{
				DoStep2(result);
			}
		};

		Services.signatureRequestService.getRequest("E37CB9FA-DD33-454E-8B59-A02D0111BA37", callback);
	}

	private static void DoStep2(SignatureRequest request)
	{
		SignatureRequest.Cancellation cancellation;

		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				return;
			}
		};

		cancellation = new SignatureRequest.Cancellation();
		cancellation.requestId = request.id;
		cancellation.motiveId = "B464EFA6-A770-49C6-B038-9FE900C93F9A";
		Services.signatureRequestService.cancelRequest(cancellation, callback);
	}
}
