package org.alarm.infrastructure.repositories;

import org.alarm.application.AlarmDSGateway;
import org.alarm.application.AlarmDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryAlarmDS implements AlarmDSGateway {

    //this could be a map with the time as key and alarmDTO as value but its good enough for now
    private final Set<AlarmDTO> dataSourceOfAlarms;

    public InMemoryAlarmDS() {
        this.dataSourceOfAlarms = new HashSet<>();
    }

    @Override
    public Set<AlarmDTO> getAlarms() {
        // this could return an unmodifiable list of alarms for security, but its fine for this exercise
        return dataSourceOfAlarms;
    }

    @Override
    public void addAlarm(AlarmDTO alarm) {
        dataSourceOfAlarms.add(alarm);
    }

    @Override
    public void removeAlarm(AlarmDTO alarm) {
        dataSourceOfAlarms.remove(alarm);
    }
}
