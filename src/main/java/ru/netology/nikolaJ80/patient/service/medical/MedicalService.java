package ru.netology.nikolaJ80.patient.service.medical;

import java.math.BigDecimal;

import ru.netology.nikolaJ80.patient.entity.BloodPressure;

public interface MedicalService {

    void checkBloodPressure(String patientId, BloodPressure bloodPressure);

    void checkTemperature(String patientId, BigDecimal temperature);
}
