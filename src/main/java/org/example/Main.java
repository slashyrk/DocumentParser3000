package org.example;

import java.io.File;

public class Main {

	public static void main(String[] args) throws Exception {
		File input = new File("document.pdf");

		// Extract text + metadata
		TikaParser.parse(input);

		// Extract attachments if it's a PDF
		if (input.getName().endsWith(".pdf")) {
			PdfBoxAttachmentExtractor.extractAttachments(input, new File("output"));
			PdfImageExtractor.extractImages(input, new File("output/images"));
		}
	}
}