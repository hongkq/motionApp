package com.seproject.neagaze.models;

/**
 * Created by neaGaze on 10/22/16.
 */

public class Schedule {
    public String name;
    public String surname;
    public String email;
    public static final String NAME_PREFIX = "Name_";
    public static final String SURNAME_PREFIX = "Surname_";
    public static final String EMAIL_PREFIX = "email_";

    public int scheduleId;
    public String Sport, Match, Date, Time, Venue, Winner, Score;
}
