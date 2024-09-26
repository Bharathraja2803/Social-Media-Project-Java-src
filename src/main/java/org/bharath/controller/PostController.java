/**
 * This is the controller used to handle the post page requests
 */
package org.bharath.controller;

import org.bharath.Main;
import org.bharath.dao.*;
import org.bharath.model.Comment;
import org.bharath.model.Like;
import org.bharath.model.Post;
import org.bharath.model.Users;
import org.bharath.utils.DBConnection;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class PostController {
    private static final Connection CONNECTION = DBConnection.getConnection();
    private static final PostDaoImpl POST_DAO = PostDaoImpl.getInstance(CONNECTION);
    private static LikeDaoImpl likeDao_ = LikeDaoImpl.getInstance(CONNECTION);
    private static CommentDaoImpl commentDao_ = CommentDaoImpl.getInstance(CONNECTION);
    private static FollowerDaoImpl followerDao_ = FollowerDaoImpl.getInstance(CONNECTION);
    private static UsersDaoImpl usersDaoImp_ = UsersDaoImpl.getInstance(CONNECTION);

    public PostController(){}

    public List<Post> getAllMyPost(int userId){
        return POST_DAO.getAllMyPost(userId);
    }

    public boolean createPost(String postContent, int userId){
        return POST_DAO.createPost(postContent, userId);
    }

    public List<Post> getAllUsersPost(){
        List<Users> listOfAllUsers = usersDaoImp_.listAllUsers();

        if(listOfAllUsers == null){
            Main.LOGGER.warning("Something went wrong!");
            return null;
        }

        List<Post> allFollowersPost = new ArrayList<>();
        for(Users users : listOfAllUsers){
            List<Post> allMyPostPerUser = POST_DAO.getAllMyPost(users.getUserId_());
            if(allMyPostPerUser == null){
                Main.LOGGER.warning("Something went wrong in fetching all users post");
                continue;
            }
            allFollowersPost.addAll(allMyPostPerUser);
        }

        if(allFollowersPost.isEmpty()){
            Main.LOGGER.warning("No post posted by any user");
            return null;
        }

        allFollowersPost.sort(new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return LocalDateTime.of(post.getPostedDate(), post.getPostedTime()).compareTo(LocalDateTime.of(t1.getPostedDate(), t1.getPostedTime()));
            }
        }.reversed());

        return allFollowersPost;
    }

    public List<Post> listAllPostsOfFollower(int userId){
        List<Users> listOfAllFollowedUsers = followerDao_.listAllFollowedUsers(userId);

        if(listOfAllFollowedUsers == null){
            Main.LOGGER.warning("Something went wrong!");
            return null;
        }

        List<Post> allFollowersPost = new ArrayList<>();
        for(Users users : listOfAllFollowedUsers){
            List<Post> allMyPostPerUser = POST_DAO.getAllMyPost(users.getUserId_());
            if(allMyPostPerUser == null){
                Main.LOGGER.warning("Something went wrong in fetching our own post");
                continue;
            }
            allFollowersPost.addAll(allMyPostPerUser);
        }

        if(allFollowersPost.isEmpty()){
            Main.LOGGER.warning("No post posted by our followers");
            return null;
        }

        allFollowersPost.sort(new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return LocalDateTime.of(post.getPostedDate(), post.getPostedTime()).compareTo(LocalDateTime.of(t1.getPostedDate(), t1.getPostedTime()));
            }
        });
        return allFollowersPost;
    }

    public boolean removeThePost(int postId){
        boolean isCommentsDeleted = commentDao_.deleteAllCommentsForThePost(postId);
        if(!isCommentsDeleted){
            Main.LOGGER.warning("Something went wrong in deleting the comments!");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return false;
        }

        boolean isLikesDeleted = likeDao_.removeAllLikeForSpecificPost(postId);
        if(!isLikesDeleted){
            Main.LOGGER.warning("Something went wrong in deleting all the likes of the user!");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return false;
        }

        return POST_DAO.removePost(postId);

    }

    public List<Comment> getAllCommentsForThePost(int postId){
        return commentDao_.getAllCommentsForThePost(postId);
    }

    public List<Like> getAllLikesForThePost(int postId){
        return likeDao_.getAllLikesForThePost(postId);
    }

    public boolean commentThePost(int userId, int postId, String commentContent){
        return commentDao_.commentThePost(userId, postId, commentContent);
    }

    public boolean removeLikeForPost(int userId, int postId){
        Like like = likeDao_.getLike(userId, postId);
        return likeDao_.removeLike(like.getLikeId());
    }

    public boolean likeThePost(int userId, int postId){
        Like like = likeDao_.getLike(userId, postId);
        if(like == null){
            boolean isLikeActionDone = likeDao_.likeThePost(userId, postId);
            if(isLikeActionDone){
                Main.LOGGER.info("Like action completed successfully");
            }else{
                Main.LOGGER.warning("Something went wrong in liking the post!");
            }
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return true;
        }
        return false;
    }
}
