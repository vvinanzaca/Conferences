package com.conference.manager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConferenceDay {
    private ConferenceSession morningSession;
    private ConferenceSession afternoonSession;
}
