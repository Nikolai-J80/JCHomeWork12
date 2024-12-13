import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.nikolaJ80.patient.entity.BloodPressure;
import ru.netology.nikolaJ80.patient.entity.HealthInfo;
import ru.netology.nikolaJ80.patient.entity.PatientInfo;
import ru.netology.nikolaJ80.patient.repository.PatientInfoFileRepository;
import ru.netology.nikolaJ80.patient.repository.PatientInfoRepository;
import ru.netology.nikolaJ80.patient.service.alert.SendAlertService;
import ru.netology.nikolaJ80.patient.service.medical.MedicalService;
import ru.netology.nikolaJ80.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceTest {
    PatientInfo patient = new PatientInfo("1", "Oleg", "Ivanov", LocalDate.of(1976, 11,
            10), new HealthInfo(new BigDecimal("36.6"), new BloodPressure(120, 75)));

    @Test
    void testCheckBloodPressure() {
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(Mockito.anyString())).thenReturn(patient);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl sut = new MedicalServiceImpl(patientInfoFileRepository, alertService);

        sut.checkBloodPressure("1", new BloodPressure(140, 90));
        sut.checkBloodPressure("1", new BloodPressure(120, 75));
        sut.checkBloodPressure("1", new BloodPressure(100, 60));

        Mockito.verify(patientInfoFileRepository, Mockito.times(3)).getById("1");
        Mockito.verify(alertService, Mockito.times(2)).send(Mockito.anyString());
    }

    @Test
    void testCheckTemperature() {
        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(Mockito.anyString())).thenReturn(patient);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl sut = new MedicalServiceImpl(patientInfoFileRepository, alertService);

        sut.checkTemperature("1", new BigDecimal("35.0"));
        sut.checkTemperature("1", new BigDecimal("36.6"));
        sut.checkTemperature("1", new BigDecimal("30.5"));

        Mockito.verify(patientInfoFileRepository, Mockito.times(3)).getById("1");
        Mockito.verify(alertService, Mockito.times(2)).send(Mockito.anyString());
    }


    @Test
    public void checkTemperature_sendMessageTest() {
        // arrange
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("1")).thenReturn(patient);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl sut = new MedicalServiceImpl(patientInfoRepository, alertService);

        //act
        sut.checkTemperature("1", new BigDecimal("30.0"));

        //assert
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: 1, need help", argumentCaptor.getValue());
    }
}
