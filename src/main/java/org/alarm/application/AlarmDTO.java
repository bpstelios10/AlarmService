package org.alarm.application;

import org.alarm.domain.Alarm;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record AlarmDTO(Set<DayOfWeek> dayOfTheWeek, LocalTime time, String urlToRingWhenTrigger) {
    public static AlarmDTO fromAlarm(Alarm alarm) {
        return new AlarmDTO(alarm.dayOfTheWeek(), alarm.time(), alarm.urlToRingWhenTrigger());
    }

    public static Alarm toAlarm(AlarmDTO alarmDTO) {
        return new Alarm(alarmDTO.dayOfTheWeek, alarmDTO.time, alarmDTO.urlToRingWhenTrigger());
    }
}
