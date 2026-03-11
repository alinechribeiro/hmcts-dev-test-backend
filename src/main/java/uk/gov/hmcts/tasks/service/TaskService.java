package uk.gov.hmcts.tasks.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.tasks.dto.TaskRequest;
import uk.gov.hmcts.tasks.dto.UpdateTaskRequest;
import uk.gov.hmcts.tasks.model.Task;
import uk.gov.hmcts.tasks.model.TaskStatus;
import uk.gov.hmcts.tasks.repository.TaskRepository;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.TODO);
        task.setDueAt(request.getDueAt());
        return repository.save(task);
    }

    public Task getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> getAll() {
        return repository.findAll();
    }

    public Task updateStatus(Long id, TaskStatus status) {
        Task task = getById(id);
        task.setStatus(status);
        return repository.save(task);
    }

    public Task update(Long id, UpdateTaskRequest request) {
        Task task = getById(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueAt(request.getDueAt());
        return repository.save(task);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        repository.deleteById(id);
    }
}

