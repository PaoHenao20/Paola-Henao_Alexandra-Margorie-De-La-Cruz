package test;

import dao.Impl.DaoEnMemoria;
import model.Odontologo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.OdontologoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OdontologoServiceTestMemoria {
    OdontologoService odontologoService = new OdontologoService(new DaoEnMemoria());

    @BeforeEach
    void setUp() {
        DaoEnMemoria daoEnMemoria = new DaoEnMemoria();
        odontologoService = new OdontologoService(daoEnMemoria);
    }

    @Test
    @DisplayName("Testear que un odontologo fue guardado")
    void caso1(){
        //Dado
        Odontologo odontologo = new Odontologo(213213,"Clara","Orozco");
        //cuando
        Odontologo odontologoDesdeDb = odontologoService.guardarOdontologo(odontologo);
        // entonces
        assertNotNull(odontologoDesdeDb.getId());
    }

    @Test
    @DisplayName("Listar todos los odontologos")
    void caso2(){

        //Dado
        Odontologo odontologo1 = new Odontologo(123456, "Martha", "Osorio");
        Odontologo odontologo2 = new Odontologo(123456, "Lucas", "Gil");
        odontologoService.guardarOdontologo(odontologo1);
        odontologoService.guardarOdontologo(odontologo2);

        // cuando
        List<Odontologo> odontologos = odontologoService.listaTodos();

        // entonces
        assertFalse(odontologos.isEmpty());
    }




}