package org.alarm.infrastructure.repositories;

import org.alarm.application.AlarmDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Set;

import static java.time.DayOfWeek.SUNDAY;
import static org.assertj.core.api.Assertions.assertThat;

class InMemoryAlarmDSTest {

    private InMemoryAlarmDS inMemoryAlarmDS;

    @BeforeEach
    void setup() {
        inMemoryAlarmDS = new InMemoryAlarmDS();

    }

    @Test
    void getAlarms_succeeds() {
        Set<AlarmDTO> alarms = inMemoryAlarmDS.getAlarms();

        assertThat(alarms).isEmpty();
    }

    @Test
    void addAlarm_succeeds_whenAddingOneThenTwoAndThenSameAlarm() {
        AlarmDTO alarm = new AlarmDTO(Set.of(SUNDAY), LocalTime.now(), "");
        AlarmDTO alarm2 = new AlarmDTO(Set.of(SUNDAY), LocalTime.now(), "");

        inMemoryAlarmDS.addAlarm(alarm);
        Set<AlarmDTO> alarms = inMemoryAlarmDS.getAlarms();

        assertThat(alarms).hasSize(1);

        inMemoryAlarmDS.addAlarm(alarm2);
        alarms = inMemoryAlarmDS.getAlarms();

        assertThat(alarms).hasSize(2);

        inMemoryAlarmDS.addAlarm(alarm);
        alarms = inMemoryAlarmDS.getAlarms();

        assertThat(alarms).hasSize(2);
    }

    @Test
    void removeAlarm_succeeds_whenRemovingNonExistingAlarmAndThenExistingAlarm() {
        AlarmDTO alarm = new AlarmDTO(Set.of(SUNDAY), LocalTime.now(), "");
        AlarmDTO alarm2 = new AlarmDTO(Set.of(SUNDAY), LocalTime.now(), "");

        inMemoryAlarmDS.addAlarm(alarm);
        Set<AlarmDTO> alarms = inMemoryAlarmDS.getAlarms();

        assertThat(alarms).hasSize(1);

        inMemoryAlarmDS.removeAlarm(alarm2);
        alarms = inMemoryAlarmDS.getAlarms();

        assertThat(alarms).hasSize(1);

        inMemoryAlarmDS.removeAlarm(alarm);
        alarms = inMemoryAlarmDS.getAlarms();

        assertThat(alarms).isEmpty();
    }
}
