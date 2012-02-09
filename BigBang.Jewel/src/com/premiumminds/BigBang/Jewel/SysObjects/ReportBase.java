package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBElement;

import org.docx4j.openpackaging.io.LoadFromZipNG;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Text;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Template;

public abstract class ReportBase
{
	protected abstract UUID GetTemplateID();
	public abstract FileXfer Generate() throws BigBangJewelException;

	protected FileXfer Generate(HashMap<String, String> parrContents)
		throws BigBangJewelException
	{
		Template lobjTemplate;
		FileXfer lobjFile;
		WordprocessingMLPackage lobjDoc;
		List<java.lang.Object> larrTexts;
		Text lobjText;
		ByteArrayOutputStream lobjOut;
		byte[] larrBytes;
		FileXfer lobjResult;
		
		int i;

		try
		{
			lobjTemplate = Template.GetInstance(Engine.getCurrentNameSpace(), GetTemplateID());
			lobjFile = lobjTemplate.getFile();
			lobjDoc =  (WordprocessingMLPackage)(new LoadFromZipNG()).get(new ByteArrayInputStream(lobjFile.getData()));

			larrTexts = lobjDoc.getMainDocumentPart().getJAXBNodesViaXPath("//w:t", true);
			for ( i = 0; i < larrTexts.size(); i++ )
			{
				lobjText = (Text)((JAXBElement<?>)larrTexts.get(i)).getValue();
				lobjText.setValue(doReplace(lobjText.getValue(), parrContents));
			}

			lobjOut = new ByteArrayOutputStream();
			(new SaveToZipFile(lobjDoc)).save(lobjOut);
//			lobjDoc.setFontMapper(new IdentityPlusMapper());
//			(new Conversion(lobjDoc)).output(lobjOut, null);

			larrBytes = lobjOut.toByteArray();
			lobjResult = new FileXfer(larrBytes.length, lobjFile.getContentType(), lobjFile.getFileName(),
					new ByteArrayInputStream(larrBytes));
//			lobjResult = new FileXfer(larrBytes.length, "application/pdf", lobjFile.getFileName().replaceFirst("\\.docx", ".pdf"),
//					new ByteArrayInputStream(larrBytes));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjResult;
	}

	private String doReplace(String pstrText, HashMap<String, String> parrContents)
	{
		int i, j;

		for ( i = pstrText.indexOf("{{"); i >= 0; i = pstrText.indexOf("{{") )
		{
			j = pstrText.indexOf("}}");
			if ( j < 0 )
			{
				pstrText = pstrText.substring(0, i) + pstrText.substring(i + 2);
				continue;
			}
			if ( j < i )
			{
				pstrText = pstrText.substring(0, j) + pstrText.substring(j + 2);
				continue;
			}
			pstrText = pstrText.substring(0, i) + parrContents.get(pstrText.substring(i + 2, j)) + pstrText.substring(j + 2);
		}

		return pstrText;
	}
}
