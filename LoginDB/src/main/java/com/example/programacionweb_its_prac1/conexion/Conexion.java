package com.example.programacionweb_its_prac1.conexion;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;

public class Conexion<T> {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String bd = dotenv.get("DB_NAME");
    private static final String usuario = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");
    private static final String host = dotenv.get("DB_HOST");
    private static final String puerto = dotenv.get("DB_PORT");
    private final String url;

    private Connection conexion;

    public Conexion() {
        url = "jdbc:mysql://" + host + ":" + puerto + "/" + bd;
    }

    public boolean abrir () {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, usuario, password);
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
    }

    public Connection obtener() {
        return conexion;
    }

    public boolean cerrar() {
        try {
            conexion.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public ArrayList<ArrayList<String>> ejecutarConsulta(String query, String[] columnas) {
        if (abrir()) {
            try {
                Statement st = this.conexion.createStatement();
                ResultSet rs = st.executeQuery(query);
                ResultSetMetaData metadata = rs.getMetaData();

                if (columnas.length == 0) {
                    int numColumnas = metadata.getColumnCount();
                    columnas = new String[numColumnas];

                    for (int i = 1; i <= numColumnas; i++) {
                        columnas[i-1] = metadata.getColumnName(i);
                    }
                }

                ArrayList<ArrayList<String>> registros = new ArrayList<ArrayList<String>>();

                while (rs.next()) {
                    ArrayList<String> registro = new ArrayList<String>();
                    for (String columna: columnas) {
                        registro.add(rs.getString(columna));
                    }
                    registros.add(registro);
                }

                return registros;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                cerrar();
            }
        }

        return null;
    }

    public boolean ejecutarActualizacion (String query, Object[] values) {
        if (this.abrir()) {
            try {
                PreparedStatement pstm = this.conexion.prepareStatement(query);
                int index = 1;

                for (Object val: values) {
                    switch (val.getClass().getName()) {
                        case "java.lang.Integer":
                            pstm.setInt(index, (Integer) val);
                            break;
                        case "java.lang.String":
                            pstm.setString(index, (String) val);
                            break;
                    }
                    index++;
                }

                pstm.execute();

                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                cerrar();
            }
        }

        return false;
    }
}
