package edu.uclm.esi.payments.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class ProxyUsuarios {

    private static ProxyUsuarios yo;
    private final String creditsUrl = "http://localhost:8001/credits/";
    private final String tokensUrl = "http://localhost:8001/tokens/";

    private ProxyUsuarios() {}

    public static ProxyUsuarios get() {
        if (yo == null)
            yo = new ProxyUsuarios();
        return yo;
    }

    public void confirm(String token, int credits) throws Exception {

    String email = obtenerEmailDesdeToken(token);
    System.out.println("[DEBUG] Email obtenido del token: " + email);

    if (email == null)
        throw new Exception("No se pudo obtener el email del token");

    String fullUrl = creditsUrl + "addcreditsbyemail/" + email + "?amount=" + credits;
    System.out.println("[DEBUG] URL de addCredits: " + fullUrl);
    HttpPost httpPost = new HttpPost(fullUrl);
    httpPost.setHeader("Authorization", ensureBearerPrefix(token));

    try (CloseableHttpClient httpclient = HttpClients.createDefault();
         CloseableHttpResponse response = httpclient.execute(httpPost)) {

        int code = response.getCode();
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        System.out.println("[DEBUG] Código de respuesta: " + code);
        System.out.println("[DEBUG] Cuerpo de respuesta: " + responseBody);

        if (code != 200) {
            throw new Exception("Error del servicio de usuarios: " + responseBody);
        }

    } catch (IOException e) {
        throw new Exception("Error al conectar con el servicio de usuarios: " + e.getMessage());
    }
}

    private String obtenerEmailDesdeToken(String token) throws Exception {
        HttpGet get = new HttpGet(tokensUrl + "obtenerEmail");
        get.setHeader("Authorization", ensureBearerPrefix(token));

        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(get)) {

            int code = response.getCode();
            if (code != 200) return null;

            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }
    }
    private String ensureBearerPrefix(String token) {
    return token.startsWith("Bearer ") ? token : "Bearer " + token;
}

    public void checkToken(String token) throws Exception {
        HttpGet httpGet = new HttpGet(tokensUrl + "validarToken");
        httpGet.setHeader("Authorization", "Bearer " + token);

        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(httpGet)) {

            int code = response.getCode();
            if (code != 200) {
                throw new Exception("No se ha podido validar el token");
            }

        } catch (Exception e) {
            throw new Exception("Error del sistema de usuarios");
        }
    }
}
