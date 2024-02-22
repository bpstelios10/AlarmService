package org.alarm.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record Alarm(Set<DayOfWeek> dayOfTheWeek, LocalTime time, String urlToRingWhenTrigger) {
}
