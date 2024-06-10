package org.example;

import com.conference.manager.application.ConferenceManager;
import com.conference.manager.domain.model.Conference;
import com.conference.manager.domain.model.ConferenceDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws CloneNotSupportedException
    {
        List<String> list = new ArrayList<>();
        list.add("Writing Fast Tests Against Enterprise Rails 60min");
        list.add("Overdoing it in Python 45min");
        list.add("Lua for the Masses 30min");
        list.add("Ruby Errors from Mismatched Gem Versions 45min");
        list.add("Common Ruby Errors 45min");
        list.add("Rails for Python Developers lightning");
        list.add("Communicating Over Distance 60min");
        list.add("Accounting-Driven Development 45min");
        list.add("Woah 30min");
        list.add("Sit Down and Write 30min");
        list.add("Pair Programming vs Noise 45min");
        list.add("Rails Magic 60min");
        list.add("Ruby on Rails: Why We Should Move On 60min");
        list.add("Clojure Ate Scala (on my project) 45min");
        list.add("Programming in the Boondocks of Seattle 30min");
        list.add("Ruby vs. Clojure for Back-End Development 30min");
        list.add("Ruby on Rails Legacy App Maintenance 60min");
        list.add("A World Without HackerNews 30min");
        list.add("User Interface CSS in Rails Apps 30min");

        List<ConferenceDay> conferenceDayList = ConferenceManager.organizeConferences(ConferenceManager.getListConference(list));
        printSchedule(conferenceDayList);
    }

    private static void printSchedule(List<ConferenceDay> conferenceDayList) {
        int trackCount = 1;
        for (ConferenceDay track : conferenceDayList) {
            System.out.println("Pista " + trackCount + ":");
            printSession(track.getMorningSession().getListConference(), 9 * 60);
            System.out.println("12:00PM Almuerzo");
            printSession(track.getAfternoonSession().getListConference(), 13 * 60);
            System.out.println("05:00PM Evento de Networking\n");
            trackCount++;
        }
    }

    private static void printSession(List<Conference> session, Integer startTime) {
        for (Conference talk : session) {
            String time = formatTime(startTime);
            System.out.println(time + " " + talk.getName() + " " + (talk.getDuration() == 5 ? "lightning" : talk.getDuration() + "min"));
            startTime += talk.getDuration();
        }
    }

    private static String formatTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        String period = hours < 12 ? "AM" : "PM";
        if (hours > 12) hours -= 12;
        return String.format("%02d:%02d%s", hours, mins, period);
    }
}
