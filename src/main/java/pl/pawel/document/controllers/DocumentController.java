package pl.pawel.document.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.pawel.document.entities.Document;
import pl.pawel.document.repos.DocumentRepository;

import java.io.IOException;

@Controller
public class DocumentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    DocumentRepository documentRepository;

    @RequestMapping("/displayUpload")
    public String displayUpload() {
        LOGGER.info("=== Inside displayUpload()");
        return "documentUpload";
    }

    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("document") MultipartFile multipartFile,@RequestParam("id") long id) {
        LOGGER.info("=== Inside uploadDocument()");
        Document document = new Document();
        document.setId(id);
        document.setName(multipartFile.getName());
        try {
            document.setData(multipartFile.getBytes());
        } catch (IOException e) {
            LOGGER.error("=== Exception inside uploadDocument() ", e);
        }
        LOGGER.info("=== document id: {}, document name: {}, document length: {}", document.getId(), document.getName(), document.getData().length);
        documentRepository.save(document);
        return "documentUpload";
    }
}
