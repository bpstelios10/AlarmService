package org.alarm.application;

import java.util.Set;

public interface AlarmDSGateway {
    Set<AlarmDTO> getAlarms();

    void addAlarm(AlarmDTO alarm);

    void removeAlarm(AlarmDTO alarm);
}
