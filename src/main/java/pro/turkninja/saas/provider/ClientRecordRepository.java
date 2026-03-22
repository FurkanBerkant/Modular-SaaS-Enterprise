package pro.turkninja.saas.provider;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClientRecordRepository extends MongoRepository<ClientRecord, String> {
    Optional<ClientRecord> findByProviderIdAndCustomerId(String providerId, String customerId);
}
