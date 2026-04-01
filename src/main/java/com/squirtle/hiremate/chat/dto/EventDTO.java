package com.squirtle.hiremate.chat.dto;

import com.squirtle.hiremate.job.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDTO {
    UUID groupId;
    Job job;
}
