package hcmute.manage.club.uteclubs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("ute-clubs")
public class UteClubsConfigProperties {
  private String dbUsername;
  private String dbPassword;
  private String accessKey;
  private String secretKey;
}
