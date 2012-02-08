package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFOperator;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Template;

public abstract class ReportBase
{
	protected abstract UUID GetTemplateID();
	public abstract FileXfer Generate() throws BigBangJewelException;

	@SuppressWarnings("unchecked")
	protected FileXfer Generate(HashMap<String, String> parrContents)
		throws BigBangJewelException
	{
		Template lobjTemplate;
		FileXfer lobjFile;
		ByteArrayInputStream lobjInput;
		PDDocument lobjDocument;
		List<PDPage> larrPages;
		PDPage lobjPage;
		int i, j, k;
		PDFStreamParser lobjParser;
		List<java.lang.Object> larrTokens;
		java.lang.Object lobjToken;
		PDFOperator lobjOp;
		COSArray larrStrings;
		java.lang.Object lobjElement;
		PDStream lobjStream;
		ContentStreamWriter lobjWriter;
		ByteArrayOutputStream lobjOutput;
		byte[] larrBytes;

		try
		{
			lobjTemplate = Template.GetInstance(Engine.getCurrentNameSpace(), GetTemplateID());
			lobjFile = lobjTemplate.getFile();
			lobjInput = new ByteArrayInputStream(lobjFile.getData());

			lobjDocument = PDDocument.load(lobjInput);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			larrPages = (List<PDPage>)lobjDocument.getDocumentCatalog().getAllPages();

			for ( i = 0; i < larrPages.size(); i++ )
			{
				lobjPage = larrPages.get(i);
				lobjParser = new PDFStreamParser(lobjPage.getContents().getStream());
				lobjParser.parse();

				larrTokens = lobjParser.getTokens();
				for ( j = 0; j < larrTokens.size(); j++ )
				{
					lobjToken = larrTokens.get(j);
					if ( lobjToken instanceof PDFOperator )
					{
						lobjOp = (PDFOperator)lobjToken;
						if ( lobjOp.getOperation().equals("Tj") )
						{
							doReplace((COSString)larrTokens.get(j - 1), parrContents);
						}
						else if ( lobjOp.getOperation().equals("TJ") )
						{
							larrStrings = (COSArray)larrTokens.get(j - 1);
							for ( k = 0; k < larrStrings.size(); k++ )
							{
								lobjElement = larrStrings.get(k);
								if ( lobjElement instanceof COSString )
									doReplace((COSString)lobjElement, parrContents);
							}
						}
					}
				}

				lobjStream = new PDStream(lobjDocument);
				lobjWriter = new ContentStreamWriter(lobjStream.createOutputStream());
				lobjWriter.writeTokens(larrTokens);
				lobjPage.setContents(lobjStream);
			}

			lobjOutput = new ByteArrayOutputStream();
			lobjDocument.save(lobjOutput);
		}
		catch (Throwable e)
		{
			try { lobjDocument.close(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lobjDocument.close();

			larrBytes = lobjOutput.toByteArray();
			lobjInput = new ByteArrayInputStream(larrBytes);
			lobjFile = new FileXfer(larrBytes.length, lobjFile.getContentType(), lobjFile.getFileName(), lobjInput);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjFile;
	}

	private void doReplace(COSString pstrSource, HashMap<String, String> parrContents)
	{
		String lstrResult;
		int i, j;

		lstrResult = pstrSource.getString();

		for ( i = lstrResult.indexOf("{{"); i >= 0; i = lstrResult.indexOf("{{") )
		{
			j = lstrResult.indexOf("}}");
			if ( j < 0 )
			{
				lstrResult = lstrResult.replaceFirst("\\{\\{", "");
				continue;
			}
			if ( j < i )
			{
				lstrResult = lstrResult.replaceFirst("\\}\\}", "");
				continue;
			}
			lstrResult.replaceFirst(lstrResult.substring(i, j + 2), parrContents.get(lstrResult.substring(i + 2, j)));
		}

		pstrSource.reset();
		try
		{
			pstrSource.append(lstrResult.getBytes("ISO-8859-1"));
		}
		catch (Throwable e)
		{
		}
	}
}
