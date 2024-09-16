package dh.backend.clinica.service;

import dh.backend.clinica.dto.request.OdontologoRequestDto;
import dh.backend.clinica.dto.response.OdontologoResponseDto;
import dh.backend.clinica.entity.Odontologo;
import dh.backend.clinica.entity.Paciente;

import java.util.List;
import java.util.Optional;

public interface IOdontologoService {
    OdontologoResponseDto guardarOdontologo(OdontologoRequestDto odontologoRequestDto);

    Optional<OdontologoResponseDto> buscarPorId(Integer id);

    List<OdontologoResponseDto> buscarTodos();

    void modificarOdontologo(OdontologoResponseDto odontologoResponseDto);

    void eliminarOdontologo(Integer id);

    List<OdontologoResponseDto> buscarPorUnaParteApellido(String parte);
}
