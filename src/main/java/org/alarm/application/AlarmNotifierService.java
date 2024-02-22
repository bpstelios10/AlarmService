package org.alarm.application;

import org.alarm.domain.Alarm;
import org.springframework.stereotype.Component;

@Component
public class AlarmNotifierService {

    private final AlarmNotifierClient alarmNotifierClient;

    public AlarmNotifierService(AlarmNotifierClient alarmNotifierClient) {
        this.alarmNotifierClient = alarmNotifierClient;
    }

    public void sendNotification(Alarm alarm) {
        alarmNotifierClient.notify(AlarmDTO.fromAlarm(alarm));
    }
}
