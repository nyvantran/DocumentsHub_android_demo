# Nhật ký cập nhật - DocumentHub (Android)

**Thời gian tạo:** 10:22 Ngày 22-04-2026

## 🛠 Các công việc đã thực hiện

### 1. Màn hình Tải lên tài liệu (Upload Fragment)
- **Thiết kế Giao diện**: Xây dựng `fragment_upload.xml` dựa trên thiết kế `upload_document_screen.png`. 
    - Sử dụng `ScrollView` để đảm bảo hiển thị tốt trên các kích thước màn hình khác nhau.
    - Tạo các ô nhập liệu bo góc, thêm các biểu tượng bắt buộc (`*`), và Dropdown cho Danh mục/Quyền riêng tư.
- **Xử lý Logic (`UploadFragment.java`)**:
    - **Chọn file thực tế**: Cập nhật nút "Select file" sử dụng `ActivityResultLauncher` và `Intent.ACTION_GET_CONTENT` để chọn file từ bộ nhớ máy thật thay vì dữ liệu cứng. Lấy tên file gốc hiển thị lên UI thông qua `ContentResolver`.
    - **Hệ thống thẻ Tag động**: Lắng nghe sự kiện `TextWatcher`, tự động tạo tag dạng "chip" có nút xóa khi người dùng gõ phím cách (Space) hoặc dấu phẩy (,).
    - Cấu hình dữ liệu mẫu cho các Spinner (Visibility, Category).

### 2. Màn hình Hồ sơ cá nhân (Profile Fragment)
- **Thiết kế Giao diện**: Xây dựng `fragment_profile.xml` bám sát thiết kế `profile_screen.png`.
    - Thiết kế header với nút Đăng xuất (Logout) màu đỏ nổi bật.
    - Cấu trúc hồ sơ cá nhân với ảnh đại diện, thông tin người dùng, đoạn giới thiệu ngắn (Bio), và thanh chuyển đổi các tab ngang (Overview, Documents, Collections, Liked, Trash).
- **Chỉnh sửa thành phần UI**: Chuyển đổi các nút Logout và Edit Profile từ `TextView` sang thành phần `Button` chuẩn để hỗ trợ phản hồi nhấp (Ripple effect). Đồng bộ màu icon `ic_book` thành màu tím để hiện rõ trên nền trắng.
- **Tính năng Đăng xuất (`ProfileFragment.java`)**: 
    - Bấm nút Đăng xuất sẽ xóa toàn bộ phiên activity stack (sử dụng cờ `FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK`) và điều hướng người dùng quay trở lại `LoginActivity`.

### 3. Popup Chỉnh sửa Hồ sơ (Edit Profile Dialog)
- **Thiết kế Giao diện**: Tạo `dialog_edit_profile.xml` với giao diện hiển thị từ dưới lên (Bottom Sheet) theo thiết kế `edit_profile_screen.png`.
    - Dùng `NestedScrollView` bao bọc nội dung để tránh bàn phím điện thoại che lấp các trường thông tin.
    - Thiết kế nút đổi ảnh đại diện tích hợp icon Camera, các trường nhập Liệu Full Name, Gender (Spinner) và Bio.
    - Thiết lập `app:backgroundTint="@null"` cho các nút bấm (Cancel, Save changes) để giữ nguyên định dạng viền/nền gốc trong file drawable.
- **Xử lý Logic (`EditProfileDialogFragment.java`)**:
    - Khởi tạo Popup kế thừa `BottomSheetDialogFragment`.
    - **Cơ chế ẩn/hiện nút "Save changes" thông minh**: Sử dụng `TextWatcher` và `OnItemSelectedListener` để phát hiện nếu có thay đổi trong Form, nút "Save changes" mới được hiển thị từ trạng thái `gone`.
    - Tích hợp sự kiện bật Popup khi nhấn vào nút "Edit Profile" bên `ProfileFragment.java`.

### 4. Màn hình Chính (Home Fragment) - Cải tiến Danh sách
- **Kiến trúc Giao diện (`fragment_home.xml`)**: Chuyển đổi toàn bộ các phần hiển thị thủ công tĩnh (Static) sang dùng `ListView` động để dễ dàng nạp dữ liệu thật sau này.
- **Quản lý Cuộn lồng nhau (Nested Scrolling)**: Tạo ra Custom View `NonScrollListView.java` giúp các danh sách con (Trending Documents) có thể được nhúng thẳng vào danh sách mẹ (Categories) mà không bị lỗi xung đột thanh cuộn hay lỗi hiển thị sai kích thước.
- **Mô hình Dữ liệu (Models)**: Tạo mới Model `Category.java` và `Document.java`.
- **Tạo Adapters**:
    - `DocumentAdapter.java`: Quản lý hiển thị cho từng Document hiển thị trong danh mục.
    - `CategorySectionAdapter.java`: Quản lý hiển thị cho từng Danh mục lớn, chứa tên Danh mục và danh sách các Documents lồng bên trong.
- **Tạo Dữ liệu mẫu (Dummy Data)**: Áp dụng vào `HomeFragment.java` với 3 danh mục hiển thị mẫu (Computer, Programming, Science) cùng nhiều tài liệu khác nhau để kiểm thử UI.

*(Lưu ý: Màn hình Chi tiết tài liệu `DocumentActivity` đang trong quá trình thực hiện và sẽ được liệt kê trong bản cập nhật kế tiếp).*
