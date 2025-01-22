package org.example.springaicustom.vectorstore.milvus;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "customaiapp.milvusaiapp")
public class VectorStoreMilvusProperties {
    private String vectorStorePath;
    private List<Resource> documentsToLoad;
}
