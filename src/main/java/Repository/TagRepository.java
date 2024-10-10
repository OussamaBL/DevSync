package Repository;

import Model.Tag;
import Repository.Interfaces.TagInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TagRepository implements TagInterface {

    private final EntityManager entityManager;

    public TagRepository(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    public Tag getTagById(Long id) {
        return null;
    }

    @Override
    public List<Tag> getAllTags() {
        try {
            List<Tag> listTags= entityManager.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
            return listTags;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Tag addTag(Tag tag) {
        EntityTransaction transaction= entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.persist(tag);
            transaction.commit();
            return tag;
        }
        catch (Exception e) {
            if (transaction.isActive()) {
                System.out.println(e.getMessage());
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }
    public Optional<Tag> findByName(String name) {
        try {
            Tag t = entityManager.createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(t);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("error for tag by name: " + e.getMessage());
        }
    }

    @Override
    public void updateTag(Tag tag) {

    }

    @Override
    public void deleteTag(Long id) {

    }
}
