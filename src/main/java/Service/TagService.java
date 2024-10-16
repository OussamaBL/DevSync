package Service;

import Exceptions.FormatIncorrectException;
import Model.Tag;
import Model.Task;
import Model.User;
import Repository.TagRepository;
import Repository.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

public class TagService {
    private final TagRepository tagRepository;

    public TagService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devsync");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.tagRepository = new TagRepository(entityManager);
    }
    public Tag insertTag(Tag tag){
        if(tag==null && tag.equals(new Tag()))
            throw new RuntimeException("tag empty");
        if(findByName(tag.getName())!=null)
            throw new RuntimeException("tag already exist");
        return tagRepository.addTag(tag);
    }
    public List<Tag> getAllTags(){
        return tagRepository.getAllTags();
    }

    public Optional<Tag> findByName(String name){
        if(name==null && name.isEmpty())
            throw new FormatIncorrectException("format de valeur est incorrect");
        return tagRepository.findByName(name);
    }

    /*public List<Task> getTasksUser(User user){
        return tagRepository.getTasksUser(user);
    }*/
}
