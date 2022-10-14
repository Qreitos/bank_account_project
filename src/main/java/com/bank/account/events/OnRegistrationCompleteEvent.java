package com.bank.account.events;

import com.bank.account.model.entity.Customer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

  private String appUrl;
  private Customer customer;

  public OnRegistrationCompleteEvent(Customer customer, String appUrl) {
    super(customer);
    this.customer = customer;
    this.appUrl = appUrl;
  }
}
