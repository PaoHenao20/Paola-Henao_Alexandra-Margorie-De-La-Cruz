package dh.backend.clinica.service.impl;

import dh.backend.clinica.entity.Odontologo;
import dh.backend.clinica.entity.Turno;
import dh.backend.clinica.exception.BadRequestException;
import dh.backend.clinica.exception.ResourceNotFoundException;
import dh.backend.clinica.repository.IOdontologoRepository;
import dh.backend.clinica.service.IOdontologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologoService implements IOdontologoService {
    @Autowired
    private IOdontologoRepository odontologoRepository;
    public OdontologoService(IOdontologoRepository odontologoRepository) {
        this.odontologoRepository = odontologoRepository;
    }
    @Override
    public Odontologo guardarOdontologo(Odontologo odontologo) {
        if (odontologo == null) {
            throw new BadRequestException("No se puede guardar un objeto nulo.");
        }

        if (odontologo.getNroMatricula() == null || odontologo.getNroMatricula().trim().isEmpty()) {
            throw new BadRequestException("El número de matrícula del odontólogo es obligatorio.");
        }

        if (odontologo.getNombre() == null || odontologo.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del odontólogo es obligatorio.");
        }

        if (odontologo.getApellido() == null || odontologo.getApellido().trim().isEmpty()) {
            throw new BadRequestException("El apellido del odontólogo es obligatorio.");
        }


        return odontologoRepository.save(odontologo);
    }



    @Override
    public Optional<Odontologo> buscarPorId(Integer id) {
        return odontologoRepository.findById(id);
    }

    @Override
    public List<Odontologo> buscarTodos() {
        return odontologoRepository.findAll();
    }

    @Override
    public void modificarOdontologo(Odontologo odontologo) {
        odontologoRepository.save(odontologo);
    }

    @Override
    public void eliminarOdontologo(Integer id) {
        Optional<Odontologo> odontologoEncontrado = odontologoRepository.findById(id);
        if(odontologoEncontrado.isPresent()){
            odontologoRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Odontologo no encontrado");
        }
    }

    @Override
    public List<Odontologo> buscarPorApellidoyNombre(String apellido, String nombre) {
        return odontologoRepository.findByApellidoAndNombre(apellido, nombre);
    }

    @Override
    public List<Odontologo> buscarPorUnaParteApellido(String parte){
        return odontologoRepository.buscarPorParteApellido(parte);
    }
}
