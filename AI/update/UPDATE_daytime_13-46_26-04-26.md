# Nhật ký cập nhật - DocumentHub (Android)

**Thời gian tạo:** 13:46 Ngày 26-04-2026

## 🛠 Các công việc đã thực hiện

### 1. Triển khai chức năng Like/Unlike tài liệu
- **API Integration**:
    * Cập nhật `DocumentService.java`: Thêm các endpoint `PUT /api/v1/documents/{id}/like` và `DELETE /api/v1/documents/{id}/like`.
    * Cập nhật `DocumentRepository.java`: Bổ sung các phương thức `likeDocument` và `unlikeDocument` để gọi API.
- **Model Update**:
    * Cập nhật `Document.java`: Thêm các phương thức setter cho `liked` (boolean) và `like_count` (int) để cho phép cập nhật trạng thái local sau khi gọi API thành công.
- **ViewModel Logic**:
    * Cập nhật `DocumentViewModel.java`: Triển khai phương thức `toggleLike()`. Phương thức này tự động nhận biết trạng thái hiện tại để gọi API like hoặc unlike tương ứng, sau đó cập nhật lại `LiveData<Document>` để UI tự động làm mới.
- **UI Enhancement (DocumentActivity)**:
    * **Logic hiển thị**: Thay đổi logic của nút Like. Khi tài liệu đã được like (`liked = true`), nút sẽ bị làm mờ (`alpha = 0.5f`) nhưng **KHÔNG** bị vô hiệu hóa (`enabled = true`). Việc này cho phép người dùng nhấn lại lần nữa để thực hiện hành động unlike.
    * **Sự kiện Click**: Thiết lập `setOnClickListener` cho `btnLike` để gọi hàm `toggleLike()` từ ViewModel.

### 2. Tối ưu hóa Code
- Dọn dẹp và chuẩn hóa các import trong `DocumentService.java` để code gọn gàng và chuyên nghiệp hơn.
