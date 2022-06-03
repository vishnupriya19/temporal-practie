package io.workshop.practice.Versioning;

import io.temporal.activity.ActivityInterface;
import io.workshop.practice.Versioning.model.Customer;
import io.workshop.practice.Versioning.model.Account;

@ActivityInterface
public interface CustomerActivities {
    boolean checkCustomerAccount(Customer customer);
    Account getCustomerAccount(Customer customer);
    Account updateCustomerAccount(Account account, int amount, String message);
    void sendBonusEmail(Customer customer, String message);
}
