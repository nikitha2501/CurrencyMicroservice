package com.nikitha.microservices.api_gateway;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private final Logger logger= LoggerFactory.
            getLogger(CircuitBreakerController.class);
    @GetMapping("/sampleApi")
   @Retry(name="sampleApi", fallbackMethod = "hardCodedResponse")
    @Retryable(maxAttempts = 5,backoff = @Backoff(delay = 1000,multiplier = 2,       // Exponential backoff (1s → 2s → 4s → 8s)
           maxDelay = 10000))

//    @CircuitBreaker(name="sampleApi", fallbackMethod = "hardCodedResponse")

//    @RateLimiter(name="sampleApi")
//    @Bulkhead(name="sampleApi")
    public String sampleApi(){
        logger.info("sample Api call received");
        ResponseEntity<String> forEntity=new RestTemplate().
                getForEntity("http://localhost:8080/api",String.class);
        return forEntity.getBody();
//        return "sample-api";
    }

    @Recover
    public String hardCodedResponse(Exception ex){
        return " fallback Method";
    }
}
