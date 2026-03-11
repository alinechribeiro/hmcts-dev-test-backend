package uk.gov.hmcts.tasks.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.tasks.dto.TaskRequest;
import uk.gov.hmcts.tasks.model.Task;
import uk.gov.hmcts.tasks.repository.TaskRepository;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TaskControllerIntegrationTest {

    @Autowired
    private TaskController controller;

    @Autowired
    private TaskRepository repository;

    @Test
    void createTask_returnsCreated() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Integration");
        request.setDescription("Integration test");
        request.setDueAt(OffsetDateTime.now().plusDays(1));

        var response = controller.create(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Integration");

        Task persisted = repository.findById(response.getId()).orElseThrow();
        assertThat(persisted.getTitle()).isEqualTo("Integration");
        assertThat(persisted.getDescription()).isEqualTo("Integration test");
    }
}

