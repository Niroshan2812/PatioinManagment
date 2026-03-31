package com.pm.patient_service.service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponceDTO;
import com.pm.patient_service.exception.EmailAlredyExistsException;
import com.pm.patient_service.exception.PationNotFoundException;
import com.pm.patient_service.grpc.BillingServiceGrpcClient;
import com.pm.patient_service.kafka.KafkaProducer;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final KafkaProducer kafkaProducer;
    private PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;


    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    // Get all the Patient
    public List<PatientResponceDTO> getPatient() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponceDTO> dtos = patients.stream().map(PatientMapper::toDTO).toList();
        return dtos;
    }

    // Post data to pation
    public PatientResponceDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlredyExistsException("A patient with this email" + " alredy exists " + patientRequestDTO.getEmail());
        }

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(
                newPatient.getId().toString(),
                newPatient.getName(),
                newPatient.getEmail()
        );
        kafkaProducer.sendMessage(newPatient);
        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponceDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        // get pation
        Patient patient = patientRepository.findById(id).orElseThrow(()->new PationNotFoundException("Pation not found with ID: "+ id ));
        // check email and etc
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)) {
            throw new EmailAlredyExistsException("A pation with this email" + " alredy exists " + patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setBirth_date(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
