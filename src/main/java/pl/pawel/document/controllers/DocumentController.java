package pl.pawel.document.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import pl.pawel.document.entities.Document;
import pl.pawel.document.repos.DocumentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class DocumentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    DocumentRepository documentRepository;

    @RequestMapping("/displayUpload")
    public String displayUpload(ModelMap modelMap) {
        LOGGER.info("=== Inside displayUpload()");
        addAllDocumentsToModel(modelMap);
        return "documentUpload";
    }

    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("document") MultipartFile multipartFile, @RequestParam("id") long id, ModelMap modelMap) {
        LOGGER.info("=== Inside uploadDocument()");
        Document document = new Document();
        document.setId(id);
        document.setName(multipartFile.getOriginalFilename());
        try {
            document.setData(multipartFile.getBytes());
        } catch (IOException e) {
            LOGGER.error("=== Exception inside uploadDocument() ", e);
        }
        LOGGER.info("=== document id: {}, document name: {}, document length: {}", document.getId(), document.getName(), document.getData().length);
        documentRepository.save(document);

        addAllDocumentsToModel(modelMap);
        return "documentUpload";
    }

    @RequestMapping("/download")
    public StreamingResponseBody download(@RequestParam("id") long id, HttpServletResponse response) {
        Document document = documentRepository.findById(id).get();
        byte[] data = document.getData();

        String fileName = document.getName();
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        return outputStream -> {
            outputStream.write(data);
        };
    }

    private void addAllDocumentsToModel(ModelMap modelMap) {
        List<Document> documents = documentRepository.findAll();
        LOGGER.info("=== documents size: {}", documents.size());
        modelMap.addAttribute("documents", documents);
    }

}
