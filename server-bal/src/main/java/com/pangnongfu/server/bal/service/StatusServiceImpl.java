package com.pangnongfu.server.bal.service;

import com.pangnongfu.server.bal.api.StatusService;
import com.pangnongfu.server.bal.dto.*;
import com.pangnongfu.server.bal.exception.BizException;
import com.pangnongfu.server.bal.task.ImageHandleTask;
import com.pangnongfu.server.dal.api.*;
import com.pangnongfu.server.dal.po.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

/**
 * Created by hao on 2015/9/25.
 */
public class StatusServiceImpl implements StatusService {

    private Logger logger= LoggerFactory.getLogger(StatusServiceImpl.class);

    StatusDao statusDao;
    AccountDao accountDao;
    AccountDetailDao accountDetailDao;
    StatusCategoryDao statusCategoryDao;
    StatusCollectDao statusCollectDao;
    StatusCommentDao statusCommentDao;
    StatusLoveDao statusLoveDao;
    StatusImageDao statusImageDao;

    public StatusServiceImpl setStatusDao(StatusDao statusDao) {
        this.statusDao = statusDao;
        return this;
    }

    public StatusServiceImpl setAccountDao(AccountDao accountDao){
        this.accountDao = accountDao;
        return this;
    }

    public StatusServiceImpl setAccountDetailDao(AccountDetailDao accountDetailDao){
        this.accountDetailDao = accountDetailDao;
        return this;
    }

    public StatusServiceImpl setStatusCategoryDao(StatusCategoryDao statusCategoryDao){
        this.statusCategoryDao = statusCategoryDao;
        return this;
    }

    public StatusServiceImpl setStatusCollectDao(StatusCollectDao statusCollectDao){
        this.statusCollectDao = statusCollectDao;
        return this;
    }

    public StatusServiceImpl setStatusCommentDao(StatusCommentDao statusCommentDao){
        this.statusCommentDao = statusCommentDao;
        return this;
    }

    public StatusServiceImpl setStatusLoveDao(StatusLoveDao statusLoveDao){
        this.statusLoveDao = statusLoveDao;
        return this;
    }

    public StatusServiceImpl setStatusImageDao(StatusImageDao statusImageDao) {
        this.statusImageDao = statusImageDao;
        return this;
    }

    private TaskExecutor taskExecutor;

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

   
    /**
     * 将Status类型转换为StatusDTO类型
     * @param status
     * @return
     */
    private StatusDTO convertStatusToDTO(Status status)throws BizException{
        StatusDTO statusDTO = null;

        //如果参数不为空，将status属性整合到StatusDTO中
        if(status != null) {
            statusDTO = new StatusDTO();
            statusDTO.setStatusId(status.getId());

            //将status中的Account类型装换成AccountSimpleDTO类型传到statusDTO中
            statusDTO.setPublisherId(convertAccountToDTO(status.getStatusPublisher()));

            //将status中的StatusCategory类型装换成StatusCategoryDTO类型传到statusDTO中
            statusDTO.setCategory(convertCategoryToDTO(status.getStatusCategory()));
            statusDTO.setText(status.getText());
            statusDTO.setCommentNum(status.getComment_num());
            statusDTO.setLoveNum(status.getLove_num());
            statusDTO.setCreateAt(status.getCreated_at());
            statusDTO.setLongitude(status.getLongitude());
            statusDTO.setLatitude(status.getLatitude());
            statusDTO.setAddress(status.getAddress());

            //通过status查找StatusImage集合
            List<StatusImage> list = statusImageDao.findByStatus(status);
            if(list != null) {
                for (StatusImage statusImage : list) {
                    statusDTO.getImages().add(convertImageToDTO(statusImage));
                }
            }
            
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "status instance is null!");
        }

        return statusDTO;
    }

    public Status convertDTOToStatus(StatusDTO statusDTO)throws BizException{
    
        Status status = null;

        if(statusDTO != null){
            status = new Status();
            //status.setId(statusDTO.getStatusId());
            status.setCreated_at(new Date());
            status.setComment_num(statusDTO.getCommentNum());
            status.setLove_num(statusDTO.getLoveNum());
            status.setStatusCategory(convertDTOToCategory(statusDTO.getCategory()));
            status.setStatusPublisher(convertDTOToAccount(statusDTO.getPublisherId()));

            //状态字段为空时抛出异常
            if(statusDTO.getText() != null && !"".equals(statusDTO.getText())) {
                status.setText(statusDTO.getText());
            }else{
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "status content is null!");
            }

            if(statusDTO.getLongitude() == 0) {
                status.setLongitude(statusDTO.getLongitude());
            }else{
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "longitude is 0!");
            }

            if(statusDTO.getLatitude() == 0) {
                status.setLatitude(statusDTO.getLatitude());
            }else{
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "latitude is 0!");
            }

            if(statusDTO.getAddress() != null && !"".equals(statusDTO.getAddress())){
                status.setAddress(statusDTO.getAddress());
            }else{
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "address is null!");
            }


            //取出StatusDTO中的StatusImageDTO,将其存到status的ListStatusImage中
            Set<StatusImage> set = status.getListStatusImage();
            StatusImage statusImage = null;
            for(StatusImageDTO statusImageDTO : statusDTO.getImages()){
                statusImage = convertDTOToImage(statusImageDTO);
                
                set.add(statusImage);
            }
            status.setListStatusImage(set);

        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "statusDTO is null!");
        }


        return status;
    }

    /**
     * 将账户Account类型转换为AccountDTO类型
     * @param account
     * @return
     */
    private AccountSimpleDTO convertAccountToDTO(Account account)throws BizException{
        AccountDetail accountDetail = null;

        if(account != null) {

            //根据账户查找用户详细信息
            accountDetail = accountDetailDao.findByAccount(account);
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND,"account is null!");
        }

        AccountSimpleDTO accountSimpleDTO = null;
        if(accountDetail != null) {

            //新建详细信息DTO
            accountSimpleDTO = new AccountSimpleDTO();
            accountSimpleDTO.setUserId(account.getId());
            accountSimpleDTO.setNickname(accountDetail.getNickname());
            accountSimpleDTO.setAvatarUrl(accountDetail.getAvatar_url());
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "accountDetail instance not found!");
        }
        return accountSimpleDTO;
    }

    /**
     * 将AccountDTO类型转换到Account类型
     * @param accountSimpleDTO
     * @return
     */
    private Account convertDTOToAccount(AccountSimpleDTO accountSimpleDTO)throws BizException{
        Account account = null;

        if(accountSimpleDTO != null){

            //直接根据userId查找账户
            account = accountDao.findOne(accountSimpleDTO.getUserId());
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "accountSimpleDTO is null!");
        }

        return account;
    }

    /**
     * 将StatusCategory类型转换到StatusCategoryDTO类型
     * @param statusCategory
     * @return
     */
    private StatusCategoryDTO convertCategoryToDTO(StatusCategory statusCategory)throws BizException{
        StatusCategoryDTO statusCategoryDTO = null;
        if(statusCategory != null) {

            //新建StatusCategoryDTO对象，并给予赋值
            statusCategoryDTO = new StatusCategoryDTO();

            //状态类型ID小于0时抛出异常
            if(statusCategory.getId() < 1){
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "category ID format incorrect!");
            }
            statusCategoryDTO.setCategoryId(statusCategory.getId());

            //状态类型名字为空时抛出异常
            if(statusCategory.getTitle() == null || "".equals(statusCategory.getTitle())){
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "category title is null!");
            }
            statusCategoryDTO.setName(statusCategory.getTitle());

            //状态类型图片路径为空时抛出异常
            if(statusCategory.getImg_url() == null || "".equals(statusCategory.getImg_url())){
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "category img_url is null!");
            }
            statusCategoryDTO.setImageUrl(statusCategory.getImg_url());
            statusCategoryDTO.setStatusNum(statusCategory.getStatus_num());
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "category instance is null!");
        }
        return statusCategoryDTO;
    }

    /**
     * 将StatusCategoryDTO类型转换为StatusCategory类型
     * @param statusCategoryDTO
     * @return
     */
    private StatusCategory convertDTOToCategory(StatusCategoryDTO statusCategoryDTO)throws BizException{
        StatusCategory statusCategory = null;

        if(statusCategoryDTO != null){

            //根据categoryId直接查找StatusCategory对象
            statusCategory = statusCategoryDao.findOne(statusCategoryDTO.getCategoryId());
        }

        return statusCategory;
    }

    /**
     * 将StatusImage类型转换到StatusImageDTO类型
     * @param statusImage
     * @return
     */
    private StatusImageDTO convertImageToDTO(StatusImage statusImage)throws BizException{
        StatusImageDTO statusImageDTO = null;

        if(statusImage != null) {
            statusImageDTO = new StatusImageDTO();
            statusImageDTO.setUrl(statusImage.getUrl());

            //根据statusImage中的quality字段转换为字符型属性
            String quality = StatusImageDTO.low_quality;
            switch (statusImage.getQuality()) {
                case 0:
                    quality = StatusImageDTO.low_quality;
                    break;
                case 1:
                    quality = StatusImageDTO.normal_quality;
                    break;
                case 2:
                    quality = StatusImageDTO.high_quality;
                    break;
                default:
                    break;
            }

            statusImageDTO.setQuality(quality);
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "statusImage instance is null!");
        }
        return statusImageDTO;
    }

    /**
     * 将StatusImageDTO类型转换为StatusImage类型
     * @param statusImageDTO
     * @return
     */
    private StatusImage convertDTOToImage(StatusImageDTO statusImageDTO)throws BizException{
        StatusImage statusImage = new StatusImage();

        if(statusImageDTO != null){

            //当图片路径为空时，抛出异常
            if(statusImageDTO.getUrl() == null || "".equals(statusImageDTO.getUrl())){
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusImage url is null!");
            }
            statusImage.setUrl(statusImageDTO.getUrl());

            //根据字符型属性转换为quality字段
            int quality = 0;
            switch (statusImageDTO.getQuality()){
                case StatusImageDTO.low_quality:
                    quality = 0;
                    break;
                case StatusImageDTO.normal_quality:
                    quality = 1;
                    break;
                case StatusImageDTO.high_quality:
                    quality = 2;
                    break;
                default:
                    break;
            }
            statusImage.setQuality(quality);
        }

        return statusImage;
    }

    /**
     * 将StatusComment转换为CommentDTO类型
     * @param statusComment
     * @return
     */
    private CommentDTO convertCommentToDTO(StatusComment statusComment)throws BizException{
        CommentDTO commentDTO = null;

        if(statusComment != null){

            //新建评论DTO
            commentDTO = new CommentDTO();
            commentDTO.setCommentId(statusComment.getId());
            commentDTO.setContent(statusComment.getContent());
            commentDTO.setCreateAt(statusComment.getCreate_at());

            //将statusComment中的account字段转换为AccountSimpleDTO类型传给commentDTO
            commentDTO.setPublisher(convertAccountToDTO(statusComment.getCommentPublisher()));
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "statusComment is null!");
        }

        return commentDTO;
    }

    @Override
    public Pageable<StatusDTO> listPublicStatuses(int page, int count) throws BizException{

        //分页参数不正确时抛出异常
        if(page < 1 || count < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "paging parameter incorrect!");
        }

        //根据分页条件得到Page对象
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Page<Status> statusPage = statusDao.findAll(new PageRequest(page - 1, count, sort));

        Pageable<StatusDTO> pageable = new Pageable<>();

        //将得到的分页结果整合到Pageable对象中
        if(statusPage.getContent().size() != 0) {
            pageable.setAllCount((int) statusPage.getTotalElements());
            pageable.setMaxPage(statusPage.getTotalPages());
            pageable.setCurrentPage(page);

            List<StatusDTO> list = new LinkedList<>();
            for(Status status : statusPage.getContent()){
                list.add(convertStatusToDTO(status));
            }
            pageable.setContent(list);
        }else{
            pageable.setAllCount((int) statusPage.getTotalElements());
            pageable.setCurrentPage(page);
            pageable.setMaxPage(statusPage.getTotalPages());

            List<StatusDTO> list = new LinkedList<>();
            pageable.setContent(list);
        }

        logger.info("list public statuses Page:" + String.valueOf(page));
        for(StatusDTO statusDTO : pageable.getContent()){
            logger.info("list public statuses statusId:" + String.valueOf(statusDTO.getStatusId()));
        }

        return pageable;
    }

    @Override
    public Pageable<StatusDTO> listStatusesByUser(long userId, int page, int count) throws BizException{
        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //分页参数不正确时抛出异常
        if(page < 1 || count < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "paging parameter incorrect!");
        }

        Account account = new Account();
        account.setId(userId);

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Page<Status> statusPage = statusDao.findByStatusPublisher(account, new PageRequest(page - 1, count, sort));

        Pageable<StatusDTO> pageable = new Pageable<>();

        //将得到的分页结果整合到Pageable对象中
        pageable.setAllCount((int) statusPage.getTotalElements());
        pageable.setMaxPage(statusPage.getTotalPages());
        pageable.setCurrentPage(page);
        List<StatusDTO> list = new LinkedList<>();
        if(statusPage.getContent().size() != 0){
            for(Status status : statusPage.getContent()){
                list.add(convertStatusToDTO(status));
            }
            pageable.setContent(list);
        }else{
            pageable.setContent(list);
        }

        logger.info("list statuses by User:" + String.valueOf(userId) + " Page:" + String.valueOf(page));
        for(StatusDTO statusDTO : pageable.getContent()){
            logger.info("list statuses statusId:" + String.valueOf(statusDTO.getStatusId()));
        }
        return pageable;
    }

    @Override
    public Pageable<StatusDTO> listStatusesByCategory(long categoryId, int page, int count) throws BizException{

        //状态类型ID小于1时抛出异常
        if(categoryId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "categoryId format incorrect!");
        }

        //分页参数不正确时抛出异常
        if(page < 1 || count < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "paging parameter incorrect!");
        }

        StatusCategory statusCategory = new StatusCategory();
        statusCategory.setId(categoryId);

        //根据分页条件得到Page对象
        //通过类型查找状态
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Page<Status> statusPage = statusDao.findByStatusCategory(statusCategory, new PageRequest(page - 1, count, sort));

        Pageable<StatusDTO> pageable = new Pageable<>();

        //将得到的分页结果整合到Pageable对象中
        pageable.setAllCount((int) statusPage.getTotalElements());
        pageable.setMaxPage(statusPage.getTotalPages());
        pageable.setCurrentPage(page);
        List<StatusDTO> list = new LinkedList<>();
        if(statusPage.getContent().size() != 0){
            for(Status status : statusPage.getContent()){
                list.add(convertStatusToDTO(status));
            }
            pageable.setContent(list);
        }else{
            pageable.setContent(list);
        }

        logger.info("list statuses by Category:"+ String.valueOf(categoryId) +
            " Page:" + String.valueOf(page));
        for(StatusDTO statusDTO : pageable.getContent()){
            logger.info("list statuses statusId:" + String.valueOf(statusDTO.getStatusId()));
        }
        return pageable;
    }

    @Override
    public Pageable<StatusDTO> listCollectedStatuses(long userId, int page, int count) throws BizException{
        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //分页参数不正确时抛出异常
        if(page < 1 || count < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "paging parameter incorrect!");
        }

        Account account = new Account();
        account.setId(userId);

        //根据分页条件得到Page对象
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Page<StatusCollect> statusPage = statusCollectDao.findByAccountCollect(account, new PageRequest(page - 1, count, sort));

        Pageable<StatusDTO> pageable = new Pageable<>();

        //将得到的分页结果整合到Pageable对象中
        pageable.setAllCount((int) statusPage.getTotalElements());
        pageable.setMaxPage(statusPage.getTotalPages());
        pageable.setCurrentPage(page);
        List<StatusDTO> list = new LinkedList<>();
        if(statusPage.getContent().size() != 0){
            for(StatusCollect statusCollect : statusPage.getContent()){
                list.add(convertStatusToDTO(statusCollect.getStatus()));
            }
            pageable.setContent(list);
        }else{
            pageable.setContent(list);
        }

        logger.info("list collected statuses by User:" + String.valueOf(userId) +
                " Page:" + String.valueOf(page));
        for(StatusDTO statusDTO : pageable.getContent()){
            logger.info("list statuses statusId:" + String.valueOf(statusDTO.getStatusId()));
        }
        return pageable;
    }

    @Override
    public void addStatus(long userId, long categoryId,String text,
                          double longitude, double latitude, String address,
                          final List<StatusImageDTO> images) throws BizException {
        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //状态类型ID小于1时抛出异常
        if(categoryId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "categoryId format incorrect!");
        }

        //状态内容为空时抛出异常
        if(text == null || "".equals(text)){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "status content is null!");
        }

        //经纬度格式不正确时抛出异常
        if(longitude < 0 || latitude < 0){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "longitude or latitude format incorrect!");
        }

        //状态地址为空时抛出异常
        if(address == null || "".equals(address)){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "address is null!");
        }

        Status status=new Status();

        Account account=new Account();
        account.setId(userId);
        status.setStatusPublisher(account);

        StatusCategory category=new StatusCategory();
        category.setId(categoryId);
        status.setStatusCategory(category);

        status.setCreated_at(new Date());
        status.setText(text);
        status.setLongitude(longitude);
        status.setLatitude(latitude);
        status.setAddress(address);

        category = statusCategoryDao.findOne(categoryId);
        category.setStatus_num(category.getStatus_num() + 1);

        statusDao.save(status);
        statusCategoryDao.save(category);

        if(status.getId()<1){
            throw new BizException(BizException.ERROR_CODE_STATUS_ADD_ERROR,"save status info error");
        }else{
            logger.info("save status info success "+String.valueOf(status.getId()));
        }

        final long statusId=status.getId();

        //处理图片
        for(final StatusImageDTO img:images){
            final StatusImage imgPo=convertDTOToImage(img);
            imgPo.setStatus(status);

            String[] urlParts=img.getUrl().split("/");
            String fileName=urlParts[urlParts.length-1];
            String baseKey=fileName.substring(0,fileName.lastIndexOf("."));

            if(img.getQuality().equals(StatusImageDTO.high_quality)){
                //生成high的图
                taskExecutor.execute(new ImageHandleTask(
                        img.getUrl(),
                        baseKey+"_high.jpg",
                        800,
                        "scale"
                ));

                //生成normal的图
                taskExecutor.execute(new ImageHandleTask(
                        img.getUrl(),
                        baseKey+"_normal.jpg",
                        500,
                        "scale"
                ));
            }

            if(images.size()==1){
                //按比例生成缩略图
                taskExecutor.execute(new ImageHandleTask(
                        img.getUrl(),
                        baseKey+"_low.jpg",
                        150,
                        "scale"
                ).setCallBack(new ImageHandleTask.HandleCallBack() {
                    @Override
                    public void onSuccess() {
                        statusImageDao.save(imgPo);
                    }
                }));
            }else{
                //生成方形缩略图
                taskExecutor.execute(new ImageHandleTask(
                        img.getUrl(),
                        baseKey+"_low.jpg",
                        150,
                        "cut"
                ).setCallBack(new ImageHandleTask.HandleCallBack() {
                    @Override
                    public void onSuccess() {
                        statusImageDao.save(imgPo);
                    }
                }));
            }
        }
    }

    @Override
    public void deleteStatus(long statusId) throws BizException {

        //状态ID为空时抛出异常
        if(statusId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusId format incorrect!");
        }

        Status status = statusDao.findOne(statusId);
        StatusCategory statusCategory = status.getStatusCategory();
        if(status != null){
            statusDao.delete(statusId);

            //更改状态类型中的评论数
            statusCategory.setStatus_num(statusCategory.getStatus_num() - 1);
            statusCategoryDao.save(statusCategory);

            logger.info("delete status success statusId:" + String.valueOf(statusId));
        }else{
            throw new BizException(BizException.ERROR_CODE_STATUS_NOT_FOUND,"status instance not found!");
        }
    }

    @Override
    public void addComment(long userId, long statusId, String content) throws BizException{

        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //状态ID为空时抛出异常
        if(statusId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusId format incorrect!");
        }

        //评论内容为空时抛出异常
        if(content == null || "".equals(content)){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "status content is null!");
        }

        Account account = new Account();
        account.setId(userId);
        Status status = statusDao.findOne(statusId);

        if(account != null && status != null) {

            //新建评论
            StatusComment statusComment = new StatusComment();
            statusComment.setCommentPublisher(account);
            statusComment.setContent(content);
            statusComment.setCreate_at(new Date());
            statusComment.setStatus(status);

            statusCommentDao.save(statusComment);

            //更改状态中评论数，评论数+1
            status.setComment_num(status.getComment_num() + 1);
            statusDao.save(status);

            logger.info("add comment success commentId:" + String.valueOf(statusComment.getId()));
        }
    }

    @Override
    public List<CommentDTO> listCommentByStatus(long statusId) throws BizException{

        //状态ID为空时抛出异常
        if(statusId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusId format incorrect!");
        }

        Status status = new Status();
        status.setId(statusId);

        List<CommentDTO> list = new LinkedList<>();

        //根据状态得到评论集合
        List<StatusComment> l = statusCommentDao.findByStatus(status);

        if(l.size() != 0) {
            CommentDTO commentDTO = null;
            for (StatusComment statusComment : l){

                //StatusComment转换类型
                commentDTO = convertCommentToDTO(statusComment);
                list.add(commentDTO);
            }
        }

        logger.info("list comments of status:" + String.valueOf(statusId));

        return list;
    }

    @Override
    public void likeStatus(long userId, long statusId) throws BizException{

        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //状态ID为空时抛出异常
        if(statusId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusId format incorrect!");
        }

        Account account = new Account();
        account.setId(userId);
        Status status = statusDao.findOne(statusId);

        if(account != null && status != null) {
            StatusLove statusLove = new StatusLove();
            statusLove.setAccountLove(account);
            statusLove.setStatus(status);

            statusLoveDao.save(statusLove);

            //更改状态的点赞数，点赞数+1
            status.setLove_num(status.getLove_num() + 1);
            statusDao.save(status);

            logger.info("like status success statusId:" + String.valueOf(statusId) + " by User:" + String.valueOf(userId));
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "account or status instance not found!");

        }
        
    }

    @Override
    public void unlikeStatus(long userId, long statusId) throws BizException{

        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //状态ID为空时抛出异常
        if(statusId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusId format incorrect!");
        }

        Account account = new Account();
        account.setId(userId);

        Status status = statusDao.findOne(statusId);

        if(account != null && status != null){
            StatusLove statusLove = statusLoveDao.findByStatusAndAccountLove(status,account);
            if(statusLove != null){
                statusLoveDao.delete(statusLove);

                //更改状态的点赞数，点赞数-1
                status.setLove_num(status.getLove_num() - 1);
                statusDao.save(status);

                logger.info("cancel like status success statusId:" + String.valueOf(statusId) + " by User:" + String.valueOf(userId));
            }
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "account or status instance not found!");
        }
    }

    @Override
    public boolean isLikeStatus(long userId, long statusId) throws BizException{

        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //状态ID为空时抛出异常
        if(statusId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusId format incorrect!");
        }

        Account account = new Account();
        account.setId(userId);

        Status status = new Status();
        status.setId(statusId);

        boolean flag = false;

        StatusLove statusLove = statusLoveDao.findByStatusAndAccountLove(status, account);
        if(statusLove != null){
            flag = true;
        }

        logger.info(String.valueOf(userId) + "like status:" + String.valueOf(statusId) + "? "+flag);

        return flag;
    }

    @Override
    public List<StatusCategoryDTO> listAllCategory() throws BizException{
        List<StatusCategory> list = statusCategoryDao.findAll();
        List<StatusCategoryDTO> dtoList = new LinkedList<>();

        if(list != null && list.size() != 0){
            for(StatusCategory statusCategory : list){
                //状态类型转换
                dtoList.add(convertCategoryToDTO(statusCategory));
            }
        }

        logger.info("list all category success");
        return dtoList;
    }

    @Override
    public void addCollection(long statusId, long userId) throws BizException{

        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //状态ID为空时抛出异常
        if(statusId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusId format incorrect!");
        }

        Account account = new Account();
        account.setId(userId);

        Status status = new Status();
        status.setId(statusId);

        if(account != null && status != null){

            //新建状态收藏对象
            StatusCollect statusCollect = new StatusCollect();
            statusCollect.setStatus(status);
            statusCollect.setAccountCollect(account);

            //对象持久化到数据库
            statusCollectDao.save(statusCollect);

            logger.info("collect status success statusId:" + String.valueOf(statusId) + " by User:" + String.valueOf(userId));
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "account or status instance not found!");
        }
    }

    @Override
    public void removeCollection(long statusId, long userId) throws BizException{

        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //状态ID为空时抛出异常
        if(statusId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusId format incorrect!");
        }

        Account account = new Account();
        account.setId(userId);

        Status status = new Status();
        status.setId(statusId);

        if(account != null && status != null) {
            StatusCollect statusCollect = statusCollectDao.findByStatusAndAccountCollect(status, account);
            if (statusCollect != null) {
                statusCollectDao.delete(statusCollect);
                logger.info("cancel collect status:" + String.valueOf(statusId) + " by User:" + String.valueOf(userId));
            }
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "account or status instance not found!");
        }
    }

    @Override
    public boolean isCollected(long statusId, long userId) throws BizException{

        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //状态ID为空时抛出异常
        if(statusId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusId format incorrect!");
        }

        boolean flag = false;

        Account account = new Account();
        account.setId(userId);

        Status status = new Status();
        status.setId(statusId);

        StatusCollect statusCollect = statusCollectDao.findByStatusAndAccountCollect(status, account);
                
        if(statusCollect != null){
            flag = true;
        }

        logger.info(String.valueOf(userId) + "collect status:" + String.valueOf(statusId) + "? "+flag);
        return flag;
    }

    @Override
    public StatusDTO getStatusById(long statusId) throws BizException{

        //状态ID为空时抛出异常
        if(statusId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "statusId format incorrect!");
        }

        //throw new NotImplementedException();
        Status status = statusDao.findOne(statusId);
        StatusDTO statusDTO = null;

        if(status != null){

            //将Status类型转换
            statusDTO = convertStatusToDTO(status);
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "status instance not found!");
        }

        logger.info("get Status success by id:"+ String.valueOf(statusId));
        return statusDTO;
    }
}
