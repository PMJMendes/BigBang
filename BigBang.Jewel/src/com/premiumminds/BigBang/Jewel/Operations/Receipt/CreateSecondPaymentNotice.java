package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.DocDataLight;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.MessageAddressData;
import com.premiumminds.BigBang.Jewel.Data.MessageAttachmentData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.PrintSet;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDetail;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.SecondPaymentNoticeReport;

public class CreateSecondPaymentNotice
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID[] marrReceiptIDs;
	public transient ConversationData mobjConvData;

	public boolean mbUseSets;
	public UUID midSet;
	public UUID midSetDocument;
	public UUID midSetDetail;
	public DocOps mobjDocOps;
	public Boolean mbTryEmail;

	private UUID midClient;
//	private OutgoingMessageData mobjMessage;

	public CreateSecondPaymentNotice(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_CreateSecondPaymentNotice;
	}

	public String ShortDesc()
	{
		return "Envio de Segundo Aviso";
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
		PrintSet lobjSet;
		PrintSetDocument lobjSetClient;
		PrintSetDetail lobjSetReceipt;

		try
		{
			midClient = ((Receipt)GetProcess().GetData()).getClient().getKey();

			if ( mobjDocOps == null )
				generateDocOp();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());

		if ( mbUseSets )
		{
			if ( mbTryEmail == null )
				mbTryEmail = checkEmail();

			try
			{
				if ( midSet == null )
				{
					lobjSet = PrintSet.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjSet.setAt(0, Constants.TID_SecondPaymentNotice);
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
					lobjSetClient.setAt(1, mobjDocOps.marrCreate2[0].mobjFile);
					lobjSetClient.setAt(2, mbTryEmail);
					lobjSetClient.SaveToDb(pdb);
					midSetDocument = lobjSetClient.getKey();
				}

				lobjSetReceipt = PrintSetDetail.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjSetReceipt.setAt(0, midSetDocument);
				lobjSetReceipt.setAt(1, null);
				lobjSetReceipt.SaveToDb(pdb);
				midSetDetail = lobjSetReceipt.getKey();
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	private void generateDocOp()
		throws BigBangJewelException
	{
		SecondPaymentNoticeReport lrepPN;
		FileXfer lobjFile;
		DocDataLight lobjDoc;

		lrepPN = new SecondPaymentNoticeReport();
		lrepPN.midClient = midClient;
		lrepPN.marrReceiptIDs = marrReceiptIDs;
		lobjFile = lrepPN.Generate();

		lobjDoc = new DocDataLight();
		lobjDoc.mstrName = "Aviso de Cobrança";
		lobjDoc.midOwnerType = Constants.ObjID_Receipt;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_SecondPaymentNotice;
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
		mobjDocOps.marrCreate2 = new DocDataLight[]{lobjDoc};
	}

	private boolean checkEmail()
	{
		Client lobjClient;
		ContactInfo[] larrInfo;
		int i;

		try
		{
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), midClient);
			if ( !Constants.ProfID_Email.equals(lobjClient.getProfile()) && !Constants.ProfID_EmailNoDAS.equals(lobjClient.getProfile()) )
				return false;
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

		mobjConvData = new ConversationData();
		mobjConvData.mstrSubject = "Envio de Segundo Aviso de Cobrança";
		mobjConvData.midType = Constants.ConvTpID_PaymentNotice;
		mobjConvData.midProcess = null;
		mobjConvData.midStartDir = Constants.MsgDir_Outgoing;
		mobjConvData.midPendingDir = null;
		mobjConvData.mdtDueDate = null;

		mobjConvData.marrMessages = new MessageData[] {new MessageData()};
		mobjConvData.marrMessages[0].mstrSubject = "Envio de Segundo Aviso de Cobrança";
		mobjConvData.marrMessages[0].midOwner = null;
		mobjConvData.marrMessages[0].mlngNumber = 0;
		mobjConvData.marrMessages[0].midDirection = Constants.MsgDir_Outgoing;
		mobjConvData.marrMessages[0].mbIsEmail = true;
		mobjConvData.marrMessages[0].mdtDate = new Timestamp(new Date().getTime());
		mobjConvData.marrMessages[0].mstrBody = " ";

		mobjConvData.marrMessages[0].marrAddresses = new MessageAddressData[larrInfo.length + 1];
		// mobjConvData.marrMessages[0].marrAddresses[0] = getSelf(Constants.UsageID_From);
		mobjConvData.marrMessages[0].marrAddresses[0] = getSelf(Constants.UsageID_BCC);

		for ( i = 0; i < larrInfo.length; i++ )
		{
			mobjConvData.marrMessages[0].marrAddresses[i + 1] = new MessageAddressData();
			mobjConvData.marrMessages[0].marrAddresses[i + 1].mstrAddress = (String)larrInfo[i].getAt(ContactInfo.I.VALUE);
			mobjConvData.marrMessages[0].marrAddresses[i + 1].midOwner = null;
			mobjConvData.marrMessages[0].marrAddresses[i + 1].midUsage = Constants.UsageID_To;
			mobjConvData.marrMessages[0].marrAddresses[i + 1].midUser = null;
			mobjConvData.marrMessages[0].marrAddresses[i + 1].midInfo = larrInfo[i].getKey();
			try
			{
				mobjConvData.marrMessages[0].marrAddresses[i + 1].mstrDisplay = larrInfo[i].getOwner().getLabel();
			}
			catch (Throwable e)
			{
				mobjConvData.marrMessages[0].marrAddresses[i + 1].mstrDisplay = null;
			}
		}

		mobjConvData.marrMessages[0].marrAttachments = new MessageAttachmentData[] {new MessageAttachmentData()};
		mobjConvData.marrMessages[0].marrAttachments[0].midOwner = null;
		mobjConvData.marrMessages[0].marrAttachments[0].midDocument = mobjDocOps.marrCreate2[0].mid;

		return true;
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
}
