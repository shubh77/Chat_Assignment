package com.reino.assignment.callback;

import com.reino.assignment.model.UserModel;

public interface EditDeleteClickListener {
    /**
     * To get the Edit, Delete Click listener.
     * @param isDelete, true-> Delete, false-> Edit
     * @param model
     */
    public void click(boolean isDelete, UserModel model);
}
