package org.example.springaicustom.vectorstore.milvus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("VectorStore")
@Primary
public class MilvusConfigVectorStore implements CommandLineRunner {
    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private VectorStoreMilvusProperties vectorStoreProperties;

    @Override
    public void run(String... args) throws Exception {
        if (vectorStore.similaritySearch("Sportsman").isEmpty()){
            log.debug("Loading documents into vector store");

            vectorStoreProperties.getTowDocumentsToLoad().
                    forEach(document -> {
                        log.info("loading document in vector DB-{}", document.getFilename());
                        TikaDocumentReader reader = new TikaDocumentReader(document);
                        List<Document> documents = reader.read();
                        TextSplitter splitter = new TokenTextSplitter();
                        List<Document> splitDocuments = splitter.split(documents);
                        vectorStore.add(splitDocuments);
                    });
            log.info("loaded vectors in vector DB-{}", vectorStoreProperties.getTowDocumentsToLoad());
        }
        log.info("Vector store loaded");
    }
}
