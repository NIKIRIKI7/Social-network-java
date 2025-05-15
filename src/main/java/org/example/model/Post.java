package org.example.model;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "posts")
public class Post extends Model {

    @Id
    private UUID id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Column
    private Timestamp creationDate;

    @Column
    private Timestamp lastEditDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column
    private int likesCount;

    @Column
    private String attachmentUrl;

    @Column
    private boolean isPublic;

    public Post() {
        this.id = UUID.randomUUID();
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.likesCount = 0;
        this.isPublic = true;
    }

    // Геттеры и сеттеры
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.lastEditDate = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Timestamp lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public String toString() {
        return "Post " + id + " by " + (author != null ? author.getLogin() : "unknown");
    }
} 