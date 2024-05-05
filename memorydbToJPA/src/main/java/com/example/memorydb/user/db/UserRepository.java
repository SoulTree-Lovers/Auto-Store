package com.example.memorydb.user.db;

import com.example.memorydb.user.model.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //query method는 카멜 케이스 기준으로 잘라서 파라미터의 순서대로 매칭

    //select * from user where score > {??}
    List<UserEntity> findAllByScoreGreaterThan(int sc);

    //select * from user where score >= {??}
    List<UserEntity> findAllByScoreGreaterThanEqual(int sc);

    //Query Method 방식 추천 join 같은 복잡한 쿼리문 사용하는 경우 native query 방식 추천
    //파라미터 바인딩은 귀찮더라고 파라미터 네임 바인딩 권장
    //select * from user where score >= ?? and score <=??
    List<UserEntity> findAllByScoreGreaterThanEqualAndScoreLessThanEqual(int min, int max);

    //jpql 문법 사용해서 하는 방법도 있음  *->u , nativeQuery=true 삭제
    //natvie query 방식
    @Query(
            value = "select * from user u where u.score >= ?1 AND u.score <= ?2",
            nativeQuery = true
    )
    List<UserEntity> score(int min, int max);

    //named parameter 기법
    /*@Query(
            value = "select * from user u where u.score >= :min AND u.score <= :max",
            nativeQuery = true
    )
    List<UserEntity> score(
            @Param(value="min") int min,
            @Param(value="max") int max);*/
}
