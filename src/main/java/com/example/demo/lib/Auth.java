package com.example.demo.lib;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.lib.exceptions.AuthException;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class Auth {

    public static ResultSet getUser(Dao dao) throws IOException {
        int userId = Integer.parseInt(Objects.requireNonNull(decrypt(new BufferedReader(new FileReader(Settings.TOKEN_FILE)).readLine())).getIssuer());
        return dao.getUserById(userId);
    }

    public static void login(String login, String password, Dao dao) throws AuthException, SQLException, UnsupportedEncodingException {
        if (login == null || password == null)  throw new AuthException("Login and password must not be null");

        ResultSet user = dao.getUserByLogin(login);
        if (!Objects.equals(user.getString("password"), password)) {
            throw new AuthException("Authentication failed");
        }


        int userId = user.getInt("id");
        String token = encrypt(userId);
        writeTokenToFile(token);

    }

    private static void writeTokenToFile(String token) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("token"))) {
            writer.write(Objects.requireNonNull(token));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(int userId) throws UnsupportedEncodingException {
        return JWT.create()
                .withIssuer(String.valueOf(userId))
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(Settings.SECRET_KEY));
    }

    public static DecodedJWT decrypt(String token) throws UnsupportedEncodingException {
        return JWT.require(Algorithm.HMAC256(Settings.SECRET_KEY))
                .build()
                .verify(token);
    }

    public static void logout() {
        new File(Settings.TOKEN_FILE).delete();
    }
}
