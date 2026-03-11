package uk.gov.hmcts.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.tasks.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}

