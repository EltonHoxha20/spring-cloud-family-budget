package al.codepie.microservices.util.http;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

@Component
@Log4j2
public class ServiceUtil {

  private final String port;
  private String serviceAddress = null;

  private static final DecimalFormat df = new DecimalFormat("#.##");


  @Autowired
  public ServiceUtil(@Value("${server.port}") String port) {

    this.port = port;
  }

  public String getServiceAddress() {
    if (serviceAddress == null) {
      serviceAddress = findMyHostname() + "/" + findMyIpAddress() + ":" + port;
    }
    return serviceAddress;
  }

  private String findMyHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "unknown host name";
    }
  }

  private String findMyIpAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      return "unknown IP address";
    }
  }

  public Double formatDouble(Double input){
    if(input!=null && input > 0){
      return Double.valueOf(df.format(input));
    }
    return  0.0;
  }
}
