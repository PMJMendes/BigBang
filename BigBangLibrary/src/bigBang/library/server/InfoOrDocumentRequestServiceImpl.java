package bigBang.library.server;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import Jewel.Engine.Engine;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest.Cancellation;
import bigBang.definitions.shared.InfoOrDocumentRequest.Response;
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.InfoRequest;
import com.premiumminds.BigBang.Jewel.Objects.RequestAddress;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.InfoRequest.CancelRequest;
import com.premiumminds.BigBang.Jewel.Operations.InfoRequest.ReceiveReply;
import com.premiumminds.BigBang.Jewel.Operations.InfoRequest.RepeatRequest;

public class InfoOrDocumentRequestServiceImpl
	extends EngineImplementor
	implements InfoOrDocumentRequestService
{
	private static final long serialVersionUID = 1L;

	public static InfoOrDocumentRequest sGetRequest(UUID pid)
		throws BigBangException
	{
		InfoRequest lobjRequest;
		IProcess lobjProcess;
		IProcess lobjParent;
		IScript lobjScript;
		RequestAddress[] larrAddresses;
		InfoOrDocumentRequest lobjResult;
		ArrayList<String> larrUsers;
		ArrayList<String> larrInfos;
		ArrayList<String> larrCCs;
		ArrayList<String> larrBCCs;
		int i;

		try
		{
			lobjRequest = InfoRequest.GetInstance(Engine.getCurrentNameSpace(), pid);
			larrAddresses = lobjRequest.GetAddresses();
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjRequest.GetProcessID());
			lobjParent = lobjProcess.GetParent();
			lobjScript = lobjParent.GetScript();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new InfoOrDocumentRequest();
		lobjResult.id = lobjRequest.getKey().toString();

		lobjResult.parentDataObjectId = lobjParent.GetDataKey().toString();
		lobjResult.parentDataTypeId = lobjScript.GetDataType().toString();
		lobjResult.requestTypeId = ((UUID)lobjRequest.getAt(1)).toString();
		lobjResult.subject = (String)lobjRequest.getAt(5);
		lobjResult.text = lobjRequest.getText();
		lobjResult.replylimit = (Integer)lobjRequest.getAt(3);

		larrUsers = new ArrayList<String>();
		larrInfos = new ArrayList<String>();
		larrCCs = new ArrayList<String>();
		larrBCCs = new ArrayList<String>();
		for ( i = 0; i < larrAddresses.length; i++ )
		{
			if ( Constants.UsageID_To.equals(larrAddresses[i].getAt(2)) )
				larrInfos.add(((UUID)larrAddresses[i].getAt(4)).toString());
			if ( Constants.UsageID_CC.equals(larrAddresses[i].getAt(2)) )
				larrCCs.add(larrAddresses[i].getLabel());
			if ( Constants.UsageID_BCC.equals(larrAddresses[i].getAt(2)) )
				larrCCs.add(larrAddresses[i].getLabel());
			if ( Constants.UsageID_ReplyTo.equals(larrAddresses[i].getAt(2)) )
				larrUsers.add(((UUID)larrAddresses[i].getAt(3)).toString());
		}

		lobjResult.toContactInfoId = larrInfos.get(0);
		lobjResult.forwardUserIds = larrUsers.toArray(new String[larrUsers.size()]);
		lobjResult.internalBCCs = StringUtils.join(larrBCCs.toArray(new String[larrBCCs.size()]), ';');
		lobjResult.externalCCs = StringUtils.join(larrCCs.toArray(new String[larrCCs.size()]), ';');

		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public InfoOrDocumentRequest getRequest(String id)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetRequest(UUID.fromString(id));
	}

	public InfoOrDocumentRequest repeatRequest(InfoOrDocumentRequest request)
		throws SessionExpiredException, BigBangException
	{
		InfoRequest lobjRequest;
		RepeatRequest lopRR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = InfoRequest.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(request.id));

			lopRR = new RepeatRequest(lobjRequest.GetProcessID());
			lopRR.mlngDays = request.replylimit;
			lopRR.mstrSubject = request.subject;
			lopRR.mstrRequestBody = request.text;

			lopRR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return InfoOrDocumentRequestServiceImpl.sGetRequest(lobjRequest.getKey());
	}

	public InfoOrDocumentRequest receiveResponse(Response response)
		throws SessionExpiredException, BigBangException
	{
		InfoRequest lobjRequest;
		ReceiveReply lopRR;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = InfoRequest.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(response.requestId));

			lopRR = new ReceiveReply(lobjRequest.GetProcessID());
			lopRR.mstrNotes = response.notes;
			lopRR.mstrEmailID = response.emailId;
			if ( response.upgrades == null )
				lopRR.mobjDocOps = null;
			else
			{
				lopRR.mobjDocOps = new DocOps();
				lopRR.mobjDocOps.marrModify = null;
				lopRR.mobjDocOps.marrDelete = null;
				lopRR.mobjDocOps.marrCreate = new DocumentData[response.upgrades.length];
				for ( i = 0; i < response.upgrades.length; i++ )
				{
					lopRR.mobjDocOps.marrCreate[i] = new DocumentData();
					lopRR.mobjDocOps.marrCreate[i].mstrName = response.upgrades[i].name;
					lopRR.mobjDocOps.marrCreate[i].midOwnerType = PNProcess.GetInstance(Engine.getCurrentNameSpace(),
							lobjRequest.GetProcessID()).GetScript().GetDataType();
					lopRR.mobjDocOps.marrCreate[i].midOwnerId = null;
					lopRR.mobjDocOps.marrCreate[i].midDocType = UUID.fromString(response.upgrades[i].docTypeId);
					lopRR.mobjDocOps.marrCreate[i].mstrText = null;
					lopRR.mobjDocOps.marrCreate[i].mobjFile = FileServiceImpl.GetFileXferStorage().
							get(UUID.fromString(response.upgrades[i].storageId)).GetVarData();
					lopRR.mobjDocOps.marrCreate[i].marrInfo = null;
					lopRR.mobjDocOps.marrCreate[i].mobjPrevValues = null;
				}
			}

			lopRR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return InfoOrDocumentRequestServiceImpl.sGetRequest(lobjRequest.getKey());
	}

	public void cancelRequest(Cancellation cancellation)
		throws SessionExpiredException, BigBangException
	{
		InfoRequest lobjRequest;
		CancelRequest lopCR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = InfoRequest.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(cancellation.requestId));

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
