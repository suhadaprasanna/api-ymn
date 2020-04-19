package api.ymn.ymnapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(value= {"api.ymn"})
@EnableZuulProxy
public class YmnApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(YmnApiApplication.class, args);
	}

}
