package dh.backend.clinica.service.impl;

import dh.backend.clinica.dto.request.OdontologoRequestDto;
import dh.backend.clinica.dto.response.OdontologoResponseDto;
import dh.backend.clinica.entity.Odontologo;
import dh.backend.clinica.exception.BadRequestException;
import dh.backend.clinica.exception.ResourceNotFoundException;
import dh.backend.clinica.repository.IOdontologoRepository;
import dh.backend.clinica.service.IOdontologoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OdontologoService implements IOdontologoService {
    private final Logger logger = LoggerFactory.getLogger(OdontologoService.class);
    private IOdontologoRepository odontologoRepository;
    @Autowired
    private ModelMapper modelMapper;

    public OdontologoService(IOdontologoRepository odontologoRepository) {
        this.odontologoRepository = odontologoRepository;
    }
    @Override
    public OdontologoResponseDto guardarOdontologo(OdontologoRequestDto odontologoRequestDto) {
        logger.info("Guardando odontólogo con matrícula: {}", odontologoRequestDto.getMatricula());
        Odontologo odontologo = new Odontologo();
        Odontologo odontologoDesdeDB = null;
        OdontologoResponseDto odontologoResponseDto = null;

        if (odontologoRequestDto == null) {
            logger.error("No se puede guardar un objeto nulo.");
            throw new BadRequestException("No se puede guardar un objeto nulo.");
        }

        if (odontologoRequestDto.getMatricula() == null || odontologoRequestDto.getMatricula().trim().isEmpty()) {
            logger.error("El número de matrícula del odontólogo es obligatorio.");
            throw new BadRequestException("El número de matrícula del odontólogo es obligatorio.");
        }

        if (odontologoRequestDto.getNombre() == null || odontologoRequestDto.getNombre().trim().isEmpty()) {
            logger.error("El nombre del odontólogo es obligatorio.");
            throw new BadRequestException("El nombre del odontólogo es obligatorio.");
        }

        if (odontologoRequestDto.getApellido() == null || odontologoRequestDto.getApellido().trim().isEmpty()) {
            logger.error("El apellido del odontólogo es obligatorio.");
            throw new BadRequestException("El apellido del odontólogo es obligatorio.");
        }else {

            odontologo.setMatricula(odontologoRequestDto.getMatricula());
            odontologo.setApellido(odontologoRequestDto.getApellido());
            odontologo.setNombre(odontologoRequestDto.getNombre());
            odontologoDesdeDB = odontologoRepository.save(odontologo);
            odontologoResponseDto = convertirOdontologoEnResponse(odontologoDesdeDB);
        }
        logger.info("Odontólogo guardado exitosamente con ID: {}", odontologoDesdeDB.getId());


        return odontologoResponseDto;
    }

    @Override
    public Optional<OdontologoResponseDto> buscarPorId(Integer id) {
        logger.info("Buscando odontólogo con ID: {}", id);
        Optional<Odontologo> odontologo = odontologoRepository.findById(id);
        OdontologoResponseDto odontologoRespuesta = convertirOdontologoEnResponse(odontologo.get());
        return Optional.of(odontologoRespuesta);

    }
    @Override
    public List<OdontologoResponseDto> buscarTodos() {
        logger.info("Buscando todos los odontólogos");
        List<Odontologo> odontologosDesdeBD = odontologoRepository.findAll();
        List<OdontologoResponseDto> odontologosRespuesta = new ArrayList<>();
        for(Odontologo t: odontologosDesdeBD){
            odontologosRespuesta.add(convertirOdontologoEnResponse(t));
        }
        return odontologosRespuesta;
    }
    @Override
    public void modificarOdontologo(OdontologoResponseDto odontologoResponseDto) {
        logger.info("Modificando odontólogo con ID: {}", odontologoResponseDto.getId());
        Optional<Odontologo> odontologoEncontrado = odontologoRepository.findById(odontologoResponseDto.getId());
        if(odontologoEncontrado.isPresent()){
            Odontologo odontologo = new Odontologo(odontologoResponseDto.getId(),
                    odontologoResponseDto.getMatricula(),
                    odontologoResponseDto.getApellido(),
                    odontologoResponseDto.getNombre(),
                    null);

            odontologoRepository.save(odontologo);
        } else {
            logger.error("Odontólogo no encontrado con ID: {}", odontologoResponseDto.getId());
            throw new ResourceNotFoundException("Odontologo no encontrado");
        }



    }
    @Override
    public void eliminarOdontologo(Integer id) {
        logger.info("Eliminando odontólogo con ID: {}", id);
        Optional<Odontologo> odontologoEncontrado = odontologoRepository.findById(id);
        if(odontologoEncontrado.isPresent()){
            odontologoRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Odontologo no encontrado");
        }
    }

    @Override
    public List<OdontologoResponseDto> buscarPorUnaParteApellido(String parte){
        logger.info("Buscando odontólogos con apellido que contiene: {}", parte);
        List<Odontologo> odontologos = odontologoRepository.buscarPorParteApellido(parte);
        List<OdontologoResponseDto> odontologosRespuesta = new ArrayList<>();
        for(Odontologo t: odontologos){
            odontologosRespuesta.add(convertirOdontologoEnResponse(t));
        }
        logger.info("Total de odontólogos encontrados con apellido que contiene '{}': {}", parte, odontologosRespuesta.size());
        return odontologosRespuesta;
    }

    private OdontologoResponseDto convertirOdontologoEnResponse(Odontologo odontologo){
        OdontologoResponseDto odontologoResponseDto = modelMapper.map(odontologo, OdontologoResponseDto.class);
        return odontologoResponseDto;
    }
}
