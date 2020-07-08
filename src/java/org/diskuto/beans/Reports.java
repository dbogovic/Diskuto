/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.diskuto.helpers.AppHelper;
import org.diskuto.helpers.Retriever;
import org.diskuto.helpers.XmlHelper;
import org.diskuto.models.Comment;
import org.diskuto.models.Forum;
import org.diskuto.models.Post;
import org.xmldb.api.base.ResourceIterator;

/**
 *
 * @author dario
 */
@Named(value = "reports")
@ViewScoped
public class Reports implements Serializable {

    private org.diskuto.models.Forum diskuto;
    private List<org.diskuto.models.Post> items = new ArrayList();
    private List<Comment> comments = new ArrayList();

    /**
     * Creates a new instance of Reports
     */
    public Reports() throws Exception {

        Retriever retrieveForum = new Retriever(AppHelper.param("name"));
        this.diskuto = retrieveForum.forum();
        if (AppHelper.getActiveUser() == null || this.diskuto == null || (!this.diskuto.getOwner().equals(AppHelper.getActiveUser().getUsername())
                && !this.diskuto.getModerators().contains(AppHelper.getActiveUser().getUsername()))) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("notFound");
        } else {
            ResourceIterator iterator = AppHelper.getResourceSet("/posts/post[diskuto=\"" + this.diskuto.getName() + "\" and reported=\"1\"]/id").getIterator();
            while (iterator.hasMoreResources()) {
                for (String id : new XmlHelper(iterator.nextResource()).makeListValue("/id")) {
                    Retriever retrievePost = new Retriever(id);
                    org.diskuto.models.Post post = retrievePost.post();
                    if (!post.isDeleted()) {
                        items.add(post);
                    }
                }
            }

            iterator = AppHelper.getResourceSet("/posts/post[diskuto=\"" + this.diskuto.getName() + "\"]/comments/comment[reported=\"1\"]").getIterator();
            while (iterator.hasMoreResources()) {
                Comment comment = new Comment();
                comment.retrieve(new XmlHelper(iterator.nextResource()));
                if (!comment.isDeleted()) {
                    Retriever retriever = new Retriever(new XmlHelper(AppHelper.getResource("data(/posts/post[diskuto=\""
                            + this.diskuto.getName() + "\"][comments/comment/id=\""
                            + comment.getId() + "\" and comments/comment/owner=\""
                            + comment.getOwner() + "\" and comments/comment/created=\""
                            + comment.getCreated() + "\"]/id)")).rawValue());
                    comment.setPost(retriever.post());
                    this.comments.add(comment);
                }
            }
        }
    }

    public void deletePost(org.diskuto.models.Post post) throws Exception {
        post.delete();
        this.items.remove(post);
    }

    public void deleteComment(Comment comment) throws Exception {
        comment.delete();
        this.comments.remove(comment);
    }

    public void okPost(org.diskuto.models.Post post) throws Exception {
        post.itsOk();
        this.items.remove(post);
    }

    public void okComment(Comment comment) throws Exception {
        comment.itsOk();
        this.comments.remove(comment);
    }

    public Forum getDiskuto() {
        return diskuto;
    }

    public void setDiskuto(Forum diskuto) {
        this.diskuto = diskuto;
    }

    public List<Post> getItems() {
        return items;
    }

    public void setItems(List<Post> items) {
        this.items = items;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
