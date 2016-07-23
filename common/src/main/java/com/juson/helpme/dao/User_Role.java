package com.juson.helpme.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;


/**
 * Created by cqx on 16/7/22.
 */
@Entity
public class User_Role {
    @Id

    private  ObjectId id;
//    private String id;


    private ObjectId userId;

    private ObjectId roleId;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }


    public ObjectId getRoleId() {
        return roleId;
    }

    public void setRoleId(ObjectId roleId) {
        this.roleId = roleId;
    }
}
