package edu.uclm.esi.payments.services;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private ProxyStripe proxyStripe;
    private ProxyUsuarios proxyUsuarios;

    public String prepay(Map<String,Object> info) throws Exception {

        JSONObject jsoConf = leerConfig("src/main/resources/conf.json");
        return this.proxyStripe.prepay(jsoConf);

    }

    public JSONObject leerConfig(String filename) throws Exception {
            
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JSONObject config = new JSONObject(content);
            return config;
        
    }

    public void confirm(String token, Integer credits) throws Exception {
        this.proxyUsuarios.confirm(token, credits);
    }
    
}
