package io.workshop.practice.AsyncActivities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface GreetingActivities {
    String composeGreeting(String greeting, String name);
}