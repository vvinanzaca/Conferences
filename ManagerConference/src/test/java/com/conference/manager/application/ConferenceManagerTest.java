package com.conference.manager.application;

import com.conference.manager.domain.model.Conference;
import com.conference.manager.domain.model.ConferenceDay;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConferenceManagerTest extends TestCase {

    public void testGetListConference() {
        List<String> conferenceDescriptions = Arrays.asList(
                "Conference 1 60min",
                "Conference 2 45min",
                "Conference 3 lightning",
                "Conference 4 30min"
        );

        List<Conference> conferences = ConferenceManager.getListConference(conferenceDescriptions);

        assertEquals(4, conferences.size());
        assertEquals("Conference 1", conferences.get(0).getName());
        assertEquals(60, conferences.get(0).getDuration().intValue());
        assertEquals("Conference 3", conferences.get(2).getName());
        assertEquals(5, conferences.get(2).getDuration().intValue());
    }

    public void testOrganizeConferences() throws CloneNotSupportedException {
        List<Conference> conferences = Arrays.asList(
                new Conference("Conference 1", 60),
                new Conference("Conference 2", 45),
                new Conference("Conference 3", 30),
                new Conference("Conference 4", 45),
                new Conference("Conference 5", 60),
                new Conference("Conference 6", 45),
                new Conference("Conference 7", 30),
                new Conference("Conference 8", 30),
                new Conference("Conference 9", 45),
                new Conference("Conference 10", 60)
        );

        List<Conference> mutableConferences = new ArrayList<>(conferences);
        List<ConferenceDay> conferenceDays = ConferenceManager.organizeConferences(mutableConferences);

        assertNotNull(conferenceDays);
        assertFalse(conferenceDays.isEmpty());

        Integer totalDuration = conferenceDays.stream()
                .mapToInt(day -> {
                    Integer morningDuration = day.getMorningSession().getListConference().stream().mapToInt(Conference::getDuration).sum();
                    Integer afternoonDuration = day.getAfternoonSession().getListConference().stream().mapToInt(Conference::getDuration).sum();
                    return morningDuration + afternoonDuration;
                })
                .sum();

        assertTrue(totalDuration <= (180 + 240) * conferenceDays.size());
    }
}