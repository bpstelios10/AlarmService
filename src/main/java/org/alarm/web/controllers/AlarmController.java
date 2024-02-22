package org.alarm.web.controllers;

import org.alarm.application.AlarmDTO;
import org.alarm.application.AlarmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@RestController
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmService service;

    public AlarmController(AlarmService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Set<AlarmDTO>> getAllAlarms() {
        Set<AlarmDTO> alarms = service.getAllAlarms();

        return ResponseEntity.ok(alarms);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAlarm(@RequestBody AlarmRequestBody request) {
        AlarmDTO alarm = new AlarmDTO(request.daysOfWeek(), LocalTime.parse(request.time()), request.urlToRingWhenTrigger());
        service.deleteAlarm(alarm);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createAlarm(@RequestBody AlarmRequestBody request) {
        service.addAlarm(request.toDTO());

        return ResponseEntity.ok().build();
    }

    record AlarmRequestBody(Set<DayOfWeek> daysOfWeek, String time, String urlToRingWhenTrigger) {
        AlarmDTO toDTO() {
            return new AlarmDTO(daysOfWeek, LocalTime.parse(time), urlToRingWhenTrigger);
        }
    }
}
