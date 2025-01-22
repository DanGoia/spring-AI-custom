package org.example.springaicustom.vectorstore.json;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

@Slf4j
@Configuration
public class VectorStoreConfig {

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel, VectorStoreProperties vectorStoreProperties) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = new File(vectorStoreProperties.getVectorStorePath());

        if (vectorStoreFile.exists()) {
            simpleVectorStore.load(vectorStoreFile);
        } else {
            vectorStoreProperties.getDocumentsToLoad().
                    forEach(document -> {
                        log.info("loading document in vector DB-{}", document.getFilename());
                        TikaDocumentReader reader = new TikaDocumentReader(document);
                        List<Document> documents = reader.read();
                        TextSplitter splitter = new TokenTextSplitter();
                        List<Document> splitDocuments = splitter.split(documents);
                        simpleVectorStore.add(splitDocuments);
                    });
            simpleVectorStore.save(vectorStoreFile);

        }
        return simpleVectorStore;
    }
}
