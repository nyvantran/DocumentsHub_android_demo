# Nhật ký cập nhật - DocumentHub (Android)

**Thời gian tạo:** 23:31 Ngày 21-04-2026

## 🛠 Các công việc đã thực hiện

### 1. Giao diện Đăng nhập (Login Screen)
- **Thiết kế Layout (`activity_login.xml`)**: Sử dụng `ConstraintLayout` và `CardView` để tạo giao diện thẻ trắng nổi trên nền xanh nhạt (`#F5F9FF`), bám sát thiết kế `login_screen.png`.
- **Thành phần UI**: 
    - Logo ứng dụng tự thiết kế bằng Vector XML (`ic_book`).
    - Các ô nhập liệu (`EditText`) bo góc 12dp, nền xám nhạt (`#F2F4F7`).
    - Nút Đăng nhập màu xanh dương (`#00BAF2`) với hiệu ứng Ripple.
- **Logic (`LoginActivity.java`)**: 
    - Ánh xạ các view và thiết lập sự kiện Click.
    - Thêm logic chuyển hướng sang `HomeActivity` sau khi "đăng nhập thành công".

### 2. Giao diện Màn hình chính (Home Screen)
- **Thiết kế Fragment Home (`fragment_home.xml`)**: 
    - Sử dụng `LinearLayout` làm gốc theo yêu cầu.
    - Tích hợp thanh tìm kiếm, danh sách danh mục cuộn ngang (`HorizontalScrollView`) và khu vực "Trending" cuộn dọc (`ScrollView`).
    - Thiết kế các Card tài liệu mẫu với ảnh minh họa và thông tin chi tiết.
- **Thanh điều hướng (Bottom Navigation)**:
    - Sử dụng `com.google.android.material.bottomnavigation.BottomNavigationView` chuẩn Material Design.
    - Tạo tệp menu (`bottom_nav_menu.xml`) với 3 mục: Home, Upload, Profile.

### 3. Cơ chế Chuyển trang & Vuốt (ViewPager2)
- **Thay thế Container**: Chuyển từ `FrameLayout` sang `ViewPager2` để hỗ trợ tính năng vuốt (swipe) giữa các màn hình.
- **Adapter (`HomeViewPagerAdapter.java`)**: Quản lý 3 Fragment: `HomeFragment`, `UploadFragment`, và `ProfileFragment`.
- **Đồng bộ hóa Hai chiều**:
    - Vuốt trang trên `ViewPager2` sẽ tự động cập nhật mục tương ứng trên `BottomNavigationView`.
    - Nhấn vào các mục trên `BottomNavigationView` sẽ điều khiển `ViewPager2` chuyển đến trang chính xác.

### 4. Tài nguyên & Cấu hình (Resources)
- **Màu sắc (`colors.xml`)**: Định nghĩa bảng màu chủ đạo (Primary, Secondary, Background, Link...).
- **Chuỗi ký tự (`strings.xml`)**: Quản lý tập trung toàn bộ văn bản hiển thị trong ứng dụng.
- **Hình ảnh (`drawables`)**: Tạo các tệp XML cho nền bo góc, viền và biểu tượng vector.
- **Manifest**: Đăng ký các Activity mới và thiết lập `LoginActivity` làm màn hình khởi chạy mặc định.

## 📁 Các tệp tin đã tạo/chỉnh sửa:
- `app/src/main/res/layout/activity_login.xml`
- `app/src/main/java/com/ptithcm/documentshub/activity/LoginActivity.java`
- `app/src/main/res/layout/activity_home.xml`
- `app/src/main/java/com/ptithcm/documentshub/activity/HomeActivity.java`
- `app/src/main/res/layout/fragment_home.xml`
- `app/src/main/java/com/ptithcm/documentshub/fragment/HomeFragment.java`
- `app/src/main/res/layout/fragment_upload.xml`
- `app/src/main/java/com/ptithcm/documentshub/fragment/UploadFragment.java`
- `app/src/main/res/layout/fragment_profile.xml`
- `app/src/main/java/com/ptithcm/documentshub/fragment/ProfileFragment.java`
- `app/src/main/java/com/ptithcm/documentshub/adapter/HomeViewPagerAdapter.java`
- `app/src/main/res/menu/bottom_nav_menu.xml`
- `app/src/main/res/values/colors.xml`
- `app/src/main/res/values/strings.xml`
- `app/src/main/AndroidManifest.xml`
