package org.alarm.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Set;

import static java.time.DayOfWeek.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlarmServiceTest {

    @Mock
    private AlarmNotifierService alarmNotifier;
    @Mock
    private AlarmDSGateway alarmDSGateway;
    @InjectMocks
    private AlarmService service;

    private final LocalDate today = LocalDate.now();
    private final ZoneId zoneId = ZoneId.of("Europe/Athens");
    private final Set<AlarmDTO> expectedAlarms = Set.of(
            new AlarmDTO(Set.of(MONDAY, TUESDAY, today.getDayOfWeek()), LocalTime.now(), "url1"),
            new AlarmDTO(Set.of(SUNDAY, today.getDayOfWeek()), LocalTime.now().plusHours(48), "url1"));

    @Test
    void getAllAlarms_succeeds_whenAlarmsExist() {
        when(alarmDSGateway.getAlarms()).thenReturn(expectedAlarms);

        Set<AlarmDTO> allAlarms = service.getAllAlarms();

        assertThat(expectedAlarms).containsAll(allAlarms);
        verifyNoMoreInteractions(alarmDSGateway);
    }

    @Test
    void getAllAlarms_succeeds_whenNoAlarmsExist() {
        when(alarmDSGateway.getAlarms()).thenReturn(Set.of());

        Set<AlarmDTO> allAlarms = service.getAllAlarms();

        assertThat(allAlarms).hasSize(0);
        verifyNoMoreInteractions(alarmDSGateway);
    }

    @Test
    void addAlarm_succeeds() {
        AlarmDTO alarmDTO = expectedAlarms.stream().findAny().orElseThrow();
        doNothing().when(alarmDSGateway).addAlarm(alarmDTO);

        assertDoesNotThrow(() -> service.addAlarm(alarmDTO));

        verifyNoMoreInteractions(alarmDSGateway);
    }

    @Test
    void deleteAlarm_succeeds() {
        AlarmDTO alarmDTO = expectedAlarms.stream().findAny().orElseThrow();
        doNothing().when(alarmDSGateway).removeAlarm(alarmDTO);

        assertDoesNotThrow(() -> service.deleteAlarm(alarmDTO));

        verifyNoMoreInteractions(alarmDSGateway);
    }

    @Test
    void checkTime_succeeds_whenAlarmShouldRing() {
        AlarmDTO alarmDTO = expectedAlarms.stream().findAny().orElseThrow();
        LocalDateTime currentDateTime = LocalDateTime.of(today, alarmDTO.time());
        ZonedDateTime existingAlarmDateTime = ZonedDateTime.of(currentDateTime, zoneId);

        try (MockedStatic<ZonedDateTime> utilities = Mockito.mockStatic(ZonedDateTime.class)) {
            utilities.when(() -> ZonedDateTime.now(zoneId)).thenReturn(existingAlarmDateTime);
            when(alarmDSGateway.getAlarms()).thenReturn(expectedAlarms);
            doNothing().when(alarmNotifier).sendNotification(any());

            assertDoesNotThrow(() -> service.checkTime());

            verifyNoMoreInteractions(alarmDSGateway, alarmNotifier);
        }
    }

    @Test
    void checkTime_succeeds_whenNoAlarmWithMatchingTime() {
        AlarmDTO alarmDTO = expectedAlarms.stream().findAny().orElseThrow();
        LocalDateTime currentDateTime = LocalDateTime.of(today, alarmDTO.time().plusHours(1));
        ZonedDateTime existingAlarmDateTime = ZonedDateTime.of(currentDateTime, zoneId);

        try (MockedStatic<ZonedDateTime> utilities = Mockito.mockStatic(ZonedDateTime.class)) {
            utilities.when(() -> ZonedDateTime.now(zoneId)).thenReturn(existingAlarmDateTime);
            when(alarmDSGateway.getAlarms()).thenReturn(expectedAlarms);

            assertDoesNotThrow(() -> service.checkTime());

            verifyNoInteractions(alarmNotifier);
            verifyNoMoreInteractions(alarmDSGateway);
        }
    }

    @Test
    void checkTime_succeeds_whenNoAlarmExists() {
        when(alarmDSGateway.getAlarms()).thenReturn(Set.of());

        assertDoesNotThrow(() -> service.checkTime());

        verifyNoInteractions(alarmNotifier);
        verifyNoMoreInteractions(alarmDSGateway);
    }

    @Test
    void setZoneId_succeeds() {
        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");

        ZoneId zoneIdReturned = service.getZoneId();

        assertThat(zoneId).isEqualTo(zoneIdReturned);

        service.setZoneIdForAlarms(tokyoZone);

        assertThat(tokyoZone).isEqualTo(service.getZoneId());

        verifyNoInteractions(alarmDSGateway, alarmNotifier);
    }
}
