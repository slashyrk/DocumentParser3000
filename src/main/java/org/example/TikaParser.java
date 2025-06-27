package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class TikaParser {
	public static void parse(File file) throws Exception {
		Tika tika = new Tika();
		Metadata metadata = new Metadata();
		String text;
		try (InputStream stream = new FileInputStream(file)) {
			text = tika.parseToString(stream, metadata);
		}

		Files.writeString(file.toPath().resolveSibling("extracted_text.txt"), text);
		
		Map<String, String> metaMap = new HashMap<>();
		for (String name : metadata.names()) {
			metaMap.put(name, metadata.get(name));
		}
		ObjectMapper mapper = new ObjectMapper();
		String metadataStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(metaMap);
		Files.writeString(file.toPath().resolveSibling("extracted_meta.txt"), metadataStr);
	}
}
