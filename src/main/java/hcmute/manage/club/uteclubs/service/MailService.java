package hcmute.manage.club.uteclubs.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import hcmute.manage.club.uteclubs.UteClubsConfigProperties;
import hcmute.manage.club.uteclubs.model.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
  private final UteClubsConfigProperties uteClubsConfigProperties;

  public void sendEmail(Mail mail) {
    try {
      AWSCredentials credentials =
          new BasicAWSCredentials(
              uteClubsConfigProperties.getAccessKey(), uteClubsConfigProperties.getSecretKey());
      AmazonSimpleEmailService client =
          AmazonSimpleEmailServiceClientBuilder.standard()
              .withRegion(Regions.US_EAST_1)
              .withCredentials(new AWSStaticCredentialsProvider(credentials))
              .build();
      SendEmailRequest request =
          new SendEmailRequest()
              .withDestination(new Destination().withToAddresses(mail.getMailTo()))
              .withMessage(
                  new Message()
                      .withBody(
                          new Body()
                              .withHtml(
                                  new Content()
                                      .withCharset("UTF-8")
                                      .withData(mail.getMailContent())))
                      .withSubject(
                          new Content().withCharset("UTF-8").withData(mail.getMailSubject())))
              .withSource(mail.getMailFrom());
      client.sendEmail(request);
      log.info("Email sent!");
    } catch (Exception ex) {
      log.error("The email was not sent. Error message: {}", ex.getMessage());
    }
  }
}
