package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_messages")
public class TicketMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private Long ticketId;

    @Column(nullable = false)
    private String senderEmail;

    @Column(length = 2000, nullable = false)
    private String messageText;

    @Column(nullable = false)
    private String visibilityScope = "CLIENT_VISIBLE"; // CLIENT_VISIBLE, INTERNAL_NOTE

    private String attachmentUrl;
    private LocalDateTime createdAt;

    public TicketMessage() {}

    public TicketMessage(Long id, String tenantId, Long ticketId, String senderEmail, String messageText, String visibilityScope, String attachmentUrl, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.senderEmail = senderEmail;
        this.messageText = messageText;
        this.visibilityScope = visibilityScope;
        this.attachmentUrl = attachmentUrl;
        this.createdAt = createdAt;
    }

    public static TicketMessageBuilder builder() {
        return new TicketMessageBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
    public String getVisibilityScope() { return visibilityScope; }
    public void setVisibilityScope(String visibilityScope) { this.visibilityScope = visibilityScope; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class TicketMessageBuilder {
        private Long id;
        private String tenantId;
        private Long ticketId;
        private String senderEmail;
        private String messageText;
        private String visibilityScope = "CLIENT_VISIBLE";
        private String attachmentUrl;
        private LocalDateTime createdAt;

        public TicketMessageBuilder id(Long id) { this.id = id; return this; }
        public TicketMessageBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public TicketMessageBuilder ticketId(Long ticketId) { this.ticketId = ticketId; return this; }
        public TicketMessageBuilder senderEmail(String senderEmail) { this.senderEmail = senderEmail; return this; }
        public TicketMessageBuilder messageText(String messageText) { this.messageText = messageText; return this; }
        public TicketMessageBuilder visibilityScope(String visibilityScope) { this.visibilityScope = visibilityScope; return this; }
        public TicketMessageBuilder attachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; return this; }
        public TicketMessageBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public TicketMessage build() {
            return new TicketMessage(id, tenantId, ticketId, senderEmail, messageText, visibilityScope, attachmentUrl, createdAt);
        }
    }
}
