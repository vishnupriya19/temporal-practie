package io.workshop.practice.Versioning;


import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import io.workshop.practice.Versioning.model.Customer;
import io.workshop.practice.Versioning.model.Account;

import java.time.Duration;

public class CustomerWorkflowImpl implements CustomerWorkflow {
    private final CustomerActivities customerActivities =
            Workflow.newActivityStub(
                    CustomerActivities.class,
                    ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofMinutes(6))
                            .build());

    private int bonus = 100;

    @Override
    public Account execute(Customer customer) {

        // Fail and fix error in getCustomerAccounts
        Account account = customerActivities.getCustomerAccount(customer);

        // STEP 1

        /*
        int version = Workflow.getVersion("addedCheck", Workflow.DEFAULT_VERSION, 1);

        //System.out.println(version);
        if (version == 1) {
            System.out.println("inside");
            Workflow.sleep(Duration.ofSeconds(1));
            boolean checked = customerActivities.checkCustomerAccount(customer);
       }
         */


        //Workflow.sleep(Duration.ofSeconds(1));
        //boolean checked = customerActivities.checkCustomerAccount(customer);
        Workflow.sleep(Duration.ofMinutes(1));
        //Workflow.sleep(Duration.ofSeconds(1));
        //boolean tst = customerActivities.checkCustomerAccount(customer);


        /*
        int newVersion = Workflow.getVersion("addedCheck2", Workflow.DEFAULT_VERSION, 2);
        //System.out.println(newVersion);
        if (newVersion == 2) {
             System.out.println("inside after sleep");
            Workflow.sleep(Duration.ofSeconds(1));
            boolean checked = customerActivities.checkCustomerAccount(customer);
           }
           */

        // END STEP 1


        // STEP 2
//        int version2 = Workflow.getVersion("addedBonus", Workflow.DEFAULT_VERSION, 2);
//        if(version2 == 2) {
//            bonus = 200;
//        }

        account = customerActivities.updateCustomerAccount(account, bonus, "Added bonus of: " + bonus);

        // STEP 2
//        if(version2 == 2) {
//            customerActivities.sendBonusEmail(customer, "You received a bonus!");
//        }

        return account;

    }
}