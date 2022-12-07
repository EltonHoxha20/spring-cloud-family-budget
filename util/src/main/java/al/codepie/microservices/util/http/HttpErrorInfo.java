package al.codepie.microservices.util.http;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Value
@AllArgsConstructor
public class HttpErrorInfo {

  HttpStatus httpStatus;
  String path;
  String message;
  ZonedDateTime timestamp;
}
