package kz.enu.statistics.repository;

import kz.enu.statistics.domain.Information;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InformationRepository extends MongoRepository<Information, String> {
    Optional<List<Information>> findAllByCityId(String cityId);
    Information findByYearAndAndCity_Id(Integer year, String id);
}
