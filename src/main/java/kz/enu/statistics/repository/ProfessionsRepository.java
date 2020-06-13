package kz.enu.statistics.repository;

import kz.enu.statistics.domain.Professions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionsRepository extends MongoRepository<Professions, String> {

}
