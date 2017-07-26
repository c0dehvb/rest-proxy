package com.midea.cmms.rest.proxy.server.sign

import com.midea.cmms.base.CacheUtil
import com.midea.cmms.base.CryptoUtil
import com.midea.cmms.base.FUtil
import com.midea.cmms.base.Result
import com.midea.cmms.base.http.PoolingHttpClient
import com.midea.cmms.rest.proxy.server.CacheConst
import com.midea.cmms.rest.proxy.server.exception.AppSignError
import groovy.json.internal.Charsets
import org.apache.commons.codec.binary.Base64
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired


/**
 * App签名校验器，对用户传入的签名进行校验
 *
 * <p>
 *     周期性连接平台获取所有AppKey和AppSecret信息
 * </p>
 */
class AppSignChecker {
    private static final Logger LOG = Logger.getLogger(AppSignChecker)

    private CacheUtil cacheUtil

    private PoolingHttpClient poolingHttpClient

    private String allAppInfoApiUrl

    private String appKey

    private String appSecret

    private byte[] KEY, IV

    private final static int cacheExpire = 300     // 秒

    void setCryptoSequence(String cryptoSequence) {
        LOG.info(cryptoSequence)
        byte[] buf=CryptoUtil.sha1(cryptoSequence.getBytes())
        KEY=Arrays.copyOf(buf, 24)
        IV=Arrays.copyOf(buf, 8)
    }

    void setAllAppInfoApiUrl(String allAppInfoApiUrl) {
        this.allAppInfoApiUrl = allAppInfoApiUrl
    }

    void setAppKey(String appKey) {
        this.appKey = appKey
    }

    void setAppSecret(String appSecret) {
        this.appSecret = appSecret
    }

    @Autowired
    void setCacheUtil(CacheUtil cacheUtil) {
        this.cacheUtil = cacheUtil
    }

    @Autowired
    void setPoolingHttpClient(PoolingHttpClient poolingHttpClient) {
        this.poolingHttpClient = poolingHttpClient
    }

    /**
     * 验证签名有效性
     * @param appKey
     * @param tm
     * @param sign
     * @throws AppSignError 签名认证失败时抛出
     */
    void checkSignature(String appKey, Long tm, String sign) {
        Map<String, Object> appInfoMap = getAllAppInfo()
        Map<String, Object> appInfo = (Map) appInfoMap.get(appKey)

        // 检验AppKey是否存在
        if (appInfo == null) {
            throw new AppSignError("找不到AppKey: " + appKey)
        }

        // 检验签名认证正确性
        String curSign = signature((String)appInfo.appKey, (String)appInfo.secret, tm)
        if (sign != curSign) {
            throw new AppSignError("App签名认证失败: " + appKey)
        }
    }

    /**
     * 调用平台接口或从Redis缓存中获取所有AppInfo信息
     * @return
     * @throws AppSignError 调用接口的签名校验失败
     */
    @SuppressWarnings("unchecked")
    private Map<String, AppInfo> getAllAppInfo() {
        return cacheUtil.getCached(CacheConst.allAppInfo(), cacheExpire, Map.class) {
            LOG.info("调用平台接口获取所有AppInfo...")

            // 计算签名(自己调用平台接口，所以要计算自己的签名)
            long tm = new Date().getTime()
            String sign = signature(this.appKey, this.appSecret, tm)
            def param = [appKey: this.appKey, tm:tm, sign:sign]

            LOG.debug("调用HTTP接口: ${allAppInfoApiUrl} - ${param}")

            // 调用平台接口获取所有AppInfo
            String json = poolingHttpClient.getHcu()
                    .getUrlContent(allAppInfoApiUrl, param, Charsets.UTF_8.name())

            LOG.debug("HTTP接口返回值: " + json)

            Result result = FUtil.fromJson(json, Result)

            if (result.code != 0) throw new AppSignError(result.msg)
            String cryptoStr = result.data
            json = decrypt(cryptoStr)      // 解密json串

            // 把结果转成 Map
            List<AppInfo> list = FUtil.fromJson(json, List)
            Map<String, AppInfo> ans = new HashMap<>(list.size())
            list.each { ans[it.appKey] = it }

            return ans
        } as Map<String, AppInfo>
    }

    /**
     * 解密字符串，base64(3des(str))，utf-8编码
     * @param str
     * @return
     */
    private String decrypt(String str) {
        byte[] buf = Base64.decodeBase64(str)
        buf = CryptoUtil.tripleDesDecrypt(KEY, IV, buf)
        return new String(buf, "UTF-8")
    }

    /**
     * 计算签名
     * @param appKey
     * @param appSecret
     * @param tm
     * @return
     */
    static String signature(String appKey, String appSecret, Long tm) {
        return CryptoUtil.md5String( (appKey + tm + appSecret).getBytes() )
    }
}
