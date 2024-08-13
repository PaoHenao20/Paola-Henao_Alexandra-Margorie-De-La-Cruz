package dao.Impl;

import dao.IDao;
import db.H2Connection;
import model.Odontologo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoH2Odontologo implements IDao<Odontologo> {
    public static final Logger logger = Logger.getLogger(DaoH2Odontologo.class);
    public static final String INSERT = "INSERT INTO ODONTOLOGOS VALUES(DEFAULT,?,?,?)";
    public static final String SELECT_ALL = "SELECT * FROM ODONTOLOGOS";


    @Override
    public Odontologo guardar(Odontologo odontologo) {
        Connection connection = null;
        Odontologo odontologoARetornar = null;


        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, odontologo.getNumeroMatricula());
            preparedStatement.setString(2, odontologo.getNombre());
            preparedStatement.setString(3, odontologo.getApellido());
            preparedStatement.executeUpdate();

            connection.commit();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                int idDB = resultSet.getInt(1);
                odontologoARetornar = new Odontologo(idDB, odontologo.getNumeroMatricula(), odontologo.getNombre(),
                        odontologo.getApellido());
            }
            logger.info("Odontologo:  "+ odontologoARetornar);

        } catch (Exception e){
            if(connection != null){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    logger.error(e.getMessage());
                } finally {
                    try {
                        connection.setAutoCommit(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return odontologoARetornar;
    }

    @Override
    public List<Odontologo> listaTodos() {
        Connection connection = null;
        List<Odontologo> odontologos= new ArrayList<>();
        Odontologo odontologoDesdeDB = null;

        try{
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);
            while (resultSet.next()){
                int idDB = resultSet.getInt(1);
                int matricula  = resultSet.getInt(2);
                String nombre = resultSet.getString(3);
                String apellido = resultSet.getString(4);


                odontologoDesdeDB = new Odontologo(idDB,  matricula, nombre, apellido);
                logger.info(odontologoDesdeDB);
                odontologos.add(odontologoDesdeDB);
            }

        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        return odontologos;
    }

}
