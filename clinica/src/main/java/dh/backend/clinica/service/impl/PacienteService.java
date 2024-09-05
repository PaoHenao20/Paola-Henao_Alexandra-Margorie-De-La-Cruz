package dh.backend.clinica.service.impl;


import dh.backend.clinica.dto.request.TurnoRequestDto;
import dh.backend.clinica.dto.response.TurnoResponseDto;
import dh.backend.clinica.entity.Odontologo;
import dh.backend.clinica.entity.Paciente;
import dh.backend.clinica.entity.Turno;
import dh.backend.clinica.exception.BadRequestException;
import dh.backend.clinica.exception.ResourceNotFoundException;
import dh.backend.clinica.repository.IPacienteRepository;
import dh.backend.clinica.service.IPacienteService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService implements IPacienteService {

    private IPacienteRepository pacienteRepository;

    public PacienteService(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente guardarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new BadRequestException("No se puede guardar un objeto nulo.");
        }

        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del paciente es obligatorio.");
        }

        if (paciente.getApellido() == null || paciente.getApellido().trim().isEmpty()) {
            throw new BadRequestException("El apellido del paciente es obligatorio.");
        }

        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
            throw new BadRequestException("El DNI del paciente es obligatorio.");
        }

        if (paciente.getDomicilio() == null) {
            throw new BadRequestException("El domicilio del paciente es obligatorio.");
        }

        return pacienteRepository.save(paciente);
    }

    @Override
    public Optional<Paciente> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public List<Paciente> buscarTodos() {
        return pacienteRepository.findAll();
    }

    @Override
    public void modificarPaciente(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    @Override
    public void eliminarPaciente(Integer id) {
        Optional<Paciente> pacienteEncontrado = buscarPorId(id);
        if(pacienteEncontrado.isPresent()){
            pacienteRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Paciente no encontrado");
        }
    }

    @Override
    public List<Paciente> buscarPorApellidoyNombre(String apellido, String nombre) {
        return pacienteRepository.findByApellidoAndNombre(apellido, nombre);
    }

    @Override
    public List<Paciente> buscarPorUnaParteApellido(String parte){
        return pacienteRepository.buscarPorParteApellido(parte);
    }
    @Override
    public List<Paciente> buscarPorProvincia(String pacienteProvincia){
        return pacienteRepository.buscarPorProvinciaPaciente(pacienteProvincia);
    }

}
