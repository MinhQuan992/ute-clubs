package hcmute.manage.club.uteclubs.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {
    private static final Integer EXPIRE_MINUTES = 2;

    private final LoadingCache<String, Integer> otpCache;

    public OtpService() {
        super();
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINUTES, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    //This method is used to push the OTP number against Key. Rewrite the OTP if it exists
    //Using username as Key
    public int generateOTP(String key) {
        clearOTP(key);
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    //This method is used to return the OTP number against Key.
    //Key value is username
    public int getOtp(String key) {
        try {
            return otpCache.get(key);
        } catch (Exception e) {
            return 0;
        }
    }

    //This method is used to clear the OTP caught already
    public void clearOTP(String key){
        otpCache.invalidate(key);
    }
}
