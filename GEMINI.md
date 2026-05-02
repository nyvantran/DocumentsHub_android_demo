# 📱 PROJECT CONTEXT: DOCUMENTHUB (ANDROID)

## 🎯 Tổng quan dự án

Ứng dụng Android "DocumentHub" - Hệ thống lưu trữ và chia sẻ tài liệu đáp ứng nhu cầu học tập và
chia sẻ kiến thưc.
Tất cả các bản mockups/thiết kế giao diện (UI) hiện được lưu trữ dưới dạng hình ảnh trong thư mục
`\UI`

## 🛠 Tech Stack & Kiến trúc

- **Ngôn ngữ:** Java.
- **Giao diện (UI):** XML Layouts.
- **Mạng & API:** sử dụng Retrofit để xử lý API
- **Kiến trúc:** MVVM để tách biệt rõ ràng giữa UI, logic nghiệp
  vụ và dữ liệu.

## 🖼 Quy trình xử lý UI (BẮT BUỘC)

Khi tôi yêu cầu tạo một màn hình mới và nhắc đến một file trong thư mục `UI/` (ví dụ:
`UI/login_screen.png`), bạn phải tuân thủ các bước sau:

1. **Phân tích Layout:** Nhận diện các thành phần chính (Toolbar, RecyclerView,
   FloatingActionButton, v.v.) và cấu trúc phân tầng (ConstraintLayout, LinearLayout).
2. **Naming Convention:** Các ID trong file XML phải tuân thủ quy tắc:
   `[loại_view]_[tên_chức_năng]` (ví dụ: `btn_login`, `tv_document_title`, `rv_document_list`).
3. **Resource Extraction:** KHÔNG hardcode text hoặc mã màu (#Hex) trực tiếp vào file layout. Hãy
   tạo định nghĩa cho `strings.xml` và `colors.xml`.
4. **Responsive:** Code layout phải hỗ trợ đa kích thước màn hình (sử dụng `match_parent`, `0dp`
   trong ConstraintLayout thay vì fix cứng kích thước như `300dp`).

## 🏗 Tổ chức thư mục (Package Structure)

Code phải được đặt vào đúng package logic:

- `com.ptithcm.documentshub.activity`: Chứa các Activities
- `com.ptithcm.documentshub.fragment`: Chứa các Fragments
- `com.ptithcm.documentshub.adapter`: Chứa các chứa các Adapter cho Listview hoặc Girdview.
- `com.ptithcm.documentshub.model`: Chứa các đối tượng định nghĩa từ API
- `com.ptithcm.documentshub.viewmodel`: Chứa các ViewModel để xử lý logic nghiệp vụ và dữ liệu cho
  UI.
- `com.ptithcm.documentshub.repository`: Logic gọi API theo kiến trúc DAO
- `com.ptithcm.documentshub.utils`: Các hàm helpers (format ngày tháng, check permission).
- `com.ptithcm.documentshub.network`: Chứa các cấu hình kết nối API (có thể sử dụng Retrofit).

## 🧠 Nguyên tắc Coding (AI Instructions)

- **Đơn giản & Rõ ràng:** Code phải dễ đọc, tránh sử dụng các kỹ thuật phức tạp không cần thiết.
- **Tái sử dụng:** Tạo các component có thể tái sử dụng (ví dụ: custom view, helper functions) để
  tránh lặp lại code.
- **Tuân thủ quy tắc Android:** Sử dụng đúng lifecycle methods, xử lý permission, và đảm bảo hiệu
  suất tốt (tránh chạy công việc nặng trên main thread).
- **Kiểm tra kỹ lưỡng:** Trước khi hoàn thành, hãy kiểm tra lại code để đảm bảo không có lỗi cú
  pháp, logic, hoặc vấn đề về hiệu suất.
- **Tài liệu hóa:** Mỗi class và method quan trọng phải có comment giải thích mục đích và cách sử
  dụng.
- **Tuân thủ quy tắc đặt tên:** Biến, method, và class phải được đặt tên rõ ràng, có ý nghĩa và tuân
  thủ quy tắc camelCase cho biến và method, PascalCase cho class.
- **Componet UI**: Ưu tiên sử dụng các component UI chuẩn của Android (TextView, Button, ListView,
  GirdView). tạo
  custom component khi cần thiết. Không sử dụng thư viện bên ngoài để tạo UI.
- **Không hardcode:** Tránh hardcode giá trị (text, màu sắc, kích thước) trong code, thay vào đó sử
  dụng resource files (`strings.xml`, `colors.xml`, `dimens.xml`).
- **Xử lý lỗi:** Luôn xử lý ngoại lệ và lỗi một cách hợp lý, đảm bảo ứng dụng không bị crash và cung
  cấp phản hồi hữu ích cho người dùng khi có lỗi xảy ra.
- **Bảo mật:** Không lưu trữ thông tin nhạy cảm (như API keys, mật khẩu) trực tiếp trong code. Sử
  dụng các phương pháp bảo mật phù hợp để bảo vệ dữ liệu người dùng.

## Nguyên tắc khi làm việc

- sau một bước thực hiện phải giải thích những gì chuẩn bị làm
- chỉ thực hiện yêu cầu được đưa ra, không được tự ý thêm bất kỳ tính năng nào khác
- mỗi lần trả lời phải nói rõ những gì chuẩn bị làm trước khi thực hiện
- trả lời mặc định bằng tiếng việt
- nếu có yêu cầu về code thì phải trả lời bằng code java và xml, không trả lời bằng pseudo code hoặc
  bất kỳ ngôn ngữ nào khác
- nếu có yêu cầu về giao diện thì phải trả lời bằng XML, không trả lời bằng pseudo code hoặc bất kỳ
  ngôn ngữ nào khác
- các button muốn có hiệu ứng background thì thêm dòng `app:backgroundTint="@null"` vào file XML để
  giữ nguyên định dạng gốc của drawable
- đọc lại file UPDATE_daytime_{hh-mm_dd-mm-yy}.md mới nhất. Để hiểu rõ hơn về những gì đã làm được
- giải thích về các api ở file AI/config/api.md và config api ở AI/config/openapi.json
- dự án đã xin quyền truy cập internet
- dự án đã có sẵn một số thư viện như Retrofit, Gson, v.v. để hỗ trợ việc gọi API và xử lý dữ liệu
- khi mà tạo nút thì phải dùng button chuẩn của android, không được dùng imageview hoặc textview để
  tạo nút


