package Repository.Interfaces;

import Model.Tag;

import java.util.List;

public interface TagInterface {
    Tag getTagById(Long id);
    List<Tag> getAllTags();
    Tag addTag(Tag tag);
    void updateTag(Tag tag);
    void deleteTag(Long id);
}
