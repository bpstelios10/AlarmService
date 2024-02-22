package org.alarm.web.controllers;

import org.alarm.application.AlarmDTO;
import org.alarm.application.AlarmService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.Set;

import static java.time.DayOfWeek.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class AlarmControllerTest {

    @Mock
    private AlarmService service;
    @InjectMocks
    private AlarmController controller;

    private final Set<AlarmDTO> expectedAlarms = Set.of(
            new AlarmDTO(Set.of(MONDAY, TUESDAY), LocalTime.now(), "url1"),
            new AlarmDTO(Set.of(SUNDAY), LocalTime.now().plusHours(48), "url1"));

    @Test
    void getAllAlarms_succeeds_whenAlarmsExist() {
        when(service.getAllAlarms()).thenReturn(expectedAlarms);

        ResponseEntity<Set<AlarmDTO>> allAlarmsResponse = controller.getAllAlarms();

        assertThat(allAlarmsResponse.getStatusCode()).isEqualTo(OK);
        assertThat(allAlarmsResponse.getBody()).containsAll(expectedAlarms);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllAlarms_succeeds_whenAlarmsDontExist() {
        when(service.getAllAlarms()).thenReturn(Set.of());

        ResponseEntity<Set<AlarmDTO>> allAlarmsResponse = controller.getAllAlarms();

        assertThat(allAlarmsResponse.getStatusCode()).isEqualTo(OK);
        assertThat(allAlarmsResponse.getBody()).isEmpty();
        verifyNoMoreInteractions(service);
    }

    @Test
    void createAlarm_succeeds() {
        AlarmController.AlarmRequestBody newAlarmRequest = new AlarmController.AlarmRequestBody(Set.of(MONDAY, TUESDAY), "10:05", "url1");
        doNothing().when(service).addAlarm(newAlarmRequest.toDTO());

        ResponseEntity<Void> allAlarmsResponse = controller.createAlarm(newAlarmRequest);

        assertThat(allAlarmsResponse.getStatusCode()).isEqualTo(OK);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deleteAlarm_succeeds() {
        AlarmDTO existingAlarm = expectedAlarms.stream().findAny().orElseThrow();
        AlarmController.AlarmRequestBody newAlarmRequest = new AlarmController.AlarmRequestBody(existingAlarm.dayOfTheWeek(), existingAlarm.time().toString(), existingAlarm.urlToRingWhenTrigger());
        doNothing().when(service).deleteAlarm(existingAlarm);

        ResponseEntity<Void> allAlarmsResponse = controller.deleteAlarm(newAlarmRequest);

        assertThat(allAlarmsResponse.getStatusCode()).isEqualTo(OK);
        verifyNoMoreInteractions(service);
    }
}
