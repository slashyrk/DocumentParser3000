package org.example;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfImageExtractor extends PDFStreamEngine {


	public static void extractImages(File inputPdf, File outputDir) throws IOException {
		outputDir.mkdirs();

		try (PDDocument document = PDDocument.load(inputPdf)) {
			int pageIndex = 0;
			int imageIndex = 0;

			for (PDPage page : document.getPages()) {
				PDResources resources = page.getResources();

				for (COSName xObjectName : resources.getXObjectNames()) {
					PDXObject xObject = resources.getXObject(xObjectName);

					if (xObject instanceof PDImageXObject) {
						PDImageXObject image = (PDImageXObject) xObject;
						BufferedImage bImage = image.getImage();

						String formatName = image.getSuffix(); // usually "jpg" or "png"
						if (formatName == null) {
							formatName = "png";
						}

						File outFile = new File(outputDir, "page_" + pageIndex + "_img_" + imageIndex + "." + formatName);
						ImageIO.write(bImage, formatName, outFile);
						System.out.println("Saved: " + outFile.getName());
						imageIndex++;
					}
				}
				pageIndex++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
