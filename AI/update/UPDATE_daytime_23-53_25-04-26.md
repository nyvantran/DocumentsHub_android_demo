# Nhật ký cập nhật - DocumentHub (Android)

**Thời gian tạo:** 23:53 Ngày 25-04-2026

## 🛠 Các công việc đã thực hiện

### 1. Thay thế hiển thị PDF (PdfRenderer)
- **Cấu trúc UI**: Thay thế `WebView` trong `activity_document.xml` bằng `RecyclerView` để hiển thị danh sách các trang PDF dưới dạng hình ảnh (`Bitmap`). Việc này giúp cuộn mượt mà hơn và tránh phụ thuộc vào thư viện bên ngoài hoặc trình duyệt.
- **Adapter**: Tạo `PdfPageAdapter` quản lý việc render từng trang PDF bằng `android.graphics.pdf.PdfRenderer`.
- **Quản lý Cache**: 
    * Triển khai `PdfCacheManager` để tải file PDF về thư mục cache và đổi tên theo `documentId`.
    * **Cơ chế LRU**: Giới hạn tối đa 5 file trong cache. Khi vượt quá, ứng dụng tự động xóa file cũ nhất (dựa trên thời gian truy cập) để giải phóng bộ nhớ.

### 2. Cập nhật luồng Tải về (Official Download)
- **API Integration**: Bổ sung endpoint `/api/v1/documents/{id}/download` để lấy link tải tài liệu chính thức.
- **DownloadManager**: Thay vì mở trình duyệt bằng Intent, ứng dụng hiện sử dụng `DownloadManager` của hệ thống để tải file trong nền. 
    * Hiển thị tiến trình tải trên thanh thông báo.
    * Người dùng không còn bị chuyển trang khi nhấn nút Download.
- **Tránh lặp sự kiện**: Thêm logic `clearDownloadUrl()` trong ViewModel để xóa link ngay sau khi bắt đầu tải, ngăn chặn việc tự động tải lại khi xoay màn hình hoặc khởi tạo lại Activity.

### 3. Quản lý Token & Bảo mật
- **Auto Refresh Token**: Cập nhật `TokenManager` để tự động làm mới `accessToken` thông qua API `/auth/refresh` khi token sắp hết hạn (ngưỡng 30 phút).
- **Service & Model**: Thêm `RefreshRequest` và cập nhật `AuthService` để hỗ trợ luồng refresh token cho Mobile client.

### 4. Cấu hình Hệ thống & Khắc phục lỗi
- **Permissions**: Thêm quyền `WRITE_EXTERNAL_STORAGE` và `READ_EXTERNAL_STORAGE` vào `AndroidManifest.xml` để hỗ trợ tính năng lưu tài liệu tải về.
- **Network Troubleshooting**: 
    * Phân tích và xử lý lỗi `java.net.ProtocolException: unexpected end of stream` (nguyên nhân do Connection Pooling khi kết nối với server local).
    * Thử nghiệm cấu hình `Connection: close` và tắt `retryOnConnectionFailure` trong `ApiClient` (đã khôi phục về trạng thái ổn định theo yêu cầu).

## 📝 Ghi chú
- Các logic kiểm tra JWT qua API `/auth/whoami/` đã được chuyển đổi về dạng tính toán thời gian (`expiry_time`) để đảm bảo tính ổn định và giảm tải cho mạng.
- Cấu hình `ApiClient` và `TokenManager` đã được đưa về trạng thái nguyên bản/ổn định nhất sau các bước thử nghiệm.
