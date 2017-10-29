------------------------------------------------------------------------------------------
Project		: GEOCODER
Chức năng	: Đồng bộ dữ liệu
File		: README.md
------------------------------------------------------------------------------------------

Mô tả bài toán:
	1. Step1: Lấy tất cả các record có trường address là NULL từ bảng dv_geocoder
	2. Step2: Dùng Geocode API, lấy địa chỉ của toạ độ (latitude, longitude) tương ứng của từng record ở Step1
	3. Step3: Update trường address của từng record bằng địa chỉ lấy được ở Step2
	4. Step4: Ngủ đông 1 khoảng thời gian trước khi quay lại Step1


Yêu cầu cài đặt:
	* Java 1.7 trở lên
	* Cài đặt đường dẫn PATH cho java

Các bước chạy chương trình:
	1. Vào folder "exec files".
	2. Thay đổi các thiết đặt trong file application.properties tương ứng với môi trường chạy thực tế.
	   Chi tiết cách thiết đặt xem trong file application.properties.
	3. Mở command tools của hệ thống
	4. Chạy lệnh như bên dưới:
		java -jar geocoder-<version>.jar --console true --loglevel debug

	   Trong đó: 
	   +) --console là tham số tuỳ chọn để bật tính năng in log lên console.
		  Mặc định: false (không bật)
		  Nếu không bật, chương trình chỉ in ra trong file log.
	   +) --loglevel là tham số tuỳ chọn để setting log level.
	      Mặc định: INFO (log ở level info)
		  Các giá trị có thể setting là: OFF < FATAL < ERROR < WARN < INFO < DEBUG < TRACE < ALL
		  Chi tiết, xem thêm tại: https://logging.apache.org/log4j/2.x/manual/customloglevels.html