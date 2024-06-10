package com.conference.manager.application;

import com.conference.manager.domain.model.Conference;
import com.conference.manager.domain.model.ConferenceSession;
import com.conference.manager.domain.model.ConferenceDay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConferenceManager {

    private static final Pattern PATTERN = Pattern.compile("(\\d+min|lightning)$");
    private static final BigDecimal RATE = new BigDecimal("0.99");
    private static final BigDecimal INITIAL_PARAMETER = new BigDecimal("9999990");
    private static final BigDecimal ONE = new BigDecimal("1");
    private static final Integer MORNING_SESSION_TIME = 180;
    private static final Integer AFTERNOON_SESSION_MAX_TIME = 240;
    public static List<Conference> getListConference(List<String> list) {
        List<Conference> listConference = new ArrayList<>();
        list.forEach(obj -> {
            Matcher matcher = PATTERN.matcher(obj);
            if (matcher.find()) {
                Integer duration = matcher.group(1).equals("lightning") ? 5 : Integer.parseInt(matcher.group(1).replace("min", ""));
                listConference.add(new Conference(obj.substring(0, matcher.start()).trim(), duration));
            }
        });
        return listConference;
    }

    public static List<ConferenceDay> organizeConferences(List<Conference> listConferences) {
        BigDecimal temperature = INITIAL_PARAMETER;
        List<ConferenceDay> currentConference = createDayConference(listConferences);
        List<ConferenceDay> bestCombination = currentConference;
        while (temperature.compareTo(ONE) > 0) {
            List<ConferenceDay> newDayConference = findSolution(new ArrayList<>(currentConference));

            if (acceptanceProbability(totalConferenceDayDuration(currentConference), totalConferenceDayDuration(newDayConference), temperature) > Math.random()) {
                currentConference = newDayConference;
            }

            if (totalConferenceDayDuration(currentConference) > totalConferenceDayDuration(bestCombination)) {
                bestCombination = currentConference;
            }

            temperature = temperature.multiply(RATE);
        }

        return bestCombination;
    }

    private static List<ConferenceDay> createDayConference(List<Conference> listConference) {
        List<ConferenceDay> listConferenceDay = new ArrayList<>();
        Collections.shuffle(listConference);
        while (!listConference.isEmpty()) {
            ConferenceDay dayConferences = Util.getConferenceDay();
            sessionInstance(listConference, dayConferences.getMorningSession(), MORNING_SESSION_TIME);
            sessionInstance(listConference, dayConferences.getAfternoonSession(), AFTERNOON_SESSION_MAX_TIME);
            listConferenceDay.add(dayConferences);
        }
        return listConferenceDay;
    }

    private static void sessionInstance(List<Conference> listConference, ConferenceSession conferenceSession,
                                        Integer duration) {
        conferenceSession.setDuration(duration);
        Integer[] currentSum = {0};
        List<Conference> sessionConference = listConference.stream()
                .filter(obj -> {
                    if (currentSum[0] + obj.getDuration() <= duration) {
                        currentSum[0] += obj.getDuration();
                        return true;
                    } else {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        sessionConference.forEach(conference -> {
            conferenceSession.getListConference().add(conference);
            listConference.remove(conference);
        });
    }

    private static List<ConferenceDay> findSolution(List<ConferenceDay> listdayConference) {
        Random random = new Random();
        Integer trackIndex = random.nextInt(listdayConference.size());
        ConferenceDay conferenceDay = listdayConference.get(trackIndex);

        if (!conferenceDay.getMorningSession().getListConference().isEmpty() &&
                !conferenceDay.getAfternoonSession().getListConference().isEmpty()) {
            List<Conference> listConference = new ArrayList<>();
            listConference.addAll(conferenceDay.getMorningSession().getListConference());
            listConference.addAll(conferenceDay.getAfternoonSession().getListConference());
            Collections.shuffle(listConference);
            conferenceDay.getMorningSession().getListConference().clear();
            conferenceDay.getAfternoonSession().getListConference().clear();

            sessionInstance(listConference, conferenceDay.getMorningSession(), MORNING_SESSION_TIME);
            sessionInstance(listConference, conferenceDay.getAfternoonSession(), AFTERNOON_SESSION_MAX_TIME);
        }

        return listdayConference;
    }

    private static Integer totalConferenceDayDuration(List<ConferenceDay> listConferenceDay) {
        Integer totalDuration = 0;
        for (ConferenceDay conferenceDay : listConferenceDay) {
            totalDuration += conferenceDay.getMorningSession().getListConference().stream()
                    .mapToInt(conference -> conference.getDuration()).sum();
            totalDuration += conferenceDay.getAfternoonSession().getListConference().stream()
                    .mapToInt(conference -> conference.getDuration()).sum();
        }
        return totalDuration;
    }

    private static double acceptanceProbability(Integer currentDuration, Integer newDuration, BigDecimal temperature) {
        if (newDuration > currentDuration) {
            return 1.0;
        }
        return Math.exp(new BigDecimal(newDuration - currentDuration).divide(temperature).doubleValue());
    }
}
