package com.library.seatmanager.dto;

import com.library.seatmanager.entity.Student;

public class StudentCreateRequest {

    private String name;
    private String phone;

    private Integer seatNumber;   // null for HALF_DAY

    private int amount;

    private Student.StudentType studentType;   // FULL_DAY / HALF_DAY

    private Student.HalfDaySlot halfDaySlot;    // required for HALF_DAY

    public StudentCreateRequest(String name, Student.HalfDaySlot halfDaySlot, Student.StudentType studentType, int amount, Integer seatNumber, String phone) {
        this.name = name;
        this.halfDaySlot = halfDaySlot;
        this.studentType = studentType;
        this.amount = amount;
        this.seatNumber = seatNumber;
        this.phone = phone;
    }


    public StudentCreateRequest() {
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

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Student.StudentType getStudentType() {
        return studentType;
    }

    public void setStudentType(Student.StudentType studentType) {
        this.studentType = studentType;
    }

    public Student.HalfDaySlot getHalfDaySlot() {
        return halfDaySlot;
    }

    public void setHalfDaySlot(Student.HalfDaySlot halfDaySlot) {
        this.halfDaySlot = halfDaySlot;
    }

    @Override
    public String toString() {
        return "StudentCreateRequest{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", seatNumber=" + seatNumber +
                ", amount=" + amount +
                ", studentType=" + studentType +
                ", halfDaySlot=" + halfDaySlot +
                '}';
    }
}
