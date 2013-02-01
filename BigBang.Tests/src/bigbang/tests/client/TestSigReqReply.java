package bigbang.tests.client;

import bigBang.definitions.shared.SignatureRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSigReqReply
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		SignatureRequest.Response response;

		AsyncCallback<SignatureRequest> callback = new AsyncCallback<SignatureRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(SignatureRequest result)
			{
				return;
			}
		};

		response = new SignatureRequest.Response();
		response.requestId = "E37CB9FA-DD33-454E-8B59-A02D0111BA37";
		Services.signatureRequestService.receiveResponse(response, callback);
	}
}
