package com.ptithcm.documentshub.network;

import android.content.Context;

import com.ptithcm.documentshub.model.RefreshRequest;
import com.ptithcm.documentshub.network.api.AuthService;
import com.ptithcm.documentshub.utils.TokenManager;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

public class TokenAuthenticator implements Authenticator {
    private final TokenManager tokenManager;
    private AuthService refreshApiService = null;
    Context context;

    public TokenAuthenticator(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public TokenAuthenticator(Context context, TokenManager tokenManager) {
        this.context = context;
        this.tokenManager = tokenManager;
    }


    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        // Nếu bản thân API refresh cũng trả về 401, tức là refresh token cũng đã hết hạn
//         Trả về null để hủy toàn bộ quá trình, đẩy lỗi về cho app (để văng ra màn hình Login)
        if (isRequestWithAccessToken(response) && response.priorResponse() != null && response.priorResponse().code() == 401) {
            tokenManager.clearTokens();
            return null;
        }

        // Khóa luồng (Thread-safe) để tránh việc nhiều request cùng gọi API refresh lúc
        synchronized (this) {
            String accessToken = tokenManager.getAccessToken();
            String refreshToken = tokenManager.getRefreshToken();

            if (refreshToken == null) {
                tokenManager.clearTokens();
                return null;
            }

            if (refreshApiService == null) {
                refreshApiService = ApiClient.getInstance().createService(AuthService.class);
            }
            // Gọi API refresh token đồng bộ để lấy token mới
            Call<ApiResponse<String>> call = refreshApiService.refresh(new RefreshRequest(refreshToken));
            retrofit2.Response<ApiResponse<String>> refreshResponse = call.execute();

            if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                String newAccessToken = refreshResponse.body().getData();

                // Cập nhật token mới vào TokenManager
                tokenManager.saveAccessToken(newAccessToken);

                // Retry request cũ với token mới
                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + newAccessToken)
                        .build();
            } else {
                // Refresh token cũng hết hạn hoặc có lỗi khác, xóa tokens và trả về null để yêu cầu login lại
                tokenManager.clearTokens();
                return null;
            }
        }
    }

    private boolean isRequestWithAccessToken(Response response) {
        String header = response.request().header("Authorization");
        return header != null && header.startsWith("Bearer");
    }
}