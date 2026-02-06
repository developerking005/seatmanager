package com.library.seatmanager.controller;

import com.library.seatmanager.dto.DashboardResponse;
import com.library.seatmanager.entity.Student;
import com.library.seatmanager.repository.SeatRepository;
import com.library.seatmanager.repository.StudentRepository;
import com.library.seatmanager.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class DashboardController {

    @Autowired
    private DashboardService service;

    @Autowired
    private SeatRepository seatRepo;

    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/dashboards/{libraryId}")
    public DashboardResponse getDashboard(
            @PathVariable Long libraryId) {

        // total seats of this library
        int totalSeats =
                seatRepo.countByLibraryId(libraryId);

        // full day active students of this library
        long filledSeats =
                studentRepo.countBySeat_Library_IdAndStudentTypeAndActiveTrue(
                        libraryId,
                        Student.StudentType.FULL_DAY
                );

        long halfDayCount =
                studentRepo.countByLibrary_IdAndStudentTypeAndActiveTrue(
                        libraryId,
                        Student.StudentType.HALF_DAY
                );

        long vacantSeats = totalSeats - filledSeats;

        return new DashboardResponse(
                totalSeats,
                filledSeats,
                vacantSeats,
                halfDayCount
        );
    }


}

