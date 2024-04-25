mvn clean package

#eval $(minikube -p minikube docker-env)
eval $(minikube docker-env)

docker build -t restaurantproducer:1.0 .