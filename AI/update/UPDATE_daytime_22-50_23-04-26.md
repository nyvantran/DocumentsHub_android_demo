# Nhật ký cập nhật - DocumentHub (Android)

**Thời gian tạo:** 22:50 Ngày 23-04-2026

## 🛠 Các công việc đã thực hiện

### 1. Kiến trúc MVVM & Hệ thống Network
- **Tổ chức lại Project**: Di chuyển dự án sang kiến trúc MVVM chuẩn.
    - Tạo các package: `com.ptithcm.documentshub.viewmodel`, `com.ptithcm.documentshub.repository`, `com.ptithcm.documentshub.network.api`.
    - Bổ sung dependencies: `Lifecycle (ViewModel, LiveData)`, `Retrofit`, `Gson`.
- **Chuẩn hóa Network Layer**:
    - `ApiResponse.java`: Lớp wrapper cho cấu trúc dữ liệu API (success, data, message, meta).
    - `ApiClient.java`: Cấu hình Retrofit với phương thức generic `createService`.
    - Tách API thành các interface chuyên biệt trong thư mục `network/api/`: `AuthService`, `DocumentService`, `CategoryService`, `UserService`.
- **Repositories & ViewModels**:
    - Triển khai `AuthRepository` và `DocumentRepository`.
    - Triển khai `LoginViewModel`, `HomeViewModel`, `DocumentViewModel`.

### 2. Chức năng Đăng nhập (Login)
- **Logic Nghiệp vụ**:
    - Hoàn thiện `LoginActivity` theo MVVM.
    - `TokenManager`: Lưu trữ JWT Token (`access_token`, `refresh_token`) vào `SharedPreferences`.
    - Xử lý trạng thái Loading trên nút bấm và thông báo lỗi qua `Toast`.
- **Models**: Tạo `LoginRequest` và `LoginResponse`.

### 3. Giao diện Trang chủ (Home Screen)
- **Cập nhật Layout theo thiết kế (`home_screen.png`)**:
    - `fragment_home.xml`: Bổ sung Header "DocumentHub", Search Bar bo góc và thanh cuộn ngang Categories.
    - `item_category_section.xml`: Thêm icon Trending và nút "See all".
    - `item_document.xml`: Nâng cấp thẻ tài liệu với vùng Preview xanh nhạt, icon Visibility (Globe), và các chỉ số Views, Downloads, Likes.
- **Tài nguyên mới**:
    - Icon Vector: `ic_trending_up`, `ic_eye`, `ic_globe`, `ic_circle_small`.
    - Adapter: Cập nhật `DocumentAdapter` và `CategorySectionAdapter` để đổ dữ liệu cho các trường mới.

### 4. Fix lỗi & Cấu hình hệ thống
- **Network Security**: Tạo `network_security_config.xml` để cho phép kết nối HTTP tới localhost và IP mạng nội bộ (khắc phục lỗi CLEARTEXT communication).
- **AndroidManifest**: Khai báo quyền `INTERNET` và kích hoạt cấu hình bảo mật mạng.

## 📝 Ghi chú
- Các logic dữ liệu mẫu (Dummy data) được giữ lại trong ViewModel như một phương án dự phòng khi API không phản hồi.
- Màn hình `DocumentActivity` đã được chuyển đổi sang MVVM thành công.
