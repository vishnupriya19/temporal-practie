package io.workshop.practice.AsyncActivities;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class GreetingWorkflowImpl implements GreetingWorkflow {
    private final GreetingActivities activities =
            Workflow.newActivityStub(
                    GreetingActivities.class,
                    ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(10)).build());

    @Override
    public String getGreeting(String name) {
        // This is a blocking call that returns only after the activity has completed.
        return activities.composeGreeting("Hello", name);
    }
}
