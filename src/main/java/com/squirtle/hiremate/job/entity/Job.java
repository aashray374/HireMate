package com.squirtle.hiremate.job.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job {
    private String jobId;
    private String jobRole;
    private String company;
    private String applicationLink;

    @Override
    public String toString() {
        return  "jobId='" + jobId + '\'' +
                ", jobRole='" + jobRole + '\'' +
                ", company='" + company + '\'' +
                ", summary='" + summary + '\'';
    }

    private String summary;
}
