package com.project.web.model;

import jakarta.persistence.*;

@Entity
public class FavoriteEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "event_id")
    private String eventId;

    // 기본 생성자
    public FavoriteEvent() {
    }

    // 생성자
    public FavoriteEvent(String username, String eventId) {
        this.username = username;
        this.eventId = eventId;
    }

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
