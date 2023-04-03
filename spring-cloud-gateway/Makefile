run-redis:
	docker run --name api-gateway-redis -d -p 6379:6379 redis:7.0.0
run-mysql:
	docker run --name order-service-mysql \
	-e MYSQL_ROOT_HOST=% \
	-e MYSQL_ROOT_PASSWORD=1234 \
	-e MYSQL_USER=order_server_user \
	-e MYSQL_PASSWORD=1234 \
	-e MYSQL_DATABASE=order_service \
	-d -p 3306:3306 mysql:8.0.30