package com.example.programacionweb_its_prac1.dao;
import com.example.programacionweb_its_prac1.conexion.Conexion;
import com.example.programacionweb_its_prac1.modelo.User;
import java.util.ArrayList;

public class UserDAO implements DAOGeneral<Integer, User>{
    private final Conexion c;
    public UserDAO() {
        c = new Conexion<User>();
    }

    @Override
    public boolean agregar(User user) {
        String query = "INSERT INTO users(name, email, username, password) VALUES (?, ?, ?, ?)";

        return c.ejecutarActualizacion(query, user.getAll());
    }

    @Override
    public ArrayList<User> consultar() {
        String query = "SELECT * FROM users";

        ArrayList<ArrayList<String>> res = c.ejecutarConsulta(query, new String[]{});
        ArrayList<User> users = new ArrayList<User>();

        for (ArrayList<String> r: res) {
            users.add(new User(Integer.parseInt(r.get(0)), r.get(1), r.get(2), r.get(3), r.get(4), r.get(5)));
        }

        return users;
    }

    public User buscar(int id) {
        String query = "SELECT * FROM users WHERE id = " + id;
        ArrayList<ArrayList<String>> res = c.ejecutarConsulta(query, new String[]{});

        if (res.size() != 1) {
            return null;
        }

        User user = null;
        for (ArrayList<String> r: res) {
            user = new User(Integer.parseInt(r.get(0)), r.get(1), r.get(2), r.get(3), r.get(4), r.get(5));
        }
        return user;
    }

    public User buscar(String columna, String valor) {
        String query = "SELECT * FROM users WHERE  " + columna  + " = '" + valor + "'";
        ArrayList<ArrayList<String>> res = c.ejecutarConsulta(query, new String[]{});

        User user = null;
        for (ArrayList<String> r: res) {
            user = new User(Integer.parseInt(r.get(0)), r.get(1), r.get(2), r.get(3), r.get(4), r.get(5));
        }
        return user;
    }

    @Override
    public boolean actualizar(Integer id, User nuevo) {
        String query = "UPDATE users SET name=?, email=?, password=? WHERE id=?";

        return c.ejecutarActualizacion(query, new Object[]{
                nuevo.getName(),
                nuevo.getEmail(),
                nuevo.getPassword(),
                id
        });
    }

    @Override
    public boolean eliminar(Integer id) {
        return false;
    }
}
