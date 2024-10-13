package Model;

import Model.Enums.StatusRequest;
import Model.Enums.TypeRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class TaskRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date_request")
    private LocalDate date_request;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusRequest status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeRequest type;


    public TaskRequest(User user,Task task,LocalDate date_request,StatusRequest status){
        this.user=user;
        this.task=task;
        this.date_request=LocalDate.now();
        this.status=status;
    }

    public TaskRequest() {

    }
}
