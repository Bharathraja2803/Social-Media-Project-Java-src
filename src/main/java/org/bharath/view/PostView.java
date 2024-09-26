/**
 * This is a view for post page to print the menu
 */
package org.bharath.view;

import javafx.geometry.Pos;
import org.bharath.Main;
import org.bharath.controller.PostController;
import org.bharath.controller.UsersController;
import org.bharath.model.Comment;
import org.bharath.model.Like;
import org.bharath.model.Post;
import org.bharath.model.Users;
import org.bharath.utils.MyIterator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PostView {
    private final PostController postController_ = new PostController();
    public PostView(){
    }
    public void printPostMainPageMenu(){
        System.out.println("Choose the below options: \n" +
                "\t1. Create Post\n" +
                "\t2. View all My Post one by one\n" +
                "\t3. View all Friends Post one by one\n" +
                "\t0. Back to previous page");
    }

    /**
     * This method performs the action related to post by the normal user
     *                 "\t1. Create Post\n" +
     *                 "\t2. View all My Post one by one\n" +
     *                 "\t3. View all Friends Post one by one\n" +
     *                 "\t0. Back to previous page
     */
    public void chooseOptionsMainPostPage(){

        postMainMethodLoop:
        while(true){
            printPostMainPageMenu();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0,3);
            switch(choice){
                case 0:
                {
                    Main.LOGGER.info("Back to previous menu");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break postMainMethodLoop;
                }
                case 1:
                {
                    System.out.println("Enter the post content:(enter 'q' to stop drafting content) ");
                    StringBuilder postContent = new StringBuilder();
                    while(true){
                        String line = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                        if(line.equals("q")){
                            break;
                        }
                        postContent.append(line);
                    }
                    if(postContent.toString().isEmpty()){
                        Main.LOGGER.warning("Post content is empty!");
                        break;
                    }
                    boolean isPostCreated = postController_.createPost(postContent.toString(),UsersController.getUsers_().getUserId_());
                    if(!isPostCreated){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Post created successfully");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 2:
                {
                    List<Post> allMyPost = postController_.getAllMyPost(UsersController.getUsers_().getUserId_());
                    if(allMyPost == null){
                        Main.LOGGER.warning("Something went wrong in fetching our own post");
                        break;
                    }
                    MyIterator<Post> myPostIterator = new MyIterator<>(allMyPost.listIterator());
                    Post currentPost = myPostIterator.next();

                    postIteratorAndAction(currentPost, myPostIterator, true);
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 3:
                {
                    List<Post> allFollowersPost = postController_.listAllPostsOfFollower(UsersController.getUsers_().getUserId_());

                    if(allFollowersPost == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    MyIterator<Post> myPostIterator = new MyIterator<>(allFollowersPost.listIterator());
                    Post currentPost = myPostIterator.next();

                    postIteratorAndAction(currentPost, myPostIterator, false);
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
            }
        }
    }

    /**
     * This method performs the action related to post by the admin user
     *                  "\t1. Create Post\n" +
     *                 "\t2. View all My Post one by one\n" +
     *                 "\t3. View all others Post one by one\n" +
     *                 "\t0. Back to previous page"
     */
    public void chooseOptionsMainPostPageIfAdmin(){

        postMainMethodLoop:
        while(true){
            printPostMainPageMenuForAdmin();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0,3);
            switch(choice){
                case 0:
                {
                    Main.LOGGER.info("Back to previous menu");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break postMainMethodLoop;
                }
                case 1:
                {
                    System.out.println("Enter the post content:(enter 'q' to stop drafting content) ");
                    StringBuilder postContent = new StringBuilder();
                    while(true){
                        String line = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                        if(line.equals("q")){
                            break;
                        }
                        postContent.append(line);
                    }
                    if(postContent.toString().isEmpty()){
                        Main.LOGGER.warning("Post content is empty!");
                        break;
                    }
                    boolean isPostCreated = postController_.createPost(postContent.toString(),UsersController.getUsers_().getUserId_());
                    if(!isPostCreated){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Post created successfully");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 2:
                {
                    List<Post> allMyPost = postController_.getAllMyPost(UsersController.getUsers_().getUserId_());
                    if(allMyPost == null){
                        Main.LOGGER.warning("Something went wrong in fetching our own post");
                        break;
                    }
                    MyIterator<Post> myPostIterator = new MyIterator<>(allMyPost.listIterator());
                    Post currentPost = myPostIterator.next();

                    postIteratorAndAction(currentPost, myPostIterator, true);
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 3:
                {
                    List<Post> allUsersPost = postController_.getAllUsersPost();

                    if(allUsersPost == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    MyIterator<Post> myPostIterator = new MyIterator<>(allUsersPost.listIterator());
                    Post currentPost = myPostIterator.next();

                    postIteratorAndAction(currentPost, myPostIterator, true);
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
            }
        }
    }

    /**
     * This method performs iteration action and like comment action on the post
     *                 "\t1. Like the post\n" +
     *                 "\t2. Comment the post\n" +
     *                 "\t3. View Likes for the post\n" +
     *                 "\t4. View Comments for the post\n" +
     *                 "\t5. Delete this post\n" +
     *                 "\t6. Next Post\n" +
     *                 "\t7. Previous Post\n" +
     *                 "\t0. Back to previous menu");
     * @param currentPost -> current post in iterator
     * @param myPostIterator -> iterator for iterating
     * @param isMySelf -> checks true if the post belongs to me
     */
    private void postIteratorAndAction(Post currentPost, MyIterator<Post> myPostIterator, boolean isMySelf) {
        myPostIteratorLoop:
        while(true){
            System.out.println("Current Post: ");
            System.out.println(currentPost);
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            printPostIteratorMenu();
            int postIteratorChoice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0,7);
            switch (postIteratorChoice){
                case 0:
                {
                    Main.LOGGER.info("Back to previous ");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break myPostIteratorLoop;
                }
                case 1:
                {
                    boolean isLikeSuccessful = postController_.likeThePost(UsersController.getUsers_().getUserId_(), currentPost.getPostId());

                    if(isLikeSuccessful){
                        break;
                    }

                    System.out.println("You have already liked the post");
                    System.out.println("Choose below options\n" +
                            "\tpress 1 -> to unlike the post\n" +
                            "\tpress 0 -> Do not take any action");
                    int choiceOfAction = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0,1);
                    switch (choiceOfAction){
                        case 0:{
                            Main.LOGGER.info("Action not performed on the post");
                            Main.CUSTOM_USER_INPUT.printSeparatorLine();
                            break;
                        }
                        case 1:
                        {
                            boolean isUnlikeDone = postController_.removeLikeForPost(UsersController.getUsers_().getUserId_(),currentPost.getPostId());
                            if(isUnlikeDone){
                                Main.LOGGER.info("Unlike done");
                                Main.CUSTOM_USER_INPUT.printSeparatorLine();
                                break;
                            }
                            Main.LOGGER.warning("Something went wrong in unliking the post");
                            Main.CUSTOM_USER_INPUT.printSeparatorLine();
                            break;
                        }
                    }
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 2:
                {
                    System.out.println("Enter the comment for the post: (press 'q' to quit from comment drafting)");
                    StringBuilder commentText = new StringBuilder();
                    while(true){
                        String line = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                        if(line.equals("q")){
                            break;
                        }
                        commentText.append(line);
                    }

                    if(commentText.toString().isEmpty()){
                        Main.LOGGER.warning("Comments entered is empty");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    boolean isCommentedSuccessfully = postController_.commentThePost(UsersController.getUsers_().getUserId_(), currentPost.getPostId(), commentText.toString());
                    if(!isCommentedSuccessfully){
                        Main.LOGGER.warning("Something went wrong in commenting the post with post id: " + currentPost.getPostId());
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Added comment successfully");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 3:
                {
                    List<Like> likeList = postController_.getAllLikesForThePost(currentPost.getPostId());

                    if(likeList == null){
                        Main.LOGGER.warning("Something went wrong in fetching all the liked for the post");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Total number of likes for the post is: " + likeList.size());
                    System.out.println("Below are the peoples who liked the post: ");
                    likeList.forEach(e -> System.out.println("User id: "+e.getUserId() + " :-> liked the post at " + e.getLikeDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy") ) +" "+ e.getLikeTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 4:
                {
                    List<Comment> commentList = postController_.getAllCommentsForThePost(currentPost.getPostId());

                    if(commentList == null){
                        Main.LOGGER.warning("Something went wrong in fetching all the comments for the post");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Total number of comments for the post is: " + commentList.size());
                    System.out.println("Below are the peoples who commented the post: ");
                    commentList.forEach(e -> System.out.println("User id: "+e.getCommentUserId() + " :-> commented the post at " + e.getCommentDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy") ) +" "+ e.getCommentTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) +"\n Comment: " + e.getCommentText()));
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 5:
                {
                    if(!isMySelf){
                        Main.LOGGER.warning("You are not supposed to delete someone else's post");
                        break;
                    }

                    boolean isPostDeleted = postController_.removeThePost(currentPost.getPostId());
                    boolean isPostRemovedFromIterator = myPostIterator.remove();
                    if(isPostDeleted && isPostRemovedFromIterator){
                        Main.LOGGER.info("Removing post is successful");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.warning("Something went wrong in removing the post");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 6:
                {
                    try{
                        currentPost = myPostIterator.next();
                        Main.LOGGER.info("Moved to next post");
                    }catch(RuntimeException e){
                        Main.LOGGER.severe(e.toString());
                        Main.LOGGER.warning("Reached end of the post");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 7:
                {
                    try{
                        currentPost = myPostIterator.previous();
                        Main.LOGGER.info("Moved to previous post");
                    }catch(RuntimeException e){
                        Main.LOGGER.severe(e.toString());
                        Main.LOGGER.warning("Reached first of the post");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
            }
        }
    }


    public void printPostIteratorMenu(){
        System.out.println("Choose the below options\n" +
                "\t1. Like the post\n" +
                "\t2. Comment the post\n" +
                "\t3. View Likes for the post\n" +
                "\t4. View Comments for the post\n" +
                "\t5. Delete this post\n" +
                "\t6. Next Post\n" +
                "\t7. Previous Post\n" +
                "\t0. Back to previous menu");
    }

    public void printPostMainPageMenuForAdmin(){
        System.out.println("Choose the below options: \n" +
                "\t1. Create Post\n" +
                "\t2. View all My Post one by one\n" +
                "\t3. View all others Post one by one\n" +
                "\t0. Back to previous page");
    }
}
