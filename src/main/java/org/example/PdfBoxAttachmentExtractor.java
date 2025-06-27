package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationFileAttachment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class PdfBoxAttachmentExtractor {

	public static void extractAttachments(File pdfFile, File outputDir) throws IOException {
		PDDocument document = PDDocument.load(pdfFile);
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		for (PDPage page : document.getPages()) {
			List<PDAnnotation> annotations = page.getAnnotations();
			for (PDAnnotation annotation : annotations) {
				if (annotation instanceof PDAnnotationFileAttachment attach) {
					var fileSpec = (PDComplexFileSpecification) attach.getFile();

					String filename = fileSpec.getFilename();
					InputStream is = fileSpec.getEmbeddedFile().createInputStream();
					File outFile = new File(outputDir, filename);

					try (OutputStream os = new FileOutputStream(outFile)) {
						is.transferTo(os);
						System.out.println("Saved attachment: " + outFile.getAbsolutePath());
					}
				}
			}
		}
		document.close();
	}
}
