package pl.dreamcode.errornotifier.errors;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends JpaRepository<Error, Long> {

    List<Error> findByProjectName(String projectName);
    Error findFirstByProjectNameOrderByIdDesc(String projectName);

    @Query(nativeQuery = true, value = "SELECT count(*) = 1 FROM project_errors WHERE project_name = ?1 AND created_at > ?2")
    Boolean shouldSendNotification(String projectName, Instant fromDate);

}
