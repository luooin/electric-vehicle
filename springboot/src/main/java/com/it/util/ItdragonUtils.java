package com.it.util;

import com.it.config.ShiroSpringConfig;
import com.it.entity.Product;
import com.it.entity.User;
import com.it.service.ProductService;
import com.it.vo.SubmitOrderVo;
//import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * 描述：〈工具类〉
 */
@Component
public class ItdragonUtils {

    private static final String ALGORITHM_NAME = "MD5";
    private static final Integer HASH_ITERATIONS = 1024;
    @Resource
    ProductService productService;

    /**
     * 加盐加密的策略非常多,根据实际业务来
     */
    public void entryptPassword(User user) {
        String salt = UUID.randomUUID().toString();
        String temPassword = user.getPlainPassword();
        Object md5Password = new SimpleHash(ALGORITHM_NAME, temPassword, ByteSource.Util.bytes(salt), HASH_ITERATIONS);
        user.setSalt(salt);
        user.setPassword(md5Password.toString());
    }

    /**
     * 判断当前系统是否需要加密
     */
    public static boolean isEncrypted() {
        ShiroSpringConfig shiroSpringConfig = new ShiroSpringConfig();
        CustomConfiguration customConfiguration = shiroSpringConfig.setIsEncrypted();
        if ("yes".equals(customConfiguration.getIsEncrypted())) {
            return true;
        } else {
            return false;
        }
    }

    public static String payType() {
        ShiroSpringConfig shiroSpringConfig = new ShiroSpringConfig();
        CustomConfiguration customConfiguration = shiroSpringConfig.setIsEncrypted();
        return customConfiguration.getPayType();
    }

    /**
     * 获取ShiroSession
     *
     * @return
     */
    public Session getShiroSession() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return session;
    }

    /**
     * 获取Session用户信息
     *
     * @return
     */
    public User getSessionUser() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user;
    }

    /**
     * 获取当前登录用户的用户名
     *
     * @return
     */
    public String getSessionUserName() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user.getUserName();
    }

    /**
     * 获取当前登录用户的头像
     *
     * @return
     */
    public String getSessionUserImg() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user.getImgUrl();
    }

    /**
     * 获取当前登录用户的id
     *
     * @return
     */
    public String getSessionUserId() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user.getId();
    }

    /**
     * 获取Session中的购物信息
     *
     * @return
     */
    public List<SubmitOrderVo> getSessionSubmitOrderVoList() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        List<SubmitOrderVo> submitOrderVoList = (List<SubmitOrderVo>) session.getAttribute("submitOrderVoList");
        return submitOrderVoList;
    }

    /**
     * 生成随便编号
     *
     * @return
     */
    public String getUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) { //有可能是负数
            hashCodeV = -hashCodeV;
        }
//         0 代表前面补充0     
//         4 代表长度为4     
//         d 代表参数为正数型
        return machineId + String.format("%010d", hashCodeV);
    }

    /**
     * 生成随便编号
     *
     * @return
     */
    public String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) { //有可能是负数
            hashCodeV = -hashCodeV;
        }
//         0 代表前面补充0     
//         4 代表长度为4     
//         d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    /**
     * 判断用户是否登录
     */
    public boolean isGogin() {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isAuthenticated();
    }

    /**
     * 字符串不为空的判断函数
     *
     * @param string
     * @return
     */
    public static boolean stringIsNotBlack(String string) {
        if (string != null && !"".equals(string)) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前ip
     *
     * @return
     */
    public static String getIp() {
        InetAddress ip;
        String addressHost = "";
        try {
            addressHost = getLocalHostLANAddress().getHostAddress();
        } catch (UnknownHostException e) {

            e.printStackTrace();
        }
        return addressHost;
    }

    // 正确的IP拿法，即优先拿site-local地址
    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

    /**
     * 协同过滤推荐
     *
     * @param userId
     * @param n      返回推荐条数
     * @return 数组，itemId
     * @throws TasteException
     * @throws ClassNotFoundException
     */
    public int[] recommendByUser(Integer userId, int n) throws TasteException, ClassNotFoundException {
       /* Class.forName("com.mysql.jdbc.Driver");
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");//本地为localhost
        dataSource.setUser("root");
        dataSource.setPassword("123");
        dataSource.setDatabaseName("obt_db");//数据库名
         *//*
        preferenceTable:表名
        userIDColumn:userId的字段名
        itemIDColumn:itemId的字段名
        preferenceColumn：偏好值字段名
        timestampColumn：时间记录字段//可为空
         *//*
        JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource, "gm_browseRecord", "userId", "itemId", "rating", "time");
        //获取模型
        DataModel model = dataModel;
        //计算相似度
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        //计算阈值,选择邻近的2个用户
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
        //推荐集合
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        //推荐数量 为n的一个合集,这里数量可以修改
        List<RecommendedItem> recommendedItems = recommender.recommend(userId, n);
        int kl_idArray[] = new int[recommendedItems.size()];
        for (int i = 0; i < recommendedItems.size(); i++) {
            kl_idArray[i] = (int) recommendedItems.get(i).getItemID();
        }
        return kl_idArray;*/
        return null;
    }

    public List<Product> getProductList(Integer userId, int n) {
        int[] ints = new int[0];
        try {
            ints = recommendByUser(userId, n);
        } catch (TasteException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Product> list = new ArrayList<>();
        for (int i : ints) {
            Product product = productService.getOne(i + "");
            if (product != null) {
                if ("1".equals(product.getState())) {
                    list.add(product);
                }
            }
        }
        return list;
    }
}
