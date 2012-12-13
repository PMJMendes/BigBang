package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.ProcessData;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.interfaces.ConversationService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Objects.MessageAddress;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.CreateConversationBase;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.ReSendMessage;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.ReceiveMessage;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.SendMessage;

public class ConversationServiceImpl
	extends EngineImplementor
	implements ConversationService
{
	private static final long serialVersionUID = 1L;

	public static Conversation.Direction sGetDirection(UUID pid)
	{
		if (Constants.MsgDir_Incoming.equals(pid) )
			return Conversation.Direction.INCOMING;

		if (Constants.MsgDir_Outgoing.equals(pid) )
			return Conversation.Direction.OUTGOING;

		return null;
	}

	public static Message.MsgAddress.Usage sGetUsage(UUID pid)
	{
		if (Constants.UsageID_From.equals(pid) )
			return Message.MsgAddress.Usage.FROM;

		if (Constants.UsageID_To.equals(pid) )
			return Message.MsgAddress.Usage.TO;

		if (Constants.UsageID_ReplyTo.equals(pid) )
			return Message.MsgAddress.Usage.REPLYTO;

		if (Constants.UsageID_CC.equals(pid) )
			return Message.MsgAddress.Usage.CC;

		if (Constants.UsageID_BCC.equals(pid) )
			return Message.MsgAddress.Usage.BCC;

		return null;
	}

	public static Message.MsgAddress sGetAddress(UUID pid)
		throws BigBangException
	{
		MessageAddress lobjAddr;
		Message.MsgAddress lobjResult;

		try
		{
			lobjAddr = MessageAddress.GetInstance(Engine.getCurrentNameSpace(), pid);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Message.MsgAddress();
		lobjResult.id = lobjAddr.getKey().toString();

		lobjResult.address = (String)lobjAddr.getAt(MessageAddress.I.ADDRESS);
		lobjResult.usage = sGetUsage((UUID)lobjAddr.getAt(MessageAddress.I.USAGE));
		lobjResult.userId = ( lobjAddr.getAt(MessageAddress.I.USER) == null ? null :
				((UUID)lobjAddr.getAt(MessageAddress.I.USER)).toString() );
		lobjResult.contactInfoId = (lobjAddr.getAt(MessageAddress.I.CONTACTINFO) == null ? null:
				((UUID)lobjAddr.getAt(MessageAddress.I.CONTACTINFO)).toString() );
		lobjResult.display = (String)lobjAddr.getAt(MessageAddress.I.DISPLAYNAME);

		return lobjResult;
	}

	public static Message sGetMessage(UUID pid)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Message lobjMsg;
		MessageAddress[] larrAddrs;
		Message lobjResult;
		int i;

		try
		{
			lobjMsg = com.premiumminds.BigBang.Jewel.Objects.Message.GetInstance(Engine.getCurrentNameSpace(), pid);
			larrAddrs = lobjMsg.GetAddresses();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Message();
		lobjResult.id = lobjMsg.getKey().toString();

		lobjResult.conversationId = ((UUID)lobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.OWNER)).toString();
		lobjResult.order = (Integer)lobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.NUMBER);
		lobjResult.direction = sGetDirection((UUID)lobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DIRECTION));
		lobjResult.kind = ( ((Boolean)lobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.ISEMAIL)) ?
				Message.Kind.EMAIL : Message.Kind.NOTE );
		lobjResult.date = ((Timestamp)lobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DATE)).toString().substring(0, 10);
		lobjResult.subject = (String)lobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.SUBJECT);
		lobjResult.text = lobjMsg.getText();

		if ( larrAddrs == null )
			lobjResult.addresses = null;
		else
		{
			lobjResult.addresses = new Message.MsgAddress[larrAddrs.length];
			for ( i = 0; i < larrAddrs.length; i++ )
				lobjResult.addresses[i] = sGetAddress(larrAddrs[i].getKey());
		}

		return lobjResult;
	}

	public static Conversation sGetConversation(UUID pid)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Conversation lobjConv;
		com.premiumminds.BigBang.Jewel.Objects.Message[] larrMsgs;
		IProcess lobjProcess;
		IProcess lobjParent;
		IScript lobjScript;
		Conversation lobjResult;
		int i;

		try
		{
			lobjConv = com.premiumminds.BigBang.Jewel.Objects.Conversation.GetInstance(Engine.getCurrentNameSpace(), pid);
			larrMsgs = lobjConv.GetCurrentMessages();
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjConv.GetProcessID());
			lobjParent = lobjProcess.GetParent();
			lobjScript = lobjParent.GetScript();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Conversation();
		lobjResult.id = lobjConv.getKey().toString();

		lobjResult.parentDataObjectId = lobjParent.GetDataKey().toString();
		lobjResult.parentDataTypeId = lobjScript.GetDataType().toString();
		lobjResult.managerId = lobjProcess.GetManagerID().toString();
		lobjResult.requestTypeId = ((UUID)lobjConv.getAt(com.premiumminds.BigBang.Jewel.Objects.Conversation.I.TYPE)).toString();
		lobjResult.subject = (String)lobjConv.getAt(com.premiumminds.BigBang.Jewel.Objects.Conversation.I.SUBJECT);
		lobjResult.startDir = sGetDirection((UUID)lobjConv.getAt(com.premiumminds.BigBang.Jewel.Objects.Conversation.I.STARTDIRECTION));
		lobjResult.pendingDir = sGetDirection((UUID)lobjConv.getAt(com.premiumminds.BigBang.Jewel.Objects.Conversation.I.PENDINGDIRECTION));
		lobjResult.replylimit = ( lobjConv.getAt(com.premiumminds.BigBang.Jewel.Objects.Conversation.I.DUEDATE) != null ?
				((int)((((Timestamp)lobjConv.getAt(com.premiumminds.BigBang.Jewel.Objects.Conversation.I.DUEDATE)).getTime() -
				(new Timestamp(new java.util.Date().getTime())).getTime()) / 86400000L)) : null );

		if ( larrMsgs == null )
			lobjResult.messages = null;
		else
		{
			lobjResult.messages = new Message[larrMsgs.length];
			for ( i = 0; i < larrMsgs.length; i++ )
				lobjResult.messages[i] = sGetMessage(larrMsgs[i].getKey());
		}

		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public TipifiedListItem[] getListItemsFilter(String listId, String filterId)
		throws SessionExpiredException, BigBangException
	{
		IProcess lobjParent;
		IEntity lrefProcess;
        MasterDB ldb;
        ResultSet lrsProcesses;
		ArrayList<TipifiedListItem> larrAux;
        IProcess lobjProcess;
        TipifiedListItem lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( !Constants.ObjID_Conversation.equals(UUID.fromString(listId)) )
			throw new BigBangException("Erro: Lista inválida para o espaço de trabalho.");

		lobjParent = BigBangProcessServiceImpl.sGetProcessFromDataObject(UUID.fromString(filterId));

		try
		{
			lrefProcess = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Jewel.Petri.Constants.ObjID_PNProcess)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsProcesses = lrefProcess.SelectByMembers(ldb,
					new int[] {Jewel.Petri.Constants.FKScript_In_Process, Jewel.Petri.Constants.FKParent_In_Process},
					new java.lang.Object[] {Constants.ProcID_Conversation, lobjParent.getKey()},
					new int[] {com.premiumminds.BigBang.Jewel.Objects.Conversation.I.SUBJECT});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		larrAux = new ArrayList<TipifiedListItem>();

		try
		{
			while ( lrsProcesses.next() )
			{
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lrsProcesses);
				if ( !lobjProcess.IsRunning() )
					continue;

				lobjResult = new TipifiedListItem();
				lobjResult.id = lobjProcess.GetDataKey().toString();
				lobjResult.value = lobjProcess.GetData().getLabel();
				larrAux.add(lobjResult);
			}
		}
		catch (Throwable e)
		{
			try { lrsProcesses.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsProcesses.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new TipifiedListItem[larrAux.size()]);
	}

	public Conversation getConversation(String id)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetConversation(UUID.fromString(id));
	}

	public Conversation createFromEmail(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		UUID lidParentType;
		UUID lidParentID;
		CreateConversationBase lopCCB;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( conversation.replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, conversation.replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		lidParentType = UUID.fromString(conversation.parentDataTypeId);
		lidParentID = UUID.fromString(conversation.parentDataObjectId);

		lopCCB = getOperation(lidParentType, lidParentID);

		lopCCB.mobjData = new ConversationData();
		lopCCB.mobjData.mid = null;
		lopCCB.mobjData.mstrSubject = conversation.messages[0].subject;
		lopCCB.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCCB.mobjData.midProcess = null;
		lopCCB.mobjData.midStartDir = Constants.MsgDir_Incoming;
		lopCCB.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Outgoing );
		lopCCB.mobjData.mdtDueDate = ldtLimit;

		lopCCB.mobjData.marrMessages = new MessageData[1];
		lopCCB.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], lidParentType, lidParentID,
				Constants.MsgDir_Incoming);

		try
		{
			lopCCB.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetConversation(lopCCB.mobjData.mid);
	}

	public Conversation saveConversation(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		ManageData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( conversation.replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, conversation.replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		try
		{
			lopMD = new ManageData(UUID.fromString(conversation.processId));
			lopMD.mobjData = new ConversationData();

			lopMD.mobjData.mid = UUID.fromString(conversation.id);

			lopMD.mobjData.mstrSubject = conversation.subject;
			lopMD.mobjData.midType = UUID.fromString(conversation.requestTypeId);
			lopMD.mobjData.midPendingDir = ( conversation.pendingDir == null ? null :
					(Conversation.Direction.INCOMING.equals(conversation.pendingDir) ?
					Constants.MsgDir_Incoming : Constants.MsgDir_Outgoing) );
			lopMD.mobjData.mdtDueDate = ldtLimit;

			lopMD.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetConversation(lopMD.mobjData.mid);
	}

	public Conversation sendMessage(Message message, Integer replylimit)
		throws SessionExpiredException, BigBangException
	{
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		com.premiumminds.BigBang.Jewel.Objects.Conversation lobjConv;
		SendMessage lopSM;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjConv = com.premiumminds.BigBang.Jewel.Objects.Conversation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(message.conversationId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		lopSM = new SendMessage(lobjConv.GetProcessID());
		lopSM.mdtDueDate = ldtLimit;

		try
		{
			lopSM.mobjData = MessageBridge.clientToServer(message, lobjConv.getParentType(), lobjConv.getParentID(),
					Constants.MsgDir_Outgoing);

			lopSM.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetConversation(lobjConv.getKey());
	}

	public Conversation repeatMessage(Message message, Integer replylimit)
		throws SessionExpiredException, BigBangException
	{
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		com.premiumminds.BigBang.Jewel.Objects.Conversation lobjConv;
		ReSendMessage lopRSM;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjConv = com.premiumminds.BigBang.Jewel.Objects.Conversation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(message.conversationId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		lopRSM = new ReSendMessage(lobjConv.GetProcessID());
		lopRSM.mdtDueDate = ldtLimit;

		try
		{
			lopRSM.mobjData = MessageBridge.clientToServer(message, lobjConv.getParentType(), lobjConv.getParentID(),
					Constants.MsgDir_Outgoing);

			lopRSM.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetConversation(lobjConv.getKey());
	}

	public Conversation receiveMessage(Message message, Integer replylimit)
		throws SessionExpiredException, BigBangException
	{
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		com.premiumminds.BigBang.Jewel.Objects.Conversation lobjConv;
		ReceiveMessage lopRM;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjConv = com.premiumminds.BigBang.Jewel.Objects.Conversation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(message.conversationId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		lopRM = new ReceiveMessage(lobjConv.GetProcessID());
		lopRM.mdtDueDate = ldtLimit;

		try
		{
			lopRM.mobjData = MessageBridge.clientToServer(message, lobjConv.getParentType(), lobjConv.getParentID(),
					Constants.MsgDir_Incoming);

			lopRM.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetConversation(lobjConv.getKey());
	}

	public void closeConversation(String conversationId, String motiveId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Conversation lobjConv;
		CloseProcess lopCP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjConv = com.premiumminds.BigBang.Jewel.Objects.Conversation.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversationId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCP = new CloseProcess(lobjConv.GetProcessID());
		lopCP.midMotive = UUID.fromString(motiveId);

		try
		{
			lopCP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	private CreateConversationBase getOperation(UUID pidParentType, UUID pidParentID)
		throws BigBangException
	{
		ProcessData lobjData;

		try
		{
			lobjData = (ProcessData)Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), pidParentType),
					pidParentID);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( Constants.ProcID_Client.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Client.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ProcID_QuoteRequest.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ProcID_Negotiation.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Negotiation.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ProcID_Policy.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Policy.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ProcID_SubPolicy.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ProcID_Casualty.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Casualty.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ProcID_SubCasualty.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ProcID_Expense.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Expense.CreateConversation(lobjData.GetProcessID());

		return null;
	}
}
