package com.library.seatmanager.controller;

import com.library.seatmanager.dto.BookingRequest;
import com.library.seatmanager.entity.Library;
import com.library.seatmanager.entity.Seat;
import com.library.seatmanager.entity.Student;
import com.library.seatmanager.repository.LibraryRepository;
import com.library.seatmanager.repository.SeatRepository;
import com.library.seatmanager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
@CrossOrigin
public class BookingController {

    @Autowired
    private SeatRepository seatRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private LibraryRepository libraryRepo;

    @PostMapping()
    public ResponseEntity<String> bookSeat(@RequestBody BookingRequest req) {

        // finding library
        Library library = libraryRepo.findById(req.getLibraryId())
                .orElseThrow(() -> new RuntimeException("Library not found"));

        // 1️⃣ Find seat by library + seat number
        Seat seat = seatRepo
                .findByLibraryIdAndSeatNumber(req.getLibraryId(), req.getSeatNumber())
                .orElseThrow(() -> new RuntimeException("Seat not found for this library"));

        // 2️⃣ Check if seat already occupied (ACTIVE student)
        Optional<Student> activeStudent =
                studentRepo.findBySeat_Library_IdAndSeat_SeatNumberAndActiveTrue(
                        req.getLibraryId(),
                        req.getSeatNumber()
                );

        if (activeStudent.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Seat already occupied");
        }

        // 3️⃣ Create student
        Student student = new Student();
        student.setName(req.getName());
        student.setPhone(req.getPhone());
        student.setSeat(seat);
        student.setSeatNumber(seat.getSeatNumber());
        student.setAmountPaid(req.getAmountPaid());
        student.setBookingDate(LocalDate.now());
        student.setExpiryDate(LocalDate.now().plusDays(30));
        student.setEndDate(LocalDateTime.now().plusDays(30));
        student.setStudentType(req.getStudentType());
        student.setLibrary(library);
        student.setActive(true);

        studentRepo.save(student);

        return ResponseEntity.ok("Seat booked successfully");
    }

}

