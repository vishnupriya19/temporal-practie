package io.workshop.practice.Versioning;

import io.temporal.activity.Activity;
import io.workshop.practice.Versioning.model.Customer;
import io.workshop.practice.Versioning.model.Account;

public class CustomerActivitiesImpl implements CustomerActivities {
    @Override
    public boolean checkCustomerAccount(Customer customer) {
        return true;
    }

    @Override
    public Account getCustomerAccount(Customer customer) {
        // create some dummy account
      Account account = new Account("account-" + customer.getAccountNum(), customer.getAccountNum(),
                "Customer Account", customer);

        return account;
        //throw Activity.wrap(new NullPointerException("demo npe error..."));
    }

    @Override
    public Account updateCustomerAccount(Account account, int amount, String message) {
        account.updateAmount(amount, message);
        return account;
    }

    @Override
    public void sendBonusEmail(Customer customer, String message) {
        // sending some email via 3rd party libs...
    }
}