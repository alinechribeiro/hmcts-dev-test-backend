package uk.gov.hmcts.tasks.dto;

import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.tasks.model.TaskStatus;

public class UpdateTaskStatusRequest {

    @NotNull
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}

