package io.workshop.practice.AsyncActivities;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import io.temporal.client.ActivityCompletionClient;

import java.util.concurrent.ForkJoinPool;

public class GreetingActivitiesImpl implements GreetingActivities {
    private final ActivityCompletionClient completionClient;

    GreetingActivitiesImpl(ActivityCompletionClient completionClient) {
        this.completionClient = completionClient;
    }

    @Override
    public String composeGreeting(String greeting, String name) {

        // Get the activity execution context
        ActivityExecutionContext context = Activity.getExecutionContext();

        // Set a correlation token that can be used to complete the activity asynchronously
        byte[] taskToken = context.getTaskToken();
        ForkJoinPool.commonPool().execute(() -> composeGreetingAsync(taskToken, greeting, name));
        //System.out.println(taskToken);
        context.doNotCompleteOnReturn();

        // Since we have set doNotCompleteOnReturn(), the workflow action method return value is
        // ignored.
        return "ignored";
    }

    // Method that will complete action execution using the defined ActivityCompletionClient
    private void composeGreetingAsync(byte[] taskToken, String greeting, String name) {
        String result = greeting + " " + name + "!";

        // Complete our workflow activity using ActivityCompletionClient
        completionClient.complete(taskToken, result);
    }
}