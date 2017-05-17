package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
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
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

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
	 * Gets a Message from the database
	 */
	private static Message sGetDBMessage(com.premiumminds.BigBang.Jewel.Objects.Message pobjMsg, boolean pbFilterOwners)
		throws BigBangException
	{
		MessageAddress[] larrAddrs;
		MessageAttachment[] larrAtts;
		Message lobjResult;
		int i;

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
		
		mailData.mdtDate = (Timestamp)bdMessage.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DATE);
		
		if (mailData.marrAttachments != null) {
			result.attachments = fillMessageAttachments(filterOwners, mailData);
		}
		
		if (mailData.marrAddresses != null) {
			result.addresses = fillMessageAddresses(filterOwners, mailData);
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
			if (attachment.attachmentId == null || attachment.attachmentId.length()==0) {
				attachment.attachmentId = tmp.mstrAttId;
			}
			
			attachment.emailId = mailData.mstrEmailID;
			attachment.name = attachment.attachmentId;
			attachment.date = mailData.mdtDate==null ? null : (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(mailData.mdtDate);
			
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
			throws BigBangException {
		com.premiumminds.BigBang.Jewel.Objects.Message lobjMsg;
		Message result = null;

		try {
			lobjMsg = com.premiumminds.BigBang.Jewel.Objects.Message
					.GetInstance(Engine.getCurrentNameSpace(), pid);
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage(), e);
		}

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		Date date = null;
		Timestamp cutTimestamp = null;
		try {
			date = formatter
					.parse(Constants.GoogleAppsConstants.MAIL_MIGRATION_DATE);
			cutTimestamp = new Timestamp(date.getTime());
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage(), e);
		}

		if (cutTimestamp != null
				&& ((Timestamp) lobjMsg
						.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.DATE))
						.before(cutTimestamp)
				&& lobjMsg
						.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.EMAILID) != null) {
			result = sGetStgMessage(lobjMsg, pbFilterOwners);
		}

		if (result == null) {

			// Tries to get the message from the DB
			result = sGetDBMessage(lobjMsg, pbFilterOwners);

			// If unable to get from DB, tries to get from storage, even though it's a "newer" message.
			// It should be able to get from the DB, for a conversation has
			// messages.
			// This is only a "last try before an error"
			if (result == null
					&& lobjMsg
							.getAt(com.premiumminds.BigBang.Jewel.Objects.Message.I.EMAILID) != null) {
				result = sGetStgMessage(lobjMsg, pbFilterOwners);
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
			throw new BigBangException(e.getMessage() + " 488 ", e);
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

	/**
	 * Gets the "selectable" conversations' list to display in a page.
	 */
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

	/**
	 * Gets aa conversation with a given id
	 */
	public Conversation getConversation(String id)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetConversation(UUID.fromString(id));
	}

	/**
	 * Gets a conversation and corresponding messages to print
	 */
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

	/**
	 * Creates a new conversation from an email
	 */
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
		
		javax.mail.Message storedMessage = null;
		try {
			storedMessage = MailConnector.getStoredMessage();
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage() + " 748 ", e);
		}
		lopCCB.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], lidParentType, lidParentID,
				lopCCB.mobjData.midStartDir, storedMessage);

		try
		{
			lopCCB.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 759 ", e);
		}

		return sGetConversation(lopCCB.mobjData.mid);
	}

	/**
	 * saves an edited conversation
	 */
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

	/**
	 * Gets an empty conversation
	 */
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
	
	/**
	 * Gets an attachment from the database, associated with a message exchange, and promoted to document
	 */
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
					Constants.MsgDir_Outgoing, null);

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
					Constants.MsgDir_Outgoing, null);

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
			throw new BigBangException(e.getMessage() + " 1104 ", e);
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
			javax.mail.Message storedMessage = MailConnector.getStoredMessage();
			lopRM.mobjData = MessageBridge.clientToServer(message, lobjConv.getParentContainerType(), lobjConv.getParentContainer(),
					Constants.MsgDir_Incoming, storedMessage);

			lopRM.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage() + " 1131 ", e);
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
		
		/*String sigB64 = "";
		// Crédite-egs
		if (Utils.getCurrency().equals("€")) {
			sigB64 = "data:image/png;base64,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";
		} else {
			// Angola
			sigB64 ="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOoAAACHCAYAAAAY/sXKAAAV50lEQVR4AezToQEAIAwDQfYfOmEGDK04cfrVnzbAct9CgFHBqIBRAaOCUQGjAkYFowJGBaMCRgWMCkYFjAoYFYwKGBWMChgVMCoYFTAqYFQwKmBUwKhgVMCo9FEw6gRzXvbOPL6Gs33j0+VtaasLLdVaKpRaSrWqS+3Som+rKFq6UEtQBAlJROwEsZNYiCUWKmgQezRv7BKxECKSiGgssSSRRSzE9ZvrOcl8epJzzidNqf7S+4/rcyYzzzMzJ2e+c9/3s9zP3oOn4bN4O/oNW4K2DtPRtL0nGnw9Go3bjUGrLlPQZ8hieC/ajhNRCRBoBdS/SQJoyvUMuylzN+Hjr0bihard8Wrt3ihb1xEVPuqPN3SVfd8Rpd/tg1Lv/IyStXrhFV38fPntnnjjw/6YsWArBFYBVfSQAN0dGoVPvx2n4Cyng1m5/kBUa+xCKUiL1+iBmvZu6OW2EFPnbcbSNbvht3oXvGYF4tteM/FKzV46wL3x2nt91OfaTWEC7IOSgCqAHjsZj/daeODlmj3xZj1nBWb1Jq5KlRsMVOA6evjhWlLaeJa3pWVr96C0boErfjJAWdpGbccA97ME1r8iAVUg7djbBy9WcyCQBNNM5er2Q61PB+uAphqA5je2feczd0KvA+uEEjV6IuLU7xBYBVTRn4T0+MlztHyMOXMsqJnouhLigrmuUOI5qjZy0TUIz1buiv3h0QKrgCrKL0STZm9EsSrd8JYOUG5AKTYU9XRd+BfjS2hsVGKsy3MSWF4z5Xq6ncAqoIpsClr3Qb5s9LFoRanyH/RDO4fphuX7K6CejrnAlmHj3JV0N9j+G09bVlUkoAqk/YctMcCxJMapNfRPls1RTNzFAoIF7VT0eZSqbVxPvRyef6sb0tIzLb0ARAKqQLpmYyhequ5gFVJCxNbdlBSTa0o1bDMaKwL2FhjU8d4baKHNrsMXReiR2NznFAmoAul9vXuELbvW3F2qzPt9McE70LCmXj6BKFLxJ/y6OQwFaUzKzLylcQBE7mvyZcBuHgE1twRUiUsHzueABauQvtVwEC2fmctbokYPvFnfGXW/GPYnGpWgdDUp1Z9dO1Uamnf5sKumQZvRucEXCagC6b279zTGhbas6et1+mDJ6t1moBap2Jl11JBAjlbKysoyjtnSqCm/gtab8OeOf4tX74EbN27lhl4koAqosxYHcWyuVUjZbcK4EYBZPafhS/GSDhaPV6rnhJd0+DhM0M9/Fw4ejcXZc5cRn3AFHNW0fls4ujrPU+XLfdDP7KXAbV6fFjspOc3BAqQiAVVA/fyHiTpozlZBpcV0GbMCAPLUZUMS48zX6/RF5fpqlBFjWTVIn4PwKbbq0iLbfTzAAJSfdJtZljHpkPGrbLnPIgFVQCVMttxeWtMjEWdhGSIozV26Ax+1HEHoWF4NuieEnFFDcft1NRBfAazKNfx6DGjNc86xsF1LZN29YwlWkYAq8SlnvNjqkimhW0zb8MBMdHXXbQ3HgpUh8Fm0HbP8grBo1U4EbA4DZ9/E6S5x7jqhfgvwk6aJVRVQRZYAS0pOdeCMGGug0p1t0v7PjBRCfmVWZ2i5V9Hv6cctgSoSUAXU5JQ0e1ugMq7s4boADwceKKVfvogBz/wHA559Snd9pcVXQBXlBeX+fY1dItZA5cwZ17ErHwKo0K5cvR7OzxOB6zCoeDE4Pfc0MlOS7AVUAdWCBFY2/lS10phU4aMBzIP0UEDVirdD/OV07J4+CW4lX4LzC0VxNTrK0rVEAqqA2qzDeHaVWASV+9l982DhgbZ09W41/zQg+ARCxo2AW6niyqpGbgkUUAVUa5K5p5wTarXVt0aPB9jIY0wYV63JR2MuY6/3FGVRCeuW0cMEVAHVkgTUjBs3NU7YJpTWBuPPWxb8AACCUv3Woziu2Bg7fHTtKriUeB5Dy7+GcbWqWb6O5AcWUEXQujjN4wgki6AyfuXoo9u3CzoYAUrXUzM05lcipJxOtyX4qIIyLfGCavUdUam8alC6GHEEuaGMD92LuS2bw/Xl5+H2ygs4sXGdWF4B9d8mKBHGqlZSr1RpMFCBTNj+bBKze/fuaYNGr+BoJA68V2lcug2cb5YhYrhdGQx743UMq1AG7qVLIDEyQh3n57xWn6N/0ScxpEwpDLMrq8r00DSkX77074NVQBVYj52Ix7NvdrHqAnNKGgfej5i8Fjdv3mYdq2K3T+D2Q2jfYwZe1AEl5DwvW5i/6TkjzwD/Xd7TlPtLq0oQB774LK2s/vmMAnRExfI8puRRrjS86tT890IqoAqsv+2OYGssp6BZbVwq/2E/5bq+22wImF9p2MQ1GD0tQPW3duztjeqNXdU0No735SB91qElZRw8Ri+XFzAouZQoRkgJo1UNr1gOjk9qSL14/t8NqoAqsEbFXODsFwJJyKwBS3eYI5doLdkwxMERBJOQG/VYpqRuRWs2dUNsnC1XFVrszmA4/kcjjFZBdSpWBMGTxgMCqYAqgpLDIF9aQYJoxK75EUFlHXa/sPFow7Zw5DeuDRo3So35HW5XNo8lJaRLO3XMDalIQBVYk5PT7AeOWq7cWI4JLlOnLy0tLakhWlL2w7KRiMMRmf2e81cjo88XoP8VWvhyP/Qv8jhjVtW36vLyC8rdDZowRiAVUEWWBUOxZy+B68YwJuVA/U795oDdOs4jl2Ha/C3YFnIMqWk3/mpfp9nUt8AhLtgzxxtZd2/n73wiAVX0ty1U/BDPK4KAKhIJqCKRSEAViUQCqkgkoIpEIgFVJBIJqCKRgCoSiQRUkUhAFYlEAqoI1lXw+rmPPaBrI/91CpMEVAF0b9hpdHGaizotPPB2Uzd82WkS5i8PzseMFyiFHz2jUqqwfk37wWjddSq4uhuP/X7hKhxcfDHBJ9DqRPFJczahUdsxqFJ/IOq1GgnPGeutXBvavds3tYPL/bDkxw6YUKcmRtiVwbh3qmFxx3Y4FrAGhRJYAVUgJZTMYcT1Svlp2nZQc0i5VunVa6n+trIBtu0+HS9U645XavZS2Ry4EDLF+au1m7njQHiMyhTRqN3YXKBCifNbuTgV6zfUYS1Z62d17RpNXC2CHeg+SOVJ4sTyfk8/hsGliqupcI5PPabyKY1+y05gFVAL23qoXiqDQ6VPnLBjV4TxgJ+OvYD//jCRx5h10Kp1+6zDeK6HqsrMXLgNGRmZ2evYpNu7j1+lw9qVc1LV4sbNv/fKA+qUuZsUlF92nmxcg2r6jSdB50LIFuFe3vUHXI09bVbnyulIphhVybvnftkcAqqAWmhyItEC0mpai/eYfIyw0q3NDczaTWEKUFrDhAtXLZ5j044jPM4s+xZBba+fn8f3h0cbx3Jc8cq6G+wxwd+SVbWu+/c0phmltS0UVlVAFVDbOkxXCwmvWr/fKqgULR7d4dwNRA3ajFbZHsZOt5VXl7DPZNYHi6A6uCxQ56D7nf/GIWjXz5/Delcn+DRrgskfv4+F37TG4VXL1TmcixVRWQuz7hWC1eAEVAGVuYyYOoVZGZg5kKk/c2vqvM3MGqhSex44FGNm9Qgv48lzCVdsgspsD4w/LYF6Jj4RT9t1Vlad5yP8k+ZsREzcJavu9paRHujzhKaWZnR8SkPfJzX0eVxDT03Toa3D7IXq2P2sQrBiuYAqoBKenFXEi1T8yap4/MnyP2LRLyF5QOU5UlNvaLZAPRJxlu5tblApI61Lm27TuK4Ny/F6bMxiC7IOW5aZFefCUYTT+fmi2DHRE3dvqZhYKeHwQYyqUoHLYRQeUAVUAZXdKIw/Q/ZFghnsue6MJd28dVtLS8/U7ty5YwZNBd0i09IyvrQF6opf97KcNVDNdFiH2sPLX91XWd1dbtN1qtnLweezxhj00nMI9HC16io7FXu68IAqoAqozChIIDr29rEZoxpWLZfb6qS7zFyJ7QvGlza6b2p96s41ZiyAyj7WazhruM4wFBd/WcXPpWv3NgN1wrs1lDWN3LrJ6jWHlC3JbprCAaqAKqCm61aS/Zuv6EDMWLDVIqwzF2yDVuZ7rN8angeyO7fvcOU3wm60zubWj46z2VhktdW39mfu0Ep3QMSp383qM4MhXeCKHw8wA3Vpp+9U94tvmy8tXu/amWiCXLgsqoAqsPpv2K/WmKH1atBmFBat2ontIcfhuyIYdf87TEFctFIXLDTiU/P6QTuP68d/UrByWYvZfkEIDDqsUoZW1uF8vmp3tuiqRqtm303IA+qS1bvYRaSy6/Neos9cBM/5jg6wDiobtMxAzbiSmMyGI8Lq/WkjnNwciEsnj+Nc2H7m+mX8WjhjVAFVYN19IIrdJ2zA0aFRI5MUPBSXoTgVfd5mDEpryIz4rMcRSbTSz+niC4CDIC5cSlKNRC07T7YIe2/3RXhGL5tTl1aaf3/ba6bF8pdOHINbyRfRr8gT7C81Wn27axq2eY6EZ61q6KZvZ90tVN0zAqoISgcORSsXmFZsuu9WhB87k++xvlSMbg1XBx7AynX7sHNfpFE3YMtBlTW/r4ef1bjyYmIy5izZoa7Ne2C3jbXrGtcL+Q1BXmNVYm6uAEdry/3nwvYhOng75HcVUAupYEG2y6el39AiTyfYbIxq3nGCsqgBm8Nsw5dbBb5fY1skoArUJ6MS6KqqYYQHj8RahHWo12qu4KZahwWeRyIBVcSZM9OyY1sHNG47hlPZ6DZztBNbbDkggvGqWZz7CCQSUAVWLqXIKWlGIxI/q5gahdr1mIGU6xl2Aukjl4AqgtL11AwtTHeB9x+KBocFStYFAfUfKgFW4Hx4ElBFIpGAKhIJqCKRSECVVbJRSOJTUA+tfAHO+egloELb4eWJERXLKK3o3hlZdx/kAHH8bd9jvjFLxXa5ExvXY2w1O3jWqIz0y5f+YX2m0PbNn4340P3I74inhe1bIyn+zAP8Hpz50xHZ2wLqoxd/kA5Y6dAF3KYOLl+Me7dv2bCy+FOa9bk9jLoP4Hy2LOPIN8vDdh0ouZZ4TpW7ezNTG1zqRet1bO+3Xcb2PptWPmjcKETt2Ib8fP9b6dc1j7Iljd/QtseQ/+85se47+Ed4WgIqtHt6ahD30iVg7Udb9O3XGFezMqbW/0A/lKX2rez2I+Z91QLHA1YjZPokzGvZDJtHeKhzTG9cT5Vf+mMH9ff/pnqhf9En9LL+6u9Nw911S1ZRL1MFKb/Hq30hM6ZgfqsWCHDuq/72adYY4/XjC9u3ggF78yaY/GFt0BLmvs8ZTeqp8uvdnDHpw3dhenhTNa86NeFZvSI2DnVDnknkzzyJa3ExZg8ip5+NrV4JY96qgEPZiccoJsr21Pf7fd8evq0/V/s5sD7pbKzpRffDN2rf7jk+mP9Vc6zt10v9vbpPT91iV8KE997GzevJdtwXumQhpjf8ELNaNM1zTweXLcbISmX14x9hWeeOiN2zU5XZ6OGqn+dN/Tw1cDsjLQ94/r0dmIpUG13lDeOej69bA3oX/L/4fdfe2P9Lr24YW9VOP/YFFn3Tyti/uq/pXr30e83Mvtcpn7yvjifHx2FS3VqY2bQeWO4RwCqgnjt4AHwQLb2pf+nZFfsXzEXOBOexNSqr7Ynv19QhOwtTvtpOiAgMMABLvXhe1Q1d7KvDO0TtZ3b4nAdxSTbAlHOxp9T2WidHMJM8txe0a4XEUydgetjWYoP7IJzavhmrenXLPocxoyUb0vqI3RWs9sXt3YXhFV5T26P+8NCuceyFY2v9zeoRnCkfvw/Xl4uBDyL3uZcuDsOafFAbt1JTtHUuTvjftInIrmPvoGlQcHb+HpdPR6qyU+vVVfvWDXYBv7cplBjLe8+22jc0Xkd95+f+oz7jQ/chM/maA7epxMgIjH27CmB4Bm8g4eghMOfSr86Oxv4Rdq/neVF5lHkFJk9oCXbPmqG298z1RmD29ed+1UKBHDzVi5kQs+tnaZxmp+514jhsGDww970aoFJndofw/6teWhdPHPu7LauASg0o+rhFF0dPvGW23+WlogaoMBJL/2hYphGVymGFbm35tiaQ28ePUfvH1a6OnJgnMdIEITXTvhHu3szQfh3YDxeOHzGVrVUVy376Pvsc3+ZYamwcNhh8IPf5zjZ7UF1eLAqze65cHiaL+YR+L52MezmweIFRju5uevYUs6x7d7RB2edwf7UEGAb4fv0l/H7ooD+QxzG13gdm56dl5eeyLj8gKT5O7fP+tKEJVLeBSAgPhdrXrAkyU66Nh3FfphfHlZgoTP7oPUz6oLbhoVC7fGZg/6L5xrWYCO3coTBsGzca3p81Ut7F4o5tMa/1F2bf/0TgOri/9jL4v/Vt2xJDy71qsu6zZ+DwGn+1vcHNGZdPndB827ZCxhUVj5u9YGa1sEdmctIffne7bFDrGt81cIgrzuzZqV4glyIjBNRHASvnRo59uzKuX/gddzIzlCW9lZaqHha/701uHd/Us5qb3DW6YfykeDwx6qTJ1dMt1y6faWo7bMkiXDh2WG0Pea2EYfHGVDfVZYYDt+w39y8/O4CW3eTmuWH7uFFq+8iaX/C7/rCy3qFVK2CKLYuZPah0xegacnu7/lAzVlMvgab1EROyQ20HjR+NjGuXw/8IXO9sa8KGJI8ypjpDy5VCWuIFdTzA2dHwAuZ+9TlMUASgR3a99a7OCJk2SW33ecy0b42TI+9VbR/2X45pDT9W2yc3b0AOqDney8oeXXBq22a1TdGldSn+nMpSqFtyu/5PP4ZzOvRXY07rUJvc+bTEiwwlzL7/sPKlcSP56vi7t25qOdBdOhkBWtTQpX4weRQ/I+FQmBaz8zdMyX7xxOludVdNM/7P03R3OztronGvTGV642piMuskxUWr8/voIcj5o4cE1EcF69n9e2hJVOx0aOUycF82oHzgGP8Z+7aPHWFsh/r5Qn+AjL8Jy7QGH2HLqKFm+xgzcTsqaIse6zTQY7DvkHNtxoPJ5+KM8sFTvPjg8C1u5r4SvmtnYvLEaDz/1AYf4sjqlfhtoqdRhy4jz7Nnjjdy19HdVpUMm642X0457iAt0wz9OpFbNxrnidJdb992rdWCTl7v1TDzJhbqMXyQ50i17+haf7O494j/CjBmX/WzkQycLi/viZ5CnntKOnsGsz//VPcAOuqu+ipciY7KfsHtVFaVFjUzJck+51yEOse9pXK+FxsD40P3Ij7sgOm+9P+LHrooKnn9Be3bgLHylE/qGHX5v+O95oQY1NXYaLBNgdu89tyWzcHf5vrFhH/KpAXpR7W93zhuvbzVMrZbUAveWmq9fEHrUMy8MNO+IS5HRWLz8CFY0b2T9UnnD/h75LMFN9/f62LEUTD2Zjz8v2mT2CBY8NZ4aUwS/aPEuDL6lGpQig4OAoD/198lJSEeIdMnM8laYZ5vK6DKLJv/a58OCQAAABiE9W99+QS4iQVAoOWMChgVjAoYFTAqGBUwKmBUMCpgVMCoYFTAqGBUwKiAUcGogFEBo4JRAaOCUQGjAkYFowJGBfpRAaMCA89JE9EoJZoGAAAAAElFTkSuQmCC";
		}
		
		lstrSig = MailConnector.editOutterBody(new HashSet<String>(Arrays.asList("_x0000_i1025")), lstrSig);*/

		return "<bigbang:signature:bb>" + lstrSig;
	}
}
