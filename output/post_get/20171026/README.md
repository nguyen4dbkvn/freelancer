------------------------------------------------------------------------------------------
Project		: POST-GET
Chức năng	: Đồng bộ dữ liệu
File		: README.md
------------------------------------------------------------------------------------------

Mô tả:
	* Bài toán POST: 
		1. Step1: Lấy dữ liệu từ database nguồn
		2. Step2: Dùng Geocode API, lấy địa chỉ của toạ độ (latitude, longitude), lưu vào trường address
		3. Step3: Chuyển dữ liệu đến database đích là database có tên được lưu trong trường db_name.
			      Dữ liệu chuyển có trường db_name để trống
		4. Step4: Ngủ đông 1 khoảng thời gian trước khi quay lại Step1

	* Bài toán GET: 
		1. Step1: Lấy dữ liệu từ database nguồn
		2. Step3: Chuyển dữ liệu đến database đích là database được setting trong file application.properties
			      Dữ liệu chuyển có trường db_name để trống
		3. Step4: Ngủ đông 1 khoảng thời gian trước khi quay lại Step1

Yêu cầu cài đặt:
	* Java 1.7 trở lên
	* Cài đặt đường dẫn PATH cho java

Các bước chạy chương trình:
	1. Vào folder "exec files".
	2. Thay đổi các thiết đặt trong file application.properties tương ứng với môi trường chạy thực tế.
	   Chi tiết cài đặt xem trong file application.properties.
	3. Mở command tools
	4. Chạy lệnh như bên dưới:
		java -jar post-get.jar --console true --loglevel debug

	   Trong đó: 
	   +) --console là tham số tuỳ chọn để bật tính năng in log lên console.
		  Mặc định: false (không bật)
		  Nếu không bật, chương trình chỉ in ra trong file log.
	   +) --loglevel là tham số tuỳ chọn để setting log level.
	      Mặc định: INFO (log ở level info)
		  Các giá trị có thể setting là: OFF < FATAL < ERROR < WARN < INFO < DEBUG < TRACE < ALL
		  Chi tiết, xem thêm tại: https://logging.apache.org/log4j/2.x/manual/customloglevels.html