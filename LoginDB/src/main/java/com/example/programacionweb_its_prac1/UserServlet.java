package com.example.programacionweb_its_prac1;

import com.example.programacionweb_its_prac1.dao.UserDAO;
import com.example.programacionweb_its_prac1.modelo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import static com.example.programacionweb_its_prac1.AutenticacionServlet.getUsers;

import java.io.IOException;
import java.util.*;

import static com.example.programacionweb_its_prac1.AutenticacionServlet.generalKey;

@WebServlet("/user-servlet/*")
public class UserServlet extends HttpServlet {
    private final JsonResponse jResp = new JsonResponse();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String authTokenHeader = req.getHeader("Authorization");
        // Validar el token
        if (!validToken(authTokenHeader.split(" ")[1])) {
            jResp.failed(req, resp, "Token inválido ", 422);
            return;
        }

        if (req.getPathInfo() == null || "/".equals(req.getPathInfo())) { // Listar todos los usuarios
            ArrayList<User> users = userDAO.consultar();

            // Remover la contraseña
            ArrayList<HashMap<String, String>> usersData = new ArrayList<>();
            for (User user : users) {
                HashMap<String, String> userData = new HashMap<>();
                userData.put("name", user.getName());
                userData.put("username", user.getUsername());
                userData.put("email", user.getEmail());
                usersData.add(userData);
            }

            jResp.success(req, resp, "Autenticación aprobada", usersData);
        }
        else { // Retornar usuario por ID
            String[] path = req.getPathInfo().split("/");
            try {
                int id = Integer.parseInt(path[1]);
                User user = userDAO.buscar(id);
                // Remover la contraseña
                HashMap<String, String> userData = new HashMap<>();
                userData.put("name", user.getName());
                userData.put("username", user.getUsername());
                userData.put("email", user.getEmail());

                jResp.success(req, resp, "Autenticación aprobada", userData);
            } catch (Exception e) {
                jResp.failed(req, resp, e.getMessage(), 500);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String authTokenHeader = req.getHeader("Authorization");
        if (!validToken(authTokenHeader.split(" ")[1])) { // Validar el token
            jResp.failed(req, resp, "Token inválido ", 422);
            return;
        }
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        if (username == null || password == null || name == null || email == null) {
            jResp.failed(req, resp, "Todos los campos son obligatorios", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        User existentUser = userDAO.buscar(username);
        if(existentUser != null) {
            jResp.failed(req, resp, "Usuario ya existe", 422);
            return;
        }

        User user = new User(name, email, username, password);

        try {
            userDAO.agregar(user);

            // Filtrado para no mostrar la contraseña
            HashMap<String, String> userData = new HashMap<>();
            userData.put("name", user.getName());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());

            jResp.success(req, resp, "Usuario creado con éxito", userData);
        } catch (Exception e) {
            jResp.failed(req, resp, e.getMessage(), 500);
        }


    }

    private boolean validToken(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith( generalKey() )
                .build();
        try {
            jwtParser.parse(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Método que se utiliza para validar el token de autenticación. Si el token es válido, se envía una respuesta exitosa.
     * Si el token no es válido, se envía una respuesta fallida.
     * @param req
     * @param resp
     * @param token Token de autenticación
     * @throws IOException

    private void validateAuthToken (HttpServletRequest req, HttpServletResponse resp, String token) throws IOException {
        // String[] chunks = token.split("\\.");

        // Base64.Decoder decoder = Base64.getUrlDecoder();

        // String header = new String(decoder.decode(chunks[0]));
        // String payload = new String(decoder.decode(chunks[1]));

        JwtParser jwtParser = Jwts.parser()
                .verifyWith( generalKey() )
                .build();
        try {
            // jwtParser.parse(token);
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            String username = claims.getSubject();
            User user = getUsers().get(username);

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("username", user.getPassword());
            userInfo.put("fullName", user.getFullName());
            userInfo.put("email", user.getEmail());

            jResp.success(req, resp, "Autenticación probada", userInfo);
        } catch (Exception e) {
            System.out.println(token);
            jResp.failed(req, resp, "Unathorized: " + e.getMessage(), 401);
        }
    }
    */
}
