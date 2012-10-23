package com.anotherbrick.inthewall;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationCenter {

  private static NotificationCenter instance = new NotificationCenter();

  public enum EventName {
    GRAPH_YEAR_CHANGED
  }

  private HashMap<EventName, ArrayList<EventSubscriber>> subscribers;

  public NotificationCenter getInstance() {
    return instance;
  }

  private NotificationCenter() {
    subscribers = new HashMap<NotificationCenter.EventName, ArrayList<EventSubscriber>>();
  }

  public void registerToEvent(EventName eventName, EventSubscriber eventSubscriber) {
    if (!subscribers.get(eventName).contains(eventSubscriber)) {
      subscribers.get(eventName).add(eventSubscriber);
    }
  }

  public void notifyEvent(EventName eventName) {
    notifyEvent(eventName, null);
  }

  public void notifyEvent(EventName eventName, Object data) {
    for (EventSubscriber es : subscribers.get(eventName)) {
      es.eventReceived(eventName, data);
    }
  }
}
