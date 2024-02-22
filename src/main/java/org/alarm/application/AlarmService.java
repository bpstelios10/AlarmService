package org.alarm.application;

import org.alarm.domain.Alarm;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AlarmService {

    private ZoneId zoneId = ZoneId.of("Europe/Athens");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private final AlarmNotifierService alarmNotifier;
    private final AlarmDSGateway alarmDSGateway;

    public AlarmService(AlarmNotifierService alarmNotifier, AlarmDSGateway alarmDSGateway) {
        this.alarmNotifier = alarmNotifier;
        this.alarmDSGateway = alarmDSGateway;
    }

    public Set<AlarmDTO> getAllAlarms() {
        return alarmDSGateway.getAlarms().stream().collect(Collectors.toUnmodifiableSet());
    }

    public void addAlarm(AlarmDTO alarmDTO) {
        alarmDSGateway.addAlarm(alarmDTO);
    }

    public void deleteAlarm(AlarmDTO alarmDTO) {
        alarmDSGateway.removeAlarm(alarmDTO);
    }

    public void setZoneIdForAlarms(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    @Scheduled(fixedDelay = 60000)
    public void checkTime() {
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DayOfWeek currentDayOfWeek = now.getDayOfWeek();
        LocalTime currentLocalTime = now.toLocalTime();
        System.out.println("Time to check is: [" + currentLocalTime.format(formatter) + "] and day: [" + currentDayOfWeek + "].");

        Optional<Alarm> matchingAlarm = alarmDSGateway.getAlarms().stream()
                .map(AlarmDTO::toAlarm)
                .filter(alarm -> alarm.dayOfTheWeek().contains(currentDayOfWeek)
                        && alarm.time().format(formatter).equals(currentLocalTime.format(formatter)))
                .findAny();

        if (matchingAlarm.isPresent()) {
            System.out.println("alarm rang");

            alarmNotifier.sendNotification(matchingAlarm.get());
        }
    }
}
