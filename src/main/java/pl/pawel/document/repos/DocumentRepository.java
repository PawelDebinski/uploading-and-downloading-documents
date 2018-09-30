package pl.pawel.document.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pawel.document.entities.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
