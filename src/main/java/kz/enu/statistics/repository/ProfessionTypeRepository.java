package kz.enu.statistics.repository;

import kz.enu.statistics.domain.ProfessionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionTypeRepository extends MongoRepository<ProfessionType, String> {
}
