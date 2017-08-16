package com.zhiwang123.mobile.phone.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.zhiwang123.mobile.phone.activity.LoginActivity;
import com.zhiwang123.mobile.phone.bean.LoginResult;

/**
 * Created by ty on 2016/10/27.
 */

public class StaticConfigs {

    private static final String TAG = StaticConfigs.class.getName();

    public static final String WXREQ_STATE = "wx5e634s8g19n";
    public static final String WXLOGIN_SCOPE = "snsapi_userinfo";
    public static final String QQLOGIN_SCOPE = "all";//"get_user_info";

    public static final int LOGIN_MODE_WX = 0;
    public static final int LOGIN_MODE_QQ = 1;
    public static final int LOGIN_MODE_PB = 2;
    public static final int LOGIN_MODE_E = 3;

    public static final int USER_ROLE_PB = 0;
    public static final int USER_ROLE_E  = 1;

    public static final String RESULT_CODE_LOGIN_KEY = "login_result_key";
    public static final int REQUEST_CODE_LOGIN = 0x1;
    public static final int RESULT_CODE_LOGIN_OK = 0x11;

//    public static final String FONT_QD = "Hiragino_Sans_GB_W3.otf";
//    public static final String FONT_QD_BLOD = "Hiragino_Sans_GB_W6.otf";

    public static final int ISTEST = 0;
    public static final int ISEXAM = 1;
    public static final int ISNULL = 255;

    public static String URL_BASE = "";

    public static final int T_1_398 = 398;
    public static final int T_2_414 = 414;
    public static final int T_3_423 = 423;
    public static final int T_4_620 = 620;
    public static final int T_5_356 = 356;
    public static final int T_6_367 = 367;
    public static final int T_7_446 = 446;
    public static final int T_8_382 = 382;
    public static final int T_9_491 = 491;
    public static final int T_10_505 = 505;
    public static final int T_11_504 = 504;
    public static final int T_12_465 = 465;
    public static final int T_13_490 = 490;
    public static final int T_14_604 = 604;

    public static final int EXAM_E_STATE_LE = 0;
    public static final int EXAM_E_STATE_IN = 1;
    public static final int EXAM_E_STATE_RI = 2;

    public static final String PAY_TYPE_AL = "MobileAliPay";
    public static final String PAY_TYPE_WX = "WeiXinPay";
    public static final String PAY_TYPE_ME = "Account";

    public static final String SP_HISTORY_XML_FILENAME = "sp_history_xml_filename";

    public static final String SP_HISTORY_XML_KEY = "sp_history_xml_key";

    public static final String KEY_EXAM_PAPER = "key_exam_paper";

    private static final String EXCEPTION_STR = "token can't be null or empty str!";

    public static final String FAVORITE_CHANGE_ACITON = "com.zhiwang123.favorite.change.action";

    public static final String CART_CHANGE_ACITON = "com.zhiwang123.cart.change.action";

    public static LoginResult mLoginResult;

    public static String getAppVersionName(Context context) {

        String versionName = "";

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return versionName;
    }



    public static void setUrlBase(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            URL_BASE = appInfo.metaData.getString("base_url");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String DEF_IMG_URL_POSTFIX = "?width=360&height=270&bgcolor=255,255,255,255";

    public static final boolean loginCheck(Context context) {

        boolean bo = (mLoginResult == null ? false : true) && (mLoginResult.accessToken == null ? false : true);

        if(!bo) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        return bo;

    }


    /**
     *                      获取焦点图信息列表 GET
     *
     Count	int	获取焦点图数，默认5
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     */

    public static final String URL_FOCUS = "CMSPages/HotPictureNews?Size=<Size>&Count=<Count>";

    public static String getUrlFocus(String size, int count) {
        if(TextUtils.isEmpty(size)) size = "400*300";
        String url = URL_BASE + URL_FOCUS.replace("<Size>", size).replace("<Count>", count + "");
        return url;
    }

    public static String getUrlFocus(int count) {
        return getUrlFocus(null, 5);
    }


    /**
     *                      获取最新课程列表 GET
     *
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     PageNum	int	返回第几页，可选，默认值为1
     PageSize	int	每页包含几条数据，可选，默认值为10
     */
    public static final String URL_COURSE_NEWEST = "Course/Newest?Size=<Size>&PageSize=<PageSize>&PageNum=<PageNum>";

    public static String getUrlCourseNewest(int pageSize, int pageNum) {
        return getUrlCourseNewest(null, pageSize, pageNum);
    }

    public static String getUrlCourseNewest(String imgSize, int pageSize, int pageNum) {
        if(TextUtils.isEmpty(imgSize)) imgSize = "360*270";
        if(pageSize <= 0) pageSize = 1;
        if(pageNum <= 0) pageNum = 1;
        String url = URL_BASE + URL_COURSE_NEWEST.replace("<Size>", imgSize).replace("<PageSize>", pageSize + "").replace("<PageNum>", pageNum + "");
        return url;
    }


    /**
     *                      获取课程(二、三级)列表 也用于搜索 GET
     *
     CategoryPkId	int	分类Id
     Title	string	课程标题，用于搜索课程
     Speeker	string	讲师姓名
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     PageNum	int	返回第几页，可选，默认值为1
     PageSize	int	每页包含几条数据，可选，默认值为10
     */
    public static final String URL_COURSE_LIST = "Course/List?CategoryPkId=<PkId>&Title=<Title>&Speeker=<Speeker>&Size=<Size>&PageSize=<PageSize>&PageNum=<PageNum>";

    public static String getUrlCourseList(int categoryPkId, String size, int pageNum, int pageSize) {
        return getUrlCourseList(categoryPkId, "", "", size, pageNum, pageSize);
    }

    public static String getUrlCourseList(int categoryPkId, String title, String speeker, String size, int pageNum, int pageSize) {

        if(TextUtils.isEmpty(title)) title = "";
        if(TextUtils.isEmpty(speeker)) speeker = "";
        if(TextUtils.isEmpty(size)) size = "360*270";

        String url = URL_BASE + URL_COURSE_LIST.replace("<PkId>", categoryPkId + "").replace("<Title>", title).replace("<Speeker>", speeker)
                .replace("<Size>", size).replace("<PageNum>", pageNum + "").replace("<PageSize>", pageSize + "");

        return url;
    }

    /**
     *                      获取一级课程分类列表 GET
     *
     */
    public static final String URL_COURSE_ROOT = "CourseCategory/Root";

    public static String getCourseRoot() {

        return URL_BASE + URL_COURSE_ROOT;
    }

    /**
     *                      获取课程详情 GET
     *
     CourseId	Guid 课程Id
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     */
    public static final String URL_COURSE_INTR = "Course/Info?CourseId=<CourseId>&Size=<Size>&TeacherSize=<TeacherSize>";

    public static String getCourseIntroduce(String courseId, String size, String teacherSize) {

        if(TextUtils.isEmpty(size)) size = "720*540";
        if(TextUtils.isEmpty(teacherSize)) teacherSize = "200*200";

        String url = URL_BASE + URL_COURSE_INTR.replace("<CourseId>", courseId).replace("<Size>", size)
                .replace("<TeacherSize>", teacherSize);

        Log.d(TAG, url);

        return url;
    }

    public static String getUrlCourseIntr(String courseId) {
        return getCourseIntroduce(courseId, null, null);
    }

    /**
     *                  获取机构列表 也用于查询机构 GET
     *
     Name   string 机构名称
     *
     */
    public static final String URL_ORGAN = "Organ/List?Name=<Name>";

    public static String getOrgans(String name) {

        if(TextUtils.isEmpty(name)) name = "";

        String url = URL_BASE + URL_ORGAN.replace("<Name>", name);

        return url;
    }

    /**
     *                  用户登录 POST
     *
     User/Login ,{UserName:"xxx",Password:"xxx"});
     *
     */
    public static final String URL_USERLOGIN = "User/Login";

    public static String getUrlLogin() {

        String url = URL_BASE + URL_USERLOGIN;

        return url;
    }


    /**
     *
     *                  机构用户登录 POST
     Organ/Login", {Key:"demo",UserName:"wo123",Password:"123456"}
     *
     */
    public static final  String URL_E_LOGIN = "Organ/Login";

    public static String getUrlELogin() {

        String url = URL_BASE + URL_E_LOGIN;

        return url;
    }

    public static final String URL_E_LOIGN_BIND = "Organ/ThirdBindLogin";

    public static String getUrlEloginBind() {

        String url = URL_BASE + URL_E_LOIGN_BIND;

        return url;

    }



    /**
     *
                        获取用户信息 GET
     *
     AccessToken	string	用户的识别令牌
     *
     */
    public static final String URL_USERINFO = "User/Info?AccessToken=<AccessToken>";

    public static String getUrlUseInfo(String token) {

        if(TextUtils.isEmpty(token)) token = "123";

        String url = URL_BASE + URL_USERINFO.replace("<AccessToken>", token);

        return url;
    }


    /**
     *                 获取购物车列表 GET
     *
     AccessToken	string	用户身份令牌
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     *
     */
    public static final String URL_CARTLIST = "ShoppingCart/List?AccessToken=<AccessToken>&Size=<Size>";

    public static String getUrlCartlist(String token) {
        return getUrlCartlist(token, "360*270");
    }

    public static  String getUrlCartlist(String token, String size) {

        if(TextUtils.isEmpty(token)) throw new RuntimeException(EXCEPTION_STR);
        if(TextUtils.isEmpty(size)) size = "360*270";

        String url = URL_BASE + URL_CARTLIST.replace("<AccessToken>", token).replace("<Size>", size);

        return url;
    }

    /**
     *
     *              获取购物车数量 GET
     *
     AccessToken	string	用户身份令牌
     *
     */
    public static final String URL_CARTNUM = "ShoppingCart/Count?AccessToken=<AccessToken>";

    public static String getUrlCartNum(String token) {

        if(TextUtils.isEmpty(token)) throw new RuntimeException(EXCEPTION_STR);

        String url = URL_BASE + URL_CARTNUM.replace("<AccessToken>", token);

        return url;

    }

    /**
     *
     *              添加至购物车 POST
     *
     AccessToken	string	用户身份令牌
     CourseId	Guid	课程Id
     CourseType	int	课程类型
     *
     *
     */
    public static final String URL_CART_ADD = "ShoppingCart/Add";

    public static String getUrlCartAdd() {

        String url = URL_BASE + URL_CART_ADD;

        return url;

    }


    /**
     *
     *              移除购物车  POST
     *
     AccessToken	string	用户身份令牌
     Ids	string	购物车Id，多个Id用半角逗号间隔
     *
     *
     */
    public static final String URL_CART_REMOVE = "ShoppingCart/Removes";

    public static final String getUrlCartRemove() {

        String url = URL_BASE + URL_CART_REMOVE;

        return url;

    }




    /**
     *               获取用户订单列表 GET
     *
     *
     AccessToken	string	用户身份令牌
     Status	int	订单状态，默认-1:全部状态，0:未付款，1:已付款
     PageNum	int	返回第几页，可选，默认值为1
     PageSize	int	每页包含几条数据，可选，默认值为10
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     *
     */

    public static enum OrderStatus {

        STATUS_ALL(-1),
        STATUS_NOP(0),
        STATUS_PAY(1),
        STATUS_CLO(2);

        private int intValue = -1;

        private OrderStatus(int intv) {
            intValue = intv;
        }

        public int getIntValue() {
            return intValue;
        }

    }

    public static final String URL_ORDERLIST = "Order/List?AccessToken=<AccessToken>&Status=<Status>&Size=<Size>&PageNum=<PageNum>&PageSize=<PageSize>";

    public static String getUrlOrderList(String token, OrderStatus status, int pageNum, int pageSize) {
        return getUrlOrderList(token, status, pageNum, pageSize, null);
    }

    public static String getUrlOrderList(String token, OrderStatus status, int pageNum, int pageSize, String picSize) {

        if(TextUtils.isEmpty(token)) throw new RuntimeException(EXCEPTION_STR);

        if(status == null) status = OrderStatus.STATUS_ALL;
        if(TextUtils.isEmpty(picSize)) picSize = "360*270";
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 10;

        String url = URL_BASE + URL_ORDERLIST.replace("<AccessToken>", token)
                .replace("<Status>", status.intValue + "")
                .replace("<Size>", picSize)
                .replace("<PageNum>", pageNum + "")
                .replace("<PageSize>", pageSize + "");

        return url;
    }

    /**
     *
     *          取消订单
     *
     AccessToken	string	用户身份令牌
     Id	Guid	订单Id
     *
     */
    public static final String URL_CANCEL_ORDRE = "Order/Cancel";

    public static String getUrlCancelOrdre() {

        String url = URL_BASE + URL_CANCEL_ORDRE;

        return url;

    }



    /**
     *
     *              获取课程热门搜索关键字
     *
     */

    public static final String URL_HOTKEYS = "Course/HotSearchKeywords";

    public static String getUrlHotkeys() {

        String url = URL_BASE + URL_HOTKEYS;
        return url;
    }

    /**
     *
     *              获取我的课程列表
     *
     AccessToken	string	用户身份令牌
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     PageNum	int	返回第几页，可选，默认值为1
     PageSize	int	每页包含几条数据，可选，默认值为10
     *
     */
    public static final String URL_MYCOURSE = "Course/MyCourses?AccessToken=<AccessToken>&Size=<Size>&PageSize=<PageSize>&PageNum=<PageNum>";

    public static String getUrlCenterCoursePublic(String token, String picSize, int pageNum, int pageSize) {

        if(token == null) throw new RuntimeException(EXCEPTION_STR);
        if(TextUtils.isEmpty(picSize)) picSize = "360*270";
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 10;

        String url = URL_BASE + URL_MYCOURSE
                .replace("<AccessToken>", token)
                .replace("<Size>", picSize)
                .replace("<PageSize>", pageSize + "")
                .replace("<PageNum>", pageNum + "");

        Log.i(TAG, "request url : " + url);

        return url;
    }

    public static String getUrlCenterCoursePublic(String token, int pageSize, int pageNum) {

        return getUrlCenterCoursePublic(token, null, pageSize, pageNum);
    }


    /**
     *              公开平台获取具体试卷 GET
     *
     * AccessToken	string	用户身份令牌
     * BuyCourseId	Guid	Course/MyCourses的BuyCourseId属性
     *
     */

    public static final String URL_EXAM_PUBLIC = "Exam/GetTestPaper?AccessToken=<AccessToken>&BuyCourseId=<BuyCourseId>";

    public static final String getUrlExamPublic(String token, String buyCourseId) {

        if(TextUtils.isEmpty(token) || TextUtils.isEmpty(buyCourseId)) throw new RuntimeException(EXCEPTION_STR);

        String url = URL_BASE + URL_EXAM_PUBLIC
                .replace("<AccessToken>", token)
                .replace("<BuyCourseId>", buyCourseId);

        return url;
    }

    /**
     *              获取E学院考试试卷 GET
     *
     * AccessToken	string	用户身份令牌
     * HaveCourseId	Guid	从OrganExam/List取得
     *
     */

    public static final String URL_EXAM_E = "OrganExam/GetTestPaper?AccessToken=<AccessToken>&HaveCourseId=<HaveCourseId>";

    public static final String getUrlExamE(String token, String haveCourseId) {

        if(TextUtils.isEmpty(token) || TextUtils.isEmpty(haveCourseId)) throw new RuntimeException(EXCEPTION_STR);

        String url = URL_BASE + URL_EXAM_E
                .replace("<AccessToken>", token)
                .replace("<HaveCourseId>", haveCourseId);

        return url;
    }


    /**
     *              提交试题答案  POST
     *
     AccessToken	string	用户身份令牌
     TestPaperId	Guid	试卷Id
     QuestionId	Guid	试题Id
     Answer	string	用户答案
     *
     */
    public static final String URL_COMMIT_ANSWERS = "Exam/SubmitQuestionAnswer";

    public static String getUrlCommitAnswers() {

        return URL_BASE + URL_COMMIT_ANSWERS;
    }

    /**
     *              e学院用户提交试题答案 POST
     *
     AccessToken	string	用户身份令牌
     TestPaperId	Guid	试卷Id
     QuestionId	Guid	试题Id
     Answer	string	用户答案
     */
    public static final String URL_COMMENT_E_ANSWERS = "OrganExam/SubmitQuestionAnswer ";
    public static String getUrlCommitAnswersE() {

        return URL_BASE + URL_COMMENT_E_ANSWERS;

    }

    /**
     *              查看试卷答案 公共 GET
     *
     AccessToken	string	用户身份令牌
     TestPaperId	Guid	试卷Id
     BuyCourseId	Guid	Course/MyCourses的BuyCourseId属性
     */
    public static final String URL_CHECK_RESULT_ANSWERS = "Exam/Result?AccessToken=<AccessToken>&TestPaperId=<TestPaperId>&BuyCourseId=<BuyCourseId>";
    public static String getUrlCheckExamResult(String token, String testPaperId, String buyCourseId) {

        if(TextUtils.isEmpty(token)) throw new RuntimeException(EXCEPTION_STR);
        if(TextUtils.isEmpty(testPaperId)) testPaperId = "";
        if(TextUtils.isEmpty(buyCourseId)) buyCourseId = "";

        String url = URL_BASE + URL_CHECK_RESULT_ANSWERS.replace("<AccessToken>", token)
                .replace("<TestPaperId>", testPaperId)
                .replace("<BuyCourseId>", buyCourseId);

        return url;

    }


    /**
     *
     *              获取教学计划列表  GET
     *
     AccessToken	string	用户身份令牌
     *
     */
    public static final String URL_TEACH_PLAN = "OrganCourse/TeachPlanList?AccessToken=<AccessToken>";

    public static String getUrlTeachPlan(String token) {

        if(TextUtils.isEmpty(token)) throw new RuntimeException(EXCEPTION_STR);

        String url = URL_BASE + URL_TEACH_PLAN.replace("<AccessToken>", token);

        return url;
    }

    /**
     *              获取教学计划下（二级）课程列表
     *
     * AccessToken	string	用户身份令牌
     *
     * TeachPlanId	Guid	教学计划Id
     *
     */

    public static final String URL_TEACH_PLAN_COURSES = "OrganCourse/CourseList?AccessToken=<AccessToken>&TeachPlanId=<TeachPlanId>";

    public static String getUrlTeachPlanCourses(String token, String teachPlanId) {

        if(TextUtils.isEmpty(token)) throw new RuntimeException(EXCEPTION_STR);

        String url = URL_BASE + URL_TEACH_PLAN_COURSES.replace("<AccessToken>", token).replace("<TeachPlanId>", teachPlanId);

        return url;
    }

    /**
     *
     *              获取E学院考试列表
     *
     AccessToken	string	用户身份令牌
     PageNum	int	返回第几页，可选，默认值为1
     PageSize	int	每页包含几条数据，可选，默认值为10
     *
     */

    public static final String URL_EXAM_E_LIST = "OrganExam/List?AccessToken=<AccessToken>&PageSize=<PageSize>&PageNum=<PageNum>";

    public static final String getUrlExamEList(String token, int pageNum, int pageSize) {

        if(TextUtils.isEmpty(token)) throw new RuntimeException(EXCEPTION_STR);
        if(pageSize <= 0) pageSize = 10;
        if(pageNum <= 0) pageNum = 1;

        String url = URL_BASE + URL_EXAM_E_LIST.replace("<AccessToken>", token)
                .replace("<PageSize>", pageSize + "")
                .replace("<PageNum>", pageNum + "");


        return url;
    }

    /**
     *
     *              发送验证码  POST
     *
     Phone	string	手机号
     *
     */
    public static final String URL_SEND_CODE = "SMS/SendCode";

    public static final String getUrlSendCode() {

        String url = URL_BASE + URL_SEND_CODE;

        return url;

    }


    /**
     *
     *              用手机号注册 POST
     *
     *
     Phone	string	手机号
     Code	string	短信验证码
     Password	string	密码
     *
     *
     */
    public static final String URL_REGIST_PHONE = "User/RegisterWithPhone";

    public static final String getUrlRegistPnone() {

        String url =  URL_BASE + URL_REGIST_PHONE;

        return url;

    }

    /**
     *
     *              重置密码
     *
     *
     */
    public static final String URL_FORGET_PHONE = "User/ResetPassword";

    public static final String getUrlForgetPhone() {


        String url = URL_BASE + URL_FORGET_PHONE;

        return url;

    }



    /**
     *
     *              获取我的课程下自课程列表
     *

     *
     */
    public static final String URL_CHIDCOURSE = "Course/MyChildCourses?BuyCourseId=<BuyCourseId>";

    public static final String getUrlChildrenCourse(String buyCourseId) {

        if(TextUtils.isEmpty(buyCourseId)) buyCourseId = "";

        String url = URL_BASE + URL_CHIDCOURSE.replace("<BuyCourseId>", buyCourseId);

        return url;

    }


    public static final String URL_ORGANCHILDCOURSE = "OrganCourse/ChildCourseList?TeachPlanId=<TeachPlanId>&CourseId=<CourseId>";

    public static final String getUrlOrganChildcourse(String teacherPlanId, String courseId) {

        if(TextUtils.isEmpty(teacherPlanId)) teacherPlanId = "";
        if(TextUtils.isEmpty(courseId)) courseId = "";

        String url = URL_BASE + URL_ORGANCHILDCOURSE.replace("<TeachPlanId>", teacherPlanId).replace("<CourseId>", courseId);

        return url;

    }


    /**
     *
     *              查看试卷答题结果
     *
     AccessToken	string	用户身份令牌
     TestPaperId	Guid	试卷Id
     *
     */
    public static final String URL_EXAMREULT_E = "OrganExam/Result?AccessToken=<AccessToken>&TestPaperId=<TestPaperId>";

    public static final String getUrlExamResultE(String token, String testPaperId) {

        if(TextUtils.isEmpty(token)) throw new RuntimeException(EXCEPTION_STR);
        if(TextUtils.isEmpty(testPaperId)) testPaperId = "";

        String url = URL_BASE + URL_EXAMREULT_E.replace("<AccessToken>", token).replace("<TestPaperId>", testPaperId);

        return url;
    }

    /**
     *
     *              获取支付方式列表 GET
     *
     */
    public static final String URL_PAYLIST = "Pay/List?SystemFlag=Android";

    public static String getUrlPayList() {

        String url = URL_BASE + URL_PAYLIST;
        return url;

    }


    /**
     *
     *              提交购买
     *
     AccessToken	string	用户身份令牌
     Key	string	支付Key
     ConfigIds	string	课程的ConfigId属性，多个请用半角逗号间隔
     FromShoppingCart	bool	是否自动从购物车移除课程，默认不移除
     *
     */
    public static final String URL_PAY_BUY = "Pay/Buy";

    public static String getUrlPayBuy() {

        String url = URL_BASE + URL_PAY_BUY;

        return url;

    }

    /**
     *
     *              课程收藏
     *
     AccessToken	string	用户身份令牌
     CourseId	string	课程Id
     CourseType	int	课程类型
     *
     */
    public static final String URL_ADD_FAV = "CourseCollect/Join";

    public static String getUrlAddFavorite() {

        String url = URL_BASE + URL_ADD_FAV;

        return url;

    }


    /**
     *
     *              移除收藏
     *
     AccessToken	string	用户身份令牌
     Id	Guid	收藏Id
     *
     */
    public static final String URL_REMOVED_FAV = "/CourseCollect/Remove";

    public static String getUrlRemovedFav() {

        String url = URL_BASE + URL_REMOVED_FAV;

        return url;

    }

    /**
     *
     *          收藏列表
     *
     AccessToken	string	用户身份令牌
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     PageNum	int	返回第几页，可选，默认值为1
     PageSize	int	每页包含几条数据，可选，默认值为10
     *
     */
    public static final String URL_LIST_FAV = "CourseCollect/List?AccessToken=<AccessToken>&Size=<Size>&PageSize=<PageSize>&PageNum=<PageNum>";

    public static String getUrlListFav(String token, String size, int pageNum, int pageSize) {

        if(TextUtils.isEmpty(token)) throw new RuntimeException(EXCEPTION_STR);
        if(TextUtils.isEmpty(size)) size = "360*270";

        String url = URL_BASE + URL_LIST_FAV.replace("<AccessToken>", token)
                .replace("<Size>", size).replace("<PageSize>", pageSize + "")
                .replace("<PageNum>", pageNum + "");

        return url;

    }

    /**
     *
     *          QQ登录　POST
     *
     Key	string	第三方登陆Key：QQLogin、WeiXinLogin
     ExtendData	string	第三方登陆后返回的带有access token及openid的字符串
     *
     */
    public static final String URL_QQ_LOGIN = "User/ThirdLogin";

    public static String getUrlQQLogin() {

        String url = URL_BASE + URL_QQ_LOGIN;

        return url;
    }

    /**
     *
     *          微信登录 POST
     *
     Key	string	第三方登陆Key：QQLogin、WeiXinLogin
     AuthorizationCode	string	第三方登陆后返回的带有access token及openid的字符串
     *
     */
    public static final String URL_WX_LOGIN = "User/ThirdLoginWithCode";

    public static String getUrlWxLogin() {

        String url = URL_BASE + URL_WX_LOGIN;

        return url;

    }


    /**
     *
     *          获取推荐分类及其下课程列表   GET
     *
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     PageSize	int	每页包含几条数据，可选，默认值为10
     *
     */

    public static final String URL_RECMD_COURSE = "CourseCategory/RecommendCategoryCourses?Size=<Size>&PageSize=<PageSize>";

    public static String getUrlRecmdCourse(String size, int pageSize) {

        String url = URL_BASE + URL_RECMD_COURSE.replace("<Size>", size).replace("<PageSize>", pageSize + "");

        return url;

    }

    /**
     *
     *          第三方绑定用手机号注册  POST
     *
     * Phone	string	手机号
     Code	string	短信验证码
     Password	string	密码
     ThirdLoginKey	string	第三方登陆Key
     ThirdLoginOpenId	string	第三方登陆返回OpenId
     Name	string	第三方登陆用户昵称
     Avatar	string	第三方登陆用户头像地址
     *
     */
    public static final String URL_BIND_REIGISt = "User/ThirdBindRegisterWithPhone";

    public static String getURlBindRegist() {

        String url = URL_BASE + URL_BIND_REIGISt;

        return url;

    }

    /**
     *
     *
     *
     *
     */
    public static final String URL_BIND_LOGIN = "User/ThirdBindLogin";

    public static final String getUrlBindLogin() {

        String url = URL_BASE + URL_BIND_LOGIN;

        return url;
    }


    /**
     *
     *                获取讲师列表 也用于搜索 GET
     *
     *
     Name	string	讲师姓名
     PageNum	int	返回第几页，可选，默认值为1
     PageSize	int	每页包含几条数据，可选，默认值为10
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     *
     */
    public static final String URL_ST = "Teacher/List?Name=<Name>&Size=<Size>&PageNum=<PageNum>&PageSize=<PageSize>";

    public static final String getURLSt(String name, String size, int pagenum, int pagesize) {

        String url = URL_BASE + URL_ST.replace("<Name>", name)
                .replace("<Size>", size)
                .replace("<PageNum>", pagenum + "")
                .replace("<PageSize>", pagesize + "");

        return  url;

    }

    /**
     *
     *  上传头像
     AccessToken	string	用户的识别令牌
     Avatar	string	base64编码的jpeg图片数据
     FileExt	string	头像扩展名
     *
     */

    public static final String URL_UP_AVATAR = "User/SetAvatar";

    public static final String getUrlUpAvatar() {

        String url = URL_BASE + URL_UP_AVATAR;

        return url;

    }


    public static final String URL_SET_NICK = "User/SetNickName";

    public static final String getUrlSetNick() {

        String url = URL_BASE + URL_SET_NICK;

        return url;

    }

    /**
     *获取CMS页面列表
     *
     *ParentId	Guid	请求CMS页面的栏目Id
     PageNum	int	返回第几页，可选，默认值为1
     PageSize	int	每页包含几条数据，可选，默认值为10
     *
     */
    public static final String URL_CMS_LIST = "CMSPages/List?ParentId=<ParentId>&PageNum=1&PageSize=10";

    public static final String getUrlCmsList(String parentId, int pagenum) {

        String url = URL_BASE + URL_CMS_LIST.replace("<ParentId>", parentId)
                .replace("<PageNum>", pagenum + "");

        return url;

    }

    /**
     *
     *获取页面详细信息

     参数说明
     参数名	类型	说明
     Id	Guid	页面ID
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     *
     */
    public static final String URL_CMS_DEITAIL = "CMSPages/Info?Id=<Id>&Size=360*270";

    public static final String getUrlCmsDeitail(String cmsId) {

        String url = URL_BASE + URL_CMS_DEITAIL.replace("<Id>", cmsId);

        return url;

    }

    public static final String URL_ADVICE = "http://chat.looyu.com/chat/chat/p.do?c=47318&f=110984&g=54627=APP";

    public static final String VERSION_CHECK_URL = "http://app.zhiwang123.com/AutoUpdate/GetNewestAutoUpdate?applicationName=ZhiWangAndroidClient";

    public static final String URL_ACTIVE_CARD = "Course/ActiveCourseCard";

    public static final String getUrlActiveCard() {

        String url = URL_BASE + URL_ACTIVE_CARD;

        return url;

    }


    /**
     *
     * 0元购买 POST
     *
     * Pay/Buy0MoneyCourse
     *
     */

    public static final String URL_0_MONEY_BUY = "Pay/Buy0MoneyCourse";

    public static final String getUrl0MoneyBuy() {

        String url = URL_BASE + URL_0_MONEY_BUY;

        return url;

    }

    /**
     *
     *  新分类接口
     *
     *  获取课程一级分类
     *
     *
     */

    public static final String URL_COURSE_CUSTOM = "CourseCategory/CustomCategory";

    public static String getCourseCustom() {

        return URL_BASE + URL_COURSE_CUSTOM;
    }


    /**
     *
     *
     *  ····················新获取课程分类下自课程列表接口
     *

     *                      获取课程(二、三级)列表 也用于搜索 GET
     *
     CategoryPkIds	String	分类Id (多个以半角逗号分隔)
     Title	string	课程标题，用于搜索课程
     Speeker	string	讲师姓名
     Size	string	图片尺寸，格式：xxx*xxx，默认120*90；可以传false，返回图片原始链接
     PageNum	int	返回第几页，可选，默认值为1
     PageSize	int	每页包含几条数据，可选，默认值为10
     */
    public static final String URL_COURSE_LIST_2 = "Course/ListByMoreCategoryPkId?CategoryPkIds=<PkIds>&Title=<Title>&Speeker=<Speeker>&Size=<Size>&PageSize=<PageSize>&PageNum=<PageNum>";

    public static String getUrlCourseList_2(String categoryPkIds, String size, int pageNum, int pageSize) {
        return getUrlCourseList_2(categoryPkIds, "", "", size, pageNum, pageSize);
    }

    public static String getUrlCourseList_2(String categoryPkIds, String title, String speeker, String size, int pageNum, int pageSize) {

        if(TextUtils.isEmpty(title)) title = "";
        if(TextUtils.isEmpty(speeker)) speeker = "";
        if(TextUtils.isEmpty(size)) size = "360*270";

        String url = URL_BASE + URL_COURSE_LIST_2.replace("<PkIds>", categoryPkIds + "").replace("<Title>", title).replace("<Speeker>", speeker)
                .replace("<Size>", size).replace("<PageNum>", pageNum + "").replace("<PageSize>", pageSize + "");

        return url;
    }


}
