package com.save_backend.src.child;

import com.save_backend.config.exception.BaseException;
import com.save_backend.src.child.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.save_backend.config.response.BaseResponseStatus.*;

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

    public PatchChildEditRes modifyChild(int childIdx, PatchChildEditReq patchChildEditReq) throws BaseException {
        //아동 존재 확인
        if(childProvider.checkChild(childIdx) ==0){
            throw new BaseException(NOT_EXIST_CHILD);
        }
        try{
            PatchChildEditRes patchChildEditRes = childDao.modifyChild(childIdx, patchChildEditReq);
            return patchChildEditRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchChildDelRes deleteChild(int childIdx) throws BaseException {
        try{
            return childDao.deleteChild(childIdx);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
