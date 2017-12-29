package com.spacelabs.weatherapp.service.interceptor;

import com.google.gson.Gson;
import com.spacelabs.weatherapp.framework.logger.Logger;
import com.spacelabs.weatherapp.service.ApiErrorResponse;
import com.spacelabs.weatherapp.ui.base.BaseMvpView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Protocol;
import okhttp3.Response;

/**
 * Created by Gurpreet on 22/12/2017.
 */
public class ErrorInterceptor implements okhttp3.Interceptor {

    // The name of the TAG
    public String TAG = ErrorInterceptor.class.getSimpleName();
    private BaseMvpView baseMvpView;

    // The public constructor of the class
    public ErrorInterceptor(BaseMvpView baseMvpView) {
        this.baseMvpView = baseMvpView;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {

            Response response = chain.proceed(chain.request());

            if (!response.isSuccessful()) {
                Logger.e("There seems to be a error in the response, processing the error");

                final Gson gson = new Gson();
                ApiErrorResponse errorData = gson.fromJson(response.body().charStream(), ApiErrorResponse.class);
                if (errorData == null) {
                    Logger.d(TAG, "intercept: errordata is null");
                    errorData = new ApiErrorResponse();
                    errorData.setMessage("Something wrong happened. Please try again.");
                    baseMvpView.onApiError(errorData);
                } else {
                    Logger.d(TAG, "intercept: error data is not null");
                    baseMvpView.onApiError(errorData);
                }
            }
            return response;
        } catch (SocketTimeoutException e) {
            Logger.e("Exception " + e.fillInStackTrace());
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(HttpURLConnection.HTTP_OK).build();
        } catch (UnknownHostException e) {
            Logger.e("Exception " + e.fillInStackTrace());
            baseMvpView.onError("Looks like there is an issue with your internet connection. Please check your connection and try again");
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(HttpURLConnection.HTTP_OK).build();
        } catch (IOException e) {
            Logger.e("Exception " + e.fillInStackTrace());
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(HttpURLConnection.HTTP_OK).build();
        } catch (Exception e) {
            Logger.e("Exception " + e.fillInStackTrace());
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(HttpURLConnection.HTTP_OK).build();
        }
    }
}
