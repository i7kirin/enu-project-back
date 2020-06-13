package kz.enu.statistics.repository;

import kz.enu.statistics.domain.Employed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployedRepository extends MongoRepository<Employed, String> {
}
