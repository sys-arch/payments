package edu.uclm.esi.payments.services;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class ProxyStripe {

    public String prepay (JSONObject jsoConf, int credits) {
        
        String key = jsoConf.getString("stripeAPIKey");
        String currency = jsoConf.getString("currency");
        long amount = (long) (jsoConf.getFloat("price") * 100);
        amount = amount * credits;
        if (credits <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid amount");
        }
        Stripe.apiKey = key;

        try {
            PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
                    .setCurrency(currency)
                    .setAmount(amount)
                    .build();
            PaymentIntent intent = PaymentIntent.create(params);
            JSONObject jso = new JSONObject(intent.toJson());
            String clientSecret = jso.getString("client_secret");
            return clientSecret;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Error creating payment intent");
        }
    }
    
}
