package edu.uclm.esi.payments.services;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private ProxyStripe proxyStripe;

    public String prepay() throws Exception {

        JSONObject jsoConf = leerConfig("src/main/resources/conf.json");
        return this.proxyStripe.prepay(jsoConf);

    }

    public JSONObject leerConfig(String filename) throws Exception {
            
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JSONObject config = new JSONObject(content);
            return config;
        
    }
    
}
