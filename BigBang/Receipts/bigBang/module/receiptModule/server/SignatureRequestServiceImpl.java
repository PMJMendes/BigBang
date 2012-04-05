package bigBang.module.receiptModule.server;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.EngineImplementor;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.receiptModule.interfaces.SignatureRequestService;

import com.premiumminds.BigBang.Jewel.Operations.SignatureRequest.CancelRequest;
import com.premiumminds.BigBang.Jewel.Operations.SignatureRequest.ReceiveReply;
import com.premiumminds.BigBang.Jewel.Operations.SignatureRequest.RepeatRequest;

public class SignatureRequestServiceImpl
	extends EngineImplementor
	implements SignatureRequestService
{
	private static final long serialVersionUID = 1L;

	public static SignatureRequest sGetRequest(UUID pid)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SignatureRequest lobjRequest;
		IProcess lobjProcess;
//		RequestAddress[] larrAddresses;
		SignatureRequest lobjResult;
//		ArrayList<String> larrUsers;
//		ArrayList<String> larrInfos;
//		ArrayList<String> larrCCs;
//		ArrayList<String> larrBCCs;
//		int i;

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.SignatureRequest.GetInstance(Engine.getCurrentNameSpace(), pid);
//			larrAddresses = lobjRequest.GetAddresses();
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjRequest.GetProcessID());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new SignatureRequest();
		lobjResult.id = lobjRequest.getKey().toString();

		lobjResult.replylimit = (int)((((Timestamp)lobjRequest.getAt(5)).getTime() -
				(new Timestamp(new java.util.Date().getTime())).getTime()) / 86400000L);

//		larrUsers = new ArrayList<String>();
//		larrInfos = new ArrayList<String>();
//		larrCCs = new ArrayList<String>();
//		larrBCCs = new ArrayList<String>();
//		for ( i = 0; i < larrAddresses.length; i++ )
//		{
//			if ( Constants.UsageID_To.equals(larrAddresses[i].getAt(2)) )
//				larrInfos.add(((UUID)larrAddresses[i].getAt(4)).toString());
//			if ( Constants.UsageID_CC.equals(larrAddresses[i].getAt(2)) )
//				larrCCs.add(larrAddresses[i].getLabel());
//			if ( Constants.UsageID_BCC.equals(larrAddresses[i].getAt(2)) )
//				larrCCs.add(larrAddresses[i].getLabel());
//			if ( Constants.UsageID_ReplyTo.equals(larrAddresses[i].getAt(2)) )
//			{
//				try
//				{
//					larrUsers.add(User.GetInstance(Engine.getCurrentNameSpace(), (UUID)larrAddresses[i].getAt(3)).getFullName());
//				}
//				catch (Throwable e)
//				{
//					throw new BigBangException(e.getMessage(), e);
//				}
//			}
//		}
//
//		lobjResult.message.toContactInfoId = larrInfos.get(0);
//		lobjResult.message.forwardUserFullNames = larrUsers.toArray(new String[larrUsers.size()]);
//		lobjResult.message.internalBCCs = StringUtils.join(larrBCCs.toArray(new String[larrBCCs.size()]), ';');
//		lobjResult.message.externalCCs = StringUtils.join(larrCCs.toArray(new String[larrCCs.size()]), ';');
//		lobjResult.message.subject = (String)lobjRequest.getAt(4);
//		lobjResult.message.text = lobjRequest.getText();

		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public SignatureRequest getRequest(String id)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetRequest(UUID.fromString(id));
	}

	public SignatureRequest repeatRequest(SignatureRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SignatureRequest lobjRequest;
		RepeatRequest lopRR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.SignatureRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.id));

			lopRR = new RepeatRequest(lobjRequest.GetProcessID());
			lopRR.mlngDays = request.replylimit;

			lopRR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetRequest(lobjRequest.getKey());
	}

	public SignatureRequest receiveResponse(SignatureRequest.Response response)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SignatureRequest lobjRequest;
		ReceiveReply lopRR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.SignatureRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(response.requestId));

			lopRR = new ReceiveReply(lobjRequest.GetProcessID());
//			lopRR.mobjMessage = MessageBridge.incomingToServer(response.message,
//					PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjRequest.GetProcessID()).GetScript().GetDataType());

			lopRR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetRequest(lobjRequest.getKey());
	}

	public void cancelRequest(SignatureRequest.Cancellation cancellation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SignatureRequest lobjRequest;
		CancelRequest lopCR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.SignatureRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(cancellation.requestId));

			lopCR = new CancelRequest(lobjRequest.GetProcessID());
			lopCR.midMotive = UUID.fromString(cancellation.motiveId);

			lopCR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}
}
