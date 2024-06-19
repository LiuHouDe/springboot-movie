package com.ispan.theater.dao;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Rated;
import com.ispan.theater.repository.RatedRepository;
import com.ispan.theater.util.DatetimeConverter;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
public class MovieDaoImpl implements MovieDao {
    private final RatedRepository ratedRepository;
    @PersistenceContext
    private Session session;

    public MovieDaoImpl(RatedRepository ratedRepository) {
        this.ratedRepository = ratedRepository;
    }

    private Session getSession() {
        return this.session;
    }
    @Override
    public List<Movie> multiConditionFindMovie(JSONObject jsonObject){
        String name=jsonObject.isNull("name")?null :jsonObject.getString("name");
        String director=jsonObject.isNull("director")?null :jsonObject.getString("director");
        String releaseDate=jsonObject.isNull("releaseDate")?null :jsonObject.getString("releaseDate");
        String endDate=jsonObject.isNull("endDate")?null :jsonObject.getString("endDate");
        Double startprice=jsonObject.isNull("startprice")?null :jsonObject.getDouble("startprice");
        Double endprice=jsonObject.isNull("endprice")?null :jsonObject.getDouble("endprice");
        String category=jsonObject.isNull("category")?null :jsonObject.getString("category");
        String rated=jsonObject.isNull("rated")?null :jsonObject.getString("rated");
        Integer startduration=jsonObject.isNull("startduration")?null :jsonObject.getInt("startduration");
        Integer endduration=jsonObject.isNull("endduration")?null :jsonObject.getInt("endduration");
        Integer start = jsonObject.isNull("start")?0 :jsonObject.getInt("start");
        Integer rows = jsonObject.isNull("rows")?10 :jsonObject.getInt("rows");
        String order = jsonObject.isNull("order") ? "name" : jsonObject.getString("order");
        boolean dir = !jsonObject.isNull("dir") && jsonObject.getBoolean("dir");

        CriteriaBuilder criteriaBuilder =this.getSession().getCriteriaBuilder();
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        //from movie
        Root<Movie> movieRoot = criteriaQuery.from(Movie.class);
        //where
        List<Predicate> predicates = new ArrayList<>();
        if( name !=null&& !name.isEmpty()){
            predicates.add(criteriaBuilder.like(movieRoot.get("name"), "%" + name + "%"));
        }

        if(director!=null&& !director.isEmpty()){
            predicates.add(criteriaBuilder.like(movieRoot.get("director"), "%" + director + "%"));
        }
        if(releaseDate!=null&& !releaseDate.isEmpty()){
            Date releaseParse = DatetimeConverter.parse(releaseDate,"yyyy-MM-dd");
            predicates.add(criteriaBuilder.greaterThan(movieRoot.get("releaseDate"), releaseParse));
        }
        if(endDate!=null&& !endDate.isEmpty()){
            Date endParse = DatetimeConverter.parse(endDate,"yyyy-MM-dd");
            predicates.add(criteriaBuilder.lessThan(movieRoot.get("endDate"), endParse));
        }
        if(startprice!=null){
            predicates.add(criteriaBuilder.greaterThan(movieRoot.get("price"), startprice));
        }
        if(endprice!=null){
            predicates.add(criteriaBuilder.lessThan(movieRoot.get("price"), endprice));
        }
        if(category!=null&& !category.isEmpty()){
            String[] categories = category.split(",");
            Predicate[] categoryPredicates = new Predicate[categories.length];
            for(int i=0;i < categories.length ; i++){
                categoryPredicates[i] = criteriaBuilder.like(movieRoot.get("categoryCode"), "%" + categories[i] + "%");
            }
            predicates.add(criteriaBuilder.or(categoryPredicates));
        }
        if(startduration!=null){
            predicates.add(criteriaBuilder.greaterThan(movieRoot.get("duration"), startduration));
        }
        if(endduration!=null){
            predicates.add(criteriaBuilder.lessThan(movieRoot.get("duration"), endduration));
        }
        if(rated!=null){
            Rated item = ratedRepository.findByCode(rated);
            predicates.add(criteriaBuilder.equal(movieRoot.get("ratedCode"), item));
        }
        if(predicates !=null && predicates.size()>0){
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        //where end
        //order by
        if(dir){
            criteriaQuery.orderBy(criteriaBuilder.desc(movieRoot.get(order)));
        }
        else{
            criteriaQuery.orderBy(criteriaBuilder.asc(movieRoot.get(order)));
        }
        //分頁
        TypedQuery<Movie> typedQuery = this.getSession().createQuery(criteriaQuery)
                .setFirstResult(start).setMaxResults(rows);
        List<Movie> movieList = typedQuery.getResultList();
        if(movieList!=null && movieList.size()>0){
            return movieList;
        }
        else{
            return null;
        }

    }
}
