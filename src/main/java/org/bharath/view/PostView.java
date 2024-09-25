/**
 * This is a view for post page to print the menu
 */
package org.bharath.view;

public class PostView {

    public void printPostMainPageMenu(){
        System.out.println("Choose the below options: \n" +
                "\t1. Create Post\n" +
                "\t2. View all My Post one by one\n" +
                "\t3. View all Friends Post one by one\n" +
                "\t0. Back to previous page");
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
