package com.midea.cmms.rest.proxy.server.test

import com.midea.cmms.base.http.PoolingHttpClient

/**
 * Created by liyilin on 2017/7/20.
 */
class RemoteTest extends GroovyTestCase {
    private PoolingHttpClient httpClient
    final String HOST = "http://10.16.85.87:8081/rest-proxy"

    void setUp() {
        httpClient = new PoolingHttpClient()
        httpClient.init()
    }

    void testIntAdd() {
        final String uri = "/int/add"
        final String url = HOST + uri
        final params = [a:1, b:2]

        def response = httpClient.getHcu().postForm(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testIntAddJson() {
        final String uri = "/int/add"
        final String url = HOST + uri
        final params = [a:1, b:2]

        def response = httpClient.getHcu().postJson(url, params)
        println "[Json] ${uri}: ${response}"
    }

    void testIntSub() {
        final String uri = "/int/sub"
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
        final String uri = "/faction/add"
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
