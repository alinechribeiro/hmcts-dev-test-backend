package uk.gov.hmcts.tasks.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.tasks.dto.TaskRequest;
import uk.gov.hmcts.tasks.dto.TaskResponse;
import uk.gov.hmcts.tasks.dto.UpdateTaskRequest;
import uk.gov.hmcts.tasks.dto.UpdateTaskStatusRequest;
import uk.gov.hmcts.tasks.model.Task;
import uk.gov.hmcts.tasks.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody TaskRequest request) {
        Task created = service.create(request);
        return toResponse(created);
    }

    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id) {
        return toResponse(service.getById(id));
    }

    @GetMapping
    public List<TaskResponse> getAll() {
        return service.getAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        Task updated = service.update(id, request);
        return toResponse(updated);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(@PathVariable Long id,
                                     @Valid @RequestBody UpdateTaskStatusRequest request) {
        Task updated = service.updateStatus(id, request.getStatus());
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setDueAt(task.getDueAt());
        return response;
    }
}

