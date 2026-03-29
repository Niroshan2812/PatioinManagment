package com.pm.patient_service.controller;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponceDTO;
import com.pm.patient_service.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponceDTO>> getPatient() {
        List<PatientResponceDTO> patients = patientService.getPatient();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponceDTO> createPation(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponceDTO pationResponseDTO = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(pationResponseDTO);
    }
}
