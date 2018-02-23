package build.dream.platform.utils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static Request buildRequest(String url, Map<String, String> requestParameters, String method, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        if (GET.equals(method)) {
            builder.get();
            List<String> requestParameterPairs = new ArrayList<String>();
            if (MapUtils.isNotEmpty(requestParameters)) {
                for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
                    requestParameterPairs.add(entry.getKey() + "=" + entry.getValue());
                }
                url = url + "?" + StringUtils.join(requestParameterPairs, "&");
            }
        } else if (POST.equals(method)) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
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

    public static String doGetWithRequestParameters(String url, long connectTimeout, long readTimeout, long writeTimeout, Map<String, String> headers, Map<String, String> requestParameters, String charsetName) throws IOException {
        OkHttpClient okHttpClient = buildOkHttpClient(connectTimeout, readTimeout, writeTimeout);
        Request request = buildRequest(url, requestParameters, GET, headers);
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String doPostWithRequestParameters(String url, long connectTimeout, long readTimeout, long writeTimeout, Map<String, String> headers, Map<String, String> requestParameters, String charsetName) throws IOException {
        OkHttpClient okHttpClient = buildOkHttpClient(connectTimeout, readTimeout, writeTimeout);
        Request request = buildRequest(url, requestParameters, POST, headers);
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("loginName", "61011888");
        String result = doPostWithRequestParameters("http://www.smartpos.top/portal/tenantWebService/showTenantInfo", 0, 0, 0, null, requestParameters, null);
        System.out.println(result);
    }
}
