package Model;

import Model.Enums.UserType;
import jakarta.persistence.*;
import lombok.extern.slf4j.XSlf4j;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "tokens")
    private int tokens;

    // Eager fetching for tasks created by the user
    @OneToMany(mappedBy = "user_create", fetch = FetchType.EAGER)
    private List<Task> createdTasks;

    // Eager fetching for tasks assigned to the user
    @OneToMany(mappedBy = "user_assigne", fetch = FetchType.EAGER)
    private List<Task> assignedTasks;

    public List<Task> getCreatedTasks() {
        return createdTasks;
    }

    public void setCreatedTasks(List<Task> createdTasks) {
        this.createdTasks = createdTasks;
    }

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(List<Task> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(Long id, String username, String password, String first_name, String last_name, String email,int tokens,UserType user_role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.role_user = user_role;
        this.tokens = tokens;
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
        this.tokens = 2;
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



    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public UserType getRole_user() {
        return role_user;
    }

    public void setRole_user(UserType role_user) {
        this.role_user = role_user;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }


}
