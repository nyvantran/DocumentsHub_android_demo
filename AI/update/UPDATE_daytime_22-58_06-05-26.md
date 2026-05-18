# CẬP NHẬT TIẾN ĐỘ DỰ ÁN DOCUMENTHUB
**Ngày thực hiện:** 06/05/2026
**Thời gian:** 22:58

## 1. Tính năng Quên mật khẩu (Forgot Password)
- **Model:** Tạo `PasswordForgotRequest` và `PasswordResetRequest`.
- **API:** Tích hợp `/api/v1/auth/forgot_password` và `/api/v1/auth/reset_password`.
- **UI/UX:** Thiết kế `ForgotPasswordActivity` với luồng 2 bước (Nhập email -> Nhập OTP & Mật khẩu mới) đồng nhất với giao diện ứng dụng.
- **Logic:** Triển khai `ForgotPasswordViewModel` để quản lý trạng thái xác thực và đổi mật khẩu.

## 2. Hợp nhất luồng Đăng ký (Unified Sign Up)
- **Tối ưu cấu trúc:** Hợp nhất 3 Activity cũ (`SignUpActivity`, `SignUpOTPActivity`, `SignUpUserPasswordActivity`) thành một `SignUpActivity` duy nhất.
- **Model:** Tạo bộ Model đăng ký hoàn chỉnh: `RegisterRequest`, `RegisterVerifyRequest`, `RegisterVerifyResponse`, `RegisterCompleteRequest`.
- **UI/UX:** Thiết kế layout bước (Step-based) với thanh chỉ báo trạng thái (Step Indicator) và logic nút Back thông minh.
- **API:** Tích hợp toàn bộ quy trình đăng ký 3 giai đoạn của server.

## 3. Quản lý Hồ sơ cá nhân (Profile Management)
- **Hiển thị dữ liệu:** Cập nhật `ProfileFragment` để hiển thị thông tin thực tế từ API `/api/v1/users/me/profile`.
- **Xử lý dữ liệu:** Tự động chuyển đổi các trường dữ liệu `null` thành `'n/a'`.
- **Đồng bộ hóa:** Chuyển đổi sang **Activity-scoped ViewModel** để đảm bảo dữ liệu luôn đồng bộ giữa Fragment và Dialog chỉnh sửa.
- **Chỉnh sửa Profile:** Hoàn thiện `EditProfileDialogFragment` cho phép cập nhật Họ tên, Giới tính, Tiểu sử với tính năng kiểm tra thay đổi dữ liệu trước khi lưu.

## 4. Tính năng Cập nhật Ảnh đại diện (Avatar Update)
- **API:** Triển khai endpoint `/api/v1/users/me/avatar` (Multipart/PUT).
- **Tiện ích:** Tạo lớp `FileUtils` hỗ trợ xử lý URI và tệp tin ảnh trên Android.
- **Xử lý lỗi:** Khắc phục lỗi `IllegalStateException` do định dạng trả về của API bằng cách chuyển sang nhận phản hồi dạng String và tự động tái đồng bộ Profile.
- **Thư viện:** Tích hợp **Glide** để tải và hiển thị ảnh đại diện mượt mà.

## 5. Sửa lỗi và Cải thiện hệ thống
- **Fix:** Sửa lỗi hộp thoại Edit Profile tự động đóng do lưu giữ trạng thái thành công từ phiên trước.
- **Fix:** Sửa lỗi shadowing biến `tvBio` gây treo dữ liệu cũ trong tab Overview.
- **Dọn dẹp:** Xóa bỏ các tệp tin Activity và Layout dư thừa sau khi hợp nhất code.

---
*Ghi chú: Toàn bộ luồng xác thực và thông tin cá nhân hiện đã hoạt động ổn định và đồng nhất với kiến trúc MVVM.*
