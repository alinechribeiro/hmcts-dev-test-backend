package uk.gov.hmcts.tasks.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.tasks.dto.TaskRequest;
import uk.gov.hmcts.tasks.dto.TaskResponse;
import uk.gov.hmcts.tasks.dto.UpdateTaskRequest;
import uk.gov.hmcts.tasks.model.Task;
import uk.gov.hmcts.tasks.model.TaskStatus;
import uk.gov.hmcts.tasks.service.TaskService;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService service;

    @InjectMocks
    private TaskController controller;

    @Test
    void create_shouldReturnCreatedTaskResponse() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Create title");
        request.setDescription("Create desc");
        request.setDueAt(OffsetDateTime.now().plusDays(1));

        Task task = new Task();
        task.setId(10L);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.TODO);
        task.setDueAt(request.getDueAt());

        when(service.create(any(TaskRequest.class))).thenReturn(task);

        TaskResponse response = controller.create(request);

        verify(service).create(any(TaskRequest.class));
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("Create title");
        assertThat(response.getDescription()).isEqualTo("Create desc");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void update_shouldReturnUpdatedTaskResponse() {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Updated title");
        request.setDescription("Updated desc");
        request.setDueAt(OffsetDateTime.now().plusDays(2));

        Task task = new Task();
        task.setId(5L);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDueAt(request.getDueAt());

        when(service.update(eq(5L), any(UpdateTaskRequest.class))).thenReturn(task);

        TaskResponse response = controller.update(5L, request);

        verify(service).update(eq(5L), any(UpdateTaskRequest.class));
        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getTitle()).isEqualTo("Updated title");
        assertThat(response.getDescription()).isEqualTo("Updated desc");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void delete_shouldDelegateToService() {
        Long id = 42L;

        controller.delete(id);

        verify(service).delete(id);
    }
}

