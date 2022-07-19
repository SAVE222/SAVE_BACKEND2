package com.save_backend.src.child;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.child.model.PatchChildRes;
import com.save_backend.src.child.model.PostChildReq;
import com.save_backend.src.child.model.PostChildRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.save_backend.config.response.BaseResponseStatus;

import static com.save_backend.config.response.BaseResponseStatus.DATABASE_ERROR;
import static com.save_backend.config.response.BaseResponseStatus.NOT_EXIST_USER;

@Service
public class ChildService {
    private final ChildDao childDao;
    private final ChildProvider childProvider;

    @Autowired
    public ChildService(ChildDao childDao, ChildProvider childProvider) {
        this.childDao = childDao;
        this.childProvider = childProvider;
    }

    public PostChildRes createChild(PostChildReq postChildReq) throws BaseException {

        if(childProvider.checkUser(postChildReq.getUserIdx())==0){
            throw new BaseException(NOT_EXIST_USER);
        }

        try{
            int childIdx = childDao.insertChild(postChildReq);
            return new PostChildRes(childIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchChildRes deleteChild(int childIdx) throws BaseException {
        try{
            return childDao.deleteChild(childIdx);
        }catch(Exception exception){
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
