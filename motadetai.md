# THÔNG TIN ĐỀ TÀI DỰ ÁN

## 3. Mô tả đề tài

**Các yêu cầu chính của đề tài:**

*   **Phân hệ Xác thực và Bảo mật (Authentication & Security)**
    Tại màn hình đăng ký, ứng dụng tích hợp quy trình đăng ký bảo mật qua 3 bước giúp xác thực người dùng hiệu quả: (1) Bước 1: Người dùng nhập email cá nhân để nhận mã OTP xác thực được gửi từ Server; (2) Bước 2: Nhập và xác minh mã OTP để nhận mã đăng ký (`registration_code`); (3) Bước 3: Hoàn tất đăng ký bằng cách thiết lập tên đăng nhập (username) và mật khẩu kèm mã đăng ký đã được cấp. Đối với việc đăng nhập truyền thống, người dùng sử dụng tên đăng nhập hoặc email cùng mật khẩu để xác thực. JWT Token (bao gồm Access Token và Refresh Token) sau khi nhận được từ Server sẽ được lưu trữ an toàn trong SharedPreferences của Android thông qua `TokenManager` nhằm duy trì phiên đăng nhập của người dùng. Các API yêu cầu xác thực sẽ tự động đính kèm Access Token trong request header dưới dạng `Bearer {token}`. Hệ thống cũng tích hợp cơ chế tự động làm mới token (Refresh Token) khi Access Token hết hạn và đăng xuất an toàn (thu hồi token trên Server). Ngoài ra, chức năng Quên mật khẩu và Đặt lại mật khẩu hỗ trợ người dùng khôi phục tài khoản bằng mã OTP xác minh qua Email.

*   **Phân hệ Trang chủ và Tìm kiếm tài liệu (Dashboard & Search)**
    Giao diện chính (Dashboard) được thiết kế hiện đại, cung cấp cho người dùng thông tin tài liệu thịnh hành (Trending Documents) được sắp xếp theo lượt xem giảm dần ngay khi khởi chạy ứng dụng. Phân hệ tích hợp thanh tìm kiếm thông minh cho phép tìm kiếm nhanh theo từ khóa (tiêu đề, tác giả, mô tả tài liệu), lọc tài liệu theo Danh mục (Categories) hoặc theo danh sách các thẻ phân loại (Tags). Danh sách tài liệu được hiển thị trực quan thông qua `RecyclerView`, tích hợp hiển thị ảnh bìa tài liệu, tên tác giả, danh mục và các chỉ số tương tác (lượt xem, lượt thích).

*   **Phân hệ Đăng tải và Quản lý tài liệu (Document Upload & Management)**
    Ứng dụng cho phép người dùng đóng góp tài liệu học tập bằng cách đăng tải các file từ thiết bị lên Server với định dạng hỗ trợ phong phú bao gồm `.pdf`, `.doc`, `.docx`, `.ppt`, `.pptx`. Người dùng điền thông tin mô tả chi tiết cho tài liệu (tiêu đề, mô tả, chọn danh mục và gắn các thẻ tag phân loại). Người dùng có thể chỉnh sửa thông tin tài liệu đã tải lên, cập nhật danh sách tag hoặc thực hiện xóa mềm (Soft Delete) tài liệu. Các tài liệu bị xóa mềm sẽ được lưu vào thùng rác và tự động xóa vĩnh viễn sau 30 ngày, người dùng có thể khôi phục (Restore) tài liệu bất cứ lúc nào trong thời hạn này.

*   **Phân hệ Xem chi tiết và Tương tác tài liệu (Document Viewer & Interaction)**
    Giao diện chi tiết tài liệu hiển thị đầy đủ các thông tin: số lượt xem, lượt thích, lượt tải, ngày đăng, danh sách tags, danh mục và thông tin tác giả. Tích hợp chức năng xem trước (Preview) tài liệu ngay trên ứng dụng thông qua định dạng PDF đã được chuyển đổi. Người dùng có thể tương tác thích (Like) hoặc bỏ thích tài liệu trực tiếp, hệ thống tự động cập nhật số lượt thích và đồng bộ trạng thái. Chức năng tải xuống (Download) cho phép người dùng lựa chọn tải về file gốc hoặc file PDF đã được tối ưu từ Server.

*   **Phân hệ Quản lý Bộ sưu tập (Collections)**
    Người dùng có thể tổ chức, phân loại tài liệu yêu thích bằng cách tự tạo các bộ sưu tập cá nhân (ví dụ: "Tài liệu ôn thi", "Lập trình Android"). Các chức năng bao gồm tạo mới bộ sưu tập, chỉnh sửa thông tin hoặc xóa bộ sưu tập. Người dùng có thể thêm tài liệu vào một hoặc nhiều bộ sưu tập thông qua giao diện hộp thoại Checkbox tiện lợi, hoặc xóa tài liệu khỏi bộ sưu tập một cách dễ dàng.

*   **Phân hệ Báo cáo Vi phạm (Reports)**
    Tính năng này cho phép người dùng báo cáo các tài liệu vi phạm bản quyền, thông tin sai lệch hoặc chứa nội dung không lành mạnh. Người dùng có thể lựa chọn lý do vi phạm đã được định nghĩa sẵn bởi Server và cung cấp thêm phần mô tả chi tiết của báo cáo để gửi lên hệ thống, giúp ban quản trị kiểm duyệt và xử lý kịp thời.

**Quản lý hồ sơ:** Người dùng có thể cập nhật thông tin cá nhân (Họ tên, giới tính, mô tả cá nhân), thay đổi ảnh đại diện (Avatar). Ảnh đại diện được tải lên server và được tải về hiển thị mượt mà dưới dạng hình tròn thông qua thư viện Glide. Màn hình cá nhân (Profile) tổ chức hiển thị theo các tab dữ liệu thực tế từ API: Danh sách tài liệu đã đăng tải, danh sách bộ sưu tập cá nhân, và danh sách các tài liệu đã thích (Liked Documents) có tích hợp chức năng bỏ thích trực tiếp kèm theo hộp thoại xác nhận.

**Tài khoản người dùng:**
*   Đăng ký tài khoản, đăng nhập, cập nhật thông tin cá nhân và quản lý dữ liệu tài liệu cá nhân.

**Giao diện người dùng:**
*   Giao diện thân thiện, trực quan, phân chia rõ ràng các tab chức năng.
*   Hỗ trợ chế độ giao diện tối (Light/Dark mode) linh hoạt dựa trên cấu hình hệ thống.
*   Thiết kế giao diện thích ứng (Responsive Layout) hỗ trợ đa kích thước màn hình thiết bị Android (sử dụng các giá trị linh hoạt, tránh fix cứng kích thước).
