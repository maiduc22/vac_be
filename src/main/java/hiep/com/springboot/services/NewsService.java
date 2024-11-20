package hiep.com.springboot.services;

import hiep.com.springboot.models.entities.NewsEntity;
import hiep.com.springboot.payload.request.NewsCreateRequest;

import java.util.List;

public interface NewsService {
    NewsEntity create(NewsCreateRequest request);

    List<NewsEntity> getAll();

    List<NewsEntity> myNews();

    NewsEntity detail(String id);

    boolean delete(String id);

    NewsEntity update(String id, NewsCreateRequest request);
}
