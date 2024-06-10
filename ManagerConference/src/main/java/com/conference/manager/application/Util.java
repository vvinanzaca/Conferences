package com.conference.manager.application;

import com.conference.manager.domain.model.ConferenceDay;
import com.conference.manager.domain.model.ConferenceSession;

import java.util.ArrayList;

public class Util {
    public static ConferenceSession getConferenceSession(){
        return ConferenceSession.builder().listConference(new ArrayList<>()).duration(1).build();
    }

    public static ConferenceDay getConferenceDay(){
        return ConferenceDay.builder().morningSession(getConferenceSession())
                .afternoonSession(getConferenceSession()).build();
    }
}
