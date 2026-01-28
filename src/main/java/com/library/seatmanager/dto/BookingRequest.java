package com.library.seatmanager.dto;

import com.library.seatmanager.entity.Student;

public class BookingRequest {
        private int seatNumber;
        private String name;
        private String phone;
        private int amountPaid;
        private Student.StudentType studentType;


    public Student.StudentType getStudentType() {
        return studentType;
    }

    public void setStudentType(Student.StudentType studentType) {
        this.studentType = studentType;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BookingRequest(int seatNumber, Student.StudentType studentType, int amountPaid, String phone, String name) {
        this.seatNumber = seatNumber;
        this.studentType = studentType;
        this.amountPaid = amountPaid;
        this.phone = phone;
        this.name = name;
    }


    @Override
    public String toString() {
        return "BookingRequest{" +
                "seatNumber=" + seatNumber +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", amountPaid=" + amountPaid +
                ", studentType=" + studentType +
                '}';
    }

    public BookingRequest() {
    }
}
