package com.midea.cmms.rest.proxy.server.test

import com.midea.cmms.base.CryptoUtil
import com.midea.cmms.base.http.PoolingHttpClient

import java.lang.reflect.Method
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by liyilin on 2017/7/20.
 */
class RemoteTest extends GroovyTestCase {
    private PoolingHttpClient httpClient
    final String HOST = "http://10.16.85.87:8081/rest-proxy"
//    final String HOST = "http://localhost:8080"
    final String appKey = "7523a4f1659148a4bc5d03101a84cdf5"
    final String appSecret = "92d9bfc22897483e8cb0acbc0cc96606"

    void setUp() {
        httpClient = new PoolingHttpClient()
        httpClient.init()
    }

    void testAllWhile() {
        final int SIZE = 5
        final that = this
        def methods = RemoteTest.class.declaredMethods
//        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
//                SIZE, SIZE, 100, TimeUnit.MILLISECONDS,
//                new LinkedBlockingDeque<Runnable>()
//        )

        while(true) {
            for (Method method : methods) {
                final m = method
//                if (m.name.startsWith("test") && m.name != "testAllWhile") {
//                    poolExecutor.execute(new Runnable() {
//                        @Override
//                        void run() {
//                            print m.name + ": "
//                            m.invoke(that)
//                        }
//                    })
//                }

                if (m.name.startsWith("test") && m.name != "testAllWhile") {
                    print m.name + ": "
                    m.invoke(that)
                }
            }
        }
    }

    void testIntAdd() {
        final long tm = new Date().time
        final String sign = CryptoUtil.md5String( (appKey + tm + appSecret).getBytes() )
        final String uri = "/int/add?appKey=${appKey}&tm=${tm}&sign=${sign}"
        final String url = HOST + uri
        final params = [a:1, b:2]

        def response = httpClient.getHcu().postForm(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testIntAddJson() {
        final long tm = new Date().time
        final String sign = CryptoUtil.md5String( (appKey + tm + appSecret).getBytes() )
        final String uri = "/int/add?appKey=${appKey}&tm=${tm}&sign=${sign}"
        final String url = HOST + uri
        final params = [a:1, b:2]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testIntSub() {
        final long tm = new Date().time
        final String sign = CryptoUtil.md5String( (appKey + tm + appSecret).getBytes() )
        final String uri = "/int/sub?appKey=${appKey}&tm=${tm}&sign=${sign}"
        final String url = HOST + uri
        final params = [a:1, b:2]

        def response = httpClient.getHcu().postForm(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testIntSubJson() {
        final String uri = "/int/sub"
        final String url = HOST + uri
        final params = [a:1, b:2]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testIntMul() {
        final String uri = "/int/mul"
        final String url = HOST + uri
        final params = [a:1, b:2]

        def response = httpClient.getHcu().postForm(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testIntMulJson() {
        final String uri = "/int/mul"
        final String url = HOST + uri
        final params = [a:1, b:2]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testIntDiv() {
        final String uri = "/int/div"
        final String url = HOST + uri
        final params = [a:1, b:2]

        def response = httpClient.getHcu().postForm(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testIntDivJson() {
        final String uri = "/int/div"
        final String url = HOST + uri
        final params = [a:1, b:2]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testIntSum() {
        final String uri = "/int/sum"
        final String url = HOST + uri
        final params = [1,2,3,4,5,6,7,8,9,10]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testFactionAddJson() {
        long tm = new Date().time
        final String sign = CryptoUtil.md5String( (appKey + tm + appSecret).getBytes() )
        final String uri = "/faction/add?appKey=${appKey}&tm=${tm}&sign=${sign}"
        final String url = HOST + uri
        final params = [[numerator: 1, denominator: 6], [numerator: 1, denominator: 3]]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testFactionSubJson() {
        final String uri = "/faction/sub"
        final String url = HOST + uri
        final params = [[numerator: 1, denominator: 6], [numerator: 1, denominator: 3]]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testFactionMulJson() {
        final String uri = "/faction/mul"
        final String url = HOST + uri
        final params = [[numerator: 1, denominator: 6], [numerator: 1, denominator: 3]]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testFactionDivJson() {
        final String uri = "/faction/div"
        final String url = HOST + uri
        final params = [[numerator: 1, denominator: 6], [numerator: 1, denominator: 3]]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testFactionSum() {
        final String uri = "/faction/sum"
        final String url = HOST + uri
        final params = [[numerator:1,denominator:6],[numerator:1,denominator:3],[numerator:1,denominator:6],[numerator:1,denominator:3]]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }
}
