package pl.pawel.document.repos;

import org.springframework.data.repository.CrudRepository;
import pl.pawel.document.entities.Document;

public interface DocumentRepository extends CrudRepository<Document, Long> {

}
