package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.ProcessData;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.Message.Attachment;
import bigBang.definitions.shared.Message.MsgAddress;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.interfaces.ConversationService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.ConversationSearchParameter;
import bigBang.library.shared.ConversationSortParameter;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MessageAddressData;
import com.premiumminds.BigBang.Jewel.Data.MessageAttachmentData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.CostCenter;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.MessageAddress;
import com.premiumminds.BigBang.Jewel.Objects.MessageAttachment;
import com.premiumminds.BigBang.Jewel.Objects.Template;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.CreateConversationBase;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.ReSendMessage;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.ReceiveMessage;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.ReopenProcess;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.SendMessage;
import com.premiumminds.BigBang.Jewel.SysObjects.HTMLConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.StorageConnector;

public class ConversationServiceImpl
	extends SearchServiceBase
	implements ConversationService
{
	private static final long serialVersionUID = 1L;
	
	

	/**
	 * Gets an attachment from the database, associated with a message exchange, and promoted to document
	 */
	private static Message.Attachment sGetDBDocument(MessageAttachment pobjAttachment, Timestamp pdtMsg)
		throws BigBangException
	{
		Document lobjDoc;
		Message.Attachment lobjResult;
		
		if (pobjAttachment.getAt(MessageAttachment.I.DOCUMENT) == null) {
			return null;
		}			

		try
		{
			lobjDoc = Document.GetInstance(Engine.getCurrentNameSpace(), (UUID)pobjAttachment.getAt(MessageAttachment.I.DOCUMENT));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if (lobjDoc == null) {
			return null;
		}
		
		lobjResult = new Message.Attachment();
		
		lobjResult.docId = lobjDoc.getKey().toString();
		lobjResult.name = lobjDoc.getLabel();
		lobjResult.docTypeId = ((UUID)lobjDoc.getAt(Document.I.TYPE)).toString();	
		lobjResult.promote = true;
		lobjResult.ownerId = lobjDoc.getAt(Document.I.OWNER).toString();

		return lobjResult;
	}

	/**
	 * This method
	 * @param foundAtts 
	 */
	private static Attachment[] sGetStgAttachments(String storageMailId, Timestamp msgDate, Map<String, String>bdAtts) 
		throws BigBangException {
		
		FileXfer[] files;
		try {
			files = StorageConnector.getAttachmentsAsFileXfer(storageMailId);
		} catch (BigBangJewelException e) {
			throw new BigBangException(e.getMessage(), e);
		}
		
		Set<String> existingAttsNames = bdAtts.keySet();

		Message.Attachment[] result = new Message.Attachment[files.length];

		for (int i=0; i<files.length; i++) {
			Message.Attachment tempAtt = new Message.Attachment();
			FileXfer tmpFile = files[i];
			tempAtt.id = null;
			tempAtt.docId = null;
			tempAtt.ownerId = null;
			tempAtt.name = tmpFile.getFileName();
			tempAtt.attachmentId = null;
			tempAtt.docTypeId = null;
			tempAtt.storageId = storageMailId;
			tempAtt.date = msgDate.toString().substring(0, 10);
			tempAtt.emailId = storageMailId;
			
			if (existingAttsNames.contains(tempAtt.name)) {
				tempAtt.docId = bdAtts.get(tempAtt.name);
			}
			
			result[i] = tempAtt;
		}
		
		return result;
	}

	/**
	 * Gets a Message from the database
	 */
	private static Message sGetDBMessage(com.premiumminds.BigBang.Jewel.Objects.Message pobjMsg, boolean pbFilterOwners)
		throws BigBangException
	{
		MessageAddress[] larrAddrs;
		MessageAttachment[] larrAtts;
		Message lobjResult;
		int i;
		//Map<String, String> foundAtts = null;

		try
		{
			larrAddrs = pobjMsg.GetAddresses();
			larrAtts = pobjMsg.GetAttachments();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Message();
		lobjResult.id = pobjMsg.getKey().toString();

		lobjResult.conversationId = ((UUID)pobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.OWNER)).toString();
		lobjResult.order = (Integer)pobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.NUMBER);
		lobjResult.direction = sGetDirection((UUID)pobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DIRECTION));
		lobjResult.kind = ( ((Boolean)pobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.ISEMAIL)) ?
				Message.Kind.EMAIL : Message.Kind.NOTE );
		lobjResult.date = ((Timestamp)pobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DATE)).toString().substring(0, 10);
		lobjResult.subject = (String)pobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.SUBJECT);
		lobjResult.text = pobjMsg.getText();
		lobjResult.emailId = (String)pobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.EMAILID);
		lobjResult.folderId = (String)pobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.FOLDERID);

		if ( larrAddrs == null || larrAddrs.length==0 )
			lobjResult.addresses = null;
		else
		{
			lobjResult.addresses = new Message.MsgAddress[larrAddrs.length];
			for ( i = 0; i < larrAddrs.length; i++ )
				lobjResult.addresses[i] = sGetAddress(larrAddrs[i].getKey(), pbFilterOwners);
		}

		if ( larrAtts == null )
			lobjResult.attachments = null;
		else
		{
			lobjResult.attachments = new Message.Attachment[larrAtts.length];
			
			for ( i = 0; i < larrAtts.length; i++ ) {
				lobjResult.attachments[i] = sGetDBAttachment(lobjResult.emailId, larrAtts[i].getKey(),
						(Timestamp)pobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DATE));
			}
		}

		return lobjResult;
	}

	
	/**
	 * Gets a Message from Google storage
	 */
	private static Message sGetStgMessage(com.premiumminds.BigBang.Jewel.Objects.Message bdMessage, boolean filterOwners)
		throws BigBangException {
		
		String storageId = bdMessage.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.EMAILID).toString();
		
		MessageData mailData = null;
		
		try {
			mailData = StorageConnector.getAsData(storageId);
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage(), e);
		}
		
		Message result = new Message();
		
		result.id = bdMessage.getKey().toString();

		result.conversationId = ((UUID)bdMessage.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.OWNER)).toString();
		result.order = (Integer)bdMessage.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.NUMBER);
		result.direction = sGetDirection((UUID)bdMessage.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DIRECTION));
		result.kind = ( ((Boolean)bdMessage.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.ISEMAIL)) ?
				Message.Kind.EMAIL : Message.Kind.NOTE );
		result.emailId = (String)bdMessage.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.EMAILID);
		result.date = ((Timestamp)bdMessage.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DATE)).toString().substring(0, 10);
		result.subject = mailData.mstrSubject;
		result.text = mailData.mstrBody;
		
		if (mailData.marrAddresses != null) {
			result.addresses = fillMessageAddresses(filterOwners, mailData);
		}
		
		if (mailData.marrAttachments != null) {
			result.attachments = fillMessageAttachments(filterOwners, mailData);
		}
		
		return result;
	}

	/**
	 * Fills an array with message attachments
	 */
	private static Attachment[] fillMessageAttachments(boolean filterOwners,
			MessageData mailData) throws BigBangException {
		
		Attachment[] attachments = new Attachment[mailData.marrAttachments.length];
		
		for (int i=0; i<mailData.marrAttachments.length; i++) {
			MessageAttachmentData tmp = mailData.marrAttachments[i];
			MessageAttachment messageAttachment = null;
			Message.Attachment attachment = null;
			
			try {
				messageAttachment = MessageAttachment.GetInstance(Engine.getCurrentNameSpace(), tmp.mid);
			} catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}
			
			attachment = new Message.Attachment();
			attachment.id = null;
			attachment.attachmentId = (String)messageAttachment.getAt(MessageAttachment.I.ATTACHMENTID);
			attachment.emailId = mailData.mstrEmailID;
			attachment.name = (String)messageAttachment.getAt(MessageAttachment.I.ATTACHMENTID);
			attachment.date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(mailData.mdtDate);
			
			attachments[i] = attachment;
		}
		
		return attachments;
	}

	/**
	 * Fills an array with message addresses
	 */
	protected static MsgAddress[] fillMessageAddresses(boolean filterOwners,
			MessageData mailData) throws BigBangException {
		MsgAddress[] addresses = new MsgAddress[mailData.marrAddresses.length];
		for (int i=0; i<mailData.marrAddresses.length; i++) {
			MessageAddressData tmp = mailData.marrAddresses[i];
			Message.MsgAddress addr = new Message.MsgAddress();
			Contact contact = null;
			ContactInfo contactInfo = null;
			MessageAddress msgAddr = null;
			try {
				msgAddr = MessageAddress.GetInstance(Engine.getCurrentNameSpace(), tmp.mid);
				contactInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(),
					(UUID)msgAddr.getAt(MessageAddress.I.CONTACTINFO));
				
				if ( (contactInfo != null) && (!filterOwners || Constants.UsageID_To.equals(tmp.midUsage))) {
					contact = ( contactInfo == null ? null : contactInfo.getOwner() );
				}
				
			} catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}
			
			addr.id = tmp.mid==null ? null : tmp.mid.toString();
			addr.address = tmp.mstrAddress==null ? null : tmp.mstrAddress;
			addr.usage = tmp.midUsage==null ? null : sGetUsage(tmp.midUsage);
			addr.userId = tmp.midUser==null ? null : tmp.midUser.toString(); 
			addr.contactInfoId = contactInfo.getKey()==null ? null : contactInfo.getKey().toString();
			addr.display = (String)msgAddr.getAt(MessageAddress.I.DISPLAYNAME);
			if (contact != null) {
				addr.ownerTypeId = contact.getOwnerType()==null ? null : contact.getOwnerType().toString();
				addr.ownerId = contact.getOwnerID()==null ? null : contact.getOwnerID().toString();
			}
			addresses[i] = addr;
		}
		return addresses;
	}

	/**
	 * Returns conversation direction (IN/OUT)
	 */
	public static ConversationStub.Direction sGetDirection(UUID pid)
	{
		if (Constants.MsgDir_Incoming.equals(pid) )
			return ConversationStub.Direction.INCOMING;

		if (Constants.MsgDir_Outgoing.equals(pid) )
			return ConversationStub.Direction.OUTGOING;

		return null;
	}

	/**
	 * Gets address usage according to its GUID
	 */
	public static Message.MsgAddress.Usage sGetUsage(UUID pid)
	{
		if ( Constants.UsageID_From.equals(pid) )
			return Message.MsgAddress.Usage.FROM;

		if ( Constants.UsageID_To.equals(pid) )
			return Message.MsgAddress.Usage.TO;

		if ( Constants.UsageID_ReplyTo.equals(pid) )
			return Message.MsgAddress.Usage.REPLYTO;

		if ( Constants.UsageID_CC.equals(pid) )
			return Message.MsgAddress.Usage.CC;

		if ( Constants.UsageID_BCC.equals(pid) )
			return Message.MsgAddress.Usage.BCC;

		return null;
	}

	/**
	 * Gets an address from the DB
	 */
	public static Message.MsgAddress sGetAddress(UUID pid, boolean pbFilterOwners)
		throws BigBangException
	{
		ContactInfo lobjInfo;
		Contact lobjContact;
		MessageAddress lobjAddr;
		UUID lidUsage;
		Message.MsgAddress lobjResult;

		lobjInfo = null;
		lobjContact = null;
		try
		{
			lobjAddr = MessageAddress.GetInstance(Engine.getCurrentNameSpace(), pid);
			lidUsage = (UUID)lobjAddr.getAt(MessageAddress.I.USAGE);
			if ( lobjAddr.getAt(MessageAddress.I.CONTACTINFO) != null )
				lobjInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(),
						(UUID)lobjAddr.getAt(MessageAddress.I.CONTACTINFO));
			if ( (lobjInfo != null) && (!pbFilterOwners || Constants.UsageID_To.equals(lidUsage)))
				lobjContact = ( lobjInfo == null ? null : lobjInfo.getOwner() );
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Message.MsgAddress();
		lobjResult.id = lobjAddr.getKey().toString();

		lobjResult.address = (String)lobjAddr.getAt(MessageAddress.I.ADDRESS);
		lobjResult.usage = sGetUsage(lidUsage);
		lobjResult.userId = ( lobjAddr.getAt(MessageAddress.I.USER) == null ? null :
				((UUID)lobjAddr.getAt(MessageAddress.I.USER)).toString() );
		lobjResult.contactInfoId = ( lobjInfo == null ? null : lobjInfo.getKey().toString() );
		lobjResult.display = (String)lobjAddr.getAt(MessageAddress.I.DISPLAYNAME);
		if ( lobjContact != null )
		{
			lobjResult.ownerTypeId = lobjContact.getOwnerType().toString();
			lobjResult.ownerId = lobjContact.getOwnerID().toString();
		}

		return lobjResult;
	}

	/**
	 * Gets an attachment from the DB
	 */
	public static Message.Attachment sGetDBAttachment(String pstrEmailId, UUID pid, Timestamp pdtMsg)
		throws BigBangException
	{
		MessageAttachment lobjAttachment;
		Attachment dbAttachment = null;

		// Gets the attachment's entity
		try
		{
			lobjAttachment = MessageAttachment.GetInstance(Engine.getCurrentNameSpace(), pid);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		dbAttachment = sGetDBDocument(lobjAttachment, pdtMsg);
			
		// If the attachment is null, it means there is no corresponding document in the DB.
		// In this case, a new attachment must be created here, to hold only the attachment info
		if (dbAttachment == null) {
			dbAttachment = new Message.Attachment();
			dbAttachment.promote = false;
		}
		
		// Fills the attachment with the possible info
		dbAttachment.attachmentId = (String) lobjAttachment.getAt(2);
		dbAttachment.date = pdtMsg.toString().substring(0, 10);
		dbAttachment.emailId = pstrEmailId;
		dbAttachment.id = pid.toString();
		dbAttachment.storageId = pstrEmailId;
		
		return dbAttachment;
	}

	/**
	 * Gets an message from the DB or storage
	 */
	public static Message sGetMessage(UUID pid, boolean pbFilterOwners)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Message lobjMsg;

		try
		{
			lobjMsg = com.premiumminds.BigBang.Jewel.Objects.Message.GetInstance(Engine.getCurrentNameSpace(), pid);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
		
		// Tries to get the message from the DB
		Message result = sGetDBMessage(lobjMsg, pbFilterOwners);

		// If unable to get from DB, tries to get from storage...
		// It should be able to get from the DB, for a conversation has messages.
		// This is only a "last try before an error"
		if ( result == null && lobjMsg.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.EMAILID) != null )
		{
			try
			{
				return sGetStgMessage(lobjMsg, pbFilterOwners);
			}
			catch (BigBangException e)
			{
			}
		}

		return result;
	}

	/**
	 * Gets a conversation from the DB
	 */
	public static Conversation sGetConversation(UUID pid)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Conversation lobjConv;
		com.premiumminds.BigBang.Jewel.Objects.Message[] larrMsgs;
		IProcess lobjProcess;
		IProcess lobjParent;
		IScript lobjScript;
		Conversation lobjResult;
		Timestamp ldtLast;
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
		{
			lobjResult.messages = null;
			lobjResult.lastDate = null;
		}
		else
		{
			ldtLast = new Timestamp(0);
			lobjResult.messages = new Message[larrMsgs.length];
			for ( i = 0; i < larrMsgs.length; i++ )
			{
				if ( ((Timestamp)larrMsgs[i].getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DATE)).after(ldtLast) )
					ldtLast = (Timestamp)larrMsgs[i].getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DATE);
				lobjResult.messages[i] = sGetMessage(larrMsgs[i].getKey(), true);
			}
			lobjResult.lastDate = ldtLast.toString().substring(0, 10);
		}

		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public TipifiedListItem[] getListItemsFilter(String listId, String filterId)
		throws SessionExpiredException, BigBangException
	{
		HashSet<UUID> lsetUsers;
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

		lsetUsers = getDelegates();

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
					new int[] {Jewel.Petri.Constants.FKScript_In_Process},
					new java.lang.Object[] {Constants.ProcID_Conversation},
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
				if ( !lsetUsers.contains(lobjProcess.GetManagerID()) )
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

	public String getForPrinting(String id)
		throws SessionExpiredException, BigBangException
	{
		ConversationData lobjData;
		com.premiumminds.BigBang.Jewel.Objects.Conversation lobjConv;
		com.premiumminds.BigBang.Jewel.Objects.Message[] larrMsgs;
		MessageAddress[] larrAddrs;
		StringBuilder lstrBuilder;
		int i, j;
		String[] larrContent;
		FileXfer lobjFile;
		UUID lidAux;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjConv = com.premiumminds.BigBang.Jewel.Objects.Conversation.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(id));
			lobjData = new ConversationData();
			lobjData.FromObject(lobjConv);

			larrMsgs = lobjConv.GetCurrentMessages();
			lobjData.marrMessages = new MessageData[larrMsgs.length];
			for ( i = 0; i < larrMsgs.length; i++ )
			{
				lobjData.marrMessages[i] = new MessageData();
				lobjData.marrMessages[i].FromObject(larrMsgs[i]);

				larrAddrs = larrMsgs[i].GetAddresses();
				lobjData.marrMessages[i].marrAddresses = new MessageAddressData[larrAddrs.length];
				for ( j = 0; j < larrAddrs.length; j++ )
				{
					lobjData.marrMessages[i].marrAddresses[j] = new MessageAddressData();
					lobjData.marrMessages[i].marrAddresses[j].FromObject(larrAddrs[j]);
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lstrBuilder = new StringBuilder();
		lobjData.Describe(lstrBuilder, "<br />");

		larrContent = new String[] {lstrBuilder.toString()};

		try
		{
			lobjFile = HTMLConnector.buildHTML(larrContent);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

    	lidAux = UUID.randomUUID();
    	FileServiceImpl.GetFileXferStorage().put(lidAux, lobjFile);

		return lidAux.toString();
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
		lopCCB.mobjData.mstrSubject = conversation.subject;
		lopCCB.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCCB.mobjData.midProcess = null;
		lopCCB.mobjData.midStartDir = ( ConversationStub.Direction.OUTGOING.equals(conversation.startDir) ?
				Constants.MsgDir_Outgoing : Constants.MsgDir_Incoming ); // On NULL, default is INCOMING
		lopCCB.mobjData.midPendingDir = ( conversation.replylimit == null ? null : ( ConversationStub.Direction.OUTGOING.equals(conversation.startDir) ?
				Constants.MsgDir_Incoming : Constants.MsgDir_Outgoing ) );
		lopCCB.mobjData.mdtDueDate = ldtLimit;

		lopCCB.mobjData.marrMessages = new MessageData[1];
		lopCCB.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], lidParentType, lidParentID,
				lopCCB.mobjData.midStartDir);

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
					(ConversationStub.Direction.INCOMING.equals(conversation.pendingDir) ?
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

	public Message getEmpty()
		throws SessionExpiredException, BigBangException
	{
		Message lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjResult = new Message();
		lobjResult.direction = ConversationStub.Direction.OUTGOING;
		lobjResult.kind = Message.Kind.EMAIL;
		lobjResult.text = getSignature() + "<br><bigbang:original:bb>";
		lobjResult.attachments = new Message.Attachment[0];
		lobjResult.addresses = new Message.MsgAddress[0];
		lobjResult.folderId = null;

		return lobjResult;
	}
	
	public Message getCommonSend(String messageId, String typeString) 
			throws SessionExpiredException, BigBangException {
		
		Message lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();
			
		lobjResult = sGetMessage(UUID.fromString(messageId), false);
		lobjResult.id = null;
		lobjResult.order = null;
		lobjResult.direction = ConversationStub.Direction.OUTGOING;
		lobjResult.date = null;
		if (typeString != null) {
			lobjResult.subject = (lobjResult.subject == null ? 
									typeString : 
									(lobjResult.subject.startsWith(typeString) ? 
											lobjResult.subject : 
											typeString + lobjResult.subject));
		}
		lobjResult.text = typeString==null ? lobjResult.text : getSignature() + "<br><bigbang:original:bb><br>-------- Mensagem Original --------<br>" + lobjResult.text;
		lobjResult.emailId = null;
		lobjResult.folderId = null;

		return lobjResult;
	}

	public Message getForReply(String messageId)
		throws SessionExpiredException, BigBangException {
		
		Message lobjResult;
		ArrayList<Message.MsgAddress> larrAddrs;
		Message.MsgAddress.Usage lobjUsage;
		int i;

		lobjResult = getCommonSend(messageId, "Re:");
		lobjResult.attachments = new Message.Attachment[0];
		
		if ( lobjResult.addresses != null ) {
			larrAddrs = new ArrayList<Message.MsgAddress>();
			lobjUsage = Message.MsgAddress.Usage.FROM;
			for ( i = 0; i < lobjResult.addresses.length; i++ )
			{
				if ( (lobjResult.addresses[i] != null) && Message.MsgAddress.Usage.REPLYTO.equals(lobjResult.addresses[i].usage) )
				{
					lobjUsage = Message.MsgAddress.Usage.REPLYTO;
					break;
				}
			}
			for ( i = 0; i < lobjResult.addresses.length; i++ )
			{
				if ( (lobjResult.addresses[i] != null) && lobjUsage.equals(lobjResult.addresses[i].usage) )
				{
					lobjResult.addresses[i].usage = Message.MsgAddress.Usage.TO;
					larrAddrs.add(lobjResult.addresses[i]);
				}
			}
			lobjResult.addresses = larrAddrs.toArray(new Message.MsgAddress[larrAddrs.size()]);
		} else {
			lobjResult.addresses = new Message.MsgAddress[0];
		}

		return lobjResult;
	}

	public Message getForReplyAll(String messageId)
		throws SessionExpiredException, BigBangException {
		
		Message lobjResult;
		ArrayList<Message.MsgAddress> larrAddrs;
		Message.MsgAddress.Usage lobjUsage;
		String lstrEmail;
		int i;

		try {
			lstrEmail = MailConnector.getUserEmail();
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage(), e);
		}
		
		lobjResult = getCommonSend(messageId, "Re:");
		lobjResult.attachments = new Message.Attachment[0];
		
		if ( lobjResult.addresses != null ) {
			larrAddrs = new ArrayList<Message.MsgAddress>();
			lobjUsage = Message.MsgAddress.Usage.FROM;
			for ( i = 0; i < lobjResult.addresses.length; i++ )
			{
				if ( (lobjResult.addresses[i] != null) && Message.MsgAddress.Usage.REPLYTO.equals(lobjResult.addresses[i].usage) )
				{
					lobjUsage = Message.MsgAddress.Usage.REPLYTO;
					break;
				}
			}
			for ( i = 0; i < lobjResult.addresses.length; i++ )
			{
				if ( lobjResult.addresses[i] != null )
				{
					if ( (lstrEmail != null) && lstrEmail.equalsIgnoreCase(lobjResult.addresses[i].address) )
						continue;

					if ( Message.MsgAddress.Usage.CC.equals(lobjResult.addresses[i].usage) ||
							Message.MsgAddress.Usage.BCC.equals(lobjResult.addresses[i].usage) ||
							Message.MsgAddress.Usage.TO.equals(lobjResult.addresses[i].usage) )
						larrAddrs.add(lobjResult.addresses[i]);
					if ( lobjUsage.equals(lobjResult.addresses[i].usage) )
					{
						lobjResult.addresses[i].usage = Message.MsgAddress.Usage.TO;
						larrAddrs.add(lobjResult.addresses[i]);
					}
				}
			}
			lobjResult.addresses = larrAddrs.toArray(new Message.MsgAddress[larrAddrs.size()]);
		} else {
			lobjResult.addresses = new Message.MsgAddress[0];
		}

		return lobjResult;
	}

	public Message getForForward(String messageId)
		throws SessionExpiredException, BigBangException
	{
		Message lobjResult = getCommonSend(messageId, "Fw:");
		
		lobjResult.addresses = new Message.MsgAddress[0];
		if ( lobjResult.attachments == null )
			lobjResult.attachments = new Message.Attachment[0];

		return lobjResult;
	}

	public Message getForRepeat(String messageId)
		throws SessionExpiredException, BigBangException
	{
		Message lobjResult = getCommonSend(messageId, null);
		
		lobjResult.text = lobjResult.text + "<bigbang:signature:bb><bigbang:original:bb>";
		
		if ( lobjResult.attachments == null )
			lobjResult.addresses = new Message.MsgAddress[0];
		if ( lobjResult.attachments == null )
			lobjResult.attachments = new Message.Attachment[0];

		return lobjResult;
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
			lopSM.mobjData = MessageBridge.clientToServer(message, lobjConv.getParentContainerType(), lobjConv.getParentContainer(),
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
			lopRSM.mobjData = MessageBridge.clientToServer(message, lobjConv.getParentContainerType(), lobjConv.getParentContainer(),
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
			lopRM.mobjData = MessageBridge.clientToServer(message, lobjConv.getParentContainerType(), lobjConv.getParentContainer(),
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

	public void reopenConversation(String conversationId, String directionId, Integer replyLimit)
		throws SessionExpiredException, BigBangException
	{
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		com.premiumminds.BigBang.Jewel.Objects.Conversation lobjConv;
		ReopenProcess lopRP;

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

		if ( replyLimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, replyLimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		lopRP = new ReopenProcess(lobjConv.GetProcessID());
		lopRP.midDir = UUID.fromString(directionId);
		lopRP.mdtLimit = ldtLimit;

		try
		{
			lopRP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Conversation;
	}

	protected String[] getColumns()
	{
		IEntity lrefMessages;

		try
		{
			lrefMessages = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Message));
		}
		catch (Throwable e)
		{
			lrefMessages = null;
		}

		return new String[] {"[:Process:Data]", "[:Process:Script:Data Class]", "[:Request Type]", "[:Request Type:Type]",
				"[:Subject]", "[:Pending Direction]", lrefMessages == null ? "NULL" :
					"(SELECT MAX([Date]) FROM (" + lrefMessages.SQLForSelectAll() + ") [AuxMsgs] WHERE [Conversation] = [Aux].[PK])"};
	}

	protected void filterAgentUser(StringBuilder pstrBuffer, UUID pidMediator)
		throws BigBangException
	{
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		ConversationSearchParameter lParam;
		String lstrAux;
		IEntity lrefProcesses;

		if ( !(pParam instanceof ConversationSearchParameter) )
			return false;
		lParam = (ConversationSearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Subject] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Request Type:Type] LIKE N'%").append(lstrAux).append("%')");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND ([:Process:Parent] IN (SELECT [PK] FROM (");
			try
			{
				lrefProcesses = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Jewel.Petri.Constants.ObjID_PNProcess));
				pstrBuffer.append(lrefProcesses.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxProcs] WHERE [:Data] = '").append(lParam.ownerId).append("'))");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		ConversationSortParameter lParam;

		if ( !(pParam instanceof ConversationSortParameter) )
			return false;
		lParam = (ConversationSortParameter)pParam;

		if ( lParam.field == ConversationSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == ConversationSortParameter.SortableField.SUBJECT )
			pstrBuffer.append("[:Subject]");

		if ( lParam.field == ConversationSortParameter.SortableField.TYPE )
			pstrBuffer.append("[:Request Type:Type]");

		if ( lParam.field == ConversationSortParameter.SortableField.PENDINGDATE )
			pstrBuffer.append("[:Due Date]");

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		ConversationStub lobjResult;

		lobjResult = new ConversationStub();

		lobjResult.id = pid.toString();
		lobjResult.parentDataObjectId = ((UUID)parrValues[0]).toString();
		lobjResult.parentDataTypeId = ((UUID)parrValues[1]).toString();
		lobjResult.requestTypeId = ((UUID)parrValues[2]).toString();
		lobjResult.requestTypeLabel = (String)parrValues[3];
		lobjResult.subject = (String)parrValues[4];
		lobjResult.lastDate = parrValues[6] == null ? null : ((Timestamp)parrValues[6]).toString().substring(0, 10);
		lobjResult.pendingDir = sGetDirection((UUID)parrValues[5]);

		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		ConversationSearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof ConversationSearchParameter) )
				continue;
			lParam = (ConversationSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Subject] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Subject]) ELSE ");

			pstrBuffer.append("CASE WHEN [:Request Type:Type] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', [:Request Type:Type]) ELSE ");

			pstrBuffer.append("0 END END");
		}

		return lbFound;
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

		if ( Constants.ObjID_Client.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Client.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ObjID_QuoteRequest.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ObjID_Negotiation.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Negotiation.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ObjID_Policy.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Policy.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ObjID_SubPolicy.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ObjID_Receipt.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Receipt.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ObjID_Casualty.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Casualty.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ObjID_SubCasualty.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.SubCasualty.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ObjID_Expense.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Expense.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ObjID_Assessment.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.Assessment.CreateConversation(lobjData.GetProcessID());

		if ( Constants.ObjID_MedicalFile.equals(pidParentType) )
			return new com.premiumminds.BigBang.Jewel.Operations.MedicalFile.CreateConversation(lobjData.GetProcessID());

		return null;
	}

	private HashSet<UUID> getDelegates()
		throws BigBangException
	{
		HashSet<UUID> lsetResult;
		IEntity lrefDecoration;
		MasterDB ldb;
        ResultSet lrsDecos;
        UserDecoration lobjDeco;

		lsetResult = new HashSet<UUID>();
		lsetResult.add(Engine.getCurrentUser());

		try
		{
			lrefDecoration = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsDecos = lrefDecoration.SelectByMembers(ldb, new int[] {UserDecoration.I.SURROGATE},
					new java.lang.Object[] {Engine.getCurrentUser()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			while ( lrsDecos.next() )
			{
				lobjDeco = UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrsDecos);
				lsetResult.add(lobjDeco.getBaseUser().getKey());
			}
		}
		catch (Throwable e)
		{
			try { lrsDecos.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		} 

		try
		{
			lrsDecos.close();
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

		return lsetResult;
	}

	private String getSignature()
		throws SessionExpiredException, BigBangException
	{
		Template lobjSig;
		FileXfer lobjFile;
		String lstrSig;
		User lobjCurrent;
		UserDecoration lobjDeco;
		CostCenter lobjCenter;

		try
		{
			lobjSig = Template.GetInstance(Engine.getCurrentNameSpace(), Constants.TID_Signature);
			lobjFile = lobjSig.getFile();
			if ( lobjFile == null )
				return "";

			lstrSig = new String(lobjFile.getData(), "UTF-8");

			lobjCurrent = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser());
			lobjDeco = UserDecoration.GetByUserID(Engine.getCurrentNameSpace(), lobjCurrent.getKey());
			lobjCenter = (lobjDeco.getAt(UserDecoration.I.COSTCENTER) == null ? null :
					CostCenter.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjDeco.getAt(UserDecoration.I.COSTCENTER)));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lstrSig = lstrSig.replaceFirst("\\{\\{FullName\\}\\}", lobjCurrent.getFullName());
		lstrSig = lstrSig.replaceFirst("\\{\\{Department\\}\\}", (lobjDeco.getAt(UserDecoration.I.TITLE) == null ? (lobjCenter == null ? "" :
				(String)lobjCenter.getAt(CostCenter.I.DISPLAYNAME)) : (String)lobjDeco.getAt(UserDecoration.I.TITLE)));
		lstrSig = lstrSig.replaceFirst("\\{\\{DirectPhone\\}\\}", (lobjDeco.getAt(UserDecoration.I.PHONE) == null ? "217 220 100" :
				(String)lobjDeco.getAt(UserDecoration.I.PHONE)));
		lstrSig = lstrSig.replaceFirst("\\{\\{Email\\}\\}", (lobjDeco.getAt(UserDecoration.I.EMAIL) == null ? "" :
				(String)lobjDeco.getAt(UserDecoration.I.EMAIL)));

		return "<bigbang:signature:bb>" + lstrSig;
	}
}
