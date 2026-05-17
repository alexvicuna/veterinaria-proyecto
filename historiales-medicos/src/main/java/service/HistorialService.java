package service;


import dto.HistorialDTO;
import model.Historial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.HistorialRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    public HistorialDTO crearHistorial(HistorialDTO dto) {
        Historial historial = mapToEntity(dto);
        return mapToDTO(historialRepository.save(historial));
    }

    public List<HistorialDTO> obtenerTodos() {
        return historialRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public HistorialDTO obtenerPorId(Long id) {
        Historial historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con ID: " + id));
        return mapToDTO(historial);
    }

    public HistorialDTO actualizarHistorial(Long id, HistorialDTO dto) {
        Historial historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con ID: " + id));

        historial.setDiagnostico(dto.getDiagnostico());
        historial.setTratamiento(dto.getTratamiento());
        historial.setVacunas(dto.getVacunas());
        historial.setIdMascota(dto.getIdMascota());

        return mapToDTO(historialRepository.save(historial));
    }

    public void eliminarHistorial(Long id) {
        Historial historial = historialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con ID: " + id));
        historialRepository.delete(historial);
    }


    private HistorialDTO mapToDTO(Historial historial) {
        HistorialDTO dto = new HistorialDTO();
        dto.setIdHistorial(historial.getIdHistorial());
        dto.setDiagnostico(historial.getDiagnostico());
        dto.setTratamiento(historial.getTratamiento());
        dto.setVacunas(historial.getVacunas());
        dto.setIdMascota(historial.getIdMascota());
        return dto;
    }

    private Historial mapToEntity(HistorialDTO dto) {
        Historial historial = new Historial();
        historial.setIdHistorial(dto.getIdHistorial());
        historial.setDiagnostico(dto.getDiagnostico());
        historial.setTratamiento(dto.getTratamiento());
        historial.setVacunas(dto.getVacunas());
        historial.setIdMascota(dto.getIdMascota());
        return historial;
    }
}