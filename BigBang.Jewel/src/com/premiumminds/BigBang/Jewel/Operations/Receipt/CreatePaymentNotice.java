package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Data.MessageAddressData;
import com.premiumminds.BigBang.Jewel.Data.MessageAttachmentData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PrintSet;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDetail;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.PaymentNoticeReport;

public class CreatePaymentNotice
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public transient UUID[] marrReceiptIDs;
	public transient ConversationData mobjConvData;

	public boolean mbUseSets;
	public UUID midSet;
	public UUID midSetDocument;
	public UUID midSetDetail;
	public DocOps mobjDocOps;
	public boolean mbTryEmail;

	private UUID midClient;
//	private OutgoingMessageData mobjMessage;

	public CreatePaymentNotice(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_CreatePaymentNotice;
	}

	public String ShortDesc()
	{
		return "Envio de Aviso de Cobrança";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi gerado um aviso de cobrança para este recibo.");
		lstrBuilder.append(pstrLineBreak).append(pstrLineBreak);

		mobjDocOps.LongDesc(lstrBuilder, pstrLineBreak);

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		UUID lidProfile;
		PrintSet lobjSet;
		PrintSetDocument lobjSetClient;
		PrintSetDetail lobjSetReceipt;
		Document lobjStamped;
		FileXfer lobjFile;
		Receipt lobjReceipt;
		Client lobjClient;
		com.premiumminds.BigBang.Jewel.Operations.Client.CreateConversation lopCCC;

		lobjReceipt = (Receipt)GetProcess().GetData();

		try
		{
			midClient = lobjReceipt.getClient().getKey();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( !mbUseSets )
		{
			try
			{
				lidProfile = ((Receipt)GetProcess().GetData()).getProfile();
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
			mbTryEmail = Constants.ProfID_Email.equals(lidProfile) || Constants.ProfID_EmailNoDAS.equals(lidProfile);
		}

		if ( mbTryEmail )
			mbTryEmail = checkEmail();

		if ( mobjDocOps == null )
		{
			try
			{
				generateDocOp(mbTryEmail);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
			mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());
			if ( mbTryEmail )
				addAttachment(mobjDocOps.marrCreate[0].mid);
		}
		else
			mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());

		try
		{
			lobjReceipt.setAt(Receipt.I.STATUS, Constants.StatusID_Payable);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}


		try
		{
			lobjFile = null;
			lobjStamped = ((Receipt)GetProcess().GetData()).getStamped(pdb);
			if ( lobjStamped != null )
			{
		    	if ( lobjStamped.getAt(Document.I.FILE) instanceof FileXfer )
		    		lobjFile = (FileXfer)lobjStamped.getAt(Document.I.FILE);
		    	else
		    		lobjFile = new FileXfer((byte[])lobjStamped.getAt(Document.I.FILE));
		    	if ( mbTryEmail )
		    		addAttachment(lobjStamped.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mbUseSets && !mbTryEmail )
		{
			try
			{
				if ( midSet == null )
				{
					lobjSet = PrintSet.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjSet.setAt(0, Constants.TID_PaymentNotice);
					lobjSet.setAt(1, new Timestamp(new java.util.Date().getTime()));
					lobjSet.setAt(2, Engine.getCurrentUser());
					lobjSet.setAt(3, (Timestamp)null);
					lobjSet.SaveToDb(pdb);
					midSet = lobjSet.getKey();
				}

				if ( midSetDocument == null )
				{
					lobjSetClient = PrintSetDocument.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjSetClient.setAt(0, midSet);
					lobjSetClient.setAt(1, mobjDocOps.marrCreate[0].mobjFile);
					lobjSetClient.setAt(2, false);
					lobjSetClient.SaveToDb(pdb);
					midSetDocument = lobjSetClient.getKey();
				}

				lobjSetReceipt = PrintSetDetail.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjSetReceipt.setAt(0, midSetDocument);
				lobjSetReceipt.setAt(1, (lobjFile == null ? null : lobjFile.GetVarData()));
				lobjSetReceipt.SaveToDb(pdb);
				midSetDetail = lobjSetReceipt.getKey();
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		if ( !mbUseSets && mbTryEmail && (mobjConvData != null) )
		{
			try
			{
				lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), midClient);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
			lopCCC = new com.premiumminds.BigBang.Jewel.Operations.Client.CreateConversation(lobjClient.GetProcessID());
			lopCCC.mobjData = mobjConvData;
			lopCCC.Execute(pdb);
		}
	}

	public boolean LocalCanUndo()
	{
		return false;
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O aviso de cobrança será cancelado. A documentação gerada será mantida, para preservar o histórico.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O aviso de cobrança foi cancelado.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}

	private void generateDocOp(boolean pbForEmail)
		throws BigBangJewelException
	{
		PaymentNoticeReport lrepPN;
		FileXfer lobjFile;
		DocumentData lobjDoc;

		lrepPN = new PaymentNoticeReport();
		lrepPN.midClient = midClient;
		lrepPN.marrReceiptIDs = marrReceiptIDs;
		lrepPN.mbForEmail = pbForEmail;
		lobjFile = lrepPN.Generate();

		lobjDoc = new DocumentData();
		lobjDoc.mstrName = "Aviso de Cobrança";
		lobjDoc.midOwnerType = Constants.ObjID_Receipt;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_PaymentNotice;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFile.GetVarData();
		lobjDoc.marrInfo = new DocInfoData[2];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número de Recibos";
		lobjDoc.marrInfo[0].mstrValue = Integer.toString(lrepPN.mlngCount);
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Total a Liquidar";
		lobjDoc.marrInfo[1].mstrValue = String.format("%,.2f", lrepPN.mdblTotal);

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate = new DocumentData[]{lobjDoc};
	}

	private boolean checkEmail()
	{
		Client lobjClient;
		ContactInfo[] larrInfo;
		int i;

		try
		{
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), midClient);
		}
		catch (Throwable e)
		{
			return false;
		}

		larrInfo = getRelevantEmails(lobjClient);
		if ( larrInfo == null )
			return false;

		if ( getSelf(Constants.UsageID_From) == null )
			return false;

		if ( mobjConvData == null )
		{
			mobjConvData = new ConversationData();
			mobjConvData.mstrSubject = "Envio de Aviso(s) de Cobrança";
			mobjConvData.midType = Constants.ConvTpID_PaymentNotice;
			mobjConvData.midProcess = null;
			mobjConvData.midStartDir = Constants.MsgDir_Outgoing;
			mobjConvData.midPendingDir = null;
			mobjConvData.mdtDueDate = null;

			mobjConvData.marrMessages = new MessageData[] {new MessageData()};
			mobjConvData.marrMessages[0].mstrSubject = "Envio de Aviso(s) de Cobrança";
			mobjConvData.marrMessages[0].midOwner = null;
			mobjConvData.marrMessages[0].mlngNumber = 0;
			mobjConvData.marrMessages[0].midDirection = Constants.MsgDir_Outgoing;
			mobjConvData.marrMessages[0].mbIsEmail = true;
			mobjConvData.marrMessages[0].mdtDate = new Timestamp(new Date().getTime());
			mobjConvData.marrMessages[0].mstrBody = getBody();

			mobjConvData.marrMessages[0].marrAddresses = new MessageAddressData[larrInfo.length + 2];
			mobjConvData.marrMessages[0].marrAddresses[0] = getSelf(Constants.UsageID_From);
			mobjConvData.marrMessages[0].marrAddresses[1] = getSelf(Constants.UsageID_BCC);

			for ( i = 0; i < larrInfo.length; i++ )
			{
				mobjConvData.marrMessages[0].marrAddresses[i + 2] = new MessageAddressData();
				mobjConvData.marrMessages[0].marrAddresses[i + 2].mstrAddress = (String)larrInfo[i].getAt(ContactInfo.I.VALUE);
				mobjConvData.marrMessages[0].marrAddresses[i + 2].midOwner = null;
				mobjConvData.marrMessages[0].marrAddresses[i + 2].midUsage = Constants.UsageID_To;
				mobjConvData.marrMessages[0].marrAddresses[i + 2].midUser = null;
				mobjConvData.marrMessages[0].marrAddresses[i + 2].midInfo = larrInfo[i].getKey();
				try
				{
					mobjConvData.marrMessages[0].marrAddresses[i + 2].mstrDisplay = larrInfo[i].getOwner().getLabel();
				}
				catch (Throwable e)
				{
					mobjConvData.marrMessages[0].marrAddresses[i + 2].mstrDisplay = null;
				}
			}

			mobjConvData.marrMessages[0].marrAttachments = new MessageAttachmentData[0];
		}

		return true;
	}

	private void addAttachment(UUID pidDoc)
	{
		MessageAttachmentData[] larrAtts;

		larrAtts = mobjConvData.marrMessages[0].marrAttachments;

		mobjConvData.marrMessages[0].marrAttachments = Arrays.copyOf(larrAtts, larrAtts.length + 1);
		mobjConvData.marrMessages[0].marrAttachments[larrAtts.length] = new MessageAttachmentData();
		mobjConvData.marrMessages[0].marrAttachments[larrAtts.length].midOwner = null;
		mobjConvData.marrMessages[0].marrAttachments[larrAtts.length].midDocument = pidDoc;
	}

	private ContactInfo[] getRelevantEmails(Client pobjClient)
	{
		Client lobjClient;
		Contact[] larrContacts;
		ArrayList<ContactInfo> larrInfo;
		ContactInfo[] larrAux;
		int i, j;

		try
		{
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), midClient);
			larrContacts = lobjClient.GetContactsByType(Constants.CtTypeID_PaymentNotices);
		}
		catch (Throwable e)
		{
			return null;
		}
		if ( (larrContacts == null) || (larrContacts.length == 0) )
			return null;

		larrInfo = new ArrayList<ContactInfo>();
		for ( i = 0; i < larrContacts.length; i++ )
		{
			try
			{
				larrAux = larrContacts[i].getCurrentInfo();
			}
			catch (Throwable e)
			{
				continue;
			}
			if ( larrAux != null )
			{
				for ( j = 0; j < larrAux.length; j++ )
				{
					if ( Constants.CInfoID_Email.equals(larrAux[j].getAt(ContactInfo.I.TYPE)) )
						larrInfo.add(larrAux[j]);
				}
			}
		}

		if ( larrInfo.size() == 0 )
			return null;

		return larrInfo.toArray(new ContactInfo[larrInfo.size()]);
	}

	private MessageAddressData getSelf(UUID pidUsage)
	{
		MessageAddressData lobjResult;
		User lobjUser;
		UserDecoration lobjDeco;

		try
		{
			lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser());
			lobjDeco = UserDecoration.GetByUserID(Engine.getCurrentNameSpace(), Engine.getCurrentUser());
		}
		catch (Throwable e)
		{
			return null;
		}
		if ( lobjDeco == null )
			return null;

		lobjResult = new MessageAddressData();
		lobjResult.mstrAddress = (String)lobjDeco.getAt(UserDecoration.I.EMAIL);
		lobjResult.midOwner = null;
		lobjResult.midUsage = pidUsage;
		lobjResult.midUser = Engine.getCurrentUser();
		lobjResult.midInfo = null;
		lobjResult.mstrDisplay = lobjUser.getDisplayName();

		return lobjResult;
	}

	private String getBody()
	{
		StringBuilder lstrBuffer;
		int i;
		Receipt lobjRec;
		SubPolicy lobjSubP;
		Policy lobjP;
		Company lobjComp;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("<table border=\"1\">");

		lstrBuffer.append("<tr>");
		lstrBuffer.append("<th>Apólice</th>");
		lstrBuffer.append("<th>Recibo</th>");
		lstrBuffer.append("<th>Ramo</th>");
		lstrBuffer.append("<th>Comp</th>");
		lstrBuffer.append("<th>Vigência</th>");
		lstrBuffer.append("<th>Até</th>");
		lstrBuffer.append("<th>Prémio</th>");
		lstrBuffer.append("<th>Dt. Lim.</th>");
		lstrBuffer.append("<th>Descrição</th>");
		lstrBuffer.append("</tr>");

		for ( i = 0; i < marrReceiptIDs.length; i++ )
		{
			try
			{
				lobjRec = Receipt.GetInstance(Engine.getCurrentNameSpace(), marrReceiptIDs[i]);
				lobjSubP = lobjRec.getSubPolicy();
				lobjP = lobjRec.getAbsolutePolicy();
				lobjComp = lobjP.GetCompany();
			}
			catch (Throwable e)
			{
				continue;
			}

			lstrBuffer.append("<tr>");
			lstrBuffer.append("<td>")
					.append(((lobjSubP == null ? lobjP.getLabel() : lobjSubP.getLabel()) + "                    ").substring(0, 20))
					.append("</td>");
			lstrBuffer.append("<td>")
					.append((lobjRec.getLabel() + "                    ").substring(0, 20))
					.append("</td>");
			lstrBuffer.append("<td>")
					.append((lobjP.GetSubLine().getDescription() + "                    ").substring(0, 20))
					.append("</td>");
			lstrBuffer.append("<td>")
					.append((((String)(lobjComp.getAt(Company.I.ACRONYM))) + "      ").substring(0, 6))
					.append("</td>");
			lstrBuffer.append("<td>")
					.append(((lobjRec.getAt(Receipt.I.MATURITYDATE) == null ? "" :
						((Timestamp)lobjRec.getAt(Receipt.I.MATURITYDATE)).toString().substring(0, 10)) + "          ").substring(0, 10))
					.append("</td>");
			lstrBuffer.append("<td>")
					.append(((lobjRec.getAt(Receipt.I.ENDDATE) == null ? "" :
						((Timestamp)lobjRec.getAt(Receipt.I.ENDDATE)).toString().substring(0, 10)) + "          ").substring(0, 10))
					.append("</td>");
			lstrBuffer.append("<td>")
					.append((String.format("%,.2f", (BigDecimal)lobjRec.getAt(Receipt.I.TOTALPREMIUM)) + "                    ").substring(0, 20))
					.append("</td>");
			lstrBuffer.append("<td>")
					.append((lobjRec.getExternalDueDate() + "          ").substring(0, 10))
					.append("</td>");
			lstrBuffer.append("<td>")
					.append(((lobjRec.getAt(Receipt.I.DESCRIPTION) == null ? "" :
						((String)lobjRec.getAt(Receipt.I.DESCRIPTION))) + "                         ").substring(0, 25))
					.append("</td>");
			lstrBuffer.append("</tr>");
		}

		lstrBuffer.append("</table>");

		return lstrBuffer.toString();
	}
}
