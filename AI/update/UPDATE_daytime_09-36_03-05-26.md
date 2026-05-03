# CẬP NHẬT TIẾN ĐỘ DỰ ÁN DOCUMENTHUB
**Ngày thực hiện:** 03/05/2026
**Thời gian:** 09:36

## 1. Nghiên cứu và Phân tích
- Đã đọc và nắm bắt toàn bộ cấu trúc mã nguồn dự án (MVVM, Retrofit, Java).
- Phân tích luồng xác thực (Authentication) và xử lý Token (Access Token, Refresh Token).
- Phân tích cấu trúc API dựa trên tài liệu `AI/config/api.md` và `openapi.json`.

## 2. Sửa lỗi hệ thống (Bug Fixes)
- **Khắc phục lỗi Crash trong TokenAuthenticator:**
    - Phát hiện xung đột giữa `@FormUrlEncoded` và `@Body` trong `AuthService.refresh()`.
    - Đã gỡ bỏ `@FormUrlEncoded` để đúng với định dạng JSON của API.
    - Thêm logic kiểm tra trong `TokenAuthenticator` để tránh vòng lặp vô tận (infinite loop) và crash khi yêu cầu login hoặc refresh bị lỗi 401.

## 3. Triển khai tính năng mới
- **Tính năng Báo cáo tài liệu (Report Document):**
    - **Models:** Tạo `ReportReason` và `ReportRequest`.
    - **Network:** Tạo `ReportService` định nghĩa các API lấy lý do báo cáo và gửi báo cáo.
    - **Repository:** Tạo `ReportRepository` để quản lý logic gọi API Report.
    - **ViewModel:** Cập nhật `DocumentViewModel` để quản lý danh sách lý do và trạng thái gửi báo cáo.
    - **UI/Layout:** Tạo `dialog_report_document.xml` theo thiết kế `report_popup.png`.
    - **Activity:** Cập nhật `DocumentActivity` để hiển thị Dialog báo cáo và thực hiện gửi dữ liệu lên server.

## 4. Các thay đổi về Tài nguyên
- Cập nhật `strings.xml` với các nhãn tiếng Việt cho tính năng Report.
- Đảm bảo tuân thủ quy tắc không hardcode text/color và sử dụng đúng ID convention.

---
*Ghi chú: Mọi thay đổi đều được kiểm tra để đảm bảo tính ổn định và không ảnh hưởng đến các tính năng cũ.*
