package bigBang.library.server;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.ExternalInfoRequest.Closing;
import bigBang.definitions.shared.ExternalInfoRequest.Incoming;
import bigBang.definitions.shared.ExternalInfoRequest.Outgoing;
import bigBang.library.interfaces.ExternRequestService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Objects.ExternRequest;
import com.premiumminds.BigBang.Jewel.Operations.ExternRequest.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.ExternRequest.ReceiveAdditionalInfo;
import com.premiumminds.BigBang.Jewel.Operations.ExternRequest.SendInformation;

public class ExternRequestServiceImpl
	extends EngineImplementor
	implements ExternRequestService
{
	private static final long serialVersionUID = 1L;

	public static ExternalInfoRequest sGetRequest(UUID pid)
		throws BigBangException
	{
		ExternRequest lobjRequest;
		IProcess lobjProcess;
		IProcess lobjParent;
		IScript lobjScript;
		ExternalInfoRequest lobjResult;

		try
		{
			lobjRequest = ExternRequest.GetInstance(Engine.getCurrentNameSpace(), pid);
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjRequest.GetProcessID());
			lobjParent = lobjProcess.GetParent();
			lobjScript = lobjParent.GetScript();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new ExternalInfoRequest();
		lobjResult.id = lobjRequest.getKey().toString();

		lobjResult.parentDataObjectId = lobjParent.GetDataKey().toString();
		lobjResult.parentDataTypeId = lobjScript.GetDataType().toString();
		lobjResult.subject = (String)lobjRequest.getAt(1);
		lobjResult.message.notes = lobjRequest.getText();
		lobjResult.originalFrom = (String)lobjRequest.getAt(3);
		lobjResult.fromInfoId = ( lobjRequest.getAt(5) == null ? null : ((UUID)lobjRequest.getAt(5)).toString() );
		lobjResult.replylimit = (int)((((Timestamp)lobjRequest.getAt(4)).getTime() -
				(new Timestamp(new java.util.Date().getTime())).getTime()) / 86400000L);

		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public ExternalInfoRequest getRequest(String id)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetRequest(UUID.fromString(id));
	}

	public ExternalInfoRequest sendInformation(Outgoing outgoing)
		throws SessionExpiredException, BigBangException
	{
		ExternRequest lobjRequest;
		SendInformation lopSI;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = ExternRequest.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(outgoing.requestId));

			lopSI = new SendInformation(lobjRequest.GetProcessID());

			lopSI.mobjMessage = MessageBridge.outgoingToServer(outgoing.message);
			lopSI.mbIsFinal = outgoing.isFinal;
			lopSI.mlngDays = outgoing.replylimit;

			lopSI.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetRequest(lobjRequest.getKey());
	}

	public ExternalInfoRequest receiveAdditional(Incoming incoming)
		throws SessionExpiredException, BigBangException
	{
		ExternRequest lobjRequest;
		ReceiveAdditionalInfo lopRAI;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = ExternRequest.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(incoming.requestId));

			lopRAI = new ReceiveAdditionalInfo(lobjRequest.GetProcessID());

			lopRAI.mobjMessage = MessageBridge.incomingToServer(incoming.message, PNProcess.GetInstance(Engine.getCurrentNameSpace(),
					lobjRequest.GetProcessID()).GetParent().GetScript().GetDataType());
			lopRAI.mlngDays = incoming.replylimit;

			lopRAI.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetRequest(lobjRequest.getKey());
	}

	public void closeRequest(Closing closing)
		throws SessionExpiredException, BigBangException
	{
		ExternRequest lobjRequest;
		CloseProcess lopCP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = ExternRequest.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(closing.requestId));

			lopCP = new CloseProcess(lobjRequest.GetProcessID());

			lopCP.midMotive = UUID.fromString(closing.motiveId);

			lopCP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}
}
