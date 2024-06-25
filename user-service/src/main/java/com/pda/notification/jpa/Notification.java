package com.pda.notification.jpa;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    private int id;
    private int userId;
    @Column(name = "rebalancing_id")
    private int rebalancingId;
    private String summary;
    private Boolean checked;
    @CreatedDate
    private LocalDateTime createdAt;

    public Notification(int userId, int rebalancingId, Boolean checked, String summary) {
        this.userId = userId;
        this.rebalancingId = rebalancingId;
        this.checked = checked;
        this.summary = summary;
        this.createdAt = LocalDateTime.now();
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}