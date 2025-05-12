package edu.uclm.esi.payments.services;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

public class ProxyUsuarios {

    private static ProxyUsuarios yo;
    private final String url = "http://localhost:8081/users/";
    private final String url2 = "http://localhost:8081/tokens/";

    //Constructor privado
    private ProxyUsuarios() {}

    public void confirm(String token, int credits) throws Exception {
        HttpGet httpGet = new HttpGet(url + "addCredits");
        httpGet.setHeader("Authorization", "Bearer " + token); // Agregar la cabecera Authorization
    
        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(httpGet)) {
    
            int code = response.getCode();
            if (code != 200) {
                throw new Exception("No se ha podido continuar con la transacción");
            }
    
        } catch (Exception e) {
            throw new Exception("Error al conectar con el servicio de usuarios");
        }
    }

    public void checkToken(String token) throws Exception {
        HttpGet httpGet = new HttpGet(url + "validarToken");
        httpGet.setHeader("Authorization", "Bearer " + token); // Agregar la cabecera Authorization
    
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
    
    //Metodo publico que devuelve una instancia de la clase
    public static ProxyUsuarios get() {
        if (yo == null)
            yo = new ProxyUsuarios();

        return yo;
    }
    
}

