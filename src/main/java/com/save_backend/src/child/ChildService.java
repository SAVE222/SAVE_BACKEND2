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


    /**
     * 1. 아동 정보 생성
     */
    public PostChildRes createChild(PostChildReq postChildReq) throws BaseException {
        //존재하는 유저(active)에 대한 아동 생성인지 확인
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


    /**
     * 3. 아동 정보 수정
     */
    public PatchChildEditRes modifyChild(int childIdx, PatchChildEditReq patchChildEditReq) throws BaseException {
        //존재하는 아동(active)에 대한 modify인지 확인
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


    /**
     * 4. 아동 삭제 (active -> inactive)
     */
    public PatchChildDelRes deleteChild(int childIdx) throws BaseException {
        //존재하는 아동(active)에 대한 delete인지 확인
        if(childProvider.checkChild(childIdx)==0){
            throw new BaseException(NOT_EXIST_CHILD);
        }
        try{
            return childDao.deleteChild(childIdx);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
