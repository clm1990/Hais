package com.hais.hais1000.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Map;

@Slf4j
public class JWTUtil {


        private static final String SECRET = "X-Token";
        //token过期时间
        public static final Integer EXPIRE_DATE = 7;


        /**
         *  生成token
         * @param map
         * @return
         */
        public static String createToken(Map<String,String> map){
            //创建过期时间
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.HOUR_OF_DAY, EXPIRE_DATE);  //10分钟

            //创建builder对象
            JWTCreator.Builder builder = JWT.create();
            //遍历map
            map.forEach((k,v)->{
                builder.withClaim(k,v);
            });
            String token = builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SECRET));

            return token;
        }

        /**
         *  验证token
         *  验证过程中如果有异常，则抛出；
         *  如果没有,则返回 DecodedJWT 对象来获取用户信息;
         * @param token
         */
        public static boolean verifyToken(String token){
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            try {
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                jwtVerifier.verify(token);
                return true;
            }catch (SignatureVerificationException e) {
                //验证的签名不一致
                e.printStackTrace();
                return false;
            }catch (TokenExpiredException e){
                e.printStackTrace();
                return false;
            }catch (AlgorithmMismatchException e){
                e.printStackTrace();
                return false;
            }catch (InvalidClaimException e){
                e.printStackTrace();
                return false;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        /**
         *  验证token
         *  验证过程中如果有异常，则抛出；
         *  如果没有,则返回 DecodedJWT 对象来获取用户信息;
         * @param token
         */
        public static DecodedJWT verify(String token){
            return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        }


}
