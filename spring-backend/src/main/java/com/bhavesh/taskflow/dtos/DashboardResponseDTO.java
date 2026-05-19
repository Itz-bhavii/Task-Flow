package com.bhavesh.taskflow.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponseDTO {
    private Integer assignedTasks;
    private Integer completedTasks;
    private Integer pendingTasks;
    private Integer overdueTasks;
    private Integer projectCounts;
}
