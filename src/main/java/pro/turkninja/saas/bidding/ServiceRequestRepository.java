package pro.turkninja.saas.bidding;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ServiceRequestRepository extends MongoRepository<ServiceRequest, String> {
    List<ServiceRequest> findByCategoryAndStatusOrderByCreatedAtDesc(String category, RequestStatus status);

}
