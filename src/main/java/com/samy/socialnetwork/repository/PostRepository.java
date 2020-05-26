package com.samy.socialnetwork.repository;

import com.samy.socialnetwork.domain.Post;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Post entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

   // Page<ProductOrder> findAllByCustomerUserLogin(String login, Pageable pageable);
   List<Post> findAllByUserProfileUserLogin(String login);

}
