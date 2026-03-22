package pro.turkninja.saas.provider;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "client_records")
@Data
public class ClientRecord {

    public record PrivateNote(String content, Instant createdAt) {}

    @Id
    private String id;

    private String providerId;
    private String customerId;

    private List<String> tags = new ArrayList<>();

    private List<PrivateNote> privateNotes = new ArrayList<>();

    public void addNote(String noteContent) {
        if(noteContent == null || noteContent.isBlank()) {
            throw new IllegalArgumentException("Not içeriği boş olamaz.");
        }
        this.privateNotes.add(new PrivateNote(noteContent, Instant.now()));
    }
}
