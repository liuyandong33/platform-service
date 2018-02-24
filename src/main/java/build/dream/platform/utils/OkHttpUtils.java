package build.dream.platform.utils;

import build.dream.platform.constants.Constants;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {
    public static final String GET = "GET";
    public static final String POST = "POST";

    public static OkHttpClient buildOkHttpClient(long connectTimeout, long readTimeout, long writeTimeout) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        return okHttpClientBuilder.build();
    }

    public static Request buildRequest(String url, Map<String, String> requestParameters, String method, Map<String, String> headers, String charsetName) throws UnsupportedEncodingException {
        Request.Builder builder = new Request.Builder();
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        if (GET.equals(method)) {
            builder.get();
            if (MapUtils.isNotEmpty(requestParameters)) {
                url = url + "?" + buildQueryString(requestParameters, charsetName);
            }
        } else if (POST.equals(method)) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder(Charset.forName(charsetName));
            if (MapUtils.isNotEmpty(requestParameters)) {
                for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), entry.getValue());
                }
            }
            builder.post(formBodyBuilder.build());
        }
        builder.url(url);
        return builder.build();
    }

    public static String doGetWithRequestParameters(String url, Map<String, String> requestParameters) throws IOException {
        return doGetWithRequestParameters(url, requestParameters, Constants.CHARSET_NAME_UTF_8);
    }

    public static String doGetWithRequestParameters(String url, Map<String, String> requestParameters, String charsetName) throws IOException {
        return doGetWithRequestParameters(url, 0, 0, 0, requestParameters, charsetName);
    }

    public static String doGetWithRequestParameters(String url, long connectTimeout, long readTimeout, long writeTimeout, Map<String, String> requestParameters) throws IOException {
        return doGetWithRequestParameters(url, connectTimeout, readTimeout, writeTimeout, requestParameters, Constants.CHARSET_NAME_UTF_8);
    }

    public static String doGetWithRequestParameters(String url, long connectTimeout, long readTimeout, long writeTimeout, Map<String, String> requestParameters, String charsetName) throws IOException {
        return doGetWithRequestParameters(url, connectTimeout, readTimeout, writeTimeout, null, requestParameters, charsetName);
    }

    public static String doGetWithRequestParameters(String url, Map<String, String> headers, Map<String, String> requestParameters) throws IOException {
        return doGetWithRequestParameters(url, headers, requestParameters, Constants.CHARSET_NAME_UTF_8);
    }

    public static String doGetWithRequestParameters(String url, Map<String, String> headers, Map<String, String> requestParameters, String charsetName) throws IOException {
        return doGetWithRequestParameters(url, 0, 0, 0, headers, requestParameters, charsetName);
    }

    public static String doGetWithRequestParameters(String url, long connectTimeout, long readTimeout, long writeTimeout, Map<String, String> headers, Map<String, String> requestParameters, String charsetName) throws IOException {
        OkHttpClient okHttpClient = buildOkHttpClient(connectTimeout, readTimeout, writeTimeout);
        Request request = buildRequest(url, requestParameters, GET, headers, charsetName);
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String doPostWithRequestParameters(String url, Map<String, String> requestParameters) throws IOException {
        return doPostWithRequestParameters(url, null, requestParameters);
    }

    public static String doPostWithRequestParameters(String url, Map<String, String> requestParameters, String charsetName) throws IOException {
        return doPostWithRequestParameters(url, null, requestParameters, charsetName);
    }

    public static String doPostWithRequestParameters(String url, long connectTimeout, long readTimeout, long writeTimeout, Map<String, String> requestParameters) throws IOException {
        return doPostWithRequestParameters(url, connectTimeout, readTimeout, writeTimeout, requestParameters, Constants.CHARSET_NAME_UTF_8);
    }

    public static String doPostWithRequestParameters(String url, long connectTimeout, long readTimeout, long writeTimeout, Map<String, String> requestParameters, String charsetName) throws IOException {
        return doPostWithRequestParameters(url, connectTimeout, readTimeout, writeTimeout, null, requestParameters, charsetName);
    }

    public static String doPostWithRequestParameters(String url, Map<String, String> headers, Map<String, String> requestParameters) throws IOException {
        return doPostWithRequestParameters(url, headers, requestParameters, Constants.CHARSET_NAME_UTF_8);
    }

    public static String doPostWithRequestParameters(String url, Map<String, String> headers, Map<String, String> requestParameters, String charsetName) throws IOException {
        return doPostWithRequestParameters(url, 0, 0, 0, headers, requestParameters, charsetName);
    }

    public static String doPostWithRequestParameters(String url, long connectTimeout, long readTimeout, long writeTimeout, Map<String, String> headers, Map<String, String> requestParameters, String charsetName) throws IOException {
        OkHttpClient okHttpClient = buildOkHttpClient(connectTimeout, readTimeout, writeTimeout);
        Request request = buildRequest(url, requestParameters, POST, headers, charsetName);
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String buildQueryString(Map<String, String> requestParameters, String charsetName) throws UnsupportedEncodingException {
        List<String> requestParameterPairs = new ArrayList<String>();
        Set<Map.Entry<String, String>> entries = requestParameters.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            requestParameterPairs.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), charsetName));
        }
        return StringUtils.join(requestParameterPairs, "&");
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("loginName", "61011888");
        String result = doPostWithRequestParameters("http://www.smartpos.top/portal/tenantWebService/showTenantInfo", requestParameters);
        System.out.println(result);
    }
}
