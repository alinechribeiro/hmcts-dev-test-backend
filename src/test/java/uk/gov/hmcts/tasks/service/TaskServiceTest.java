package uk.gov.hmcts.tasks.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.gov.hmcts.tasks.dto.TaskRequest;
import uk.gov.hmcts.tasks.dto.UpdateTaskRequest;
import uk.gov.hmcts.tasks.model.Task;
import uk.gov.hmcts.tasks.model.TaskStatus;
import uk.gov.hmcts.tasks.repository.TaskRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    private TaskRepository repository;
    private TaskService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TaskRepository.class);
        service = new TaskService(repository);
    }

    @Test
    void create_shouldPersistTask() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Test");
        request.setDescription("Desc");
        request.setDueAt(OffsetDateTime.now().plusDays(1));

        Task saved = new Task();
        saved.setId(1L);
        saved.setTitle(request.getTitle());
        saved.setDescription(request.getDescription());
        saved.setStatus(TaskStatus.TODO);
        saved.setDueAt(request.getDueAt());

        when(repository.save(any(Task.class))).thenReturn(saved);

        Task result = service.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void getById_notFoundThrows() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void delete_shouldRemoveWhenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        Mockito.verify(repository).deleteById(1L);
    }

    @Test
    void delete_notFoundThrows() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> service.delete(99L));
    }

    @Test
    void update_shouldChangeFields() {
        Task existing = new Task();
        existing.setId(1L);
        existing.setTitle("Old");
        existing.setDescription("Old desc");
        existing.setStatus(TaskStatus.TODO);
        existing.setDueAt(OffsetDateTime.now().plusDays(1));

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("New");
        request.setDescription("New desc");
        request.setDueAt(OffsetDateTime.now().plusDays(2));

        Task updated = service.update(1L, request);

        assertThat(updated.getTitle()).isEqualTo("New");
        assertThat(updated.getDescription()).isEqualTo("New desc");
    }
}

