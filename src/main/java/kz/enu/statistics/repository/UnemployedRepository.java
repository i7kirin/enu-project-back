package kz.enu.statistics.repository;

import kz.enu.statistics.domain.Unemployed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnemployedRepository extends MongoRepository<Unemployed, String> {
}
