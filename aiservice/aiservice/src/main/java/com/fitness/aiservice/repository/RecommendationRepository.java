package com.fitness.aiservice.repository;

import com.fitness.aiservice.model.Recommendation;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends ReactiveMongoRepository<Recommendation, String> {
     List<Recommendation> findByUserId(String userId);

    Optional<Recommendation> findByActivityId(String activityId);
}
