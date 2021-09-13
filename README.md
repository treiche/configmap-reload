### Preconditions
Readings: 
[Spring Cloud Kubernetes Docs](https://docs.spring.io/spring-cloud-kubernetes/docs/current/reference/html/)   
Download [minikube](https://github.com/kubernetes/minikube/releases).   
Install [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl) 

### Local Start-Up
#### Start Minikube
```
minikube start --driver=docker
```

#### Setup Minikube Acccount
```
kubectl create rolebinding default-sa-view --clusterrole=view --serviceaccount=default:default --namespace=default
```  

#### Setup Docker and point shell to minikube's docker-daemon
```
minikube docker-env
eval $(minikube -p minikube docker-env)
```

#### Build project
```
mvn clean install
```

#### Build and push Docker image
```
docker build -t config-demo .
```

#### Create or update Configmaps
Changing the mounted ConfigMap article-protection-config.yaml needs some time to be 
applied due to a defined TTL. Please check the 
[docs](https://kubernetes.io/docs/concepts/configuration/configmap/#mounted-configmaps-are-updated-automatically) for more information. 
```
kubectl apply -f src/k8s/config-map.yaml
kubectl apply -f src/k8s/article-protection-config.yaml
```

#### Deploy the Pod
```
kubectl apply -f src/k8s/pod.yaml
```

#### Forward port to access via localhost
```
kubectl port-forward pod/config-demo 8080:8080
```

#### Check Pod Logs
```
kubectl logs -f pod/config-demo
```

#### Shutdown Minikube
```
minikube stop
```