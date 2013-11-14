package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import microsoft.exchange.webservices.data.Attachment;
import microsoft.exchange.webservices.data.BasePropertySet;
import microsoft.exchange.webservices.data.BodyType;
import microsoft.exchange.webservices.data.ConflictResolutionMode;
import microsoft.exchange.webservices.data.DeleteMode;
import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailAddressCollection;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.ExtendedProperty;
import microsoft.exchange.webservices.data.ExtendedPropertyDefinition;
import microsoft.exchange.webservices.data.FileAttachment;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.FolderId;
import microsoft.exchange.webservices.data.FolderView;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemAttachment;
import microsoft.exchange.webservices.data.ItemId;
import microsoft.exchange.webservices.data.ItemSchema;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.LogicalOperator;
import microsoft.exchange.webservices.data.Mailbox;
import microsoft.exchange.webservices.data.MapiPropertyType;
import microsoft.exchange.webservices.data.NameResolution;
import microsoft.exchange.webservices.data.NameResolutionCollection;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.SearchFilter;
import microsoft.exchange.webservices.data.ServiceObject;
import microsoft.exchange.webservices.data.SortDirection;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;
import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.Security.Password;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.MessageAddressData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;

public class MailConnector
{
    private static final UUID EXTPROPID_MSGID = UUID.fromString("99FC5365-04F9-41C9-90DE-904AD1A82AC8");
    private static final UUID EXTPROPID_REFID = UUID.fromString("5BB5DCD6-C8C6-4AF9-80F6-A278F0F7CB9D");
    private static final UUID EXTPROPID_THISID = UUID.fromString("D08B09DE-E1B3-47CE-9ECF-8D936F4DDA8A");

	private static String getUserName()
		throws BigBangJewelException
	{
		User lobjUser;

		try
		{
			lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser());
			return lobjUser.getUserName();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static String getUserPassword()
		throws BigBangJewelException
	{
		User lobjUser;
		Password lobjPass;

		try
		{
			lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser());
			if ( lobjUser.getAt(2) instanceof Password )
				lobjPass = (Password)lobjUser.getAt(2);
			else
				lobjPass = new Password((String)lobjUser.getAt(2), true);
			return lobjPass.GetClear();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static Session GetMailSession()
		throws BigBangJewelException
	{
		String lstrServer;
		JewelAuthenticator lauth;
		Properties lprops;

		lstrServer = (String)Engine.getUserData().get("MailServer");

		lauth = new JewelAuthenticator(getUserName(), getUserPassword());

		lprops = new Properties();
		lprops.put("mail.host", lstrServer);
		lprops.put("mail.from", getUserEmail());
		lprops.put("mail.smtp.submitter", lauth.getPasswordAuthentication().getUserName());
		lprops.put("mail.smtp.auth", "true");
		lprops.put("mail.smtp.host", lstrServer);
		lprops.put("mail.smtp.port", "25");

		return Session.getInstance(lprops, lauth);
	}

//	private static Store GetMailStore()
//		throws BigBangJewelException
//	{
//		Session lsession;
//		Store lstore;
//
//		lsession = GetMailSession();
//		try
//		{
//			lstore = lsession.getStore("imaps");
//			lstore.connect();
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		return lstore;
//	}

	private static Transport GetMailTransport()
		throws BigBangJewelException
	{
		Session lsession;
		Transport lxport;

		lsession = GetMailSession();
		try
		{
			lxport = lsession.getTransport("smtp");
			lxport.connect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lxport;
	}

	private static ExchangeService GetService()
		throws BigBangJewelException
	{
		String lstrServer;
		ExchangeService lsvc;

		lstrServer = (String)Engine.getUserData().get("MailServer");

		try
		{
			lsvc = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
			lsvc.setCredentials(new WebCredentials(getUserName(), getUserPassword()));
			lsvc.setUrl(new URI("https://" + lstrServer + "/EWS/Exchange.asmx"));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lsvc;
	}

	private static Mailbox GetMailbox(ExchangeService psvc)
		throws BigBangJewelException
	{
		String lstrEmail;
		int i;

		lstrEmail = getUserEmail();
		if ( lstrEmail == null )
			throw new BigBangJewelException("Erro: Utilizador sem endereço de email.");

		i = lstrEmail.indexOf('@');
		if ( i < 0 )
			throw new BigBangJewelException("Erro: Utilizador com endereço de email inválido.");

		return new Mailbox("bbsystem" + lstrEmail.substring(i));
	}

	private static Folder GetRoot(ExchangeService psvc)
		throws BigBangJewelException
	{
		try
		{
			return Folder.bind(psvc, WellKnownFolderName.MsgFolderRoot);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static Folder GetInbox(ExchangeService psvc)
		throws BigBangJewelException
	{
		try
		{
			return Folder.bind(psvc, WellKnownFolderName.Inbox);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static Folder GetSentItems(ExchangeService psvc)
		throws BigBangJewelException
	{
		try
		{
			return Folder.bind(psvc, WellKnownFolderName.SentItems);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static Folder GetBaseBigBangFolder(ExchangeService psvc)
		throws BigBangJewelException
	{
		FolderId lrefRoot;
		Folder lobjFolder;
		ArrayList<Folder> larrFolders;
		int i;

		lobjFolder = null;

		try
		{
			lrefRoot = new FolderId(WellKnownFolderName.MsgFolderRoot, GetMailbox(psvc));
			larrFolders = psvc.findFolders(lrefRoot, new FolderView(Integer.MAX_VALUE)).getFolders();

			for ( i = 0; i < larrFolders.size(); i++ )
			{
				if ( "bigbang".equals(larrFolders.get(i).getDisplayName()) )
				{
					lobjFolder = larrFolders.get(i);
					break;
				}
			}

			if ( lobjFolder == null )
			{
				lobjFolder = new Folder(GetService());
				lobjFolder.setDisplayName("bigbang");
				lobjFolder.save(lrefRoot);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjFolder;
	}

	private static Folder GetBigBangFolder(ExchangeService psvc, Date pdtRef)
		throws BigBangJewelException
	{
		String lstrName;
		FolderId lrefRoot;
		Folder lobjFolder;
		ArrayList<Folder> larrFolders;
		int i;

		lstrName = new SimpleDateFormat("yyyyMM").format(pdtRef);

		lobjFolder = null;

		try
		{
			lrefRoot = GetBaseBigBangFolder(psvc).getId();
			larrFolders = psvc.findFolders(lrefRoot, new FolderView(Integer.MAX_VALUE)).getFolders();

			for ( i = 0; i < larrFolders.size(); i++ )
			{
				if ( lstrName.equals(larrFolders.get(i).getDisplayName()) )
				{
					lobjFolder = larrFolders.get(i);
					break;
				}
			}

			if ( lobjFolder == null )
			{
				lobjFolder = new Folder(GetService());
				lobjFolder.setDisplayName(lstrName);
				lobjFolder.save(lrefRoot);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjFolder;
	}

//	private static Folder GetProcessedFolder(ExchangeService psvc)
//		throws BigBangJewelException
//	{
//		Folder lobjFolder;
//		Folder lobjParent;
//		ArrayList<Folder> larrFolders;
//		int i;
//
//		lobjFolder = null;
//		lobjParent = GetBigBangFolder(psvc);
//
//		try
//		{
//			larrFolders = psvc.findFolders(lobjParent.getId(), new FolderView(Integer.MAX_VALUE)).getFolders();
//
//			for ( i = 0; i < larrFolders.size(); i++ )
//			{
//				if ( "tratados".equals(larrFolders.get(i).getDisplayName()) )
//				{
//					lobjFolder = larrFolders.get(i);
//					break;
//				}
//			}
//
//			if ( lobjFolder == null )
//			{
//				lobjFolder = new Folder(GetService());
//				lobjFolder.setDisplayName("tratados");
//				lobjFolder.save(lobjParent.getId());
//			}
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		return lobjFolder;
//	}

	private static ArrayList<Folder> GetFolders(ExchangeService psvc, FolderId prefSource)
		throws BigBangJewelException
	{
		FolderView lobjView;

		lobjView = new FolderView(Integer.MAX_VALUE);

		try
		{
			return psvc.findFolders(prefSource, lobjView).getFolders();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static ArrayList<Item> GetItems(ExchangeService psvc, FolderId prefSource, int plngCount)
		throws BigBangJewelException
	{
		ItemView lobjView;
		PropertySet lobjPropSet;
		SearchFilter lobjFilter;

		lobjView = new ItemView(plngCount);
		lobjPropSet = new PropertySet(BasePropertySet.FirstClassProperties);
		lobjPropSet.setRequestedBodyType(BodyType.Text);
		lobjView.setPropertySet(lobjPropSet);

		try
		{
			lobjFilter = new SearchFilter.SearchFilterCollection(LogicalOperator.Or,
					new SearchFilter.Not(new SearchFilter.Exists(GetPropDef())), new SearchFilter.IsEqualTo(GetPropDef(), "_"));
			//All of this is because removeExtendedProperty throws a NullPointerException. See DoUnprocessItem.
			lobjView.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);
			return psvc.findItems(prefSource, lobjFilter, lobjView).getItems();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static ArrayList<Item> GetItemByTag(ExchangeService psvc, FolderId prefSource, String pstrTag)
		throws BigBangJewelException
	{
		ItemView lobjView;
		SearchFilter lobjFilter;

		lobjView = new ItemView(2);
		lobjView.setPropertySet(new PropertySet(BasePropertySet.IdOnly, GetPropDef()));

		try
		{
			lobjFilter = new SearchFilter.IsEqualTo(GetPropDef(), pstrTag);
			return psvc.findItems(prefSource, lobjFilter, lobjView).getItems();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static Folder GetFolder(ExchangeService psvc, FolderId prefFolder)
		throws BigBangJewelException
	{
		try
		{
			return Folder.bind(psvc, prefFolder);
		}
		catch (Exception e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static Item GetItem(ExchangeService psvc, ItemId prefItem)
		throws BigBangJewelException
	{
		try
		{
			return Item.bind(psvc, prefItem, new PropertySet(
					BasePropertySet.FirstClassProperties/*, GetPropDef(), GetRefDef(), GetThisDef(),
					ItemSchema.Id, ItemSchema.Subject,
					ItemSchema.Body, EmailMessageSchema.From, ItemSchema.DateTimeSent, ItemSchema.Attachments,
					ItemSchema.MimeContent*/));
		}
		catch (Exception e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static ServiceObject[] GetContents(ExchangeService psvc, FolderId prefSource, int plngCount)
		throws BigBangJewelException
	{
		Folder lobjAux;
		FolderId lrefParent;
		ArrayList<ServiceObject> larrTmp;

		lobjAux = null;
		if ( !prefSource.getUniqueId().equals(GetRoot(psvc).getId().getUniqueId()) )
		{
			lobjAux = GetFolder(psvc, prefSource);
			try
			{
				lrefParent = lobjAux.getParentFolderId();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
			lobjAux = GetFolder(psvc, lrefParent);
		}

		larrTmp = new ArrayList<ServiceObject>();

		if ( prefSource.getUniqueId().equals(GetInbox(psvc).getId().getUniqueId()) )
			larrTmp.add(GetSentItems(psvc));
		if ( prefSource.getUniqueId().equals(GetSentItems(psvc).getId().getUniqueId()) )
			larrTmp.add(GetInbox(psvc));
		if ( lobjAux != null  )
			larrTmp.add(lobjAux);
		larrTmp.addAll(GetFolders(psvc, prefSource));
		larrTmp.addAll(GetItems(psvc, prefSource, plngCount));

		return larrTmp.toArray(new ServiceObject[larrTmp.size()]);
	}

	private static ExtendedPropertyDefinition GetPropDef()
		throws BigBangJewelException
	{
		try
		{
			return new ExtendedPropertyDefinition(EXTPROPID_MSGID, "BigBang Tag", MapiPropertyType.String);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static ExtendedPropertyDefinition GetRefDef()
		throws BigBangJewelException
	{
		try
		{
			return new ExtendedPropertyDefinition(EXTPROPID_REFID, "BigBang Linkback", MapiPropertyType.String);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private static ExtendedPropertyDefinition GetThisDef()
		throws BigBangJewelException
	{
		try
		{
			return new ExtendedPropertyDefinition(EXTPROPID_THISID, "BigBang Self-Reference", MapiPropertyType.String);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static String getUserEmail()
		throws BigBangJewelException
	{
		UserDecoration lobjDeco;

    	lobjDeco = UserDecoration.GetByUserID(Engine.getCurrentNameSpace(), Engine.getCurrentUser());
		if ( lobjDeco == null )
			return null;

		return (String)lobjDeco.getAt(UserDecoration.I.EMAIL);
	}

	public static String getLoggedEmail()
		throws BigBangJewelException
	{
		ExchangeService lsvc;
		NameResolutionCollection larrNames;
		String lstr;

		lsvc = GetService();

		try
		{
			larrNames = lsvc.resolveName("smtp:" + getUserEmail());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lstr = null;
		for ( NameResolution nr : larrNames )
		{
			lstr = nr.getMailbox().getAddress();
		}

		return lstr;
	}

	public static PropertySet getPropSet()
		throws BigBangJewelException
	{
		return new PropertySet(BasePropertySet.FirstClassProperties/*, GetPropDef(), GetRefDef(), GetThisDef()*/);
	}

	public static void DoSendMail(OutgoingMessageData pobjMessage, SQLServer pdb)
		throws BigBangJewelException
	{
		String[] larrTos;
        ResultSet lrs;
		IEntity lrefDecos;
		String[] larrReplyTos;
		FileXfer[] larrFiles;
		Document lobjDoc;
		int i;

		if ( pobjMessage.marrContactInfos == null )
			larrTos = null;
		else
		{
			larrTos = new String[pobjMessage.marrContactInfos.length];
			for ( i = 0; i < pobjMessage.marrContactInfos.length; i++ )
			{
				larrTos[i] = (String)ContactInfo.GetInstance(Engine.getCurrentNameSpace(),
						pobjMessage.marrContactInfos[i]).getAt(2);
			}
		}

		if ( larrTos != null )
		{
	        lrs = null;
			try
			{
				lrefDecos = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
				larrReplyTos = new String[pobjMessage.marrUsers.length];
				for ( i = 0; i < pobjMessage.marrUsers.length; i++ )
				{
					lrs = lrefDecos.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {pobjMessage.marrUsers[i]}, new int[0]);
				    if (lrs.next())
				    	larrReplyTos[i] = (String)UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrs).getAt(1);
				    else
				    	larrReplyTos[i] = null;
				    lrs.close();
				}
			}
			catch (Throwable e)
			{
				if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( pobjMessage.marrAttachments == null )
				larrFiles = null;
			else
			{
				larrFiles = new FileXfer[pobjMessage.marrAttachments.length];
				for ( i = 0; i < larrFiles.length; i++ )
				{
					lobjDoc = Document.GetInstance(Engine.getCurrentNameSpace(), pobjMessage.marrAttachments[i]);
					larrFiles[i] = lobjDoc.getFile();
				}
			}
			DoSendMail(larrReplyTos, larrTos, pobjMessage.marrCCs, pobjMessage.marrBCCs,
					pobjMessage.mstrSubject, pobjMessage.mstrBody, larrFiles);
		}
	}

	public static void DoSendMail(String[] parrReplyTo, String[] parrTo, String[] parrCC, String[] parrBCC,
			String pstrSubject, String pstrBody, FileXfer[] parrAttachments)
		throws BigBangJewelException
	{
		Session lsession;
		Transport lxport;
		MimeMessage lmsg;
		Address[] larrAddr;
		MimeMultipart lmpMessage;
		MimeBodyPart lbp;
		int i;

		lsession = GetMailSession();
		lxport = GetMailTransport();

		lmsg = new MimeMessage(lsession);
		try
		{
			lmsg.setFrom(InternetAddress.getLocalAddress(lsession));
			if ( parrReplyTo != null )
			{
				larrAddr = new Address[parrReplyTo.length];
				for ( i = 0; i < parrReplyTo.length; i++ )
					if ( parrReplyTo[i] != null )
						larrAddr[i] = new InternetAddress(parrReplyTo[i]);
				lmsg.setReplyTo(larrAddr);
			}
			for ( i = 0; i < parrTo.length; i++ )
				lmsg.addRecipient(Message.RecipientType.TO, new InternetAddress(parrTo[i]));
			if ( parrCC != null )
				for ( i = 0; i < parrCC.length; i++ )
					lmsg.addRecipient(Message.RecipientType.CC, new InternetAddress(parrCC[i]));
			if ( parrBCC != null )
				for ( i = 0; i < parrBCC.length; i++ )
					lmsg.addRecipient(Message.RecipientType.BCC, new InternetAddress(parrBCC[i]));
			lmsg.setSubject(pstrSubject);

			if ( (parrAttachments == null) || (parrAttachments.length == 0) )
			{
				lmsg.setText(pstrBody, "UTF-8");
				lmsg.addHeader("Content-Type", "text/html");
			}
			else
			{
				lmpMessage = new MimeMultipart();

				lbp = new MimeBodyPart();
				lbp.setText(pstrBody, "UTF-8");
				lmpMessage.addBodyPart(lbp);

				for ( i = 0; i < parrAttachments.length; i++ )
				{
					if ( parrAttachments[i] == null )
						continue;

					lbp = new MimeBodyPart();
					lbp.setDataHandler(new DataHandler(new ByteArrayDataSource(parrAttachments[i].getData(),
							parrAttachments[i].getContentType())));
					lbp.setFileName(parrAttachments[i].getFileName());
					lmpMessage.addBodyPart(lbp);
				}

				lmsg.setContent(lmpMessage);
				lmsg.addHeader("Content-Type", lmpMessage.getContentType());
			}

			lmsg.addHeader("MIME-Version", "1.0");
			lmsg.saveChanges();
			lxport.sendMessage(lmsg, lmsg.getAllRecipients());
			lxport.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static ServiceObject[] DoGetMail(boolean pbSent)
		throws BigBangJewelException
	{
		ExchangeService lsvc;
		Folder lobjFolder;

		lsvc = GetService();

		lobjFolder = ( pbSent ? GetSentItems(lsvc) : GetInbox(lsvc) );

		if ( lobjFolder == null )
			return null;

		return GetContents(lsvc, lobjFolder.getId(), 30);
	}

	public static ServiceObject[] DoGetMailAll(boolean pbSent)
		throws BigBangJewelException
	{
		ExchangeService lsvc;
		Folder lobjFolder;

		lsvc = GetService();

		lobjFolder = ( pbSent ? GetSentItems(lsvc) : GetInbox(lsvc) );

		if ( lobjFolder == null )
			return null;

		return GetContents(lsvc, lobjFolder.getId(), Integer.MAX_VALUE);
	}

	public static ServiceObject[] DoGetFolder(String pstrUniqueID)
		throws BigBangJewelException
	{
		try
		{
			return GetContents(GetService(), new FolderId(pstrUniqueID), 30);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static Item DoGetItem(String pstrUniqueID)
		throws BigBangJewelException
	{
		try
		{
			return GetItem(GetService(), new ItemId(pstrUniqueID));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static Map<String, String> DoProcessItem(String pstrUniqueID, UUID pidTag, Date pdtRef)
		throws BigBangJewelException
	{
		ExchangeService lsvc;
		Item litem;
		FolderId lrefDest;
		Item lnew;
		ArrayList<Item> larrAux;
		Map<String, String> larrResult;

		lsvc = GetService();

		larrResult = new HashMap<String, String>();

		try
		{
			litem = Item.bind(lsvc, new ItemId(pstrUniqueID));
			lrefDest = GetBigBangFolder(lsvc, pdtRef).getId();

			litem.setExtendedProperty(GetPropDef(), pidTag.toString());
			litem.update(ConflictResolutionMode.AutoResolve);

			lnew = litem.copy(lrefDest);
			if ( lnew == null )
			{
				larrAux = GetItemByTag(lsvc, lrefDest, pidTag.toString());
				if ( larrAux.size() == 1 )
					lnew = Item.bind(lsvc, larrAux.get(0).getId());
			}
			if ( lnew != null )
			{
				larrResult.put("_", lnew.getId().getUniqueId());

				lnew.setExtendedProperty(GetRefDef(), litem.getId().getUniqueId());
				lnew.update(ConflictResolutionMode.AutoResolve);
				lnew = Item.bind(lsvc, lnew.getId());

				lnew.setExtendedProperty(GetThisDef(), lnew.getId().getUniqueId());
				lnew.update(ConflictResolutionMode.AutoResolve);
				lnew = Item.bind(lsvc, lnew.getId());

				litem = Item.bind(lsvc, new ItemId(pstrUniqueID));
				litem.load();

				if ( litem.getHasAttachments() )
				{
					lnew = Item.bind(lsvc, lnew.getId());
					lnew.load();

					OUTER: for ( Attachment latti : litem.getAttachments() )
					{
						for ( Attachment lattn : lnew .getAttachments() )
						{
							if ( (((lattn.getContentId() != null) && lattn.getContentId().equals(latti.getContentId())) || 
									((lattn.getName() != null) && lattn.getName().equals(latti.getName()))) &&
									(larrResult.get(lattn.getId()) == null) )
							{
								larrResult.put(latti.getId(), lattn.getId());
								larrResult.put(lattn.getId(), latti.getId()); //Ignored by caller, used for duplicate filename tracking
								continue OUTER;
							}
						}
					}
				}
			}
		}
		catch (BigBangJewelException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrResult;
	}

	public static void DoUnprocessItem(String pstrUniqueID)
		throws BigBangJewelException
	{
		ExchangeService lsvc;
		Item litem, lorig;

		lsvc = GetService();

		try
		{
			litem = Item.bind(lsvc, new ItemId(pstrUniqueID));
			litem.load(new PropertySet(BasePropertySet.IdOnly, GetRefDef()));
			for ( ExtendedProperty lep : litem.getExtendedProperties() )
			{
				if ( EXTPROPID_REFID.equals(lep.getPropertyDefinition().getPropertySetId()) )
				{
					lorig = null;
					try
					{
						lorig = Item.bind(lsvc, new ItemId((String)lep.getValue()));
					}
					catch (Throwable e1)
					{
					}
					if ( lorig != null )
					{
						lorig.setExtendedProperty(GetPropDef(), "_"); //removeExtendedProperty throws NullPointerException
						lorig.update(ConflictResolutionMode.AutoResolve);
					}
					break;
				}
			}
			litem.delete(DeleteMode.HardDelete);
		}
		catch (BigBangJewelException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static FileXfer DoGetAttachment(String pstrEmailId, String pstrAttachmentId)
		throws BigBangJewelException
	{
		ExchangeService lsvc;
		Item lobjItem;
		byte[] larrBytes;
		FileXfer lobjFile;

		lsvc = GetService();

		try
		{
			lobjItem = GetItem(lsvc, new ItemId(pstrEmailId));
			lobjItem.load();

			for ( Attachment lobjAtt: lobjItem.getAttachments() )
			{
				if ( !lobjAtt.getId().equals(pstrAttachmentId) )
					continue;

				if ( lobjAtt instanceof FileAttachment )
				{
					((FileAttachment)lobjAtt).load();
					larrBytes = ((FileAttachment)lobjAtt).getContent();
					lobjFile = new FileXfer(larrBytes.length, ( lobjAtt.getContentType() == null ? "application/octet-stream" : lobjAtt.getContentType() ),
							((FileAttachment)lobjAtt).getName(), new ByteArrayInputStream(larrBytes));
				}
				else if ( lobjAtt instanceof ItemAttachment )
				{
					((ItemAttachment)lobjAtt).load();
					larrBytes = new byte[0]; //((ItemAttachment)lobjAtt).getItem().getMimeContent().getContent();
					lobjFile = new FileXfer(larrBytes.length, ( lobjAtt.getContentType() == null ? "application/octet-stream" : lobjAtt.getContentType() ),
							((ItemAttachment)lobjAtt).getName() + ".eml", new ByteArrayInputStream(larrBytes));
				}
				else
					continue;

				return lobjFile;
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		throw new BigBangJewelException("Erro: Anexo não encontrado na mensagem indicada.");
	}

	public static void SendFromData(MessageData pobjMessage)
		throws BigBangJewelException
	{
		int llngTo, llngCC, llngBCC, llngReplyTo;
		int i;
		InternetAddress[] larrTos;
		InternetAddress[] larrCCs;
		InternetAddress[] larrBCCs;
		InternetAddress[] larrReplyTos;
		FileXfer[] larrFiles;
		Document lobjDoc;

		if ( pobjMessage.marrAddresses == null )
			return;

		llngTo = 0;
		llngCC = 0;
		llngBCC = 0;
		llngReplyTo = 0;
		for ( i = 0; i < pobjMessage.marrAddresses.length; i++ )
		{
			if ( Constants.UsageID_To.equals(pobjMessage.marrAddresses[i].midUsage) )
				llngTo++;
			else if ( Constants.UsageID_CC.equals(pobjMessage.marrAddresses[i].midUsage) )
				llngCC++;
			else if ( Constants.UsageID_BCC.equals(pobjMessage.marrAddresses[i].midUsage) )
				llngBCC++;
			else if ( Constants.UsageID_ReplyTo.equals(pobjMessage.marrAddresses[i].midUsage) )
				llngReplyTo++;
		}
		larrTos = new InternetAddress[llngTo];
		larrCCs = new InternetAddress[llngCC];
		larrBCCs = new InternetAddress[llngBCC];
		larrReplyTos = new InternetAddress[llngReplyTo];
		llngTo = 0;
		llngCC = 0;
		llngBCC = 0;
		llngReplyTo = 0;
		for ( i = 0; i < pobjMessage.marrAddresses.length; i++ )
		{
			if ( Constants.UsageID_To.equals(pobjMessage.marrAddresses[i].midUsage) )
			{
				larrTos[llngTo] = BuildAddress(pobjMessage.marrAddresses[i]);
				llngTo++;
			}
			else if ( Constants.UsageID_CC.equals(pobjMessage.marrAddresses[i].midUsage) )
			{
				larrCCs[llngCC] = BuildAddress(pobjMessage.marrAddresses[i]);
				llngCC++;
			}
			else if ( Constants.UsageID_BCC.equals(pobjMessage.marrAddresses[i].midUsage) )
			{
				larrBCCs[llngBCC] = BuildAddress(pobjMessage.marrAddresses[i]);
				llngBCC++;
			}
			else if ( Constants.UsageID_ReplyTo.equals(pobjMessage.marrAddresses[i].midUsage) )
			{
				larrReplyTos[llngReplyTo] = BuildAddress(pobjMessage.marrAddresses[i]);
				llngReplyTo++;
			}
		}

		if ( pobjMessage.marrAttachments == null )
			larrFiles = null;
		else
		{
			larrFiles = new FileXfer[pobjMessage.marrAttachments.length];
			for ( i = 0; i < larrFiles.length; i++ )
			{
				lobjDoc = Document.GetInstance(Engine.getCurrentNameSpace(), pobjMessage.marrAttachments[i].midDocument);
				larrFiles[i] = lobjDoc.getFile();
			}
		}

		DoSendMail(larrReplyTos, larrTos, larrCCs, larrBCCs, pobjMessage.mstrSubject, pobjMessage.mstrBody, larrFiles);
	}

	public static void DoSendMail(InternetAddress[] parrReplyTo, InternetAddress[] parrTo, InternetAddress[] parrCC, InternetAddress[] parrBCC,
			String pstrSubject, String pstrBody, FileXfer[] parrAttachments)
		throws BigBangJewelException
	{
		Session lsession;
		Transport lxport;
		MimeMessage lmsg;
		MimeMultipart lmpMessage;
		MimeBodyPart lbp;
		int i;

		lsession = GetMailSession();
		lxport = GetMailTransport();

		lmsg = new MimeMessage(lsession);
		try
		{
			lmsg.setFrom(InternetAddress.getLocalAddress(lsession));

			if ( parrReplyTo.length > 0 )
				lmsg.setReplyTo(parrReplyTo);
			for ( i = 0; i < parrTo.length; i++ )
				lmsg.addRecipient(Message.RecipientType.TO, parrTo[i]);
			for ( i = 0; i < parrCC.length; i++ )
				lmsg.addRecipient(Message.RecipientType.CC, parrCC[i]);
			for ( i = 0; i < parrBCC.length; i++ )
				lmsg.addRecipient(Message.RecipientType.BCC, parrBCC[i]);

			lmsg.setSubject(pstrSubject);

			if ( (parrAttachments == null) || (parrAttachments.length == 0) )
			{
				lmsg.setContent(pstrBody, "text/html; charset=UTF-8");
			}
			else
			{
				lmpMessage = new MimeMultipart();

				lbp = new MimeBodyPart();
				lbp.setContent(pstrBody, "text/html; charset=UTF-8");
				lmpMessage.addBodyPart(lbp);

				for ( i = 0; i < parrAttachments.length; i++ )
				{
					if ( parrAttachments[i] == null )
						continue;

					lbp = new MimeBodyPart();
					lbp.setDataHandler(new DataHandler(new ByteArrayDataSource(parrAttachments[i].getData(),
							parrAttachments[i].getContentType())));
					lbp.setFileName(parrAttachments[i].getFileName());
					lmpMessage.addBodyPart(lbp);
				}

				lmsg.setContent(lmpMessage, lmpMessage.getContentType());
			}

			lmsg.addHeader("MIME-Version", "1.0");
			lmsg.saveChanges();
			lxport.sendMessage(lmsg, lmsg.getAllRecipients());
			lxport.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static MessageData GetAsData(String pstrUniqueID)
		throws BigBangJewelException
	{
		Item lobjItem;
		EmailAddress lobjFrom;
		EmailAddressCollection larrTo;
		EmailAddressCollection larrReplyTo;
		EmailAddressCollection larrCC;
		EmailAddressCollection larrBCC;
		MessageData lobjResult;
		int llngLen;
		int i;

		lobjItem = DoGetItem(pstrUniqueID);

		lobjResult = new MessageData();

		lobjFrom = null;
		larrTo = null;
		larrCC = null;
		larrBCC = null;
		larrReplyTo = null;
		try
		{
			lobjResult.mstrSubject = lobjItem.getSubject();
			lobjResult.midOwner = null;
			lobjResult.mlngNumber = -1;
			lobjResult.mbIsEmail = true;
			lobjResult.mdtDate = new Timestamp(lobjItem.getDateTimeReceived().getTime());
			lobjResult.mstrBody = lobjItem.getBody().toString();

			lobjResult.midDirection = Constants.MsgDir_Incoming;
			if ( lobjItem instanceof EmailMessage )
			{
				lobjFrom = ((EmailMessage)lobjItem).getFrom();
				larrTo = ((EmailMessage)lobjItem).getToRecipients();
				larrCC = ((EmailMessage)lobjItem).getCcRecipients();
				larrBCC = ((EmailMessage)lobjItem).getBccRecipients();
				larrReplyTo = ((EmailMessage)lobjItem).getReplyTo();

				lobjResult.midDirection = ( getLoggedEmail().equalsIgnoreCase(lobjFrom.getAddress()) ?
						Constants.MsgDir_Outgoing : Constants.MsgDir_Incoming );
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		llngLen = ( lobjFrom == null ? 0 : 1) + (larrTo == null ? 0 : larrTo.getCount()) + (larrCC == null ? 0 : larrCC.getCount()) +
				(larrBCC == null ? 0 : larrBCC.getCount()) + (larrReplyTo == null ? 0 : larrReplyTo.getCount());

		if ( llngLen < 1)
			lobjResult.marrAddresses = null;
		else
		{
			lobjResult.marrAddresses = new MessageAddressData[llngLen];
			i = 0;
			if ( lobjFrom != null )
			{
				lobjResult.marrAddresses[i] = GetAddress(lobjFrom, Constants.UsageID_From);
				i++;
			}
			if ( larrTo != null )
			{
				for ( EmailAddress laddr : larrTo )
				{
					lobjResult.marrAddresses[i] = GetAddress(laddr, Constants.UsageID_To);
					i++;
				}
			}
			if ( larrCC != null )
			{
				for ( EmailAddress laddr : larrCC )
				{
					lobjResult.marrAddresses[i] = GetAddress(laddr, Constants.UsageID_CC);
					i++;
				}
			}
			if ( larrBCC != null )
			{
				for ( EmailAddress laddr : larrBCC )
				{
					lobjResult.marrAddresses[i] = GetAddress(laddr, Constants.UsageID_BCC);
					i++;
				}
			}
			if ( larrReplyTo != null )
			{
				for ( EmailAddress laddr : larrReplyTo )
				{
					lobjResult.marrAddresses[i] = GetAddress(laddr, Constants.UsageID_ReplyTo);
					i++;
				}
			}
		}

		return lobjResult;
	}

	public static MessageAddressData GetAddress(EmailAddress pobjSource, UUID pidUsage)
	{
		MessageAddressData lobjResult;

		lobjResult = new MessageAddressData();
		lobjResult.mstrAddress = pobjSource.getAddress();
		lobjResult.midOwner = null;
		lobjResult.midUsage = pidUsage;
		lobjResult.midUser = null;
		lobjResult.midInfo = null;
		lobjResult.mstrDisplay = pobjSource.getName();

		return lobjResult;
	}

	public static InternetAddress BuildAddress(MessageAddressData pobjSource)
		throws BigBangJewelException
	{
		try
		{
			if ( pobjSource.mstrDisplay == null )
				return new InternetAddress(pobjSource.mstrAddress);
			else
				return new InternetAddress(pobjSource.mstrAddress, pobjSource.mstrDisplay);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
