package com.ispan.theater.service;

import com.ispan.theater.domain.Category;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.MoviePicture;
import com.ispan.theater.domain.Rated;
import com.ispan.theater.repository.*;
import com.ispan.theater.util.DatetimeConverter;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MovieActRepository movieActRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private RatedRepository ratedRepository;
    @Autowired
    private MoviePictureRepository moviePictureRepository;

    @CacheEvict(value = "movieFindList", allEntries = true)
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }//test passed

    @CacheEvict(value = "movieFindList", allEntries = true)
    public Movie getMovieById(Integer id) {//test passed
        Optional<Movie> optional = movieRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public boolean existsMovieByName(String name) {
        Movie movie = movieRepository.findByName(name);
        return movie != null;
    }

    @CacheEvict(value = "movieFindList", allEntries = true)
    public List<Movie> getMovieByName(String name, Integer page) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("start", 10 * page);
        jsonObject.put("rows", 10);
        return movieRepository.multiConditionFindMovie(jsonObject);
    }

    @CacheEvict(value = "movieFindList", allEntries = true)
    public Movie jsonToMovie(JSONObject jsonObject) {//test passed
        Movie movie = null;
        if (movieRepository.findByName(jsonObject.getString("name")) == null) {
            movie = new Movie();
            movie.setName(jsonObject.getString("name"));
            movie.setDescription(jsonObject.getString("description"));
            movie.setDirector(jsonObject.getString("director"));
            movie.setCategoryCode(jsonObject.getString("categoryCode"));
            movie.setDuration(jsonObject.getInt("duration"));
            movie.setRatedCode(ratedRepository.findByCode(jsonObject.getString("ratedCode")));
            movie.setReleaseDate(DatetimeConverter.parse(jsonObject.getString("releaseDate"), "yyyy-MM-dd"));
            movie.setEndDate(DatetimeConverter.parse(jsonObject.getString("endDate"), "yyyy-MM-dd"));
            movie.setCreateDate(new Date());
            movie.setModifyDate(new Date());
//            movie.setImage(jsonObject.getString("image"));
            movie.setViewer(0);
            movieRepository.save(movie);
            return movie;
        } else {
            return null;
        }
    }

    //棄用 目前使用multiFind1
    public List<Movie> multiFind(JSONObject jsonObject) {//test passed
        if (movieRepository.multiConditionFindMovie(jsonObject) != null) {
            return movieRepository.multiConditionFindMovie(jsonObject);
        } else {
            return null;
        }
    }

    @CacheEvict(value = "movieFindList", allEntries = true)
    public Movie updateMovie(JSONObject jsonObject) {//test pass
        System.out.println(jsonObject.toString());
        Integer id = jsonObject.getInt("id");
        String name = jsonObject.isNull("name") ? null : jsonObject.getString("name");
        String name_eng = jsonObject.isNull("name_eng") ? null : jsonObject.getString("name_eng");
        String description = jsonObject.isNull("description") ? null : jsonObject.getString("description");
        String director = jsonObject.isNull("director") ? null : jsonObject.getString("director");
        String releaseDate = jsonObject.isNull("releaseDate") ? null : jsonObject.getString("releaseDate");
        String endDate = jsonObject.isNull("endDate") ? null : jsonObject.getString("endDate");
        Double price = jsonObject.isNull("price") ? null : jsonObject.getDouble("price");
        JSONArray categories = jsonObject.getJSONArray("category");
        System.out.println(categories);
//        String category = jsonObject.isNull("category") ? null : jsonObject.getString("category");
        List<String> categoryCodes = new ArrayList<>();

        List<Object> code = categories.toList();
        categoryCodes.add(code.toString().trim().replace("[", "").replace("]", ""));

        String rated = jsonObject.isNull("rated") ? null : jsonObject.getString("rated");
        Integer duration = jsonObject.isNull("duration") ? null : jsonObject.getInt("duration");
        String image = jsonObject.isNull("image") ? null : jsonObject.getString("image");
        if (movieRepository.findById(id).isEmpty()) {
            return null;
        }
        Movie movie = movieRepository.findById(id).get();
        if (name != null && !name.isEmpty()) {
            movie.setName(name);
        }
        if (name_eng != null && !name_eng.isEmpty()) {
            movie.setName_eng(name_eng);
        }
        if (director != null && director.length() > 0) {
            movie.setDirector(director);
        }
        if (description != null && description.length() > 0) {
            movie.setDescription(description);
        }
        if (releaseDate != null && releaseDate.length() > 0) {
            movie.setReleaseDate(DatetimeConverter.parse(releaseDate, "yyyy-MM-dd"));
        }
        if (endDate != null && endDate.length() > 0) {
            movie.setEndDate(DatetimeConverter.parse(endDate, "yyyy-MM-dd"));
        }
        if (price != null && price.doubleValue() > 0) {
            movie.setPrice(price);
        }
        if (!categoryCodes.isEmpty()) {
            for (String categoryCode : categoryCodes) {
                movie.setCategoryCode(categoryCode);
            }

        }
        if (rated != null && rated.length() > 0) {
            Rated temp = ratedRepository.findByCode(rated);
            movie.setRatedCode(temp);
        }
        if (duration != null && duration > 0) {
            movie.setDuration(duration);
        }

        movie.setModifyDate(new Date());
        return movieRepository.save(movie);
    }

    //@CacheEvict(value = "movieFindList", allEntries = true)
    public Movie insertMovie(JSONObject jsonObject) {//test pass
        String name = jsonObject.isNull("name") ? null : jsonObject.getString("name");
        String name_eng = jsonObject.isNull("name_eng") ? null : jsonObject.getString("name_eng");
        String description = jsonObject.isNull("description") ? null : jsonObject.getString("description");
        String director = jsonObject.isNull("director") ? null : jsonObject.getString("director");
        String releaseDate = jsonObject.isNull("releaseDate") ? null : jsonObject.getString("releaseDate");
        String endDate = jsonObject.isNull("endDate") ? null : jsonObject.getString("endDate");
        Double price = jsonObject.isNull("price") ? null : jsonObject.getDouble("price");
        JSONArray categories = jsonObject.getJSONArray("category");
        System.out.println(categories);
        List<String> categoryCodes = new ArrayList<>();
        List<Object> code = categories.toList();
        categoryCodes.add(code.toString().trim().replace("[", "").replace("]", ""));
        String rated = jsonObject.isNull("rated") ? null : jsonObject.getString("rated");
        Integer duration = jsonObject.isNull("duration") ? null : jsonObject.getInt("duration");
        if (name == null || releaseDate == null || endDate == null || price == null || categories == null || rated == null || duration == null) {
            return null;
        }
//        if (movieRepository.findByName(name) != null) {
//            return null;
//        }
        Movie movie = new Movie();
        if (name != null && !name.isEmpty()) {
            movie.setName(name);

        }
        if (name_eng != null && !name_eng.isEmpty()) {
            movie.setName_eng(name_eng);

        }
        if (director != null && director.length() > 0) {
            movie.setDirector(director);

        }
        if (description != null && description.length() > 0) {
            movie.setDescription(description);
;
        }
        if (releaseDate != null && releaseDate.length() > 0) {
            movie.setReleaseDate(DatetimeConverter.parse(releaseDate, "yyyy-MM-dd"));

        }
        if (endDate != null && endDate.length() > 0) {
            movie.setEndDate(DatetimeConverter.parse(endDate, "yyyy-MM-dd"));

        }
        if (price != null && price.doubleValue() > 0) {
            movie.setPrice(price);

        }
        if (!categoryCodes.isEmpty()) {
            for (String categoryCode : categoryCodes) {
                movie.setCategoryCode(categoryCode);
            }
        }
        if (rated != null && rated.length() > 0) {
            Rated temp = ratedRepository.findByCode(rated);
            movie.setRatedCode(temp);
        }
        if (duration != null && duration > 0) {
            movie.setDuration(duration);
        }
        movie.setViewer(0);
        movie.setCreateDate(new Date());
        movie.setModifyDate(new Date());
        System.out.println(movie.toString());
        return movieRepository.save(movie);
    }

    @CacheEvict(value = "movieFindList", allEntries = true)
    public void deleteMovie(Movie movie) {
        List<MoviePicture> list= moviePictureRepository.findByMovieId(movie);
        moviePictureRepository.deleteAll(list);
        movieRepository.delete(movie);
    }

    @CacheEvict(value = "movieFindList", allEntries = true)
    public JSONObject movieToJson(Movie movie) {
        String photoUrl = "/backstage/movie/photo/" + movie.getId();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("id", movie.getId());
        jsonObject.put("name", movie.getName());
        jsonObject.put("name_eng", movie.getName_eng());
        jsonObject.put("description", movie.getDescription());
        jsonObject.put("director", movie.getDirector());
        jsonObject.put("releaseDate", movie.getReleaseDate());
        jsonObject.put("endDate", movie.getEndDate());
        jsonObject.put("price", movie.getPrice());
        String rateStr = movie.getCategoryCode();
        String[] cateArray = rateStr.split(",");
        for (String cate : cateArray) {
            Category temp = categoryRepository.findByCode(cate.trim());
            if (temp != null) {
                jsonArray.put(temp.getCode());
            }
        }
        jsonObject.put("category", jsonArray);
        jsonObject.put("rated", movie.getRatedCode().getCode());
        jsonObject.put("duration", movie.getDuration());
        return jsonObject;
    }

    public long count(String name) {
        return movieRepository.countByNameLike(name);
    }

    //多條件搜尋 分頁版本
    //@Cacheable(value = "movieFindList", key = "#root.methodName")
    public Page<Movie> findMulti1(JSONObject jsonObject) {
        String name = jsonObject.isNull("name") ? null : jsonObject.getString("name");
        String director = jsonObject.isNull("director") ? null : jsonObject.getString("director");
        String releaseDate = jsonObject.isNull("releaseDate") ? null : jsonObject.getString("releaseDate");
        String endDate = jsonObject.isNull("endDate") ? null : jsonObject.getString("endDate");
        Double startprice = jsonObject.isNull("startprice") ? null : jsonObject.getDouble("startprice");
        Double endprice = jsonObject.isNull("endprice") ? null : jsonObject.getDouble("endprice");
        String category = jsonObject.isNull("category") ? null : jsonObject.getString("category");
        String rated = jsonObject.isNull("rated") ? null : jsonObject.getString("rated");
        Integer startduration = jsonObject.isNull("startduration") ? null : jsonObject.getInt("startduration");
        Integer endduration = jsonObject.isNull("endduration") ? null : jsonObject.getInt("endduration");
        int start = jsonObject.isNull("start") ? 0 : jsonObject.getInt("start");
        int rows = jsonObject.isNull("rows") ? 10 : jsonObject.getInt("rows");
        String order = jsonObject.isNull("order") ? "name" : jsonObject.getString("order");
        boolean dir = !jsonObject.isNull("dir") && jsonObject.getBoolean("dir");
        Pageable pageable;
        if (dir) {
            pageable = PageRequest.of(start, rows, Sort.Direction.DESC, order);
        } else {
            pageable = PageRequest.of(start, rows, Sort.Direction.ASC, order);
        }
        Specification<Movie> spec = (root, query, builder) -> {
            //where
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                Pattern pattern = Pattern.compile("[\\u4E00-\\u9FA5]+");
                Matcher matcher = pattern.matcher(name);
                Predicate namePredicate;
                if (matcher.find()) {
                    System.out.println("中文");
                    namePredicate =builder.like(root.get("name"), "%" + name + "%");
                } else {
                    System.out.println("英文");
                    namePredicate=builder.like(root.get("name_eng"), "%" + name + "%");
                }
                Predicate directorPredicate = builder.like(root.get("director"), "%" + name + "%");
                predicates.add(builder.or(namePredicate, directorPredicate));
            }

//            if (director != null && !director.isEmpty()) {
//                predicates.add(builder.like(root.get("director"), "%" + director + "%"));
//            }
            if (releaseDate != null && !releaseDate.isEmpty()) {
                Date releaseParse = DatetimeConverter.parse(releaseDate, "yyyy-MM-dd");
                predicates.add(builder.greaterThanOrEqualTo(root.get("releaseDate"), releaseParse));
            }
            if (endDate != null && !endDate.isEmpty()) {
                Date endParse = DatetimeConverter.parse(endDate, "yyyy-MM-dd");
                predicates.add(builder.lessThanOrEqualTo(root.get("endDate"), endParse));
            }
            if (startprice != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), startprice));
            }
            if (endprice != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), endprice));
            }
            if (category != null && !category.isEmpty()) {
                String[] categories = category.split(",");
                Predicate[] categoryPredicates = new Predicate[categories.length];
                for (int i = 0; i < categories.length; i++) {
                    categoryPredicates[i] = builder.like(root.get("categoryCode"), "%" + categories[i] + "%");
                }
                predicates.add(builder.or(categoryPredicates));
            }
            if (startduration != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("duration"), startduration));
            }
            if (endduration != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("duration"), endduration));
            }
            if (rated != null) {
                Rated item = ratedRepository.findByCode(rated);
                predicates.add(builder.equal(root.get("ratedCode"), item));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        return movieRepository.findAll(spec, pageable);
    }

    public Long count(JSONObject jsonObject) {
        String name = jsonObject.isNull("name") ? null : jsonObject.getString("name");
        String director = jsonObject.isNull("director") ? null : jsonObject.getString("director");
        String releaseDate = jsonObject.isNull("releaseDate") ? null : jsonObject.getString("releaseDate");
        String endDate = jsonObject.isNull("endDate") ? null : jsonObject.getString("endDate");
        Double startprice = jsonObject.isNull("startprice") ? null : jsonObject.getDouble("startprice");
        Double endprice = jsonObject.isNull("endprice") ? null : jsonObject.getDouble("endprice");
        String category = jsonObject.isNull("category") ? null : jsonObject.getString("category");
        String rated = jsonObject.isNull("rated") ? null : jsonObject.getString("rated");
        Integer startduration = jsonObject.isNull("startduration") ? null : jsonObject.getInt("startduration");
        Integer endduration = jsonObject.isNull("endduration") ? null : jsonObject.getInt("endduration");
        Pageable pageable;
        Specification<Movie> spec = (root, query, builder) -> {
            //where
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                Pattern pattern = Pattern.compile("[\\u4E00-\\u9FA5]+");
                Matcher matcher = pattern.matcher(name);
                if (matcher.find()) {
                    System.out.println("中文");
                    predicates.add(builder.like(root.get("name"), "%" + name + "%"));
                } else {
                    System.out.println("英文");
                    predicates.add(builder.like(root.get("name_eng"), "%" + name + "%"));
                }
            }
            if (director != null && !director.isEmpty()) {
                predicates.add(builder.like(root.get("director"), "%" + director + "%"));
            }
            if (releaseDate != null && !releaseDate.isEmpty()) {
                Date releaseParse = DatetimeConverter.parse(releaseDate, "yyyy-MM-dd");
                predicates.add(builder.greaterThanOrEqualTo(root.get("releaseDate"), releaseParse));
            }
            if (endDate != null && !endDate.isEmpty()) {
                Date endParse = DatetimeConverter.parse(endDate, "yyyy-MM-dd");
                predicates.add(builder.lessThanOrEqualTo(root.get("endDate"), endParse));
            }
            if (startprice != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), startprice));
            }
            if (endprice != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), endprice));
            }
            if (category != null && !category.isEmpty()) {
                String[] categories = category.split(",");
                Predicate[] categoryPredicates = new Predicate[categories.length];
                for (int i = 0; i < categories.length; i++) {
                    categoryPredicates[i] = builder.like(root.get("categoryCode"), "%" + categories[i] + "%");
                }
                predicates.add(builder.or(categoryPredicates));
            }
            if (startduration != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("duration"), startduration));
            }
            if (endduration != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("duration"), endduration));
            }
            if (rated != null) {
                Rated item = ratedRepository.findByCode(rated);
                predicates.add(builder.equal(root.get("ratedCode"), item));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        return movieRepository.count(spec);
    }

    @CacheEvict(value = "movieFindList", allEntries = true)
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }


}
