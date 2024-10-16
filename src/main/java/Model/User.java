package Model;

import Model.Enums.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.XSlf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name", nullable = false)
    private String last_name;

    @Column(name="email",unique = true,nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_user", nullable = false)
    private UserType role_user;

    @Column(name = "monthlyToken")
    private int monthlyToken;

    @Column(name = "dailyToken")
    private int dailyToken;

    // Eager fetching for tasks created by the user
    @OneToMany(mappedBy = "user_create", fetch = FetchType.EAGER)
    private List<Task> createdTasks;

    // Eager fetching for tasks assigned to the user
    @OneToMany(mappedBy = "user_assigne", fetch = FetchType.EAGER)
    private List<Task> assignedTasks;

    public User(Long id, String username, String password, String first_name, String last_name, String email,int monthlyToken,int dailyToken,UserType user_role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.role_user = user_role;
        this.monthlyToken = monthlyToken;
        this.dailyToken = dailyToken;
        createdTasks=new ArrayList<>();
        assignedTasks=new ArrayList<>();
    }
    public User(String username, String password, String first_name, String last_name, String email, UserType user_role) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.role_user = user_role;
        this.monthlyToken = 1;
        this.dailyToken = 2;
        createdTasks=new ArrayList<>();
        assignedTasks=new ArrayList<>();
    }
    public User(){
        createdTasks=new ArrayList<>();
        assignedTasks=new ArrayList<>();
    }

    public User(Long id){
        this.id=id;
        createdTasks=new ArrayList<>();
        assignedTasks=new ArrayList<>();
    }

}
