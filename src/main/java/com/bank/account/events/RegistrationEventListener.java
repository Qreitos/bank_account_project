package com.bank.account.events;

import com.bank.account.model.entity.Customer;
import com.bank.account.service.CustomerService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationEventListener implements ApplicationListener<OnRegistrationCompleteEvent> {

  private final CustomerService customerService;
  private final JavaMailSender javaMailSender;

  public void onApplicationEvent(@NotNull OnRegistrationCompleteEvent event) {
    this.confirmRegistration(event);
  }

  private void confirmRegistration(OnRegistrationCompleteEvent event) {
    Customer customer = event.getCustomer();
    String token = UUID.randomUUID().toString();
    this.customerService.createVerificationToken(customer, token);
    String recipientAddress = customer.getEmail();
    String subject = "Registration confirmation";

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom("bank.account@azet.sk");
    mailMessage.setTo(recipientAddress);
    mailMessage.setSubject(subject);
    mailMessage.setText(
        "Hello, "
            + customer.getForName()
            + "!\n\n Please use this token for confirmation of your account by POST method "
            + "(/api/verification?token=[your_token]"
            + ("\n\n" + token + "\n\nThank you and have a nice day. :)"));

    javaMailSender.send(mailMessage);
  }
}
