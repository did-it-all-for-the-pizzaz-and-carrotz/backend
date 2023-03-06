package tech.carrotly.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.carrotly.restapi.model.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
