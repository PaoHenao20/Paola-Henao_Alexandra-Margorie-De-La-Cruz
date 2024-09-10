package dh.backend.clinica.service.impl;

import dh.backend.clinica.dto.request.TurnoModifyDto;
import dh.backend.clinica.dto.request.TurnoRequestDto;
import dh.backend.clinica.dto.response.OdontologoResponseDto;
import dh.backend.clinica.dto.response.PacienteResponseDto;
import dh.backend.clinica.dto.response.TurnoResponseDto;
import dh.backend.clinica.entity.Odontologo;
import dh.backend.clinica.entity.Paciente;
import dh.backend.clinica.entity.Turno;
import dh.backend.clinica.exception.BadRequestException;
import dh.backend.clinica.exception.ResourceNotFoundException;
import dh.backend.clinica.repository.ITurnoRepository;
import dh.backend.clinica.service.IOdontologoService;
import dh.backend.clinica.service.IPacienteService;
import dh.backend.clinica.service.ITurnoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoService implements ITurnoService {
    private final Logger logger = LoggerFactory.getLogger(TurnoService.class);
    private ITurnoRepository turnoRepository;
    private IPacienteService pacienteService;
    private IOdontologoService odontologoService;
    @Autowired
    private ModelMapper modelMapper;

    public TurnoService(ITurnoRepository turnoRepository, IPacienteService pacienteService, IOdontologoService odontologoService) {
        this.turnoRepository = turnoRepository;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
    }

    @Override
    public TurnoResponseDto guardarTurno(TurnoRequestDto turnoRequestDto){
        logger.info("Guardando turno para el paciente con ID: {} y odontólogo con ID: {}", turnoRequestDto.getPaciente_id(), turnoRequestDto.getOdontologo_id());
        Optional<Paciente> paciente = pacienteService.buscarPorId(turnoRequestDto.getPaciente_id());
        Optional<OdontologoResponseDto> odontologo = odontologoService.buscarPorId(turnoRequestDto.getOdontologo_id());
        Turno turno = new Turno();
        Turno turnoDesdeBD = null;
        TurnoResponseDto turnoResponseDto = null;
        if(paciente.isPresent() && odontologo.isPresent()){

            turno.setPaciente(paciente.get());
            turno.setOdontologo(modelMapper.map(odontologo, Odontologo.class));
            turno.setFecha(LocalDate.parse(turnoRequestDto.getFecha()));


            turnoDesdeBD = turnoRepository.save(turno);


            turnoResponseDto = convertirTurnoEnResponse(turnoDesdeBD);
            logger.info("Turno guardado exitosamente con ID: {}", turnoDesdeBD.getId());
            return turnoResponseDto;
        }else{
            logger.error("No se pudo guardar el turno. Paciente o odontólogo no encontrado.");
            throw new BadRequestException("Turno no se puede guardar. Paciente o odontologo no encontrado");
        }

    }

    @Override
    public Optional<TurnoResponseDto> buscarPorId(Integer id) {
        logger.info("Buscando turno con ID: {}", id);
        Optional<Turno> turno = turnoRepository.findById(id);
        if(turno.isPresent()) {
            logger.info("Turno encontrado con ID: {}", id);
            TurnoResponseDto turnoRespuesta = convertirTurnoEnResponse(turno.get());
            return Optional.of(turnoRespuesta);
        }else {
            logger.error("Turno no encontrado con ID: {}", id);
            throw new ResourceNotFoundException("Turno no encontrado");
        }

    }

    @Override
    public List<TurnoResponseDto> buscarTodos() {
        logger.info("Buscando todos los turnos");
        List<Turno> turnosDesdeBD = turnoRepository.findAll();
        List<TurnoResponseDto> turnosRespuesta = new ArrayList<>();
        for(Turno t: turnosDesdeBD){
            turnosRespuesta.add(convertirTurnoEnResponse(t));
        }
        logger.info("Total de turnos encontrados: {}", turnosRespuesta.size());
        return turnosRespuesta;
    }

    @Override
    public void modificarTurnos(TurnoModifyDto turnoModifyDto) {
        logger.info("Modificando turno con ID: {}", turnoModifyDto.getId());
        Optional<Paciente> paciente = pacienteService.buscarPorId(turnoModifyDto.getPaciente_id());
        Optional<OdontologoResponseDto> odontologo = odontologoService.buscarPorId(turnoModifyDto.getOdontologo_id());
        if(paciente.isPresent() && odontologo.isPresent()){
            Turno turno = new Turno(
                    turnoModifyDto.getId(),
                    paciente.get(), modelMapper.map(odontologo,Odontologo.class), LocalDate.parse(turnoModifyDto.getFecha())
            );
            turnoRepository.save(turno);
            logger.info("Turno con ID: {} modificado exitosamente", turnoModifyDto.getId());

        }else{
            logger.error("No se pudo modificar el turno. Paciente o odontólogo no encontrado.");
            throw new BadRequestException("Turno no se puede modificar. Paciente o odontologo no encontrado");
        }
    }


    @Override
    public void eliminarTurno(Integer id){
        logger.info("Eliminando turno con ID: {}", id);
        Optional<Turno> turnoEncontrado = turnoRepository.findById(id);
        if(turnoEncontrado.isPresent()){
            turnoRepository.deleteById(id);
            logger.info("Turno con ID: {} eliminado exitosamente", id);

        } else {
            logger.error("Turno no encontrado con ID: {}", id);
            throw new ResourceNotFoundException("Turno no encontrado");
        }

    }

    @Override
    public Optional<TurnoResponseDto> buscarTurnosPorPaciente(String pacienteApellido) {
        logger.info("Buscando turnos para el paciente con apellido: {}", pacienteApellido);
        Optional<Turno> turno = turnoRepository.buscarPorApellidoPaciente(pacienteApellido);
        TurnoResponseDto turnoParaResponder = null;
         if(turno.isPresent()) {
             logger.info("Turno encontrado para el paciente con apellido: {}", pacienteApellido);
             turnoParaResponder = convertirTurnoEnResponse(turno.get());
         }else {
             logger.error("No se encontraron turnos para el paciente con apellido: {}", pacienteApellido);
             throw new ResourceNotFoundException("Turno no encontrado");
         }
        return Optional.ofNullable(turnoParaResponder);
    }

    @Override
    public Optional<TurnoResponseDto> buscarTurnosPorOdontologo(String odontologoApellido) {
        logger.info("Buscando turnos para el odontólogo con apellido: {}", odontologoApellido);
        Optional<Turno> turno = turnoRepository.buscarPorApellidoOdontologo(odontologoApellido);
        TurnoResponseDto turnoParaResponder = null;
        if(turno.isPresent()) {
            logger.info("Turno encontrado para el odontólogo con apellido: {}", odontologoApellido);
            turnoParaResponder = convertirTurnoEnResponse(turno.get());
        }else {
            logger.error("No se encontraron turnos para el odontólogo con apellido: {}", odontologoApellido);
            throw new ResourceNotFoundException("Turno no encontrado");
        }
        return Optional.ofNullable(turnoParaResponder);
    }


    private TurnoResponseDto convertirTurnoEnResponse(Turno turno){
        TurnoResponseDto turnoResponseDto = modelMapper.map(turno, TurnoResponseDto.class);
        turnoResponseDto.setPacienteResponseDto(modelMapper.map(turno.getPaciente(), PacienteResponseDto.class));
        turnoResponseDto.setOdontologoResponseDto(modelMapper.map(turno.getOdontologo(), OdontologoResponseDto.class));
        return turnoResponseDto;
    }


}
