package com.github.qiuzhanghua.jnatsdemo;

import io.nats.client.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@SpringBootApplication
public class JnatsDemoApplication {

  public static void main(String[] args) {
//    SpringApplication.run(JnatsDemoApplication.class, args);
    try {
      Connection connection = Nats.connect(Nats.DEFAULT_URL);

      // send
//      connection.publish("foo", "从Java 发消息".getBytes("UTF-8"));
//      connection.close();

      // receive
      AsyncSubscription subscribe = connection.subscribe("foo", msg -> {
        try {
          System.out.println(new String(msg.getData(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      });
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          System.out.println("Closing ...");
          subscribe.unsubscribe();
          connection.close();
          System.out.println("Closed.");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
