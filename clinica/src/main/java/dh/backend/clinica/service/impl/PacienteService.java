package dh.backend.clinica.service.impl;

import dh.backend.clinica.entity.Paciente;
import dh.backend.clinica.exception.BadRequestException;
import dh.backend.clinica.exception.ResourceNotFoundException;
import dh.backend.clinica.repository.IPacienteRepository;
import dh.backend.clinica.service.IPacienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService implements IPacienteService {
    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);
    private IPacienteRepository pacienteRepository;

    public PacienteService(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente guardarPaciente(Paciente paciente) {
        logger.info("Guardando paciente: {}", paciente);
        if (paciente == null) {
            logger.error("Intento de guardar un paciente nulo.");
            throw new BadRequestException("No se puede guardar un objeto nulo.");
        }

        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            logger.error("Paciente sin nombre");
            throw new BadRequestException("El nombre del paciente es obligatorio.");
        }

        if (paciente.getApellido() == null || paciente.getApellido().trim().isEmpty()) {
            logger.error("Paciente sin apellido");
            throw new BadRequestException("El apellido del paciente es obligatorio.");
        }

        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
            logger.error("Paciente sin DNI");
            throw new BadRequestException("El DNI del paciente es obligatorio.");
        }

        if (paciente.getDomicilio() == null) {
            logger.error("Paciente sin domicilio");
            throw new BadRequestException("El domicilio del paciente es obligatorio.");
        }

        return pacienteRepository.save(paciente);
    }

    @Override
    public Optional<Paciente> buscarPorId(Integer id) {
        logger.info("Buscando paciente por ID: {}", id);
        return pacienteRepository.findById(id);
    }

    @Override
    public List<Paciente> buscarTodos() {
        logger.info("Buscando todos los pacientes");
        return pacienteRepository.findAll();
    }

    @Override
    public void modificarPaciente(Paciente paciente) {
        logger.info("Modificando paciente: {}", paciente);
        pacienteRepository.save(paciente);
    }

    @Override
    public void eliminarPaciente(Integer id) {
        logger.info("Eliminando paciente con ID: {}", id);
        Optional<Paciente> pacienteEncontrado = buscarPorId(id);
        if(pacienteEncontrado.isPresent()){
            pacienteRepository.deleteById(id);
            logger.info("Paciente con ID: {} eliminado correctamente", id);
        } else {
            logger.error("Paciente no encontrado con ID: {}", id);
            throw new ResourceNotFoundException("Paciente no encontrado");
        }
    }

    @Override
    public List<Paciente> buscarPorApellidoyNombre(String apellido, String nombre) {
        logger.info("Buscando paciente por Apellido y Nombre: {}", apellido + nombre);
        return pacienteRepository.findByApellidoAndNombre(apellido, nombre);
    }

    @Override
    public List<Paciente> buscarPorUnaParteApellido(String parte){
        logger.info("Buscando paciente por parte del apellido");
        return pacienteRepository.buscarPorParteApellido(parte);
    }
    @Override
    public List<Paciente> buscarPorProvincia(String pacienteProvincia){
        logger.info("Buscando paciente por Provincia: {}", pacienteProvincia);
        return pacienteRepository.buscarPorProvinciaPaciente(pacienteProvincia);
    }

}
