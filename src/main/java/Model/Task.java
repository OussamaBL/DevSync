package Model;

import Model.Enums.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title",unique = true,nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "date_create",nullable = false)
    private LocalDate date_create;

    public LocalDate getDate_start() {
        return date_start;
    }

    public void setDate_start(LocalDate date_start) {
        this.date_start = date_start;
    }

    @Column(name = "date_start")
    private LocalDate date_start;

    @Column(name = "date_fin",nullable = true)
    private LocalDate date_fin;
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private TaskStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_create_id", nullable = false)
    private User user_create;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_assigne_id", nullable = false)
    private User user_assigne;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> listTags ;

    public int getId() {
        return id;
    }

    public User getUser_create() {
        return user_create;
    }

    public void setUser_create(User user_create) {
        this.user_create = user_create;
    }

    public User getUser_assigne() {
        return user_assigne;
    }

    public void setUser_assigne(User user_assigne) {
        this.user_assigne = user_assigne;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate_create() {
        return date_create;
    }

    public void setDate_create(LocalDate date_create) {
        this.date_create = date_create;
    }


    public LocalDate getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(LocalDate date_fin) {
        this.date_fin = date_fin;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }



    public List<Tag> getListTags() {
        return listTags;
    }

    public void setListTags(List<Tag> listTags) {
        this.listTags = listTags;
    }

    public Task(int id, String title, String description,LocalDate date_start, LocalDate date_fin, TaskStatus status, User user_create,User user_assigne) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date_start = date_start;
        this.date_fin = date_fin;
        this.status = status;
        this.user_create = user_create;
        this.user_assigne = user_assigne;
        this.listTags = new ArrayList<>();
    }
    public Task(int id,LocalDate date_fin,TaskStatus taskStatus){
        this.id=id;
        this.date_fin=date_fin;
        this.status=taskStatus;
    }
    public Task(int id){
        this.id=id;
        this.listTags = new ArrayList<>();
    }
    public Task(){
        this.listTags = new ArrayList<>();
    }
    public Task(String title,String description,LocalDate date_start, LocalDate date_fin,User user_create,User user_assigne, TaskStatus status){
        this.title=title;
        this.description=description;
        this.date_create=LocalDate.now();
        this.date_start=date_start;
        this.date_fin=date_fin;
        this.status=status;
        this.user_create=user_create;
        this.user_assigne=user_assigne;
        this.listTags=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date_create=" + date_create +
                ", date_start=" + date_start +
                ", date_fin=" + date_fin +
                ", status=" + status +
                ", user_create=" + user_create +
                ", user_assigne=" + user_assigne +
               // ", listTags=" + listTags +
                '}';
    }

    /*@PrePersist
    @PreUpdate
    public void validateDates() {
        if (this.date_create.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Task creation date cannot be in the past.");
        }
    }*/

}
